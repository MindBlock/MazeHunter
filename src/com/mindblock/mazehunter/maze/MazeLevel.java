package com.mindblock.mazehunter.maze;

public class MazeLevel {

	private int index;
	private Completion c;
	
	public MazeLevel(int index, Completion c){
		this.index = index;
		this.c = c;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public Completion getCompletion(){
		return this.c;
	}
	
	public void setCompletion(Completion c){
		this.c = c;
	}
}
