package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.screen.GameStage;
import com.sawyerharris.gravitygame.screen.LevelEditScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen.Context;

public class Overlay {

	private GravityGame game = GravityGame.getInstance();

	private GameStage stage;
	private float screenWidth;
	private float screenHeight;

	/** Victory panel actors */
	private BorderedItem border;
	private TextItem levelComplete;
	private TextItem levelName;
	private TextItem levelAttempts;
	private TextItem unlockedLabel;
	private TextItem unlockedTextItem;
	private TextureItem unlockedTextureItem;
	private TextItem backToMenu;
	private TextItem nextLevel;

	private Color victoryPanelColor;

	/** Editor buttons */
	private TextItem testButton;
	private TextItem saveButton;
	private TextItem uploadButton;

	private int editFontSize = 32;

	private Color editButtonColor = new Color(0.3f, 0.3f, 0.3f, 0.6f);

	public Overlay(Batch batch, ShapeRenderer renderer) {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		stage = new GameStage(new ScreenViewport(), batch, renderer);
		
		createVictoryPanel();
		hideVictoryPanel();
	}

	public void createVictoryPanel() {
		float x = screenWidth / 6;
		float y = screenHeight / 8;
		float width = screenWidth * 2 / 3;
		float height = screenHeight * 3 / 4;

		float itemWidth = width - 2 * BorderedItem.BORDER_WIDTH;
		float itemHeight = (height - 2 * BorderedItem.BORDER_WIDTH) / 5;
		
		victoryPanelColor = new Color(0.6f, 0.85f, 0.95f, 0.9f);
		
		border = new BorderedItem(x, y, width, height, Color.CLEAR, Touchable.disabled);
		levelComplete = new TextItem(x + BorderedItem.BORDER_WIDTH, y + height - BorderedItem.BORDER_WIDTH - itemHeight,
				itemWidth, itemHeight, victoryPanelColor, Touchable.disabled, "Level Complete!", 24);
		levelName = new TextItem(x + BorderedItem.BORDER_WIDTH, y + height - BorderedItem.BORDER_WIDTH - 2 * itemHeight,
				itemWidth, itemHeight, victoryPanelColor, Touchable.disabled, "", 24);
		levelAttempts = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 3 * itemHeight, itemWidth, itemHeight,
				victoryPanelColor, Touchable.disabled, "Number of attempts: ", 24);
		unlockedLabel = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
				victoryPanelColor, Touchable.disabled, "Unlocked: ", 24);
		unlockedTextItem = new TextItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
				y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
				victoryPanelColor, Touchable.disabled, "None", 24);
		unlockedTextureItem = new TextureItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
				y + height - BorderedItem.BORDER_WIDTH - 4 * itemHeight, itemWidth / 2, itemHeight,
				victoryPanelColor, Touchable.disabled,
				GravityGame.getInstance().getAssets().getShipAnimation(0, "default").getKeyFrame(0));
		backToMenu = new TextItem(x + BorderedItem.BORDER_WIDTH,
				y + height - BorderedItem.BORDER_WIDTH - 5 * itemHeight, itemWidth / 2, itemHeight,
				victoryPanelColor, Touchable.enabled, "Back to Menu", 24) {
			@Override
			public void click() {
				game.setScreenToMenu();
			}
		};
		nextLevel = new TextItem(x + BorderedItem.BORDER_WIDTH + itemWidth / 2,
				y + height - BorderedItem.BORDER_WIDTH - 5 * itemHeight, itemWidth / 2, itemHeight,
				victoryPanelColor, Touchable.enabled, "Next Level", 24) {
			@Override
			public void click() {
				Level next = game.getLevels().nextLevel();
				game.setScreenToPlay(next, Context.PLAYING);
			}
		};

		stage.addActor(border);
		stage.addActor(levelComplete);
		stage.addActor(levelName);
		stage.addActor(levelAttempts);
		stage.addActor(unlockedLabel);
		stage.addActor(unlockedTextItem);
		stage.addActor(unlockedTextureItem);
		stage.addActor(backToMenu);
		stage.addActor(nextLevel);
	}
	
	public void createEditButtons() {
		float buttonHeight = screenHeight / 6;
		testButton = new TextItem(0, 0, screenWidth / 3, buttonHeight, editButtonColor, Touchable.enabled, "Test", editFontSize) {
			@Override
			public void click() {
				Screen screen = game.getScreen();
				if (screen instanceof LevelEditScreen) {
					LevelEditScreen les = (LevelEditScreen) screen;
					les.testLevel();
				}
			}
		};
		
		saveButton = new TextItem(screenWidth / 3, 0, screenWidth / 3, buttonHeight, editButtonColor , Touchable.enabled, "Save", editFontSize) {
			@Override
			public void click() {
				Screen screen = game.getScreen();
				if (screen instanceof LevelEditScreen) {
					LevelEditScreen les = (LevelEditScreen) screen;
					les.saveLevel();
				}
			}
		};
		
		uploadButton = new TextItem(2 * screenWidth / 3, 0, screenWidth / 3, buttonHeight, editButtonColor, Touchable.enabled, "Upload", editFontSize) {
			@Override
			public void click() {
				Screen screen = game.getScreen();
				if (screen instanceof LevelEditScreen) {
					LevelEditScreen les = (LevelEditScreen) screen;
					les.uploadLevel();
				}
			}
		};
		
		stage.addActor(testButton);
		stage.addActor(saveButton);
		stage.addActor(uploadButton);
	}

	public void showVictoryPanel(String name, int numAttempts, int unlockStyle, Color color) {
		if (unlockStyle == -1) {
			unlockedTextItem.setVisible(true);
			unlockedTextItem.setColor(color);
			unlockedTextureItem.setVisible(false);
		} else {
			unlockedTextureItem.setTexture(
					GravityGame.getInstance().getAssets().getShipAnimation(unlockStyle, "default").getKeyFrame(0));

			unlockedTextItem.setVisible(false);
			unlockedTextureItem.setColor(color);
			unlockedTextureItem.setVisible(true);
		}

		levelAttempts.setText("Number of attempts: " + numAttempts);

		levelName.setText(name);

		border.setColor(color);
		border.setVisible(true);
		levelComplete.setColor(color);
		levelComplete.setVisible(true);
		levelName.setColor(color);
		levelName.setVisible(true);
		levelAttempts.setColor(color);
		levelAttempts.setVisible(true);
		unlockedLabel.setColor(color);
		unlockedLabel.setVisible(true);
		backToMenu.setColor(color);
		backToMenu.setVisible(true);
		nextLevel.setColor(color);
		nextLevel.setVisible(true);
	}

	public void hideVictoryPanel() {
		border.setVisible(false);
		levelComplete.setVisible(false);
		levelName.setVisible(false);
		levelAttempts.setVisible(false);
		unlockedLabel.setVisible(false);
		unlockedTextItem.setVisible(false);
		unlockedTextureItem.setVisible(false);
		backToMenu.setVisible(false);
		nextLevel.setVisible(false);
	}
	
	public void draw() {
		stage.draw();
	}

	public GameStage getStage() {
		return stage;
	}
}
