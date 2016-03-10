package com.sansan.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sansan.mobilesafe.db.dao.NumberLocateQueryUtils;

public class NumberLocateQueryActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private TextView tv_numer_reuslt;
	private EditText et_number;
	
	/**
	 * ϵͳ�ṩ���񶯷���
	 */
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_numberlocate);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_numer_reuslt = (TextView) findViewById(R.id.tv_numer_reuslt);
		et_number = (EditText) findViewById(R.id.et_number);
		
		tv_title.setOnClickListener(this);
		tv_title.setText("�����ز�ѯ");
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		et_number.addTextChangedListener(new TextWatcher() {

			/**
			 * ���ı������仯��ʱ��ص�
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!= null&&s.length()>=3){
					//��ѯ���ݿ⣬������ʾ���
					String address = NumberLocateQueryUtils.queryNumber(s.toString().trim());
					tv_numer_reuslt.setText(address);
				}
			}
			
			/**
			 * ���ı������仯֮ǰ�ص�
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			/**
			 * ���ı������仯֮��ص�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
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
	 * ��ѯ���������
	 * @param view
	 */
	public void query(View view){
		String phone = et_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "���벻Ϊ��", Toast.LENGTH_SHORT).show();
			 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 et_number.startAnimation(shake);
			 
			 //���绰����Ϊ�յ�ʱ�򣬾�ȥ���ֻ������û�
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,500,500,1000,2000};
			 //-1���ظ� 0ѭ���� 1��
			 vibrator.vibrate(pattern, -1);
			 
			return;
		}else{
			String address = NumberLocateQueryUtils.queryNumber(phone);
			tv_numer_reuslt.setText(address);
			//ȥ���ݿ��ѯ���������
			//1.�����ѯ ��2.���ص����ݿ�--���ݿ�
			//дһ�������࣬ȥ��ѯ���ݿ�
//			Log.i("NumberAddressQueryActivity", "��Ҫ��ѯ�ĵ绰����=="+phone);
		}
		
	}

}
