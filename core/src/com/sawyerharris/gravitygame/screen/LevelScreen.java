package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.Level.PlanetMeta;
import com.sawyerharris.gravitygame.game.Planet;
import com.sawyerharris.gravitygame.game.Ship;
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.ui.Overlay;

/**
 * A GameScreen that has an associated Level.
 *
 * @author Sawyer Harris
 *
 */
public abstract class LevelScreen extends GameScreen {
	private static final Theme DEFAULT_THEME = GravityGame.getInstance().getThemes().getTheme("default");

	private final GravityGame game = GravityGame.getInstance();

	/** Width, height of world */
	public static final int WORLD_WIDTH = 1280;
	public static final int WORLD_HEIGHT = 1920;

	private ArrayList<Planet> planets;
	private Ship ship;
	private Overlay overlay;
	private Level level;

	public LevelScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer, WORLD_WIDTH, WORLD_HEIGHT);
		getBackground().setTheme(DEFAULT_THEME);

		overlay = new Overlay(batch, renderer);
		getMux().addProcessor(overlay.getStage());
		
		
		ship = new Ship();
		getStage().addActor(ship);

		planets = new ArrayList<Planet>();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		overlay.draw();
	}

	public void loadLevel(Level level) {
		this.level = level;

		// Load ship
		ship.setPosition(level.getShipOrigin());
		ship.setInitialPosition(level.getShipOrigin());
		ship.reset();

		// Load planets
		planets.clear();
		for (PlanetMeta meta : level.getPlanets()) {
			Vector2 position = meta.getPosition();
			int radius = meta.getRadius();
			boolean home = meta.isHomePlanet();
			TextureRegion region = game.getAssets().getPlanet(game.getThemes().getTheme(level.getTheme()).getPlanet());
			Planet planet = new Planet(position, radius, region, home);
			planets.add(planet);
			getStage().addActor(planet);
		}
	}

	/**
	 * Returns the screen's current level.
	 * 
	 * @return level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Returns list of planet actors.
	 * 
	 * @return planets
	 */
	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	/**
	 * Returns ship actor.
	 * 
	 * @return ship
	 */
	public Ship getShip() {
		return ship;
	}
}
