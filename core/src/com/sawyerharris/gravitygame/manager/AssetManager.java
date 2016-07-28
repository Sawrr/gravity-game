package com.sawyerharris.gravitygame.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Loads and retrieves game assets including images, sounds, and fonts.
 * 
 * @author Sawyer Harris
 *
 */
public class AssetManager implements Disposable {
	/** Names of asset files and folders */
	private static final String SHIPS_TEXTURE_FILE = "art/ships.png";
	private static final String SHIPS_META_FILE = "meta/ships.txt";
	private static final String PLANETS_TEXTURE_FILE = "art/planets.png";
	private static final String PLANETS_META_FILE = "meta/planets.txt";
	private static final String BACKGROUNDS_FOLDER = "art/backgrounds/";
	private static final String BACKGROUNDS_META_FILE = "meta/backgrounds.txt";
	private static final String SOUNDS_FOLDER = "audio/sounds/";
	private static final String SOUNDS_META_FILE = "meta/sounds.txt";
	private static final String MUSIC_FOLDER = "audio/music/";
	private static final String MUSIC_META_FILE = "meta/music.txt";
	private static final String FONT_NAME = "fonts/tcm.TTF";

	/** Asset data structures */
	private ArrayList<ObjectMap<String, Animation>> shipAnimations;
	private ObjectMap<String, TextureRegion> planets;
	private ObjectMap<String, Texture> backgrounds;
	private ObjectMap<String, Sound> sounds;
	private ObjectMap<String, Music> music;
	private ObjectMap<Integer, BitmapFont> fonts;

	/** Font generation */
	private FileHandle fontName;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter param;
	
	/** Textures from which texture regions will be extracted */
	private Texture shipsTexture;
	private Texture planetsTexture;

	/**
	 * Loads assets and prepares font generation.
	 */
	public AssetManager() {
		loadShips();
		loadPlanets();
		loadBackgrounds();
		loadSounds();
		loadMusic();
		prepareFontGenerator();
	}

	private void loadShips() {
		shipAnimations = new ArrayList<ObjectMap<String, Animation>>();
		//shipsTexture = new Texture(SHIPS_TEXTURE_FILE);
	}

	private void loadPlanets() {
		planets = new ObjectMap<String, TextureRegion>();
		//planetsTexture = new Texture(PLANETS_TEXTURE_FILE);
	}

	private void loadBackgrounds() {
		backgrounds = new ObjectMap<String, Texture>();
	}

	private void loadSounds() {
		sounds = new ObjectMap<String, Sound>();
	}

	private void loadMusic() {
		music = new ObjectMap<String, Music>();
	}
	
	private void prepareFontGenerator() {
		fonts = new ObjectMap<Integer, BitmapFont>();

		fontName = Gdx.files.internal(FONT_NAME);
		generator = new FreeTypeFontGenerator(fontName);
		param = new FreeTypeFontParameter();
	}

	public Animation getShipAnimation(int style, String animName) {
		if (style < 0 || style >= shipAnimations.size()) {
			throw new IndexOutOfBoundsException("Style index out of bounds.");
		}
		if (!shipAnimations.get(style).containsKey(animName)) {
			throw new IllegalArgumentException("Animation name does not exist");
		}
		return shipAnimations.get(style).get(animName);
	}

	public TextureRegion getPlanet(String planetName) {
		if (!planets.containsKey(planetName)) {
			throw new IllegalArgumentException("Planet name does not exist");
		}
		return planets.get(planetName);
	}

	public Texture getBackground(String backgroundName) {
		if (!backgrounds.containsKey(backgroundName)) {
			throw new IllegalArgumentException("Background name does not exist");
		}
		return backgrounds.get(backgroundName);
	}

	public Sound getSound(String soundName) {
		if (!sounds.containsKey(soundName)) {
			throw new IllegalArgumentException("Sound name does not exist");
		}
		return sounds.get(soundName);
	}

	public Music getMusic(String musicName) {
		if (!music.containsKey(musicName)) {
			throw new IllegalArgumentException("Music does not exist");
		}
		return music.get(musicName);
	}

	public BitmapFont getFont(int size) {
		if (!fonts.containsKey(size)) {
			param.size = size;
			fonts.put(size, generator.generateFont(param));
		}
		return fonts.get(size);
	}

	public TextureRegion getTestRegion() {
		Texture texture = new Texture(Gdx.files.internal("test.png"));
		return new TextureRegion(texture);
	}

	@Override
	public void dispose() {
		//shipsTexture.dispose();
		shipAnimations.clear();
		//planetsTexture.dispose();
		planets.clear();
		for (ObjectMap.Entry entry : backgrounds) {
			Texture text = (Texture) entry.value;
			text.dispose();
		}
		for (ObjectMap.Entry entry : sounds) {
			Sound sound = (Sound) entry.value;
			sound.dispose();
		}
		for (ObjectMap.Entry entry : music) {
			Music music = (Music) entry.value;
			music.dispose();
		}
		for (ObjectMap.Entry entry : fonts) {
			BitmapFont font = (BitmapFont) entry.value;
			font.dispose();
		}
	}
}
