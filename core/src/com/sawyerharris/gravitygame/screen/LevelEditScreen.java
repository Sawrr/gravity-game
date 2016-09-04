package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Timer;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.Planet;
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.game.Level.PlanetMeta;
import com.sawyerharris.gravitygame.screen.LevelPlayScreen.Context;

/**
 * Level editor screen for custom levels. Allows ship and planets to be
 * translated and planets to be scaled.
 * 
 * @author Sawyer Harris
 *
 */
public class LevelEditScreen extends LevelScreen {
	/** Singleton instance of game */
	private final GravityGame game = GravityGame.getInstance();

	/** Scalar for how much a mouse scroll translates into a planet zoom */
	private static final float SCROLL_SCALAR = 10f;

	/** Level name that will be applied if the user saves */
	private String customLevelName;
	/** Index in custom level list of level if it is saved */
	private int customLevelIndex;

	/**
	 * Constructs the level edit screen with the given batch and shape renderer.
	 * 
	 * @param batch
	 * @param renderer
	 */
	public LevelEditScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer);
		getCamera().setZoom(1f);

		getShip().setTouchable(Touchable.enabled);
		getShip().addListener(new ActorGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				getShip().translate(x, y);
			}
		});

		getOverlay().createEditButtons();
	}

	/**
	 * Sets the name of the custom level that will be applied if the user saves
	 * the level.
	 * 
	 * @param name
	 */
	public void setCustomLevelName(String name) {
		customLevelName = name;
	}

	/**
	 * Sets the index in the list of custom levels at which the current level
	 * will be placed if the user saves the level.
	 * 
	 * @param index
	 */
	public void setCustomLevelIndex(int index) {
		customLevelIndex = index;
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
		getCamera().translate(new Vector2(deltaX, deltaY));
	}

	@Override
	public void zoom(float initialDistance, float distance) {
	}

	@Override
	public void tap(float x, float y, int count, int button) {
		// Create new planet
		Theme theme = LevelScreen.DEFAULT_THEME;
		TextureRegion region = game.getAssets().getPlanet(theme.getPlanet());
		Vector3 worldCoords = getCamera().unproject(new Vector3(x, y, 0));
		Vector2 position = new Vector2(worldCoords.x, worldCoords.y);
		final Planet planet = new Planet(position, Planet.MIN_RADIUS, region, false);
		addPlanetListener(planet);
		getPlanets().add(planet);
		getStage().addActor(planet);
	}

	/**
	 * Adds an ActorGestureListener to the given planet so the player may
	 * translate and scale it when editing their custom levels.
	 * 
	 * @param planet
	 *            planet to add an ActorGestureListener to
	 */
	private void addPlanetListener(final Planet planet) {
		planet.addListener(new ActorGestureListener() {
			private boolean inflate;

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				planet.translate(x, y);
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (planet.isHomePlanet()) {
					planet.remove();
					getPlanets().remove(planet);
				} else {
					planet.setHomePlanet(true);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				inflate = true;
				Timer.schedule(new Timer.Task() {

					@Override
					public void run() {
						if (!inflate) {
							this.cancel();
						}
						planet.zoom(1);
					}
				}, 0, 0.01f);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				inflate = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});
	}

	@Override
	public void keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			game.setScreenToMenu();
		}
	}

	@Override
	public void scrolled(int amount) {
		Vector2 coords = getStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = getStage().hit(coords.x, coords.y, true);
		if (actor instanceof Planet) {
			Planet planet = (Planet) actor;
			planet.zoom(amount * SCROLL_SCALAR);
		}
	}

	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
	}

	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
	}

	/**
	 * Checks if the current level being edited is valid for testing i.e. has a
	 * home planet.
	 * 
	 * @return true if the current level is ready to test
	 */
	private boolean checkLevel() {
		for (Actor actor : getStage().getActors()) {
			if (actor instanceof Planet) {
				if (((Planet) actor).isHomePlanet()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Constructs a Level instance from the current level being edited.
	 * 
	 * @return a new Level instance based on the current level being edited
	 */
	private Level makeLevel() {
		ArrayList<PlanetMeta> planetList = new ArrayList<PlanetMeta>();
		for (Planet planet : getPlanets()) {
			planetList.add(new PlanetMeta(planet.getPosition(), planet.getRadius(), planet.isHomePlanet()));
		}
		return new Level(customLevelName, game.getPlayerStatus().getUsername(), getShip().getInitialPosition(),
				planetList);
	}

	/**
	 * If the current level being edited has a home planet, test the level on
	 * the level play screen.
	 */
	public void testLevel() {
		if (checkLevel()) {
			game.setScreenToPlay(makeLevel(), Context.TESTING);
		}
	}

	/**
	 * Save the current level being edited in the custom level list at the
	 * custom level index specified when the level was loaded for editing.
	 */
	public void saveLevel() {
		game.getLevels().saveLevel(customLevelIndex, makeLevel());
	}

	/**
	 * If level has a home planet and the player has set their username, upload
	 * the level to the online custom level database.
	 */
	public void uploadLevel() {
		if (checkLevel() && !game.getPlayerStatus().getUsername().equals("")) {
			System.out.println(game.getLevels().uploadLevel(makeLevel()));
		}
	}

	@Override
	public void loadLevel(Level level) {
		super.loadLevel(level);
		for (Planet planet : getPlanets()) {
			addPlanetListener(planet);
		}
	}
}
