package com.sansan.mobilesafe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sansan.mobilesafe.R;
import com.sansan.mobilesafe.SettingActivity;
import com.sansan.mobilesafe.base.BaseFragment;

public class MenuRightFragment extends BaseFragment {

	private TextView tv_setings;
	private View view;  

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.right_menu_content, null);
		tv_setings = (TextView) view.findViewById(R.id.tv_setings);
		tv_setings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SettingActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}  
    
    
}
