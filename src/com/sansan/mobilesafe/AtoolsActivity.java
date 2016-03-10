package com.sansan.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sansan.mobilesafe.utils.SmsUtils;
import com.sansan.mobilesafe.utils.SmsUtils.BackUpCallBack;

public class AtoolsActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_title.setText("�߼�����");
		tv_title.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_titlebar_left:
			finish();
			break;
		}
	}
	
	/**
	 * ����¼��������������ز�ѯ��ҳ��
	 */
	public void numberlocate(View v){
		Intent intent = new Intent(AtoolsActivity.this,NumberLocateQueryActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * ����¼������ŵı���
	 * @param view
	 */
	public void smsBackup(View view){
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		new Thread(){
			public void run() {
				try {
					SmsUtils.backupSms(getApplicationContext(), new BackUpCallBack() {
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
						}
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "���ݳɹ�", 0).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "����ʧ��", 0).show();
						}
					});
				}finally{
					pd.dismiss();
				}
			};
		}.start();
		
	}
	/**
	 * ����¼������ŵĻ�ԭ
	 * @param view
	 */
	public void smsRestore(View view){
		
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this, "��ԭ�ɹ�", 0).show();
	}
}
