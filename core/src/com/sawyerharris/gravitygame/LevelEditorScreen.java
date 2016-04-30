package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.sawyerharris.gravitygame.Level.PlanetMeta;

/**
 * Screen for Level Editor
 * All custom levels have the same width and height
 * 
 * @author Sawyer Harris
 *
 */
public class LevelEditorScreen implements Screen {

	public static final int CUSTOM_LEVEL_WIDTH = 1440;
	public static final int CUSTOM_LEVEL_HEIGHT = 2560;
	public static final Theme DEFAULT_THEME = GravityGame.getThemes().get("testTheme");
	public static final Vector2 DEFAULT_SHIP_ORIGIN = new Vector2(CUSTOM_LEVEL_WIDTH / 2, 500);
	private static final int SCROLL_TO_RADIUS_CHANGE = 5;
	
	private GravityGame game;
	private Level level;
	private Stage stage;
	
	private Theme theme;
	private Background background;
	private Ship ship;
	
	private FillViewport viewport;
	private GameCamera camera;
	
	public LevelEditorScreen(GravityGame gam, Level lvl) {
		game = gam;
		
		if (lvl != null) {
			level = lvl;
		} else {
			level = Level.DEFAULT_LEVEL;
		}
		
		// Viewport, camera, stage
		viewport = new FillViewport(GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		camera = new GameCamera(Level.DEFAULT_LEVEL);
		camera.setToOrtho(false);
		viewport.setCamera(camera);
		stage = new Stage(viewport);
		
		// Load level data
		theme = GravityGame.getThemes().get(level.getThemeName());
		background = new Background(this, camera, level);
		ship = new Ship(this, level.getShipOrigin());
		
		camera.zoom = camera.worldZoomLevel;
		camera.position.set(level.getWidth() / 2, level.getHeight() / 2, 0);
		
		stage.addActor(background);
		for (PlanetMeta planet : level.getPlanets()) {
			stage.addActor(new Planet(planet, theme, this));
		}
		stage.addActor(ship);
		
		// Input processing
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(stage);
		InputHandler inp = new InputHandler(this, camera);
		mux.addProcessor(inp.getGestureDetector());
		mux.addProcessor(inp.getButtonProcessor());
		Gdx.input.setInputProcessor(mux);
	}
	
	/**
	 * Adds planet to the level editor screen at position
	 * @param position
	 */
	public void addPlanet(Vector2 position) {
		Planet planet = new Planet(position, DEFAULT_THEME, this);
		stage.addActor(planet);
	}
	
	/**
	 * Checks for mouse over planet when scrolling
	 * @param amount scroll amount
	 * @return whether mouse is over planet
	 */
	public boolean scrollCheckForPlanet(int amount) {
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		Vector3 coords3d = camera.unproject(new Vector3(x, y, 0));
		Vector2 coords = new Vector2(coords3d.x, coords3d.y);
		for (Actor actor : stage.getActors()) {
			if (actor instanceof Planet) {
				Planet planet = (Planet) actor;
				Vector2 loc = new Vector2(planet.getPosition());
				if (loc.sub(coords).len2() <= Math.pow(planet.getRadius(),2)) {
					planet.changeRadiusBy(amount * SCROLL_TO_RADIUS_CHANGE);
					return true;
				}
			}
		}
		// No planet detected
		camera.zoom(amount * GameCamera.SCROLL_TO_ZOOM);
		return false;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
