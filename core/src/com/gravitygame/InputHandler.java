package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputHandler {	
	private static GameState state = GravityGame.state;
	private static Ship ship = GravityGame.ship;
	private static GameScreen screen = GravityGame.screen;
	
	public static Vector2 dragVector;
	
	public InputHandler() {
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(new DesktopListener());
		input.addProcessor(new GestureDetector(new TouchListener()));
		Gdx.input.setInputProcessor(input);
	}
	
	public static void pan(float x, float y, float deltaX, float deltaY) {
		switch (state) {
			case VIEWING:
				screen.pan(deltaX, deltaY);
				break;
			case AIMING:
				dragVector.x += deltaX;
				dragVector.y += deltaY;
				break;
			default:
				break;
		}
	}
	
	public static void panStop(float x, float y) {
		switch (state) {
			case VIEWING:
				break;
			case AIMING:
				startFiring(dragVector);
				dragVector.x = 0;
				dragVector.y = 1f;
				break;
			default:
				break;
		}
	}

	public static void zoom(float zoomDistance) {
		switch (state) {
			case VIEWING:
				screen.zoom(zoomDistance);
				break;
			default:
				break;
		}
	}
	
	public static void tap(Vector3 screenPos, int count) {
		switch (state) {
			case VIEWING:
				startAiming();
				break;
			case AIMING:
				startViewing();
				break;
			default:
				break;
		}
	}
	
	public static void touchDown() {
		switch (state) {
			case FIRING:
				ship.startBoost(200f);
			default:
				break;
		}
	}
	
	public static void touchUp() {
		switch (state) {
			case FIRING:
				ship.stopBoost();
			default:
				break;
		}
	}
}
