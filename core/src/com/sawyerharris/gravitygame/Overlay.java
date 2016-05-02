package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

public class Overlay {
	private static final float BAR_WIDTH = 15;
	private static final float BAR_HEIGHT = 150;
	
	private static final float BANNER_OFFSET = 100;
	private static final float NAME_OFFSET = 20;
	private static final float MSG_OFFSET = 20;
	private static final float LOWER_OFFSET = 20;
	private static final float MSG_MARGIN = 50;
	private static final int NAME_FONT_SIZE = 22;
	private static final int MSG_FONT_SIZE = 19;
	
	private int screenWidth;
	private int screenHeight;
	private int boost;
	
	private Level level;
	private Ship ship;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private FreeTypeFontGenerator gen;
	private FreeTypeFontParameter nameParam;
	private FreeTypeFontParameter msgParam;
	private BitmapFont nameFont;
	private BitmapFont msgFont;
	
	public Overlay(Level level, Ship ship) {
		this.level = level;
		this.ship = ship;
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		gen = new FreeTypeFontGenerator(GravityGame.fontPath);
		nameParam = new FreeTypeFontParameter();
		nameParam.size = NAME_FONT_SIZE;
		msgParam = new FreeTypeFontParameter();
		msgParam.size = MSG_FONT_SIZE;
		nameFont = gen.generateFont(nameParam);
		msgFont = gen.generateFont(msgParam);
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
	}
	
	public void drawLevelHeader(float alpha) {
		
		GlyphLayout name = new GlyphLayout(nameFont, level.getName(), Color.WHITE, screenWidth, Align.center, true);
		GlyphLayout msg = new GlyphLayout(msgFont, level.getMessage(), Color.WHITE, screenWidth - 2 * MSG_MARGIN, Align.center, true);
		
		float bannerHeight = NAME_OFFSET + name.height + LOWER_OFFSET;;
		
		if (!level.getMessage().equals("")) {
			bannerHeight += MSG_OFFSET + msg.height;
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		sr.setColor(new Color(0.3f, 0f, 1f, alpha));
		sr.rect(0, screenHeight - BANNER_OFFSET - bannerHeight, screenWidth, bannerHeight);
		sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		sb.begin();
		nameFont.draw(sb, name, 0, screenHeight - BANNER_OFFSET - NAME_OFFSET);
		msgFont.draw(sb, msg, MSG_MARGIN, screenHeight - BANNER_OFFSET - NAME_OFFSET - name.height - MSG_OFFSET);
		sb.end();
	}
	
	public void drawBoostBar() {
		boost = ship.getBoost();
		
		sr.begin(ShapeType.Line);
		sr.setColor(Color.BLACK);
		sr.rect(screenWidth - 2 * BAR_WIDTH, screenHeight - 1.2f * BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
		sr.rect(screenWidth - 2 * BAR_WIDTH - 1, screenHeight - 1.2f * BAR_HEIGHT - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2);
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.SCARLET);
		sr.rect(screenWidth - 2 * BAR_WIDTH, screenHeight - 1.2f * BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT * boost / Ship.INITIAL_BOOST);
		sr.end();
	}
	
	public void drawBackButton() {
		
	}
}
