package com.gravitygame;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class GravityGame extends Game {
	public static GameScreen screen;
	public static int screenWidth;
	public static int screenHeight;
	public static Ship ship;
	public static Array<Mass> massArray;
	public static GameState state;

	public void create() {
		if (Gdx.app.getType() == ApplicationType.Android) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		} else {
			screenWidth = 720;
			screenHeight = 1280;
		}
		
		new Audio();
		new MapLoader();
		new InputHandler(this);
		
		this.setScreen(new MainMenuScreen(this, screenWidth, screenHeight));

	}
	
	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		Audio.dispose();
	}
}
