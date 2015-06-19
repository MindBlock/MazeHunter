package com.mindblock.mazehunter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	

	private static final int NUMBER_OF_BUTTONS = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		this.setInitialImages();
	}
	
	private void setInitialImages(){
		RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.main_rl_layout);
		rl_main.setBackgroundResource(R.drawable.background_main);
		
		rl_main.addView(this.setMainLayouts(rl_main));
	}
	
	private LinearLayout setMainLayouts(RelativeLayout rl_main){
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		//Add image buttons
		linearLayout.addView(this.getImageButton(R.drawable.button_the_maze));
		linearLayout.addView(this.getImageButton(R.drawable.button_skills));
		linearLayout.addView(this.getImageButton(R.drawable.button_shop));

		
		return linearLayout;
	}
	
	
	private ImageButton getImageButton(int ID){
		ImageButton ibMaze = new ImageButton(this);
		
		ibMaze.setImageResource(ID);
		ibMaze.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ibMaze.setBackgroundColor(Color.TRANSPARENT);
		
		//Calculate height fitting:
		ibMaze.setAdjustViewBounds(true);
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		ibMaze.setMaxHeight(maxHeight);
		
		return ibMaze;
	}
	
	
	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

}
