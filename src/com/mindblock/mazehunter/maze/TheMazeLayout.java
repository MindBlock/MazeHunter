package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TheMazeLayout extends Activity{
	
	private static final int NUMBER_OF_LEVELS = 50;
	private static final int LEVELS_UNLOCKED = 22;
	private Typeface typeFace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_maze_layout);
		this.typeFace = Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf");

		this.addListOfLevels();
	}
	
	
	private void addListOfLevels(){
		RelativeLayout rlMazeLayout = (RelativeLayout) findViewById(R.id.rl_maze_layout);
		rlMazeLayout.setBackgroundResource(R.drawable.background_main_clean);
		
		//Outer Linear Layout
		LinearLayout mainLayout = new LinearLayout(this);
		LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llParams.gravity = Gravity.CENTER_HORIZONTAL;
		mainLayout.setLayoutParams(llParams);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.addView(this.getLevelImage());
		
		//Scroll View
		ScrollView scrollView = new ScrollView(this);
		
		//Inner Linear Layout
		LinearLayout scrollLayout = new LinearLayout(this);
		scrollLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		scrollLayout.setOrientation(LinearLayout.VERTICAL);
		scrollLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		this.addMazeButtons(scrollLayout);
		
		//Putting it all together
		scrollView.addView(scrollLayout);
		mainLayout.addView(scrollView);
		rlMazeLayout.addView(mainLayout);
	}
	
	
	private void addMazeButtons(LinearLayout scrollLayout){
		for (int level = 1; level <= LEVELS_UNLOCKED; level++){
			Button levelButton = new Button(this);
			//This is where we would load the properties of this index
			//Actually, we would usually just loop over those...
			
			//Check the completion level
			
			if (level%4 == 1)
				levelButton.setBackgroundResource(R.drawable.border_default_completion);
			else if (level%4 == 2)
				levelButton.setBackgroundResource(R.drawable.border_bronze_completion);
			else if (level%4 == 3)
				levelButton.setBackgroundResource(R.drawable.border_silver_completion);
			else
				levelButton.setBackgroundResource(R.drawable.border_gold_completion);
			
			levelButton.setTypeface(this.typeFace);
			levelButton.setText("Maze " + level);
			
			//Set margin:
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int horizontalOffset = (int) this.getDeviceWidth()/10;
			params.setMargins(horizontalOffset, 5, horizontalOffset, 5);
			levelButton.setLayoutParams(params);
			
			scrollLayout.addView(levelButton);
		}
	}
	
	private ImageView getLevelImage(){
		ImageView levelImage = new ImageView(this);
		levelImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		levelImage.setImageResource(R.drawable.image_the_maze_blank);
		
		levelImage.setAdjustViewBounds(true);
		int maxHeight = (int) (0.15*this.getDeviceHeight());
		levelImage.setMaxHeight(maxHeight);
		
		return levelImage;
	}
	
	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}
	
	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}
}
