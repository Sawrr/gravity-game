package com.sawyerharris.gravitygame;

/**
 * Level theme
 * Immutable: fields may only be set by libgdx JSON deserialization
 * 
 * @author Sawyer Harris
 * 
 */
public class Theme {
	private String name;
	private String background;
	private String planet;
	private String music;
	private float paraScalar;
	private float srcViewWidth;
		
	/**
	 * Returns name of theme
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns name of background image
	 * @return background
	 */
	public String getBackground() {
		return background;
	}
	
	/**
	 * Returns name of planet image
	 * @return planet
	 */
	public String getPlanet() {
		return planet;
	}
	
	/**
	 * Returns name of music file
	 * @return music
	 */
	public String getMusic() {
		return music;
	}
	
	/**
	 * Returns scalar used in parallax
	 * @return paraScalar
	 */
	public float getParaScalar() {
		return paraScalar;
	}
	
	/**
	 * Returns width in pixels of background image to be displayed
	 * @return srcViewWidth
	 */
	public float getSrcViewWidth() {
		return srcViewWidth;
	}
}
