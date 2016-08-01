package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.Planet;
import com.sawyerharris.gravitygame.game.Ship;
import com.sawyerharris.gravitygame.ui.Overlay;

/**
 * A GameScreen that has an associated Level.
 *
 * @author Sawyer Harris
 *
 */
public abstract class LevelScreen extends GameScreen {
	/** Width, height of world */
	public static final int WORLD_WIDTH = 1280;
	public static final int WORLD_HEIGHT = 1920;
	
	private ArrayList<Planet> planets;
	private Ship ship;
	private Overlay overlay;
	private Level level;
	
	public LevelScreen(Batch batch, ShapeRenderer renderer, int worldWidth, int worldHeight) {
		super(batch, renderer, worldWidth, worldHeight);
		getBackground().setTheme(GravityGame.getInstance().getThemes().getTheme("test"));
	}
	
	public void loadLevel(Level level) {
		
	}
}
