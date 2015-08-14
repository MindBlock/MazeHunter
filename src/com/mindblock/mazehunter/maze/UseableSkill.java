package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;

public enum UseableSkill {

	DISTRACT(R.drawable.in_game_distract_locked, R.drawable.in_game_distract_unlocked, R.drawable.in_game_distract_selected),
	JUMP(R.drawable.in_game_jump_locked, R.drawable.in_game_jump_unlocked, R.drawable.in_game_jump_selected),
	RETURN(R.drawable.in_game_return_locked, R.drawable.in_game_return_unlocked, R.drawable.in_game_return_selected),
	REVEAL(R.drawable.in_game_reveal_locked, R.drawable.in_game_reveal_unlocked, R.drawable.in_game_reveal_selected),
	TREASURE(R.drawable.in_game_treasure_locked, R.drawable.in_game_treasure_unlocked, R.drawable.in_game_treasure_selected),
	FREEZE(R.drawable.in_game_freeze_locked, R.drawable.in_game_freeze_unlocked, R.drawable.in_game_freeze_selected);
	
	private int lockedID, unlockedID, selectedID;
	
	private UseableSkill(int lockedID, int unlockedID, int selectedID){
		this.lockedID = lockedID;
		this.unlockedID = unlockedID;
		this.selectedID = selectedID;
	}

	public int getLockedID() {
		return lockedID;
	}

	public int getUnlockedID() {
		return unlockedID;
	}

	public int getSelectedID() {
		return selectedID;
	}

}
