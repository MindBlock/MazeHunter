package com.mindblock.mazehunter.save;

import android.content.Context;
import android.content.SharedPreferences;

public class PlayerInformation {

	
	public static final String NUMBER_OF_STARS = "NUMER_OF_STARS";
	public static final String SPEED_MULTIPLIER = "SPEED_MULTIPLIER";
	public static final String UNUSED_STARS = "UNUSED_STARS";
	private Context context;
	
	private int totalStars;
	private int unusedStars;
	private int speedMultiplier;
	
	public PlayerInformation(Context context){
		
		SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		this.totalStars = myPrefs.getInt(PlayerInformation.NUMBER_OF_STARS, 0);
		this.speedMultiplier = myPrefs.getInt(PlayerInformation.SPEED_MULTIPLIER, 2);
		this.unusedStars = myPrefs.getInt(PlayerInformation.UNUSED_STARS, this.totalStars);
		this.context = context;
	}

	public int getTotalStars() {
		return totalStars;
	}

	public int getUnusedStars() {
		return unusedStars;
	}

	public void setUnusedStars(int unusedStars) {
		this.unusedStars = unusedStars;
		
		SharedPreferences myPrefs = this.context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor e = myPrefs.edit();
		
		e.putInt(PlayerInformation.UNUSED_STARS, this.unusedStars);
		e.commit();
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
