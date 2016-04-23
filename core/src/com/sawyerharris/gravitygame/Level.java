package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/**
 * Game level
 * Immutability: fields may only be set by libgdx JSON deserialization
 * 					except for name, themeName, and the planets
 * 
 * @author Sawyer Harris
 * 
 */
public class Level {
	private String name;
	private LevelType type;
	private String message;
	private String themeName;
	private int width;
	private int height;
	private Vector2 shipOrigin;
	private ArrayList<PlanetMeta> planets;
	
	/**
	 * Type of level
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum LevelType {
		OFFICIAL, TUTORIAL, CUSTOM
	}
	
	/**
	 * Object containing planet data
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public static class PlanetMeta {
		private Vector2 position;
		private int radius;
		private boolean home;
		
		public Vector2 getPosition() {
			return position;
		}
		
		public int getRadius() {
			return radius;
		}
		
		public boolean getHome() {
			return home;
		}
	}
	
	/**
	 * Sets name of level (custom levels only)
	 * @param name
	 */
	public void setName(String name) {
		if (type == LevelType.CUSTOM) {
			this.name = name;
		}
	}
	
	/**
	 * Sets name of level theme (custom levels only)
	 * @param themeName
	 */
	public void setThemeName(String themeName) {
		if (type == LevelType.CUSTOM) {
			if (GravityGame.getThemes().containsKey(themeName)) {
				this.themeName = themeName;
			} else {
				throw new IllegalArgumentException("Invalid theme name");
			}
		}
	}
	
	/**
	 * Returns name of level
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns type of level (official, tutorial, custom)
	 * @return
	 */
	public LevelType getLevelType() {
		return type;
	}
	
	/**
	 * Returns level message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Returns name of level theme
	 * @return themeName
	 */
	public String getThemeName() {
		return themeName;
	}
	
	/**
	 * Returns width of level
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns height of level
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns ship origin
	 * @return shipOrigin
	 */
	public Vector2 getShipOrigin() {
		return shipOrigin;
	}
	
	/**
	 * Returns ArrayList of PlanetMeta objects
	 * @return planets
	 */
	public ArrayList<PlanetMeta> getPlanets() {
		return planets;
	}
}