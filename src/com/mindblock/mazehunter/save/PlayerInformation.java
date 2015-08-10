package com.mindblock.mazehunter.save;

import android.content.Context;
import android.content.SharedPreferences;

public class PlayerInformation {

	
	public static final String NUMBER_OF_STARS = "NUMER_OF_STARS";
	
	private int totalStars;
	private int unusedStars;
	private int usedStars;
	
	public PlayerInformation(Context context){
		
		SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		this.totalStars = myPrefs.getInt(PlayerInformation.NUMBER_OF_STARS, 0);
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
	
	
}
