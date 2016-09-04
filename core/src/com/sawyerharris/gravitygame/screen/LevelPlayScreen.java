package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Input.Keys;
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
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.ui.LevelHeader;

/**
 * Level play screen, where the player can play a level.
 * 
 * @author Sawyer Harris
 *
 */
public class LevelPlayScreen extends LevelScreen {
	/** Singleton instance of game */
	private final GravityGame game = GravityGame.getInstance();

	/** Scalars for converting gestures into movements */
	private static final float VEL_SCALAR = 0.1f;
	private static final float ZOOM_SCALAR = 0.0005f;

	/** State of gameplay */
	private GameplayState state;

	/** Position and zoom of camera when in aiming mode */
	private Vector2 cameraAimingPosition;
	private float cameraAimingZoom;

	/** Number of attempts player has taken to beat the level */
	private int numAttempts;

	/** Level header, used by overlay */
	private LevelHeader header;

	/**
	 * Context in which game is being played i.e. normally, in test mode, or as
	 * a custom level
	 */
	private Context context;

	/**
	 * Constructs the level play screen with the given batch and shape renderer.
	 * 
	 * @param batch
	 * @param renderer
	 */
	public LevelPlayScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer);

		cameraAimingPosition = new Vector2(0, 0);
		cameraAimingZoom = 1f;

		numAttempts = 0;

		header = new LevelHeader(batch, renderer);

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

	/**
	 * Checks if ship has collided with a planet.
	 */
	private void checkCollisions() {
		// Check for planet collisions
		for (Planet planet : getPlanets()) {
			if (getShip().getPosition().dst(planet.getPosition()) < planet.getRadius() + Ship.RADIUS) {
				if (planet.isHomePlanet()) {
					victory();
				} else {
					aim();
				}
			}
		}
	}

	/**
	 * Reset camera position and zoom to defaults.
	 */
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

		header.setText(level.getName(), level.getMessage());
		Theme theme = game.getThemes().getTheme(level.getTheme());
		header.setColor(theme.getColor());
		header.show();

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
		switch (context) {
		case CUSTOM:
		case PLAYING:
			if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
				header.hide();
				game.setScreenToMenu();
			}
			break;
		case TESTING:
			if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
				header.hide();
				game.setScreenBackToEdit();
			}
			break;
		}
	}

	@Override
	public void scrolled(int amount) {
	}

	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == GameplayState.FIRING && getShip().getBoost() > 0) {
			getShip().startBoosting();
		}
	}

	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		getShip().stopBoosting();
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
		getShip().setTouchable(Touchable.disabled);
	}

	/**
	 * Called when a level is beaten.
	 */
	public void victory() {
		state = GameplayState.VICTORY;
		header.hide();
		switch (context) {
		case CUSTOM:
			game.setScreenToMenu();
			break;
		case PLAYING:
			int shipStyleUnlock = game.getLevels().levelCompleted();
			Level level = getLevel();
			Theme theme = game.getThemes().getTheme(level.getTheme());
			getOverlay().showVictoryPanel(getLevel().getName(), numAttempts, shipStyleUnlock, theme.getColor());
			break;
		case TESTING:
			game.setScreenBackToEdit();
			break;
		}

	}

	@Override
	public void render(float delta) {
		if (state == GameplayState.FIRING) {
			try {
				getShip().physicsUpdate(delta, getPlanets());
			} catch (IllegalArgumentException e) {
				// Ship position out of bounds
				aim();
			}

			checkCollisions();

			getCamera().setMoveTarget(getShip().getPosition());
		}
		super.render(delta);
		header.draw();
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

	/**
	 * Context in which level is being played.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum Context {
		PLAYING, TESTING, CUSTOM
	}

	/**
	 * Sets the context of the level being played.
	 * 
	 * @param con
	 *            context to set
	 */
	public void setContext(Context con) {
		context = con;
	}
}
