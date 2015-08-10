package com.mindblock.mazehunter.maze;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.main.MainActivity;
import com.mindblock.mazehunter.main.MainActivity.SpecificButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
	}

	private LinearLayout getImageButton(SpecificButton sb){
		ImageButton ib = new ImageButton(this);

		LinearLayout iButtonLayout = new LinearLayout(this);
		iButtonLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iButtonLayout.setOrientation(LinearLayout.HORIZONTAL);

		ib.setImageResource(sb.ID);
		ib.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
		ib.setBackgroundColor(Color.TRANSPARENT);

		//Calculate height fitting:
		ib.setAdjustViewBounds(true);
		int maxHeight = (int) ((2*this.getDeviceHeight()/3)/NUMBER_OF_BUTTONS);
		ib.setMaxHeight(maxHeight);
		ib.setMaxWidth((int) (0.65*this.getDeviceWidth()));

		//Check which button we need to add functionality for
		switch (sb) {

		case EASY: 
			this.setButtonListenerEasy(ib);
			break;
//		TODO: case NORMAL: 
//			this.setButtonListenerNormal(ib);
//			break;
//		TODO: case HARD: 
//			this.setButtonListenerHard(ib);
//			break;
		}

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

	private double getDeviceHeight(){
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}

	public enum SpecificButton {
		EASY(R.drawable.button_easy),
		NORMAL(R.drawable.button_normal),
		HARD(R.drawable.button_hard);

		private int ID;

		private SpecificButton(int ID){
			this.ID = ID;
		}
	}

	public void onBackPressed(){
		Intent i = new Intent(TheMazeDifficulties.this,MainActivity.class);    
		startActivity(i);  
	}
}
