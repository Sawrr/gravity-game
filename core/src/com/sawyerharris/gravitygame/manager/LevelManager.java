package com.sawyerharris.gravitygame.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SerializationException;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;

/**
 * Loads and manages retrieval, saving, uploading, and downloading of levels.
 * @author Sawyer
 *
 */
public class LevelManager {
	/** Name of Preferences file to load */
	private static final String PREFS_NAME = "com.sawyerharris.gravitygame.customlevels";
	/** Level folder */
	private static final String LEVELS_FOLDER = "levels/";
	/** Level list file */
	private static final String LEVELS_META_FILE = "meta/levels.txt";

	/** JSON */
	private JsonReader reader;
	private Json json;

	/** Levels data structures */
	private ObjectMap<String, Level> levels;
	private ObjectMap<String, Level> tutorialLevels;
	private ArrayList<Level> customLevels;
	private ArrayList<Level> onlineLevels;
	
	/** Level status */
	private int currentLevel;
	private boolean onTutorialLevels;
	
	/** Custom levels preferences */
	private Preferences prefs;

	/**
	 * Loads levels.
	 */
	public LevelManager() {
		reader = new JsonReader();
		json = new Json();

		loadLevels();
		loadLevelStatus();
		loadCustomLevels();
		loadOnlineLevels();
	}
	
	private void loadLevels() {
		levels = new ObjectMap<String, Level>();
		tutorialLevels = new ObjectMap<String, Level>();
		FileHandle file = Gdx.files.internal(LEVELS_META_FILE);
		JsonValue meta = reader.parse(file);
		
		String[] levelNames = meta.get("official").asStringArray();
		for (int i = 0; i < levelNames.length; i++) {
			FileHandle levelFile = Gdx.files.internal(LEVELS_FOLDER + levelNames[i]);
			Level level = json.fromJson(Level.class, levelFile);
			levels.put(levelNames[i], level);
		}
		
		String[] tutorialLevelNames = meta.get("tutorial").asStringArray();
		for (int i = 0; i < tutorialLevelNames.length; i++) {
			FileHandle levelFile = Gdx.files.internal(LEVELS_FOLDER + tutorialLevelNames[i]);
			Level level = json.fromJson(Level.class, levelFile);
			tutorialLevels.put(tutorialLevelNames[i], level);
		}
	}
	
	private void loadLevelStatus() {
		currentLevel = GravityGame.getInstance().getPlayerStatus().getHighestLevel();
		onTutorialLevels = !GravityGame.getInstance().getPlayerStatus().isTutorialCompleted();
	}
	
	private void loadCustomLevels() {
		customLevels = new ArrayList<Level>();
		prefs = Gdx.app.getPreferences(PREFS_NAME);
		String customLevelStr = prefs.getString("customLevels");
		try {
			String[] list = reader.parse(customLevelStr).asStringArray();
			for (String levelStr : list) {
				Level level = json.fromJson(Level.class, levelStr);
				customLevels.add(level);
			}
		} catch (Exception e) {
			System.out.println("Unable to load custom levels: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	private void loadOnlineLevels() {
		onlineLevels = new ArrayList<Level>();
		
		try {
			//testUpload();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			URL localhost = new URL("http://localhost:8080/?op=read&group=default");
			BufferedReader in = new BufferedReader(
			new InputStreamReader(localhost.openStream()));

			String inputLine;
			boolean begin = false;
			StringBuilder sb = new StringBuilder();
			sb.append("[\"");
			while (!begin) {
				inputLine = in.readLine();
				if (inputLine != null && inputLine.equals("begin")) {
					begin = true;
				}
			}
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("")) {
					continue;
				}
				sb.append(inputLine);
				sb.append("\",\"");
			}
			sb.delete(sb.length() - 3, sb.length() - 1);
			in.close();
			sb.append("]");
			
			String[] list = reader.parse(sb.toString()).asStringArray();
			for (String levelStr : list) {
				Level level = json.fromJson(Level.class, levelStr);
				onlineLevels.add(level);
			}
			
		} catch (Exception e) {
			System.out.println("Unable to load online levels: " + e.getMessage());
			e.printStackTrace(System.out);
		}
		
		Level level = onlineLevels.get(0);
		System.out.println(level);
	}
	
	private void testUpload() throws Exception {
		Level level = customLevels.get(0);
		String str = "http://localhost:8080/?op=write&group=default&level=";
		str += json.toJson(level);
		str += "&hash=";
		str += json.toJson(level).hashCode();
		str += "&author=sawr&pass=s17gg";
		System.out.println(str);
		URL localhost = new URL(str);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(localhost.openStream()));
	}
	
	/**
	 * Returns the level of given name.
	 * 
	 * @param levelName
	 *            name of level
	 * @return level
	 */
	public Level getLevel(String levelName) {
		if (!levels.containsKey(levelName)) {
			throw new IllegalArgumentException("Level name does not exist");
		}
		return levels.get(levelName);
	}
	
	/**
	 * Returns the tutorial level of given name.
	 * 
	 * @param tutorialLevelName
	 *            name of tutorial level
	 * @return tutorial level
	 */
	public Level getTutorialLevel(String tutorialLevelName) {
		if (!tutorialLevels.containsKey(tutorialLevelName)) {
			throw new IllegalArgumentException("Tutorial level name does not exist");
		}
		return levels.get(tutorialLevelName);
	}
	
	/**
	 * Returns list of custom levels.
	 * @return custom levels
	 */
	public ArrayList<Level> getCustomLevels() {
		return customLevels;
	}
	
	/**
	 * Returns list of user-uploaded online levels.
	 * @return online levels
	 */
	public ArrayList<Level> getOnlineLevels() {
		return onlineLevels;
	}
}
