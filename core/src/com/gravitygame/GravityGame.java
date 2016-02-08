package com.gravitygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GravityGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public final int screenWidth = 640;
	public final int screenHeight = 480;
	Music bgMusic;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this, screenWidth, screenHeight));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("samplebgmusic.mp3"));
		bgMusic.play();
		bgMusic.setVolume(0.0f);
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		bgMusic.dispose();
	}
}
