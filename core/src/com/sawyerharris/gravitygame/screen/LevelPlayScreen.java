package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.Planet;
import com.sawyerharris.gravitygame.game.Ship;

public class LevelPlayScreen extends LevelScreen {
	private final GravityGame game = GravityGame.getInstance();

	private static final float VEL_SCALAR = 0.1f;
	private static final float ZOOM_SCALAR = 0.0005f;

	private GameplayState state;

	private Vector2 cameraAimingPosition;
	private float cameraAimingZoom;

	private int numAttempts;

	public LevelPlayScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer);

		cameraAimingPosition = new Vector2(0, 0);
		cameraAimingZoom = 1f;

		numAttempts = 0;

		getShip().setTouchable(Touchable.enabled);
		getShip().addListener(new ActorGestureListener() {
			@Override
			public void fling(InputEvent event, float velX, float velY, int button) {
				if (state == GameplayState.AIMING) {
					getShip().setVelocity(new Vector2(velX, velY).scl(VEL_SCALAR));
					fire();
				}
			}
		});

		aim();
	}

	private void checkCollisions() {
		// Check for planet collisions
		for (Planet planet : getPlanets()) {
			if (getShip().getPosition().dst(planet.getPosition()) < planet.getRadius() + Ship.RADIUS) {
				if (planet.isHomePlanet()) {
					victory();
				} else {
					resetLevel();
				}
			}
		}
	}

	private void resetCamera() {
		cameraAimingPosition = new Vector2(0, 0);
		cameraAimingZoom = 1f;
	}

	@Override
	public void loadLevel(Level level) {
		super.loadLevel(level);
		getOverlay().hideVictoryPanel();
		numAttempts = 0;
		resetCamera();
		aim();
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
		if (state == GameplayState.AIMING) {
			getCamera().translate(new Vector2(deltaX, deltaY));
			getCamera().stopAutoMove();
			getCamera().stopAutoZoom();
			cameraAimingPosition = getCamera().getPosition();
		}
	}

	@Override
	public void zoom(float initialDistance, float distance) {
		if (state == GameplayState.AIMING) {
			getCamera().zoom((initialDistance - distance) * ZOOM_SCALAR);
			cameraAimingZoom = getCamera().getZoom();
		}
	}

	@Override
	public void tap(float x, float y, int count, int button) {
	}

	@Override
	public void keyDown(int keycode) {
	}

	@Override
	public void scrolled(int amount) {
	}

	/**
	 * Resets the level to its initial state.
	 */
	public void resetLevel() {
		aim();
	}

	/**
	 * Returns the state of the game.
	 * 
	 * @return state
	 */
	public GameplayState getState() {
		return state;
	}

	/**
	 * Called when the ship is being aimed.
	 */
	public void aim() {
		getShip().reset();
		getCamera().setMoveTarget(cameraAimingPosition);
		getCamera().setZoomTarget(cameraAimingZoom);
		state = GameplayState.AIMING;
	}

	/**
	 * Called when the ship is fired.
	 */
	public void fire() {
		state = GameplayState.FIRING;
		numAttempts++;
	}

	/**
	 * Called when a level is beaten.
	 */
	public void victory() {
		state = GameplayState.VICTORY;
		int shipStyleUnlock = game.getLevels().levelCompleted();
		getOverlay().showVictoryPanel(getLevel().getName(), numAttempts, shipStyleUnlock);
	}

	@Override
	public void render(float delta) {
		if (state == GameplayState.FIRING) {
			try {
				getShip().physicsUpdate(delta, getPlanets());
			} catch (IllegalArgumentException e) {
				// Ship position out of bounds
				resetLevel();
			}

			checkCollisions();

			getCamera().setMoveTarget(getShip().getPosition());
		}
		super.render(delta);
	}

	/**
	 * State of game while playing.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum GameplayState {
		AIMING, FIRING, VICTORY
	}
}
