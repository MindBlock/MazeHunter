package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.main.MainActivity;
import com.mindblock.mazehunter.shop.ShopLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TheMazeLayout extends Activity{
	
	private static final int NUMBER_OF_LEVELS = 50;
	private static final int LEVELS_UNLOCKED = 1;
	
	private static final String COMPLETION_NONE = "NONE";
	private static final String COMPLETION_BRONZE = "BRONZE";
	private static final String COMPLETION_SILVER = "SILVER";
	private static final String COMPLETION_GOLD = "GOLD";
	private Typeface typeFace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_maze_layout);
		this.typeFace = Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf");

		//TODO: Make fragments, current level use dumb AI and progress per fragment
		
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
			//TODO:
			//Check the completion level
			
			levelButton.setBackgroundResource(R.drawable.border_silver_completion);
			
			levelButton.setTypeface(this.typeFace);
			levelButton.setText("Maze " + level);
			
			levelButton.setOnClickListener(new LevelListener(level, COMPLETION_NONE));
			
			//Set margin:
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int horizontalOffset = (int) this.getDeviceWidth()/10;
			params.setMargins(horizontalOffset, 5, horizontalOffset, 5);
			params.height = (int) (this.getDeviceHeight()/12);
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
	
	public class LevelListener implements OnClickListener{

		private int level;
		private String completion;
		
		public LevelListener(int level, String completion){
			this.level = level;
			this.completion = completion;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(TheMazeLayout.this, TheMaze.class);
			//Add extras?
			intent.putExtra("COMPLETION", this.completion);
			intent.putExtra("LEVEL", this.level);
			TheMazeLayout.this.startActivity(intent);
		}
		
	}
	
	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}
	
	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}
}
