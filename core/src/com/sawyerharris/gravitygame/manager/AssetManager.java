package com.sawyerharris.gravitygame.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

	public AssetManager() {
		// TODO Auto-generated constructor stub
	}

	public Animation getShipAnimation(int style, String animName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTheme(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public Texture getPlanet(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public FileHandle getFont() {
		return Gdx.files.internal("fonts/tcm.TTF");
	}

	public TextureRegion getTestRegion() {
		Texture texture = new Texture(Gdx.files.internal("test.png"));
		return new TextureRegion(texture);
	}

}
