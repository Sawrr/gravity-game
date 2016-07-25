package com.sawyerharris.gravitygame.database;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.lang.String;
import java.util.Date;
import java.util.List;

@Entity
public class Level {
	@Parent Key<LevelGroup> group;
	@Id public Long id;

	public String author;
	public String level;
	@Index public Date date;

	public Level() {
	
	}
	
	public Level(String group, String level, String author) {
		this.date = new Date();
		this.author = author;
		this.level = level;
		this.group = Key.create(LevelGroup.class, group);  // Creating the Ancestor key
	}
}