package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class Overlay {
	public static final float HEADER_SHOW_TIME = 3f;
	public static final float BANNER_MAX_ALPHA = 0.4f;
	public static final float TEXT_MAX_ALPHA = 1f;
	public static final float ALPHA_INCREMENT = 0.001f;
	
	private static final float BAR_WIDTH = 30;
	private static final float BAR_HEIGHT = 300;
	
	private static final float BANNER_OFFSET = 160;
	private static final float NAME_OFFSET = 40;
	private static final float MSG_OFFSET = 40;
	private static final float LOWER_OFFSET = 40;
	private static final float MSG_MARGIN = 100;
	private static final int NAME_FONT_SIZE = 44;
	private static final int MSG_FONT_SIZE = 38;
	
	private static final float BUTTON_FONT_SCALE = 3f;
	
	private int screenWidth;
	private int screenHeight;
	private int boost;
	
	private Level level;
	private Theme theme;
	private Ship ship;
	private Screen screen;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private FreeTypeFontGenerator gen;
	private FreeTypeFontParameter nameParam;
	private FreeTypeFontParameter msgParam;
	private BitmapFont nameFont;
	private BitmapFont msgFont;
	
	private Stage stage;
	private FillViewport viewport;
	private TextButton testButton;
	private TextButton saveButton;
	private Skin skin;
	
	public Overlay(Level level, Ship ship, Screen screen) {
		this.level = level;
		this.theme = GravityGame.getThemes().get(level.getThemeName());
		this.ship = ship;
		this.screen = screen;
		
		viewport = new FillViewport(GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		stage = new Stage(viewport);
		skin = GravityGame.getSkin();
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		sr.setProjectionMatrix(viewport.getCamera().combined);
		sb.setProjectionMatrix(viewport.getCamera().combined);
		gen = new FreeTypeFontGenerator(GravityGame.fontPath);
		nameParam = new FreeTypeFontParameter();
		nameParam.size = NAME_FONT_SIZE;
		msgParam = new FreeTypeFontParameter();
		msgParam.size = MSG_FONT_SIZE;
		nameFont = gen.generateFont(nameParam);
		msgFont = gen.generateFont(msgParam);
		screenWidth = GravityGame.getScreenWidth();
		screenHeight = GravityGame.getScreenHeight();
	}
	
	public void drawLevelHeader(float bannerAlpha, float textAlpha) {
		// Apply transparency
		Color bannerColor = theme.getColor();
		Color textColor = new Color(1f, 1f, 1f, 1f);
		bannerColor.a = bannerAlpha;
		textColor.a = textAlpha;
		
		GlyphLayout name = new GlyphLayout(nameFont, level.getName(), textColor, screenWidth, Align.center, true);
		GlyphLayout msg = new GlyphLayout(msgFont, level.getMessage(), textColor, screenWidth - 2 * MSG_MARGIN, Align.center, true);
		
		float bannerHeight = NAME_OFFSET + name.height + LOWER_OFFSET;;
		
		if (!level.getMessage().equals("")) {
			bannerHeight += MSG_OFFSET + msg.height;
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		sr.begin(ShapeType.Filled);
		sr.setColor(bannerColor);
		sr.rect(0, screenHeight - BANNER_OFFSET - bannerHeight, screenWidth, bannerHeight);
		sr.end();
		
		sb.begin();
		nameFont.draw(sb, name, 0, screenHeight - BANNER_OFFSET - NAME_OFFSET);
		msgFont.draw(sb, msg, MSG_MARGIN, screenHeight - BANNER_OFFSET - NAME_OFFSET - name.height - MSG_OFFSET);
		sb.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void drawBoostBar() {
		boost = ship.getBoost();
		
		sr.begin(ShapeType.Line);
		sr.setColor(Color.BLACK);
		sr.rect(screenWidth - 2 * BAR_WIDTH, screenHeight - 1.2f * BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
		sr.rect(screenWidth - 2 * BAR_WIDTH - 1, screenHeight - 1.2f * BAR_HEIGHT - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2);
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.SCARLET);
		sr.rect(screenWidth - 2 * BAR_WIDTH, screenHeight - 1.2f * BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT * boost / Ship.INITIAL_BOOST);
		sr.end();
	}
	
	public void createLevelEditorButtons() {
		testButton = new TextButton("Test", skin);
		testButton.setX(0);
		testButton.setY(screenHeight - 100);
		testButton.setWidth(screenWidth / 2);
		testButton.setHeight(100);
		testButton.getLabel().setFontScale(BUTTON_FONT_SCALE);
		testButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int pointer, int button) {
				LevelEditorScreen les = (LevelEditorScreen) screen;
				les.testLevel();
			}
		});
		stage.addActor(testButton);
		
		saveButton = new TextButton("Save and Quit", skin);
		saveButton.setX(screenWidth / 2);
		saveButton.setY(screenHeight - 100);
		saveButton.setWidth(screenWidth / 2);
		saveButton.setHeight(100);
		saveButton.getLabel().setFontScale(BUTTON_FONT_SCALE);
		saveButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int pointer, int button) {
				LevelEditorScreen les = (LevelEditorScreen) screen;
				les.saveLevel();
			}
		});
		stage.addActor(saveButton);
	}
	
	public void drawLevelEditorButtons() {
		stage.draw();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void dispose() {
		stage.dispose();
	}
}
