package com.mindblock.mazehunter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		this.setInitialImages();
	}
	
	private void setInitialImages(){
		RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.main_rl_layout);
		rl_main.setBackgroundResource(R.drawable.background_main);
	}

}
