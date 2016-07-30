package com.sawyerharris.gravitygame.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.sawyerharris.gravitygame.game.Theme;

/**
 * Loads and retrieves level themes.
 * 
 * @author Sawyer Harris
 *
 */
public class ThemeManager {

	/** Theme folder */
	private static final String THEMES_FOLDER = "themes/";
	/** Theme list file */
	private static final String THEMES_META_FILE = "meta/themes.txt";

	/** JSON */
	private JsonReader reader;
	private Json json;

	/** Themes data structure */
	private ObjectMap<String, Theme> themes;

	/**
	 * Loads themes.
	 */
	public ThemeManager() {
		reader = new JsonReader();
		json = new Json();

		themes = new ObjectMap<String, Theme>();
		FileHandle file = Gdx.files.internal(THEMES_META_FILE);
		JsonValue meta = reader.parse(file);

		String[] themeNames = meta.asStringArray();
		for (int i = 0; i < themeNames.length; i++) {
			FileHandle themeFile = Gdx.files.internal(THEMES_FOLDER + themeNames[i]);
			Theme theme = json.fromJson(Theme.class, themeFile);
			themes.put(theme.getName(), theme);
		}
	}

	/**
	 * Returns the theme of given name.
	 * 
	 * @param themeName
	 *            name of theme
	 * @return theme
	 */
	public Theme getTheme(String themeName) {
		if (!themes.containsKey(themeName)) {
			throw new IllegalArgumentException("Theme name does not exist");
		}
		return themes.get(themeName);
	}
}
