package com.sawyerharris.gravitygame.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.sawyerharris.gravitygame.screen.LevelScreen;

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
	/** Author of level */
	private String author;
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
	 * Empty constructor, used only by JSON deserialization.
	 */
	public Level() {
	}

	/**
	 * Constructs a custom level with the given parameters. Custom levels have
	 * no message and use the default theme.
	 * 
	 * @param name
	 * @param author
	 * @param shipOrigin
	 * @param planets
	 */
	public Level(String name, String author, Vector2 shipOrigin, ArrayList<PlanetMeta> planets) {
		this.name = name;
		this.author = author;
		this.message = null;
		this.type = LevelType.CUSTOM;
		this.theme = LevelScreen.DEFAULT_THEME.getName();
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
	 * Gets author of level.
	 * 
	 * @return author
	 */
	public String getAuthor() {
		return author;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PlanetMeta planet : planets) {
			sb.append(planet.toString());
			sb.append(" ");
		}
		return "Level [name=" + name + ", message=" + message + ", type=" + type + ", theme=" + theme + ", shipOrigin="
				+ shipOrigin + ", planets= [" + sb.toString() + "]]";
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

		public PlanetMeta() {
			
		}
		
		public PlanetMeta(Vector2 position, int radius, boolean homePlanet) {
			this.position = position;
			this.radius = radius;
			this.homePlanet = homePlanet;
		}
		
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

		@Override
		public String toString() {
			return "PlanetMeta [position=" + position + ", radius=" + radius + ", homePlanet=" + homePlanet + "]";
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
