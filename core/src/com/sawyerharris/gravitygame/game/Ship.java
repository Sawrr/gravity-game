package com.sawyerharris.gravitygame.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.sawyerharris.gravitygame.screen.GameScreen;
import com.sawyerharris.gravitygame.screen.LevelEditScreen;
import com.sawyerharris.gravitygame.screen.LevelScreen;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen;

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
	
	/** Screen that contains this ship */
	private LevelScreen screen;
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
	/** Whether ship can be fired */
	private boolean flingable;
	/** Whether ship can be moved */
	private boolean pannable;
	/** Amount of boost remaining */
	private int boost;
	/** If the ship is using boost */
	private boolean boosting;

	public Ship(LevelScreen scrn) {
		sprite = new ShipSprite();
		reset();

		screen = scrn;
		if (screen instanceof LevelPlayScreen) {
			flingable = true;
			pannable = false;
		} else if (screen instanceof LevelEditScreen) {
			flingable = false;
			pannable = true;
		} else {
			throw new IllegalArgumentException(
					"Screen containing ship must be either LevelPlayScreen or LevelEditScreen");
		}

		addListener(new ActorGestureListener() {
			private static final float FLING_SCALAR = 0f;

			@Override
			public void fling(InputEvent event, float velX, float velY, int button) {
				if (flingable) {
					LevelPlayScreen lps = (LevelPlayScreen) screen;
					vel = new Vector2(velX, velY).scl(FLING_SCALAR);
					lps.fire();
				}
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (pannable) {
					Vector2 pos = new Vector2(x, y);
					setInitialPosition(pos);
					setPosition(pos);
				}
			}
		});
	}

	public void startBoosting() {
		sprite.setAnimation(GravityGame.getInstance().getAssets().getShipAnimation(style, "boost"));
		boosting = true;
	}

	public void stopBoosting() {
		sprite.setAnimation(GravityGame.getInstance().getAssets().getShipAnimation(style, "default"));
		boosting = false;
	}

	public void reset() {
		style = GravityGame.getInstance().getPlayerStatus().getShipStyle();
		setPosition(initialPosition);
		vel.set(0, 0);
		stopBoosting();
		boost = MAX_BOOST;
	}

	public void setFlingable(boolean fling) {
		flingable = fling;
	}

	public void setPannable(boolean pan) {
		pannable = pan;
	}

	/**
	 * Returns the ship's position.
	 * @return Vector2 of position
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	public void setPosition(Vector2 pos) {
		if (pos.x < 0 || pos.x > GameScreen.WORLD_WIDTH || pos.y < 0 || pos.y > GameScreen.WORLD_HEIGHT) {
			throw new IllegalArgumentException("Ship position out of bounds.");
		}
		setPosition(pos.x, pos.y);
		sprite.setCenter(pos.x, pos.y);
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth() * sprite.getScaleX(),
				sprite.getHeight() * sprite.getScaleY());
	}

	public Vector2 getInitialPosition() {
		return initialPosition;
	}

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

	private Vector2 computeGravityForce(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 force = new Vector2(0, 0);
		for (Planet planet : planets) {
			Vector2 r = new Vector2(pos);
			r.sub(planet.getPosition());
			float rcube = (float) Math.pow(r.len(), 3);
			force.add(r.scl(-planet.getMass() / rcube));
		}
		return force;
	}
	

	private Vector2 computeAccel(Vector2 shipPosition, ArrayList<Planet> planets) {
		Vector2 acc = computeGravityForce(shipPosition, planets);
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

	private class ShipSprite extends Sprite {
		private float time;
		private Animation animation;

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
