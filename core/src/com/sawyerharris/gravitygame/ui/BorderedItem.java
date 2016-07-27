package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

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
	protected static final int BORDER_WIDTH = 10;
	/** Amount to augment opacity when item is pressed */
	private static final float ON_PRESS_OPACITY_MOD = 0.2f;

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
		setBounds(x, y, width, height);

		if (isTouchable()) {
			addListener(new ActorGestureListener() {
				@Override
				public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
					getColor().a += ON_PRESS_OPACITY_MOD;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					getColor().a -= ON_PRESS_OPACITY_MOD;
				}

				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					click();
				}
			});
		}
	}

	/**
	 * Called when this item is clicked.
	 */
	public void click() {
	}

	/**
	 * Draws the bordered item using the given shape renderer (GameStage's)
	 * @param renderer shape renderer
	 */
	public void drawItem(ShapeRenderer renderer) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();

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
	}
}
