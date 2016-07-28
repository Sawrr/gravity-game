package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
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
	public static final int BORDER_WIDTH = 10;
	/** Amount to augment opacity when item is pressed */
	private static final float ON_PRESS_OPACITY_MOD = 0.2f;
	
	private boolean touch;

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
			touch = true;
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

		Group parent = this.getParent();
		
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		
		float drawHeight = height;
		float drawY = y;
		float drawTopBorder = BORDER_WIDTH;
		float drawBotBorder = BORDER_WIDTH;
		
		if (parent != null && parent instanceof ScrollPanel) {
			x += parent.getX();
			y += parent.getY();
			
			drawY = y;
			
			// Check for culling
			if (parent.getCullingArea() != null) {			
				Rectangle cull = new Rectangle(parent.getCullingArea());
				cull.setPosition(cull.x + parent.getX(), cull.y + parent.getY());
				
				float cullRight = cull.x + cull.width;
				float cullLeft = cull.x;
				float cullTop = cull.y + cull.height;
				float cullBottom = cull.y;
				
				if (!(x <= cullRight && y <= cullTop && x + width >= cullLeft && y + height >= cullBottom)) {
					if (touch) {
						setTouchable(Touchable.disabled);
					}
					return;
				} else {
					if (touch) {
						setTouchable(Touchable.enabled);
					}
				}
			}
			
			if (y < parent.getY() + BORDER_WIDTH && y >= parent.getY()) {
				drawY = parent.getY() + BORDER_WIDTH;
				drawHeight = height - (parent.getY() + BORDER_WIDTH - y);
				drawBotBorder = y - parent.getY();
			} else if (y < parent.getY() && y + height >= parent.getY() + 2 * BORDER_WIDTH) {
				drawY = parent.getY() + BORDER_WIDTH;
				drawHeight = height - (parent.getY() + BORDER_WIDTH - y);
				drawBotBorder = 0;
			} else if (y + height < parent.getY() + 2 * BORDER_WIDTH && y + height >= parent.getY() + BORDER_WIDTH) {
				drawY = parent.getY() + BORDER_WIDTH;
				drawBotBorder = 0;
				drawTopBorder = y + height - parent.getY() - BORDER_WIDTH;
				drawHeight = drawTopBorder;
			} else if (y + height > parent.getY() + parent.getHeight() - BORDER_WIDTH && y + height <= parent.getY() + parent.getHeight()) {
				drawHeight = height - (y + height - parent.getY() - parent.getHeight() + BORDER_WIDTH);
				drawTopBorder = BORDER_WIDTH - (y + height - parent.getY() - parent.getHeight() + BORDER_WIDTH);
			} else if (y + height > parent.getY() + parent.getHeight() && y <= parent.getY() + parent.getHeight() - 2 * BORDER_WIDTH) {
				drawHeight = height - (y + height - parent.getY() - parent.getHeight() + BORDER_WIDTH);
				drawTopBorder = 0;
			} else if (y > parent.getY() + parent.getHeight() - 2 * BORDER_WIDTH && y <= parent.getY() + parent.getHeight() - BORDER_WIDTH) {
				drawTopBorder = 0;
				drawBotBorder = parent.getY() + parent.getHeight() - BORDER_WIDTH - y;
				drawHeight = drawBotBorder;
			}
		}

		renderer.set(ShapeType.Filled);
		
		// Draw border
		renderer.setColor(BORDER_COLOR);
		
		// Bottom
		renderer.rect(x + BORDER_WIDTH, drawY, width - 2 * BORDER_WIDTH, drawBotBorder);
		
		// Top
		renderer.rect(x + BORDER_WIDTH, drawY + drawHeight - drawTopBorder, width - 2 * BORDER_WIDTH, drawTopBorder);
		
		// Left
		renderer.rect(x, drawY, BORDER_WIDTH, drawHeight);
		
		// Right
		renderer.rect(x + width - BORDER_WIDTH, drawY, BORDER_WIDTH, drawHeight);

		// Draw inside
		renderer.setColor(getColor());
		renderer.rect(x + BORDER_WIDTH, drawY + drawBotBorder, width - BORDER_WIDTH * 2,
				drawHeight - drawTopBorder - drawBotBorder);
		
	}
}
