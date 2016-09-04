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

/**
 * The header of a level that displays when the level is being played.
 * 
 * @author Sawyer Harris
 *
 */
public class LevelHeader {
	/** Singleton instance of game */
	private final GravityGame game = GravityGame.getInstance();

	/** Time in seconds for header to fade in/out and display */
	private static final float FADE_TIME = 1f;
	private static final float DISPLAY_TIME = 3f;

	/** Font sizes for level name and message */
	private static final int NAME_FONT_SIZE = 38;
	private static final int MSG_FONT_SIZE = 32;

	/** Color of header text */
	private static final Color TEXT_COLOR = Color.WHITE;

	/** Maximum color alpha values to fade in to */
	public static final float BANNER_MAX_ALPHA = 0.6f;
	public static final float TEXT_MAX_ALPHA = 1f;

	/** Margins for banner elements */
	private static final int MSG_MARGIN = 100;
	private static final int BANNER_OFFSET = 160;
	private static final int NAME_OFFSET = 40;
	private static final int LOWER_OFFSET = 40;
	private static final int MSG_OFFSET = 40;

	/** Batch and shape renderer */
	private Batch batch;
	private ShapeRenderer renderer;

	/** Time since start of header display */
	private float time;

	/** Glyph layouts for name and message */
	private GlyphLayout nameGL;
	private GlyphLayout msgGL;

	/** Fonts for name and message */
	private BitmapFont nameFont;
	private BitmapFont msgFont;

	/** Height of banner, based on font size and message */
	private float bannerHeight;

	/** Colors of banner and text */
	private Color color;
	private Color textColor;

	/** The name and message of the level */
	private String name;
	private String msg;

	/**
	 * Constructs a level header with the given batch and shape renderer.
	 * 
	 * @param batch
	 * @param renderer
	 */
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

	/**
	 * Sets the name and message text to be displayed.
	 * 
	 * @param name
	 *            level name
	 * @param msg
	 *            level message
	 */
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

	/**
	 * Sets the color of the banner.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = new Color(color);
	}

	/**
	 * Starts to header display and resets time to zero.
	 */
	public void show() {
		time = 0;
	}

	/**
	 * Stops the header display by setting time to > the total display time
	 */
	public void hide() {
		time = 2 * FADE_TIME + DISPLAY_TIME + 1;
	}

	/**
	 * Draws the header based on the time.
	 */
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
		renderer.rect(0, Gdx.graphics.getHeight() - BANNER_OFFSET - bannerHeight, Gdx.graphics.getWidth(),
				bannerHeight);
		renderer.end();

		batch.begin();
		nameFont.draw(batch, nameGL, 0, Gdx.graphics.getHeight() - BANNER_OFFSET - NAME_OFFSET);
		msgFont.draw(batch, msgGL, MSG_MARGIN,
				Gdx.graphics.getHeight() - BANNER_OFFSET - NAME_OFFSET - nameGL.height - MSG_OFFSET);
		batch.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
