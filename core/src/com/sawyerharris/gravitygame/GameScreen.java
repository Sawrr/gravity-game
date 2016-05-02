package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
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
 * @author Sawyer Harris
 *
 */
public class GameScreen implements Screen {
	/** Time between physics updates */
	public static final float DELTA_TIME = 0.001f;
	/** Vertical offset for centering on ship */
	private static final Vector2 SHIP_VIEW_OFFSET = new Vector2(0, 350);
	
	private GravityGame game;
	private Stage stage;
	private Level level;
	private Theme theme;
	private Background background;
	private ArrayList<Planet> planets;
	private Ship ship;
	
	private Overlay overlay;
	private boolean showHeader;
	private float bannerAlpha;
	private float textAlpha;
	private Task fadeInTask;
	private Task fadeOutTask;
	private Task headerShowTask;

	private FillViewport viewport;
	private GameCamera camera;
	
	private boolean viewCenterOnShip;
	private Vector2 moveTarget;
	private float zoomTarget;
	
	private Task physicsTask;
	
	private final Vector2 worldMoveTarget;
	private Vector2 lastCamPosition;
	private float lastCamZoom;
	
	/**
	 * Constructor creates GameScreen for a given Level
	 * @param level
	 */
	public GameScreen(GravityGame gam, Level lvl) {		
		game = gam;
		level = lvl;
		theme = GravityGame.getThemes().get(level.getThemeName());
		
		worldMoveTarget = new Vector2(level.getWidth() / 2, level.getHeight() / 2);
		
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
		InputHandler inp = new InputHandler(this, camera);
		mux.addProcessor(inp.getGestureDetector());
		mux.addProcessor(inp.getButtonProcessor());
		Gdx.input.setInputProcessor(mux);

		// Create background, planets, ship and add to stage
		background = new Background(this, camera, level);
		planets = new ArrayList<Planet>();
		for (PlanetMeta planet : level.getPlanets()) {
			planets.add(new Planet(planet, theme, this));
		}
		ship = new Ship(this, level.getShipOrigin());
		stage.addActor(background);		
		for (Planet planet : planets) {
			stage.addActor(planet);
		}
		stage.addActor(ship);
		
		/* Sets camera zoom and position to entire level,
		 * then sets mode to aiming which centers view on ship
		 */
		camera.position.set(worldMoveTarget.x, worldMoveTarget.y, 0);
		camera.zoom = camera.worldZoomLevel;
		lastCamPosition = new Vector2(ship.getPosition()).add(SHIP_VIEW_OFFSET);
		lastCamZoom = GameCamera.SHIP_ZOOM_LEVEL;
		setStateAiming();
		
		overlay = new Overlay(level, ship);
		showHeader = true;
		
		fadeInTask = new Task(){
			@Override
			public void run() {
				fadeInHeader();
			}
		};
		
		Timer.schedule(fadeInTask, DELTA_TIME, DELTA_TIME);
		
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
	
	/**
	 * Player is aiming ship
	 */
	public void setStateAiming() {
		GravityGame.setState(GameState.AIMING);
		ship.setTouchable(Touchable.enabled);
		zoomTarget = lastCamZoom;
		moveTarget = lastCamPosition;
		camera.setAutoMoving(true);
	}
	
	/**
	 * Ship has been fired
	 * @param fireVector
	 */
	public void setStateFiring(Vector2 fireVector) {
		ship.setVelocity(fireVector);
		GravityGame.setState(GameState.FIRING);
		ship.setTouchable(Touchable.disabled);
		
		lastCamZoom = camera.zoom;
		lastCamPosition = new Vector2(camera.position.x, camera.position.y);
		
		zoomTarget = 0;
		// moveTarget is set in render method
		camera.setAutoMoving(true);
	}
	
	/**
	 * Toggle between centering camera on ship and world
	 * Triggers a camera auto move
	 */
	public void toggleShipWorldAutoMove() {
		viewCenterOnShip = !viewCenterOnShip;
		if (viewCenterOnShip) {
			zoomTarget = GameCamera.SHIP_ZOOM_LEVEL;
			moveTarget = new Vector2(ship.getPosition()).add(SHIP_VIEW_OFFSET);
		} else {
			zoomTarget = camera.worldZoomLevel;
			moveTarget = new Vector2(level.getWidth() / 2, level.getHeight() / 2);
		}
		camera.setAutoMoving(true);
	}
	
	/**
	 * Updates position and velocity of ship
	 */
	private void physicsUpdate() {
		ship.update(DELTA_TIME, planets);
		checkForCollisions();
		if (!camera.isAutoMoving()) {
			checkShipPosition();
		}
	}
	
	/**
	 * Checks if the ship has collided or gone out of bounds
	 */
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
	}

