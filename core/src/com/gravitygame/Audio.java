package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Audio {
	static Sound boost;
	
	public Audio() {
		boost = Gdx.audio.newSound(Gdx.files.internal("rockettrail.mp3"));
	}
	
	public static void playMusic(int id) {
		
	}
	
	public static void stopMusic() {
		
	}
	
	public static void playBoost() {
		
	}
	
	public static void stopBoost() {
		
	}
	
	public static void dispose() {
		boost.dispose();
	}
	
}
