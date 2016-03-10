package com.sansan.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.sansan.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {
	public static final String TAG = "CallSmsSafeService";
	private InnerSmsReceiver receiver;
	private BlackNumberDao dao;
	private TelephonyManager tm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class InnerSmsReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG,"�ڲ��㲥�����ߣ� ���ŵ�����");
			//��鷢�����Ƿ��Ǻ��������룬���ö�������ȫ�����ء�
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//�õ����ŷ�����
				String sender = smsMessage.getOriginatingAddress();
				String result = dao.findMode(sender);
				if("2".equals(result)||"3".equals(result)){
					Log.i(TAG,"���ض���");
					abortBroadcast();
				}
				//��ʾ���롣
				String body = smsMessage.getMessageBody();
				if(body.contains("fapiao")){
					//���ͷ��Ʊ���ĺ�  ���Էִʼ�����
					Log.i(TAG,"���ط�Ʊ����");
					abortBroadcast();
				}
			}
		}
	}
	
	@Override
	public void onCreate() {
		dao = new BlackNumberDao(this);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new InnerSmsReceiver();
		IntentFilter filter =  new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		//���ȼ�
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver,filter);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}
	
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://����״̬��
				String result = dao.findMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					Log.i(TAG,"�Ҷϵ绰��������");
					//�۲���м�¼���ݿ����ݵı仯��
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver( incomingNumber,new Handler()));
					endCall();//����һ�������������е� Զ�̷���ķ�����
					//�������ú󣬺��м�¼���ܻ�û�����ɡ�
					//ɾ�����м�¼��
					//����һ��Ӧ�ó�����ϵ�˵�Ӧ�õ�˽�����ݿ�
					//deleteCallLog(incomingNumber);
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;
		public CallLogObserver(String incomingNumber,Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			Log.i(TAG,"���ݿ�����ݱ仯�ˣ������˺��м�¼");
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
		}
	}
	/**
	 * ���������ṩ��ɾ�����м�¼
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver  resolver = getContentResolver();
		//���м�¼uri��·��
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
	
	public void endCall() {
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			//����servicemanager���ֽ���
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}