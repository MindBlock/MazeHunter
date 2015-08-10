package com.mindblock.mazehunter.maze.clear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mindblock.mazehunter.maze.TheMazeLayout1;
import com.mindblock.mazehunter.save.LevelCompletion;

import android.content.Context;

public class ClearMemory {
	
	
	public void clearCompletion(Context context){
		LevelCompletion lc = new LevelCompletion();
		lc.load(context);
		List<Map<Integer, String>> completionList = lc.getMazeCompletionList();
		List<Map<Integer, String>> clearList = new ArrayList<Map<Integer, String>>();
		
		for (int i = 0; i < completionList.size(); i++){
			Map<Integer, String> newMap = new HashMap<Integer, String>();
			for (Iterator<Integer> it = completionList.get(i).keySet().iterator();it.hasNext();){
				newMap.put(it.next(), TheMazeLayout1.COMPLETION_NONE);
			}
			clearList.add(newMap);
		}
		lc.setMazeCompletionList(clearList);
		lc.save(context);
	}
}
