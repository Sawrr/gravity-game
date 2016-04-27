package com.sawyerharris.gravitygame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Game camera
 * 
 * @author Sawyer Harris
 *
 */
public class GameCamera extends OrthographicCamera {
	/** Scalar for panning camera */
	private static final float PAN_SCALAR = 0.75f;
	/** Scalar for converting scrolling to zoom distance */
	public static final float SCROLL_TO_ZOOM = 0.05f;
	
	/** Value of zoom when centering on ship */
	public static final float SHIP_ZOOM_LEVEL = 1.0f;
	/** Distance between ship and edge of screen that triggers camera following */
	public static final float SHIP_OFFSCREEN_BUFFER = 250;
	
	/** Dimensions of level */
	private final int worldWidth;
	private final int worldHeight;
	
	public final float maxZoom;
	public final float worldZoomLevel;
	
	/** Is the camera currently auto moving */
	private boolean isAutoMoving;

	/**
	 * Create camera for given level
	 * @param level
	 */
	public GameCamera(Level level) {
		worldWidth = level.getWidth();
		worldHeight = level.getHeight();
		maxZoom = Math.min(worldWidth/GravityGame.getScreenWidth(), 
				worldHeight/GravityGame.getScreenHeight());
		worldZoomLevel = Math.min(worldWidth/GravityGame.getScreenWidth(), 
				worldHeight/GravityGame.getScreenHeight());
		isAutoMoving = false;
	}
	
	/**
	 * Pans the camera by given dx, dy after scaling
	 * @param dx
	 * @param dy
	 */
	public void pan(float dx, float dy) {
		translate(dx * PAN_SCALAR, dy * PAN_SCALAR);
		clamp();
	}
	
	/**
	 * Zooms the camera by a given distance
	 * @param distance
	 */
	public void zoom(float distance) {
		if (zoom + distance <= maxZoom && zoom + distance >= 1f) {
			zoom += distance;
		}
		clamp();
	}
	
	/**
	 * Automatically moves the camera to the targets
	 * If zoomTarget = 0, do not alter zoom
	 * @param moveTarget
	 * @param zoomTarget
	 */
	public void autoMove(Vector2 moveTarget, float zoomTarget) {
		isAutoMoving = true;
		
		float dx, dy, dzoom, oldZoom;
		Vector3 oldPos = new Vector3(position);
		oldZoom = zoom;
		dx = moveTarget.x - position.x;
		dy = moveTarget.y - position.y;
		dzoom = zoomTarget - zoom;
		
		position.x += dx/7f;
		position.y += dy/7f;
		if (zoomTarget != 0 ) {
			zoom += dzoom/7f;
		}
		clamp();
		
		if (oldPos.sub(position).len() < 1f && Math.abs(oldZoom - zoom) < 1f) {
			isAutoMoving = false;
		}
	}
	
	/**
	 * Keeps the camera within bounds
	 */
	public void clamp() {
		float effectiveViewportWidth = viewportWidth * zoom;
		float effectiveViewportHeight = viewportHeight * zoom;
		position.x = MathUtils.clamp(position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
		position.y = MathUtils.clamp(position.y, effectiveViewportHeight / 2f, worldHeight - effectiveViewportHeight / 2f);
	}
	
	/**
	 * Returns whether camera is auto moving
	 * @return isAutoMoving
	 */
	public boolean isAutoMoving() {
		return isAutoMoving;
	}
	
	/**
	 * Sets whether camera is auto moving
	 * @param isAutoMoving
	 */
	public void setAutoMoving(boolean isAutoMoving) {
		this.isAutoMoving = isAutoMoving;
	}
}
