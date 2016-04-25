package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.sawyerharris.gravitygame.GravityGame.GameState;
import com.sawyerharris.gravitygame.Level.PlanetMeta;

/**
 * Game screen for when the game is being played
 * 
 * @author Sawyer
 *
 */
public class GameScreen implements Screen {
	/** Time between physics updates */
	public static final float DELTA_TIME = 0.001f;
	
	private Stage stage;
	private Level level;
	private Theme theme;
	private Background background;
	private ArrayList<Planet> planets;
	private Ship ship;

	private FillViewport viewport;
	private GameCamera camera;
	
	private boolean viewCenterOnShip;
	private Vector2 moveTarget;
	private float zoomTarget;
	
	/**
	 * Constructor creates GameScreen for a given Level
	 * @param level
	 */
	public GameScreen(Level lvl) {		
		level = lvl;
		theme = GravityGame.getThemes().get(level.getThemeName());
		
		// Viewport, camera, stage
		viewCenterOnShip = true;
		viewport = new FillViewport(GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		camera = new GameCamera(level);
		camera.setToOrtho(false);
		viewport.setCamera(camera);
		stage = new Stage(viewport);
		
		// Input processing
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(stage);
		mux.addProcessor(new ScrollProcessor());
		mux.addProcessor(new GestureDetector(new InputHandler(this, camera)));
		Gdx.input.setInputProcessor(mux);

		// Create background, planets, ship and add to stage
		background = new Background(camera, level);
		planets = new ArrayList<Planet>();
		for (PlanetMeta planet : level.getPlanets()) {
			planets.add(new Planet(planet, theme));
		}
		ship = new Ship(this, level.getShipOrigin());
		stage.addActor(background);		
		for (Planet planet : planets) {
			stage.addActor(planet);
		}
		stage.addActor(ship);
		
		// Starts in aiming mode
		setStateAiming();
		
		Timer.schedule(new Task(){
			@Override
			public void run() {
				if (GravityGame.getState() == GameState.FIRING) {
					physicsUpdate();
				}
			}
		}, DELTA_TIME, DELTA_TIME);
	}
	
	/**
	 * Used to detect mouse wheel scrolling
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class ScrollProcessor extends InputAdapter {
		@Override
		public boolean scrolled(int amount) {
			if (GravityGame.getState() == GameState.VIEW_MOVING) {
				setStateAiming();
			}
			camera.zoom(GameCamera.SCROLL_TO_ZOOM * amount);
			
			// move the following to LevelEditorScreen
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 coords3d = viewport.getCamera().unproject(new Vector3(x, y, 0));
			Vector2 coords = new Vector2(coords3d.x, coords3d.y);
			for (Planet planet : planets) {
				Vector2 loc = new Vector2(planet.getPosition());
				if (loc.sub(coords).len2() <= Math.pow(planet.getRadius(),2)) {
					System.out.println("on planet");
				}
			}
			return true;
		}
	}
	
	public void setStateAiming() {
		GravityGame.setState(GameState.AIMING);
		ship.setTouchable(Touchable.enabled);
	}
	
	public void setStateFiring(Vector2 fireVector) {
		ship.setVelocity(fireVector);
		GravityGame.setState(GameState.FIRING);
		ship.setTouchable(Touchable.disabled);
	}
	
	public void setStateViewMoving() {
		GravityGame.setState(GameState.VIEW_MOVING);
		viewCenterOnShip = !viewCenterOnShip;
		if (viewCenterOnShip) {
			zoomTarget = GameCamera.SHIP_ZOOM_LEVEL;
			moveTarget = ship.getPosition();
		} else {
			zoomTarget = camera.worldZoomLevel;
			moveTarget = new Vector2(level.getWidth() / 2, level.getHeight() / 2);
		}
	}
	
	/**
	 * Updates position and velocity of ship
	 */
	private void physicsUpdate() {
		ship.update(DELTA_TIME, planets);
		checkForCollisions();
	}
	
	private void checkForCollisions() {
		for (Planet planet : planets) {
			float dist = new Vector2(ship.getPosition()).sub(planet.getPosition()).len();
			if (dist <= Ship.COLLISION_RADIUS + planet.getRadius()) {
				if (planet.isHome()) {
					// TODO make this right
					
				} else {
					// TODO make this right
					reset();
				}
			}
		}
	}
	
	/**
	 * Resets level to initial state
	 */
	public void reset() {
		ship.setPosition(ship.getInitialPosition());
		setStateAiming();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (GravityGame.getState() == GameState.VIEW_MOVING) {
			boolean finished = camera.autoMove(moveTarget, zoomTarget);
			if (finished) {
				setStateAiming();
			}
		}
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height);
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
