package com.sawyerharris.gravitygame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * Gesture Listener for planets in Level Editor
 * 
 * @author Sawyer Harris
 *
 */
public class PlanetGestureListener extends ActorGestureListener {
	private Planet planet;
	
	public PlanetGestureListener(Planet planet, float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay) {
		super(halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay);
		this.planet = planet;
	}
	
	@Override
	public void zoom(InputEvent event, float initialDistance, float distance) {
		planet.changeRadiusBy((int) ((distance - initialDistance) / 100));
		planet.updateBounds();
	}
	
	@Override		
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		planet.getPosition().add(new Vector2(deltaX, deltaY));
		planet.updateBounds();
	}
		
	@Override
	public void tap(InputEvent event, float x, float y, int pointer, int button) {
		if (planet.isHome()) {
			planet.remove();
		} else {
			planet.setHome(true);
		}					
	}
}
