package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.ui.TextItem;

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
	/** Width, height of world */
	public static final float WORLD_WIDTH = 1280;
	public static final float WORLD_HEIGHT = 1920;

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
	public GameScreen(Batch bat, ShapeRenderer rend) {
		batch = bat;
		renderer = rend;
		camera = new GameCamera();
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		background = new ParallaxBackground(camera, batch);
		stage = new GameStage(viewport, batch, renderer);
		
		detector = new ScreenGestureDetector(new ScreenGestureAdapter());

		mux = new InputMultiplexer();
		mux.addProcessor(stage);
		mux.addProcessor(detector);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.autoMove();
		camera.update();
		
		//background.draw();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
	 * Called when the screen is touched.
	 */
	public abstract void touchDown(int screenX, int screenY, int pointer, int button);
	
	/**
	 * Called when the touch on the screen is released.
	 */
	public abstract void touchUp(int screenX, int screenY, int pointer, int button);
	
	/**
	 * Detects gestures on the screen and hands them to the GameScreen instance.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScreenGestureDetector extends GestureDetector {
		
		public ScreenGestureDetector(GestureListener listener) {
			super(listener);
		}

		@Override
		public boolean keyDown(int keycode) {
			GameScreen.this.keyDown(keycode);
			return true;
		}

		@Override
		public boolean scrolled(int amount) {
			GameScreen.this.scrolled(amount);
			return true;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			System.out.println("touched");
			GameScreen.this.touchDown(screenX, screenY, pointer, button);
			return true;
		}
		
		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			GameScreen.this.touchUp(screenX, screenY, pointer, button);
			return true;
		}
	}
	
	/**
	 * Listens for gestures and sends them to GameScreen instance.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScreenGestureAdapter extends GestureAdapter {
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			GameScreen.this.pan(x, y, deltaX, deltaY);
			return true;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			GameScreen.this.zoom(initialDistance, distance);
			return true;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			GameScreen.this.tap(x, y, count, button);
			return true;
		}
	}
}
