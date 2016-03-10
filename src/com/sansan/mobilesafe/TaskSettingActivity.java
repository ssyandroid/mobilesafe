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
		
		tv_title.setText("�ֻ���������");
		tb_show_sys.setOnClickListener(this);
		tb_auto_clean.setOnClickListener(this);
		tv_title.setOnClickListener(this);
		
		sp = new SharedPreferencesUtils(this);
		
		boolean showsystem = sp.getBoolean("showsystem");
		if(showsystem){
			//��ʾϵͳ�����Ѿ�����
			tb_show_sys.setChecked(true);
		}else{
			//�ر�ϵͳ������ʾ
			tb_show_sys.setChecked(false);
		}
		boolean lockclean = sp.getBoolean("lockclean");
		if(lockclean){
			//��ʾϵͳ�����Ѿ�����
			tb_auto_clean.setChecked(true);
		}else{
			//�ر�ϵͳ������ʾ
			tb_auto_clean.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//ͨ������
		case R.id.tb_show_sys:
			//�ж��Ƿ���ѡ��
			//����Ѿ���ʾ��ر�
			if(tb_show_sys.isChecked()){
				tb_show_sys.setChecked(false);
				sp.saveBoolean("showsystem", false);
			//����ʾ��ʱ�����
			}else{
				tb_show_sys.setChecked(true);
				sp.saveBoolean("showsystem", true);
			}
			break;
		case R.id.tb_auto_clean:
			if(tb_auto_clean.isChecked()){
				tb_auto_clean.setChecked(false);
				sp.saveBoolean("lockclean", false);
			//����ʾ��ʱ�����
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
		//�����Ĺ㲥�¼���һ������Ĺ㲥�¼������嵥�ļ����ù㲥�������ǲ�����Ч�ġ�
		//ֻ���ڴ�������ע������Ż���Ч��
		Intent intent = new Intent(TaskSettingActivity.this,AutoCleanService.class);
		if(tb_auto_clean.isChecked()){
			startService(intent);
		}else{
			stopService(intent);
		}
	}
}
