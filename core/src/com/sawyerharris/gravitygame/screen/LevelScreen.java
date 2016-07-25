package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A GameScreen that has an associated Level.
 *
 * @author Sawyer Harris
 *
 */
public abstract class LevelScreen extends GameScreen {
	private ArrayList<Planet> planets;
	private Ship ship;
	private Overlay overlay;
	private Level level;
	
	public LevelScreen(Batch batch) {
		super(batch);
		// TODO Auto-generated constructor stub
	}
	
	public loadLevel(Level level) {
		
	}
}
