package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

public class DoorOverlay {

	
	private static int OVERLAY1111 = R.drawable.maze_room_overlay_1111;
	private static int OVERLAY1110 = R.drawable.maze_room_overlay_1110;
	private static int OVERLAY1101 = R.drawable.maze_room_overlay_1101;
	private static int OVERLAY1100 = R.drawable.maze_room_overlay_1100;
	private static int OVERLAY1011 = R.drawable.maze_room_overlay_1011;
	private static int OVERLAY1010 = R.drawable.maze_room_overlay_1010;
	private static int OVERLAY1001 = R.drawable.maze_room_overlay_1001;
	private static int OVERLAY1000 = R.drawable.maze_room_overlay_1000;
	private static int OVERLAY0111 = R.drawable.maze_room_overlay_0111;
	private static int OVERLAY0110 = R.drawable.maze_room_overlay_0110;
	private static int OVERLAY0101 = R.drawable.maze_room_overlay_0101;
	private static int OVERLAY0100 = R.drawable.maze_room_overlay_0100;
	private static int OVERLAY0011 = R.drawable.maze_room_overlay_0011;
	private static int OVERLAY0001 = R.drawable.maze_room_overlay_0001;
	private static int OVERLAY0010 = R.drawable.maze_room_overlay_0010;
	private static int ERROR = -1;
	
	
	public static int getDoorOverlayDrawable(String doorConfig){
		
		int doorCode = Integer.parseInt("1" + doorConfig);
		switch (doorCode){
		
		case 11111: return OVERLAY1111;
		case 11110: return OVERLAY1110;
		case 11101: return OVERLAY1101;
		case 11100: return OVERLAY1100;
		case 11011: return OVERLAY1011;
		case 11010: return OVERLAY1010;
		case 11001: return OVERLAY1001;
		case 11000: return OVERLAY1000;
		case 10111: return OVERLAY0111;
		case 10110: return OVERLAY0110;
		case 10101: return OVERLAY0101;
		case 10100: return OVERLAY0100;
		case 10011: return OVERLAY0011;
		case 10001: return OVERLAY0001;
		case 10010: return OVERLAY0010;
		default: return ERROR;
		}
	}
}
