package com.sansan.mobilesafe;

import com.sansan.mobilesafe.service.AddressService;
import com.sansan.mobilesafe.service.CallSmsSafeService;
import com.sansan.mobilesafe.ui.SettingToggleButtonView;
import com.sansan.mobilesafe.utils.ServiceUtils;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CallSmsSafeSettingActivity extends Activity implements OnClickListener{

	private TextView tv_title;
	private SharedPreferencesUtils sp;
	private Boolean isOpenCallSafe = false;
	//黑名单拦截设置
	private SettingToggleButtonView tb_call_safe;
	private Intent callSmsSafeIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe_setting);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_title.setText("设置骚扰拦截");
		tv_title.setOnClickListener(this);
		
		tb_call_safe = (SettingToggleButtonView) findViewById(R.id.tb_call_safe);
		tb_call_safe.setOnClickListener(this);
		
		sp = new SharedPreferencesUtils(this);
		isOpenCallSafe  = sp.getBoolean("openCallSafe");
		
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(
				CallSmsSafeSettingActivity.this,
				"com.sansan.mobilesafe.service.CallSmsSafeService");
		if(isOpenCallSafe){
			//监听来电的服务是开启的
			if(!iscallSmsServiceRunning){
				startService(callSmsSafeIntent);
			}
			tb_call_safe.setChecked(true);
		}else{
			tb_call_safe.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_titlebar_left:
			finish();
			break;
		case R.id.tb_call_safe:
			if (tb_call_safe.isChecked()) {
				// 变为非选中状态
				tb_call_safe.setChecked(false);
				stopService(callSmsSafeIntent);
				sp.saveBoolean("openCallSafe", false);
			} else {
				// 选择状态
				tb_call_safe.setChecked(true);
				startService(callSmsSafeIntent);
				sp.saveBoolean("openCallSafe", true);
			}
			break;
		}
	}
}
