package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovementDrawer extends SurfaceView implements SurfaceHolder.Callback{

	public static Bitmap doorImageRoom1, doorImageRoom2, roomBackground1, roomBackground2, minimapEnter, minimapExit;
	private Paint p;
	public static int direction;
	private float xPlayer, yPlayer;
	private MainThread thread;
	public static boolean running = true;
	public static boolean moveingOutOfRoom = true;
	public static boolean minimapClicked = false;
	private boolean initiation = true;

	private final int MOVE_OUT = 0;
	private final int MOVE_IN = 1;
	private final int MOVE_DONE = -1;

	private final double SPEED = 2.7;

	public MovementDrawer(Context context, int doorOverlayRoom, int direction, int roomDrawable) {
		super(context);

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

		this.xPlayer = width/2;
		this.yPlayer = width/2;

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

			//First check if this is the start phase
			if (this.initiation){

				this.initiation = false;
				this.drawFirstRoom(canvas);
				this.drawMinimapSmall(canvas);
				MovementDrawer.running = false;
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
					this.drawMinimapSmall(canvas);
					MovementDrawer.running = false;
				}
			}
		}

	}


	public void drawFirstRoom(Canvas canvas){

		canvas.drawBitmap(MovementDrawer.roomBackground1, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom1, 0, 0, this.p);

		canvas.drawCircle(this.xPlayer, this.yPlayer, 15, this.p);
	}

	public void drawSecondRoom(Canvas canvas){

		canvas.drawBitmap(MovementDrawer.roomBackground2, 0, 0, this.p);
		canvas.drawBitmap(MovementDrawer.doorImageRoom2, 0, 0, this.p);
		canvas.drawCircle(this.xPlayer, this.yPlayer, 15, this.p);
	}

	public void drawMinimapSmall(Canvas canvas){
		int size = this.getDeviceWidth();
		canvas.drawBitmap(MovementDrawer.minimapEnter, 5*size/6, 5*size/6, this.p);
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
		MovementDrawer.roomBackground2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), roomDrawable), width, width, false);
		MovementDrawer.doorImageRoom2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), doorOverlayRoom), width, width, false);

		MovementDrawer.moveingOutOfRoom = true;
		MovementDrawer.running = true;
	}

	private void shutdownThread(){
		Log.e("MovementDrawer", "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
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

	public void minimapClicked(){
		MovementDrawer.minimapClicked = !MovementDrawer.minimapClicked;

	}

}
