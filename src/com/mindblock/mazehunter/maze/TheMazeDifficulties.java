package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.main.MainActivity;
import com.mindblock.mazehunter.save.PlayerInformation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TheMazeDifficulties extends Activity{

	private static final int NUMBER_OF_BUTTONS = 3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.difficulties);

		this.addDifficultiesLayout();
	}


	private void addDifficultiesLayout(){
		RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_difficulties_layout);
		rlLayout.setBackgroundResource(R.drawable.background_difficulties);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);

		//Add image buttons
		linearLayout.addView(this.getImageButton(SpecificButton.EASY));
		linearLayout.addView(this.getImageButton(SpecificButton.NORMAL));
		linearLayout.addView(this.getImageButton(SpecificButton.HARD));

		rlLayout.addView(linearLayout);
		rlLayout.addView(this.setInformationLayout());
	}

	private LinearLayout setInformationLayout(){

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.BOTTOM);

		ImageView stars = new ImageView(this);
		stars.setImageResource(R.drawable.star_counter);
		stars.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		stars.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		stars.setAdjustViewBounds(true);
		int maxHeight = (int) (0.1*this.getDeviceHeight());
		stars.setMaxHeight(maxHeight);
		stars.setMaxWidth((int) (maxHeight*(350/200)));

		//Textview containing nr of stars
		TextView tv = new TextView(this);
		tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bitwise.ttf"));
		tv.setTextSize((int) ((double) maxHeight/3));
		int nr_stars = new PlayerInformation(this).getTotalStars();
		tv.setText(Integer.toString(nr_stars));

		linearLayout.addView(stars);
		linearLayout.addView(tv);

		return linearLayout;
	}

	private LinearLayout getImageButton(SpecificButton sb){
		ImageButton ib = new ImageButton(this);
		int totalStars = new PlayerInformation(this).getTotalStars();

		LinearLayout iButtonLayout = new LinearLayout(this);
		iButtonLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iButtonLayout.setOrientation(LinearLayout.HORIZONTAL);

		//Check which button we need to add functionality for
		switch (sb) {

		case EASY: 
			ib.setImageResource(sb.ID);
			this.setButtonListenerEasy(ib);
			break;
		case NORMAL: 
			if (totalStars >= 200){
				ib.setImageResource(sb.ID);
				this.setButtonListenerNormal(ib);
			}
			else
				ib.setImageResource(sb.lockedID);
			break;
		case HARD: 
			if (totalStars >= 400){
				ib.setImageResource(sb.ID);
				//TODO: this.setButtonListenerHard(ib);
			}
			else
				ib.setImageResource(sb.lockedID);
			break;
		default:
			ib.setImageResource(sb.ID);
			this.setButtonListenerEasy(ib);
			break;
		}

		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		ib.setMaxHeight(maxHeight);
		ib.setMaxWidth((int) (0.65*this.getDeviceWidth()));


		iButtonLayout.addView(ib);

		return iButtonLayout;
	}


	private void setButtonListenerEasy(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TheMazeDifficulties.this, TheMazeEasy.class);
				//Add extras?
				TheMazeDifficulties.this.startActivity(intent);
			}
		});
	}

	private void setButtonListenerNormal(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TheMazeDifficulties.this, TheMazeNormal.class);
				//Add extras?
				TheMazeDifficulties.this.startActivity(intent);
			}
		});
	}

	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}

	public enum SpecificButton {
		EASY(R.drawable.button_easy, -1),
		NORMAL(R.drawable.button_normal, R.drawable.button_normal_locked),
		HARD(R.drawable.button_hard, R.drawable.button_hard_locked);

		private int ID;
		private int lockedID;

		private SpecificButton(int ID, int lockedID){
			this.ID = ID;
			this.lockedID = lockedID;
		}
	}

	public void onBackPressed(){
		Intent i = new Intent(TheMazeDifficulties.this,MainActivity.class);    
		startActivity(i);  
	}
}
