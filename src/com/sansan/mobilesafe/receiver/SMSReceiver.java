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
		// д���ն��ŵĴ���
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = new SharedPreferencesUtils(context);
		
		for(Object b:objs){
			//�����ĳһ������
			SmsMessage sms =SmsMessage.createFromPdu((byte[]) b);
			//������
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber");
//			Toast.makeText(context, sender, 1).show();
			Log.i(TAG, "====sender=="+sender);
			String body = sms.getMessageBody();
			
			if(sender.contains(safenumber)){
				
				if("#weizhi#".equals(body)){
					//�õ��ֻ���GPS
					Log.i(TAG, "�õ��ֻ���GPS");
					//��������
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					String lastlocation = sp.getString("lastlocation");
					if(TextUtils.isEmpty(lastlocation)){
						//λ��û�еõ�
						SmsManager.getDefault().sendTextMessage(sender, null, "��ȡλ����.....", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//������㲥��ֹ��
					abortBroadcast();
				}else if("#jingbao#".equals(body)){
					//���ű���Ӱ��
					Log.i(TAG, "���ű���Ӱ��");
					MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
					player.setLooping(true);//ѭ��
					player.setVolume(1.0f, 1.0f);
					player.start();
					
					abortBroadcast();
				}
				else if("#qingchu#".equals(body)){
					//Զ���������
					Log.i(TAG, "Զ���������");
					abortBroadcast();
				}
				else if("#suoping#".equals(body)){
					//Զ������
					Log.i(TAG, "Զ������");
					abortBroadcast();
				}
			}
			
			
			
		}
 

	}

}
