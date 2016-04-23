package com.sawyerharris.gravitygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Planet
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Mass area density of planets */
	private static final int density = 500;
	
	private Vector2 position;
	private int radius;
	private int mass;
	
	private Texture texture;
	
	/**
	 * Constructor
	 * @param position
	 * @param radius
	 */
	protected Planet(Vector2 position, int radius) {
		this.position = position;
		this.radius = radius;
		// Normalized pi because units are irrelevant
		this.mass = density * radius * radius;
		
		if (this instanceof HomePlanet) {
			texture = GravityGame.getTextures().get(AssetLoader.HOME_PLANET_IMG);
			if (texture == null) {
				System.out.println("Error: image not found: " + AssetLoader.HOME_PLANET_IMG);
				System.exit(1);
			}
		}
	}
	
	/**
	 * Constructor with given theme
	 * @param position
	 * @param radius
	 * @param theme
	 */
	public Planet(Vector2 position, int radius, Theme theme) {
		this(position, radius);
		
		texture = GravityGame.getTextures().get(theme.getPlanet());
		if (texture == null) {
			System.out.println("Error: image not found: " + theme.getPlanet());
			System.exit(1);
		}
		
		setBounds(position.x - radius, position.y - radius, 2 * radius, 2 * radius);
		
		// if LEVEL_EDITOR mode, add the following
		addListener(new DragListener() {
			@Override		
			public void drag(InputEvent event, float x, float y, int pointer) {
				System.out.println("planet x: " + (x + getX()) + " y: " + (y + getY()));
			}
		});
		
		addListener(new ActorGestureListener() {
			@Override
			public void zoom(InputEvent event, float initialDistance, float distance) {
				System.out.println("Planet zoom: " + distance);
			}
		});
	}
		
	/**
	 * Returns planet position
	 * @return position
	 */
	public Vector2 getPosition() {
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
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(texture, position.x - radius, position.y - radius, 2 * radius, 2 * radius);
	}
}
