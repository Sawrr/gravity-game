package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sawyerharris.gravitygame.ui.BorderedItem;
import com.sawyerharris.gravitygame.ui.ScrollPanel;

public class GameStage extends Stage {

	private ShapeRenderer shapeRenderer;
	private ArrayList<BorderedItem> items;
	
	public GameStage() {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		items = new ArrayList<BorderedItem>();
	}
	
	public GameStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		items = new ArrayList<BorderedItem>();
	}

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
	
	@Override
	public void draw() {
		drawItems();
		super.draw();
	}
	
	private void drawItems() {
		shapeRenderer.begin();
		for (BorderedItem item : items) {
			item.drawItem(shapeRenderer);
		}
		shapeRenderer.end();
	}
}
