package com.mindblock.mazehunter.shop;
import com.mindblock.mazehunter.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;


public class ShopLayout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_layout);

		this.addShopLayout();
	}
	
	
	private void addShopLayout(){
		RelativeLayout rlShopLayout = (RelativeLayout) findViewById(R.id.rl_shop_layout);
		rlShopLayout.setBackgroundResource(R.drawable.background_main_clean);
	}
}
