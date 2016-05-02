package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Overlay {
	private static final float BAR_WIDTH = 15;
	private static final float BAR_HEIGHT = 150;
	private int screenWidth;
	private int screenHeight;
	private int boost;
	
	private Level level;
	private Ship ship;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	
	public Overlay(Level level, Ship ship) {
		this.level = level;
		this.ship = ship;
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		generator = new FreeTypeFontGenerator(GravityGame.fontPath);
		parameter = new FreeTypeFontParameter();
		font = generator.generateFont(parameter);
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
	}
	
	public void drawLevelName(float alpha) {
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		sr.setColor(new Color(0.3f, 0f, 1f, alpha));
		sr.rect(0, screenHeight - 100, screenWidth, -100);
		sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		sb.begin();
		font.draw(sb, level.getName(), screenWidth / 3, screenHeight - 150);
		sb.end();
	}
	
	public void drawLevelMessage() {
		
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
