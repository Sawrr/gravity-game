package com.sawyerharris.gravitygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.sawyerharris.gravitygame.GravityGame.GameState;
import com.sawyerharris.gravitygame.Level.PlanetMeta;

/**
 * Planet
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Mass area density of planets */
	private static final int density = 500;
	
	/** Whether planet is the home planet */
	private final boolean home;
	
	private Vector2 position;
	private int radius;
	private int mass;
	
	private Texture texture;
	private Theme theme;
	
	/**
	 * Constructor converts PlanetMeta into actual Planet
	 * @param planet PlanetMeta data
	 * @param theme Theme of level
	 */
	public Planet(PlanetMeta planet, Theme theme) {
		this.home = planet.getHome();
		this.position = planet.getPosition();
		this.radius = planet.getRadius();
		// Normalized pi because units are irrelevant
		this.mass = density * radius * radius;
		
		setTheme(theme);
		setBounds(position.x - radius, position.y - radius, 2 * radius, 2 * radius);
		
		if (GravityGame.getState() == GameState.LEVEL_EDITOR) {
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
		} else {
			setTouchable(Touchable.disabled);
		}
	}
	
	/**
	 * Sets the planet's theme
	 * @param theme
	 */
	public void setTheme(Theme theme) {
		if (home) {
			texture = GravityGame.getTextures().get(AssetLoader.HOME_PLANET_IMG);
			if (texture == null) {
				System.out.println("Error: image not found: " + AssetLoader.HOME_PLANET_IMG);
				System.exit(1);
			} else {
				this.theme = theme;
			}
		} else {
			texture = GravityGame.getTextures().get(theme.getPlanet());
			if (texture == null) {
				System.out.println("Error: image not found: " + theme.getPlanet());
				System.exit(1);
			} else {
				this.theme = theme;
			}
		}
	}
		
	public boolean isHome() {
		return home;
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
