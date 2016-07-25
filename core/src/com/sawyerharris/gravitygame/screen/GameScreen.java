package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
	/** Width, height of world */
	public static final float WORLD_WIDTH = 0;
	public static final float WORLD_HEIGHT = 0;

	/** Stage holds actors to be drawn */
	private Stage stage;
	/** Camera manages the location and zoom of the world to be drawn */
	private GameCamera camera;
	/** Viewport sets the camera's width, height based on the physical screen */
	private ExtendViewport viewport;
	/** Background provides parallax effect on space background */
	private ParallaxBackground background;
	/** Mux holds hierarchy of input processors */
	private InputMultiplexer mux;
	/** Detector passes along gesture input on the screen */
	private ScreenGestureDetector detector;

	/**
	 * Constructs the camera, viewport, background, stage, detector, and mux.
	 */
	public GameScreen(Batch batch) {
		camera = new GameCamera();
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		background = new ParallaxBackground(camera, batch);
		stage = new Stage(viewport, batch);

		detector = new ScreenGestureDetector();

		mux = new InputMultiplexer();
		mux.addProcessor(detector);
	}

	/**
	 * Called when the screen is panned
	 */
	public abstract void pan(float x, float y, float deltaX, float deltaY);

	/**
	 * Called when the screen is zoomed
	 */
	public abstract void zoom(float initialDistance, float distance);

	/**
	 * Called when the screen is tapped
	 */
	public abstract void tap(float x, float y, int count, int button);

	/**
	 * Called when a key is pressed
	 */
	public abstract void keyDown(int keycode);

	/**
	 * Called when the mouse wheel is scrolled
	 */
	public abstract void scrolled(int amount);

	/**
	 * Called when the screen is touched
	 */
	public abstract void touchDown(int screenX, int screenY, int pointer, int button);

	@Override
	public void render(float delta) {
		background.draw();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	/**
	 * Detects gestures on the screen and hands them to the GameScreen instance.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScreenGestureDetector extends GestureDetector {
		/** Parameters used by gesture detector */
		private static final float HALF_TAP_SQUARE_SIZE = 0;
		private static final float TAP_COUNT_INTERVAL = 0;
		private static final float LONG_PRESS_DURATION = 0;
		private static final float MAX_FLING_DELAY = 0;

		/**
		 * Constructs the detector with the default parameters.
		 */
		public ScreenGestureDetector() {
			super(HALF_TAP_SQUARE_SIZE, TAP_COUNT_INTERVAL, LONG_PRESS_DURATION, MAX_FLING_DELAY,
					detector.new ScreenGestureAdapter());
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
			GameScreen.this.touchDown(screenX, screenY, pointer, button);
			return true;
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
}
