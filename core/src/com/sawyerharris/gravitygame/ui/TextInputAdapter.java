package com.sawyerharris.gravitygame.ui;

import com.badlogic.gdx.Input.TextInputListener;

/**
 * Simple text input adapter allows for pop-up text input. Should be
 * instantiated anonymously to override methods.
 * 
 * @author Sawyer Harris
 *
 */
public class TextInputAdapter implements TextInputListener {

	@Override
	public void input(String text) {
	}

	@Override
	public void canceled() {
	}
}
