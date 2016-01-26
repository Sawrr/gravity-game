package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
	final GravityGame game;
	int screenWidth;
	int screenHeight;
	OrthographicCamera camera;
	Viewport viewport;
	GameState state;

	Array<Mass> massArray = new Array<Mass>();
	Array<PhysicsObject> physArray = new Array<PhysicsObject>();
	Mass massOne = new Mass(new Vector2(200, 200), 50, 80);
	Mass massTwo = new Mass(new Vector2(400, 200), 50, 0);
	Ship myShip = new Ship(new Vector2(300, 200), new Vector2(10, 180), 10);
	
	ShapeRenderer shapeRenderer = new ShapeRenderer();

	Boolean touch = false;
	int touchCount = 0;
	
	public GameScreen(GravityGame gam, int width, int height) {
		this.game = gam;
		screenWidth = width;
		screenHeight = height;
		
		this.state = GameState.AIMING;
		
		// Create mass and ship
		massArray.add(massOne);
		massArray.add(massTwo);
		physArray.add(myShip);

		camera = new OrthographicCamera();
		viewport = new FitViewport(640, 480, camera);
		camera.setToOrtho(false, screenHeight, screenWidth);
	}

	public void checkForCollisions() {
		for (Mass mass : massArray) {
			float dist = myShip.getDiffVector(mass).len();
			if (dist <= myShip.radius + mass.radius) {
				System.out.println("Collision detected");
				System.exit(0);
			}
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (this.state) {
			case AIMING:
				if (Gdx.input.isTouched() && !touch) {
					touch = true;
					System.out.println("started touch");
				} else if (!Gdx.input.isTouched() && touch) {
					touch = false;
					System.out.println("ended touch");
					touchCount++;
				}
				if (touchCount > 5) {
					this.state = GameState.FIRING;
				}
				break;
			case FIRING:
				for (PhysicsObject physObj : physArray) {
					physObj.update(delta, massArray);
				}
				checkForCollisions();
				break;
		}

		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		// Draw masses
		shapeRenderer.setColor(1, 0, 0, 1);
		for (Mass mass : massArray) {
			mass.draw(shapeRenderer);
		}

		// Draw ship
		shapeRenderer.setColor(0, 1, 0, 1);
		myShip.draw(shapeRenderer);
		shapeRenderer.end();

	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}