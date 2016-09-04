package com.sawyerharris.gravitygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sawyerharris.gravitygame.game.GravityGame;

/**
 * Desktop launcher initializes GravityGame as a Java application on desktop.
 * 
 * @author Sawyer Harris
 *
 */
public class DesktopLauncher {
	private static final int WIDTH = 720;
	private static final int HEIGHT = 1280;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;
		config.resizable = false;
		new LwjglApplication(new GravityGame(), config);
	}
}
