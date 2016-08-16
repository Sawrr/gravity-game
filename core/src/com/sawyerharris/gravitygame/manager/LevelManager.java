package com.sawyerharris.gravitygame.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.sawyerharris.gravitygame.game.GravityGame;
import com.sawyerharris.gravitygame.game.Level;
import com.sawyerharris.gravitygame.game.PlayerStatus;

/**
 * Loads and manages retrieval, saving, uploading, and downloading of levels.
 * 
 * @author Sawyer Harris
 *
 */
public class LevelManager {
	private final GravityGame game = GravityGame.getInstance();
	
	/** URL of level server */
	private static final String LEVEL_SERVER = "http://localhost:8080/?";

	/** Name of Preferences file to load */
	private static final String PREFS_NAME = "com.sawyerharris.gravitygame.customlevels";
	/** Level folder */
	private static final String LEVELS_FOLDER = "levels/";
	/** Level list file */
	private static final String LEVELS_META_FILE = "meta/levels.txt";

	/** JSON */
	private JsonReader reader;
	private Json json;

	/** Levels lists */
	private ArrayList<Level> levels;
	private ArrayList<Level> tutorialLevels;
	private ArrayList<Level> customLevels;
	private ArrayList<Level> onlineLevels;

	/** Level status */
	private int currentLevel;
	private boolean onTutorialLevels;

	/** Custom levels preferences */
	private Preferences prefs;

	/** List of level indexes that correspond to ship style unlocks */
	ArrayList<Integer> styleUnlockLevelIndexList;

	/**
	 * Loads levels.
	 */
	public LevelManager() {
		reader = new JsonReader();
		json = new Json();

		prepareShipStyleUnlockList();
		loadLevels();
		loadLevelStatus();
		loadCustomLevels();
		loadOnlineLevels();
	}

	private boolean checkLevel(Level l) {
		if (l.getName() == null || l.getAuthor() == null || l.getShipOrigin() == null || l.getTheme() == null
				|| l.getType() == null || l.getPlanets() == null) {
			return false;
		}
		return true;
	}

	private void prepareShipStyleUnlockList() {
		styleUnlockLevelIndexList = new ArrayList<Integer>();
		styleUnlockLevelIndexList.add(1);
		styleUnlockLevelIndexList.add(3);
		styleUnlockLevelIndexList.add(6);
	}

	private void loadLevels() {
		levels = new ArrayList<Level>();
		tutorialLevels = new ArrayList<Level>();
		FileHandle file = Gdx.files.internal(LEVELS_META_FILE);
		JsonValue meta = reader.parse(file);

		String[] levelNames = meta.get("official").asStringArray();
		for (int i = 0; i < levelNames.length; i++) {
			FileHandle levelFile = Gdx.files.internal(LEVELS_FOLDER + levelNames[i]);
			Level level = json.fromJson(Level.class, levelFile);
			levels.add(level);
		}

		String[] tutorialLevelNames = meta.get("tutorial").asStringArray();
		for (int i = 0; i < tutorialLevelNames.length; i++) {
			FileHandle levelFile = Gdx.files.internal(LEVELS_FOLDER + tutorialLevelNames[i]);
			Level level = json.fromJson(Level.class, levelFile);
			tutorialLevels.add(level);
		}
	}

	private void loadLevelStatus() {
		currentLevel = game.getPlayerStatus().getHighestLevel();
		onTutorialLevels = !game.getPlayerStatus().isTutorialCompleted();
	}

