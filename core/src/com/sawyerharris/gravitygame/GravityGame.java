package com.sawyerharris.gravitygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
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
	
	/**
	 * Called when game started
	 */
	@Override
	public void create () {
		AssetLoader.loadThemes(themes);
		themes = Collections.unmodifiableMap(themes);
		AssetLoader.loadLevels(levels, levelNames);
		levels = Collections.unmodifiableMap(levels);
		levelNames = Collections.unmodifiableList(levelNames);
		AssetLoader.loadThemeTextures(textures, themes);
		textures = Collections.unmodifiableMap(textures);
		
		// Test case: Circular orbit with radius, velocity = 1
		Planet planet = new Planet(new Vector3(0,0,0), 1);
		Ship ship = new Ship(new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		ArrayList<Planet> planets = new ArrayList<Planet>();
		planets.add(planet);
		// Number of periods of revolution
		int N = 2;
		float dt = 0.001f;
		for (int i = 0; i < N*(int)(2*Math.PI/dt); i++) {
			ship.update(dt, planets);
			System.out.println("Error in r: " + (ship.getPosition().len() - 1.0f));
			System.out.println("Error in v: " + (ship.getVelocity().len() - 1.0f));
			System.out.println("x: " + ship.getPosition().x + " y: " + ship.getPosition().y);
			System.out.println("vx: " + ship.getVelocity().x + " vy: " + ship.getVelocity().y);
		}
	}
	
	/**
	 * Renders game via screen
	 */
	@Override
	public void render () {
		super.render();
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