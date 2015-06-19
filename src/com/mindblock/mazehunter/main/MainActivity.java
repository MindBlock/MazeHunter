package com.mindblock.mazehunter.main;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.maze.TheMazeLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
		RelativeLayout rlMain = (RelativeLayout) findViewById(R.id.main_rl_layout);
		rlMain.setBackgroundResource(R.drawable.background_main);

		rlMain.addView(this.setMainLayouts(rlMain));
	}

	private LinearLayout setMainLayouts(RelativeLayout rl_main){

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		//Add image buttons
		linearLayout.addView(this.getImageButton(SpecificButton.MAZE));
		linearLayout.addView(this.getImageButton(SpecificButton.SKILLS));
		linearLayout.addView(this.getImageButton(SpecificButton.SHOP));


		return linearLayout;
	}


	private ImageButton getImageButton(SpecificButton sb){
		ImageButton ib = new ImageButton(this);

		ib.setImageResource(sb.ID);
		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		ib.setMaxHeight(maxHeight);

		//Check which button we need to add functionality for
		switch (sb) {

		case MAZE: 
			this.setButtonListenerMaze(ib);
			break;
		case SKILLS: 
			this.setButtonListenerSkills(ib);
			break;
		case SHOP: 
			this.setButtonListenerShop(ib);
			break;
		}

		return ib;
	}


	private void setButtonListenerMaze(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TheMazeLayout.class);
				//Add extras?
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void setButtonListenerSkills(ImageButton ib){

	}

	private void setButtonListenerShop(ImageButton ib){

	}


	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}



	public enum SpecificButton {
		MAZE(R.drawable.button_the_maze),
		SKILLS(R.drawable.button_skills),
		SHOP(R.drawable.button_shop);

		private int ID;

		private SpecificButton(int ID){
			this.ID = ID;
		}
	}
}
