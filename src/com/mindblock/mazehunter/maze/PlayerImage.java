package com.mindblock.mazehunter.maze;

import java.util.ArrayList;
import java.util.List;

import com.mindblock.mazehunter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PlayerImage {

	private List<Bitmap> ListUp = new ArrayList<Bitmap>();
	private List<Bitmap> ListDown = new ArrayList<Bitmap>();
	private List<Bitmap> ListLeft = new ArrayList<Bitmap>();
	private List<Bitmap> ListRight = new ArrayList<Bitmap>();

	private int currentIndexUp = 0;
	private int currentIndexDown = 0;
	private int currentIndexLeft = 0;
	private int currentIndexRight = 0;

	public PlayerImage(Context context, int size){
		this.initLists(context, size);
	}

	private void initLists(Context context, int size){

		Bitmap b = null;
		
		//UP
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_0);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_1);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_2);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_3);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_2);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_up_1);
		this.ListUp.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));

		//DOWN
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_0);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_1);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_2);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_3);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_2);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_down_1);
		this.ListDown.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));

		//LEFT
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_0);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_1);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_2);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_3);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_2);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_left_1);
		this.ListLeft.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));

		//RIGHT
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_0);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_1);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_2);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_3);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_2);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));
		
		b = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotic_mouse_right_1);
		this.ListRight.add(Bitmap.createScaledBitmap(b, (int) (size/4.0), (int) (size/4.0), true));

		b.recycle();
		b = null;
	}

	public Bitmap getNextImage(int direction){

		switch (direction){

		case TheMaze.RIGHT_DIRECTION: return this.getRight(true);
		case TheMaze.DOWN_DIRECTION: return this.getDown(true);
		case TheMaze.LEFT_DIRECTION: return this.getLeft(true);

		}

		return this.getUp(true);
	}

	public Bitmap getImage(int direction){

		switch (direction){

		case TheMaze.RIGHT_DIRECTION: return this.getRight(false);
		case TheMaze.DOWN_DIRECTION: return this.getDown(false);
		case TheMaze.LEFT_DIRECTION: return this.getLeft(false);

		}

		return this.getUp(false);
	}

	private Bitmap getUp(boolean iterate){
		Bitmap r = this.ListUp.get(this.currentIndexUp);
		if (iterate)
			this.currentIndexUp = (this.currentIndexUp + 1) % this.ListUp.size();
		return r;
	}

	private Bitmap getDown(boolean iterate){
		Bitmap r = this.ListDown.get(this.currentIndexDown);
		if (iterate)
			this.currentIndexDown = (this.currentIndexDown + 1) % this.ListDown.size();
		return r;
	}

	private Bitmap getLeft(boolean iterate){
		Bitmap r = this.ListLeft.get(this.currentIndexLeft);
		if (iterate)
			this.currentIndexLeft = (this.currentIndexLeft + 1) % this.ListLeft.size();
		return r;
	}

	private Bitmap getRight(boolean iterate){
		Bitmap r = this.ListRight.get(this.currentIndexRight);
		if (iterate)
			this.currentIndexRight = (this.currentIndexRight + 1) % this.ListRight.size();
		return r;
	}


}
