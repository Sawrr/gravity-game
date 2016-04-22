package com.sawyerharris.gravitygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
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
	/** Name of art folder in assets */
	private static final String ART_FOLDER = "art/";
	/** Json object for reading files */
	private static final Json JSON = new Json();
	
	/**
	 * Container class for list of themes
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private static class ThemeList {
		public ArrayList<String> themeNames;
	}
	
	/**
	 * Loads themes into Map parameter
	 * @param themes
	 */
	public static void loadThemes(Map<String, Theme> themes) {				
		FileHandle themeListFile = Gdx.files.internal(THEME_LIST);
		ArrayList<String> themeList;
		if (themeListFile.exists()) {
			themeList = JSON.fromJson(ThemeList.class, themeListFile).themeNames;
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
		public ArrayList<String> levelNames;
	}
	
	/**
	 * Loads levels into Map parameter
	 * @param levels
	 */
	public static void loadLevels(Map<String, Level> levels, List<String> levelNames) {				
		FileHandle levelListFile = Gdx.files.internal(LEVEL_LIST);
		if (levelListFile.exists()) {
			ArrayList<String> list = JSON.fromJson(LevelList.class, levelListFile).levelNames;
			levelNames.addAll(list);
			FileHandle levelDir = Gdx.files.internal(LEVEL_FOLDER);
			if (levelDir.exists()) {
				for (String name : levelNames) {
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
	/**
	 * Loads background and planet textures from themes
	 * @param textures
	 * @param themes
	 */
	public static void loadThemeTextures(Map<String, Texture> textures, Map<String, Theme> themes) {
		for (Map.Entry<String, Theme> theme : themes.entrySet()) {
			String background = theme.getValue().getBackground();
			try {
				Texture bg = new Texture(ART_FOLDER + background);
				textures.put(background, bg);
			} catch (GdxRuntimeException e) {
				System.out.println("Error: background image not found: " + background);
				System.exit(1);
			}
			
			String planet = theme.getValue().getPlanet();
			try {
				Texture pl = new Texture(ART_FOLDER + planet);
				textures.put(planet, pl);
			} catch (GdxRuntimeException e) {
				System.out.println("Error: planet image not found: " + planet);
				System.exit(1);
			}
		}
	}
}