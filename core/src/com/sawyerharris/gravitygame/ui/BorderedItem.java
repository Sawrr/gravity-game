package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * UI element with a border that contains content.
 * 
 * @author Sawyer Harris
 *
 */
public abstract class BorderedItem extends Group {
	/** Color of the border */
	private static final Color BORDER_COLOR = new Color(1, 1, 1, 0.5f);
	/** Width of border */
	private static final int BORDER_WIDTH = 10;
	/** Amount to augment opacity when item is pressed */
	private static final float ON_PRESS_OPACITY_MOD = 0.2f;

	private ShapeRenderer renderer;

	/**
	 * Constructs a bordered item from the given parameters.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 *            Color of content
	 * @param touchable
	 *            Touchable.enabled, disabled, or childrenOnly
	 */
	public BorderedItem(float x, float y, float width, float height, Color color, Touchable touchable) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setColor(color);
		setTouchable(touchable);

		renderer = new ShapeRenderer();

		if (isTouchable()) {
			addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					getColor().a += ON_PRESS_OPACITY_MOD;
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					getColor().a -= ON_PRESS_OPACITY_MOD;
				}

				@Override
				public void clicked(InputEvent event, float x, float y) {
					click();
				}
			});
		}
	}

	/**
	 * Called when this item is clicked.
	 */
	public abstract void click();

	@Override
	public void draw(Batch batch, float alpha) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();

		renderer.setAutoShapeType(true);
		renderer.begin();
		renderer.set(ShapeType.Filled);

		// Draw border
		renderer.setColor(BORDER_COLOR);
		renderer.rect(x, y, width, BORDER_WIDTH);
		renderer.rect(x, y + height - BORDER_WIDTH, width, BORDER_WIDTH);
		renderer.rect(x, y + BORDER_WIDTH, BORDER_WIDTH, height - 2 * BORDER_WIDTH);
		renderer.rect(x + width - BORDER_WIDTH, y + BORDER_WIDTH, BORDER_WIDTH, height - 2 * BORDER_WIDTH);

		// Draw inside
		renderer.setColor(getColor());
		renderer.rect(getX() + BORDER_WIDTH, getY() + BORDER_WIDTH, getWidth() - BORDER_WIDTH * 2,
				getHeight() - BORDER_WIDTH * 2);
		renderer.end();
	}

}
