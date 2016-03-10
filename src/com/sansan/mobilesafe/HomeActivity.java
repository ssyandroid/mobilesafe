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
	private static String[] titles = { "垃圾清理", "手机加速", "流量监控", "骚扰拦截", "软件管理",
			"手机杀毒", "手机防盗", "高级工具" };
	private static String[] descs = { "剩余存储空间100%", "让手机满血复活", "看看流量去哪了",
			"防骚扰反欺诈哦", "已装？款软件呀", "三重引擎防护中", "丢了也不用怕哈", "胆小勿进耶~" };

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
	// 初始化SlideMenu
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
		//判断是否设置过密码
		if(isSetupPwd()){
			//已经设置密码了，弹出的是输入对话框，然后进入防盗主页面
			showEnterDialog();
		}else{
			//没有设置密码，进入设置密码页面
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
		tv_dialog_title.setText("请输入防盗密码");
		et_setup_pwd = (EditText) view.findViewById(R.id.et_find_pwd);
		EditText et_setup_confirm = (EditText) view.findViewById(R.id.et_find_cpwd);
		et_setup_confirm.setVisibility(View.GONE);
		Button ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		Button cancel = (Button) view.findViewById(R.id.bt_dialog_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把这个对话框取消掉
				myDialog.setDismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String savePassword = sp.getString("password");//取出加密后的
				if(TextUtils.isEmpty(password)){
					Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_LONG).show();
					return;
				}
				if(MD5Utils.md5Password(password).equals(savePassword)){
					//输入的密码是我之前设置的密码
					//把对话框消掉，进入主页面；
					myDialog.setDismiss();
					Intent intent = new Intent(HomeActivity.this,LostFindHomeActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_LONG).show();
					et_setup_pwd.setText("");
					return;
				}
			}
		});
		myDialog.show();
	}
	
	private void initRightMenu() {
		sm = getSlidingMenu();
		// 2 设置滑动菜单是在左边出来还是右边出来
		// 参数可以设置左边LEFT，也可以设置右边RIGHT ，还能设置左右LEFT_RIGHT
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 3 设置滑动菜单出来之后，内容页，显示的剩余宽度
		sm.setBehindOffset(20);
		// 4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
		sm.setShadowDrawable(R.drawable.shadow);
		// 5 设置阴影的宽度
		sm.setShadowWidth(10);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置滑动时菜单的是否淡入淡出
//		sm.setFadeEnabled(true);
		// 设置滑动时拖拽效果
//		sm.setBehindScrollScale(0.333f);
		// 6 设置滑动菜单的范围
		// 第一个参数 SlidingMenu.TOUCHMODE_FULLSCREEN 可以全屏滑动
		// 第二个参数 SlidingMenu.TOUCHMODE_MARGIN 只能在边沿滑动
		// 第三个参数 SlidingMenu.TOUCHMODE_NONE 不能滑动
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		Fragment leftMenuFragment = new MenuLeftFragment();
		setBehindContentView(R.layout.left_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_menu_frame, leftMenuFragment, "LeftMenu")
				.commit();

		// 设置右边（二级）侧滑菜单
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
