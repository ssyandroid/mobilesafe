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
	 * 系统提供的振动服务
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
		tv_title.setText("归属地查询");
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		et_number.addTextChangedListener(new TextWatcher() {

			/**
			 * 当文本发生变化的时候回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!= null&&s.length()>=3){
					//查询数据库，并且显示结果
					String address = NumberLocateQueryUtils.queryNumber(s.toString().trim());
					tv_numer_reuslt.setText(address);
				}
			}
			
			/**
			 * 当文本发生变化之前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			/**
			 * 当文本发生变化之后回调
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
	 * 查询号码归属地
	 * @param view
	 */
	public void query(View view){
		String phone = et_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码不为空", Toast.LENGTH_SHORT).show();
			 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 et_number.startAnimation(shake);
			 
			 //当电话号码为空的时候，就去振动手机提醒用户
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,500,500,1000,2000};
			 //-1不重复 0循环振动 1；
			 vibrator.vibrate(pattern, -1);
			 
			return;
		}else{
			String address = NumberLocateQueryUtils.queryNumber(phone);
			tv_numer_reuslt.setText(address);
			//去数据库查询号码归属地
			//1.网络查询 ；2.本地的数据库--数据库
			//写一个工具类，去查询数据库
//			Log.i("NumberAddressQueryActivity", "您要查询的电话号码=="+phone);
		}
		
	}

}
