package com.gravitygame;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Ship extends PhysicsObject {
	public float radius;

	public Ship(Vector2 pos, Vector2 vel, float radius) {
		super(pos, vel);
		this.radius = radius;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(pos.x, pos.y, radius);
	}
}
