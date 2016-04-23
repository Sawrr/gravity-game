package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.sawyerharris.gravitygame.Level.PlanetObj;

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
	
	public GameScreen(Level level) {
		// TODO handle state
		
		this.level = level;
		this.theme = GravityGame.getThemes().get(level.getThemeName());
		
		viewport = new FillViewport(GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		camera = new GameCamera(level);
		camera.setToOrtho(false);
		viewport.setCamera(camera);
		
		stage = new Stage(viewport);
		
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(stage);
		mux.addProcessor(new ScrollProcessor());
		Gdx.input.setInputProcessor(mux);

		background = new Background(camera, level);
		
		planets = new ArrayList<Planet>();
		for (PlanetObj planet : level.getPlanets()) {
			planets.add(new Planet(planet.getPosition(), planet.getRadius(), theme));
		}

		ship = new Ship(level.getShipOrigin());
		
		stage.addActor(background);
		stage.addActor(ship);
		for (Planet planet : planets) {
			stage.addActor(planet);
		}
		
		Timer.schedule(new Task(){
			@Override
			public void run() {
				physicsUpdate();
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
			System.out.println("scrolled");
			// move the following to LevelEditorScreen
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 coords3d = viewport.getCamera().unproject(new Vector3(x, y, 0));
			Vector2 coords = new Vector2(coords3d.x, coords3d.y);
			for (Planet planet : planets) {
				Vector2 loc = new Vector2(planet.getPosition());
				System.out.println(loc.x + " " + loc.y + " | " + coords.x + " " + coords.y);
				if (loc.sub(coords).len2() <= Math.pow(planet.getRadius(),2)) {
					System.out.println("on planet");
				}
			}
			return true;
		}
	}
	
	/**
	 * Updates position and velocity of ship
	 */
	private void physicsUpdate() {
		ship.update(DELTA_TIME, planets);
	}
	
	/**
	 * Resets level to initial state
	 */
	public void reset() {
		ship.setPosition(ship.getInitialPosition());
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		// TODO Auto-generated method stub

	}

}
