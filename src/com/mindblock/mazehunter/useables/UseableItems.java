package com.mindblock.mazehunter.useables;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class UseableItems {

	public static final String DISTRACT = "DISTRACT";
	public static final String JUMP = "JUMP";
	public static final String RETURN = "RETURN";
	public static final String REVEAL = "REVEAL";
	public static final String TREASURE = "TREASURE";
	public static final String FREEZE = "FREEZE";
	
	private Map<String, Boolean> items;
	private Context context;
	
	public UseableItems(Context context){
		items = new LinkedHashMap<String, Boolean>();
		this.context = context;
		
		this.loadAll();
	}
	
	public Map<String, Boolean> getAllUseableItems(){
		return this.items;
	}
	
	public void unlockUseableItem(String item){
		this.items.put(item, true);
		this.saveAll();
	}
	
	public void lockUseableItem(String item){
		this.items.put(item, false);
		this.saveAll();
	}
	
	public void reset(){
		
		for (Iterator<String> it = this.items.keySet().iterator(); it.hasNext();){
			this.items.put(it.next(), false);
		}
		this.saveAll();
	}
	
	private void loadAll(){
		
		SharedPreferences myPrefs = this.context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		items.put(UseableItems.DISTRACT, myPrefs.getBoolean(UseableItems.DISTRACT, false));
		items.put(UseableItems.JUMP, myPrefs.getBoolean(UseableItems.JUMP, false));
		items.put(UseableItems.RETURN, myPrefs.getBoolean(UseableItems.RETURN, false));
		items.put(UseableItems.REVEAL, myPrefs.getBoolean(UseableItems.REVEAL, false));
		items.put(UseableItems.TREASURE, myPrefs.getBoolean(UseableItems.TREASURE, false));
		items.put(UseableItems.FREEZE, myPrefs.getBoolean(UseableItems.FREEZE, false));
	}
	
	private void saveAll(){
		
		SharedPreferences myPrefs = this.context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor e = myPrefs.edit();
		
		e.putBoolean(UseableItems.DISTRACT, this.items.get(UseableItems.DISTRACT));
		e.putBoolean(UseableItems.JUMP, this.items.get(UseableItems.JUMP));
		e.putBoolean(UseableItems.RETURN, this.items.get(UseableItems.RETURN));
		e.putBoolean(UseableItems.REVEAL, this.items.get(UseableItems.REVEAL));
		e.putBoolean(UseableItems.TREASURE, this.items.get(UseableItems.TREASURE));
		e.putBoolean(UseableItems.FREEZE, this.items.get(UseableItems.FREEZE));
		
		e.commit();
	}
}
