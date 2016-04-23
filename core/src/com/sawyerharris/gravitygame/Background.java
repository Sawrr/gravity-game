package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.sawyerharris.gravitygame.GravityGame.GameState;

/**
 * Draws the background image with parallax
 * @author Sawyer Harris
 *
 */
public class Background extends Actor {
	private Screen screen;
	private GameCamera camera;
	private Level level;
	private Theme theme;
	private Texture texture;
	private int bgWidth;
	private int bgHeight;
	private int screenWidth;
	private int screenHeight;
	
	private static final float panScalar = 0.5f;
	
	public Background(Screen screen, GameCamera camera, Level level) {
		this.screen = screen;
		this.camera = camera;
		this.level = level;
		this.theme = GravityGame.getThemes().get(level.getThemeName());
		this.texture = GravityGame.getTextures().get(theme.getBackground());
		this.bgWidth = texture.getWidth();
		this.bgHeight = texture.getHeight();
		this.screenWidth = GravityGame.getScreenWidth();
		this.screenHeight = GravityGame.getScreenHeight();
		
		setBounds(0, 0, GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		
		addListener(new ActorGestureListener() {			
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				panCamera(deltaX, deltaY);
			}
			
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (count == 2) {
					doubleTap();
				}
			}
			
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
			}
			
			@Override
			public void zoom(InputEvent event, float initialDistance, float distance) {
				zoomCamera(distance);
			}
		});
	}
	
	// TODO fix the VIEW_MOVING interruption and add it for scrolling and everything else
	
	private void panCamera(float dx, float dy) {
		if (screen instanceof GameScreen && GravityGame.getState() == GameState.VIEW_MOVING) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateAiming();
			System.out.println(GravityGame.getState());
		}
		camera.pan(dx, dy);
		setBounds(camera.position.x - screenWidth / 2, 
				camera.position.y - screenWidth / 2, screenWidth, screenHeight);
	}
	
	private void zoomCamera(float dist) {
		if (screen instanceof GameScreen && GravityGame.getState() == GameState.VIEW_MOVING) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateAiming();
			System.out.println(GravityGame.getState());
		}
		camera.zoom(dist);
	}
	
	private void doubleTap() {
		if (screen instanceof GameScreen) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateViewMoving();
		}
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		// Camera variables
		float x = camera.position.x - camera.zoom * GravityGame.getScreenWidth()/2;
		float y = camera.position.y - camera.zoom * GravityGame.getScreenHeight()/2;
		float width = GravityGame.getScreenWidth() * camera.zoom;
		float height = GravityGame.getScreenHeight() * camera.zoom;

		// Normalized camera distance from center ( y is inverted )
		float norX = camera.position.x / level.getWidth() - 0.5f;
		float norY = 1 - camera.position.y / level.getHeight() - 0.5f;
		
		// Texture variables
		int srcWidth = (int) theme.getSrcViewWidth();
		int srcHeight = (int) (theme.getSrcViewWidth() / GravityGame.getAspectRatio());
		int srcX = (int) ((bgWidth  - srcWidth)/2 + norX * GravityGame.getAspectRatio() * theme.getParaScalar() * bgWidth);
		int srcY = (int) ((bgHeight - srcHeight)/2 + norY * theme.getParaScalar() * bgHeight);
		
		batch.draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, false, false);
	}
}
