package com.mindblock.mazehunter.skills;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.save.PlayerInformation;
import com.mindblock.mazehunter.text.FitTextSize;
import com.mindblock.mazehunter.useables.UseableItems;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SkillsLayout extends Activity{

	private static final int NUMBER_OF_BUTTONS = 8;
	private UseableItems items;
	private PlayerInformation player;
	private TextView remaining;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.skills_layout);

		this.items = new UseableItems(this);
		this.player = new PlayerInformation(this);
		
		this.addShopLayout();
	}


	private void addShopLayout(){
		RelativeLayout rlSkillsLayout = (RelativeLayout) findViewById(R.id.rl_skills_layout);
		rlSkillsLayout.setBackgroundResource(R.drawable.background_main_skills);

		rlSkillsLayout.addView(this.setSkillButtons(), 0);
		rlSkillsLayout.addView(this.setInfoLayout(), 1);
	}

	private LinearLayout setSkillButtons(){
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);

		//Add buttons
		linearLayout.addView(this.getSkillButton(SkillButton.DISTRACT));//distract
		linearLayout.addView(this.getSkillButton(SkillButton.JUMP));//jump
		linearLayout.addView(this.getSkillButton(SkillButton.RETURN));//return
		linearLayout.addView(this.getSkillButton(SkillButton.REVEAL));//reveal
		linearLayout.addView(this.getSkillButton(SkillButton.TREASURE));//invincible
		linearLayout.addView(this.getSkillButton(SkillButton.FREEZE));//freeze

		return linearLayout;
	}

	private LinearLayout setInfoLayout(){
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.BOTTOM);

		linearLayout.addView(this.getInfo());

		return linearLayout;
	}

	private LinearLayout getSkillButton(SkillButton sb){

		ImageButton ib = new ImageButton(this);

		LinearLayout iButtonLayout = new LinearLayout(this);
		iButtonLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iButtonLayout.setOrientation(LinearLayout.HORIZONTAL);

		if (this.items.getAllUseableItems().get(sb.getName()))
			ib.setImageResource(sb.UnlockedID);
		else
			ib.setImageResource(sb.LockedID);

		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		ib.setMaxHeight(maxHeight);
		ib.setMaxWidth((int) (0.9*this.getDeviceWidth()));
		ib.setPadding(0, 5, 0, 5);

		switch(sb){
		case DISTRACT: ib.setOnClickListener(new distractListener(sb));
			break;
		case JUMP: ib.setOnClickListener(new jumpListener(sb));
			break;
		case RETURN: ib.setOnClickListener(new returnListener(sb));
			break;
		case REVEAL: ib.setOnClickListener(new revealListener(sb));
			break;
		case TREASURE: ib.setOnClickListener(new treasureListener(sb));
			break;
		case FREEZE: ib.setOnClickListener(new freezeListener(sb));
			break;
		}

		iButtonLayout.addView(ib);

		return iButtonLayout;
	}


	private LinearLayout getInfo(){

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);

		//Textview containing nr of stars remaining
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		int maxWidth = (int) (0.5*this.getDeviceWidth());

		this.remaining = new TextView(this);
		this.remaining.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf"));
		int nr_stars = this.player.getUnusedStars();
		this.remaining.setText("Remaining stars : " + Integer.toString(nr_stars));
		FitTextSize.correctSize(this.remaining, maxWidth, maxHeight);
		this.remaining.setGravity(Gravity.CENTER_HORIZONTAL);

		//Reset button
		ImageButton ib = new ImageButton(this);
		ib.setImageResource(R.drawable.button_reset_skills);
		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		ib.setMaxHeight(maxHeight);
		ib.setMaxWidth((int) ((217/88)*maxHeight)); //dimension of image

		ib.setOnClickListener(new resetListener());

		linearLayout.addView(this.remaining);
		linearLayout.addView(ib);

		return linearLayout;
	}


	public class resetListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			items.reset();
			player.setUnusedStars(player.getTotalStars());
			redrawButtons();
			Log.e("SKILLS", "Reset skills");
		}
	}
	
	public class distractListener implements OnClickListener{

		private SkillButton sb;
		
		public distractListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.DISTRACT) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}
	
	public class jumpListener implements OnClickListener{

		private SkillButton sb;
		
		public jumpListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.JUMP) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}
	
	public class returnListener implements OnClickListener{

		private SkillButton sb;
		
		public returnListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.RETURN) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}
	
	public class revealListener implements OnClickListener{

		private SkillButton sb;
		
		public revealListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.REVEAL) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}
	
	public class treasureListener implements OnClickListener{

		private SkillButton sb;
		
		public treasureListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.TREASURE) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}
	
	public class freezeListener implements OnClickListener{

		private SkillButton sb;
		
		public freezeListener(SkillButton sb) {
			this.sb = sb;
		}
		
		@Override
		public void onClick(View v) {
			if (!items.getAllUseableItems().get(UseableItems.FREEZE) && player.getUnusedStars() >= sb.getCost()){
				player.setUnusedStars(player.getTotalStars() - sb.getCost());
				items.unlockUseableItem(sb.getName());
				
				redrawButtons();
			}
		}
	}

	protected void redrawButtons(){

		RelativeLayout rlSkillsLayout = (RelativeLayout) findViewById(R.id.rl_skills_layout);
		rlSkillsLayout.removeViewAt(0);
		rlSkillsLayout.addView(this.setSkillButtons(), 0);
		
		int nr_stars = player.getUnusedStars();
		remaining.setText("Remaining stars : " + Integer.toString(nr_stars));
	}


	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}


	public enum SkillButton {
		DISTRACT(R.drawable.distract_locked, R.drawable.distract_unlocked, UseableItems.DISTRACT, 100),
		JUMP(R.drawable.jump_locked, R.drawable.jump_unlocked, UseableItems.JUMP, 150),
		RETURN(R.drawable.return_locked, R.drawable.return_unlocked, UseableItems.RETURN, 200),
		REVEAL(R.drawable.reveal_locked, R.drawable.reveal_unlocked, UseableItems.REVEAL, 100),
		TREASURE(R.drawable.treasure_locked, R.drawable.treasure_unlocked, UseableItems.TREASURE, 150),
		FREEZE(R.drawable.freeze_locked, R.drawable.freeze_unlocked, UseableItems.FREEZE, 150);

		private int LockedID, UnlockedID, cost;
		private String name;

		private SkillButton(int LockedID, int UnlockedID, String name, int cost){
			this.LockedID = LockedID;
			this.UnlockedID = UnlockedID;
			this.name = name;
			this.cost = cost;
		}

		public int getLockedID(){
			return this.LockedID;
		}

		public int getUnlockedID(){
			return this.UnlockedID;
		}

		public String getName(){
			return this.name;
		}
		
		public int getCost(){
			return this.cost;
		}
	}
	
}
