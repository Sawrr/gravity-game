package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.sawyerharris.gravitygame.screen.GameScreen;

/**
 * Represents a Planet actor.
 * 
 * @author Sawyer Harris
 *
 */
public class Planet extends Actor {
	/** Maximum radius allowed */
	private static final int MAX_RADIUS = 0;
	/** Mass area density */
	private static final float MASS_DENSITY = 0;

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
	public Planet(Vector2 position, int radius, TextureRegion textureRegion, boolean homePlanet, Touchable touchable) {
		setTouchable(touchable);
		region = textureRegion;

		sprite = new Sprite(textureRegion);
		sprite.setOriginCenter();

		setPosition(position);
		setRadius(radius);
		setHomePlanet(homePlanet);

		if (isTouchable()) {
			addListener(new ActorGestureListener() {
				@Override
				public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
					try {
						setPosition(new Vector2(x, y));
					} catch (IllegalArgumentException e) {
						//
					}
				}

				@Override
				public void zoom(InputEvent event, float initialDistance, float distance) {
					try {
						setRadius((int) (distance - initialDistance));
					} catch (IllegalArgumentException e) {
						//
					}
				}
			});
		}
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
		if (pos.x < 0 || pos.x > GameScreen.WORLD_WIDTH || pos.y < 0 || pos.y > GameScreen.WORLD_HEIGHT) {
			throw new IllegalArgumentException("Planet position out of bounds.");
		}
		setPosition(pos.x, pos.y);
		sprite.setCenter(pos.x, pos.y);
		if (isTouchable()) {
			setBounds(sprite.getX(), sprite.getY(), sprite.getWidth() * sprite.getScaleX(),
					sprite.getHeight() * sprite.getScaleY());
		}
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
		sprite.setScale(rad / (float) sprite.getRegionWidth());
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
			sprite.setRegion(GravityGame.getInstance().getAssets().getPlanet("home"));
		} else {
			sprite.setRegion(region);
		}
	}

	@Override
	public void draw(Batch batch, float alpha) {
		sprite.draw(batch);
	}
}
