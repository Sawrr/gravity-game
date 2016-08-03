package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.ui.TextItem;

public class LevelPlayScreen extends LevelScreen {

	private GameplayState state;

	private Vector2 cameraAimingPosition;

	public LevelPlayScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer);
		aim();
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
		getCamera().translate(new Vector2(deltaX, deltaY));
	}

	@Override
	public void zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyDown(int keycode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrolled(int amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Resets the level to its initial state.
	 */
	public void resetLevel() {

	}

	/**
	 * Returns the state of the game.
	 * 
	 * @return state
	 */
	public GameplayState getState() {
		return state;
	}

	/**
	 * Called when the ship is being aimed.
	 */
	public void aim() {
		getShip().reset();
		getShip().setTouchable(Touchable.enabled);
	}

	/**
	 * Called when the ship is fired.
	 */
	public void fire() {

	}

	/**
	 * Called when a level is failed.
	 */
	public void failure() {

	}

	/**
	 * Called when a level is beaten.
	 */
	public void victory() {

	}

	/**
	 * State of game while playing.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum GameplayState {
		AIMING, FIRING, FAILURE, VICTORY
	}

}
