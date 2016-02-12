package com.gravitygame;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class GravityGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public int screenWidth;
	public int screenHeight;
	public Music bgMusic;
	public Array<String> mapNameArray;
	public Sound boostSound;

	public void create() {
		if (Gdx.app.getType() == ApplicationType.Android) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		} else {
			screenWidth = 720;
			screenHeight = 1280;
		}
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this, screenWidth, screenHeight));
		
		 boostSound = Gdx.audio.newSound(Gdx.files.internal("rockettrail.mp3"));
		//bgMusic = Gdx.audio.newMusic(Gdx.files.internal("samplebgmusic.mp3"));
		//bgMusic.play();
		//bgMusic.setVolume(0.0f);
		
		FileHandle mapListFile = Gdx.files.internal("maplist.txt");
		Json json = new Json();
		mapNameArray = json.fromJson(MapList.class, mapListFile).mapList;
	}

	public static class MapList {
		public Array<String> mapList;
	}
	
	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		//bgMusic.dispose();
		boostSound.dispose();
	}
}
