package com.sawyerharris.gravitygame.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents an immutable game level. Custom levels have no message and use the
 * default theme.
 * 
 * @author Sawyer Harris
 *
 */
public class Level {
	/** Name of level */
	private String name;
	/** Message to be shown when level is loaded */
	private String message;
	/** Type of level */
	private LevelType type;
	/** Level theme */
	private String theme;
	/** Ship initial position */
	private Vector2 shipOrigin;
	/** List of planets */
	private ArrayList<PlanetMeta> planets;

	/**
	 * Default constructor, used only by JSON deserialization.
	 */
	public Level() {
	}

	/**
	 * Constructs a custom level with the given parameters. Custom levels have
	 * no message and use the default theme.
	 * 
	 * @param name
	 * @param shipOrigin
	 * @param planets
	 */
	public Level(String name, Vector2 shipOrigin, ArrayList<PlanetMeta> planets) {
		this.name = name;
		this.message = null;
		this.type = LevelType.CUSTOM;
		this.theme = GravityGame.getInstance().getAssets().getTheme("default");
		this.shipOrigin = shipOrigin;
		this.planets = planets;
	}

	/**
	 * Gets initial position of ship.
	 * 
	 * @return shipOrigin
	 */
	public Vector2 getShipOrigin() {
		return shipOrigin;
	}

	/**
	 * Gets name of level.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets message of level.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the type of level.
	 * 
	 * @return type
	 */
	public LevelType getType() {
		return type;
	}

	/**
	 * Gets theme of level.
	 * 
	 * @return theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Gets the list of planets.
	 * 
	 * @return planets
	 */
	public ArrayList<PlanetMeta> getPlanets() {
		return planets;
	}

	/**
	 * A basic representation of a planet, used for constructing Planet actors.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public static class PlanetMeta {
		/** Initial position of planet */
		private Vector2 position;
		/** Initial radius of planet */
		private int radius;
		/** If the planet should be a home planet */
		private boolean homePlanet;

		/**
		 * Gets the initial position of the planet.
		 * 
		 * @return initial position
		 */
		public Vector2 getPosition() {
			return position;
		}

		/**
		 * Gets the initial radius of the planet.
		 * 
		 * @return initial radius
		 */
		public int getRadius() {
			return radius;
		}

		/**
		 * Gets if this planet should be a home planet.
		 * 
		 * @return true if it is a home planet
		 */
		public boolean isHomePlanet() {
			return homePlanet;
		}
	}

	/**
	 * Type of level.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum LevelType {
		OFFICIAL, TUTORIAL, CUSTOM
	}
}
