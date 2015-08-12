package com.mindblock.mazehunter.main;

import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.info.InfoLayout;
import com.mindblock.mazehunter.maze.TheMazeDifficulties;
import com.mindblock.mazehunter.save.PlayerInformation;
import com.mindblock.mazehunter.settings.SettingsLayout;
import com.mindblock.mazehunter.skills.SkillsLayout;
import com.mindblock.mazehunter.text.FitTextSize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {


	private static final int NUMBER_OF_BUTTONS = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		this.setInitialImages();
	}

	private void setInitialImages(){
		RelativeLayout rlMain = (RelativeLayout) findViewById(R.id.main_rl_layout);
		rlMain.setBackgroundResource(R.drawable.background_main);

		rlMain.addView(this.setMainLayouts());
		rlMain.addView(this.setInformationLayout());
	}

	private LinearLayout setMainLayouts(){

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);

		//Add image buttons
		linearLayout.addView(this.getImageButton(SpecificButton.MAZE));
		linearLayout.addView(this.getImageButton(SpecificButton.SKILLS));
		linearLayout.addView(this.getImageButton(SpecificButton.SETTINGS));
		linearLayout.addView(this.getImageButton(SpecificButton.INFO));

		return linearLayout;
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
		int nr_stars = new PlayerInformation(this).getTotalStars();
		tv.setText(Integer.toString(nr_stars));
		FitTextSize.correctHeight(tv, maxHeight);
		
		linearLayout.addView(stars);
		linearLayout.addView(tv);

		return linearLayout;
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

		case MAZE: 
			this.setButtonListenerMaze(ib);
			break;
		case SKILLS: 
			this.setButtonListenerSkills(ib);
			break;
		case INFO: 
			this.setButtonListenerInfo(ib);
			break;
		case SETTINGS:
			this.setButtonListenerSettings(ib);
			break;
		}

		iButtonLayout.addView(ib);

		return iButtonLayout;
	}


	private void setButtonListenerMaze(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TheMazeDifficulties.class);
				//Add extras?
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void setButtonListenerSkills(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SkillsLayout.class);
				//Add extras?
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void setButtonListenerInfo(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, InfoLayout.class);
				//Add extras?
				MainActivity.this.startActivity(intent);
			}
		});
	}
	
	private void setButtonListenerSettings(ImageButton ib){
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingsLayout.class);
				//Add extras?
				MainActivity.this.startActivity(intent);
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
		MAZE(R.drawable.button_the_maze),
		SKILLS(R.drawable.button_skills),
		SETTINGS(R.drawable.button_settings),
		INFO(R.drawable.button_info);

		private int ID;

		private SpecificButton(int ID){
			this.ID = ID;
		}
	}

	public void onBackPressed(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
