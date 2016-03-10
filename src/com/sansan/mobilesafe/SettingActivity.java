package com.sansan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {
	
	private TextView tv_seting;
	private TextView tv_title;
	private TextView tv_show_adress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setings);
		
		tv_seting = (TextView) findViewById(R.id.tv_seting);
		tv_show_adress = (TextView) findViewById(R.id.tv_show_adress);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_title.setText("设置中心");
		tv_title.setOnClickListener(this);
		tv_seting.setOnClickListener(this);
		tv_show_adress.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//通用设置
		case R.id.tv_seting:
			Intent intent = new Intent(SettingActivity.this,GeneralSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_show_adress:
			startActivity(new Intent(SettingActivity.this,ShowNumberAddressActivity.class));
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
	}
}
