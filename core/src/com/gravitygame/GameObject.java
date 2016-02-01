package com.gravitygame;

import com.badlogic.gdx.math.Vector2;

public class GameObject {
	public Vector2 pos;

	public GameObject() {
		
	}
	
	public GameObject(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getDiffVector(GameObject obj) {
		Vector2 diffVector = new Vector2();
		diffVector.set(this.pos);
		diffVector.sub(obj.pos);
		return diffVector;
	}
}
