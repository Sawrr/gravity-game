package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.screen.LevelScreen;

/**
 * Represents a Planet actor.
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Maximum, minimum radius allowed */
	public static final int MAX_RADIUS = 200;
	public static final int MIN_RADIUS = 50;
	/** Mass area density */
	private static final float MASS_DENSITY = 1000;

	/** Radius of planet */
	private int radius;
	/** Texture region of planet style */
	private TextureRegion region;
	/** Sprite of planet to be drawn */
	private Sprite sprite;
	/** Whether the planet a home planet */
	private boolean homePlanet;
	/** Mass of planet based on radius assuming uniform density */
	private float mass;

	/**
	 * Constructs a Planet actor with the given parameters.
	 * 
	 * @param position
	 *            position of planet
	 * @param radius
	 *            radius of planet
	 * @param textureRegion
	 *            style of planet
	 * @param homePlanet
	 *            if this planet is a home planet
	 * @param touchable
	 *            Touchable.enabled, disabled, or childrenOnly
	 */
	public Planet(Vector2 position, int radius, TextureRegion textureRegion, boolean homePlanet) {
		region = textureRegion;

		sprite = new Sprite(textureRegion);
		sprite.setOriginCenter();

		setPosition(position);
		setRadius(radius);
		setHomePlanet(homePlanet);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// Set hit detection on planet actor to be circular i.e. based on radius
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return new Vector2(x, y).len() <= radius ? this : null;
	}

	/**
	 * Returns the radius of the planet.
	 * 
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Returns whether the planet is a home planet.
	 * 
	 * @return true if home planet
	 */
	public boolean isHomePlanet() {
		return homePlanet;
	}

	/**
	 * Returns the planet's mass.
	 * 
	 * @return mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Returns the planet's position.
	 * 
	 * @return Vector2 of position
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}

	/**
	 * Sets the position of the planet, also updating the sprite.
	 * 
	 * @param pos
	 *            position
	 */
	public void setPosition(Vector2 pos) {
		if (pos.x < -LevelScreen.WORLD_WIDTH / 2 || pos.x > LevelScreen.WORLD_WIDTH / 2
				|| pos.y < -LevelScreen.WORLD_HEIGHT / 2 || pos.y > LevelScreen.WORLD_HEIGHT / 2) {
			throw new IllegalArgumentException("Planet position out of bounds.");
		}
		setPosition(pos.x, pos.y);
		sprite.setCenter(pos.x, pos.y);
	}

	/**
	 * Sets the radius of the planet, also updating the sprite.
	 * 
	 * @param rad
	 *            radius
	 */
	public void setRadius(int rad) {
		if (rad < 0 || rad > MAX_RADIUS) {
			throw new IllegalArgumentException("Invalid planet radius.");
		}
		radius = rad;
		sprite.setScale(rad / (float) (sprite.getRegionWidth() / 2));
		mass = MASS_DENSITY * rad * rad;
	}

	/**
	 * Sets whether the planet is a home planet, also updating the sprite.
	 * 
	 * @param home
	 *            true if home planet
	 */
	public void setHomePlanet(boolean home) {
		homePlanet = home;
		if (homePlanet) {
			sprite.setRegion(GravityGame.getInstance().getAssets().getPlanet("earth"));
		} else {
			sprite.setRegion(region);
		}
	}

	@Override
	public void draw(Batch batch, float alpha) {
		sprite.draw(batch);
	}

	/**
	 * Translates the planet by the given amount and ensures planet stays within
	 * bounds (used by level editor).
	 * 
	 * @param x
	 *            amount to move in x direction
	 * @param y
	 *            amount to move in y direction
	 */
	public void translate(float x, float y) {
		Vector2 pos = new Vector2(getPosition()).add(x, y);
		float border = radius;
		if (pos.x < -LevelScreen.WORLD_WIDTH / 2 + border) {
			pos.x = -LevelScreen.WORLD_WIDTH / 2 + border;
		}
		if (pos.x > LevelScreen.WORLD_WIDTH / 2 - border) {
			pos.x = LevelScreen.WORLD_WIDTH / 2 - border;
		}
		if (pos.y < -LevelScreen.WORLD_HEIGHT / 2 + border) {
			pos.y = -LevelScreen.WORLD_HEIGHT / 2 + border;
		}
		if (pos.y > LevelScreen.WORLD_HEIGHT / 2 - border) {
			pos.y = LevelScreen.WORLD_HEIGHT / 2 - border;
		}

		setPosition(pos);
	}

	/**
	 * Scales the radius of the planet by the given amount, ensuring the max and
	 * min radii are not exceeded (used by level editor).
	 * 
	 * @param amount
	 *            amount to scale radius
	 */
	public void zoom(float amount) {
		radius += amount;
		if (radius > MAX_RADIUS) {
			radius = MAX_RADIUS;
		} else if (radius < MIN_RADIUS) {
			radius = MIN_RADIUS;
		}
		setRadius(radius);
	}
}
