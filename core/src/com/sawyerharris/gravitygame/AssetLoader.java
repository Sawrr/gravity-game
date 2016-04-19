package com.sawyerharris.gravitygame;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Class with methods to load themes and levels
 * Internal directories can't be listed, so instead
 * I'm using this asinine system
 * 
 * @author Sawyer Harris
 *
 */
public class AssetLoader {
	/** Name of file containing theme names */
	private static final String THEME_LIST = "themes.txt";
	/** Name of themes folder in assets */
	private static final String THEME_FOLDER = "themes/";
	/** Name of file containing level names */
	private static final String LEVEL_LIST = "levels.txt";
	/** Name of levels folder in assets */
	private static final String LEVEL_FOLDER = "levels/";
	/** Json object for reading files */
	private static final Json JSON = new Json();
	
	/**
	 * Container class for list of themes
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private static class ThemeList {
		public String[] themeList;
	}
	
	/**
	 * Loads themes into HashMap parameter
	 * @param themes
	 */
	public static void loadThemes(HashMap<String, Theme> themes) {				
		FileHandle themeListFile = Gdx.files.internal(THEME_LIST);
		String[] themeList;
		if (themeListFile.exists()) {
			themeList = JSON.fromJson(ThemeList.class, themeListFile).themeList;
			FileHandle themeDir = Gdx.files.internal(THEME_FOLDER);
			if (themeDir.exists()) {
				for (String name : themeList) {
					FileHandle file = Gdx.files.internal(THEME_FOLDER + name);
					Theme theme = JSON.fromJson(Theme.class, file);
					themes.put(theme.getName(), theme);
				}
			} else {
				System.out.println("Error: Theme folder not found");
				System.exit(1);
			}
		} else {
			System.out.println("Error: Theme list not found");
			System.exit(1);
		}
	}
	
	/**
	 * Container class for list of levels
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private static class LevelList {
		public String[] levelList;
	}
	
	/**
	 * Loads levels into HashMap parameter
	 * @param levels
	 */
	public static void loadLevels(HashMap<String, Level> levels) {				
		FileHandle levelListFile = Gdx.files.internal(LEVEL_LIST);
		String[] levelList;
		if (levelListFile.exists()) {
			levelList = JSON.fromJson(LevelList.class, levelListFile).levelList;
			FileHandle levelDir = Gdx.files.internal(LEVEL_FOLDER);
			if (levelDir.exists()) {
				for (String name : levelList) {
					FileHandle file = Gdx.files.internal(LEVEL_FOLDER + name);
					Level level = JSON.fromJson(Level.class, file);
					levels.put(level.getName(), level);
				}
			} else {
				System.out.println("Error: Level folder not found");
				System.exit(1);
			}
		} else {
			System.out.println("Error: Level list not found");
			System.exit(1);
		}
	}
}