package com.sansan.mobilesafe.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sansan.mobilesafe.HomeActivity;

public abstract class BaseFragment extends Fragment {

	private View view;
	public Context ct;
	public SlidingMenu sm;
	public boolean flag = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ct = getActivity();
//		sm = ((HomeActivity)getActivity()).getSlidingMenu();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}
	
	/**
	 * 初始化数据
	 * @param savedInstanceState 
	 */
	public abstract void initData(Bundle savedInstanceState);

	/**
	 * 初始化view
	 */
	public abstract View initView(LayoutInflater inflater);

	
}
