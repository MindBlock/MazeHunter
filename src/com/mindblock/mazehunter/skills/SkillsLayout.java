package com.mindblock.mazehunter.skills;

import com.mindblock.mazehunter.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

public class SkillsLayout extends Activity{

	
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
		rlSkillsLayout.setBackgroundResource(R.drawable.background_main_clean);
	}
}
