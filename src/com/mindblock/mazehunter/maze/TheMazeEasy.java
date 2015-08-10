package com.mindblock.mazehunter.maze;

import java.util.Map;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.main.MainActivity;
import com.mindblock.mazehunter.save.LevelCompletion;

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

public class TheMazeEasy extends Activity{

	public static final int LEVELS_TOTAL = 100;
	public static final String COMPLETION_NONE = "NONE";
	public static final String COMPLETION_BRONZE = "BRONZE";
	public static final String COMPLETION_SILVER = "SILVER";
	public static final String COMPLETION_GOLD = "GOLD";
	private Typeface typeFace;
	private LevelCompletion levelCompletion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_maze_layout);
		this.typeFace = Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf");
		//		new ClearMemory().clearCompletion(this);
		this.levelCompletion = new LevelCompletion();

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

		this.levelCompletion.load(this);

		Map<Integer, String> completionMap = this.levelCompletion.getMazeCompletionList().get(0);
		int totalLevelsUnlocked = this.unlockedLevels(completionMap);
		
		for (int level = 1; level <= totalLevelsUnlocked; level++){
			Button levelButton = new Button(this);

			String completion = completionMap.get(level);

			//Check completion
			//Gold?
			if (TheMazeEasy.COMPLETION_GOLD.equals(completion)){
				levelButton.setBackgroundResource(R.drawable.border_gold_completion);
				levelButton.setOnClickListener(new LevelListener(level, COMPLETION_GOLD));
			}
			//Silver?
			else if (TheMazeEasy.COMPLETION_SILVER.equals(completion)){
				levelButton.setBackgroundResource(R.drawable.border_silver_completion);
				levelButton.setOnClickListener(new LevelListener(level, COMPLETION_SILVER));
			}
			//Bronze?
			else if (TheMazeEasy.COMPLETION_BRONZE.equals(completion)){
				levelButton.setBackgroundResource(R.drawable.border_bronze_completion);
				levelButton.setOnClickListener(new LevelListener(level, COMPLETION_BRONZE));
			}
			//Default, None
			else {
				levelButton.setBackgroundResource(R.drawable.border_default_completion);
				levelButton.setOnClickListener(new LevelListener(level, COMPLETION_NONE));
			}

			levelButton.setTypeface(this.typeFace);
			levelButton.setText("Maze " + level);

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
		levelImage.setImageResource(R.drawable.image_the_maze_easy);

		levelImage.setAdjustViewBounds(true);
		int maxHeight = (int) (0.15*this.getDeviceHeight());
		levelImage.setMaxHeight(maxHeight);

		return levelImage;
	}


	protected void setLoadingScreen(){
		setContentView(R.layout.loading_screen);
		RelativeLayout rlMazeLayout = (RelativeLayout) findViewById(R.id.rl_maze_loading);
		rlMazeLayout.setBackgroundResource(R.drawable.background_main_loading);
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

			//Show loading screen until maze is loaded
			setLoadingScreen();

			Intent intent = new Intent(TheMazeEasy.this, TheMaze.class);
			//Add extras?
			intent.putExtra("COMPLETION", this.completion);
			intent.putExtra("LEVEL", this.level);
			intent.putExtra("MAZE_FRAGMENT", 1);
			TheMazeEasy.this.startActivity(intent);
		}

	}


	private int unlockedLevels(Map<Integer, String> completionMap){
		int levels = this.indexLastCompletedMaze(completionMap);
		if (levels + 5 > LEVELS_TOTAL)
			return LEVELS_TOTAL;
		else
			return levels+5;
	}

	private int indexLastCompletedMaze(Map<Integer, String> completionMap){
		int index = 0;

		for (int i = 1; i <= completionMap.size(); i++){
			if (completionMap.containsKey(i) && !COMPLETION_NONE.equals(completionMap.get(i))){
				index = i;
			}
		}
		return index;
	}

	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}

	public void onBackPressed(){
		Intent i = new Intent(TheMazeEasy.this,TheMazeDifficulties.class);    
		startActivity(i);  
	}
}
