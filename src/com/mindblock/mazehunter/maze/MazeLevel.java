package com.mindblock.mazehunter.maze;

public class MazeLevel {

	private int index;
	private String completion;
	
	public MazeLevel(int index, String completion){
		this.index = index;
		this.completion = completion;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public String getCompletion(){
		return this.completion;
	}
	
	public void setCompletion(String completion){
		this.completion = completion;
	}
}
