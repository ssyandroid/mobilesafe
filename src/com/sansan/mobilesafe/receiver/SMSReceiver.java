package com.sansan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.sansan.mobilesafe.R;
import com.sansan.mobilesafe.service.GPSService;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class SMSReceiver extends BroadcastReceiver {
	

	private static final String TAG = "SMSReceiver";
	private SharedPreferencesUtils sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 写接收短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = new SharedPreferencesUtils(context);
		
		for(Object b:objs){
			//具体的某一条短信
			SmsMessage sms =SmsMessage.createFromPdu((byte[]) b);
			//发送者
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber");
//			Toast.makeText(context, sender, 1).show();
			Log.i(TAG, "====sender=="+sender);
			String body = sms.getMessageBody();
			
			if(sender.contains(safenumber)){
				
				if("#weizhi#".equals(body)){
					//得到手机的GPS
					Log.i(TAG, "得到手机的GPS");
					//启动服务
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					String lastlocation = sp.getString("lastlocation");
					if(TextUtils.isEmpty(lastlocation)){
						//位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null, "获取位置中.....", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//把这个广播终止掉
					abortBroadcast();
				}else if("#jingbao#".equals(body)){
					//播放报警影音
					Log.i(TAG, "播放报警影音");
					MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
					player.setLooping(true);//循环
					player.setVolume(1.0f, 1.0f);
					player.start();
					
					abortBroadcast();
				}
				else if("#qingchu#".equals(body)){
					//远程清除数据
					Log.i(TAG, "远程清除数据");
					abortBroadcast();
				}
				else if("#suoping#".equals(body)){
					//远程锁屏
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}
			}
			
			
			
		}
 

	}

}
