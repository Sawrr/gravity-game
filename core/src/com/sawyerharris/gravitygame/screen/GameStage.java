package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sawyerharris.gravitygame.ui.BorderedItem;
import com.sawyerharris.gravitygame.ui.ScrollPanel;

/**
 * An extended stage that provides functionality for drawing UI items with a
 * shape renderer.
 * 
 * @author Sawyer Harris
 *
 */
public class GameStage extends Stage {
	/** Shape renderer */
	private ShapeRenderer shapeRenderer;
	/** UI items to be drawn with shape renderer */
	private ArrayList<BorderedItem> items;

	/**
	 * Constructs the super class stage and the items list.
	 * 
	 * @param viewport
	 * @param batch
	 * @param renderer
	 */
	public GameStage(Viewport viewport, Batch batch, ShapeRenderer renderer) {
		super(viewport, batch);
		shapeRenderer = renderer;
		items = new ArrayList<BorderedItem>();
	}

	/**
	 * If actor being added is a UI element, then add it to the list of UI items
	 * to be drawn with shape renderer. Then call super.addActor(actor).
	 * 
	 * @param actor
	 */
	@Override
	public void addActor(Actor actor) {
		if (actor instanceof BorderedItem) {
			items.add((BorderedItem) actor);
			if (actor instanceof ScrollPanel) {
				for (Actor child : ((ScrollPanel) actor).getChildren()) {
					items.add((BorderedItem) child);
				}
			}
		}
		super.addActor(actor);
	}

	/**
	 * Draws UI shapes first, then draws other elements on top.
	 */
	@Override
	public void draw() {
		drawItems();
		super.draw();
	}

	private void drawItems() {
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin();
		for (BorderedItem item : items) {
			if (item.isVisible()) {
				item.drawItem(shapeRenderer);
			}
		}
		shapeRenderer.end();
	}
}
