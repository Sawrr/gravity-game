package com.sawyerharris.gravitygame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class ShipGestureListener extends ActorGestureListener implements EventListener {

	private Ship ship;
	
	public ShipGestureListener(Ship ship) {
		super();
		this.ship = ship;		
	}
	
	@Override
	public void fling(InputEvent event, float velocityX, float velocityY, int button) {
		ship.fling(new Vector2(velocityX, velocityY));
	}
	
	@Override		
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		ship.pan(new Vector2(deltaX, deltaY));
	}
}
