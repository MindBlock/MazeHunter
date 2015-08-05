package com.mindblock.mazehunter.maze.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.mindblock.mazehunter.maze.Coordinate;
import com.mindblock.mazehunter.maze.MazeInfo;
import com.mindblock.mazehunter.maze.Room;

public class Enemy {

	public static final int STRATEGY_RANDOM = 0;
	
	private MazeInfo mazeInfo;
	private int strategy;
	
	
	public Enemy(MazeInfo mazeInfo, int strategy){
		this.strategy = strategy;
		this.mazeInfo = mazeInfo;
	}
	
	
	public void move(){
		
		//Decide what kind of move to make
		switch(this.strategy){
		
		case Enemy.STRATEGY_RANDOM: this.randomMove();
		
		}
	}
	
	
	private void randomMove(){
		Coordinate enemy = this.mazeInfo.getEnemyCoordinate();
		Room room = this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()];
		
		this.calculateRandomMove(room, enemy);
	}
	
	private void calculateRandomMove(Room room, Coordinate enemy){
		List<Coordinate> movementList = new ArrayList<Coordinate>();
		
		if (room.isDown()) movementList.add(new Coordinate(1, 0));
		if (room.isUp()) movementList.add(new Coordinate(-1, 0));
		if (room.isLeft()) movementList.add(new Coordinate(0, -1));
		if (room.isRight()) movementList.add(new Coordinate(0, 1));
		
		int moveIndex = new Random().nextInt(movementList.size());
		
		Coordinate toMove = movementList.get(moveIndex);
		this.mazeInfo.moveEnemy(toMove.getX(), toMove.getY());
		
		Log.e("ENEMY", "Moving enemy with dX: " + toMove.getY() + " and dY: " + toMove.getX());
	}
}