	/**
	 * Checks whether ship position is almost off screen
	 * If so the camera must adjust
	 */
	private void checkShipPosition() {
		float x = ship.getPosition().x;
		float y = ship.getPosition().y;
		
		float cameraTopEdgeX = camera.position.x + camera.zoom * (camera.viewportWidth / 2 - GameCamera.SHIP_OFFSCREEN_BUFFER);
		float cameraBotEdgeX = camera.position.x - camera.zoom * (camera.viewportWidth / 2 - GameCamera.SHIP_OFFSCREEN_BUFFER);
		float cameraTopEdgeY = camera.position.y + camera.zoom * (camera.viewportHeight / 2 - GameCamera.SHIP_OFFSCREEN_BUFFER);
		float cameraBotEdgeY = camera.position.y - camera.zoom * (camera.viewportHeight / 2 - GameCamera.SHIP_OFFSCREEN_BUFFER);
		
		if (x > cameraTopEdgeX || x < cameraBotEdgeX || y > cameraTopEdgeY || y < cameraBotEdgeY) {
			camera.zoom(0.000001f * ship.getVelocity().len());
			if (x > cameraTopEdgeX) {
				camera.pan(x - cameraTopEdgeX, 0);
			} else if (x < cameraBotEdgeX) {
				camera.pan(x - cameraBotEdgeX, 0);
			}
			if (y > cameraTopEdgeY) {
				camera.pan(0, y - cameraTopEdgeY);
			} else if (y < cameraBotEdgeY) {
				camera.pan(0, y - cameraBotEdgeY);
			}
		}
	}
	
	/**
	 * Resets level to initial state
	 */
	public void reset() {
		ship.reset();
		setStateAiming();
	}
	
	public void fadeInHeader() {
		float ratio = Overlay.BANNER_MAX_ALPHA / Overlay.TEXT_MAX_ALPHA;
		float inc = Overlay.ALPHA_INCREMENT;
		textAlpha += inc;
		bannerAlpha += inc * ratio;
		
		if (textAlpha >= Overlay.TEXT_MAX_ALPHA || bannerAlpha >= Overlay.BANNER_MAX_ALPHA) {
			textAlpha = Overlay.TEXT_MAX_ALPHA;
			bannerAlpha = Overlay.BANNER_MAX_ALPHA;
			fadeInTask.cancel();
			
			if (headerShowTask == null) {
				headerShowTask = new Task() {
					@Override
					public void run() {
						fadeOutTask = new Task() {
							@Override
							public void run() {
								fadeOutHeader();
							}
						};
						Timer.schedule(fadeOutTask, DELTA_TIME, DELTA_TIME);
					}
				};
				Timer.schedule(headerShowTask, Overlay.HEADER_SHOW_TIME);
			}
		}
	}
	
	public void fadeOutHeader() {
		float ratio = Overlay.BANNER_MAX_ALPHA / Overlay.TEXT_MAX_ALPHA;
		float inc = Overlay.ALPHA_INCREMENT;
		textAlpha -= inc;
		bannerAlpha -= inc * ratio;
		
		if (textAlpha < 0f || bannerAlpha < 0f) {
			textAlpha = 0f;
			bannerAlpha = 0f;
			fadeOutTask.cancel();
			showHeader = false;
		}
	}
	
	/**
	 * Returns the screen's ship
	 * @return ship
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * Returns the camera
	 * @return camera
	 */
	public GameCamera getCamera() {
		return camera;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (camera.isAutoMoving()) {
			if (GravityGame.getState() == GameState.FIRING) {
				moveTarget = new Vector2(ship.getPosition()).add(SHIP_VIEW_OFFSET);
			}
			camera.autoMove(moveTarget, zoomTarget);
		}
		
		stage.draw();
		overlay.drawBoostBar();
		if (showHeader) {
			overlay.drawLevelHeader(bannerAlpha, textAlpha);
		}
	}

	/**
	 * Called when window is resized
	 * Game is not resizable so only called once
	 * during initialization
	 */
	@Override
	public void resize(int width, int height) {
		if (Gdx.app.getType() == ApplicationType.Desktop)
			viewport.update(width , height);
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
		if (fadeInTask != null) {
			fadeInTask.cancel();	
		}
		if (fadeOutTask != null) {
			fadeOutTask.cancel();	
		}
		if (headerShowTask != null) {
			headerShowTask.cancel();	
		}
		stage.dispose();
	}

}
