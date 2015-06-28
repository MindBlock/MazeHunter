package com.mindblock.mazehunter.useables;

import java.util.HashMap;
import java.util.Map;

public class UseableItems {

	
	private Map<String, Integer> items;
	private static UseableItems useableItems = null;
	
	private UseableItems(){
		items = new HashMap<String, Integer>();
	}
	
	public static UseableItems getUseableItem(){
		if (useableItems != null)
			return useableItems;
		return new UseableItems();
	}
	
	public Map<String, Integer> getUseableItems(){
		return this.items;
	}
	
	public void addUseableItem(String name){
		if (this.items.containsKey(name)){
			this.items.put(name, this.items.get(name)+1);
		}
		else {
			this.items.put(name, 1);
		}
	}
}
