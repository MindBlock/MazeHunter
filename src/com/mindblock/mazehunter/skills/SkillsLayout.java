package com.mindblock.mazehunter.skills;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.save.PlayerInformation;
import com.mindblock.mazehunter.text.FitTextSize;
import com.mindblock.mazehunter.useables.UseableItems;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SkillsLayout extends Activity{

	private static final int NUMBER_OF_BUTTONS = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.skills_layout);

		this.addShopLayout();
	}


	private void addShopLayout(){
		RelativeLayout rlSkillsLayout = (RelativeLayout) findViewById(R.id.rl_skills_layout);
		rlSkillsLayout.setBackgroundResource(R.drawable.background_main_skills);

		rlSkillsLayout.addView(this.setSkillButtons());
		rlSkillsLayout.addView(this.setInfoLayout());
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
		linearLayout.addView(this.getSkillButton(SkillButton.INVINCIBLE));//invincible
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

		UseableItems items = new UseableItems(this);
		if (items.getAllUseableItems().get(sb.getName()))
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

		//TODO: switch for button click listener

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
		
		TextView tv = new TextView(this);
		tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf"));
		int nr_stars = new PlayerInformation(this).getUnusedStars();
		tv.setText("Remaining stars : " + Integer.toString(nr_stars));
		FitTextSize.correctSize(tv, maxWidth, maxHeight);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		//Reset button
		ImageButton ib = new ImageButton(this);
		ib.setImageResource(R.drawable.button_reset_skills);
		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		ib.setMaxHeight(maxHeight);
		ib.setMaxWidth((int) ((217/88)*maxHeight)); //dimension of image

		//TODO: add listener
		
		linearLayout.addView(tv);
		linearLayout.addView(ib);

		return linearLayout;
	}



	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}


	public enum SkillButton {
		DISTRACT(R.drawable.distract_locked, R.drawable.distract_unlocked, UseableItems.DISTRACT),
		JUMP(R.drawable.jump_locked, R.drawable.jump_unlocked, UseableItems.JUMP),
		RETURN(R.drawable.return_locked, R.drawable.return_unlocked, UseableItems.RETURN),
		REVEAL(R.drawable.reveal_locked, R.drawable.reveal_unlocked, UseableItems.REVEAL),
		INVINCIBLE(R.drawable.invincible_locked, R.drawable.invincible_unlocked, UseableItems.INVINCIBLE),
		FREEZE(R.drawable.freeze_locked, R.drawable.freeze_unlocked, UseableItems.FREEZE);

		private int LockedID, UnlockedID;
		private String name;

		private SkillButton(int LockedID, int UnlockedID, String name){
			this.LockedID = LockedID;
			this.UnlockedID = UnlockedID;
			this.name = name;
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
	}
}
