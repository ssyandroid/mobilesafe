package com.sansan.mobilesafe.base;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T, Q> extends BaseAdapter {

	private Context ct;
	private List<T> list;
	private Q view;// 这里不一定是ListView,比如GridView,CustomListView
	
	public MyBaseAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyBaseAdapter(Context ct, List<T> list) {
		super();
		this.ct = ct;
		this.list = list;
	}

	public MyBaseAdapter(Context ct, List<T> list, Q view) {
		super();
		this.ct = ct;
		this.list = list;
		this.view = view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
//		return list.get(position);
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	

}
