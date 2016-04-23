package com.sawyerharris.gravitygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Gravity Game
 * 
 * @author Sawyer Harris
 * 
 */
public class GravityGame extends Game {
	/** Map of themes */
	private static Map<String, Theme> themes = new HashMap<String, Theme>();
	/** Map of levels */
	private static Map<String, Level> levels = new HashMap<String, Level>();
	/** Ordered list of levels */
	private static List<String> levelNames = new ArrayList<String>();
	/** Map of Textures */
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	/** Tracks user progress and settings */
	private Preferences statusPrefs;
	/** State of the game */
	private GameState state;
	
	private static int screenWidth;
	private static int screenHeight;
	private static float aspectRatio;
	
	/**
	 * Called when game started
	 */
	@Override
	public void create() {
		// TODO improve this
		if (Gdx.app.getType() == ApplicationType.Android) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		} else {
			screenWidth = 720;
			screenHeight = 1280;
		}
		aspectRatio = screenWidth / (float) screenHeight;
		
		AssetLoader.loadThemes(themes);
		themes = Collections.unmodifiableMap(themes);
		AssetLoader.loadLevels(levels, levelNames);
		levels = Collections.unmodifiableMap(levels);
		levelNames = Collections.unmodifiableList(levelNames);
		AssetLoader.loadThemeTextures(textures, themes);
		AssetLoader.loadOtherTextures(textures);
		textures = Collections.unmodifiableMap(textures);
		
		setScreen(new GameScreen());
	}
	
	/**
	 * Renders game via screen
	 */
	@Override
	public void render () {
		super.render();
	}
	
	public static float getAspectRatio() {
		return aspectRatio;
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	/**
	 * Returns unmodifiable map of Themes
	 * @return themes
	 */
	public static Map<String, Theme> getThemes() {
		return themes;
	}
	
	/**
	 * Returns unmodifiable map of Levels
	 * @return levels
	 */
	public static Map<String, Level> getLevels() {
		return levels;
	}
	
	/**
	 * Returns unmodifiable list of level names
	 * @return levelNames
	 */
	public static List<String> getLevelNames() {
		return levelNames;
	}
	
	public static Map<String, Texture> getTextures() {
		return textures;
	}
	
	/**
	 * Returns game state
	 * @return state
	 */
	public GameState getState() {
		return state;
	}
	
	/**
	 * Specifies current state of game
	 * 
	 * @author Sawyer Harris
	 * 
	 */
	public enum GameState {
		MENU, LEVEL_EDITOR, VIEWING, VIEW_MOVING, AIMING, FIRING, LEVEL_FAILURE, LEVEL_SUCCESS
	}
}