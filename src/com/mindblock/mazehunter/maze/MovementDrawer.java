package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

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

	public static Bitmap doorImageRoom1, doorImageRoom2, roomBackground1, roomBackground2, minimapEnter, minimapExit;
	public Bitmap enemyToken, playerToken;
	private Paint p;
	public static int direction;
	private float xPlayer, yPlayer, wIntroMessage, hIntroMessage;
	private MainThread thread;
	public static boolean running = true;
	public static boolean moveingOutOfRoom = true;
	public static boolean minimapClicked = false;
	public static boolean exitMinimap = false;
	private boolean initiation = true;
	private boolean drawingIntroMessage = true;
	private final int INTRO_MESSAGE_DURATION = 150; //ticks
	private int currentIntroMessageTicks = 0;
	private boolean startIntroMessageTicking = false;
	private boolean playerImageUpToDate = false;
	private int mazeSize;
	private Room[][] roomsVisited;
	private PlayerImage pImage;

	private final int MOVE_OUT = 0;
	private final int MOVE_IN = 1;
	private final int MOVE_DONE = -1;

	private final double SPEED = 3.5;

	public MovementDrawer(Context context, int doorOverlayRoom, int direction, int roomDrawable, int mazeSize) {
		super(context);

		this.mazeSize = mazeSize;
		int width = this.getDeviceWidth();
		MovementDrawer.direction = direction;
		//Scale the image
		MovementDrawer.roomBackground1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), roomDrawable), width, width, false);
		MovementDrawer.doorImageRoom1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), doorOverlayRoom), width, width, false);

		//Set room 2 to null
		MovementDrawer.roomBackground2 = null;
		MovementDrawer.doorImageRoom2 = null;

		MovementDrawer.minimapEnter = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.minimap), width/6, width/6, false);
		MovementDrawer.minimapExit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.exit_minimap), width/6, width/6, false);

		int sizePerRoom = (int) ((double) (0.7*this.getDeviceWidth())/this.mazeSize) - 2;
		this.enemyToken = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.enemy_token), sizePerRoom, sizePerRoom, false);
		this.playerToken = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.player_token), sizePerRoom, sizePerRoom, false);
		
		this.xPlayer = width/2;
		this.yPlayer = width/2;
		this.wIntroMessage = 20f;
		this.hIntroMessage = 0.125f*20f;

		setZOrderOnTop(true);
		// Tell the SurfaceHolder ( -> getHolder() ) to receive SurfaceHolder
		// Callback
		getHolder().addCallback(this);
		//        getHolder().setFormat(PixelFormat.TRANSPARENT);

		this.thread = new MainThread(getHolder(), this);

		this.p = new Paint();
	}


	public void drawPlayer(Canvas canvas){
		if (MovementDrawer.running && canvas != null) {

			//Init sprites of player movement
			if (!this.playerImageUpToDate) {
				 this.pImage = new PlayerImage(MovementDrawer.direction, this.getContext());
				 this.playerImageUpToDate = true;
			}
			
			//First check if this is the start phase
			if (this.drawingIntroMessage){
				if (this.initiation){
					this.initiation = false;
					this.drawFirstRoom(canvas);
				}
				this.drawIntroMessage(canvas);
			}
			else {

				
				int moveResult = this.movePlayer();
				if (this.MOVE_OUT == moveResult){
					this.drawFirstRoom(canvas);
				}
				else if (this.MOVE_IN == moveResult){
					this.drawSecondRoom(canvas);
				}

				//If neither moving out or into a room, stop updating and drawing
				else {
					Log.e("MovementDrawer", "Drawing set to false");
					if (MovementDrawer.minimapClicked)
						this.drawMinimap(canvas);
					//If the exit minimap button has been clicked, draw
					if (MovementDrawer.exitMinimap){
						try{
							this.drawSecondRoom(canvas);
						} catch(NullPointerException e){
							this.drawFirstRoom(canvas);
						}
						MovementDrawer.exitMinimap = false;
					}
					this.drawMinimapSmall(canvas, MovementDrawer.minimapClicked);
					MovementDrawer.running = false;
				}
			}
		}

	}


	public void drawIntroMessage(Canvas canvas){
		
		float size = this.getDeviceWidth();
		double finalWidth = 0.8*size;

		if (!this.startIntroMessageTicking){
			double increment = this.SPEED;
			this.wIntroMessage += increment;
			this.hIntroMessage += 0.125*increment;
		}
		else {
			this.currentIntroMessageTicks += 1;
		}

		this.drawFirstRoom(canvas);
		Bitmap introMessage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.intro_message_maze), 
				(int) (this.wIntroMessage), 
				(int) (this.hIntroMessage), false);

		canvas.drawBitmap(introMessage, (size - this.wIntroMessage)/2,  size/3 - hIntroMessage/2, this.p);

		if (!this.startIntroMessageTicking && this.wIntroMessage >= finalWidth){
			this.startIntroMessageTicking = true;
		}

		if (this.currentIntroMessageTicks >= this.INTRO_MESSAGE_DURATION){

			this.drawFirstRoom(canvas);
			this.drawMinimapSmall(canvas, false);

			this.currentIntroMessageTicks = 0;
			this.startIntroMessageTicking = false;
			this.drawingIntroMessage = false;
			MovementDrawer.running = false;
		}
	}

	public void drawFirstRoom(Canvas canvas){

		canvas.drawBitmap(MovementDrawer.roomBackground1, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom1, 0, 0, this.p);

		//TODO: Replace with actual player
		canvas.drawBitmap(this.pImage.getNextImage(), this.xPlayer-20, this.yPlayer-40, this.p);
	}

	public void drawSecondRoom(Canvas canvas){

		canvas.drawBitmap(MovementDrawer.roomBackground2, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom2, 0, 0, this.p);
		
		//TODO: Replace with actual player
		canvas.drawBitmap(this.pImage.getNextImage(), this.xPlayer-20, this.yPlayer-40, this.p);
	}

	public void drawMinimapSmall(Canvas canvas, boolean minimapEnabled){
		int size = this.getDeviceWidth();
		if (minimapEnabled)
			canvas.drawBitmap(MovementDrawer.minimapExit, 5*size/6, 5*size/6, this.p);
		else
			canvas.drawBitmap(MovementDrawer.minimapEnter, 5*size/6, 5*size/6, this.p);
	}

	private void drawMinimap(Canvas canvas){
		//Fill 70% with minimap
		int size = (int) (0.7*this.getDeviceWidth());
		int offSet = (int) (0.15*this.getDeviceWidth());
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
				
				if (room.isEnemy()){
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
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Right
		if (MovementDrawer.direction == TheMaze.RIGHT_DIRECTION){
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
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Left
		if (MovementDrawer.direction == TheMaze.LEFT_DIRECTION){
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
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//Down
		if (MovementDrawer.direction == TheMaze.DOWN_DIRECTION){
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
				this.yPlayer = size/2;
				this.xPlayer = size/2;
				//And return DONE
				return this.MOVE_DONE;
			}
		}

		//If no direction is chose, don't do anything
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
		MovementDrawer.running = true;
		this.thread.setRunning(true);
		this.thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.shutdownThread();
	}

	public void updateRoom(int roomDrawable, int direction, int doorOverlayRoom){
		int width = this.getDeviceWidth();
		MovementDrawer.direction = direction;
		//Check if the old room needs to be set
		if (MovementDrawer.roomBackground2 != null && MovementDrawer.doorImageRoom2 != null){

			MovementDrawer.doorImageRoom1 = MovementDrawer.doorImageRoom2;
			MovementDrawer.roomBackground1 = MovementDrawer.roomBackground2;
		}
		//Scale the image
		MovementDrawer.roomBackground2 = TheMaze.getRoomImage(roomDrawable);
		MovementDrawer.doorImageRoom2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), doorOverlayRoom), width, width, false);

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

	public void minimapClicked(Room[][] roomsVisited){
		MovementDrawer.minimapClicked = !MovementDrawer.minimapClicked;
		this.roomsVisited = roomsVisited;
		//If exiting the minimap, redraw room
		if (!MovementDrawer.minimapClicked){
			MovementDrawer.exitMinimap = true;
		}
	}

}
