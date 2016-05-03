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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Gravity Game
 * 
 * @author Sawyer Harris
 * 
 */
public class GravityGame extends Game {
	public static final int DESKTOP_WIDTH = 720;
	public static final int DESKTOP_HEIGHT = 1280;
	public static final int DESKTOP_SCREEN_WIDTH = 360;
	public static final int DESKTOP_SCREEN_HEIGHT = 640;
	
	private static Level tempLevel;
	private int customLevelEditId;
	
	/** Path of ttf font */
	public static FileHandle fontPath;
	
	private static Skin skin;
	
	/** Map of themes */
	private static Map<String, Theme> themes = new HashMap<String, Theme>();
	/** Map of levels */
	private static Map<String, Level> levels = new HashMap<String, Level>();
	/** Ordered list of levels */
	private static List<String> levelNames = new ArrayList<String>();
	/** Map of tutorial levels */
	private static Map<String, Level> tutorialLevels = new HashMap<String, Level>();
	/** Ordered list of tutorial levels */
	private static List<String> tutorialLevelNames = new ArrayList<String>();
	/** Map of Textures */
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	/** List of custom levels */
	private static ArrayList<Level> customLevels = new ArrayList<Level>();
	
	/** Name of the status preferences file */
	private static final String statusPrefsName = "com.sawyerharris.gravitygame.status";
	/** Tracks user progress and settings */
	private static Preferences statusPrefs;
	/** Status Preferences Names */
	private static String currentLevelStr = "currentLevel";
	private static String highestLevelStr = "highestLevel";
	private static String tutorialCompletedStr = "tutorialCompleted";
	private static String soundOnStr = "soundOn";
	
	/** Name of the custom levels file */
	private static final String customLevelPrefsName = "com.sawyerharris.gravitygame.customlevels";
	/** Name of JSON string stored in customLevelPrefs */
	public static final String customLevelPrefsStr = "customLevelsStr";
	/** Contains player's custom levels */
	private static Preferences customLevelPrefs;
	
	/** Game status variables */
	private static int currentLevel;
	private static int highestLevel;
	private static boolean tutorialCompleted;
	private static boolean soundOn;
	
