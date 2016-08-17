package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.sawyerharris.gravitygame.game.GravityGame;

public class LevelHeader {

	private final GravityGame game = GravityGame.getInstance();
	
	private static final float FADE_TIME = 1f;
	private static final float DISPLAY_TIME = 3f;

	private static final int NAME_FONT_SIZE = 38;
	private static final int MSG_FONT_SIZE = 32;

	private static final Color TEXT_COLOR = Color.WHITE;

	public static final float BANNER_MAX_ALPHA = 0.6f;
	public static final float TEXT_MAX_ALPHA = 1f;
	
	private static final int MSG_MARGIN = 100;
	private static final int BANNER_OFFSET = 160;
	private static final int NAME_OFFSET = 40;
	private static final int LOWER_OFFSET = 40;
	private static final int MSG_OFFSET = 40;
	
	private Batch batch;
	private ShapeRenderer renderer;
	
	private float time;
	
	private GlyphLayout nameGL;
	private GlyphLayout msgGL;

	private BitmapFont nameFont;
	private BitmapFont msgFont;

	private float bannerHeight;
	private Color color;
	private Color textColor;

	private String name;
	private String msg;
	
	public LevelHeader(Batch batch, ShapeRenderer renderer) {
		this.batch = batch;
		this.renderer = renderer;
		
		nameFont = game.getAssets().getFont(NAME_FONT_SIZE);
		msgFont = game.getAssets().getFont(MSG_FONT_SIZE);
		
		textColor = new Color(TEXT_COLOR);
		
		setText("", "");
		setColor(Color.BLACK);
		
		hide();
	}

	public void setText(String name, String msg) {
		if (name == null) {
			throw new IllegalArgumentException("Level header name cannot be null");
		}
		this.name = name;
		if (msg == null) {
			this.msg = "";
		} else {
			this.msg = msg;	
		}
	}
	
	public void setColor(Color color) {
		this.color = new Color(color);
	}
	
	public void show() {
		time = 0;
	}
	
	public void hide() {
		time = 2 * FADE_TIME + DISPLAY_TIME + 1;
	}
	
	public void draw() {
		time += Gdx.graphics.getDeltaTime();
		if (time <= FADE_TIME) {
			// Fade in
			color.a = time / FADE_TIME * BANNER_MAX_ALPHA;
			textColor.a = time / FADE_TIME * TEXT_MAX_ALPHA;
		} else if (time > FADE_TIME && time <= FADE_TIME + DISPLAY_TIME) {
			// Display
		} else if (time > FADE_TIME + DISPLAY_TIME && time <= 2 * FADE_TIME + DISPLAY_TIME) {
			// Fade out
			color.a = (1 - (time - FADE_TIME - DISPLAY_TIME) / FADE_TIME) * BANNER_MAX_ALPHA;
			textColor.a = (1 - (time - FADE_TIME - DISPLAY_TIME) / FADE_TIME) * TEXT_MAX_ALPHA;
		} else {
			return;
		}
		
		nameGL = new GlyphLayout(nameFont, name, textColor, Gdx.graphics.getWidth(), Align.center, true);
		msgGL = new GlyphLayout(msgFont, msg, textColor, Gdx.graphics.getWidth() - 2 * MSG_MARGIN, Align.center, true);
		
		bannerHeight = NAME_OFFSET + nameGL.height + LOWER_OFFSET;
		
		if (!msg.equals("")) {
			bannerHeight += MSG_OFFSET + msgGL.height;
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(0, Gdx.graphics.getHeight() - BANNER_OFFSET - bannerHeight, Gdx.graphics.getWidth(), bannerHeight);
		renderer.end();
		
		batch.begin();
		nameFont.draw(batch, nameGL, 0, Gdx.graphics.getHeight() - BANNER_OFFSET - NAME_OFFSET);
		msgFont.draw(batch, msgGL, MSG_MARGIN, Gdx.graphics.getHeight() - BANNER_OFFSET - NAME_OFFSET - nameGL.height - MSG_OFFSET);
		batch.end();
		
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
