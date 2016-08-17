package com.sawyerharris.gravitygame.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.PlayerStatus;
import com.sawyerharris.gravitygame.game.Theme;
import com.sawyerharris.gravitygame.manager.AssetManager;
import com.sawyerharris.gravitygame.manager.LevelManager;
import com.sawyerharris.gravitygame.ui.ScrollPanel;
import com.sawyerharris.gravitygame.ui.TextInputAdapter;
import com.sawyerharris.gravitygame.ui.TextItem;

/**
 * Menu screen holds many UI actors in its stage that provide menu
 * functionality.
 * 
 * @author Sawyer Harris
 *
 */
public class MenuScreen extends GameScreen {
	private final GravityGame game = GravityGame.getInstance();
	private final AssetManager assets = game.getAssets();
	private final LevelManager levels = game.getLevels();
	private final PlayerStatus status = game.getPlayerStatus();
	
	/** Menu world dimensions */
	private static final int WORLD_WIDTH = 8000;
	private static final int WORLD_HEIGHT = 6000;

	private static final int FONT_SIZE = 48;

	/** Theme */
	private static final Theme THEME = GravityGame.getInstance().getThemes().getTheme("n90");

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

	public void createMenuItems() {
		// First clear the menu
		getStage().clear();
		
		// ROOT

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
				// Set level to first tutorial level
				Level level = game.getLevels().getTutorialLevels().get(0);
				game.setScreenToPlay(level);
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

		// OPTIONS

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
				game.getPlayerStatus().resetProgress();
				createMenuItems();
			}
		};

		TextItem setUsernameButton = new TextItem(OPTIONS.position.x - 250, OPTIONS.position.y - 400, 500, 150,
				THEME.getColor(), Touchable.enabled, "Set Username", FONT_SIZE) {
			@Override
			public void click() {
				TextInputAdapter listener = new TextInputAdapter() {
					@Override
					public void input(String text) {
						status.setUsername(text);
						status.flush();
					}
				};
				Gdx.input.getTextInput(listener, "Enter username", status.getUsername(), "");
			}
		};

		getStage().addActor(optionsBackButton);
		getStage().addActor(soundToggleButton);
		getStage().addActor(shipStyleButton);
		getStage().addActor(resetProgressButton);
		getStage().addActor(setUsernameButton);

		// SHIP_STYLE

		ScrollPanel shipStylePanel = new ScrollPanel(SHIP_STYLE.position.x - 100, SHIP_STYLE.position.y - 600, 600,
				1200, THEME.getColor(), 500) {
			@Override
			public void click(int index) {
				if (index <= status.getHighestShipStyle()) {
					status.setShipStyle(index);
					status.flush();
				}
			}
		};

		int numStyles = assets.getNumShipStyles();
		for (int i = 0; i < numStyles; i++) {
			if (i <= status.getHighestShipStyle()) {
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

		// EDITOR

		ScrollPanel levelEditPanel = new ScrollPanel(EDITOR.position.x - 100, EDITOR.position.y - 600, 600, 1200,
				THEME.getColor(), 200) {
			@Override
			public void click(final int index) {
				TextInputAdapter listener = new TextInputAdapter() {
					@Override
					public void input(String text) {
						game.setScreenToEdit(text, index);
					}
				};

				// TODO: make sure button cannot be pressed twice
				
				ArrayList<Level> levelList = levels.getCustomLevels();
				if (index == levelList.size()) {
					Gdx.input.getTextInput(listener, "Enter level name", "", "");
				} else {
					Level level = levelList.get(index);
					Gdx.input.getTextInput(listener, "Enter level name", level.getName(), "");
				}
			}
		};

		// Load existing levels into scroll panel
		for (Level level : levels.getCustomLevels()) {
			levelEditPanel.addTextItem(level.getName(), 40);
		}

		// Add new level
		levelEditPanel.addTextItem("New level", 40);

		TextItem editBackButton = new TextItem(EDITOR.position.x - 500, EDITOR.position.y - 75, 300, 150,
				THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(ROOT);
			}
		};

		getStage().addActor(levelEditPanel);
		getStage().addActor(editBackButton);

		// LEVELS

		TextItem officialLevelsButton = new TextItem(LEVELS.position.x - 375, LEVELS.position.y + 250, 500, 150,
				THEME.getColor(), Touchable.enabled, "Official Levels", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(OFFICIAL_LEVELS);
			}
		};

		TextItem customLevelsButton = new TextItem(LEVELS.position.x - 375, LEVELS.position.y - 75, 500, 150,
				THEME.getColor(), Touchable.enabled, "Custom Levels", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(CUSTOM_LEVELS);
			}
		};

		TextItem onlineLevelsButton = new TextItem(LEVELS.position.x - 375, LEVELS.position.y - 400, 500, 150,
				THEME.getColor(), Touchable.enabled, "Online Levels", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(ONLINE_LEVELS);
			}
		};

		TextItem levelsBackButton = new TextItem(LEVELS.position.x + 200, LEVELS.position.y - 75, 300, 150,
				THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(ROOT);
			}
		};

		getStage().addActor(officialLevelsButton);
		getStage().addActor(customLevelsButton);
		getStage().addActor(onlineLevelsButton);
		getStage().addActor(levelsBackButton);

		// OFFICIAL_LEVELS

		ScrollPanel officialLevelPanel = new ScrollPanel(OFFICIAL_LEVELS.position.x - 300,
				OFFICIAL_LEVELS.position.y - 350, 600, 1000, THEME.getColor(), 200) {
			@Override
			public void click(final int index) {
				if (index <= status.getHighestLevel()) {
					game.setScreenToPlay(levels.getOfficialLevels().get(index));
				}
			}
		};

		// Load official levels into scroll panel
		ArrayList<Level> levelList = levels.getOfficialLevels();
		for (int i = 0; i < levelList.size(); i++) {
			if (i <= status.getHighestLevel()) {
				officialLevelPanel.addTextItem(levelList.get(i).getName(), 40);
			} else {
				officialLevelPanel.addTextItem("?", 40);
			}
		}

		TextItem officialLevelsBackButton = new TextItem(OFFICIAL_LEVELS.position.x - 150,
				OFFICIAL_LEVELS.position.y - 600, 300, 150, THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(LEVELS);
			}
		};

		getStage().addActor(officialLevelPanel);
		getStage().addActor(officialLevelsBackButton);

		// ONLINE_LEVELS

		ScrollPanel onlineLevelPanel = new ScrollPanel(ONLINE_LEVELS.position.x - 300, ONLINE_LEVELS.position.y - 600,
				600, 1000, THEME.getColor(), 200) {
			@Override
			public void click(final int index) {
				game.setScreenToPlay(levels.getOnlineLevels().get(index));
			}
		};

		// Load online levels into scroll panel
		ArrayList<Level> onlineLevelList = levels.getOnlineLevels();
		for (int i = 0; i < onlineLevelList.size(); i++) {
			onlineLevelPanel.addTextItem(onlineLevelList.get(i).getName(), 40);
		}

		TextItem onlineLevelsBackButton = new TextItem(ONLINE_LEVELS.position.x - 150, ONLINE_LEVELS.position.y + 500,
				300, 150, THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(LEVELS);
			}
		};

		getStage().addActor(onlineLevelPanel);
		getStage().addActor(onlineLevelsBackButton);
		
		// CUSTOM_LEVELS

		ScrollPanel customLevelPanel = new ScrollPanel(CUSTOM_LEVELS.position.x - 500, CUSTOM_LEVELS.position.y - 600,
				600, 1200, THEME.getColor(), 200) {
			@Override
			public void click(final int index) {
				game.setScreenToPlay(levels.getCustomLevels().get(index));
			}
		};

		// Load custom levels into scroll panel
		ArrayList<Level> customLevelList = levels.getCustomLevels();
		for (int i = 0; i < customLevelList.size(); i++) {
			customLevelPanel.addTextItem(customLevelList.get(i).getName(), 40);
		}

		TextItem customLevelsBackButton = new TextItem(CUSTOM_LEVELS.position.x + 200, CUSTOM_LEVELS.position.y - 75,
				300, 150, THEME.getColor(), Touchable.enabled, "Back", FONT_SIZE) {
			@Override
			public void click() {
				moveToNode(LEVELS);
			}
		};

		getStage().addActor(customLevelPanel);
		getStage().addActor(customLevelsBackButton);

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
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
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
