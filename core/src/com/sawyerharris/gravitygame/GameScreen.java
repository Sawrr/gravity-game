package com.sawyerharris.gravitygame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;

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
	private Background background;
	private ArrayList<Planet> planets;
	private Ship ship;

	private FillViewport viewport;
	private GameCamera camera;
	
	public GameScreen() {
		viewport = new FillViewport(1024, 1024);
		camera = new GameCamera(GravityGame.getLevels().get("testLevel"));
		camera.setToOrtho(false, GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		viewport.setCamera(camera);
		
		stage = new Stage(viewport);
		
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(stage);
		mux.addProcessor(new ScrollProcessor());
		Gdx.input.setInputProcessor(mux);

		background = new Background(camera, GravityGame.getLevels().get("testLevel"));
		
		planets = new ArrayList<Planet>();
		planets.add(new Planet(new Vector2(1000, 750), 50, GravityGame.getThemes().get("testTheme")));
		
		ship = new Ship(new Vector2(500, 500), new Vector2(0, 150));
		
		stage.addActor(background);
		stage.addActor(ship);
		stage.addActor(planets.get(0));
		

		Timer.schedule(new Task(){
			@Override
			public void run() {
				physicsUpdate();
			}
		}, DELTA_TIME, DELTA_TIME);
	}
	
	private class ScrollProcessor extends InputAdapter {
		@Override
		public boolean scrolled(int amount) {
			System.out.println("scrolled");
			return true;
		}
	}
	
	private void physicsUpdate() {
		ship.update(DELTA_TIME, planets);
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
