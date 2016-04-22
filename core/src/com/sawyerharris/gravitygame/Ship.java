package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Spaceship
 * (mass = 1)
 * 
 * @author Sawyer Harris
 *
 */
public class Ship extends Actor {
	/** Gravitational Constant */
	private static final int G = 1;
	/** Characteristic radius of ship used for collisions */
	private static final int collisionRadius = 20;
	/** Characteristic radius of ship used for drag listener */
	private static final int dragRadius = 40;
	
	private Vector3 position;
	private Vector3 velocity;
	private float angle;
	
	private Texture texture;
	private Sprite sprite;
	
	public Ship(Vector3 position, Vector3 velocity) {
		this.position = position;
		this.velocity = velocity;
		
		texture = GravityGame.getTextures().get(AssetLoader.SHIP_IMG);
		if (texture == null) {
			System.out.println("Error: " + AssetLoader.SHIP_IMG + " not found");
			System.exit(1);
		}
		sprite = new Sprite(texture);
		
		setBounds(position.x - dragRadius, position.y - dragRadius, 2 * dragRadius, 2 * dragRadius);
		addListener(new DragListener() {
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				System.out.println("x: " + (x + getX()) + " y: " + (y + getY()));
			}
		});
	}
	
	/**
	 * Perform physics update on position and velocity 
	 */
	public void update(float delta, ArrayList<Planet> planets) {
		RK4(delta, planets);
	}
	
	/**
	 * Computes force of gravity on ship due to list of Planet objects
	 * @param planets list of Planet objects
	 * @return force
	 */
	public Vector3 computeGravityForce(Vector3 shipPosition, ArrayList<Planet> planets) {
		Vector3 force = new Vector3(0, 0, 0);
		for (Planet planet : planets) {
			Vector3 r = new Vector3(position);
			r.sub(planet.getPosition());
			float rcube = (float) Math.pow(r.len(), 3);
			force.add(r.scl(-G * planet.getMass() / rcube));
		}
		return force;
	}
	
	/**
	 * Updates position and velocity using Runge-Kutta 4th order method
	 */
	public void RK4(float delta, ArrayList<Planet> planets) {
		Vector3 vela = new Vector3(velocity).add(computeGravityForce(position, planets).scl(delta / 2));
		Vector3 posa = new Vector3(position).add(new Vector3(velocity).scl(delta / 2));
		
		Vector3 velb = new Vector3(velocity).add(computeGravityForce(posa, planets).scl(delta / 2));
		Vector3 posb = new Vector3(position).add(new Vector3(vela).scl(delta / 2));
		
		Vector3 velc = new Vector3(velocity).add(computeGravityForce(posb, planets).scl(delta));
		Vector3 posc = new Vector3(position).add(new Vector3(velb).scl(delta));
		
		Vector3 veld = new Vector3(velocity).add(computeGravityForce(posc, planets).scl(delta));
		Vector3 posd = new Vector3(position).add(new Vector3(velc).scl(delta));
		
		velocity = new Vector3(vela.add(velb.scl(2)).add(velc).add(veld.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector3(velocity).scl(1.0f/2));
		position = new Vector3(posa.add(posb.scl(2)).add(posc).add(posd.scl(1.0f/2)).scl(1.0f/3)).sub(new Vector3(position).scl(1.0f/2));
		
		angle = new Vector2(velocity.x, velocity.y).angle();
	}
	
	/**
	 * Returns position of ship
	 * @return position
	 */
	public Vector3 getPosition() {
		return position;
	}
	
	/**
	 * Returns velocity of ship
	 * @return velocity
	 */
	public Vector3 getVelocity() {
		return velocity;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		sprite.setCenter(position.x, position.y);
		sprite.setRotation(angle);
		sprite.draw(batch);
	}
}
