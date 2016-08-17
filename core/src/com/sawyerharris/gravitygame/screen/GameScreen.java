package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * An abstract screen used by the game. Uses a stage, camera, viewport,
 * background, input mux, and gesture detector to manage the game world. Does
 * not need to be disposed as its stage only references assets held in the
 * AssetManager. The gesture detector should always be the last input processor
 * in the mux.
 * 
 * @author Sawyer Harris
 *
 */
public abstract class GameScreen extends ScreenAdapter {
	/** Viewport dimensions */
	private static final int VIEWPORT_WIDTH = 1000;
	private static final int VIEWPORT_HEIGHT = 1500;
	
	/** Batch to draw on */
	private final Batch batch;
	/** Shape renderer for drawing UI */
	private final ShapeRenderer renderer;
	/** Stage holds actors to be drawn */
	private final GameStage stage;
	/** Camera manages the location and zoom of the world to be drawn */
	private final GameCamera camera;
	/** Viewport sets the camera's width, height based on the physical screen */
	private final ExtendViewport viewport;
	/** Background provides parallax effect on space background */
	private final ParallaxBackground background;
	/** Mux holds hierarchy of input processors */
	private final InputMultiplexer mux;
	/** Detector passes along gesture input on the screen */
	private final ScreenGestureDetector detector;

	/**
	 * Constructs the camera, viewport, background, stage, detector, and mux.
	 */
	public GameScreen(Batch bat, ShapeRenderer rend, int worldWidth, int worldHeight) {
		batch = bat;
		renderer = rend;
		camera = new GameCamera(worldWidth, worldHeight);
		viewport = new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
		background = new ParallaxBackground(camera, batch);
		stage = new GameStage(viewport, batch, renderer);
		
		detector = new ScreenGestureDetector(100, 1.0f, 1.11f, 0.15f, new ScreenGestureAdapter());

		mux = new InputMultiplexer();
		mux.addProcessor(stage);
		mux.addProcessor(detector);
		
		camera.setPosition(0,0);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.autoMove();
		camera.update();
		
		// Draw background
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.draw();
		
		// Draw stage actors
		// projection matrix is set by stage.draw()
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		//viewport.update(width, height);
	}

	
	public GameStage getStage() {
		return stage;
	}

	public GameCamera getCamera() {
		return camera;
	}

	public ParallaxBackground getBackground() {
		return background;
	}

	public InputMultiplexer getMux() {
		return mux;
	}

	/**
	 * Called when the screen is panned.
	 */
	public abstract void pan(float x, float y, float deltaX, float deltaY);

	/**
	 * Called when the screen is zoomed.
	 */
	public abstract void zoom(float initialDistance, float distance);

	/**
	 * Called when the screen is tapped.
	 */
	public abstract void tap(float x, float y, int count, int button);

	/**
	 * Called when a key is pressed.
	 */
	public abstract void keyDown(int keycode);

	/**
	 * Called when the mouse wheel is scrolled.
	 */
	public abstract void scrolled(int amount);
	
	/**
	 * Called when the screen is touched by mouse or finger.
	 */
	public abstract void touchDown(int screenX, int screenY, int pointer, int button);

	/**
	 * Called when the touch on the screen is lifted.
	 */
	public abstract void touchUp(int screenX, int screenY, int pointer, int button);
	
	/**
	 * Detects gestures on the screen and hands them to the GameScreen instance.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScreenGestureDetector extends GestureDetector {
				
		public ScreenGestureDetector(int i, float f, float g, float h, GestureListener listener) {
			super(i, f, g, h, listener);
		}

		@Override
		public boolean keyDown(int keycode) {
			GameScreen.this.keyDown(keycode);
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			GameScreen.this.scrolled(amount);
			return false;
		}
		
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			super.touchDown(screenX, screenY, pointer, button);
			GameScreen.this.touchDown(screenX, screenY, pointer, button);
			return false;
		}
		
		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			super.touchUp(screenX, screenY, pointer, button);
			GameScreen.this.touchUp(screenX, screenY, pointer, button);
			return false;
		}
	}
	
	/**
	 * Listens for gestures and sends them to GameScreen instance.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScreenGestureAdapter extends GestureAdapter {
		public boolean firstPan = true;
		
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (!firstPan) {
				GameScreen.this.pan(x, y, -deltaX, deltaY);
			}
			firstPan = false;
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			GameScreen.this.zoom(initialDistance, distance);
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			firstPan = true;
			GameScreen.this.tap(x, y, count, button);
			return false;
		}
	}
}
