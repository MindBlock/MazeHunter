package com.mindblock.mazehunter.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mindblock.mazehunter.R;

public class TheMaze extends Activity{

	private int level;
	private String completion;
	private static final int NUMBER_OF_USEABLE_ITEMS = 6;
	private MazeInfo mazeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("COMPLETION") && extras.containsKey("LEVEL")) {
			this.completion= extras.getString("COMPLETION");
			this.level = extras.getInt("LEVEL");
		}

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maze_game_layout);
		
		//get and set all info regarding this maze
		List<String[]> info = this.getMazeInfo();
		this.mazeInfo = new MazeInfo(this.getMazeInfo());

		this.addMazeLayout();
	}


	private void addMazeLayout(){
		RelativeLayout rlMazeLayout = (RelativeLayout) findViewById(R.id.rl_maze_game_layout);
		rlMazeLayout.setBackgroundResource(R.drawable.background_maze_game);

		LinearLayout ll = new LinearLayout(this);

		//Layout stuff
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		//Add usable item bar
		ll.addView(this.getUseableItemsLayout(), 0);
		//Add background image view
		ll.addView(this.getStartRoomLayout(), 1);

		rlMazeLayout.addView(ll);
	}


	private LinearLayout getUseableItemsLayout(){

		LinearLayout useableItemsLayout = new LinearLayout(this);
		useableItemsLayout.setOrientation(LinearLayout.HORIZONTAL);
		useableItemsLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		for (int i = 0; i < NUMBER_OF_USEABLE_ITEMS ; i++){

			LinearLayout iButtonLayout = new LinearLayout(this);
			iButtonLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			iButtonLayout.setOrientation(LinearLayout.VERTICAL);

			ImageButton useableItem = new ImageButton(this);

			//Remove padding
			useableItem.setPadding(0, 0, 0, 0);

			//Calculate width fitting:
			useableItem.setAdjustViewBounds(true);
			int maxWidth = (int) (this.getDeviceWidth()/NUMBER_OF_USEABLE_ITEMS);
			useableItem.setMaxWidth(maxWidth);

			//Here you actually loop through the list of useable items and add the image. 
			//If list.size() < NUMBER_OF_USEABLE_ITEMS, fill the rest with dummy like here:
			useableItem.setImageResource(R.drawable.background_useable_item);

			useableItem.setBackgroundColor(Color.TRANSPARENT);

			//TODO: Add onClick listener

			iButtonLayout.addView(useableItem);

			useableItemsLayout.addView(iButtonLayout);
		}

		return useableItemsLayout;
	}


	private LinearLayout getStartRoomLayout(){
		LinearLayout roomLayout = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.height = (int) this.getDeviceWidth();
		roomLayout.setLayoutParams(params);
		roomLayout.setBackgroundResource(R.drawable.maze_room_start);


		//TODO:Check what kind of room and set doors etc
		//		room.setImageResource(R.drawable.maze_room_start);

		//		roomLayout.addView(room);

		return roomLayout;
	}
	
	

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}
	
	

	private List<String[]> getMazeInfo(){

		List<String[]> mazeInfo = new ArrayList<String[]>();

		try {
			InputStream mazeText = getAssets().open("mazes/maze" + Integer.toString(this.level) + ".txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(mazeText));

			String line = null;
			while ((line = in.readLine()) != null){
				if (!line.isEmpty()){
					mazeInfo.add(line.split("\\|"));
				}
			}
		} catch (IOException e) {

		}

		return mazeInfo;
	}
	
	
	private void addDoorLeft(){
		
		
	}
}
