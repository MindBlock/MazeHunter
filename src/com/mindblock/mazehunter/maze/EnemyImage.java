package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EnemyImage {

	private Bitmap up, down, left, right;
	
	
	public EnemyImage(Context context, int size){
		
		this.initImages(context, size);
	}
	
	private void initImages(Context context, int size){
		
		Bitmap b = null;
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_up);
		this.up = Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_down);
		this.down = Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_left);
		this.left = Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_right);
		this.right = Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true);
		
		b.recycle();
		b = null;
	}
	
	public Bitmap getDirection(int direction){
		
		switch(direction){
		case TheMaze.RIGHT_DIRECTION: return this.right;
		case TheMaze.DOWN_DIRECTION: return this.down;
		case TheMaze.LEFT_DIRECTION: return this.left;
		}
		return this.up;
	}
}

