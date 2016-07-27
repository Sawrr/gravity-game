package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.manager.AssetManager;
import com.sawyerharris.gravitygame.manager.LevelManager;
import com.sawyerharris.gravitygame.manager.ThemeManager;
import com.sawyerharris.gravitygame.screen.GameStage;
import com.sawyerharris.gravitygame.screen.LevelEditScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen;
import com.sawyerharris.gravitygame.screen.MenuScreen;
import com.sawyerharris.gravitygame.ui.BorderedItem;
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

	// Begin test
	private Stage stage;
	
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

		batch = new SpriteBatch();

		//menuScreen = new MenuScreen(batch);
		//levelEditScreen = new LevelEditScreen(batch);
		//levelPlayScreen = new LevelPlayScreen(batch);

		assets = new AssetManager();
		levels = new LevelManager();
		themes = new ThemeManager();

		status = new PlayerStatus();
		
		// Begin testing
		stage = new GameStage();
		TextItem item = new TextItem(250, 50, 250, 100, new Color(1, 0, 0.5f, 0.5f), Touchable.enabled, "Da bes level kek\nAuthor: hedgehog hero\nUploaded: 7/26/2016", 18) {
			@Override
			public void click() {
				System.out.println("kekboz");
			}
		};
		stage.addActor(item);
		
		TextureItem item2 = new TextureItem(250, 300, 250, 250, new Color(0, 1, 0.5f, 0.5f), Touchable.enabled, assets.getTestRegion());
		stage.addActor(item2);
		
		Color spColor = new Color(0, 1, 0.5f, 0.5f);
		ScrollPanel sp = new ScrollPanel(25, 250, 200, 400, spColor, 100){
			@Override
			public void click(int index) {
				System.out.println(index);
			}
		};
		sp.addTextItem("test", 18);
		sp.addTextItem("hoo", 18);
		sp.addTextItem("yah", 18);
		//sp.addTextItem("boiz", 18);
		stage.addActor(sp);
		
		Gdx.input.setInputProcessor(stage);
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
		
		stage.draw();
		//super.render();
	}
}
