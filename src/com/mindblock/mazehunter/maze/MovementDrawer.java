package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.save.PlayerInformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovementDrawer extends SurfaceView implements SurfaceHolder.Callback{

	public static Bitmap doorImageRoom1, doorImageRoom2, roomBackground1, roomBackground2, minimapEnter, minimapExit, skipTurn;
	public Bitmap enemyToken, playerToken, completedImage, failedImage;
	private Paint p;
	public static int direction, previousDirection;
	private float xPlayer, yPlayer, xEnemy, yEnemy;
	private volatile MainThread thread;
	public static boolean running = true;
	public static boolean moveingOutOfRoom = true;
	public static boolean minimapClicked = false;
	public static boolean skipTurnClicked = false;
	public static boolean exitMinimap = false;
	public boolean gameOverMoving = false;
	private boolean surfaceCreated = false;
	private boolean enemyStartSet = false;
	private int directionEnemy;
	private boolean initiation = true;
	private boolean levelCompleted = false;
	private boolean gameOverSkipTurn = false;
	private boolean gameOverMovingToEnemy = false;
	private boolean returnStart = false;
	private static boolean renderingSkip = false;
	private static boolean exitSkipTurn = false;
	private int currentFrames = 0;
	private int mazeSize;
	private Room[][] roomsVisited;
	private PlayerImage pImage;
	private EnemyImage eImage;
	private int size;
	private int startX, startY, endX, endY;
	private long startTimeEnemy;

	private final int ENEMY_MOVEMENT_TIME = 2000;//ms
	private final int MOVE_OUT = 0;
	private final int MOVE_IN = 1;
	private final int MOVE_DONE = -1;
	private final int FRAMES_PER_UPDATE = 10;

	private double SPEED;



	public MovementDrawer(Context context, Bitmap doorOverlayRoom, int direction, int roomDrawable, int mazeSize) {
		super(context);

		this.SPEED = 7.5*(new PlayerInformation(context).getSpeedMultiplier()+1);
		this.mazeSize = mazeSize;
		this.size = this.getDeviceWidth();
		MovementDrawer.direction = direction;
		MovementDrawer.previousDirection = direction;

		Bitmap b = null;

		//Scale the image
		b = BitmapFactory.decodeResource(getResources(), roomDrawable);
		MovementDrawer.roomBackground1 = Bitmap.createScaledBitmap(b, this.size, this.size, true);
		MovementDrawer.doorImageRoom1 = doorOverlayRoom;

		//Set room 2 to null
		MovementDrawer.roomBackground2 = null;
		MovementDrawer.doorImageRoom2 = null;

		b = BitmapFactory.decodeResource(getResources(), R.drawable.minimap);
		MovementDrawer.minimapEnter = Bitmap.createScaledBitmap(b, this.size/6, this.size/6, true);

		b = BitmapFactory.decodeResource(getResources(), R.drawable.exit_minimap);
		MovementDrawer.minimapExit = Bitmap.createScaledBitmap(b, this.size/6, this.size/6, true);

		b = BitmapFactory.decodeResource(getResources(), R.drawable.skip_turn);
		MovementDrawer.skipTurn = Bitmap.createScaledBitmap(b, this.size/6, this.size/6, true);

		int sizePerRoom = (int) ((double) (0.7*this.size)/this.mazeSize) - 2;

		b = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_token);
		this.enemyToken = Bitmap.createScaledBitmap(b, sizePerRoom, sizePerRoom, true);

		b = BitmapFactory.decodeResource(getResources(), R.drawable.player_token);
		this.playerToken = Bitmap.createScaledBitmap(b, sizePerRoom, sizePerRoom, true);

		b = BitmapFactory.decodeResource(getResources(), R.drawable.level_completed);
		this.completedImage = Bitmap.createScaledBitmap(b, 2*this.size/3, this.size/2, true);

		b = BitmapFactory.decodeResource(getResources(), R.drawable.level_failed);
		this.failedImage = Bitmap.createScaledBitmap(b, 2*this.size/3, this.size/2, true);

		this.xPlayer = this.size/2;
		this.yPlayer = this.size/2;

		this.pImage = new PlayerImage(context, this.size);
		this.eImage = new EnemyImage(context, this.size);

		setZOrderOnTop(true);
		// Tell the SurfaceHolder ( -> getHolder() ) to receive SurfaceHolder
		// Callback
		getHolder().addCallback(this);

		this.thread = new MainThread(getHolder(), this);

		this.p = new Paint();

		b.recycle();
		b = null;
	}


	public synchronized void drawPlayer(Canvas canvas){
		if (MovementDrawer.running && canvas != null) {

			if(this.initiation){
				this.drawFirstRoom(canvas, false);
			}

			if (this.returnStart){
				this.returnStart = false;
				this.xPlayer = this.size/2;
				this.yPlayer = this.size/2;
				this.drawSecondRoom(canvas, false, false);
				this.drawMinimapSmall(canvas, false);
				this.drawSkipTurn(canvas);
				MovementDrawer.running = false;
				return;
			}

			//Enemy movement when game is over, draw only second room
			if (this.gameOverMoving && !MovementDrawer.moveingOutOfRoom){
				this.moveEnemy(canvas);
			}

			int moveResult = this.movePlayer();
			if (this.MOVE_OUT == moveResult){
				this.drawFirstRoom(canvas, true);
				this.initiation = false;
			}
			else if (this.MOVE_IN == moveResult){
				this.drawSecondRoom(canvas, true, this.gameOverMoving);
				this.initiation = false;
			}

			//If neither moving out or into a room, stop updating and drawing
			else {
				if (!this.initiation){
					this.drawSecondRoom(canvas, false, this.gameOverMoving);
				}
				else {
					this.drawFirstRoom(canvas, false);
				}
				//MINIMAP HANDLING
				if (MovementDrawer.minimapClicked)
					this.drawMinimap(canvas, false);
				//If the exit minimap button has been clicked, draw
				if (MovementDrawer.exitMinimap){

					//force draw
					this.currentFrames = 0;

					try{
						this.drawSecondRoom(canvas, false, false);
					} catch(NullPointerException e){
						this.drawFirstRoom(canvas, false);
					}
					MovementDrawer.exitMinimap = false;
				}

				//SKIP TURN HANDLING
				if (MovementDrawer.skipTurnClicked){
					MovementDrawer.renderingSkip = true;
					this.startTimeEnemy = System.currentTimeMillis();
					MovementDrawer.skipTurnClicked = false;
				}
				if (MovementDrawer.exitSkipTurn){
					MovementDrawer.exitSkipTurn = false;

					//force draw
					this.currentFrames = 0;

					try{
						this.drawSecondRoom(canvas, false, false);
					} catch(NullPointerException e){
						this.drawFirstRoom(canvas, false);
					}
				}
				if(MovementDrawer.renderingSkip){
					this.moveEnemyMinimap(canvas);
				}
				else {
					this.drawMinimapSmall(canvas, MovementDrawer.minimapClicked);
					this.drawSkipTurn(canvas);
					MovementDrawer.running = false;
					Log.e("MovementDrawer", "Drawing set to false");
				}
			}
		}
	}


	public void drawFirstRoom(Canvas canvas, boolean updateImage){

		canvas.drawBitmap(MovementDrawer.roomBackground1, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom1, 0, 0, this.p);

		this.drawPlayerInRoom(canvas, updateImage, false);

		if (this.gameOverSkipTurn){
			canvas.drawBitmap(this.eImage.getDirection(TheMaze.DOWN_DIRECTION), this.size/2 - this.size/8, this.size/2 - this.size/8, this.p);
			canvas.drawBitmap(this.failedImage, this.size/6, this.size/5, this.p);
		}

	}

	public void drawSecondRoom(Canvas canvas, boolean updateImage, boolean drawEnemy){

		canvas.drawBitmap(MovementDrawer.roomBackground2, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom2, 0, 0, this.p);

		this.drawPlayerInRoom(canvas, updateImage, drawEnemy);

		//Check if level is completed (3 treasures)
		if (this.levelCompleted){
			canvas.drawBitmap(this.completedImage, this.size/6, this.size/5, this.p);
		}

		//Check if level is failed
		else if (this.gameOverMoving || this.gameOverSkipTurn){
			canvas.drawBitmap(this.failedImage, this.size/6, this.size/5, this.p);
		}

		else if (this.gameOverMovingToEnemy){
			canvas.drawBitmap(this.failedImage, this.size/6, this.size/5, this.p);
			canvas.drawBitmap(this.eImage.getDirection(TheMaze.DOWN_DIRECTION), this.size/2 - this.size/8, this.size/2 - this.size/8, this.p);
		}
	}


	private void drawPlayerInRoom(Canvas canvas, boolean updateImage, boolean drawEnemy){

		if (!updateImage){
			if (drawEnemy){
				canvas.drawBitmap(this.eImage.getDirection(TheMaze.DOWN_DIRECTION), this.xEnemy - this.size/8, this.yEnemy - this.size/8, this.p);
			}
			else if(this.gameOverMovingToEnemy || this.gameOverSkipTurn){
				canvas.drawBitmap(this.eImage.getDirection(TheMaze.DOWN_DIRECTION), this.size/2 - this.size/8, this.size/2 - this.size/8, this.p);
			}
			else {
				canvas.drawBitmap(this.pImage.getImage(MovementDrawer.previousDirection), this.xPlayer - this.size/8, this.yPlayer - this.size/8, this.p);
			}
		}

		//Only update image once per X frames passed
		else {
			if (this.currentFrames % this.FRAMES_PER_UPDATE == 0){
				canvas.drawBitmap(this.pImage.getNextImage(MovementDrawer.direction), this.xPlayer - this.size/8, this.yPlayer - this.size/8, this.p);
				this.currentFrames = 0;

				if (drawEnemy){
					canvas.drawBitmap(this.eImage.getDirection(this.directionEnemy), this.xEnemy - this.size/8, this.yEnemy - this.size/8, this.p);
				}
			}
			else {
				canvas.drawBitmap(this.pImage.getImage(MovementDrawer.direction), this.xPlayer - this.size/8, this.yPlayer - this.size/8, this.p);

				if (drawEnemy){
					canvas.drawBitmap(this.eImage.getDirection(this.directionEnemy), this.xEnemy - this.size/8, this.yEnemy - this.size/8, this.p);
				}
			}
			this.currentFrames += 1;
		}
	}


	private void moveEnemyMinimap(Canvas canvas){

		int size = (int) (0.7*this.size);
		int offSet = (int) (0.15*this.size);
		double sizePerRoom = ((double) size/this.mazeSize);
		long dT = System.currentTimeMillis() - this.startTimeEnemy;

		if(dT < this.ENEMY_MOVEMENT_TIME){


			this.drawMinimap(canvas, true);
			canvas.drawBitmap(enemyToken, 
					offSet + (int) sizePerRoom*startX + (int) (sizePerRoom*(endX-startX)*((double) dT/ENEMY_MOVEMENT_TIME)), 
					offSet + (int) sizePerRoom*startY + (int) (sizePerRoom*(endY-startY)*((double) dT/ENEMY_MOVEMENT_TIME)), this.p);

			//Try to sleep for 20ms
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}
		}
		else {
			MovementDrawer.exitSkipTurn = true;
			MovementDrawer.renderingSkip = false;
		}
	}

	public void drawMinimapSmall(Canvas canvas, boolean minimapEnabled){
		if (minimapEnabled)
			canvas.drawBitmap(MovementDrawer.minimapExit, 5*this.size/6, 5*this.size/6, this.p);
		else
			canvas.drawBitmap(MovementDrawer.minimapEnter, 5*this.size/6, 5*this.size/6, this.p);
	}

	private void drawSkipTurn(Canvas canvas){
		canvas.drawBitmap(MovementDrawer.skipTurn, 5*this.size/6, 0, this.p);
	}

	private void drawMinimap(Canvas canvas, boolean enemyMoving){
		//Fill 70% with minimap
		int size = (int) (0.7*this.size);
		int offSet = (int) (0.15*this.size);
		int sizePerRoom = (int) ((double) size/this.mazeSize);

		//Outer contour
		this.p.setColor(Color.rgb(0, 38, 78));
		Rect r = new Rect(offSet-5, offSet-5, size+5+offSet, size+5+offSet);
		canvas.drawRect(r, this.p);

		int width = this.roomsVisited.length;
		int height = this.roomsVisited[0].length;

		for (int w = 0; w < width; w++){
			for (int h = 0; h < height; h++){

				Room room = this.roomsVisited[w][h];
				if (room.isVisited())
					if (room.isTreasure())
						this.p.setColor(Color.rgb(242, 255, 190));
					else
						this.p.setColor(Color.rgb(236, 255, 255));
				else 
					this.p.setColor(Color.rgb(49, 46, 89));

				Rect rect = new Rect(offSet + h*sizePerRoom + 1, offSet + w*sizePerRoom + 1, 
						offSet + (h+1)*sizePerRoom - 1, offSet + (w+1)*sizePerRoom - 1);
				canvas.drawRect(rect, this.p);

				if (room.isPlayerHere()){
					canvas.drawBitmap(this.playerToken, 
							offSet + h*sizePerRoom, offSet + w*sizePerRoom, this.p);
				}

				if (room.isEnemy() && !enemyMoving){
					canvas.drawBitmap(this.enemyToken, 
							offSet + h*sizePerRoom, offSet + w*sizePerRoom, this.p);
				}

				//Check doors
				if (room.isVisited()){

					this.p.setColor(Color.rgb(78, 168, 15));

					if (room.isUp()){
						Rect doorRect = new Rect(offSet + h*sizePerRoom + (int) (0.333*sizePerRoom), 
								offSet + w*sizePerRoom, 
								offSet + h*sizePerRoom + (int) (0.667*sizePerRoom), 
								offSet + w*sizePerRoom + 3);
						canvas.drawRect(doorRect, this.p);
					}

					if (room.isDown()){
						Rect doorRect = new Rect(offSet + h*sizePerRoom + (int) (0.333*sizePerRoom), 
								offSet + (w+1)*sizePerRoom - 3, 
								offSet + h*sizePerRoom + (int) (0.667*sizePerRoom), 
								offSet + (w+1)*sizePerRoom);
						canvas.drawRect(doorRect, this.p);
					}

					if (room.isLeft()){
						Rect doorRect = new Rect(offSet + h*sizePerRoom, 
								offSet + w*sizePerRoom + (int) (0.333*sizePerRoom), 
								offSet + h*sizePerRoom + 3, 
								offSet + w*sizePerRoom + (int) (0.667*sizePerRoom));
						canvas.drawRect(doorRect, this.p);
					}

					if (room.isRight()){
						Rect doorRect = new Rect(offSet + (h+1)*sizePerRoom - 3, 
								offSet + w*sizePerRoom + (int) (0.333*sizePerRoom), 
								offSet + (h+1)*sizePerRoom, 
								offSet + w*sizePerRoom + (int) (0.667*sizePerRoom));
						canvas.drawRect(doorRect, this.p);
					}
				}
			}
		}
	}


	private void moveEnemy(Canvas canvas){

		if (this.directionEnemy == TheMaze.UP_DIRECTION){

			if (!this.enemyStartSet){
				this.xEnemy = this.size/2;
				this.yEnemy = this.size;
				this.enemyStartSet = true;
			}
			this.yEnemy -= this.SPEED;
		}
		else if (this.directionEnemy == TheMaze.DOWN_DIRECTION){

			if (!this.enemyStartSet){
				this.xEnemy = this.size/2;
				this.yEnemy = 0;
				this.enemyStartSet = true;
			}
			this.yEnemy += this.SPEED;
		}
		else if (this.directionEnemy == TheMaze.LEFT_DIRECTION){

			if (!this.enemyStartSet){
				this.xEnemy = this.size;
				this.yEnemy = this.size/2;
				this.enemyStartSet = true;
			}
			this.xEnemy -= this.SPEED;
		}
		else if (this.directionEnemy == TheMaze.RIGHT_DIRECTION){

			if (!this.enemyStartSet){
				this.xEnemy = 0;
				this.yEnemy = this.size/2;
				this.enemyStartSet = true;
			}
			this.xEnemy += this.SPEED;
		}

	}

	private int movePlayer(){
		float size = this.getDeviceWidth();

		//Up
		if (MovementDrawer.direction == TheMaze.UP_DIRECTION){
			if (MovementDrawer.moveingOutOfRoom && this.yPlayer >= 0){
				this.yPlayer -= this.SPEED;

				//If after iteration, the player is now outside of screen:
				if (this.yPlayer < 0){
					MovementDrawer.moveingOutOfRoom = false;
					//set y coordinate to bottom of view
					this.yPlayer = size;
					return this.MOVE_IN;
				}
				return this.MOVE_OUT;
			}
			else if (!MovementDrawer.moveingOutOfRoom && this.yPlayer >= size/2){
				this.yPlayer -= this.SPEED;
				return this.MOVE_IN;
			}
			else {
				//Set player to start position
				MovementDrawer.direction = TheMaze.NO_DIRECTION;
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Right
		else if (MovementDrawer.direction == TheMaze.RIGHT_DIRECTION){
			if (MovementDrawer.moveingOutOfRoom && this.xPlayer <= size){
				this.xPlayer += this.SPEED;

				//If after iteration, the player is now outside of screen:
				if (this.xPlayer > size){
					MovementDrawer.moveingOutOfRoom = false;
					//set x coordinate to left of view
					this.xPlayer = 0;
					return this.MOVE_IN;
				}
				return this.MOVE_OUT;
			}
			else if (!MovementDrawer.moveingOutOfRoom && this.xPlayer <= size/2){
				this.xPlayer += this.SPEED;
				return this.MOVE_IN;
			}
			else {
				//Set player to start position
				MovementDrawer.direction = TheMaze.NO_DIRECTION;
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Left
		else if (MovementDrawer.direction == TheMaze.LEFT_DIRECTION){
			if (MovementDrawer.moveingOutOfRoom && this.xPlayer >= 0){
				this.xPlayer -= this.SPEED;

				//If after iteration, the player is now outside of screen:
				if (this.xPlayer < 0){
					MovementDrawer.moveingOutOfRoom = false;
					//set x coordinate to right of view
					this.xPlayer = size;
					return this.MOVE_IN;
				}
				return this.MOVE_OUT;
			}
			else if (!MovementDrawer.moveingOutOfRoom && this.xPlayer >= size/2){
				this.xPlayer -= this.SPEED;
				return this.MOVE_IN;
			}
			else {
				//Set player to start position
				MovementDrawer.direction = TheMaze.NO_DIRECTION;
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Down
		else if (MovementDrawer.direction == TheMaze.DOWN_DIRECTION){
			if (MovementDrawer.moveingOutOfRoom && this.yPlayer <= size){
				this.yPlayer += this.SPEED;

				//If after iteration, the player is now outside of screen:
				if (this.yPlayer > size){
					MovementDrawer.moveingOutOfRoom = false;
					//set y coordinate to top of view
					this.yPlayer = 0;
					return this.MOVE_IN;
				}
				return this.MOVE_OUT;
			}
			else if (!MovementDrawer.moveingOutOfRoom && this.yPlayer <= size/2){
				this.yPlayer += this.SPEED;
				return this.MOVE_IN;
			}
			else {
				//Set player to start position
				MovementDrawer.direction = TheMaze.NO_DIRECTION;
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//If no direction is chosen, don't do anything
		return this.MOVE_DONE;
	}

	/**
	 * 
	 * @return the width in pixels of the devices
	 */
	protected int getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}

	public boolean isDrawing(){
		return MovementDrawer.running;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!this.surfaceCreated){
			MovementDrawer.running = true;
			this.thread.setRunning(true);
			this.thread.start();
			this.surfaceCreated = true;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.shutdownThread();
	}

	public void updateRoom(int roomDrawable, int direction, Bitmap doorOverlayRoom){
		MovementDrawer.direction = direction;
		MovementDrawer.previousDirection = direction;
		//Check if the old room needs to be set
		if (MovementDrawer.roomBackground2 != null && MovementDrawer.doorImageRoom2 != null){

			MovementDrawer.doorImageRoom1 = MovementDrawer.doorImageRoom2;
			MovementDrawer.roomBackground1 = MovementDrawer.roomBackground2;
		}
		//Scale the image
		MovementDrawer.roomBackground2 = TheMaze.getRoomImage(roomDrawable);
		MovementDrawer.doorImageRoom2 = doorOverlayRoom;

		MovementDrawer.moveingOutOfRoom = true;
		MovementDrawer.running = true;
	}

	private void shutdownThread(){
		Log.e("MovementDrawer", "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		MovementDrawer.minimapClicked = false;
		MovementDrawer.exitMinimap = false;

		this.thread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.e("MovementDrawer", "Thread was shut down cleanly");
	}

	/**
	 * restars the surface after an add is displayed or the home button is pressed
	 */
	public void restartSurface() {
		if (this.thread == null)
			return;
		if (this.thread.getState() == Thread.State.TERMINATED){
			this.thread = new MainThread(getHolder(), this);
			MovementDrawer.running = true;
			this.thread.setRunning(true);
			this.thread.start();  // Start a new thread
		} else {
			synchronized (this.thread){
				this.notify(); // notify existing thread
			} 
		}
	}

	public void minimapClicked(Room[][] roomsVisited){
		MovementDrawer.minimapClicked = !MovementDrawer.minimapClicked;
		this.roomsVisited = roomsVisited;
		//If exiting the minimap, redraw room
		if (!MovementDrawer.minimapClicked){
			MovementDrawer.exitMinimap = true;
		}
	}


	public void skipTurnClicked(Room[][] roomsVisited, int startX, int startY, int endX, int endY){

		this.roomsVisited = roomsVisited;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;

		MovementDrawer.skipTurnClicked = true;
	}


	public void levelComplete() {
		this.levelCompleted = true;
	}

	public void gameOverMoving(int directionEnemy){
		this.directionEnemy = directionEnemy;
		this.gameOverMoving = true;
		this.enemyStartSet = false;
	}

	public void gameOverSkipTurn(){
		this.gameOverSkipTurn = true;
	}

	public void gameOverMovingToEnemy(){
		this.gameOverMovingToEnemy = true;
	}

	public void returnStart(int roomDrawable, Bitmap doorOverlayRoom){
		this.returnStart = true;

		//Check if the old room needs to be set
		if (MovementDrawer.roomBackground2 != null && MovementDrawer.doorImageRoom2 != null){

			MovementDrawer.doorImageRoom1 = MovementDrawer.doorImageRoom2;
			MovementDrawer.roomBackground1 = MovementDrawer.roomBackground2;
		}
		//Scale the image
		MovementDrawer.roomBackground2 = TheMaze.getRoomImage(roomDrawable);
		MovementDrawer.doorImageRoom2 = doorOverlayRoom;

		MovementDrawer.running = true;
	}


	public boolean surfaceCreated(){
		return this.surfaceCreated;
	}
}
