package com.gravitygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class GravityGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public final int screenWidth = 640;
	public final int screenHeight = 480;
	public Music bgMusic;
	public Array<String> mapNameArray;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this, screenWidth, screenHeight));
		
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("samplebgmusic.mp3"));
		bgMusic.play();
		bgMusic.setVolume(0.0f);
		
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
		bgMusic.dispose();
	}
}
