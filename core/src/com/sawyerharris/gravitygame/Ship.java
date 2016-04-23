package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Spaceship
 * (mass, G = 1)
 * 
 * @author Sawyer Harris
 *
 */
public class Ship extends Actor {
	/** Characteristic radius of ship used for collisions */
	private static final int collisionRadius = 20;
	/** Characteristic radius of ship used for drag listener */
	private static final int dragRadius = 40;
	
	private Vector2 initialPosition;
	private Vector2 position;
	private Vector2 velocity;
	private float angle;
	
	private Texture texture;
	private Sprite sprite;
	
	public Ship(Vector2 initialPosition, Vector2 velocity) {
		this.initialPosition = initialPosition;
		this.position = initialPosition;
		this.velocity = velocity;
		setBounds(position.x - dragRadius, position.y - dragRadius, 2 * dragRadius, 2 * dragRadius);
		
		texture = GravityGame.getTextures().get(AssetLoader.SHIP_IMG);
		if (texture == null) {
			System.out.println("Error: " + AssetLoader.SHIP_IMG + " not found");
			System.exit(1);
		}
		sprite = new Sprite(texture);
		
		addListener(new DragListener() {
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				System.out.println("x: " + (x + getX()) + " y: " + (y + getY()));
			}
		});
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
	 * Updates position and velocity using Runge-Kutta 4th order method
	 */
	public void RK4(float dt, ArrayList<Planet> planets) {
		Vector2 vela = new Vector2(velocity).add(computeGravityForce(position, planets).scl(dt / 2));
		Vector2 posa = new Vector2(position).add(new Vector2(velocity).scl(dt / 2));
		
		Vector2 velb = new Vector2(velocity).add(computeGravityForce(posa, planets).scl(dt / 2));
		Vector2 posb = new Vector2(position).add(new Vector2(vela).scl(dt / 2));
		
		Vector2 velc = new Vector2(velocity).add(computeGravityForce(posb, planets).scl(dt));
		Vector2 posc = new Vector2(position).add(new Vector2(velb).scl(dt));
		
		Vector2 veld = new Vector2(velocity).add(computeGravityForce(posc, planets).scl(dt));
		Vector2 posd = new Vector2(position).add(new Vector2(velc).scl(dt));
		
		velocity = new Vector2(vela.add(velb.scl(2)).add(velc).add(veld.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector2(velocity).scl(1.0f/2));
		position = new Vector2(posa.add(posb.scl(2)).add(posc).add(posd.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector2(position).scl(1.0f/2));
		
		angle = velocity.angle();
	}
	
	/**
	 * Returns position of ship
	 * @return position
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * Returns velocity of ship
	 * @return velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(angle);
		sprite.draw(batch);
	}
}
