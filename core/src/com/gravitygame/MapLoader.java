package com.gravitygame;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MapLoader {
	private static final String mapListName = "maplist.txt";
	private static Json json;
	private static FileHandle mapListFile;
	
	public static Array<String> mapNameArray;
	
	public MapLoader() {
		mapListFile = Gdx.files.internal(mapListName);
		json = new Json();
		mapNameArray = json.fromJson(MapList.class, mapListFile).mapList;
	}
	
	public static void loadMap(int id) {
		
	}
	
	private static class MapList {
		public Array<String> mapList;
	}
	
	
}
