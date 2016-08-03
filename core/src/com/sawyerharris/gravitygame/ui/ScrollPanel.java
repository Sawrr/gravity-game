package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class ScrollPanel extends BorderedItem {

	private static final float DRAG = 0.95f;

	private Color color;
	private float iHeight;

	private float velocity;
	private float scrollY;
	
	private int size;

	public ScrollPanel(float x, float y, float width, float height, Color col, float itemHeight) {
		super(x, y, width, height, Color.CLEAR, Touchable.disabled);
		color = col;
		iHeight = itemHeight;
		
		scrollY = 0;
		velocity = 0;
		size = 0;
		
		setTouchable(Touchable.enabled);
		addListener(new ActorGestureListener() {
			@Override
			public void fling(InputEvent event, float velX, float velY, int button) {
				velocity = velY / 100;
			}
			
			@Override
			public void pan(InputEvent event, float x, float y, float dx, float dy) {
				scroll(dy);
				velocity = 0;
			}
		});
		
		setCullingArea(new Rectangle(0, BORDER_WIDTH, width, height - 2 * BORDER_WIDTH));
	}

	private void scroll(float amount) {
		// If the scroll panel is not full, do not scroll
		if (iHeight * size < getWidth()) {
			return;
		}
		scrollY += amount;
		if (scrollY < 0) {
			amount = amount - scrollY;
			scrollY = 0;
		}
		float floor = size * iHeight + 2 * BorderedItem.BORDER_WIDTH;
		if (scrollY - floor > -getHeight()) {
			amount = -getHeight() - (scrollY - floor) + amount;
			scrollY = floor - getHeight();
		}
		for (Actor actor : getChildren()) {
			actor.moveBy(0, amount);
		}
	}
	
	public void click(int index) {
	}

	public void addTextItem(String text, int fontSize) {
		final int index = size;
		TextItem item = new TextItem(BORDER_WIDTH, getHeight() - ((index + 1) * iHeight) - BORDER_WIDTH, getWidth() - 2 * BORDER_WIDTH, iHeight, color, Touchable.enabled, text, fontSize) {
			@Override
			public void click() {
				ScrollPanel.this.click(index);
			}
		};
		addActor(item);
		size++;
	}
	
	public void addTextureItem(TextureRegion region) {
		final int index = size;
		TextureItem item = new TextureItem(BORDER_WIDTH, getHeight() - ((index + 1) * iHeight) - BORDER_WIDTH, getWidth() - 2 * BORDER_WIDTH, iHeight, color, Touchable.enabled, region) {
			@Override
			public void click() {
				ScrollPanel.this.click(index);
			}
		};
		addActor(item);
		size++;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		if (velocity != 0) {
			velocity *= DRAG;
			scroll(velocity);
		}
		
		batch.flush();
		clipBegin(getX(), getY() + BORDER_WIDTH, getWidth(), getHeight() - 2 * BORDER_WIDTH);
		super.draw(batch, alpha);
		clipEnd();
	}
}
