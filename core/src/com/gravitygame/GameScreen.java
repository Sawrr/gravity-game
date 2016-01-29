package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

	public GameScreen(GravityGame gam, int width, int height) {
		this.game = gam;
		screenWidth = width;
		screenHeight = height;

		this.state = GameState.AIMING;

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new DesktopListener(this));
		inputMultiplexer.addProcessor(new GestureDetector(new TouchListener(this)));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
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

	public void pan(float x, float y) {
		camera.translate(x, y);
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, screenWidth - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, screenHeight - effectiveViewportHeight / 2f);
	}

	public void zoom(float zoomDistance) {
		camera.zoom += zoomDistance;
		System.out.println(camera.zoom);
		camera.zoom = MathUtils.clamp(camera.zoom, 1f, screenWidth/camera.viewportWidth);
	}
	
	public void tap(Vector3 screenPos) {
		camera.unproject(screenPos);
		System.out.println(screenPos.x + ", " + screenPos.y);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (this.state) {
		case AIMING:
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