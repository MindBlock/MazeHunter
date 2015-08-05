package com.mindblock.mazehunter.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mindblock.mazehunter.maze.TheMazeLayout1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LevelCompletion {

	private final int NUMBER_OF_MAZES = 1;
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
		
		for (int mazeListIndex = 0; mazeListIndex < this.NUMBER_OF_MAZES; mazeListIndex ++){
			Map<Integer, String> levelMap = new HashMap<Integer, String>();
			for (int level = 1; level <= TheMazeLayout1.LEVELS_TOTAL; level ++){
				String completion = myPrefs.getString(this.MAZE_FRAGMENT_PREFIX + (mazeListIndex+1) + this.MAZE_LEVEL_PREFIX + level, 
						TheMazeLayout1.COMPLETION_NONE);
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
		
		for (int mazeListIndex = 0; mazeListIndex < this.mazeCompletionList.size(); mazeListIndex ++){
			for (Iterator<Integer> levelIt = this.mazeCompletionList.get(mazeListIndex).keySet().iterator(); levelIt.hasNext();){
				int level = levelIt.next();
				e.putString(this.MAZE_FRAGMENT_PREFIX + (mazeListIndex+1) + this.MAZE_LEVEL_PREFIX + level, 
						this.mazeCompletionList.get(mazeListIndex).get(level)); // add or overwrite someValue
			}
		}
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
