package com.sawyerharris.gravitygame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Game camera
 * 
 * @author Sawyer Harris
 *
 */
public class GameCamera extends OrthographicCamera {
	/** Scalar for panning camera */
	private static final float PAN_SCALAR = 0.5f;
	/** Scalar for converting scrolling to zoom distance */
	public static final float SCROLL_TO_ZOOM = 0.05f;
	
	/** Value of zoom when centering on ship */
	public static final float SHIP_ZOOM_LEVEL = 1.0f;
	
	private static final float WORLD_ZOOM_LEVEL_SCALAR = 1.2f;
	
	/** Dimensions of level */
	private final int worldWidth;
	private final int worldHeight;
	
	public final float maxZoom;
	public final float worldZoomLevel;

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
				worldHeight/GravityGame.getScreenHeight()) / WORLD_ZOOM_LEVEL_SCALAR;
	}
	
	/**
	 * Pans the camera by given dx, dy after scaling
	 * @param dx
	 * @param dy
	 */
	public void pan(float dx, float dy) {
		//TODO fix the jitter
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
	 * @param moveTarget
	 * @param zoomTarget
	 * @return true when finished
	 */
	public boolean autoMove(Vector2 moveTarget, float zoomTarget) {
		float dx = moveTarget.x - position.x;
		float dy = moveTarget.y - position.y;
		float dzoom = zoomTarget - zoom;
		if ((Math.hypot(dx, dy) > 3f) || Math.abs(dzoom) > 3f) {
			position.x += dx/7f;
			position.y += dy/7f;
			zoom += dzoom/7f;
			clamp();
			return false;
		} else {
			return true;
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

}
