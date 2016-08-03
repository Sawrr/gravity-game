package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Gdx;
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
	private static final float PARA_SCALAR = 0.1f;
	/** Batch for drawing background */
	private Batch batch;
	/** Camera's position required for parallax */
	private GameCamera camera;
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
		//setTheme(DEFAULT_THEME);
	}

	/**
	 * Sets the background texture to that specified by the given theme and sets
	 * the region to be the source view width of the theme.
	 * 
	 * @param theme
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
		texture = GravityGame.getInstance().getAssets().getBackground(theme.getBackground());
		int width = texture.getWidth();
		int height = texture.getHeight();
		float aspectRatio = camera.viewportWidth / camera.viewportHeight;
		int srcViewWidth = theme.getSrcViewWidth();
		int srcViewHeight = (int) (srcViewWidth / aspectRatio);
		int x = (width - srcViewWidth) / 2;
		int y = (height - srcViewHeight) / 2;
		region = new TextureRegion(texture, x, y, srcViewWidth, srcViewHeight);
	}

	/**
	 * Draws the background with the parallax effect.
	 */
	public void draw() {
		// Make sure theme has been set before drawing.
		if (theme == null) {
			throw new IllegalStateException("Theme must be set before drawing background.");
		}
		
		int width = texture.getWidth();
		int height = texture.getHeight();
		
		int srcWidth = region.getRegionWidth();
		int srcHeight = region.getRegionHeight();
		
		float aspectRatio = camera.viewportWidth / camera.viewportHeight;
		
		float norX = camera.position.x / camera.viewportWidth - 0.5f;
		float norY = 1 - camera.position.y / camera.viewportHeight - 0.5f;
		
		int srcX = (int) ((width  - srcWidth) / 2 + norX * PARA_SCALAR * width);
		int srcY = (int) ((height - srcHeight) / 2 + norY * PARA_SCALAR * height / aspectRatio);
		
		region.setRegion(srcX, srcY, srcWidth, srcHeight);
		
		batch.begin();
		batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
}
