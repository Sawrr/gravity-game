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
	
	/** Name of the status preferences file */
	private static String statusPrefsName = "com.sawyerharris.gravitygame.status";
	/** Tracks user progress and settings */
	private static Preferences statusPrefs;
	/** Status Preferences Names */
	private static String currentLevelStr = "currentLevel";
	private static String highestLevelStr = "highestLevel";
	private static String soundOnStr = "soundOn";
	
	/** Name of the custom levels file */
	private static String customLevelPrefsName = "com.sawyerharris.gravitygame.customlevels";
	/** Contains player's custom levels */
	private static Preferences customLevelPrefs;
	
	/** Game status variables */
	private static int currentLevel;
	private static int highestLevel;
	private static boolean soundOn;
	
	/** State of the game */
	private static GameState state;
	
	/** Screen parameters */
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
		
		statusPrefs = Gdx.app.getPreferences(statusPrefsName);
		currentLevel = statusPrefs.getInteger(currentLevelStr);
		highestLevel = statusPrefs.getInteger(highestLevelStr);
		soundOn = statusPrefs.getBoolean(soundOnStr);
		
		setScreen(new GameScreen(levels.get("testLevel")));
	}
	
	/**
	 * Advances game to next level according to levelNames
	 */
	public void nextLevel() {
		currentLevel++;
		statusPrefs.putInteger(currentLevelStr, currentLevel);
		statusPrefs.flush();
		try {
			state = GameState.AIMING;
			getScreen().dispose();
			setScreen(new GameScreen(levels.get(levelNames.get(currentLevel))));
		} catch (IndexOutOfBoundsException e) {
			allLevelsFinished();
		}
	}
	
	/**
	 * Announces congrats at beating all levels
	 */
	public void allLevelsFinished() {
		
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
	
	/**
	 * Returns unmodifiable map of textures
	 * @return textures
	 */
	public static Map<String, Texture> getTextures() {
		return textures;
	}
	
	public static void setState(GameState s) {
		if (s == null) {
			throw new IllegalArgumentException("Invalid GameState in GravityGame.setState()");
		}
		state = s;
	}
	
	/**
	 * Returns game state
	 * @return state
	 */
	public static GameState getState() {
		return state;
	}
	
	/**
	 * Specifies current state of game
	 * 
	 * @author Sawyer Harris
	 * 
	 */
	public enum GameState {
		MENU, LEVEL_EDITOR, VIEW_MOVING, AIMING, FIRING, LEVEL_FAILURE, LEVEL_SUCCESS
	}
}