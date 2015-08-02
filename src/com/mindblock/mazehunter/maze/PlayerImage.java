package com.mindblock.mazehunter.maze;

import java.util.ArrayList;
import java.util.List;

import com.mindblock.mazehunter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class PlayerImage {

	private List<Bitmap> playerMovementList = new ArrayList<Bitmap>();
	private int currentIndex;
	
	public PlayerImage(int direction, Context context){
		this.currentIndex = 0;
		this.initList(direction, context);
	}
	
	private void initList(int direction, Context context){
		//TODO: Check direction, add multiple images per direction etc...
		this.playerMovementList.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.the_dude_front_0), 40, 80, false));
	}
	
	public Bitmap getNextImage(){
		Bitmap r = this.playerMovementList.get(currentIndex);
		this.currentIndex = (this.currentIndex + 1) % this.playerMovementList.size();
		return r;
	}
	
	
}
