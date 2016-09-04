package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Implementation of BorderedItem that has a texture instead of text.
 * 
 * @author Sawyer Harris
 *
 */
public class TextureItem extends BorderedItem {
	/** Scalar factor by which to scale texture */
	private static final float SCALE_FACTOR = 1.0f;
	/** Sprite that holds texture information */
	private Sprite sprite;

	/**
	 * Constructs a TextureItem with the given parameters.
	 * 
	 * @param x
	 *            x of bottom left corner
	 * @param y
	 *            y of bottom left corner
	 * @param width
	 *            width of TextureItem
	 * @param height
	 *            height of TextureItem
	 * @param color
	 *            color of TextureItem
	 * @param touchable
	 *            if item can be touched
	 * @param region
	 *            texture region
	 */
	public TextureItem(float x, float y, float width, float height, Color color, Touchable touchable,
			TextureRegion region) {
		super(x, y, width, height, color, touchable);
		setTexture(region);
	}

	/**
	 * Sets the texture region of the TextureItem.
	 * 
	 * @param region
	 */
	public void setTexture(TextureRegion region) {
		float centerX = getX() + getWidth() / 2;
		float centerY = getY() + getHeight() / 2;
		sprite = new Sprite(region);
		sprite.setCenter(centerX, centerY);
		float scaleX = SCALE_FACTOR * (getWidth() - 2 * BORDER_WIDTH) / sprite.getWidth();
		float scaleY = SCALE_FACTOR * (getHeight() - 2 * BORDER_WIDTH) / sprite.getHeight();
		sprite.setScale(Math.min(scaleX, scaleY));
	}

	@Override
	public void draw(Batch batch, float alpha) {
		float centerX = getX() + getWidth() / 2;
		float centerY = getY() + getHeight() / 2;
		sprite.setCenter(centerX, centerY);
		sprite.draw(batch);
	}
}
