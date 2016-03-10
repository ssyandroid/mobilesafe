package com.sansan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class LostFindSettingActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private TextView tv_safe_number;
	private SharedPreferencesUtils sp;
	private String number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_settings);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
		tv_title.setText("ÊÖ»ú·ÀµÁÉèÖÃ");
		tv_title.setOnClickListener(this);
		tv_safe_number.setOnClickListener(this);
		
		sp = new SharedPreferencesUtils(this);
		String number = sp.getString("safenumber");
		if(number != null){
			tv_safe_number.setText("Ç×ÓÑºÅÂë£º"+number);
		}else{
			tv_safe_number.setText("Ç×ÓÑºÅÂë£ºÎÞÐ§");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_titlebar_left:
			finish();
			break;
		case R.id.tv_safe_number:
			 pickContact();
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
//		String name = null;
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
//					int columnIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
					number = cursor.getString(column).replace("-", "").replace(" ", "");
//					name = cursor.getString(columnIndex);
				}
				cursor.close();
				 tv_safe_number.setText("Ç×ÓÑºÅÂë£º"+number);
				sp.saveString("safenumber", number);
			}
		}
	}
}
