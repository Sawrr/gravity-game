package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sawyerharris.gravitygame.manager.AssetManager;
import com.sawyerharris.gravitygame.manager.LevelManager;
import com.sawyerharris.gravitygame.manager.ThemeManager;
import com.sawyerharris.gravitygame.screen.LevelEditScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen.Context;
import com.sawyerharris.gravitygame.screen.MenuScreen;

/**
 * Singleton Game class. An application listener that delegates the render
 * method to its current game screen.
 * 
 * @author Sawyer Harris
 *
 */
public class GravityGame extends Game {
	/** Singleton instance */
	private static GravityGame game;

	/** Sprite batch that all screens will use */
	private SpriteBatch batch;
	/** Shape renderer that all screens will use */
	private ShapeRenderer renderer;

	/** Game screens */
	private MenuScreen menuScreen;
	private LevelEditScreen levelEditScreen;
	private LevelPlayScreen levelPlayScreen;

	/** Managers */
	private AssetManager assets;
	private LevelManager levels;
	private ThemeManager themes;

	/** Player status */
	private PlayerStatus status;

	@Override
	public void create() {
		// Make sure create() is only called once
		if (game != null) {
			throw new IllegalStateException("GravityGame.create() can only be called once.");
		}
		// Set up singleton
		game = this;

		status = new PlayerStatus();

		assets = new AssetManager();
		levels = new LevelManager();
		themes = new ThemeManager();

		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);

		levelEditScreen = new LevelEditScreen(batch, renderer);
		levelPlayScreen = new LevelPlayScreen(batch, renderer);
		menuScreen = new MenuScreen(batch, renderer);

		// Catch android back button in input listeners
		Gdx.input.setCatchBackKey(true);

		setScreenToMenu();
	}

	/**
	 * Gets the singleton game instance.
	 * 
	 * @return game instance
	 */
	public static GravityGame getInstance() {
		// Make sure create() has been called
		if (game == null) {
			throw new IllegalStateException("Cannot call GravityGame.getInstance() before create() has been called.");
		}
		return game;
	}

	/**
	 * Gets the menu screen instance.
	 * 
	 * @return menuScreen
	 */
	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	/**
	 * Gets the level editor screen instance.
	 * 
	 * @return levelEditScreen
	 */
	public LevelEditScreen getLevelEditScreen() {
		return levelEditScreen;
	}

	/**
	 * Gets the level gameplay screen instance.
	 * 
	 * @return levelPlayScreen
	 */
	public LevelPlayScreen getLevelPlayScreen() {
		return levelPlayScreen;
	}

	/**
	 * Sets the current screen to the menu. Refreshes menu contents and
	 * redirects input to the MenuScreen instance.
	 */
	public void setScreenToMenu() {
		menuScreen.createMenuItems();
		setScreen(menuScreen);
		Gdx.input.setInputProcessor(menuScreen.getMux());
	}

	/**
	 * Sets the current screen to the game play screen. Loads the given level in
	 * the given game play context and redirects input to the MenuScreen
	 * instance.
	 * 
	 * @param level
	 *            level to be played
	 * @param context
	 *            context of how level will be played. i.e. normally, in test
	 *            mode, or as a custom level.
	 */
	public void setScreenToPlay(Level level, Context context) {
		if (level == null) {
			setScreenToMenu();
			return;
		}
		levelPlayScreen.loadLevel(level);
		levelPlayScreen.setContext(context);
		setScreen(levelPlayScreen);
		Gdx.input.setInputProcessor(levelPlayScreen.getMux());

		// Set current level index and onTutorialLevel boolean
		int index;
		switch (level.getType()) {
		case CUSTOM:
			// Custom levels do not set current level index
			break;
		case OFFICIAL:
			index = levels.getOfficialLevels().indexOf(level);
			levels.setCurrentLevelIndex(index);
			levels.setOnTutorialLevels(false);
			break;
		case TUTORIAL:
			index = levels.getTutorialLevels().indexOf(level);
			levels.setCurrentLevelIndex(index);
			levels.setOnTutorialLevels(true);
			break;
		}
	}

	/**
	 * Sets screen to level editor screen. Loads the custom level at the given
	 * index of the custom levels list. Sets the custom level's name to the
	 * given parameter to allow for level renaming. Redirects input to the level
	 * editor screen instance.
	 * 
	 * @param name
	 *            new name for custom level
	 * @param index
	 *            index of custom level in list
	 */
	public void setScreenToEdit(String name, int index) {
		Level level = null;
		try {
			level = levels.getCustomLevels().get(index);
		} catch (IndexOutOfBoundsException e) {
			//
		}
		levelEditScreen.loadLevel(level);
		levelEditScreen.setCustomLevelIndex(index);
		levelEditScreen.setCustomLevelName(name);
		setScreen(levelEditScreen);
		Gdx.input.setInputProcessor(levelEditScreen.getMux());
	}

	/**
	 * Sets the screen to the level editor without loading a new level. i.e.
	 * returns to the level editor after testing a level on the level play
	 * screen. Redirects input to the level editor screen instance.
	 */
	public void setScreenBackToEdit() {
		setScreen(levelEditScreen);
		Gdx.input.setInputProcessor(levelEditScreen.getMux());
	}

	/**
	 * Returns the asset manager so game assets may be accessed.
	 * 
	 * @return assets
	 */
	public AssetManager getAssets() {
		return assets;
	}

	/**
	 * Returns the theme manager so themes may be accessed.
	 * 
	 * @return themes
	 */
	public ThemeManager getThemes() {
		return themes;
	}

	/**
	 * Returns the level manager so levels may be accessed, saved, and uploaded.
	 * 
	 * @return levels
	 */
	public LevelManager getLevels() {
		return levels;
	}

	/**
	 * Returns the player status.
	 * 
	 * @return status
	 */
	public PlayerStatus getPlayerStatus() {
		return status;
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.dispose();
	}

	@Override
	public void render() {
		// Clears screen before rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Game.render() delegates to the current screen's render()
		super.render();
	}
}