	// Not saved
	private static int currentTutorialLevel;
	
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
			screenWidth = DESKTOP_WIDTH;
			screenHeight = DESKTOP_HEIGHT;
		}
		aspectRatio = screenWidth / (float) screenHeight;
		
		AssetLoader.loadThemes(themes);
		themes = Collections.unmodifiableMap(themes);
		
		AssetLoader.loadLevels(levels, levelNames);
		levels = Collections.unmodifiableMap(levels);
		levelNames = Collections.unmodifiableList(levelNames);
		
		AssetLoader.loadTutorialLevels(tutorialLevels, tutorialLevelNames);
		tutorialLevels = Collections.unmodifiableMap(tutorialLevels);
		tutorialLevelNames = Collections.unmodifiableList(tutorialLevelNames);
		
		AssetLoader.loadThemeTextures(textures, themes);
		AssetLoader.loadOtherTextures(textures);
		textures = Collections.unmodifiableMap(textures);
		fontPath = AssetLoader.loadFont();
		skin = AssetLoader.loadSkin();
		
		statusPrefs = Gdx.app.getPreferences(statusPrefsName);
		//currentLevel = statusPrefs.getInteger(currentLevelStr);
		tutorialCompleted = statusPrefs.getBoolean(tutorialCompletedStr);
		currentTutorialLevel = 0;
		highestLevel = statusPrefs.getInteger(highestLevelStr);
		soundOn = statusPrefs.getBoolean(soundOnStr);
		
		customLevelPrefs = Gdx.app.getPreferences(customLevelPrefsName);
		String customLevelsStr = customLevelPrefs.getString(customLevelPrefsStr);
		AssetLoader.loadCustomLevels(customLevels, customLevelsStr);
		
		Gdx.input.setCatchBackKey(true);
		
		setState(GameState.MENU);
		setScreen(new MainMenuScreen(this));
		
		if (!tutorialCompleted) {
			playLevel(tutorialLevels.get(tutorialLevelNames.get(0)), false);			
		}
		
		//playLevel(levels.get(levelNames.get(0)), false);
		//editLevel(customLevels.get(0), 0);
		//Level myCustomLevel = new Level();
		//myCustomLevel.setName("myCustLevel");
		//editLevel(myCustomLevel, 0);
	}
	
	/**
	 * Starts playing a given level
	 * @param level given level
	 * @param testing whether in level editor test mode
	 */
	public void playLevel(Level level, boolean testing) {
		getScreen().dispose();
		setScreen(new GameScreen(this, level, testing));
	}
	
	/**
	 * Starts editing the given level at custom level index id
	 * @param level given level object
	 * @param id index of custom level array
	 */
	public void editLevel(Level level, int id) {
		customLevelEditId = id;
		getScreen().dispose();
		setScreen(new LevelEditorScreen(this, level, id));
	}
	
	/**
	 * Returns to level editing after testing
	 */
	public void resumeLevelEditing() {
		editLevel(tempLevel, customLevelEditId);
	}
	
	/**
	 * Advances game to next level according to levelNames
	 */
	public void nextLevel() {
		currentLevel++;
		statusPrefs.putInteger(currentLevelStr, currentLevel);
		if (currentLevel > highestLevel) {
			highestLevel = currentLevel;
			statusPrefs.putInteger(highestLevelStr, highestLevel);
		}
		statusPrefs.flush();
		try {
			playLevel(levels.get(levelNames.get(currentLevel)), false);
		} catch (IndexOutOfBoundsException e) {
			allLevelsFinished();
		}
	}
	
	/**
	 * Advances game to next tutorial level
	 */
	public void nextTutorialLevel() {
		currentTutorialLevel++;
		try {
			playLevel(tutorialLevels.get(tutorialLevelNames.get(currentTutorialLevel)), false);
		} catch (IndexOutOfBoundsException e) {
			tutorialFinished();
		}
	}
	
	/**
	 * Returns to main menu
	 */
	public void toMainMenu() {
		setState(GameState.MENU);
		getScreen().dispose();
		setScreen(new MainMenuScreen(this));
	}
	
	/**
	 * Announces congrats at beating all levels
	 */
	public void allLevelsFinished() {
		System.out.println("allLevelsFinished");
		currentLevel = 0;
		statusPrefs.putInteger(currentLevelStr, currentLevel);
		statusPrefs.flush();
		
		// TODO congrats at beating the game, go back to main menu
		toMainMenu();
	}
	
	/**
	 * Announces congrats at finishing the tutorial
	 */
	public void tutorialFinished() {
		System.out.println("tutorialFinished");
		// TODO congrats screen on beating tutorial, go back to main menu
		statusPrefs.putBoolean(tutorialCompletedStr, true);
		statusPrefs.flush();
		toMainMenu();
	}
	
	/**
	 * Renders game via screen
	 */
	@Override
	public void render() {
		super.render();
	}
	
	public static int getCurrentLevel() {
		return currentLevel;
	}
	
	public static void setCurrentLevel(int current) {
		currentLevel = current;
		statusPrefs.putInteger(currentLevelStr, currentLevel);
		statusPrefs.flush();
	}
	
	public static int getHighestLevel() {
		return highestLevel;
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
	 * Returns unmodifiable map of tutorial levels
	 * @return tutorialLevels
	 */
	public static Map<String, Level> getTutorialLevels() {
		return tutorialLevels;
	}
	
	/**
	 * Returns unmodifiable list of level names
	 * @return levelNames
	 */
	public static List<String> getLevelNames() {
		return levelNames;
	}
	
	/**
	 * Returns unmodifiable list of tutorial level names
	 * @return tutorialLevelNames
	 */
	public static List<String> getTutorialLevelNames() {
		return tutorialLevelNames;
	}
	
	/**
	 * Returns unmodifiable map of textures
	 * @return textures
	 */
	public static Map<String, Texture> getTextures() {
		return textures;
	}
	
	public static Preferences getCustomLevelPrefs() {
		return customLevelPrefs;
	}
	
	public static ArrayList<Level> getCustomLevels() {
		return customLevels;
	}
	
	public static Skin getSkin() {
		return skin;
	}
	
	public static void setTempLevel(Level lvl) {
		tempLevel = lvl;
	}
	
	public static Level getTempLevel() {
		return tempLevel;
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
		MENU, LEVEL_EDITOR, AIMING, FIRING, LEVEL_FAILURE, LEVEL_SUCCESS
	}
}