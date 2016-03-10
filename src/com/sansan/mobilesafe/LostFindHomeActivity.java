package com.sansan.mobilesafe;

import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LostFindHomeActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private ImageView iv_title_right;
	private Button bt_off_find;
	private SharedPreferencesUtils sp;
	private TextView iv_lost;
	private ImageView iv_pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find_home);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		iv_lost = (TextView) findViewById(R.id.iv_lost);
		iv_title_right = (ImageView) findViewById(R.id.iv_titlebar_right);
		iv_pro = (ImageView) findViewById(R.id.iv_pro);
		bt_off_find = (Button) findViewById(R.id.bt_off_find);
		tv_title.setText("手机防盗");
		iv_title_right.setVisibility(View.VISIBLE);
		iv_title_right.setOnClickListener(this);
		tv_title.setOnClickListener(this);
		bt_off_find.setOnClickListener(this);
		bt_off_find.setText("关闭手机防盗");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 通用设置
		case R.id.iv_titlebar_right:
			//进入防盗设置
			Intent intent = new Intent(LostFindHomeActivity.this,LostFindSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_off_find:
			offFind();
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
		
	}

	private void offFind() {
		sp = new SharedPreferencesUtils(this);
		String password = sp.getString("password");
		if(!TextUtils.isEmpty(password)){
			sp.saveString("password", "");
			sp.saveString("safenumber", "");
			Toast.makeText(this, "手机防盗已关闭", Toast.LENGTH_SHORT).show();
			iv_pro.setBackgroundResource(R.drawable.protection_closed);
			iv_lost.setText("亲爱的主人，您已经关闭手机防盗，如果丢失，小安安就不能帮您定位找回！请点击开启~");
			bt_off_find.setText("开启手机防盗");
			bt_off_find.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LostFindHomeActivity.this,LostFindActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
		
	}
}
