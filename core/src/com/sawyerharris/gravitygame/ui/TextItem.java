package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.sawyerharris.gravitygame.game.GravityGame;

/**
 * Implementation of BorderedItem with text.
 * 
 * @author Sawyer Harris
 *
 */
public class TextItem extends BorderedItem {
	/** Color of text */
	private static final Color TEXT_COLOR = Color.WHITE;
	/** Margin of text from sides */
	private static final int TEXT_MARGIN = 10;

	/** Width of text */
	private float textWidth;

	/** Glyph layout of text */
	private GlyphLayout textGL;

	/** Font of text */
	private BitmapFont font;

	/**
	 * Constructs a TextItem with the given parameters.
	 * 
	 * @param x
	 *            x of bottom left corner
	 * @param y
	 *            y of bottom left corner
	 * @param width
	 *            width of TextItem
	 * @param height
	 *            height of TextItem
	 * @param color
	 *            color of TextItem
	 * @param touchable
	 *            if item can be touched
	 * @param text
	 *            text of item
	 * @param fontSize
	 *            font size
	 */
	public TextItem(float x, float y, float width, float height, Color color, Touchable touchable, String text,
			int fontSize) {
		super(x, y, width, height, color, touchable);
		textWidth = width;

		font = GravityGame.getInstance().getAssets().getFont(fontSize);
		setText(text);
	}

	/**
	 * Sets the text of the item.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		textGL = new GlyphLayout(font, text, TEXT_COLOR, textWidth - 2 * BORDER_WIDTH - 2 * TEXT_MARGIN, Align.center,
				true);
	}

	@Override
	public void draw(Batch batch, float alpha) {
		font.draw(batch, textGL, getX() + BORDER_WIDTH + TEXT_MARGIN, getY() + (getHeight() + textGL.height) / 2);
	}
}
