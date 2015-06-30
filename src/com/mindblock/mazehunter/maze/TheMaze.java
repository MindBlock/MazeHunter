package com.mindblock.mazehunter.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
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
	protected MazeInfo mazeInfo;
	private List<Coordinate> allTreasureList;
	private List<Coordinate> obtainedTreasureList;
	private List<Coordinate> roomsVisited;
	protected LinearLayout roomLayout;


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
		this.mazeInfo = new MazeInfo(this.getMazeInfo());
		this.allTreasureList = this.mazeInfo.getTreasureList();
		this.obtainedTreasureList = new ArrayList<Coordinate>(this.allTreasureList.size());
		this.roomsVisited = new ArrayList<Coordinate>();

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
		this.initStartRoomLayout();
		ll.addView(this.roomLayout, 1);

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


	private void initStartRoomLayout(){
		this.roomLayout = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.height = (int) this.getDeviceWidth();
		roomLayout.setLayoutParams(params);
		roomLayout.setBackgroundResource(R.drawable.maze_room_start);
		roomLayout.setOnTouchListener(new DirectionListener());

		//set doors
		ImageView overlay = new ImageView(this);
		Room[][] maze = mazeInfo.getMaze();
		Coordinate player = this.mazeInfo.getPlayerCoordinate();
		overlay.setImageResource(DoorOverlay.getDoorOverlayDrawable(
				maze[player.getX()][player.getY()].getBitRoom()));
		this.roomLayout.addView(overlay);

	}

	protected void updateRoomLayout(Room newRoom){

		//Check what kind of room it is:
		if (newRoom.isStart()){
			this.roomLayout.setBackgroundResource(R.drawable.maze_room_start);
		}
		else if(newRoom.isTreasure()){
			//TODO: change to treasure room
			this.roomLayout.setBackgroundResource(R.drawable.maze_room);
		}
		else {
			this.roomLayout.setBackgroundResource(R.drawable.maze_room);
		}

		//Next: update the doors overlay:
		this.roomLayout.removeAllViews();
		ImageView overlay = new ImageView(this);
		Room[][] maze = mazeInfo.getMaze();
		Coordinate player = this.mazeInfo.getPlayerCoordinate();
		overlay.setImageResource(DoorOverlay.getDoorOverlayDrawable(
				maze[player.getX()][player.getY()].getBitRoom()));
		this.roomLayout.addView(overlay);
	}


	/**
	 * 
	 * @return the width in pixels of the devices
	 */
	protected double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}



	/**
	 * 
	 * @return list of strings arrays containing the data of this specific maze.
	 */
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


	public class DirectionListener implements OnTouchListener{


		private boolean moving;
		private long lastTouched;
		private int delay = 1000; //3seconds to next touch
		private double sizeImageOver2; 

		public DirectionListener() {

			this.sizeImageOver2 = getDeviceWidth()/2;
			this.moving = false;
			this.lastTouched = System.currentTimeMillis();
		}


		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (moving){
				if (System.currentTimeMillis() - this.lastTouched > this.delay){
					moving = false;
				}
				else {
					return false;
				}
			}

			float x = event.getX(); //the most recent x coordinate of the touch
			float y = event.getY(); //the most recent y coordinate of the touch
			double direction = Math.atan2(this.sizeImageOver2 - y, x - this.sizeImageOver2) + Math.PI; //[0 ... 2pi]

			
			Room[][] maze = mazeInfo.getMaze();
			Coordinate player = mazeInfo.getPlayerCoordinate();
			Room thisRoom = maze[player.getX()][player.getY()];

			//Down
			if (direction > Math.PI/4 && direction < 3*Math.PI/4 && thisRoom.isDown()){
				this.goDown(player, maze, thisRoom);
				this.lastTouched = System.currentTimeMillis();
				this.moving = true;
				Log.e("MOVED", "____DOWN____");
			}
			//Right
			else if (direction > 3*Math.PI/4 && direction <= 5*Math.PI/4 && thisRoom.isRight()){
				this.goRight(player, maze, thisRoom);
				this.lastTouched = System.currentTimeMillis();
				this.moving = true;
				Log.e("MOVED", "____RIGHT____");
			}
			//Up
			else if (direction > 5*Math.PI/4 && direction <= 7*Math.PI/4 && thisRoom.isUp()){
				this.goUp(player, maze, thisRoom);
				this.lastTouched = System.currentTimeMillis();
				this.moving = true;
				Log.e("MOVED", "____UP____");
			}
			//Left
			else if ((direction > 7*Math.PI/4 || direction <= Math.PI/4) && thisRoom.isLeft()){
				this.goLeft(player, maze, thisRoom);
				this.lastTouched = System.currentTimeMillis();
				this.moving = true;
				Log.e("MOVED", "____LEFT____");
			}

			return false;
		}


		private void goRight(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(0, 1);
			//TODO: move enemy
			//TODO: animate player movement

			updateRoomLayout(maze[player.getX()][player.getY()]);
		}

		private void goUp(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(-1, 0);
			//TODO: move enemy
			//TODO: animate player movement

			updateRoomLayout(maze[player.getX()][player.getY()]);
		}

		private void goLeft(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(0, -1);
			//TODO: move enemy
			//TODO: animate player movement

			updateRoomLayout(maze[player.getX()][player.getY()]);
		}

		private void goDown(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(1, 0);
			//TODO: move enemy
			//TODO: animate player movement

			updateRoomLayout(maze[player.getX()][player.getY()]);
		}

	}
}
