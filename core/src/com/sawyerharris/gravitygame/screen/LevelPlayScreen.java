package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class LevelPlayScreen extends LevelScreen {

	private GameplayState state;

	private Vector2 cameraAimingPosition;

	public LevelPlayScreen(Batch batch) {
		super(batch);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub

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
