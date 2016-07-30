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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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

	/** JSON Reader */
	private JsonReader reader;

	/**
	 * Loads assets and prepares font generation.
	 */
	public AssetManager() {
		reader = new JsonReader();

		loadShips();
		loadPlanets();
		loadBackgrounds();
		loadSounds();
		loadMusic();
		prepareFontGenerator();
	}

	private void loadShips() {
		shipAnimations = new ArrayList<ObjectMap<String, Animation>>();
		shipsTexture = new Texture(SHIPS_TEXTURE_FILE);
		FileHandle file = Gdx.files.internal(SHIPS_META_FILE);
		JsonValue meta = reader.parse(file);

		int spriteDim = meta.getInt("spriteDim");
		int animDuration = meta.getInt("animDuration");
		int numBoostFrames = meta.getInt("numBoostFrames");
		String[] styles = meta.get("shipStyleList").asStringArray();

		for (int i = 0; i < styles.length; i++) {
			Animation def = new Animation(0, new TextureRegion(shipsTexture, 0, i * spriteDim, spriteDim, spriteDim));
			TextureRegion[] boostFrames = new TextureRegion[numBoostFrames];
			for (int j = 0; j < numBoostFrames; j++) {
				boostFrames[j] = new TextureRegion(shipsTexture, spriteDim * (1 + j), i * spriteDim, spriteDim,
						spriteDim);
			}
			Animation boost = new Animation(animDuration, boostFrames);
			ObjectMap<String, Animation> map = new ObjectMap<String, Animation>();
			map.put("default", def);
			map.put("boost", boost);
			shipAnimations.add(map);
		}
	}

	private void loadPlanets() {
		planets = new ObjectMap<String, TextureRegion>();
		planetsTexture = new Texture(PLANETS_TEXTURE_FILE);
		FileHandle file = Gdx.files.internal(PLANETS_META_FILE);
		JsonValue meta = reader.parse(file);

		int spriteDim = meta.getInt("spriteDim");
		String[] planetList = meta.get("planetList").asStringArray();

		for (int i = 0; i < planetList.length; i++) {
			TextureRegion planet = new TextureRegion(planetsTexture, 0, spriteDim * i, spriteDim, spriteDim);
			planets.put(planetList[i], planet);
		}
	}

	private void loadBackgrounds() {
		backgrounds = new ObjectMap<String, Texture>();
		FileHandle file = Gdx.files.internal(BACKGROUNDS_META_FILE);
		JsonValue meta = reader.parse(file);

		String[] bgs = meta.asStringArray();
		for (int i = 0; i < bgs.length; i++) {
			FileHandle bgFile = Gdx.files.internal(BACKGROUNDS_FOLDER + bgs[i]);
			backgrounds.put(bgs[i], new Texture(bgFile));
		}
	}

	private void loadSounds() {
		sounds = new ObjectMap<String, Sound>();
		FileHandle file = Gdx.files.internal(SOUNDS_META_FILE);
		JsonValue meta = reader.parse(file);

		String[] soundNames = meta.asStringArray();
		for (int i = 0; i < soundNames.length; i++) {
			FileHandle soundFile = Gdx.files.internal(SOUNDS_FOLDER + soundNames[i]);
			sounds.put(soundNames[i], Gdx.audio.newSound(soundFile));
		}
	}

	private void loadMusic() {
		music = new ObjectMap<String, Music>();
		FileHandle file = Gdx.files.internal(MUSIC_META_FILE);
		JsonValue meta = reader.parse(file);

		String[] musicNames = meta.asStringArray();
		for (int i = 0; i < musicNames.length; i++) {
			FileHandle musicFile = Gdx.files.internal(MUSIC_FOLDER + musicNames[i]);
			music.put(musicNames[i], Gdx.audio.newMusic(musicFile));
		}
	}

	private void prepareFontGenerator() {
		fonts = new ObjectMap<Integer, BitmapFont>();

		fontName = Gdx.files.internal(FONT_NAME);
		generator = new FreeTypeFontGenerator(fontName);
		param = new FreeTypeFontParameter();
	}

	/**
	 * Returns the ship animation of the given style and animation name.
	 * 
	 * @param style
	 *            index of ship style
	 * @param animName
	 *            animation name
	 * @return ship animation
	 */
	public Animation getShipAnimation(int style, String animName) {
		if (style < 0 || style >= shipAnimations.size()) {
			throw new IndexOutOfBoundsException("Style index out of bounds.");
		}
		if (!shipAnimations.get(style).containsKey(animName)) {
			throw new IllegalArgumentException("Animation name does not exist");
		}
		return shipAnimations.get(style).get(animName);
	}

	/**
	 * Returns the planet texture region of the given name.
	 * 
	 * @param planetName
	 *            name of planet
	 * @return planet texture region
	 */
	public TextureRegion getPlanet(String planetName) {
		if (!planets.containsKey(planetName)) {
			throw new IllegalArgumentException("Planet name does not exist");
		}
		return planets.get(planetName);
	}

	/**
	 * Returns the background texture of the given file name.
	 * 
	 * @param backgroundName
	 *            name of background image file
	 * @return background texture
	 */
	public Texture getBackground(String backgroundName) {
		if (!backgrounds.containsKey(backgroundName)) {
			throw new IllegalArgumentException("Background name does not exist");
		}
		return backgrounds.get(backgroundName);
	}

	/**
	 * Returns the sound of given file name.
	 * 
	 * @param soundName
	 *            name of sound file
	 * @return sound
	 */
	public Sound getSound(String soundName) {
		if (!sounds.containsKey(soundName)) {
			throw new IllegalArgumentException("Sound name does not exist");
		}
		return sounds.get(soundName);
	}

	/**
	 * Returns the music of given file name.
	 * 
	 * @param musicName
	 *            name of sound file
	 * @return music
	 */
	public Music getMusic(String musicName) {
		if (!music.containsKey(musicName)) {
			throw new IllegalArgumentException("Music does not exist");
		}
		return music.get(musicName);
	}

	/**
	 * Returns a bitmap font of the given font size. If a font of the given size
	 * does not exist, it will be created.
	 * 
	 * @param size
	 *            font size
	 * @return bitmap font
	 */
	public BitmapFont getFont(int size) {
		if (!fonts.containsKey(size)) {
			param.size = size;
			fonts.put(size, generator.generateFont(param));
		}
		return fonts.get(size);
	}

	@Override
	public void dispose() {
		shipsTexture.dispose();
		shipAnimations.clear();
		planetsTexture.dispose();
		planets.clear();
		for (ObjectMap.Entry<String, Texture> entry : backgrounds) {
			Texture text = (Texture) entry.value;
			text.dispose();
		}
		for (ObjectMap.Entry<String, Sound> entry : sounds) {
			Sound sound = (Sound) entry.value;
			sound.dispose();
		}
		for (ObjectMap.Entry<String, Music> entry : music) {
			Music music = (Music) entry.value;
			music.dispose();
		}
		for (ObjectMap.Entry<Integer, BitmapFont> entry : fonts) {
			BitmapFont font = (BitmapFont) entry.value;
			font.dispose();
		}
	}
}
