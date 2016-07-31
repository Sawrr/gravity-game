package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * An OrthographicCamera that provides methods for smoothly auto moving to a
 * target position and zoom level.
 * 
 * @author Sawyer Harris
 *
 */
public class GameCamera extends OrthographicCamera {
	/** Maximum zoom level */
	private static final float MIN_ZOOM = 0.2f;

	/** Target camera values */
	private Vector2 moveTarget;
	private float zoomTarget;
	/** Auto movement method */
	private MoveMode mode;
	/** Speed parameter for auto move */
	private float speed;
	
	private boolean autoMoving;
	private boolean autoZooming;

	/**
	 * Default constructor.
	 */
	public GameCamera() {
		setMoveTarget(new Vector2(0,0));
		zoomTarget = 1f;
		zoom = 0.8f;
		mode = MoveMode.LOGARITHMIC;
		speed = 1/10f;
	}

	/**
	 * Translate the camera by the vector direction.
	 * 
	 * @param direction
	 */
	public void translate(Vector2 direction) {
		super.translate(direction);
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
	 * Sets the camera's zoom level.
	 * 
	 * @param zoom
	 */
	public void setZoom(float zoom) {
		this.zoom = zoom;
		clamp();
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
	 * Sets the camera's position.
	 * 
	 * @param pos
	 *            position
	 */
	public void setPosition(Vector2 pos) {
		if (pos == null) {
			throw new NullPointerException();
		}
		setPosition(pos.x, pos.y);
	}
	
	/**
	 * Sets the camera's position.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		position.set(x, y, position.z);
		clamp();
	}

	/**
	 * Sets the position the camera should move to.
	 * 
	 * @param target
	 */
	public void setMoveTarget(Vector2 target) {
		if (target == null) {
			throw new NullPointerException();
		}
		moveTarget = target;
		autoMoving = true;
	}

	/**
	 * Sets the level the camera should zoom to.
	 * 
	 * @param target
	 */
	public void setZoomTarget(float target) {
		zoomTarget = target;
		autoZooming = true;
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
		if (autoMoving && position.dst(new Vector3(moveTarget, position.z)) > 1f) {
			System.out.println("translating");
			Vector2 dist = null;
			switch (mode) {
			case LINEAR:
				dist = new Vector2(moveTarget).sub(position.x, position.y).nor().scl(speed);
				break;
			case LOGARITHMIC:
				dist = new Vector2(moveTarget).sub(position.x, position.y).scl(speed);
				break;
			}
			translate(dist);
		} else {
			autoMoving = false;
		}
		if (autoZooming && Math.abs(zoom - zoomTarget) > 0.000001f) {
			System.out.println("zooming");
			float amount = 0;
			switch (mode) {
			case LINEAR:
				if (zoomTarget > zoom) {
					amount = 0.01f;
				} else {
					amount = -0.01f;
				}
				break;
			case LOGARITHMIC:
				amount = (zoomTarget - zoom) * speed;
				break;
			}
			zoom(amount);
		} else {
			autoZooming = false;
		}
	}

	/**
	 * Clamps the camera's position and zoom to stay within the boundaries
	 * allowed by the world.
	 */
	private void clamp() {
		float effectiveViewportWidth = viewportWidth * zoom;
		float effectiveViewportHeight = viewportHeight * zoom;
		position.x = MathUtils.clamp(position.x, (effectiveViewportWidth - viewportWidth)/ 2f,
				(viewportWidth - effectiveViewportWidth) / 2f);
		position.y = MathUtils.clamp(position.y, (effectiveViewportHeight - viewportHeight)/ 2f,
				(viewportHeight - effectiveViewportHeight) / 2f);
		zoom = MathUtils.clamp(zoom, MIN_ZOOM, 1f);
	}

	/**
	 * Movement types for camera auto movement.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum MoveMode {
		LINEAR, LOGARITHMIC
	}
}
