package com.sansan.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sansan.mobilesafe.service.AddressService;
import com.sansan.mobilesafe.ui.SettingTextButtonView;
import com.sansan.mobilesafe.ui.SettingToggleButtonView;
import com.sansan.mobilesafe.utils.ServiceUtils;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class ShowNumberAddressActivity extends Activity implements OnClickListener {
	
	private TextView tv_title;
	private SharedPreferencesUtils sp;

	
	// �����Ƿ�����ʾ������
	private SettingToggleButtonView siv_show_address;
	private Intent showAddress;
		
	//���ù�������ʾ�򱳾�
	private SettingTextButtonView scv_changebg;
	
	final String [] items = {"��͸��","������","��ʿ��","������","ƻ����"};
	private boolean isShowAddress;
	private boolean isServiceRunning;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showadress);
		
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_title.setText("����������");
		tv_title.setOnClickListener(this);
		sp = new SharedPreferencesUtils(this);
		
		// ���ú����������ʾ�ռ�
		siv_show_address = (SettingToggleButtonView) findViewById(R.id.tb_open_numberfloat);
		siv_show_address.setOnClickListener(this);
		
		scv_changebg = (SettingTextButtonView) findViewById(R.id.tv_numberfloat_style);
		
		isShowAddress = sp.getBoolean("showAddress");
		
		showAddress = new Intent(this, AddressService.class);
		isServiceRunning = ServiceUtils.isServiceRunning(
				ShowNumberAddressActivity.this,
				"com.sansan.mobilesafe.service.AddressService");
		
		if(isShowAddress){
			//��������ķ����ǿ�����
			if(!isServiceRunning){
				startService(showAddress);
			}
			siv_show_address.setChecked(true);
			scv_changebg.setVisibility(View.VISIBLE);
		}else{
			siv_show_address.setChecked(false);
			scv_changebg.setVisibility(View.GONE);
		}
		
		//���ú�������صı���
		int which = sp.getInt("which");
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		if(isShowAddress){
			//��������ķ����ǿ�����
			if(!isServiceRunning){
				startService(showAddress);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//ͨ������
		case R.id.tb_open_numberfloat:
			if (siv_show_address.isChecked()) {
				// ��Ϊ��ѡ��״̬
				siv_show_address.setChecked(false);
				stopService(showAddress);
				scv_changebg.setVisibility(View.GONE);
				sp.saveBoolean("showAddress", false);
			} else {
				// ѡ��״̬
				siv_show_address.setChecked(true);
				startService(showAddress);
				scv_changebg.setVisibility(View.VISIBLE);
				sp.saveBoolean("showAddress", true);
			}
			break;
		case R.id.tv_numberfloat_style:
			styleNmuber();
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
	}
	
	public void styleNmuber(){
		int dd = sp.getInt("which");
		// ����һ���Ի���
		AlertDialog.Builder builder = new Builder(ShowNumberAddressActivity.this);
		builder.setTitle("��������ʾ����");
		builder.setSingleChoiceItems(items,dd, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				//����ѡ�����
				sp.saveInt("which", which);
				scv_changebg.setDesc(items[which]);
				
				//ȡ���Ի���
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
}
