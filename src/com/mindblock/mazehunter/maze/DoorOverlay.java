package com.mindblock.mazehunter.maze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mindblock.mazehunter.R;

public class DoorOverlay {

	
	private Bitmap OVERLAY1111;
	private Bitmap OVERLAY1110;
	private Bitmap OVERLAY1101;
	private Bitmap OVERLAY1100;
	private Bitmap OVERLAY1011;
	private Bitmap OVERLAY1010;
	private Bitmap OVERLAY1001;
	private Bitmap OVERLAY1000;
	private Bitmap OVERLAY0111;
	private Bitmap OVERLAY0110;
	private Bitmap OVERLAY0101;
	private Bitmap OVERLAY0100;
	private Bitmap OVERLAY0011;
	private Bitmap OVERLAY0001;
	private Bitmap OVERLAY0010;
	
	private static DoorOverlay doorOverlay;
	
	private DoorOverlay(int size, Context context){
		
		this.initImages(size, context);
	}
	
	
	private void initImages(int size, Context context){
		Bitmap b = null;
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1111);
		this.OVERLAY1111 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1110);
		this.OVERLAY1110 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1101);
		this.OVERLAY1101 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1100);
		this.OVERLAY1100 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1011);
		this.OVERLAY1011 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1010);
		this.OVERLAY1010 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1001);
		this.OVERLAY1001 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_1000);
		this.OVERLAY1000 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0111);
		this.OVERLAY0111 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0110);
		this.OVERLAY0110 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0101);
		this.OVERLAY0101 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0100);
		this.OVERLAY0100 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0011);
		this.OVERLAY0011 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0010);
		this.OVERLAY0010 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.maze_room_overlay_0001);
		this.OVERLAY0001 = Bitmap.createScaledBitmap(b, size, size, true);
		
		b.recycle();
		b = null;
	}
	
	
	public static DoorOverlay getInstance(int size, Context context){
		if (doorOverlay == null)
			doorOverlay = new DoorOverlay(size, context);
		return doorOverlay;
	}
	
	public Bitmap getDoorOverlayImage(String doorConfig){
		
		int doorCode = Integer.parseInt("1" + doorConfig);
		switch (doorCode){
		
		case 11111: return OVERLAY1111;
		case 11110: return OVERLAY1110;
		case 11101: return OVERLAY1101;
		case 11100: return OVERLAY1100;
		case 11011: return OVERLAY1011;
		case 11010: return OVERLAY1010;
		case 11001: return OVERLAY1001;
		case 11000: return OVERLAY1000;
		case 10111: return OVERLAY0111;
		case 10110: return OVERLAY0110;
		case 10101: return OVERLAY0101;
		case 10100: return OVERLAY0100;
		case 10011: return OVERLAY0011;
		case 10001: return OVERLAY0001;
		case 10010: return OVERLAY0010;
		default: return OVERLAY1111;
		}
	}
}
