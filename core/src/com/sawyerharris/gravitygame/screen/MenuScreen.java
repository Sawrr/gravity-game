package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.manager.AssetManager;
import com.sawyerharris.gravitygame.ui.ScrollPanel;
import com.sawyerharris.gravitygame.ui.TextItem;
import com.sawyerharris.gravitygame.ui.TextureItem;

/**
 * Menu screen holds many UI actors in its stage that provide menu
 * functionality.
 * 
 * @author Sawyer Harris
 *
 */
public class MenuScreen extends GameScreen {
	/** Menu world dimensions */
	private static final int WORLD_WIDTH = 6000;
	private static final int WORLD_HEIGHT = 6000;

	private static final int FONT_SIZE = 48;

	/** Theme */
	private static final Theme THEME = GravityGame.getInstance().getThemes().getTheme("menu");

	/** Current node representing location in menu */
	private Node currentNode;

	/** Menu nodes */
	private static final Node ROOT = new Node(new Vector2(0, 0), null);

	private static final Node LEVELS = new Node(new Vector2(-1500, 0), ROOT);
	private static final Node OFFICIAL_LEVELS = new Node(new Vector2(-1500, 1500), LEVELS);
	private static final Node CUSTOM_LEVELS = new Node(new Vector2(-3000, 0), LEVELS);
	private static final Node ONLINE_LEVELS = new Node(new Vector2(-1500, -1500), LEVELS);

	private static final Node OPTIONS = new Node(new Vector2(0, -1500), ROOT);
	private static final Node SHIP_STYLE = new Node(new Vector2(1500, -1500), OPTIONS);

	private static final Node EDITOR = new Node(new Vector2(1500, 0), ROOT);

	public MenuScreen(Batch batch, ShapeRenderer renderer) {
		super(batch, renderer, WORLD_WIDTH, WORLD_HEIGHT);
		getBackground().setTheme(THEME);

		moveToNode(ROOT);

		createMenuItems();
	}

	private void createMenuItems() {
		// ROOT MENU

		TextItem playButton = new TextItem(-250, 400, 500, 150, THEME.getColor(), Touchable.enabled, "Play",
				FONT_SIZE) {
			@Override
			public void click() {
				System.out.println("Play");
			}
		};

		TextItem tutorialButton = new TextItem(-250, 200, 500, 150, THEME.getColor(), Touchable.enabled, "Tutorial",
				FONT_SIZE) {
			@Override
			public void click() {
				System.out.println("Tutorial");
			}
		};

		TextItem levelSelectButton = new TextItem(-250, 0, 500, 150, THEME.getColor(), Touchable.enabled,
				"Level Select", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(LEVELS);
			}
		};

		TextItem levelEditorButton = new TextItem(-250, -200, 500, 150, THEME.getColor(), Touchable.enabled,
				"Level Editor", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(EDITOR);
			}
		};

		TextItem optionsButton = new TextItem(-250, -400, 500, 150, THEME.getColor(), Touchable.enabled, "Options",
				FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(OPTIONS);
			}
		};

		getStage().addActor(playButton);
		getStage().addActor(tutorialButton);
		getStage().addActor(levelSelectButton);
		getStage().addActor(levelEditorButton);
		getStage().addActor(optionsButton);

		// OPTIONS MENU

		TextItem optionsBackButton = new TextItem(OPTIONS.position.x - 150, OPTIONS.position.y + 400, 300, 150,
				THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(ROOT);
			}
		};

		TextItem soundToggleButton = new TextItem(OPTIONS.position.x - 250, OPTIONS.position.y + 200, 500, 150,
				THEME.getColor(), Touchable.enabled, "Sound Toggle", FONT_SIZE) {
			@Override
			public void click() {
				System.out.println("Sound toggled");
			}
		};

		TextItem shipStyleButton = new TextItem(OPTIONS.position.x - 250, OPTIONS.position.y, 500, 150,
				THEME.getColor(), Touchable.enabled, "Choose Ship Style", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(SHIP_STYLE);
			}
		};

		TextItem resetProgressButton = new TextItem(OPTIONS.position.x - 250, OPTIONS.position.y - 200, 500, 150,
				THEME.getColor(), Touchable.enabled, "Reset Progress", FONT_SIZE) {
			@Override
			public void click() {
				System.out.println("Progress reset");
			}
		};

		getStage().addActor(optionsBackButton);
		getStage().addActor(soundToggleButton);
		getStage().addActor(shipStyleButton);
		getStage().addActor(resetProgressButton);

		// SHIP STYLE
		ScrollPanel shipStylePanel = new ScrollPanel(SHIP_STYLE.position.x - 100, SHIP_STYLE.position.y - 600, 600,
				1200, THEME.getColor(), 500) {
					@Override
					public void click(int index) {
						GravityGame.getInstance().getPlayerStatus().setShipStyle(index);
					}
				};

		AssetManager assets = GravityGame.getInstance().getAssets();
		int numStyles = assets.getNumShipStyles();
		for (int i = 0; i < numStyles; i++) {
			if (i <= GravityGame.getInstance().getPlayerStatus().getHighestShipStyle()) {
				shipStylePanel.addTextureItem(assets.getShipAnimation(i, "default").getKeyFrame(0));
			} else {
				shipStylePanel.addTextItem("?", 80);
			}
		}
		
		TextItem shipStyleBackButton = new TextItem(SHIP_STYLE.position.x - 500, SHIP_STYLE.position.y - 75, 300, 150,
				THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(OPTIONS);
			}
		};	
		
		getStage().addActor(shipStylePanel);
		getStage().addActor(shipStyleBackButton);
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
		 * @param position
		 *            Vector2 location of node
		 * @param previous
		 *            previous node in tree
		 */
		public Node(Vector2 position, Node previous) {
			this.position = position;
			this.previous = previous;
		}
	}
}
