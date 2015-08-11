package com.mindblock.mazehunter.save;

import android.content.Context;
import android.content.SharedPreferences;

public class PlayerInformation {

	
	public static final String NUMBER_OF_STARS = "NUMER_OF_STARS";
	public static final String SPEED_MULTIPLIER = "SPEED_MULTIPLIER";
	private Context context;
	
	private int totalStars;
	private int unusedStars;
	private int usedStars;
	private int speedMultiplier;
	
	public PlayerInformation(Context context){
		
		SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		this.totalStars = myPrefs.getInt(PlayerInformation.NUMBER_OF_STARS, 0);
		this.speedMultiplier = myPrefs.getInt(PlayerInformation.SPEED_MULTIPLIER, 2);
		this.context = context;
	}

	public int getTotalStars() {
		return totalStars;
	}

	public void setTotalStars(int totalStars) {
		this.totalStars = totalStars;
	}

	public int getUnusedStars() {
		return unusedStars;
	}

	public void setUnusedStars(int unusedStars) {
		this.unusedStars = unusedStars;
	}

	public int getUsedStars() {
		return usedStars;
	}

	public void setUsedStars(int usedStars) {
		this.usedStars = usedStars;
	}

	public int getSpeedMultiplier() {
		return speedMultiplier;
	}

	public void setSpeedMultiplier(int speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
		
		SharedPreferences myPrefs = this.context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor e = myPrefs.edit();
		
		e.putInt(PlayerInformation.SPEED_MULTIPLIER, speedMultiplier);
		e.commit();
	}
	
	
}
