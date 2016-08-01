package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.ui.TextItem;

/**
 * Menu screen holds many UI actors in its stage that provide menu
 * functionality.
 * 
 * @author Sawyer Harris
 *
 */
public class MenuScreen extends GameScreen {
	/** Menu world dimensions */
	private static final int WORLD_WIDTH = 4000;
	private static final int WORLD_HEIGHT = 2000;
	
	/** Theme */
	private static final Theme THEME = GravityGame.getInstance().getThemes().getTheme("test");
	
	/** Current node representing location in menu */
	private Node currentNode;

	/** Menu nodes */
	private static final Node ROOT = new Node(new Vector2(0, 0), null);

	private static final Node LEVELS = new Node(new Vector2(-1000, 0), ROOT);
	private static final Node OFFICIAL_LEVELS = new Node(new Vector2(-100, 100), LEVELS);
	private static final Node CUSTOM_LEVELS = new Node(new Vector2(-200, 0), LEVELS);
	private static final Node ONLINE_LEVELS = new Node(new Vector2(-100, -100), LEVELS);

	private static final Node OPTIONS = new Node(new Vector2(0, -100), ROOT);
	private static final Node SHIP_STYLE = new Node(new Vector2(100, -100), OPTIONS);

	private static final Node EDITOR = new Node(new Vector2(100, 0), ROOT);

	public MenuScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer, WORLD_WIDTH, WORLD_HEIGHT);
		getBackground().setTheme(THEME);
		
		TextItem center = new TextItem(-250, 0, 500, 100, new Color(1f, 1f, 1f, 0.2f), Touchable.enabled, "MENU", 30) {
			@Override
			public void click() {
				moveToNode(LEVELS);
			}
		};

		getStage().addActor(center);
		
		getCamera().setZoom(1f);
		moveToNode(ROOT);
	}

	private void moveToNode(Node node) {
		if (node == null) {
			// Root node, do nothing
			return;
		}
		currentNode = node;
		getCamera().setMoveTarget(node.position);
	}

	@Override
	public void keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			moveToNode(currentNode.previous);
		}
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
	}

	@Override
	public void zoom(float initialDistance, float distance) {
	}

	@Override
	public void tap(float x, float y, int count, int button) {
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

	/**
	 * Represents a position in the menu. Each node except for the root has a
	 * previous node much like a tree.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private static class Node {
		public Vector2 position;
		public Node previous;

		/**
		 * Constructs a Node at the given position with a link to the previous
		 * Node in the tree.
		 * 
		 * @param position Vector2 location of node
		 * @param previous previous node in tree
		 */
		public Node(Vector2 position, Node previous) {
			this.position = position;
			this.previous = previous;
		}
	}
}