	private void loadCustomLevels() {
		customLevels = new ArrayList<Level>();
		prefs = Gdx.app.getPreferences(PREFS_NAME);
		String customLevelStr = prefs.getString("customLevels");
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Level> list = json.fromJson(ArrayList.class, Level.class, customLevelStr);
			if (list != null) {
				for (Level level : list) {
					if (!checkLevel(level)) {
						list.remove(level);
					}
				}
				customLevels = list;
			}
		} catch (SerializationException e) {
			System.out.println("Unable to load custom levels.");
			e.printStackTrace(System.out);
		}
	}

	private void loadOnlineLevels() {
		onlineLevels = new ArrayList<Level>();

		BufferedReader in;
		try {
			URL url = new URL(LEVEL_SERVER + "op=read&group=default");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			boolean begin = false;
			while (!begin) {
				inputLine = in.readLine();
				if (inputLine == null) {
					System.out.println("Stream ended before begin was detected.");
					return;
				}
				if (inputLine.equals("begin")) {
					begin = true;
				}
			}

			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("")) {
					continue;
				}
				try {
					Level level = json.fromJson(Level.class, inputLine);
					if (checkLevel(level)) {
						onlineLevels.add(level);
					}
				} catch (SerializationException e) {
					// Invalid level formatting
					e.printStackTrace(System.out);
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Unable to connect to level server.");
			e.printStackTrace(System.out);
			return;
		}
	}

	/**
	 * Called by LevelPlayScreen when the current level is beaten. Checks if
	 * highest level unlocked needs to be updated and also checks for any space
	 * ship style unlocks.
	 * 
	 * @return -1 if no unlock, otherwise returns the index of the space ship
	 *         style unlocked
	 */
	public int levelCompleted() {
		PlayerStatus status = game.getPlayerStatus();
		if (onTutorialLevels) {
			if (currentLevel == tutorialLevels.size() - 1) {
				// Tutorial completed
				currentLevel = 0;
				onTutorialLevels = false;
				status.setTutorialCompleted(true);
				status.flush();
				System.out.println("tut completed");
			} else {
				// Advance current level index to next tutorial level
				System.out.println("tut advance");
				currentLevel++;
			}
			return -1;
		} else {
			currentLevel++;
			// Check for new highest level
			if (currentLevel > status.getHighestLevel()) {
				status.setHighestLevel(currentLevel);
				status.flush();
			}
			// Check for ship style unlocks
			for (Integer index : styleUnlockLevelIndexList) {
				if (currentLevel == index) {
					status.setHighestShipStyle(index);
					status.flush();
					return index;
				}
			}
			return -1;
		}
	}

	/**
	 * Returns the level of index currentLevel.
	 * 
	 * @return current level
	 */
	public Level nextLevel() {
		if (onTutorialLevels) {
			return tutorialLevels.get(currentLevel);
		} else {
			if (currentLevel == levels.size()) {
				// There are no more levels; game has been beaten
				return null;
			} else {
				return levels.get(currentLevel);
			}
		}
	}

	/**
	 * Returns the index of the current level (official or tutorial).
	 * 
	 * @return index of current level
	 */
	public int getCurrentLevelIndex() {
		return currentLevel;
	}

	/**
	 * Sets the index of the current level
	 * 
	 * @param currentLevel
	 *            index of current level to set
	 */
	public void setCurrentLevelIndex(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * Gets whether the current level index refers to the tutorial levels list.
	 * 
	 * @return true if the current level index is on tutorial levels
	 */
	public boolean isOnTutorialLevels() {
		return onTutorialLevels;
	}

	/**
	 * Sets whether the current level index refers to the tutorial levels list.
	 * 
	 * @param onTutorialLevels
	 *            true if index should refer to tutorial levels
	 */
	public void setOnTutorialLevels(boolean onTutorialLevels) {
		this.onTutorialLevels = onTutorialLevels;
	}

	/**
	 * Saves a custom level at the given index.
	 * 
	 * @param index
	 *            index in which to save
	 * @param level
	 *            custom level to save
	 * @throws IndexOutOfBoundsException
	 *             if index < 0 || index >= customLevels.size()
	 */
	public void saveLevel(int index, Level level) throws IndexOutOfBoundsException {
		if (index == customLevels.size()) {
			customLevels.add(index, level);
		} else {
			customLevels.set(index, level);
		}
		prefs.putString("customLevels", json.toJson(customLevels));
		prefs.flush();
	}

	/**
	 * Uploads the given level to the database.
	 * 
	 * @param level
	 *            Level to be uploaded
	 * @return true if level was successfully uploaded
	 */
	public boolean uploadLevel(Level level) {
		// Check for valid level
		if (level == null) {
			return false;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(LEVEL_SERVER);
		sb.append("op=write&group=default&level=");
		sb.append(json.toJson(level));
		sb.append("&hash=");
		sb.append(json.toJson(level).hashCode());
		sb.append("&author=");
		sb.append(level.getAuthor());
		sb.append("&pass=s17gg");
		try {
			URL url = new URL(sb.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			in.close();
			loadOnlineLevels();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Returns list of official levels.
	 * 
	 * @return levels
	 */
	public ArrayList<Level> getOfficialLevels() {
		return levels;
	}

	/**
	 * Return list of tutorial levels.
	 * 
	 * @return tutorialLevels
	 */
	public ArrayList<Level> getTutorialLevels() {
		return tutorialLevels;
	}

	/**
	 * Returns list of custom levels.
	 * 
	 * @return custom levels
	 */
	public ArrayList<Level> getCustomLevels() {
		return customLevels;
	}

	/**
	 * Returns list of user-uploaded online levels.
	 * 
	 * @return online levels
	 */
	public ArrayList<Level> getOnlineLevels() {
		return onlineLevels;
	}
}
