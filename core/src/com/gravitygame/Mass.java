package com.gravitygame;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Mass extends GameObject {
	public float radius;
	public float density;

	public Mass() {
		super();
	}
	
	public Mass(Vector2 pos, float radius, float density) {
		super(pos);
		this.radius = radius;
		this.density = density;
	}

	public float getMass() {
		float mass = (float) Math.PI * radius * radius * density;
		return mass;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(pos.x, pos.y, radius);
	}
}