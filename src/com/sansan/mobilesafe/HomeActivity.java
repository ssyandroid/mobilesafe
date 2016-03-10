package com.sansan.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sansan.mobilesafe.fragment.MenuLeftFragment;
import com.sansan.mobilesafe.fragment.MenuRightFragment;
import com.sansan.mobilesafe.ui.MyAlertDialog;
import com.sansan.mobilesafe.utils.MD5Utils;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;

public class HomeActivity extends SlidingFragmentActivity {
	private GridView gv_home;
	private MyAdapter adapter;
	private SlidingMenu sm;
	private static String[] titles = { "��������", "�ֻ�����", "�������", "ɧ������", "�������",
			"�ֻ�ɱ��", "�ֻ�����", "�߼�����" };
	private static String[] descs = { "ʣ��洢�ռ�100%", "���ֻ���Ѫ����", "��������ȥ����",
			"��ɧ�ŷ���թŶ", "��װ�������ѽ", "�������������", "����Ҳ�����¹�", "��С���Ү~" };

	private static int[] ids = { R.drawable.ql, R.drawable.jc, R.drawable.ll,
			R.drawable.sr, R.drawable.rj, R.drawable.sd, R.drawable.sr,
			R.drawable.ql };
	private ImageView iv_left;
	private ImageView iv_right;
	private SharedPreferencesUtils sp;
	private EditText et_setup_pwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initRightMenu();
		initView();
		
	}
	// ��ʼ��SlideMenu
	private void initView() {
		iv_left = (ImageView) findViewById(R.id.iv_top_left);
		iv_right = (ImageView) findViewById(R.id.iv_top_right);
		gv_home = (GridView) findViewById(R.id.gv_home);
		adapter = new MyAdapter();
		gv_home.setAdapter(adapter);
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(HomeActivity.this,CleanActivity.class));
					break;
				case 1:
					startActivity(new Intent(HomeActivity.this,TaskManagerActivity.class));
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this,TrafficManagerActivity.class));
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this,CallSmsSafeActivity.class));
					break;
				case 4:
					startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
					break;
				case 5:
					startActivity(new Intent(HomeActivity.this,AntiVirusActivity.class));
					break;
				case 6:
					showLostFindDialog();
					break;
				case 7:
					Intent intent = new Intent(HomeActivity.this,AtoolsActivity.class);
					startActivity(intent);
					break;
				}

			}

		});
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				sm.toggle();
				sm.showMenu(true);
			}
		});
		iv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			sm.showSecondaryMenu(true);
			}
		});
		
	}

	private void showLostFindDialog() {
		sp = new SharedPreferencesUtils(this);
		//�ж��Ƿ����ù�����
		if(isSetupPwd()){
			//�Ѿ����������ˣ�������������Ի���Ȼ����������ҳ��
			showEnterDialog();
		}else{
			//û���������룬������������ҳ��
			Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
			startActivity(intent);
		}
		
	}
	
	private boolean isSetupPwd() {
		String password = sp.getString("password");
//		if(TextUtils.isEmpty(password)){
//			return false;
//		}else{
//			return true;
//		}
		return !TextUtils.isEmpty(password);
	}
	
	private void showEnterDialog() {
		final MyAlertDialog myDialog = new MyAlertDialog(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.input_dialog, null);
		myDialog.setView(view);
		TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_title);
		tv_dialog_title.setText("�������������");
		et_setup_pwd = (EditText) view.findViewById(R.id.et_find_pwd);
		EditText et_setup_confirm = (EditText) view.findViewById(R.id.et_find_cpwd);
		et_setup_confirm.setVisibility(View.GONE);
		Button ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		Button cancel = (Button) view.findViewById(R.id.bt_dialog_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//������Ի���ȡ����
				myDialog.setDismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  ȡ������
				String password = et_setup_pwd.getText().toString().trim();
				String savePassword = sp.getString("password");//ȡ�����ܺ��
				if(TextUtils.isEmpty(password)){
					Toast.makeText(HomeActivity.this, "����Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				if(MD5Utils.md5Password(password).equals(savePassword)){
					//�������������֮ǰ���õ�����
					//�ѶԻ���������������ҳ�棻
					myDialog.setDismiss();
					Intent intent = new Intent(HomeActivity.this,LostFindHomeActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "�������", Toast.LENGTH_LONG).show();
					et_setup_pwd.setText("");
					return;
				}
			}
		});
		myDialog.show();
	}
	
	private void initRightMenu() {
		sm = getSlidingMenu();
		// 2 ���û����˵�������߳��������ұ߳���
		// ���������������LEFT��Ҳ���������ұ�RIGHT ��������������LEFT_RIGHT
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 3 ���û����˵�����֮������ҳ����ʾ��ʣ����
		sm.setBehindOffset(20);
		// 4 ���û����˵�����Ӱ ������Ӱ����Ӱ��Ҫ�ڿ�ʼ��ʱ���ر𰵣������ı䵭
		sm.setShadowDrawable(R.drawable.shadow);
		// 5 ������Ӱ�Ŀ��
		sm.setShadowWidth(10);
		// ���ý��뽥��Ч����ֵ
		sm.setFadeDegree(0.35f);
		// ���û���ʱ�˵����Ƿ��뵭��
//		sm.setFadeEnabled(true);
		// ���û���ʱ��קЧ��
//		sm.setBehindScrollScale(0.333f);
		// 6 ���û����˵��ķ�Χ
		// ��һ������ SlidingMenu.TOUCHMODE_FULLSCREEN ����ȫ������
		// �ڶ������� SlidingMenu.TOUCHMODE_MARGIN ֻ���ڱ��ػ���
		// ���������� SlidingMenu.TOUCHMODE_NONE ���ܻ���
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		Fragment leftMenuFragment = new MenuLeftFragment();
		setBehindContentView(R.layout.left_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_menu_frame, leftMenuFragment, "LeftMenu")
				.commit();

		// �����ұߣ��������໬�˵�
		sm.setSecondaryMenu(R.layout.right_menu);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		Fragment rightMenuFragment = new MenuRightFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.right_menu_frame, rightMenuFragment, "RightMenu")
				.commit();

	}
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_gg, null);
			ImageView iv_left = (ImageView) view.findViewById(R.id.iv_list_left);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_list_title);
			TextView tv_desc = (TextView) view.findViewById(R.id.tv_list_desc);

			iv_left.setBackgroundResource(ids[position]);
			tv_title.setText(titles[position]);
			tv_desc.setText(descs[position]);
			return view;
		}

	}
}
