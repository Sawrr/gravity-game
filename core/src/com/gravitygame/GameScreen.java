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
	private GameMap gameMap;

	private SpriteBatch spriteBatch = new SpriteBatch();
	private Texture bgTexture;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int dragDeltaX;
	private int dragDeltaY;
	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();

	private String mapName;
	private int mapId;
	
	public GameScreen(GravityGame game, String mapName, int mapId) {
		this.game = game;
		this.mapName = mapName;
		this.mapId = mapId;

		state = GameState.VIEWING;
		
		inputMultiplexer.addProcessor(new DesktopListener(this));
		inputMultiplexer.addProcessor(new GestureDetector(new TouchListener(this)));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		FileHandle file = Gdx.files.internal("maps/" + mapName);
		Json json = new Json();
		gameMap = json.fromJson(GameMap.class, file);
		
		// Extract world width, height
		worldWidth = gameMap.width;
		worldHeight = gameMap.height;
		
		// Set Ship screen
		gameMap.ship.screen = this;
		
		// Create texture from background image name
		bgTexture = new Texture(gameMap.background);
				
		camera = new OrthographicCamera();
		viewport = new FitViewport(game.screenWidth, game.screenHeight, camera);
		camera.setToOrtho(false);
		clampCamera();
	}

	////////////////////////////////
	// Handle mouse / touch input //
	////////////////////////////////
	
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
			default:
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
			default:
				break;
		}
	}

	public void zoom(float zoomDistance) {
		switch (state) {
			case VIEWING:
				camera.zoom += zoomDistance;
				clampCamera();
				break;
			default:
				break;
		}
	}
	
	public void tap(Vector3 screenPos) {
		switch (state) {
			case VIEWING:
				startAiming();
				break;
			default:
				break;
		}
	}
	
	public void touchDown() {
		switch (state) {
			case FIRING:
				gameMap.ship.startBoost(200f);
			default:
				break;
		}
	}
	
	public void touchUp() {
		switch (state) {
			case FIRING:
				gameMap.ship.stopBoost();
			default:
				break;
		}
	}

	////////////////////////////////////////
	//         Everything else            //
	////////////////////////////////////////
	
	public void checkForCollisions() {
		for (Mass mass : gameMap.massArray) {
			float dist = gameMap.ship.getDiffVector(mass).len();
			if (dist <= gameMap.ship.radius + mass.radius) {
				if (mass instanceof Goal) {
					nextMap();
				} else {
					reset();	
				}
			}
			if (gameMap.ship.pos.x <= 0 || gameMap.ship.pos.x >= worldWidth || gameMap.ship.pos.y <= 0 || gameMap.ship.pos.y >= worldHeight) {
				reset();
			}
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
	
	public void reset() {
		dispose();
		game.setScreen(new GameScreen(game, mapName, mapId));		
	}
	
	public void nextMap() {
		dispose();
		int nextMapId = mapId + 1;
		if (game.mapNameArray.size != nextMapId){
			String nextMapName = game.mapNameArray.get(nextMapId);
			game.setScreen(new GameScreen(game, nextMapName, nextMapId));
		} else {
			System.out.println("You have won the game.");
			System.exit(0);
		}
	}
	
	////////////////////////////////////////
	//         libgdx methods             //
	////////////////////////////////////////
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (state) {
			case FIRING:
				gameMap.ship.update(delta, gameMap.massArray);
				checkForCollisions();
				camera.position.x = gameMap.ship.pos.x;
				camera.position.y = gameMap.ship.pos.y;
				camera.zoom = 1f;
				clampCamera();
				break;
			default:
				break;
		}

		camera.update();
		
		float aspectRatio = (float) bgTexture.getHeight() / bgTexture.getWidth();
		float paraScalar = 0.8f;
		float paraX = (camera.position.x - game.screenWidth/2 * camera.zoom) * paraScalar;
		float paraY = (camera.position.y - game.screenHeight/2 * camera.zoom) * paraScalar;
		float paraWidth = worldWidth * camera.zoom;
		float paraHeight = worldHeight * camera.zoom * aspectRatio;
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(bgTexture, paraX, paraY, paraWidth, paraHeight);
		spriteBatch.end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		// Draw masses
		for (Mass mass : gameMap.massArray) {
			if (mass instanceof Goal) {
				shapeRenderer.setColor(0, 0, 1, 1);				
			} else {
				shapeRenderer.setColor(1, 0, 0, 1);
			}
			mass.draw(shapeRenderer);
		}

		// Draw ship
		shapeRenderer.setColor(0, 1, 0, 1);
		if (gameMap.ship.isBoosting) {
			shapeRenderer.setColor(0, 1, 1, 1);
		}
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
		bgTexture.dispose();
		gameMap.ship.boostSound.dispose();
	}
}