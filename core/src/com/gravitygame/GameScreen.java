package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
	public final GravityGame game;
	public int worldWidth;
	public int worldHeight;
	private GameCamera camera;
	private Viewport viewport;
	private GameState state;
	public GameMap gameMap;

	public float shipZoomLevel = 1f;
	public float shipCamOffsetY = 200;
	public float worldZoomLevel;
	
	private SpriteBatch spriteBatch = new SpriteBatch();
	private Texture bgTexture;
	private Texture shipTexture;
	private Texture shipBoostTexture;
	private Sprite shipSprite;
	private int bgWidth;
	private int bgHeight;
	
	private float aspectRatio;
	private float paraScalar;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Vector2 dragVector;
	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();

	private String mapName;
	private int mapId;
	
	private Vector2 moveTarget;
	private float zoomTarget;
	private GameState stateTarget;
	
	public GameScreen(GravityGame game, String mapName, int mapId) {
		this.game = game;
		this.mapName = mapName;
		this.mapId = mapId;
		
		inputMultiplexer.addProcessor(new DesktopListener(this));
		inputMultiplexer.addProcessor(new GestureDetector(new TouchListener(this)));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		loadFromJson();
		
		aspectRatio = game.screenWidth / (float) game.screenHeight;
		paraScalar = 0.1f;
		
		// Extract world width, height
		worldWidth = gameMap.width;
		worldHeight = gameMap.height;
		
		// Create texture from background image name
		bgTexture = new Texture(gameMap.background);
		bgWidth = bgTexture.getWidth();
		bgHeight = bgTexture.getHeight();
		
		shipTexture = new Texture(Gdx.files.internal("rocket.png"));
		shipBoostTexture = new Texture(Gdx.files.internal("rocket_boost.png"));
		shipSprite = new Sprite(shipTexture);
		
		dragVector = new Vector2(0,1f);
				
		camera = new GameCamera();
		viewport = new FillViewport(game.screenWidth, game.screenHeight, camera);
		camera.setToOrtho(false);
		camera.centerOnShip(this);
		
		worldZoomLevel = Math.min(worldWidth/camera.viewportWidth, worldHeight/camera.viewportHeight)/1.2f;
		
		state = GameState.VIEWING;
		camera.centerOnWorld(this);
	}

	public void loadFromJson() {
		FileHandle file = Gdx.files.internal("maps/" + mapName);
		Json json = new Json();
		gameMap = json.fromJson(GameMap.class, file);
		gameMap.ship.screen = this;
	}
	
	////////////////////////////////
	// Handle mouse / touch input //
	////////////////////////////////
	
	public void pan(float x, float y, float deltaX, float deltaY) {
		switch (state) {
			case VIEWING:
				camera.translate(deltaX, deltaY);
				camera.clampPos(worldWidth, worldHeight);
				break;
			case AIMING:
				dragVector.x += deltaX;
				dragVector.y += deltaY;
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
				startFiring(dragVector);
				dragVector.x = 0;
				dragVector.y = 1f;
				break;
			default:
				break;
		}
	}

	public void zoom(float zoomDistance) {
		switch (state) {
			case VIEWING:
				float maxZoom = Math.min(worldWidth/camera.viewportWidth, worldHeight/camera.viewportHeight);
				if (camera.zoom + zoomDistance <= maxZoom && camera.zoom + zoomDistance >= 1f) {
					camera.zoom += zoomDistance;
				}
				camera.clampPos(worldWidth, worldHeight);
				break;
			default:
				break;
		}
	}
	
	public void tap(Vector3 screenPos, int count) {
		switch (state) {
			case VIEWING:
				startAiming();
				break;
			case AIMING:
				startViewing();
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
	
	public void startViewing() {
		state = GameState.MOVING;
		moveTarget = new Vector2(worldWidth / 2, worldHeight / 2);
		zoomTarget = worldZoomLevel;
		stateTarget = GameState.VIEWING;
	}
	
	public void startAiming() {
		state = GameState.MOVING;
		moveTarget = new Vector2(gameMap.ship.pos.x, gameMap.ship.pos.y + shipCamOffsetY);
		zoomTarget = shipZoomLevel;
		stateTarget = GameState.AIMING;
	}
	
	public void startFiring(Vector2 dragVector) {
		Vector2 Vinitial = new Vector2(dragVector);
		state = GameState.FIRING;
		gameMap.ship.vel[0] = Vinitial;
		
	}
		
	public void reset() {
		loadFromJson();
		state = GameState.AIMING;
		game.boostSound.stop();
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
	//        Rendering methods           //
	////////////////////////////////////////
	
	public void drawShip(SpriteBatch sb) {
		// Set ship angle
		shipSprite.setRotation(gameMap.ship.angle);
		
		// Set ship texture
		if (gameMap.ship.isBoosting) {
			shipSprite.setTexture(shipBoostTexture);
		} else {
			shipSprite.setTexture(shipTexture);
		}
		
		// Draw ship
		shipSprite.setCenter(gameMap.ship.pos.x, gameMap.ship.pos.y);
		shipSprite.draw(sb);
	}
	
	public void drawBackground(SpriteBatch sb) {
		// Camera variables
		float x = camera.position.x - camera.zoom * game.screenWidth/2;
		float y = camera.position.y - camera.zoom * game.screenHeight/2;
		float width = game.screenWidth * camera.zoom;
		float height = game.screenHeight * camera.zoom;

		// Normalized camera distance from center ( y is inverted )
		float norX = camera.position.x / worldWidth - 0.5f;
		float norY = 1 - camera.position.y / worldHeight - 0.5f;
		
		// Texture variables
		int srcWidth = gameMap.viewWidth;
		int srcHeight = (int) (gameMap.viewWidth / aspectRatio);
		int srcX = (int) ((bgWidth  - srcWidth)/2 + norX * aspectRatio * paraScalar * bgWidth);
		int srcY = (int) ((bgHeight - srcHeight)/2 + norY * paraScalar * bgHeight);
		
		sb.draw(bgTexture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, false, false);
	}
	
	////////////////////////////////////////
	//         LIBGDX methods             //
	////////////////////////////////////////
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (state) {
			case VIEWING:
				gameMap.ship.angle = 90;
				break;
			case AIMING: 
				gameMap.ship.angle = dragVector.angle();
				break;
			case FIRING:
				gameMap.ship.update(delta, gameMap.massArray);
				gameMap.ship.angle = gameMap.ship.vel[0].angle();
				checkForCollisions();
				camera.centerOnShip(this);
				break;
			case MOVING:
				float dx = moveTarget.x - camera.position.x;
				float dy = moveTarget.y - camera.position.y;
				float dzoom = zoomTarget - camera.zoom;
				if ((Math.hypot(dx, dy) > 3f) || Math.abs(dzoom) > 3f) {
					camera.position.x += dx/7f;
					camera.position.y += dy/7f;
					camera.zoom += dzoom/7f;
				} else {
					if (stateTarget == GameState.VIEWING) {
						camera.centerOnWorld(this);
					} else if (stateTarget == GameState.AIMING) {
						camera.centerOnShip(this);
					}
					state = stateTarget;
				}
				break;
			default:
				break;
		}
		
		camera.update();
				
		// Sprite batch
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		drawBackground(spriteBatch);
		drawShip(spriteBatch);
		
		spriteBatch.end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		// Draw masses as circles
		for (Mass mass : gameMap.massArray) {
			if (mass instanceof Goal) {
				shapeRenderer.setColor(0, 0, 1, 1);				
			} else {
				shapeRenderer.setColor(1, 0, 0, 1);
			}
			mass.draw(shapeRenderer);
		}

		// Draw ship as circle
		/*
		shapeRenderer.setColor(0, 1, 0, 1);
		if (gameMap.ship.isBoosting) {
			shapeRenderer.setColor(0, 1, 1, 1);
		}
		gameMap.ship.draw(shapeRenderer);
		*/
		
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
	}
}