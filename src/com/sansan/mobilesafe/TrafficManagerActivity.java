package com.sansan.mobilesafe;

import android.app.Activity;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TrafficStats.getMobileTxBytes(); //3g/2g网络上传的总流量
		// TrafficStats.getMobileRxBytes();//3g/2g网络下载的总流量
		// TrafficStats.getTotalRxBytes();// 手机卡 + wifi
		// TrafficStats.getTotalTxBytes();
		// TrafficStats.getUidRxBytes(uid);
		// TrafficStats.getUidTxBytes(uid);
		setContentView(R.layout.activity_traffic_manager);
	}
}
