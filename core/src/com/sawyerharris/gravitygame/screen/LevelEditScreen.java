package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.ui.TextItem;

public class LevelEditScreen extends LevelScreen {
	/** Level name that will be applied if the user saves */
	private String customLevelName;
	/** Index in custom level list of level if it is saved */
	private int customLevelIndex;

	public LevelEditScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer);
		getCamera().setZoom(1f);
		TextItem item = new TextItem(-250, 250, 500, 100, new Color(1f, 1f, 1f, 0.2f), Touchable.enabled, "EDIT", 30);
		getStage().addActor(item);
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
		getCamera().translate(new Vector2(deltaX, deltaY).scl(0.75f));
		getCamera().stopAutoMove();
		getCamera().stopAutoZoom();
	}

	@Override
	public void zoom(float initialDistance, float distance) {
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
	
	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
	}

	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
	}

	// must check that level has a home planet
	public void testLevel() {

	}

	public void saveLevel() {

	}

	// must check that author name has been set
	// must check that level has a home planet
	public void uploadLevel() {

	}
}
