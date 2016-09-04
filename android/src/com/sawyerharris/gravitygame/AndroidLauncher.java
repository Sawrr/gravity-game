package com.sawyerharris.gravitygame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sawyerharris.gravitygame.game.GravityGame;

/**
 * Android launcher initializes GravityGame as an android application.
 * 
 * @author Sawyer Harris
 *
 */
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GravityGame(), config);
	}
}
