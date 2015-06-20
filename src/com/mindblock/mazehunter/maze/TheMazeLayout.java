package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

import android.app.Activity;
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
	private static final int LEVELS_UNLOCKED = 12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_maze_layout);

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
			
			
//			levelButton.setScaleType(Button.ScaleType.CENTER_INSIDE);
			levelButton.setBackgroundResource(R.drawable.button_the_maze);
			levelButton.setText("Maze " + level);
			
			//Set margin:
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 20);
			levelButton.setLayoutParams(params);
			
			scrollLayout.addView(levelButton);
		}
	}
	
	private ImageView getLevelImage(){
		ImageView levelImage = new ImageView(this);
		
		levelImage.setImageResource(R.drawable.button_shop);
		
		levelImage.setAdjustViewBounds(true);
		int maxHeight = (int) (0.15*this.getDeviceHeight());
		levelImage.setMaxHeight(maxHeight);
		
		return levelImage;
	}
	
	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}
}
