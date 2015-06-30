package com.mindblock.mazehunter.maze;

import java.util.ArrayList;
import java.util.List;


public class MazeInfo {

	private Room[][] maze;
	private Coordinate player, enemy;
	private List<Coordinate> treasureList;
	
	public MazeInfo(List<String[]> mazeList){
		
		this.maze = new Room[mazeList.size()][mazeList.get(0).length];
		this.treasureList = new ArrayList<Coordinate>(3);
		
		for (int row = 0; row < mazeList.size(); row++){
			for (int col = 0; col < mazeList.get(row).length; col++){
				
				Room room = new Room(mazeList.get(row)[col]);
				this.maze[row][col] = room;
				
				//Checks
				if (room.isStart())
					this.player = new Coordinate(row, col);
				else if (room.isEnemy())
					this.enemy = new Coordinate(row, col);
				else if (room.isTreasure()){
					this.treasureList.add(new Coordinate(row, col));
				}
			}
		}
	}
	
	
	public Room[][] getMaze(){
		return this.maze;
	}
	
	public Coordinate getPlayerCoordinate(){
		return this.player;
	}
	
	public Coordinate getEnemyCoordinate(){
		return this.enemy;
	}
	
	public void movePlayer(int xChange, int yChange){
		this.player.setX(this.player.getX() + xChange);
		this.player.setY(this.player.getY() + yChange);
	}
	
	public void moveEnemy(int xChange, int yChange){
		this.enemy.setX(this.enemy.getX() + xChange);
		this.enemy.setY(this.enemy.getY() + yChange);
	}
	
	public List<Coordinate> getTreasureList(){
		return this.treasureList;
	}
	
}
