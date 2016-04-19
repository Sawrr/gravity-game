package com.sawyerharris.gravitygame;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;

/**
 * Gravity Game
 * 
 * @author Sawyer Harris
 * 
 */
public class GravityGame extends Game {
	/** Max number of custom levels the user may create */
	public static final int NUM_CUSTOM_LEVELS = 10;
	/** Map of themes */
	public static final HashMap<String, Theme> themes = new HashMap<String, Theme>();
	/** Map of levels */
	public static final HashMap<String, Level> levels = new HashMap<String, Level>();
	
	/** Tracks user progress and settings */
	private Preferences statusPrefs;
	/** State of the game */
	private GameState state;
	
	/**
	 * Called when game started
	 */
	@Override
	public void create () {
		AssetLoader.loadThemes(themes);
		AssetLoader.loadLevels(levels);
	}
	
	/**
	 * Renders game via screen
	 */
	@Override
	public void render () {
		super.render();
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