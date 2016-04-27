package com.sawyerharris.gravitygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sawyerharris.gravitygame.GravityGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GravityGame.DESKTOP_SCREEN_WIDTH;
		config.height = GravityGame.DESKTOP_SCREEN_HEIGHT;
		config.resizable = false;
		new LwjglApplication(new GravityGame(), config);
	}
}
