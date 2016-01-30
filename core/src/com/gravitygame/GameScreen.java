package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private final GravityGame game;
	private int worldWidth;
	private int worldHeight;
	private OrthographicCamera camera;
	private Viewport viewport;
	private GameState state;

	Array<Mass> massArray = new Array<Mass>();
	Array<PhysicsObject> physArray = new Array<PhysicsObject>();
	Mass massOne = new Mass(new Vector2(200, 500), 30, 30);
	Mass massTwo = new Mass(new Vector2(400, 200), 50, 50);
	Ship myShip = new Ship(new Vector2(300, 200), new Vector2(10, 180), 10);

	private SpriteBatch spriteBatch = new SpriteBatch();
	private Texture background = new Texture("spacebg.jpg");
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int dragDeltaX;
	private int dragDeltaY;

	public GameScreen(GravityGame gam, int width, int height) {
		this.game = gam;
		this.worldWidth = width;
		this.worldHeight = height;

		this.state = GameState.VIEWING;

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new DesktopListener(this));
		inputMultiplexer.addProcessor(new GestureDetector(new TouchListener(this)));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		// Create mass and ship
		massArray.add(massOne);
		massArray.add(massTwo);
		physArray.add(myShip);

		camera = new OrthographicCamera();
		viewport = new FitViewport(game.screenWidth, game.screenHeight, camera);
		camera.setToOrtho(false, game.screenHeight, game.screenWidth);
	}

	public void checkForCollisions() {
		for (Mass mass : massArray) {
			float dist = myShip.getDiffVector(mass).len();
			if (dist <= myShip.radius + mass.radius) {
				System.out.println("Collision detected");
				System.exit(0);
			}
			if (myShip.pos.x <= 0 || myShip.pos.x >= worldWidth || myShip.pos.y <= 0 || myShip.pos.y >= worldHeight) {
				System.out.println("Ship out of bounds");
				System.exit(0);
			}
		}
	}

	public void pan(float x, float y, float deltaX, float deltaY) {
		switch (this.state) {
			case VIEWING:
				camera.translate(deltaX, deltaY);
				clampCamera();
				break;
			case AIMING:
				dragDeltaX += deltaX;
				dragDeltaY += deltaY;
				break;
			case FIRING:
				break;
		}
	}
	

	public void panStop(float x, float y) {
		switch (this.state) {
			case VIEWING:
				break;
			case AIMING:
				Vector2 dragVector = new Vector2(dragDeltaX, dragDeltaY);
				startFiring(dragVector);
				dragDeltaX = 0;
				dragDeltaY = 0;
				break;
			case FIRING:
				break;
		}
	}

	public void zoom(float zoomDistance) {
		switch (this.state) {
			case VIEWING:
				camera.zoom += zoomDistance;
				clampCamera();
				break;
			case AIMING:
				break;
			case FIRING:
				break;
		}
	}
	
	public void tap(Vector3 screenPos) {
		switch (this.state) {
			case VIEWING:
				startAiming();
				break;
			case AIMING:
				break;
			case FIRING:
				break;
		}
	}

	public void startAiming() {
		this.state = GameState.AIMING;
		camera.position.x = myShip.pos.x;
		camera.position.y = myShip.pos.y;
		camera.zoom = 1f;
		clampCamera();
	}
	
	public void startFiring(Vector2 dragVector) {
		this.state = GameState.FIRING;
		myShip.vel[0] = dragVector;
	}
	
	public void clampCamera() {
		camera.zoom = MathUtils.clamp(camera.zoom, 1f, worldWidth/camera.viewportWidth);
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, worldHeight - effectiveViewportHeight / 2f);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (this.state) {
			case VIEWING:
				break;
			case AIMING:
				break;
			case FIRING:
				for (PhysicsObject physObj : physArray) {
					physObj.update(delta, massArray);
				}
				checkForCollisions();
				camera.position.x = myShip.pos.x;
				camera.position.y = myShip.pos.y;
				camera.zoom = 1f;
				clampCamera();
				break;
		}

		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, worldWidth, worldHeight);
		spriteBatch.end();
		
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