package com.sawyerharris.gravitygame;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.sawyerharris.gravitygame.GravityGame.GameState;

public class InputHandler {
	private static final float TOUCH_TO_ZOOM = 0.0001f;
	private GameCamera camera;
	private Screen screen;
	private GestureDetector gestureDetector;
	private GestureProcessor gestureProcessor;
	private ButtonProcessor buttonProcessor;
	
	public InputHandler(Screen screen, GameCamera camera) {
		this.camera = camera;
		this.screen = screen;		
		this.gestureProcessor = new GestureProcessor();
		this.gestureDetector = new GestureDetector(100, 1.0f, 1.1f, 0.15f, gestureProcessor);
		this.buttonProcessor = new ButtonProcessor();
	}
	

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}
	
	public class GestureProcessor extends GestureAdapter {
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (screen instanceof GameScreen) {
				GameScreen gs = (GameScreen) screen;
				gs.getCamera().setAutoMoving(false);
			}
			camera.pan(-deltaX, deltaY);
			return true;
		}
		
		@Override
		public boolean zoom(float initialDistance, float distance) {
			if (screen instanceof GameScreen) {
				GameScreen gs = (GameScreen) screen;
				gs.getCamera().setAutoMoving(false);
			}
			float zoomDistance = TOUCH_TO_ZOOM * (initialDistance - distance);
			camera.zoom(zoomDistance);
			return true;
		}
		
		
		@Override
		public boolean tap(float x, float y, int count, int button) {
			if (screen instanceof GameScreen && GravityGame.getState() == GameState.AIMING) {
				GameScreen gs = (GameScreen) screen;
				gs.toggleShipWorldAutoMove();
			}	
			return true;
		}
	}

	public ButtonProcessor getButtonProcessor() {
		return buttonProcessor;
	}
	
	public class ButtonProcessor extends InputAdapter{
		@Override
		public boolean keyDown(int keycode) {
			if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
				System.out.println("Back button");
			}
			return true;
		}
		
		@Override
		public boolean scrolled(int amount) {
			if (screen instanceof GameScreen) {
				GameScreen gs = (GameScreen) screen;
				gs.getCamera().setAutoMoving(false);
				camera.zoom(GameCamera.SCROLL_TO_ZOOM * amount);
			}
			
			// TODO if screen instanceof LevelEditorScreen && state = leveleditor
			/*
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 coords3d = viewport.getCamera().unproject(new Vector3(x, y, 0));
			Vector2 coords = new Vector2(coords3d.x, coords3d.y);
			for (Planet planet : planets) {
				Vector2 loc = new Vector2(planet.getPosition());
				if (loc.sub(coords).len2() <= Math.pow(planet.getRadius(),2)) {
					System.out.println("on planet");
				}
			}
			*/
			return true;
		}
	}
}