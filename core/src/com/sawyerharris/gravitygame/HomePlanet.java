package com.sawyerharris.gravitygame;

import com.badlogic.gdx.math.Vector2;

/**
 * Home planet
 * The player's goal is to reach the home planet
 * 
 * @author Sawyer Harris
 *
 */
public class HomePlanet extends Planet {
	/**
	 * Constructor. Home planet image is set in super constructor
	 * @param position
	 * @param radius
	 */
	public HomePlanet(Vector2 position, int radius) {
		super(position, radius);		
	}
}
