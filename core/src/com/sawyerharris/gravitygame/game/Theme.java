package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.graphics.Color;

/**
 * A level theme, includes a background, planet style, and color.
 * 
 * @author Sawyer Harris
 *
 */
public class Theme {
	/** Theme name */
	private String name;
	/** Name of background texture */
	private String background;
	/** Name of planet texture */
	private String planet;
	/** Amount of background image to show */
	private int srcViewWidth;
	/** Color that matches background */
	private Color color;

	/**
	 * Empty constructor, only used by JSON deserialization.
	 */
	public Theme() {
	}
	
	/**
	 * Gets the name of the theme.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the name of the background.
	 * 
	 * @return background
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * Gets the name of the planet texture.
	 * 
	 * @return planet
	 */
	public String getPlanet() {
		return planet;
	}

	/**
	 * Gets the source view width.
	 * 
	 * @return srcViewWidth
	 */
	public int getSrcViewWidth() {
		return srcViewWidth;
	}

	/**
	 * Gets the color of the theme.
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Theme [name=" + name + ", background=" + background + ", planet=" + planet + ", srcViewWidth="
				+ srcViewWidth + ", color=" + color + "]";
	}
}
