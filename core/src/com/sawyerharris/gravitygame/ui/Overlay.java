package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.manager.LevelManager;
import com.sawyerharris.gravitygame.screen.GameStage;

public class Overlay {

	private GravityGame game = GravityGame.getInstance();
	
	private GameStage stage;
	private float screenWidth;
	private float screenHeight;

	public Overlay(Batch batch, ShapeRenderer renderer) {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		stage = new GameStage(new ScreenViewport(), batch, renderer);

		createVictoryScreen("1-2: Bump n Grind", 27, 2);
	}

	public void createVictoryScreen(String name, int numAttempts, int unlockStyle) {
		float x = screenWidth / 5;
		float y = screenHeight / 8;
		float width = screenWidth * 3 / 5;
		float height = screenHeight * 3 / 4;

		float itemWidth = width - 2 * BorderedItem.BORDER_WIDTH;
		float itemHeight = (height - 2 * BorderedItem.BORDER_WIDTH) / 5;

		BorderedItem border = new BorderedItem(x, y, width, height, new Color(1f, 1f, 1f, 0.6f), Touchable.disabled);
		TextItem levelComplete = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - itemHeight, itemWidth, itemHeight, new Color(1f, 0f, 1f, 0.7f),
				Touchable.disabled, "Level Complete!", 24);
		TextItem levelName = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 2 * itemHeight, itemWidth, itemHeight,
				new Color(1f, 0f, 1f, 0.7f), Touchable.disabled, name, 24);
		TextItem levelAttempts = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 3 * itemHeight, itemWidth, itemHeight,
				new Color(1f, 0f, 1f, 0.7f), Touchable.disabled, "Number of attempts: " + numAttempts, 24);
		TextItem unlockedLabel = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
				new Color(1f, 0f, 1f, 0.7f), Touchable.disabled, "Unlocked: ", 24);
		BorderedItem unlockedItem = null;
		if (unlockStyle == -1) {
			unlockedItem = new TextItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
					y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
					new Color(1f, 0f, 1f, 0.7f), Touchable.disabled, "None", 24);
		} else {
			unlockedItem = new TextureItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
					y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
					new Color(1f, 0f, 1f, 0.7f), Touchable.disabled,
					GravityGame.getInstance().getAssets().getShipAnimation(unlockStyle, "default").getKeyFrame(0));
		}
		TextItem backToMenu = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 5 * itemHeight, itemWidth / 2, itemHeight,
				new Color(1f, 0f, 1f, 0.7f), Touchable.enabled, "Back to Menu", 24) {
			@Override
			public void click() {
				GravityGame.getInstance().setScreenToMenu();
			}
		};
		TextItem nextLevel = new TextItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
				y + height - BorderedItem.BORDER_WIDTH - 5 * itemHeight, itemWidth / 2, itemHeight,
				new Color(1f, 0f, 1f, 0.7f), Touchable.enabled, "Next Level", 24) {
			@Override
			public void click() {
				LevelManager levels = game.getLevels();
				game.setScreenToPlay(levels.getOfficialLevels().get(levels.getCurrentLevelIndex()));
			}
		};
		stage.addActor(border);
		stage.addActor(levelComplete);
		stage.addActor(levelName);
		stage.addActor(levelAttempts);
		stage.addActor(unlockedLabel);
		stage.addActor(unlockedItem);
		stage.addActor(backToMenu);
		stage.addActor(nextLevel);
	}

	public void draw() {
		stage.draw();
	}

	public GameStage getStage() {
		return stage;
	}
}
