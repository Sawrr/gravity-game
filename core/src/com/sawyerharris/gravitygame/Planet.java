package com.sawyerharris.gravitygame;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Planet
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Mass area density of planets */
	private static final int density = 1;
	
	private Vector3 position;
	private int radius;
	private int mass;
	
	public Planet(Vector3 position, int radius) {
		this.position = position;
		this.radius = radius;
		// Normalized pi because units are irrelevant
		this.mass = density * radius * radius;
	}
	
	/**
	 * Returns planet position
	 * @return position
	 */
	public Vector3 getPosition() {
		return position;
	}
	
	/**
	 * Return planet radius
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Return planet mass
	 * @return mass
	 */
	public int getMass() {
		return mass;
	}
}
