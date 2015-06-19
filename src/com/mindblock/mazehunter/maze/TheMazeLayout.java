package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.R.drawable;
import com.mindblock.mazehunter.R.id;
import com.mindblock.mazehunter.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

public class TheMazeLayout extends Activity{
	
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
	}

}
