package com.sansan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sansan.mobilesafe.service.AutoCleanService;
import com.sansan.mobilesafe.ui.SettingToggleButtonView;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class TaskSettingActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private SettingToggleButtonView tb_show_sys;
	private SharedPreferencesUtils sp;
	private SettingToggleButtonView tb_auto_clean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tb_show_sys = (SettingToggleButtonView) findViewById(R.id.tb_show_sys);
		tb_auto_clean = (SettingToggleButtonView) findViewById(R.id.tb_auto_clean);
		
		tv_title.setText("手机加速设置");
		tb_show_sys.setOnClickListener(this);
		tb_auto_clean.setOnClickListener(this);
		tv_title.setOnClickListener(this);
		
		sp = new SharedPreferencesUtils(this);
		
		boolean showsystem = sp.getBoolean("showsystem");
		if(showsystem){
			//显示系统进程已经开启
			tb_show_sys.setChecked(true);
		}else{
			//关闭系统进程显示
			tb_show_sys.setChecked(false);
		}
		boolean lockclean = sp.getBoolean("lockclean");
		if(lockclean){
			//显示系统进程已经开启
			tb_auto_clean.setChecked(true);
		}else{
			//关闭系统进程显示
			tb_auto_clean.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//通用设置
		case R.id.tb_show_sys:
			//判断是否有选中
			//如果已经显示则关闭
			if(tb_show_sys.isChecked()){
				tb_show_sys.setChecked(false);
				sp.saveBoolean("showsystem", false);
			//不显示的时候调用
			}else{
				tb_show_sys.setChecked(true);
				sp.saveBoolean("showsystem", true);
			}
			break;
		case R.id.tb_auto_clean:
			if(tb_auto_clean.isChecked()){
				tb_auto_clean.setChecked(false);
				sp.saveBoolean("lockclean", false);
			//不显示的时候调用
			}else{
				tb_auto_clean.setChecked(true);
				sp.saveBoolean("lockclean", true);
			}
			lockClean();
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
		
	}

	private void lockClean() {
		//锁屏的广播事件是一个特殊的广播事件，在清单文件配置广播接收者是不会生效的。
		//只能在代码里面注册里面才会生效。
		Intent intent = new Intent(TaskSettingActivity.this,AutoCleanService.class);
		if(tb_auto_clean.isChecked()){
			startService(intent);
		}else{
			stopService(intent);
		}
	}
}
