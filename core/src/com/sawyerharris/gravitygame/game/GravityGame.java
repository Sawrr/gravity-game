package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.manager.AssetManager;
import com.sawyerharris.gravitygame.manager.LevelManager;
import com.sawyerharris.gravitygame.manager.ThemeManager;
import com.sawyerharris.gravitygame.screen.GameStage;
import com.sawyerharris.gravitygame.screen.LevelEditScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen;
import com.sawyerharris.gravitygame.screen.MenuScreen;
import com.sawyerharris.gravitygame.ui.ScrollPanel;
import com.sawyerharris.gravitygame.ui.TextItem;
import com.sawyerharris.gravitygame.ui.TextureItem;

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
	public void create() {
		// Make sure create() is only called once
		if (game != null) {
			throw new IllegalStateException("GravityGame.create() can only be called once.");
		}
		game = this;

		status = new PlayerStatus();
		
		assets = new AssetManager();
		levels = new LevelManager();
		themes = new ThemeManager();
		
		batch = new SpriteBatch();

		menuScreen = new MenuScreen(batch);
		levelEditScreen = new LevelEditScreen(batch);
		levelPlayScreen = new LevelPlayScreen(batch);

		// Test
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
}
