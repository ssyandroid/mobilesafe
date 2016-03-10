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
			//��������������ִ������ط�
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			
			// ��ȡ֮ǰ�����SiM��Ϣ��
			String saveSim = sp.getString("sim");
			
			//��ȡ��ǰ��sim����Ϣ
			String realSim = tm.getSimSerialNumber();
			
			//�Ƚ��Ƿ�һ��
			if(saveSim.equals(realSim)){
				//simû�б��������ͬһ������
			}else{
				// sim �Ѿ���� ��һ�����Ÿ���ȫ����
				Toast.makeText(context, "SIM���Ѿ����", Toast.LENGTH_SHORT).show();
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber"), null, "SIM���Ѿ��������ע���ֻ��Ƿ񱻵���", null, null);
			}

		}
		
	}

}
