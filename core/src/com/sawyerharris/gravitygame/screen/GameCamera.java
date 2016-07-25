package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * An OrthographicCamera that provides methods for smoothly auto moving to a
 * target position and zoom level.
 * 
 * @author Sawyer Harris
 *
 */
public class GameCamera extends OrthographicCamera {
	private static final float MAX_ZOOM = 0;

	/** Target camera values */
	private Vector2 moveTarget;
	private float zoomTarget;
	/** Auto movement method */
	private MoveMode mode;
	/** Speed parameter for auto move */
	private float speed;

	/**
	 * Default constructor.
	 */
	public GameCamera() {

	}

	/**
	 * Pans the camera by the vector direction.
	 * 
	 * @param direction
	 */
	public void pan(Vector2 direction) {
		translate(direction);
		clamp();
	}

	/**
	 * Zooms the camera by the amount.
	 * 
	 * @param amount
	 *            amount to zoom
	 */
	public void zoom(float amount) {
		zoom += amount;
		clamp();
	}

	/**
	 * Returns the camera's zoom level.
	 * 
	 * @return zoom
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * Returns a new Vector2 at the camera's position.
	 * 
	 * @return Vector2 of position
	 */
	public Vector2 getPosition() {
		return new Vector2(position.x, position.y);
	}

	/**
	 * Sets the position the camera should move to.
	 * 
	 * @param target
	 */
	public void setMoveTarget(Vector2 target) {
		moveTarget = target;
	}

	/**
	 * Sets the level the camera should zoom to.
	 * 
	 * @param target
	 */
	public void setZoomTarget(float target) {
		zoomTarget = target;
	}

	/**
	 * Sets auto movement speed of camera
	 * 
	 * @param speed
	 */
	public void setMoveSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Sets the auto move mode of the camera
	 * 
	 * @param mode
	 */
	public void setMoveMode(MoveMode mode) {
		this.mode = mode;
	}

	/**
	 * Auto moves the camera by one step. Should be called every tick until the
	 * camera is at the desired location.
	 */
	public void autoMove() {

	}

	/**
	 * Clamps the camera's position and zoom to stay within the boundaries
	 * allowed by the world.
	 */
	private void clamp() {

	}

	/**
	 * Movement types for camera auto movement.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum MoveMode {
		LINEAR, EXPONENTIAL
	}
}
