package com.sawyerharris.gravitygame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Contains data that describe the player's status with the game.
 * 
 * @author Sawyer Harris
 *
 */
public class PlayerStatus {
	/** Name of Preferences file to load */
	private static final String PREFS_NAME = "com.sawyerharris.gravitygame.playerstatus";

	/** Preferences file that saves player status */
	private Preferences prefs;
	/** Index of highest level reached */
	private int highestLevel;
	/** Index of highest ship style unlocked */
	private int highestShipStyle;
	/** Index of current ship style */
	private int shipStyle;
	/** Whether tutorial has been completed */
	private boolean tutorialCompleted;
	/** Whether sound is disabled */
	private boolean soundOff;

	/**
	 * Loads player status or creates a new one with default values.
	 */
	public PlayerStatus() {
		prefs = Gdx.app.getPreferences(PREFS_NAME);
		highestLevel = prefs.getInteger("highestLevel");
		highestShipStyle = prefs.getInteger("highestShipStyle");
		shipStyle = prefs.getInteger("shipStyle");
		tutorialCompleted = prefs.getBoolean("tutorialCompleted");
		soundOff = prefs.getBoolean("soundOff");
	}

	/**
	 * Gets the index of the highest level the player has reached.
	 * 
	 * @return highestLevel
	 */
	public int getHighestLevel() {
		return highestLevel;
	}

	/**
	 * Sets the index of the highest level the player has reached.
	 * 
	 * @param highestLevel
	 */
	public void setHighestLevel(int highestLevel) {
		this.highestLevel = highestLevel;
	}

	/**
	 * Gets the index of the highest ship style unlocked.
	 * 
	 * @return highestShipStyle
	 */
	public int getHighestShipStyle() {
		return highestShipStyle;
	}

	/**
	 * Sets the index of the highest ship style unlocked.
	 * 
	 * @param highestShipStyle
	 */
	public void setHighestShipStyle(int highestShipStyle) {
		this.highestShipStyle = highestShipStyle;
	}

	/**
	 * Gets the index of the current ship style.
	 * 
	 * @return shipStyle
	 */
	public int getShipStyle() {
		return shipStyle;
	}

	/**
	 * Sets the index of the current ship style.
	 * 
	 * @param shipStyle
	 */
	public void setShipStyle(int shipStyle) {
		this.shipStyle = shipStyle;
	}

	/**
	 * Gets if the tutorial is completed.
	 * 
	 * @return true if tutorial completed
	 */
	public boolean isTutorialCompleted() {
		return tutorialCompleted;
	}

	/**
	 * Sets if the tutorial is completed.
	 * 
	 * @param tutorialCompleted
	 */
	public void setTutorialCompleted(boolean tutorialCompleted) {
		this.tutorialCompleted = tutorialCompleted;
	}

	/**
	 * Gets if the sound is off.
	 * 
	 * @return true if sound off
	 */
	public boolean isSoundOff() {
		return soundOff;
	}

	/**
	 * Sets if the sound is off.
	 * 
	 * @param soundOff
	 */
	public void setSoundOff(boolean soundOff) {
		this.soundOff = soundOff;
	}

	/**
	 * Flushes changes to the preferences file.
	 */
	public void flush() {
		prefs.putInteger("highestLevel", highestLevel);
		prefs.putInteger("highestShipStyle", highestShipStyle);
		prefs.putInteger("shipStyle", shipStyle);
		prefs.putBoolean("tutorialCompleted", tutorialCompleted);
		prefs.putBoolean("soundOff", soundOff);
		prefs.flush();
	}

}
