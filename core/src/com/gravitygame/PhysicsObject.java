package com.gravitygame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PhysicsObject extends GameObject {
	private static final int NUM_STEPS = 4;

	public Vector2[] vel = new Vector2[NUM_STEPS];
	public Vector2[] acc = new Vector2[NUM_STEPS];

	private static final float G = 5;

	public PhysicsObject() {
	
	}
	
	public PhysicsObject(Vector2 pos, Vector2 vel) {
		super(pos);
		for (int i = 0; i < NUM_STEPS; i++) {
			this.vel[i] = vel;
			this.acc[i] = new Vector2(0, 0);
		}
	}

	public Vector2 computeGravity(Array<Mass> massArray) {
		Vector2 grav = new Vector2(0, 0);
		for (Mass mass : massArray) {
			Vector2 diff = this.getDiffVector(mass);
			float r = diff.len();
			float rcube = (float) Math.pow(r, 3);
			grav.add(diff.scl(-G * mass.getMass() / rcube));
		}
		return grav;
	}

	public void computeAccel(Array<Mass> massArray) {
		this.acc[0].set(computeGravity(massArray));
	}

	public void adamsBashforth4(float delta, Vector2[] vectors, Vector2 updateVector) {
		Vector2 v1 = new Vector2();
		v1.set(vectors[0]);
		v1.scl(delta * 55 / 24);

		Vector2 v2 = new Vector2();
		v2.set(vectors[1]);
		v2.scl(delta * 59 / 24);

		Vector2 v3 = new Vector2();
		v3.set(vectors[2]);
		v3.scl(delta * 37 / 24);

		Vector2 v4 = new Vector2();
		v4.set(vectors[3]);
		v4.scl(delta * 9 / 24);

		updateVector.add(v1);
		updateVector.sub(v2);
		updateVector.add(v3);
		updateVector.sub(v4);
	}

	public void eulerMethod(float delta, Vector2 abcvector, Vector2 updateVector) {
		Vector2 v1 = new Vector2();
		v1.set(abcvector);
		v1.scl(delta);
		updateVector.add(v1);
	}

	public void update(float delta, Array<Mass> massArray) {
		computeAccel(massArray);

		// Update position
		adamsBashforth4(delta, this.vel, this.pos);

		// Update velocity
		adamsBashforth4(delta, this.acc, this.vel[0]);

		// Shift old values
		for (int i = 1; i < NUM_STEPS; i++) {
			this.vel[i].set(this.vel[i - 1]);
			this.acc[i].set(this.acc[i - 1]);
		}
	}

}
