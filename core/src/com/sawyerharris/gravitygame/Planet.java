package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.Level.PlanetMeta;

/**
 * Planet
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Mass area DENSITY of planets */
	private static final int DENSITY = 1500;
	
	/** Minimum allowed radius */
	private static final float MIN_RADIUS = 30;
	/** Maximum allowed radius */
	private static final float MAX_RADIUS = 200;
	/** The minimum radius at which a planet can be grabbed or zoomed */
	private static final int MIN_RADIUS_BOUND = 150;
	/** Default radius when planet is added in level editor */
	private static final int DEFAULT_RADIUS = 50;
	
	/** Whether planet is the home planet */
	private boolean home;
	
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
	public Planet(PlanetMeta planet, Theme theme, Screen screen) {
		this.home = planet.getHome();
		this.position = planet.getPosition();
		this.radius = planet.getRadius();
		
		initializePlanet(theme, screen);
	}
	
	/**
	 * Constructor used by level editor to add planets
	 * @param pos Initial position of ship
	 * @param theme Theme of level
	 */
	public Planet(Vector2 pos, Theme theme, Screen screen) {
		this.home = false;
		this.position = pos;
		this.radius = DEFAULT_RADIUS;
		
		initializePlanet(theme, screen);
	}
	
	/**
	 * Used by constructors
	 * Adds listeners, sets mass, theme, and bounds
	 * @param theme
	 * @param screen
	 */
	private void initializePlanet(Theme theme, Screen screen) {
		// Normalized pi because units are irrelevant
		this.mass = DENSITY * radius * radius;
		
		if (screen instanceof LevelEditorScreen) {
			addListener(new PlanetGestureListener(this, 5f, 0.4f, 1.1f, 0.15f));
		} else {
			setTouchable(Touchable.disabled);
		}
		
		setTheme(theme);
		updateBounds();
	}
	
	/**
	 * Updates bounds of planet for gestures
	 */
	public void updateBounds() {
		int radBound = Math.max(radius, MIN_RADIUS_BOUND);
		setBounds(position.x - radBound, position.y - radBound, 2 * radBound, 2 * radBound);
	}
	
	/**
	 * Changes planet radius by amount
	 * @param amount
	 */
	public void changeRadiusBy(int amount) {
		if (radius + amount <= MAX_RADIUS && radius + amount >= MIN_RADIUS) {
			radius += amount;	
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
	
	/**
	 * Sets whether planet is the home planet
	 * @param home
	 */
	public void setHome(boolean home) {
		this.home = home;
		setTheme(theme);
	}
	
	/**
	 * Returns whether planet is the home planet
	 * @return home
	 */
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
	
	/**
	 * Return planet's theme
	 * @return theme
	 */
	public Theme getTheme() {
		return theme;
	}
	
	/**
	 * Called when planet is rendered
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(texture, position.x - radius, position.y - radius, 2 * radius, 2 * radius);
	}
}
