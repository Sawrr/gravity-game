package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.sawyerharris.gravitygame.ui.BorderedItem;

/**
 * Menu screen holds many UI actors in its stage that provide menu
 * functionality.
 * 
 * @author Sawyer Harris
 *
 */
public class MenuScreen extends GameScreen {
	private ArrayList<BorderedItem> items;
	
	/*
	 * Private node class has name, Vector2 position, previous
	 * 
	 * Private moveToNode method sets the current node and calls the camera to move to the position
	 * when the back button is pressed, moveToNode will be called with the current node's previous as the destination unless previous is null
	 */
	
	
	public MenuScreen(Batch batch) {
		super(batch);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyDown(int keycode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
	}

	@Override
	public void zoom(float initialDistance, float distance) {
	}

	@Override
	public void tap(float x, float y, int count, int button) {
	}

	@Override
	public void scrolled(int amount) {
	}

	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
	}
	
	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
	}

}
