package com.sansan.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sansan.mobilesafe.ui.SettingToggleButtonView;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class GeneralSettingActivity extends Activity implements OnClickListener {
	
	private TextView tv_title;
	private SettingToggleButtonView tb_update;
	private SharedPreferencesUtils sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generalseting);
		
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tb_update = (SettingToggleButtonView) findViewById(R.id.tb_update);
		tv_title.setText("ͨ������");
		tb_update.setOnClickListener(this);
		tv_title.setOnClickListener(this);
		sp = new SharedPreferencesUtils(this);
		boolean update = sp.getBoolean("update");
		if(update){
			//�Զ������Ѿ�����
			tb_update.setChecked(true);
		}else{
			//�Զ������Ѿ��ر�
			tb_update.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//ͨ������
		case R.id.tb_update:
			//�ж��Ƿ���ѡ��
			//�Ѿ����Զ�������
			if(tb_update.isChecked()){
				tb_update.setChecked(false);
				sp.saveBoolean("update", false);
			//û�д��Զ�����	
			}else{
				tb_update.setChecked(true);
				sp.saveBoolean("update", true);
			}
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
	}
}
