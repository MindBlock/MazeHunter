package com.mindblock.mazehunter.maze;

public class Room {

	
	private boolean left, right, up, down, start, treasure, enemy, visited, playerHere;
	private String bitRoom;
	
	public Room(String roomInfo){
		char[] bits = roomInfo.toCharArray();
		
		this.left = bits[0] == '1' ? true : false;
		this.right = bits[1] == '1' ? true : false;
		this.up = bits[2] == '1' ? true : false;
		this.down = bits[3] == '1' ? true : false;
		this.start = bits[4] == '1' ? true : false;
		this.treasure = bits[5] == '1' ? true : false;
		this.enemy = bits[6] == '1' ? true : false;
		
		this.bitRoom = roomInfo.substring(0, 4);
		
		if (this.isStart())
			this.setVisited();
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isStart() {
		return start;
	}

	public boolean isTreasure() {
		return treasure;
	}

	public boolean isEnemy() {
		return enemy;
	}
	
	public String getBitRoom(){
		return this.bitRoom;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited() {
		this.visited = true;
	}
	
	public boolean isPlayerHere(){
		return this.playerHere;
	}
	
	public void setPlayerHere(boolean playerHere){
		this.playerHere = playerHere;
	}
}
