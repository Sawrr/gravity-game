package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	@Override
	public void create() {
		// Make sure create() is only called once
		if (game != null) {
			throw new IllegalStateException("GravityGame.create() can only be called once.");
		}
		game = this;

		batch = new SpriteBatch();

		menuScreen = new MenuScreen(batch);
		levelEditScreen = new LevelEditScreen(batch);
		levelPlayScreen = new LevelPlayScreen(batch);

		assets = new AssetManager();
		levels = new LevelManager();
		themes = new ThemeManager();

		status = new PlayerStatus();
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.dispose();
	}

	@Override
	public void render() {
		super.render();
	}
}
