package com.sawyerharris.gravitygame.database;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Date;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Objectify;


public class LevelWriter {

	private static final String PASSWORD = "s17gg";

	public LevelWriter() {
	    ObjectifyService.register(LevelGroup.class);
		ObjectifyService.register(Level.class);
	}

	public void write(String group, String level, int hashCode, String password, String author) {
		if (group == null || level == null || author == null || group.equals("") || level.equals("") || author.equals("")) {
			// One of the fields is null or empty
			return;
		}
		
		if (!password.equals(PASSWORD)) {
			// Wrong password
			return;
		}
		
		if (!hashCodeCheck(hashCode, level)) {
			// Level's hashcode does not match the submitted code
			return;
		}
		
		Level uploadLevel = new Level(group, level, author);

		// Use Objectify to save the level and now() is used to make the call synchronously
		ObjectifyService.ofy().save().entity(uploadLevel).now();
	}
	
	private boolean hashCodeCheck(int code, String str) {
		return str.hashCode() == code;
	}
}