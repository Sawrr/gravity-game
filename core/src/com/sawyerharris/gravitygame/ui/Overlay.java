package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sawyerharris.gravitygame.screen.GameStage;

public class Overlay {

	private GameStage stage;
	private float screenWidth;
	private float screenHeight;

	public Overlay(Batch batch, ShapeRenderer renderer) {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		stage = new GameStage(new ScreenViewport(), batch, renderer);

		createVictoryScreen();
	}

	public void createVictoryScreen() {
		float x = screenWidth / 5;
		float y = screenHeight / 8;
		float width = screenWidth * 3 / 5;
		float height = screenHeight * 3 / 4;

		TextItem border = new TextItem(x, y, width, height, new Color(1f, 1f, 1f, 0.6f), Touchable.disabled, "", 0);
		TextItem test = new TextItem(x + 50, y + 50, 150, 150, new Color(1f, 0f, 1f, 0.7f), Touchable.enabled, "hey", 18);
		stage.addActor(border);
		stage.addActor(test);
	}

	public void draw() {
		stage.draw();
	}

	public GameStage getStage() {
		return stage;
	}
}
