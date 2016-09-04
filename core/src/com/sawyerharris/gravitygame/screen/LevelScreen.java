package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
	/** Singleton instance of game */
	private final GravityGame game = GravityGame.getInstance();

	/** Default theme to use */
	public static final Theme DEFAULT_THEME = GravityGame.getInstance().getThemes().getTheme("cone");

	/** Width, height of world */
	public static final int WORLD_WIDTH = 1280;
	public static final int WORLD_HEIGHT = 1920;

	/** Default location of ship origin */
	public static final Vector2 DEFAULT_SHIP_ORIGIN = new Vector2(0, 0);

	/** Planets, ship, overlay and level */
	private ArrayList<Planet> planets;
	private Ship ship;
	private Overlay overlay;
	private Level level;

	/**
	 * Constructs a level screen with the given batch and shape renderer.
	 * 
	 * @param batch
	 * @param renderer
	 */
	public LevelScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer, WORLD_WIDTH, WORLD_HEIGHT);
		getBackground().setTheme(DEFAULT_THEME);

		overlay = new Overlay(batch, renderer);
		getMux().addProcessor(0, overlay.getStage());

		ship = new Ship();

		planets = new ArrayList<Planet>();
	}

	/**
	 * Returns the level screen's overlay.
	 * 
	 * @return overlay
	 */
	public Overlay getOverlay() {
		return overlay;
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		overlay.draw();
	}

	/**
	 * Loads the contents of a level including the ship, planets, and
	 * background.
	 * 
	 * @param level
	 *            level to load
	 */
	public void loadLevel(Level level) {
		this.level = level;

		getStage().clear();

		Vector2 shipOrigin;
		ArrayList<PlanetMeta> planetList;
		Theme theme;

		if (level == null) {
			// No level provided, so a new custom level
			shipOrigin = new Vector2(DEFAULT_SHIP_ORIGIN);
			planetList = new ArrayList<PlanetMeta>();
			theme = DEFAULT_THEME;
		} else {
			// Load from provided level
			shipOrigin = level.getShipOrigin();
			planetList = level.getPlanets();
			theme = game.getThemes().getTheme(level.getTheme());
		}

		// Load ship
		ship.setPosition(shipOrigin);
		ship.setInitialPosition(shipOrigin);
		ship.reset();
		getStage().addActor(ship);

		// Load planets
		planets.clear();
		for (PlanetMeta meta : planetList) {
			Vector2 position = meta.getPosition();
			int radius = meta.getRadius();
			boolean home = meta.isHomePlanet();
			TextureRegion region = game.getAssets().getPlanet(game.getThemes().getTheme(level.getTheme()).getPlanet());
			Planet planet = new Planet(position, radius, region, home);
			planets.add(planet);
			getStage().addActor(planet);
		}

		// Set background
		getBackground().setTheme(theme);
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
