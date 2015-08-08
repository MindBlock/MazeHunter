package com.mindblock.mazehunter.maze;

import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private SurfaceHolder surfaceHolder;
	private MovementDrawer md;
	private boolean running;

	public MainThread(SurfaceHolder surfaceHolder, MovementDrawer md) {
		this.surfaceHolder = surfaceHolder;
		this.md = md;
		this.running = false;
	}

	public void run() {
		Log.e("MainThread", "Starting game loop");
		Canvas canvas = null;
		while (running){
			if (md.isDrawing()){
				try {
					canvas = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						canvas.drawColor(0, Mode.CLEAR);
						md.drawPlayer(canvas);
					}
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}


	public void setRunning(boolean running){
		this.running = running;
	}

}