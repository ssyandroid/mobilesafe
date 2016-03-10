package com.sansan.mobilesafe;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CleanActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean);
		
		TabHost tabHost = getTabHost();//获取当前界面tabhost的容器，容器里面可以放置很多的小标签
		
		TabSpec tab1 = tabHost.newTabSpec("缓存清理");
		TabSpec tab2 = tabHost.newTabSpec("SD卡清理");
		
		//设置两个选项卡的标签
		tab1.setIndicator("缓存清理");
		tab2.setIndicator("SD卡清理");
		//设置两个选项卡里面的内容
		tab1.setContent(new Intent(this,CleanCacheActivity.class));
		tab2.setContent(new Intent(this,CleanSDActivity.class));
		
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		
		
	}
}
