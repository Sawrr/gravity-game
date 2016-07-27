package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class TextureItem extends BorderedItem {

	private static final float SCALE_FACTOR = 1.0f;
	private Sprite sprite;
	
	public TextureItem(float x, float y, float width, float height, Color color, Touchable touchable, TextureRegion region) {
		super(x, y, width, height, color, touchable);
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
		sprite.draw(batch, alpha);
	}
}
