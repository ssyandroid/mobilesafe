package com.sansan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	private TelephonyManager tm;
	private SharedPreferencesUtils sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		sp = new SharedPreferencesUtils(context);
		boolean protecting = sp.getBoolean("protecting");
		if(protecting){
			//开启防盗保护才执行这个地方
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			
			// 读取之前保存的SiM信息；
			String saveSim = sp.getString("sim");
			
			//读取当前的sim卡信息
			String realSim = tm.getSimSerialNumber();
			
			//比较是否一样
			if(saveSim.equals(realSim)){
				//sim没有变更，还是同一个哥们
			}else{
				// sim 已经变更 发一个短信给安全号码
				Toast.makeText(context, "SIM卡已经变更", Toast.LENGTH_SHORT).show();
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber"), null, "SIM卡已经变更，请注意手机是否被盗！", null, null);
			}

		}
		
	}

}
