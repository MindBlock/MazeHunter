package com.mindblock.mazehunter.maze;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;

import com.mindblock.mazehunter.useables.UseableItems;

public class AvailableItems {

	private UseableItems items;
	private Context context;
	private Map<String, Boolean> availableItems;
	private Map<String, Boolean> availableItemsSelected;
	private Map<String, UseableSkill> resources;
	
	public AvailableItems(Context context){
		
		this.context = context;
		this.items = new UseableItems(this.context);
		
		this.availableItems = new LinkedHashMap<String, Boolean>();
		this.availableItemsSelected = new LinkedHashMap<String, Boolean>();
		this.resources = new LinkedHashMap<String, UseableSkill>();
		
		for (Iterator<String> it = this.items.getAllUseableItems().keySet().iterator(); it.hasNext();){
			String key = it.next();
			this.availableItems.put(key, this.items.getAllUseableItems().get(key));
			this.availableItemsSelected.put(key, false);
			
			this.determineResource(key);
		}
	}
	
	private void determineResource(String key){
		
		if ("DISTRACT".equals(key))
			this.resources.put(key, UseableSkill.DISTRACT);
		else if ("JUMP".equals(key))
			this.resources.put(key, UseableSkill.JUMP);
		else if ("RETURN".equals(key))
			this.resources.put(key, UseableSkill.RETURN);
		else if ("REVEAL".equals(key))
			this.resources.put(key, UseableSkill.REVEAL);
		else if ("TREASURE".equals(key))
			this.resources.put(key, UseableSkill.TREASURE);
		else if ("FREEZE".equals(key))
			this.resources.put(key, UseableSkill.FREEZE);
	}
	
	public Map<String, Boolean> getAllAvailableItems(){
		return this.availableItems;
	}
	
	public void useItem(String key){
		this.availableItems.put(key, false);
	}
	
	public void setSelected(String key, boolean selected){
		this.availableItemsSelected.put(key, selected);
	}
	
	public boolean selected(String key){
		return this.availableItemsSelected.get(key);
	}
	
	public UseableSkill getResources(String key){
		return this.resources.get(key);
	}
	
}
