package com.mindblock.mazehunter.maze.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.mindblock.mazehunter.maze.Coordinate;
import com.mindblock.mazehunter.maze.MazeInfo;
import com.mindblock.mazehunter.maze.Room;
import com.mindblock.mazehunter.maze.TheMaze;

public class Enemy {

	public static final int STRATEGY_RANDOM = 0;
	public static final int STRATEGY_PHEROMONES = 1;
	public static final int STRATEGY_HARD = 2;

	private MazeInfo mazeInfo;
	private int strategy;
	private int direction;
	private double PHEROMONE_CORRECT = 0.80;
	private List<Coordinate> playerPath;


	public Enemy(MazeInfo mazeInfo, int strategy){
		this.strategy = strategy;
		this.mazeInfo = mazeInfo;
		this.playerPath = new ArrayList<Coordinate>();
	}


	public void move(){

		//Decide what kind of move to make
		switch(this.strategy){

		case Enemy.STRATEGY_RANDOM: 
			this.randomMove();
			break;
		case Enemy.STRATEGY_PHEROMONES: 
			this.pheromoneMove(false);
			break;
		case Enemy.STRATEGY_HARD:
			this.pheromoneMove(true);
			break;
		}
	}


	private void randomMove(){
		Coordinate enemy = this.mazeInfo.getEnemyCoordinate();
		Room room = this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()];

		this.calculateRandomMove(room, enemy);
	}

	private void calculateRandomMove(Room room, Coordinate enemy){
		List<Coordinate> movementList = new ArrayList<Coordinate>();

		if (room.isDistractPlaced()){
			switch (room.getDistractDirection()){
			case TheMaze.DOWN_DIRECTION:
				if (room.isUp()) movementList.add(new Coordinate(-1, 0));
				if (room.isLeft()) movementList.add(new Coordinate(0, -1));
				if (room.isRight()) movementList.add(new Coordinate(0, 1));
				break;
			case TheMaze.UP_DIRECTION:
				if (room.isDown()) movementList.add(new Coordinate(1, 0));
				if (room.isLeft()) movementList.add(new Coordinate(0, -1));
				if (room.isRight()) movementList.add(new Coordinate(0, 1));
				break;
			case TheMaze.LEFT_DIRECTION:
				if (room.isDown()) movementList.add(new Coordinate(1, 0));
				if (room.isUp()) movementList.add(new Coordinate(-1, 0));
				if (room.isRight()) movementList.add(new Coordinate(0, 1));
				break;
			case TheMaze.RIGHT_DIRECTION:
				if (room.isDown()) movementList.add(new Coordinate(1, 0));
				if (room.isUp()) movementList.add(new Coordinate(-1, 0));
				if (room.isLeft()) movementList.add(new Coordinate(0, -1));
				break;
			}
			room.setDistractPlaced(false, TheMaze.NO_DIRECTION);
		}
		else if (!room.isDistractPlaced() || movementList.isEmpty()){
			if (room.isDown()) movementList.add(new Coordinate(1, 0));
			if (room.isUp()) movementList.add(new Coordinate(-1, 0));
			if (room.isLeft()) movementList.add(new Coordinate(0, -1));
			if (room.isRight()) movementList.add(new Coordinate(0, 1));
		}

		int moveIndex = new Random().nextInt(movementList.size());

		Coordinate toMove = movementList.get(moveIndex);
		this.mazeInfo.moveEnemy(toMove.getX(), toMove.getY());

		//Check direction
		if (toMove.getY() == -1)
			this.direction = TheMaze.LEFT_DIRECTION;
		else if (toMove.getY() == 1)
			this.direction = TheMaze.RIGHT_DIRECTION;
		else if (toMove.getX() == -1)
			this.direction = TheMaze.UP_DIRECTION;
		else
			this.direction = TheMaze.DOWN_DIRECTION;

		Log.e("ENEMY", "Moving enemy with dX: " + toMove.getY() + " and dY: " + toMove.getX());
	}


	private void pheromoneMove(boolean hard){

		if (hard)
			this.PHEROMONE_CORRECT = 1;

		//Check if player isn't standing still, otherwise track movement
		Coordinate player = new Coordinate(this.mazeInfo.getPlayerCoordinate().getX(), this.mazeInfo.getPlayerCoordinate().getY());
		if (!this.playerPath.isEmpty()){
			if (!this.sameCoordinate(player, this.playerPath.get(this.playerPath.size()-1)))
				this.playerPath.add(player);
		}
		else
			this.playerPath.add(player);

		Coordinate enemy = this.mazeInfo.getEnemyCoordinate();
		//If player hasn't visited the current room, move randomly
		if (!this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isVisited()){
			this.randomMove();
		}
		//Otherwise:
		else {
			int indexInPath = this.getIndexInPlayerPath(enemy);
			if (indexInPath == -1 || indexInPath+1 > this.playerPath.size()-1 || new Random().nextDouble() > this.PHEROMONE_CORRECT 
					|| this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isDistractPlaced()){
				this.randomMove();
			}
			else {
				Coordinate toMove = this.playerPath.get(indexInPath+1);
				int dX = toMove.getX() - enemy.getX();
				int dY = toMove.getY() - enemy.getY();
				
				//Check direction
				if (dY == -1)
					this.direction = TheMaze.LEFT_DIRECTION;
				else if (dY == 1)
					this.direction = TheMaze.RIGHT_DIRECTION;
				else if (dX == -1)
					this.direction = TheMaze.UP_DIRECTION;
				else
					this.direction = TheMaze.DOWN_DIRECTION;

				//Check if player hasn't done a jump or return
				if (Math.abs(dX) > 1 || Math.abs(dY) > 1 
						|| (!this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isUp() && this.direction == TheMaze.UP_DIRECTION)
						|| (!this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isDown() && this.direction == TheMaze.DOWN_DIRECTION)
						|| (!this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isLeft() && this.direction == TheMaze.LEFT_DIRECTION)
						|| (!this.mazeInfo.getMaze()[enemy.getX()][enemy.getY()].isRight() && this.direction == TheMaze.RIGHT_DIRECTION)){
					this.randomMove();
					return;
				}
				this.mazeInfo.moveEnemy(dX, dY);
				
				Log.e("ENEMY", "Moving enemy with dX: " + dY + " and dY: " + dX);
			}
		}

	}

	public int getLastDirection(){
		return this.direction;
	}

	private boolean sameCoordinate(Coordinate a, Coordinate b){
		return (a.getX() == b.getX() && a.getY() == b.getY());
	}

	private int getIndexInPlayerPath(Coordinate c){
		for (int index = this.playerPath.size()-1; index >= 0; index--)
			if (this.sameCoordinate(c, this.playerPath.get(index)))
				return index;
		return -1;
	}
}
