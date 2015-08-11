package com.mindblock.mazehunter.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mindblock.mazehunter.maze.TheMazeEasy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LevelCompletion {

	private final int NUMBER_OF_MAZES = 3;
	private final String MAZE_FRAGMENT_PREFIX = "MAZE_FRAGMENT_";
	private final String MAZE_LEVEL_PREFIX = "_LEVEL_";
	
	private List<Map<Integer, String>> mazeCompletionList;
	
	
	
	public LevelCompletion(){
		this.mazeCompletionList = new ArrayList<Map<Integer,String>>();
	}
	
	
	
	/** clears all current information and loads level information from the shared preferences
	 * 
	 * @param context context of activity
	 */
	public void load(Context context){
		this.mazeCompletionList.clear();
		
		SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		
		for (int mazeListIndex = 0; mazeListIndex < this.NUMBER_OF_MAZES; mazeListIndex++){
			Map<Integer, String> levelMap = new HashMap<Integer, String>();
			for (int level = 1; level <= TheMazeEasy.LEVELS_TOTAL; level ++){
				String completion = myPrefs.getString(this.MAZE_FRAGMENT_PREFIX + (mazeListIndex+1) + this.MAZE_LEVEL_PREFIX + level, 
						TheMazeEasy.COMPLETION_NONE);
				levelMap.put(level, completion);
			}
			this.mazeCompletionList.add(levelMap);
		}
		Log.e("LEVEL_COMPLETION", "Loaded level completion information!");
	}
	
	
	
	/**
	 * Save current level information attached to this instance to the shared preferences
	 * @param context context of activity
	 */
	public void save(Context context){
		SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor e = myPrefs.edit();
		int total_stars = 0;
		
		for (int mazeListIndex = 0; mazeListIndex < this.mazeCompletionList.size(); mazeListIndex ++){
			for (Iterator<Integer> levelIt = this.mazeCompletionList.get(mazeListIndex).keySet().iterator(); levelIt.hasNext();){
				int level = levelIt.next();
				String completion = this.mazeCompletionList.get(mazeListIndex).get(level);
				e.putString(this.MAZE_FRAGMENT_PREFIX + (mazeListIndex+1) + this.MAZE_LEVEL_PREFIX + level, 
						completion); // add or overwrite someValue
				
				//Count stars:
				if (TheMazeEasy.COMPLETION_GOLD.equals(completion))
					total_stars += 3;
				else if (TheMazeEasy.COMPLETION_SILVER.equals(completion))
					total_stars += 2;
				else if (TheMazeEasy.COMPLETION_BRONZE.equals(completion))
					total_stars += 1;
			}
		}
		//Save stars
		e.putInt(PlayerInformation.NUMBER_OF_STARS, total_stars);
		
		boolean succes = e.commit(); // this saves to disk and notifies observers
		
		if (succes){
			Log.e("LEVEL_COMPLETION", "Save succesfull!");
		}
		else {
			Log.e("LEVEL_COMPLETION", "Save failed =(");
		}
	}
	
	
	public List<Map<Integer, String>> getMazeCompletionList(){
		return this.mazeCompletionList;
	}
	
	public void setMazeCompletionList(List<Map<Integer, String>> mazeCompletionList){
		this.mazeCompletionList = mazeCompletionList;
	}
}
