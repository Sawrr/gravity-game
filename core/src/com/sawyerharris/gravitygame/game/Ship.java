package com.sawyerharris.gravitygame.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sawyerharris.gravitygame.screen.GameScreen;

/**
 * Represents a Ship actor.
 * 
 * @author Sawyer Harris
 *
 */
public class Ship extends Actor {
	/** Maximum amount of boost a ship may have */
	public static final int MAX_BOOST = 0;
	/** Scalar for how much boost is applied */
	private static final float BOOST_SCALAR = 0;

	/** Initial position of ship */
	private Vector2 initialPosition;
	/** Ship velocity used for physics update */
	private Vector2 vel;
	/** Ship position used for physics update */
	private Vector2 pos;
	/** Animated sprite of ship */
	private ShipSprite sprite;
	/** Index of ship style in animation list */
	private int style;
	/** Amount of boost remaining */
	private int boost;
	/** If the ship is using boost */
	private boolean boosting;

	/**
	 * Constructs a ship.
	 */
	public Ship() {
		sprite = new ShipSprite();
		reset();
	}

	/**
	 * Start using boost.
	 */
	public void startBoosting() {
		sprite.setAnimation(GravityGame.getInstance().getAssets().getShipAnimation(style, "boost"));
		boosting = true;
	}

	/**
	 * Stop using boost.
	 */
	public void stopBoosting() {
		sprite.setAnimation(GravityGame.getInstance().getAssets().getShipAnimation(style, "default"));
		boosting = false;
	}

	/**
	 * Resets ship to initial state.
	 */
	public void reset() {
		style = GravityGame.getInstance().getPlayerStatus().getShipStyle();
		setPosition(initialPosition);
		vel.set(0, 0);
		stopBoosting();
		boost = MAX_BOOST;
	}

	/**
	 * Returns the ship's position.
	 * 
	 * @return Vector2 of position
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}

	/**
	 * Sets the ship's position.
	 * 
	 * @param pos
	 */
	public void setPosition(Vector2 pos) {
		if (pos.x < 0 || pos.x > GameScreen.WORLD_WIDTH || pos.y < 0 || pos.y > GameScreen.WORLD_HEIGHT) {
			throw new IllegalArgumentException("Ship position out of bounds.");
		}
		setPosition(pos.x, pos.y);
		sprite.setCenter(pos.x, pos.y);
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth() * sprite.getScaleX(),
				sprite.getHeight() * sprite.getScaleY());
	}

	/**
	 * Returns the ship's initial position.
	 * 
	 * @return initialPosition
	 */
	public Vector2 getInitialPosition() {
		return initialPosition;
	}

	/**
	 * Sets the ship's initial position.
	 * 
	 * @param pos
	 */
	public void setInitialPosition(Vector2 pos) {
		if (pos.x < 0 || pos.x > GameScreen.WORLD_WIDTH || pos.y < 0 || pos.y > GameScreen.WORLD_HEIGHT) {
			throw new IllegalArgumentException("Ship position out of bounds.");
		}
		initialPosition = pos;
	}

	/**
	 * Updates ship's position and velocity using Runge-Kutta 4th Order method.
	 * 
	 * @param dt
	 */
	public void physicsUpdate(float dt, ArrayList<Planet> planets) {
		pos.set(getPosition());

		Vector2 vela = new Vector2(vel).add(computeAccel(pos, planets).scl(dt / 2));
		Vector2 posa = new Vector2(pos).add(new Vector2(vel).scl(dt / 2));

		Vector2 velb = new Vector2(vel).add(computeAccel(posa, planets).scl(dt / 2));
		Vector2 posb = new Vector2(pos).add(new Vector2(vela).scl(dt / 2));

		Vector2 velc = new Vector2(vel).add(computeAccel(posb, planets).scl(dt));
		Vector2 posc = new Vector2(pos).add(new Vector2(velb).scl(dt));

		Vector2 veld = new Vector2(vel).add(computeAccel(posc, planets).scl(dt));
		Vector2 posd = new Vector2(pos).add(new Vector2(velc).scl(dt));

		vel = new Vector2(vela.add(velb.scl(2)).add(velc).add(veld.scl(1.0f / 2)).scl(1.0f / 3))
				.sub(new Vector2(vel).scl(1.0f / 2));
		pos = new Vector2(posa.add(posb.scl(2)).add(posc).add(posd.scl(1.0f / 2)).scl(1.0f / 3))
				.sub(new Vector2(pos).scl(1.0f / 2));

		setPosition(pos);
	}

	/**
	 * Computes the acceleration of the ship due to the gravity of the planets.
	 * 
	 * @param shipPosition
	 *            position of ship
	 * @param planets
	 *            list of planets
	 * @return acceleration
	 */
	private Vector2 computeGravityAccel(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 acc = new Vector2(0, 0);
		for (Planet planet : planets) {
			Vector2 r = new Vector2(pos);
			r.sub(planet.getPosition());
			float rcube = (float) Math.pow(r.len(), 3);
			acc.add(r.scl(-planet.getMass() / rcube));
		}
		return acc;
	}

	/**
	 * Computes the acceleration of the ship due to gravity and boost.
	 * 
	 * @param shipPosition
	 *            position of ship
	 * @param planets
	 *            list of planets
	 * @return acceleration
	 */
	private Vector2 computeAccel(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 acc = computeGravityAccel(shipPosition, planets);
		if (boosting) {
			if (boost > 0) {
				Vector2 boostVec = new Vector2(vel).nor().scl(BOOST_SCALAR);
				acc.add(boostVec);
				boost--;
			} else {
				// Ran out of boost
				stopBoosting();
			}
		}
		return acc;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		sprite.draw(batch, alpha);
	}

	/**
	 * Animated ship sprite.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ShipSprite extends Sprite {
		/** Animation state time */
		private float time;
		/** Animation */
		private Animation animation;

		/**
		 * Sets the sprite's animation.
		 * 
		 * @param anim
		 */
		public void setAnimation(Animation anim) {
			animation = anim;
			time = 0;
		}

		@Override
		public void draw(Batch batch, float alpha) {
			time += Gdx.graphics.getDeltaTime();
			setRegion(animation.getKeyFrame(time));
			setRotation(vel.angle());
			super.draw(batch, alpha);
		}
	}

}
