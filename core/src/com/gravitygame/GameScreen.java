package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
	private final GravityGame game;
	private int worldWidth;
	private int worldHeight;
	private OrthographicCamera camera;
	private Viewport viewport;
	private GameState state;
	private GameMap gameMap = new GameMap();

	private SpriteBatch spriteBatch = new SpriteBatch();
	private Texture bgTexture;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int dragDeltaX;
	private int dragDeltaY;

	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	
	private Json json = new Json();
	
	public GameScreen(GravityGame game, String mapName) {
		this.game = game;

		state = GameState.VIEWING;
		
		inputMultiplexer.addProcessor(new DesktopListener(this));
		inputMultiplexer.addProcessor(new GestureDetector(new TouchListener(this)));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		FileHandle file = Gdx.files.internal("maps/" + mapName + ".txt");
		gameMap = json.fromJson(GameMap.class, file);
		
		// Extract world width, height
		worldWidth = gameMap.width;
		worldHeight = gameMap.height;
		
		// Create texture from background image name
		this.bgTexture = new Texture(gameMap.background);
				
		camera = new OrthographicCamera();
		viewport = new FitViewport(game.screenWidth, game.screenHeight, camera);
		camera.setToOrtho(false, worldWidth/2, worldHeight/2);
		clampCamera();
	}

	public void checkForCollisions() {
		for (Mass mass : gameMap.massArray) {
			float dist = gameMap.ship.getDiffVector(mass).len();
			if (dist <= gameMap.ship.radius + mass.radius) {
				System.out.println("Collision detected");
				game.create();
			}
			if (gameMap.ship.pos.x <= 0 || gameMap.ship.pos.x >= worldWidth || gameMap.ship.pos.y <= 0 || gameMap.ship.pos.y >= worldHeight) {
				System.out.println("Ship out of bounds");
				game.create();
			}
		}
	}

	public void pan(float x, float y, float deltaX, float deltaY) {
		switch (state) {
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
		switch (state) {
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
		switch (state) {
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
		switch (state) {
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
		state = GameState.AIMING;
		camera.position.x = gameMap.ship.pos.x;
		camera.position.y = gameMap.ship.pos.y;
		camera.zoom = 1f;
		clampCamera();
	}
	
	public void startFiring(Vector2 dragVector) {
		state = GameState.FIRING;
		gameMap.ship.vel[0] = dragVector;
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

		switch (state) {
			case VIEWING:
				break;
			case AIMING:
				break;
			case FIRING:
				gameMap.ship.update(delta, gameMap.massArray);
				checkForCollisions();
				camera.position.x = gameMap.ship.pos.x;
				camera.position.y = gameMap.ship.pos.y;
				camera.zoom = 1f;
				clampCamera();
				break;
		}

		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(bgTexture, 0, 0, worldWidth, worldHeight);
		spriteBatch.end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		// Draw masses
		shapeRenderer.setColor(1, 0, 0, 1);
		for (Mass mass : gameMap.massArray) {
			mass.draw(shapeRenderer);
		}

		// Draw ship
		shapeRenderer.setColor(0, 1, 0, 1);
		gameMap.ship.draw(shapeRenderer);
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
		shapeRenderer.dispose();
		spriteBatch.dispose();
		bgTexture.dispose();
	}
}