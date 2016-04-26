package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
	
	private GravityGame game;
	private Stage stage;
	private Level level;
	private Theme theme;
	private Background background;
	private ArrayList<Planet> planets;
	private Ship ship;

	private ScreenViewport viewport;
	private GameCamera camera;
	
	private boolean viewCenterOnShip;
	private Vector2 moveTarget;
	private float zoomTarget;
	
	private Task physicsTask;
	
	/**
	 * Constructor creates GameScreen for a given Level
	 * @param level
	 */
	public GameScreen(GravityGame gam, Level lvl) {		
		game = gam;
		level = lvl;
		theme = GravityGame.getThemes().get(level.getThemeName());
		
		// Viewport, camera, stage
		viewCenterOnShip = true;
		viewport = new ScreenViewport();
		camera = new GameCamera(level);
		camera.setToOrtho(false);
		viewport.setCamera(camera);
		stage = new Stage(viewport);
		
		// Input processing
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(stage);
		InputHandler inp = new InputHandler(this, camera);
		mux.addProcessor(inp.getGestureDetector());
		mux.addProcessor(inp.getButtonProcessor());
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
		
		physicsTask = new Task(){
			@Override
			public void run() {
				if (GravityGame.getState() == GameState.FIRING) {
					physicsUpdate();
				}
			}
		};
		
		Timer.schedule(physicsTask, DELTA_TIME, DELTA_TIME);
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
		// Check for planet collisions
		for (Planet planet : planets) {
			float dist = new Vector2(ship.getPosition()).sub(planet.getPosition()).len();
			if (dist <= Ship.COLLISION_RADIUS + planet.getRadius()) {
				if (planet.isHome()) {
					// TODO success and then reset
					game.nextLevel();
					return;
				} else {
					// TODO explosion and then reset
					reset();
					return;
				}
			}
		}
	
		// Check boundaries of level
		float x, y, width, height;
		x = ship.getPosition().x;
		y = ship.getPosition().y;
		width = level.getWidth();
		height = level.getHeight();
		
		if (x <= 0 || x > width || y <= 0 || y > height) {
			reset();
			return;
		}
		
		float cameraTopEdgeX = camera.position.x + camera.zoom * (camera.viewportWidth / 2 - camera.SHIP_OFFSCREEN_BUFFER);
		float cameraBotEdgeX = camera.position.x - camera.zoom * (camera.viewportWidth / 2 - camera.SHIP_OFFSCREEN_BUFFER);
		float cameraTopEdgeY = camera.position.y + camera.zoom * (camera.viewportHeight / 2 - camera.SHIP_OFFSCREEN_BUFFER);
		float cameraBotEdgeY = camera.position.y - camera.zoom * (camera.viewportHeight / 2 - camera.SHIP_OFFSCREEN_BUFFER);
		
		if (x > cameraTopEdgeX || x < cameraBotEdgeX || y > cameraTopEdgeY || y < cameraBotEdgeY) {
			camera.zoom(0.000005f * ship.getVelocity().len());
		}
	}

	
	/**
	 * Resets level to initial state
	 */
	public void reset() {
		ship.reset();
		setStateAiming();
	}
	
	/**
	 * Returns the screen's ship
	 * @return ship
	 */
	public Ship getShip() {
		return ship;
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
		//viewport.update(width, height);
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
		physicsTask.cancel();
		stage.dispose();		
	}

}
