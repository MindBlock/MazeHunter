package com.mindblock.mazehunter.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
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
	public static final int START_ROOM = R.drawable.maze_room_start;
	public static final int TREASURE_ROOM = R.drawable.maze_room_chest;
	public static final int OTHER_ROOM = R.drawable.maze_room;
	public static Bitmap startRoom, treasureRoom, otherRoom;
	protected MovementDrawer md;
	protected Enemy enemy;
	private LevelCompletion levelCompletion;
	private AvailableItems availableItems;
	protected boolean levelFinished = false;
	protected boolean levelFailed = false;
	protected boolean levelFailedMovingToEnemy = false;
	private DoorOverlay doorOverlay;
	private LinearLayout mainLayout;

	private AdView adView;
	private PublisherInterstitialAd adInterstitial = null;

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

		//Init available items
		this.availableItems = new AvailableItems(this);

		this.adInterstitial = com.tappx.TAPPXAdInterstitial.Configure(this, this.getString(R.string.tappx_key), new AdListener() {
			@Override 
			public void onAdLoaded() {
				Log.e("AD", "interstitial add is loaded");
			}
			
			@Override
			public void onAdOpened() {
				Log.e("AD", "interstitial add is opened");
			}
			
			@Override
			public void onAdClosed(){
				Log.e("AD", "interstitial add is closed");
			}
		});

		PublisherAdRequest publisherAdRequest = new PublisherAdRequest.Builder().build();
		this.adInterstitial.loadAd(publisherAdRequest);

		//Init the 3 kinds of rooms
		int width = (int) this.getDeviceWidth();
		TheMaze.startRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.START_ROOM), width, width, true);
		TheMaze.treasureRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.TREASURE_ROOM), width, width, true);
		TheMaze.otherRoom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), TheMaze.OTHER_ROOM), width, width, true);

		//Init all door overlay images here:
		this.initDoorImages();

		//get and set all info regarding this maze
		this.mazeInfo = new MazeInfo(this.getMazeInfo());
		this.obtainedTreasureList = new ArrayList<Coordinate>();

		//Initialize level completion map
		this.levelCompletion = new LevelCompletion();
		this.levelCompletion.load(this);

		switch (this.mazeFragment){
		case 1:
			this.enemy = new Enemy(this.mazeInfo, Enemy.STRATEGY_RANDOM);
			break;
		case 2:
			this.enemy = new Enemy(this.mazeInfo, Enemy.STRATEGY_PHEROMONES);
			break;
		case 3:
			this.enemy = new Enemy(this.mazeInfo, Enemy.STRATEGY_HARD);
			break;
		}
		this.addMazeLayout();
	}


	private void addMazeLayout(){
		RelativeLayout rlMazeLayout = (RelativeLayout) findViewById(R.id.rl_maze_game_layout);
		rlMazeLayout.setBackgroundResource(R.drawable.background_maze_game);

		this.mainLayout = new LinearLayout(this);

		//Layout stuff
		this.mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.mainLayout.setOrientation(LinearLayout.VERTICAL);
		this.mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		//Add usable item bar
		this.mainLayout.addView(this.getUseableItemsLayout(), 0);
		//Add background image view
		this.initStartRoomLayout();
		this.mainLayout.addView(this.roomLayout, 1);

		//add information layer
		this.mainLayout.addView(this.getInformationLayout(), 2);

		//add ad-banner
		this.mainLayout.addView(this.getAdLayout(), 3);

		rlMazeLayout.addView(this.mainLayout);
	}


	private LinearLayout getAdLayout(){

		LinearLayout adLayout = new LinearLayout(this);
		adLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		adLayout.setGravity(Gravity.BOTTOM);

		this.adView = new AdView(this);
		this.adView.setAdSize(AdSize.BANNER);
		this.adView.setAdUnitId(this.getString(R.string.banner_ad_unit_id));

		AdRequest adRequest = new AdRequest.Builder().build();

		this.adView.loadAd(adRequest);

		adLayout.addView(this.adView);

		return adLayout;
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

		for (Iterator<String> it = this.availableItems.getAllAvailableItems().keySet().iterator(); it.hasNext();){

			LinearLayout iButtonLayout = new LinearLayout(this);
			iButtonLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			iButtonLayout.setOrientation(LinearLayout.VERTICAL);

			ImageButton useableItem = new ImageButton(this);

			//Remove padding
			useableItem.setPadding(5, 10, 5, 10);

			//Calculate width fitting:
			useableItem.setAdjustViewBounds(true);
			int maxWidth = (int) ((this.getDeviceWidth()-10.0)/NUMBER_OF_USEABLE_ITEMS);
			useableItem.setMaxWidth(maxWidth);

			//Check which image needs to be displayed
			String key = it.next();
			UseableSkill useableSkill = this.availableItems.getResources(key);
			if (this.availableItems.getAllAvailableItems().get(key)){
				if (this.availableItems.selected(key)){
					useableItem.setImageResource(useableSkill.getSelectedID());
				}
				else {
					useableItem.setImageResource(useableSkill.getUnlockedID());
				}
			}
			else {
				useableItem.setImageResource(useableSkill.getLockedID());
			}


			useableItem.setBackgroundColor(Color.TRANSPARENT);


			switch(useableSkill){
			case DISTRACT: useableItem.setOnClickListener(new UseableClicked(key));
			break;
			case JUMP: useableItem.setOnClickListener(new UseableClicked(key));
			break;
			case RETURN: useableItem.setOnClickListener(new ReturnClicked(key));
			break;
			case REVEAL: useableItem.setOnClickListener(new RevealClicked(key));
			break;
			case TREASURE: useableItem.setOnClickListener(new TreasureClicked(key));
			break;
			case FREEZE:useableItem.setOnClickListener(new UseableClicked(key));
			break;
			}

			iButtonLayout.addView(useableItem);

			useableItemsLayout.addView(iButtonLayout);
		}

		return useableItemsLayout;
	}


	protected void redrawUseableItemLayout(){
		this.mainLayout.removeViewAt(0);
		this.mainLayout.addView(this.getUseableItemsLayout(), 0);
	}

	public class UseableClicked implements OnClickListener{

		private String name;

		public UseableClicked(String name) {
			this.name = name;
		}

		@Override
		public void onClick(View v) {
			if (availableItems.getAllAvailableItems().get(this.name)){
				//If already selected, unselect
				if (availableItems.selected(name)){
					availableItems.setSelected(name, false);
				}
				else {
					availableItems.setSelected(name, true);
				}
				redrawUseableItemLayout();
			}
		}
	}

	public class ReturnClicked implements OnClickListener{

		private String name;

		public ReturnClicked(String name) {
			this.name = name;
		}

		@Override
		public void onClick(View v) {
			if (availableItems.getAllAvailableItems().get(this.name)){
				availableItems.useItem(name);
				mazeInfo.moveToStart();
				Coordinate player = mazeInfo.getPlayerCoordinate();
				md.returnStart(TheMaze.START_ROOM, doorOverlay.getDoorOverlayImage(mazeInfo.getMaze()[player.getX()][player.getY()].getBitRoom()));
				redrawUseableItemLayout();
			}
		}
	}

	public class TreasureClicked implements OnClickListener{

		private String name;

		public TreasureClicked(String name) {
			this.name = name;
		}

		@Override
		public void onClick(View v) {
			if (availableItems.getAllAvailableItems().get(this.name)){

				boolean reveal = false;
				for (Coordinate t : mazeInfo.getTreasureList()){
					if (!containsCoordinate(obtainedTreasureList, t)){
						mazeInfo.getMaze()[t.getX()][t.getY()].setVisited();
						reveal = true;
						break;
					}
				}
				if (reveal)
					availableItems.useItem(name);
				redrawUseableItemLayout();
			}
		}

	}


	public class RevealClicked implements OnClickListener{

		private String name;

		public RevealClicked(String name) {
			this.name = name;
		}

		@Override
		public void onClick(View v) {
			//Check if not every room has been visited yet
			boolean allVisited = true;
			List<Coordinate> notVisited = new ArrayList<Coordinate>();
			for (int i = 0; i < mazeInfo.getMaze().length; i++){
				for (int j = 0; j < mazeInfo.getMaze()[0].length; j++){
					if (!mazeInfo.getMaze()[i][j].isVisited()){
						allVisited = false; 
						notVisited.add(new Coordinate(i, j));
					}
				}
			}

			if (availableItems.getAllAvailableItems().get(this.name) && !allVisited){
				availableItems.useItem(name);
				Coordinate r = notVisited.get(new Random().nextInt(notVisited.size()));
				mazeInfo.getMaze()[r.getX()][r.getY()].setVisited();
				redrawUseableItemLayout();
			}
		}
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
		this.md = new MovementDrawer(this, this.doorOverlay.getDoorOverlayImage(
				maze[player.getX()][player.getY()].getBitRoom()), NO_DIRECTION, START_ROOM, maze.length);
		this.roomLayout.addView(this.md);

	}


	private void initDoorImages(){
		this.doorOverlay = DoorOverlay.getInstance((int) this.getDeviceWidth(), this);
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

		if (this.levelFailed){
			this.md.gameOverMoving(this.enemy.getLastDirection());
		}

		//Next: update the doors overlay and movement:
		Room[][] maze = mazeInfo.getMaze();
		Coordinate player = this.mazeInfo.getPlayerCoordinate();
		this.md.updateRoom(roomSort, direction, 
				this.doorOverlay.getDoorOverlayImage(maze[player.getX()][player.getY()].getBitRoom()));
	}



	private void updateTreasureCount(int treasuresObtained){

		//Check if player bested own score
		int bestTreasures = 0;

		if (TheMazeEasy.COMPLETION_GOLD.equals(this.completion)) bestTreasures = 3;
		else if (TheMazeEasy.COMPLETION_SILVER.equals(this.completion)) bestTreasures = 2;
		else if (TheMazeEasy.COMPLETION_BRONZE.equals(this.completion)) bestTreasures = 1;

		if (treasuresObtained > bestTreasures){
			String newCompletion = TheMazeEasy.COMPLETION_BRONZE;

			if (treasuresObtained == 2) newCompletion = TheMazeEasy.COMPLETION_SILVER;
			else if (treasuresObtained == 3) newCompletion = TheMazeEasy.COMPLETION_GOLD;

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
			//To ensure maze is finished:
			this.levelFinished = true;
			this.md.levelComplete();
			break;
		default :
			Bitmap b0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.obtained_treasures_0), 
					(int) this.getDeviceWidth()/3, (int) this.getDeviceWidth()/9, false);
			this.treasureCount.setImageBitmap(b0);
			break;
		}
	}


	private void levelComplete(){
		
		//Wait until drawing is finished
		while (this.md != null && this.md.isDrawing());

		roomLayout.removeAllViews();
		md = null;
		Intent i;
		if (mazeFragment == 1)
			i = new Intent(TheMaze.this,TheMazeEasy.class);
		else if (mazeFragment == 2)
			i = new Intent(TheMaze.this,TheMazeNormal.class);
		else 
			i = new Intent(TheMaze.this,TheMazeHard.class);
		startActivity(i);
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
			InputStream mazeText = getAssets().open("mazes/maze" + Integer.toString(this.level+(100*(this.mazeFragment-1))) + ".txt");
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

			//Check if level is finished, any touch will exit to maze layout.
			if (levelFinished || levelFailed || levelFailedMovingToEnemy){
				levelComplete();
				return true;
			}

			//Do nothing if still drawing
			if (md.isDrawing() || MovementDrawer.exitMinimap){
				return false;
			}

			float x = event.getX(); //the most recent x coordinate of the touch
			float y = event.getY(); //the most recent y coordinate of the touch
			double direction = Math.atan2(this.sizeImageOver2 - y, x - this.sizeImageOver2) + Math.PI; //[0 ... 2pi]

			//Check if mini-map is clicked, bottom right: 5*size/6, 5*size/6
			if (x > 5*getDeviceWidth()/6 && y > 5*getDeviceWidth()/6){
				Log.e("MINIMAP", "MINIMAP CLICKED");
				MovementDrawer.running = true;
				md.minimapClicked(mazeInfo.getMaze());
				//exit method, no movement should be possible in either case
				return false;
			}

			//Check if skip-turn is clicked, top right: 5*size/6, 0
			if (!MovementDrawer.minimapClicked && x > 5*getDeviceWidth()/6 && y < getDeviceWidth()/6){
				Log.e("SKIPTURN", "SKIP TURN CLICKED");

				int startX = mazeInfo.getEnemyCoordinate().getY();
				int startY = mazeInfo.getEnemyCoordinate().getX();
				enemy.move();
				int endX = mazeInfo.getEnemyCoordinate().getY();
				int endY = mazeInfo.getEnemyCoordinate().getX();

				MovementDrawer.running = true;
				md.skipTurnClicked(mazeInfo.getMaze(), startX, startY, endX, endY);

				//Check if game is over
				if (this.sameCoordinate(mazeInfo.getEnemyCoordinate(), mazeInfo.getPlayerCoordinate())){
					Log.e("GAME_OVER", "GAME_OVER");
					md.gameOverSkipTurn();
					levelFailed = true;
				}

				//exit method, no movement should be possible
				return false;
			}

			if (MovementDrawer.minimapClicked) {
				return false;
			}

			Room[][] maze = mazeInfo.getMaze();
			Coordinate player = mazeInfo.getPlayerCoordinate();
			Room thisRoom = maze[player.getX()][player.getY()];

			//Down
			if (direction > Math.PI/4 && direction < 3*Math.PI/4 && (thisRoom.isDown() || availableItems.selected("JUMP"))){
				this.goDown(player, maze, thisRoom);
				Log.e("MOVED", "____DOWN____");
			}
			//Right
			else if (direction > 3*Math.PI/4 && direction <= 5*Math.PI/4 && (thisRoom.isRight() || availableItems.selected("JUMP"))){
				this.goRight(player, maze, thisRoom);
				Log.e("MOVED", "____RIGHT____");
			}
			//Up
			else if (direction > 5*Math.PI/4 && direction <= 7*Math.PI/4 && (thisRoom.isUp() || availableItems.selected("JUMP"))){
				this.goUp(player, maze, thisRoom);
				Log.e("MOVED", "____UP____");
			}
			//Left
			else if ((direction > 7*Math.PI/4 || direction <= Math.PI/4) && (thisRoom.isLeft() || availableItems.selected("JUMP"))){
				this.goLeft(player, maze, thisRoom);
				Log.e("MOVED", "____LEFT____");
			}

			return false;
		}


		private void goRight(Coordinate player, Room[][] maze, Room room){

			//Check if jump used
			if (!room.isRight() && player.getY() < mazeInfo.getMaze().length -1)
				availableItems.useItem("JUMP");
			else 
				availableItems.setSelected("JUMP", false);
			redrawUseableItemLayout();
			if (!(player.getY() < mazeInfo.getMaze().length -1))
				return;

			if (availableItems.selected("DISTRACT")){
				room.setDistractPlaced(true, TheMaze.RIGHT_DIRECTION);
				availableItems.useItem("DISTRACT");
				redrawUseableItemLayout();
			}

			mazeInfo.movePlayer(0, 1);
			//Check if moved into enemy room
			if (this.sameCoordinate(mazeInfo.getPlayerCoordinate(), mazeInfo.getEnemyCoordinate())){
				md.gameOverMovingToEnemy();
				levelFailedMovingToEnemy = true;
			}

			if (availableItems.selected("FREEZE")){
				availableItems.useItem("FREEZE");
				redrawUseableItemLayout();
			}
			else
				enemy.move();

			//Check if game is over
			if (this.sameCoordinate(mazeInfo.getEnemyCoordinate(), mazeInfo.getPlayerCoordinate())){
				Log.e("GAME_OVER", "GAME_OVER");
				levelFailed = true;
				
				//show add
				if (adInterstitial.isLoaded()){
					com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
				}
			}

			updateRoomLayout(maze[player.getX()][player.getY()], RIGHT_DIRECTION);
		}

		private void goUp(Coordinate player, Room[][] maze, Room room){

			//Check if jump used
			if (!room.isUp() && player.getX() > 0)
				availableItems.useItem("JUMP");
			else
				availableItems.setSelected("JUMP", false);
			redrawUseableItemLayout();
			if (!(player.getX() > 0))
				return;

			if (availableItems.selected("DISTRACT")){
				room.setDistractPlaced(true, TheMaze.UP_DIRECTION);
				availableItems.useItem("DISTRACT");
				redrawUseableItemLayout();
			}

			mazeInfo.movePlayer(-1, 0);
			//Check if moved into enemy room
			if (this.sameCoordinate(mazeInfo.getPlayerCoordinate(), mazeInfo.getEnemyCoordinate())){
				md.gameOverMovingToEnemy();
				levelFailedMovingToEnemy = true;
			}

			if (availableItems.selected("FREEZE")){
				availableItems.useItem("FREEZE");
				redrawUseableItemLayout();
			}
			else
				enemy.move();

			//Check if game is over
			if (this.sameCoordinate(mazeInfo.getEnemyCoordinate(), mazeInfo.getPlayerCoordinate())){
				Log.e("GAME_OVER", "GAME_OVER");
				levelFailed = true;
				
				//show add
				if (adInterstitial.isLoaded()){
					com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
				}
			}

			updateRoomLayout(maze[player.getX()][player.getY()], UP_DIRECTION);
		}

		private void goLeft(Coordinate player, Room[][] maze, Room room){

			//Check if jump used
			if (!room.isLeft() && player.getY() > 0)
				availableItems.useItem("JUMP");
			else 
				availableItems.setSelected("JUMP", false);
			redrawUseableItemLayout();
			if (!(player.getY() > 0))
				return;

			if (availableItems.selected("DISTRACT")){
				room.setDistractPlaced(true, TheMaze.LEFT_DIRECTION);
				availableItems.useItem("DISTRACT");
				redrawUseableItemLayout();
			}

			mazeInfo.movePlayer(0, -1);
			//Check if moved into enemy room
			if (this.sameCoordinate(mazeInfo.getPlayerCoordinate(), mazeInfo.getEnemyCoordinate())){
				md.gameOverMovingToEnemy();
				levelFailedMovingToEnemy = true;
			}

			if (availableItems.selected("FREEZE")){
				availableItems.useItem("FREEZE");
				redrawUseableItemLayout();
			}
			else
				enemy.move();

			//Check if game is over
			if (this.sameCoordinate(mazeInfo.getEnemyCoordinate(), mazeInfo.getPlayerCoordinate())){
				Log.e("GAME_OVER", "GAME_OVER");
				levelFailed = true;
				
				//show add
				if (adInterstitial.isLoaded()){
					com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
				}
			}

			updateRoomLayout(maze[player.getX()][player.getY()], LEFT_DIRECTION);
		}

		private void goDown(Coordinate player, Room[][] maze, Room room){

			//Check if jump used
			if (!room.isDown() && player.getX() < mazeInfo.getMaze().length -1)
				availableItems.useItem("JUMP");
			else 
				availableItems.setSelected("JUMP", false);
			redrawUseableItemLayout();
			if (!(player.getX() < mazeInfo.getMaze().length -1))
				return;

			if (availableItems.selected("DISTRACT")){
				room.setDistractPlaced(true, TheMaze.DOWN_DIRECTION);
				availableItems.useItem("DISTRACT");
				redrawUseableItemLayout();
			}


			mazeInfo.movePlayer(1, 0);
			//Check if moved into enemy room
			if (this.sameCoordinate(mazeInfo.getPlayerCoordinate(), mazeInfo.getEnemyCoordinate())){
				md.gameOverMovingToEnemy();
				levelFailedMovingToEnemy = true;
			}

			if (availableItems.selected("FREEZE")){
				availableItems.useItem("FREEZE");
				redrawUseableItemLayout();
			}
			else
				enemy.move();

			//Check if game is over
			if (this.sameCoordinate(mazeInfo.getEnemyCoordinate(), mazeInfo.getPlayerCoordinate())){
				Log.e("GAME_OVER", "GAME_OVER");
				levelFailed = true;
				
				//show add
				if (adInterstitial.isLoaded()){
					com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
				}
			}

			updateRoomLayout(maze[player.getX()][player.getY()], DOWN_DIRECTION);
		}

		private boolean sameCoordinate(Coordinate a, Coordinate b){
			return (a.getX() == b.getX() && a.getY() == b.getY());
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

		while (this.md.isDrawing());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you wish to exit?").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).show();
	}


	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:
				roomLayout.removeAllViews();
				md = null;
				Intent i;
				if (mazeFragment == 1)
					i = new Intent(TheMaze.this,TheMazeEasy.class);
				else if (mazeFragment == 2)
					i = new Intent(TheMaze.this,TheMazeNormal.class);
				else 
					i = new Intent(TheMaze.this,TheMazeHard.class);
				startActivity(i); 
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				//No button clicked
				break;
			}
		}
	};

	
	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		try {
			md.surfaceDestroyed(md.getHolder());
		}catch(Exception e){};
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		if (md.surfaceCreated())
			md.restartSurface();
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

}
