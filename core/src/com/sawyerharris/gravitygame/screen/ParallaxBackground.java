package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Theme;

/**
 * Displays a space background with a parallax effect based on the camera's
 * position relative to the center of the world.
 * 
 * @author Sawyer Harris
 *
 */
public class ParallaxBackground {
	/** Camera's position required for parallax */
	private GameCamera camera;
	/** Batch to draw to */
	private Batch batch;
	/** Texture of background */
	private Texture texture;
	/** Texture region to be drawn */
	private TextureRegion region;
	/** Theme specifies background image and source view width */
	private Theme theme;

	/**
	 * Constructor sets the camera and batch.
	 * 
	 * @param camera
	 * @param batch
	 */
	public ParallaxBackground(GameCamera camera, Batch batch) {
		if (camera == null || batch == null) {
			throw new NullPointerException();
		}
		this.camera = camera;
		this.batch = batch;
	}

	/**
	 * Sets the background texture to that specified by the given theme and sets
	 * the region to be the source view width of the theme.
	 * 
	 * @param theme
	 */
	public void setTheme(Theme theme) {
		// texture = GravityGame.getInstance().
		// region =
	}

	/**
	 * Draws the background with the parallax effect.
	 */
	public void draw() {
		// Make sure theme has been set before drawing.
		if (theme == null) {
			throw new IllegalStateException("Theme must be set before drawing background.");
		}
	}
}
