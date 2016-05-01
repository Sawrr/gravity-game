package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Overlay {
	Level level;
	Ship ship;
	GameCamera camera;
	ShapeRenderer sr;
	
	public Overlay(Level level, Ship ship, GameCamera camera) {
		this.level = level;
		this.ship = ship;
		this.camera = camera;
		
		sr = new ShapeRenderer();
	}
	
	public void drawLevelName() {
		
	}
	
	public void drawLevelMessage() {
		
	}
	
	public void drawBoostBar() {
		int boost = ship.getBoost();
		float barWidth = 20;
		float barHeight = 150;
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		
		sr.begin(ShapeType.Line);
		sr.setColor(Color.BLACK);
		sr.rect(screenWidth - 2 * barWidth, screenHeight - 1.2f * barHeight, barWidth, barHeight);
		sr.rect(screenWidth - 2 * barWidth - 1, screenHeight - 1.2f * barHeight - 1, barWidth + 2, barHeight + 2);
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.SCARLET);
		sr.rect(screenWidth - 2 * barWidth, screenHeight - 1.2f * barHeight, barWidth, barHeight * boost / Ship.INITIAL_BOOST);
		sr.end();
	}
	
	public void drawBackButton() {
		
	}
}
