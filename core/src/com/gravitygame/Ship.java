package com.gravitygame;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Ship extends PhysicsObject {
	public float radius;
	public Boolean isBoosting = false;
	public float boost = 60;
	public float boostScalar;
	public GameScreen screen;
	public float angle;
		
	public Ship(Vector2 pos, Vector2 vel, float radius) {
		super(pos, vel);
		this.radius = radius;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(pos.x, pos.y, radius);
	}
	
	public void startBoost(float scalar) {
		boostScalar = scalar;
		if (boost > 0) {
			isBoosting = true;
			screen.game.boostSound.play();
		}
	}
	
	public void stopBoost() {
		isBoosting = false;
		screen.game.boostSound.stop();
	}
	
	@Override
	public void computeAccel(Array<Mass> massArray) {
		Vector2 newAcc = computeGravity(massArray);
		if (isBoosting) {
			if (boost > 0) {
				Vector2 boostVec = new Vector2(vel[0].x, vel[0].y);
				boostVec.nor();
				boostVec.scl(boostScalar);
				newAcc.add(boostVec);
				boost--;
			} else {
				stopBoost();
			}
		}
		this.acc[0].set(newAcc);
	}
}
