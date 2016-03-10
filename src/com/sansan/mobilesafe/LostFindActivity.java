package com.sansan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sansan.mobilesafe.ui.MyAlertDialog;
import com.sansan.mobilesafe.utils.MD5Utils;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class LostFindActivity extends Activity implements OnClickListener {

	protected static final String TAG = "LostFindActivity";
	private TextView tv_title;
	private Button bt_find;
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private SharedPreferencesUtils sp;
	private ImageView iv_lost;
	private LinearLayout lost_guide;
	private TextView tv_safe_number;
	private Button bt_find_ok;
	private String number = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind);
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		iv_lost = (ImageView) findViewById(R.id.iv_lost);
		bt_find = (Button) findViewById(R.id.bt_open_find);
		bt_find_ok = (Button) findViewById(R.id.bt_find_ok);
		lost_guide = (LinearLayout) findViewById(R.id.lost_guide);
		tv_safe_number = (TextView) findViewById(R.id.tv_getsafenumber);
		tv_title.setText("手机防盗");
		bt_find.setOnClickListener(this);
		bt_find_ok.setOnClickListener(this);
		
		tv_title.setOnClickListener(this);
		tv_safe_number.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 通用设置
		case R.id.bt_open_find:
			showSetupPwdDialog();
			break;
		case R.id.tv_getsafenumber:
			//获取安全号码
			pickContact();
			break;
		case R.id.bt_find_ok:
			if(number != null){
				//进入开启的防盗页面
				Intent i = new Intent(LostFindActivity.this,LostFindHomeActivity.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(LostFindActivity.this, "请选择亲友号码", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_titlebar_left:
			finish();
			break;
		}

	}
	
	static final int PICK_CONTACT_REQUEST = 1; // The request code
	
	private void pickContact() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,Uri.parse("content://contacts"));
		pickContactIntent.setType(Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String name = null;
		// Check which request it is that we're responding to
		if (requestCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// Get the URI that points to the selected contact
				Uri contactUri = data.getData();
				
				// We only need the NUMBER column, because there will be only
				// one row in the result
				String[] projection = { Phone.NUMBER,Phone.DISPLAY_NAME};
				Cursor cursor = getContentResolver().query(contactUri,
						projection, null, null, null);
				
				if (cursor.moveToFirst()){
					// Retrieve the phone number from the NUMBER column
					int column = cursor.getColumnIndex(Phone.NUMBER);
					int columnIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
					number = cursor.getString(column).replace("-", "").replace(" ", "");
					name = cursor.getString(columnIndex);
				}
				cursor.close();
				tv_safe_number.setText(name+"："+number);
				sp.saveString("safenumber", number);
			}
		}
	}
	
	private void showSetupPwdDialog() {
		final MyAlertDialog myDialog = new MyAlertDialog(LostFindActivity.this);
		View view = View.inflate(LostFindActivity.this, R.layout.input_dialog, null);
		myDialog.setView(view);
		myDialog.setCancelable(false);
		TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_title);
		tv_dialog_title.setText("请设置防盗密码");
		et_setup_pwd = (EditText) view.findViewById(R.id.et_find_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_find_cpwd);
		ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		cancel = (Button) view.findViewById(R.id.bt_dialog_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把这个对话框取消掉
				myDialog.setDismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String password_confirm = et_setup_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)){
					Toast.makeText(LostFindActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//判断是否一致才去保存
				if(password.equals(password_confirm)){
					//一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面
					sp = new SharedPreferencesUtils(LostFindActivity.this);
					//保存加密后的
					sp.saveString("password", MD5Utils.md5Password(password));
					myDialog.setDismiss();
					iv_lost.setVisibility(View.GONE);
					lost_guide.setVisibility(View.VISIBLE);
					bt_find_ok.setVisibility(View.VISIBLE);
					getSimNumber();
				}else{
					Toast.makeText(LostFindActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
					return ;
				}
			}
		});
		myDialog.show();
	}
	
	/**
	 * 读取手机sim的信息
	 */
	private TelephonyManager tm;
	
	/**
	 * 获取并保存sim卡的序列号
	 * getSimNumber 
	 */
	private void getSimNumber() {
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim = tm.getSimSerialNumber();
		sp.saveString("sim", sim);
		
	}
}
