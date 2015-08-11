package com.mindblock.mazehunter.settings;
import com.mindblock.mazehunter.R;
import com.mindblock.mazehunter.save.PlayerInformation;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class SettingsLayout extends Activity{

	PlayerInformation pi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_layout);

		this.addSettingsLayout();
	}
	
	
	private void addSettingsLayout(){
		RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_settings_layout);
		rlLayout.setBackgroundResource(R.drawable.background_settings);
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		
		linearLayout.addView(this.setSeekBarLabel());
		linearLayout.addView(this.setSeekBar());
		
		rlLayout.addView(linearLayout);
	}
	
	
	private ImageView setSeekBarLabel(){
		ImageView iv = new ImageView(this);
		Bitmap label = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_label), 
				(int) (0.66*this.getDeviceWidth()), (int) (0.1*this.getDeviceWidth()), true);
		
		iv.setImageBitmap(label);
		
		return iv;
	}
	
	private SeekBar setSeekBar(){
		
		this.pi = new PlayerInformation(this);
		
		SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(3);
//      seekBar.setIndeterminate(true);

        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

        thumb.setIntrinsicHeight(80);
        thumb.setIntrinsicWidth(30);
        seekBar.setThumb(thumb);
        seekBar.setProgress(this.pi.getSpeedMultiplier());
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setBackgroundColor(Color.argb(150, 0, 150, 220));

        LayoutParams lp = new LayoutParams((int) (0.66*this.getDeviceWidth()), (int) (0.1*this.getDeviceWidth()));
        seekBar.setLayoutParams(lp);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar arg0) {
                
            }

            public void onStartTrackingTouch(SeekBar arg0) {
                
            }

            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                pi.setSpeedMultiplier(progress);
            }
        });
        
        return seekBar;
	}
	
	
	private double getDeviceWidth(){
		return this.getResources().getDisplayMetrics().widthPixels;
	}
}
