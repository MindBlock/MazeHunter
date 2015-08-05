package com.mindblock.mazehunter.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mindblock.mazehunter.maze.enemy.Enemy;
import com.mindblock.mazehunter.save.LevelCompletion;

public class TheMaze extends Activity{

	private int level;
	private String completion;
	private int mazeFragment;
	protected MazeInfo mazeInfo;
	private List<Coordinate> obtainedTreasureList;
	protected LinearLayout roomLayout;
	private ImageView treasureCount;
	private static final int NUMBER_OF_USEABLE_ITEMS = 6;
	public static final int LEFT_DIRECTION = 0;
	public static final int RIGHT_DIRECTION = 1;
	public static final int UP_DIRECTION = 2;
	public static final int DOWN_DIRECTION = 3;
	public static final int NO_DIRECTION = -1;
	public static final int DELAY = 3000;//3s
	public static final int START_ROOM = R.drawable.maze_room_start;
	public static final int TREASURE_ROOM = R.drawable.maze_room_chest;
	public static final int OTHER_ROOM = R.drawable.maze_room;
	public static Bitmap startRoom, treasureRoom, otherRoom;
	protected MovementDrawer md;
	protected Enemy enemy;
	private LevelCompletion levelCompletion;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("COMPLETION") && extras.containsKey("LEVEL") && extras.containsKey("MAZE_FRAGMENT")) {
			this.completion= extras.getString("COMPLETION");
			this.level = extras.getInt("LEVEL");
			this.mazeFragment = extras.getInt("MAZE_FRAGMENT");
		}
		

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maze_game_layout);

		//Init the 3 kinds of rooms
		int width = (int) this.getDeviceWidth();
		TheMaze.startRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.START_ROOM), width, width, false);
		TheMaze.treasureRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.TREASURE_ROOM), width, width, false);
		TheMaze.otherRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.OTHER_ROOM), width, width, false);

		//get and set all info regarding this maze
		this.mazeInfo = new MazeInfo(this.getMazeInfo());
		this.obtainedTreasureList = new ArrayList<Coordinate>();

		//Initialize level completion map
		this.levelCompletion = new LevelCompletion();
		this.levelCompletion.load(this);
		
		//TODO: Check what difficulty of enemy should be!
		this.enemy = new Enemy(this.mazeInfo, Enemy.STRATEGY_RANDOM);

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

		//add information layer
		ll.addView(this.getInformationLayout(), 2);

		rlMazeLayout.addView(ll);
	}


	private LinearLayout getInformationLayout(){

		LinearLayout informationLayout = new LinearLayout(this);
		informationLayout.setOrientation(LinearLayout.HORIZONTAL);
		informationLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		this.treasureCount = new ImageView(this);
		Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_0), 
				(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
		this.treasureCount.setImageBitmap(b);
		this.treasureCount.setPadding(0, 0, 0, 0);

		informationLayout.addView(this.treasureCount);

		return informationLayout;
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
		roomLayout.setOnTouchListener(new DirectionListener());

		//set doors
		Room[][] maze = mazeInfo.getMaze();
		Coordinate player = this.mazeInfo.getPlayerCoordinate();
		this.md = new MovementDrawer(this, DoorOverlay.getDoorOverlayDrawable(
				maze[player.getX()][player.getY()].getBitRoom()), NO_DIRECTION, START_ROOM, maze.length);
		this.roomLayout.addView(this.md);

	}

	protected void updateRoomLayout(Room newRoom, int direction){

		int roomSort = 0;
		//Check what kind of room it is:
		if (newRoom.isStart()){
			roomSort = START_ROOM;
		}
		else if(newRoom.isTreasure()){
			if (!containsCoordinate(this.obtainedTreasureList, this.mazeInfo.getPlayerCoordinate())){
				this.obtainedTreasureList.add(new Coordinate(
						this.mazeInfo.getPlayerCoordinate().getX(), 
						this.mazeInfo.getPlayerCoordinate().getY()));
				this.updateTreasureCount(this.obtainedTreasureList.size());
				Log.e("TheMaze","Treasure chests found: " + this.obtainedTreasureList.size());
			}
			roomSort = TREASURE_ROOM;
		}
		else {
			roomSort = OTHER_ROOM;
		}

		//Next: update the doors overlay and movement:
		Room[][] maze = mazeInfo.getMaze();
		Coordinate player = this.mazeInfo.getPlayerCoordinate();
		this.md.updateRoom(roomSort, direction, 
				DoorOverlay.getDoorOverlayDrawable(maze[player.getX()][player.getY()].getBitRoom()));
	}


	private void updateTreasureCount(int treasuresObtained){

		//Check if player bested own score
		int bestTreasures = 0;
		
		if (TheMazeLayout1.COMPLETION_GOLD.equals(this.completion)) bestTreasures = 3;
		else if (TheMazeLayout1.COMPLETION_SILVER.equals(this.completion)) bestTreasures = 2;
		else if (TheMazeLayout1.COMPLETION_BRONZE.equals(this.completion)) bestTreasures = 1;
		
		if (treasuresObtained > bestTreasures){
			String newCompletion = TheMazeLayout1.COMPLETION_BRONZE;
			
			if (treasuresObtained == 2) newCompletion = TheMazeLayout1.COMPLETION_SILVER;
			else if (treasuresObtained == 3) newCompletion = TheMazeLayout1.COMPLETION_GOLD;
			
			//Update completion
			this.levelCompletion.getMazeCompletionList().get(this.mazeFragment-1).put(this.level, newCompletion);
			//Save it
			this.levelCompletion.save(this);
		}
		//Done checking
		
		switch(treasuresObtained){

		case 1: 
			Bitmap b1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_1), 
					(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
			this.treasureCount.setImageBitmap(b1);
			break;
		case 2:
			Bitmap b2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_2), 
					(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
			this.treasureCount.setImageBitmap(b2);
			break;
		case 3:
			Bitmap b3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_3), 
					(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
			this.treasureCount.setImageBitmap(b3);
			break;
		default :
			Bitmap b0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_0), 
					(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
			this.treasureCount.setImageBitmap(b0);
			break;
		}
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
	 * @return the height in pixels of the devices
	 */
	protected double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
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


		private double sizeImageOver2; 

		public DirectionListener() {

			this.sizeImageOver2 = getDeviceWidth()/2;
		}


		@Override
		public boolean onTouch(View v, MotionEvent event) {

			//Do nothing if still drawing
			if (md.isDrawing() || MovementDrawer.exitMinimap){
				return false;
			}
			

			float x = event.getX(); //the most recent x coordinate of the touch
			float y = event.getY(); //the most recent y coordinate of the touch
			double direction = Math.atan2(this.sizeImageOver2 - y, x - this.sizeImageOver2) + Math.PI; //[0 ... 2pi]
			
			//Check if minimap is clicked, bottom right: 5*size/6, 5*size/6
			if (x > 5*getDeviceWidth()/6 && y > 5*getDeviceWidth()/6){
				Log.e("MINIMAP", "MINIMAP CLICKED");
				MovementDrawer.running = true;
				md.minimapClicked(mazeInfo.getMaze());
				//exit method, no movement should be possible in either case
				return false;
			}

			if (MovementDrawer.minimapClicked) {
				return false;
			}

			Room[][] maze = mazeInfo.getMaze();
			Coordinate player = mazeInfo.getPlayerCoordinate();
			Room thisRoom = maze[player.getX()][player.getY()];

			//Down
			if (direction > Math.PI/4 && direction < 3*Math.PI/4 && thisRoom.isDown()){
				this.goDown(player, maze, thisRoom);
				Log.e("MOVED", "____DOWN____");
			}
			//Right
			else if (direction > 3*Math.PI/4 && direction <= 5*Math.PI/4 && thisRoom.isRight()){
				this.goRight(player, maze, thisRoom);
				Log.e("MOVED", "____RIGHT____");
			}
			//Up
			else if (direction > 5*Math.PI/4 && direction <= 7*Math.PI/4 && thisRoom.isUp()){
				this.goUp(player, maze, thisRoom);
				Log.e("MOVED", "____UP____");
			}
			//Left
			else if ((direction > 7*Math.PI/4 || direction <= Math.PI/4) && thisRoom.isLeft()){
				this.goLeft(player, maze, thisRoom);
				Log.e("MOVED", "____LEFT____");
			}

			return false;
		}


		private void goRight(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(0, 1);
			enemy.move();

			updateRoomLayout(maze[player.getX()][player.getY()], RIGHT_DIRECTION);
		}

		private void goUp(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(-1, 0);
			enemy.move();

			updateRoomLayout(maze[player.getX()][player.getY()], UP_DIRECTION);
		}

		private void goLeft(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(0, -1);
			enemy.move();

			updateRoomLayout(maze[player.getX()][player.getY()], LEFT_DIRECTION);
		}

		private void goDown(Coordinate player, Room[][] maze, Room room){

			mazeInfo.movePlayer(1, 0);
			enemy.move();

			updateRoomLayout(maze[player.getX()][player.getY()], DOWN_DIRECTION);
		}

	}

	private boolean containsCoordinate(List<Coordinate> list, Coordinate c){
		for (Coordinate in : list){
			if (in.getX() == c.getX() && in.getY() == c.getY()){
				return true;
			}
		}
		return false;
	}


	public static Bitmap getRoomImage(int room){

		if (room == TheMaze.START_ROOM){
			return TheMaze.startRoom;
		}
		else if (room == TheMaze.TREASURE_ROOM){
			return TheMaze.treasureRoom;
		}
		return TheMaze.otherRoom;
	}
	

	public void onBackPressed(){
		Intent i = new Intent(TheMaze.this,TheMazeLayout1.class);    
        startActivity(i);  
	}
}
