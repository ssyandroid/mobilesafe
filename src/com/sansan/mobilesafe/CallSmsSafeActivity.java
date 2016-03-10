package com.sansan.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sansan.mobilesafe.bean.BlackNumberInfo;
import com.sansan.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;
	private ListView lv_callsms_safe;
	private CallSmsSafeAdapter adapter;
	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_cancel;
	private Button bt_ok;
	private ImageView iv_title_right;
	private LinearLayout ll_loading;
	private int offset = 0;
	private int maxnumber = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		tv_title = (TextView) findViewById(R.id.tv_titlebar_left);
		tv_title.setText("ɧ������");
		tv_title.setOnClickListener(this);
		
		iv_title_right = (ImageView) findViewById(R.id.iv_titlebar_right);
		iv_title_right.setVisibility(View.VISIBLE);
		iv_title_right.setOnClickListener(this);
		
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		
		dao = new BlackNumberDao(this);
		fillData();
		// listviewע��һ�������¼��ļ�������
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			// ��������״̬�����仯��ʱ��
			int s = 0;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// ����״̬
					// �жϵ�ǰlistview������λ��
					// ��ȡ���һ���ɼ���Ŀ�ڼ��������λ�á�
					int lastposition = lv_callsms_safe.getLastVisiblePosition();

					// ����������20��item λ�ô�0��ʼ�� ���һ����Ŀ��λ�� 19
					if (lastposition == (infos.size() - 1)) {
//						System.out.println("�б����ƶ��������һ��λ�ã����ظ�������ݡ�����");
						
						if(s != infos.size()){
							
							offset += maxnumber;
							fillData();
							s = infos.size();
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// ��ָ��������
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// ���Ի���״̬
					break;
				}
			}
			
			// ������ʱ����õķ�����
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(offset, maxnumber);
				} else {// ԭ���Ѿ����ع������ˡ�
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new CallSmsSafeAdapter();
							lv_callsms_safe.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_titlebar_left:
			finish();
			break;
		case R.id.iv_titlebar_right:
			//�����������
			Intent intent = new Intent(CallSmsSafeActivity.this,CallSmsSafeSettingActivity.class);
			startActivity(intent);
			break;
		}
		
	}
	
	private class CallSmsSafeAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return infos.size();
		}
		//�ж��ٸ���Ŀ����ʾ����������ͻᱻ���ö��ٴ�
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//1.�����ڴ���view���󴴽��ĸ���
			if(convertView==null){
//				Log.i(TAG,"�����µ�view����"+position);
			//��һ�������ļ�ת����  view����
				view  = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				//2.�����Ӻ��Ӳ�ѯ�Ĵ���  �ڴ��ж���ĵ�ַ��
				holder = new ViewHolder();
				holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_block_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				//��������������ʱ���ҵ����ǵ����ã�����ڼ��±������ڸ��׵Ŀڴ�
				view.setTag(holder);
			}else{
//				Log.i(TAG,"��������ʷ��view���󣬸�����ʷ�����view����"+position);
				view = convertView;
				holder = (ViewHolder) view.getTag();//5%
			}
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if("1".equals(mode)){
				holder.tv_mode.setText("�绰����");
			}else if("2".equals(mode)){
				holder.tv_mode.setText("��������");
			}else{
				holder.tv_mode.setText("ȫ������");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
					builder.setTitle("����");
					builder.setMessage("ȷ��Ҫɾ��������¼ô��");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//ɾ�����ݿ������
							dao.delete(infos.get(position).getNumber());
							//���½��档
							infos.remove(position);
							//֪ͨlistview��������������
							adapter.notifyDataSetChanged();
						}
					});
					builder.setNegativeButton("ȡ��", null);
					builder.show();
				}
			});
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	/**
	 * view���������
	 *��¼���ӵ��ڴ��ַ��
	 *�൱��һ�����±�
	 */
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	public void addBlackNumber(View view){
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.add_blacknumber_dialog, null);
		et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_cancel = (Button) contentView.findViewById(R.id.bt_dialog_cancle);
		bt_ok = (Button) contentView.findViewById(R.id.bt_dialog_ok);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if(TextUtils.isEmpty(blacknumber)){
					Toast.makeText(getApplicationContext(), "���������벻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				String mode ;
				if(cb_phone.isChecked()&&cb_sms.isChecked()){
					//ȫ������
					mode = "3";
				}else if(cb_phone.isChecked()){
					//�绰����
					mode = "1";
				}else if(cb_sms.isChecked()){
					//��������
					mode = "2";
				}else{
					Toast.makeText(getApplicationContext(), "��ѡ������ģʽ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(dao.find(blacknumber)){
					Toast.makeText(getApplicationContext(), "�����Ѿ�����", Toast.LENGTH_SHORT).show();
					et_blacknumber.setText("");
					return;
				}
				//���ݱ��ӵ����ݿ�
				dao.add(blacknumber, mode);
				//����listview������������ݡ�
				BlackNumberInfo info = new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				infos.add(0, info);
				//֪ͨlistview�������������ݸ����ˡ�
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		
	}
}