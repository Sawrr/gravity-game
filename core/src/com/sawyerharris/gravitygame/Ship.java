package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sawyerharris.gravitygame.GravityGame.GameState;

/**
 * Spaceship
 * (mass, G = 1)
 * 
 * @author Sawyer Harris
 *
 */
public class Ship extends Actor {
	/** Characteristic radius of ship used for collisions */
	public static final int COLLISION_RADIUS = 20;
	/** Characteristic radius of ship used for drag listener */
	private static final int TOUCH_RADIUS = 80;
	/** Conversion from fling velocity to ship initial velocity */
	private static final float FLING_SCALAR = 0.2f;
	/** Maximum fling velocity */
	private static final float MAX_FLING_VELOCITY = 1000f;
	/** Initial angle ship faces */
	private static final float INITIAL_ANGLE = 90;
	/** Initial amount of boost */
	private static final int INITIAL_BOOST = 2750;
	/** Boost velocity scalar */
	private static final float BOOST_SCALAR = 200f;
	
	/** Amount of boost available */
	private int boost;
	/** Whether boost is being used */
	private boolean isBoosting;
	
	private Screen screen;
	private Vector2 initialPosition;
	private Vector2 position;
	private Vector2 velocity;
	private float angle;
	
	private Texture shipTexture;
	private Texture shipBoostTexture;
	private Sprite sprite;
	
	/**
	 * Constructor creates ship on given screen at initialPosition
	 * @param screen
	 * @param initialPosition
	 */
	public Ship(Screen screen, Vector2 initialPosition) {
		this.screen = screen;
		this.initialPosition = initialPosition;
		this.position = initialPosition;
		this.velocity = new Vector2(0,0);
		this.angle = INITIAL_ANGLE;
		this.boost = INITIAL_BOOST;
		this.isBoosting = false;

		updateBounds();
		loadTextures();

		addListener(new ShipGestureListener(this));
	}
	
	/**
	 * Called when ship is flung in Aiming mode
	 * @param velocityX
	 * @param velocityY
	 */
	public void fling(Vector2 initialVelocity) {
		if (screen instanceof GameScreen && GravityGame.getState() == GameState.AIMING) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateFiring(initialVelocity.scl(FLING_SCALAR));
		}
	}
	
	/**
	 * Called when ship is panned in Level Editor mode
	 * @param deltaPos
	 */
	public void pan(Vector2 deltaPos) {
		if (screen instanceof LevelEditorScreen) {
			position.add(deltaPos);
			updateBounds();
		}
	}
	
	/**
	 * Loads ship textures into fields
	 */
	private void loadTextures() {
		shipTexture = GravityGame.getTextures().get(AssetLoader.SHIP_IMG);
		if (shipTexture == null) {
			System.out.println("Error: " + AssetLoader.SHIP_IMG + " not found");
			System.exit(1);
		}
		shipBoostTexture = GravityGame.getTextures().get(AssetLoader.SHIP_BOOST_IMG);
		if (shipBoostTexture == null) {
			System.out.println("Error: " + AssetLoader.SHIP_BOOST_IMG + " not found");
			System.exit(1);
		}
		sprite = new Sprite(shipTexture);
	}
	
	/**
	 * Updates bounds of ship for gestures
	 */
	public void updateBounds() {
		setBounds(position.x - TOUCH_RADIUS, position.y - TOUCH_RADIUS, 2 * TOUCH_RADIUS, 2 * TOUCH_RADIUS);
	}
	
	/**
	 * Perform physics update on ship
	 * @param dt delta time
	 * @param planets list of planets
	 */
	public void update(float dt, ArrayList<Planet> planets) {
		RK4(dt, planets);
	}
	
	/**
	 * Computes force of gravity on ship due to list of Planet objects
	 * @param planets list of Planet objects
	 * @return force
	 */
	public Vector2 computeGravityForce(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 force = new Vector2(0, 0);
		for (Planet planet : planets) {
			Vector2 r = new Vector2(position);
			r.sub(planet.getPosition());
			float rcube = (float) Math.pow(r.len(), 3);
			force.add(r.scl(-planet.getMass() / rcube));
		}
		return force;
	}
	
	/**
	 * Computes acceleration on ship (gravity + potential boosting)
	 * @param shipPosition
	 * @param planets
	 * @return acceleration vector
	 */
	public Vector2 computeAccel(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 acc = computeGravityForce(shipPosition, planets);
		if (Gdx.input.isTouched() && boost > 0) {
			if (!isBoosting) {
				startBoosting();
			}
				
			Vector2 boostVec = new Vector2(velocity).nor().scl(BOOST_SCALAR);
			acc.add(boostVec);
			boost--;
		} else {
			if (isBoosting) {
				stopBoosting();
			}
		}
		return acc;
	}
	
	/**
	 * Updates position and velocity using Runge-Kutta 4th order method
	 */
	private void RK4(float dt, ArrayList<Planet> planets) {
		Vector2 vela = new Vector2(velocity).add(computeAccel(position, planets).scl(dt / 2));
		Vector2 posa = new Vector2(position).add(new Vector2(velocity).scl(dt / 2));
		
		Vector2 velb = new Vector2(velocity).add(computeAccel(posa, planets).scl(dt / 2));
		Vector2 posb = new Vector2(position).add(new Vector2(vela).scl(dt / 2));
		
		Vector2 velc = new Vector2(velocity).add(computeAccel(posb, planets).scl(dt));
		Vector2 posc = new Vector2(position).add(new Vector2(velb).scl(dt));
		
		Vector2 veld = new Vector2(velocity).add(computeAccel(posc, planets).scl(dt));
		Vector2 posd = new Vector2(position).add(new Vector2(velc).scl(dt));
		
		velocity = new Vector2(vela.add(velb.scl(2)).add(velc).add(veld.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector2(velocity).scl(1.0f/2));
		position = new Vector2(posa.add(posb.scl(2)).add(posc).add(posd.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector2(position).scl(1.0f/2));
		
		angle = velocity.angle();
	}
	
	/**
	 * Resets the ship's position, velocity, and angle to initial state
	 */
	public void reset() {
		angle = INITIAL_ANGLE;
		position = initialPosition;
		velocity.set(0,0);
		boost = INITIAL_BOOST;
	}
	
	/**
	 * Called when ship begins boosting
	 */
	private void startBoosting() {
		isBoosting = true;
		sprite.setTexture(shipBoostTexture);
	}
	
	/**
	 * Called when ship stops boosting
	 */
	private void stopBoosting() {
		isBoosting = false;
		sprite.setTexture(shipTexture);
	}
	
	/**
	 * Returns the ship's initial position
	 * @return initialPosition
	 */
	public Vector2 getInitialPosition() {
		return initialPosition;
	}
	
	/**
	 * Returns position of ship
	 * @return position
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * Sets the ship's velocity
	 * @param velocity
	 */
	public void setVelocity(Vector2 velocity) {
		if (velocity.len() > MAX_FLING_VELOCITY) {
			velocity.nor().scl(MAX_FLING_VELOCITY);
		}
		this.velocity = velocity;
	}
	
	/**
	 * Returns velocity of ship
	 * @return velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	/**
	 * Called when ship is rendered
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(angle);
		sprite.draw(batch);
	}
}
