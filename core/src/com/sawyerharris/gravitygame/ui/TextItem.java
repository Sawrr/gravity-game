package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.sawyerharris.gravitygame.game.GravityGame;

public class TextItem extends BorderedItem {
	
	private static final Color TEXT_COLOR = Color.WHITE;

	private static final int TEXT_MARGIN = 10;
	
	private float textWidth;
	private GlyphLayout textGL;
	private BitmapFont font;
	
	public TextItem(float x, float y, float width, float height, Color color, Touchable touchable, String text, int fontSize) {
		super(x, y, width, height, color, touchable);
		textWidth = width;
		
		font = GravityGame.getInstance().getAssets().getFont(fontSize);
		setText(text);
	}
	
	public void setText(String text) {
		textGL = new GlyphLayout(font, text, TEXT_COLOR, textWidth - 2 * BORDER_WIDTH - 2 * TEXT_MARGIN, Align.center, true);
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		font.draw(batch, textGL, getX() + BORDER_WIDTH + TEXT_MARGIN, getY() + (getHeight() + textGL.height) / 2);
	}
}
