package com.sawyerharris.gravitygame.database;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class LevelGroup {
  @Id public String levelGroup;
}