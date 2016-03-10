package com.sansan.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.sansan.mobilesafe.bean.UpdateJson;
import com.sansan.mobilesafe.service.AddressService;
import com.sansan.mobilesafe.ui.MyAlertDialog;
import com.sansan.mobilesafe.utils.AssetsToData;
import com.sansan.mobilesafe.utils.GsonUtils;
import com.sansan.mobilesafe.utils.ServiceUtils;
import com.sansan.mobilesafe.utils.SharedPreferencesUtils;
import com.sansan.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int ENTER_HOME = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView tv_version;
	private String description = null;
	private String apkurl = null;
	private SharedPreferencesUtils sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
	}

	private void initView() {
		tv_version = (TextView) findViewById(R.id.tv_vesion);
		tv_version.setText("版本号：" + getVersionName());

		sp = new SharedPreferencesUtils(this);
		boolean update = sp.getBoolean("update");

		installShortCut();

		// 拷贝数据库
		// 初始化数据库文件
		// 把asset下的数据库 拷贝到系统的目录里面
		copyDB("address.db");
		copyDB("commonnum.db");
		copyDB("antivirus.db");

		if (update) {
			// 检查升级
			checkUpdate();
		} else {
			// 自动升级已经关闭
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 进入主页面
					enterHome();

				}
			}, 2000);

		}

		// 是否开启来电悬浮窗
		if (sp.getBoolean("showAddress")) {
			boolean isServiceRunning = ServiceUtils.isServiceRunning(
					SplashActivity.this,
					"com.sansan.mobilesafe.service.AddressService");
			if (!isServiceRunning) {
				Intent showAddress = new Intent(this, AddressService.class);
				startService(showAddress);
			}
		}

		// 透明动画
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(600);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}

	private void installShortCut() {
		boolean shortcut = sp.getBoolean("shortcut");
		if (shortcut) {
			return;
		}
		// 发送广播的意图， 大吼一声告诉桌面，要创建快捷图标了
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷方式 要包含3个重要的信息 1，名称 2.图标 3.干什么事情
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));
		// 桌面点击图标对应的意图。
		Intent shortcutIntent = new Intent();
		shortcutIntent.setAction("android.intent.action.MAIN");
		shortcutIntent.addCategory("android.intent.category.LAUNCHER");
		shortcutIntent.setClassName(getPackageName(),
				"com.sansan.mobilesafe.SplashActivity");
		// shortcutIntent.setAction("com.sansan.xxxx");
		// shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		sendBroadcast(intent);
		sp.saveBoolean("shortcut", true);
	}

	/**
	 * path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
	 * 
	 * @param dbfilename
	 */
	private void copyDB(String dbfilename) {
		// 只要你拷贝了一次，我就不要你再拷贝了
		try {
			File file = new File(getFilesDir(), dbfilename);
			if (file.exists() && file.length() > 0) {
				// 正常了，就不需要拷贝了
				// Log.i(TAG, "正常了，就不需要拷贝了");
			} else {
				InputStream is = getAssets().open(dbfilename);
				new AssetsToData().assetsFileToData(file, is);
				// FileOutputStream fos = new FileOutputStream(file);
				// byte[] buffer = new byte[1024];
				// int len = 0;
				// while((len = is.read(buffer))!= -1){
				// fos.write(buffer, 0, len);
				// }
				// is.close();
				// fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:// 显示升级的对话框
				Log.i(TAG, "显示升级的对话框");
				showUpdateDialog();
				break;
			case ENTER_HOME:// 进入主页面
				enterHome();
				break;

			case URL_ERROR:// URL错误
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				break;

			case NETWORK_ERROR:// 网络异常
				enterHome();
				Toast.makeText(SplashActivity.this, "网络异常", 0).show();
				break;

			case JSON_ERROR:// JSON解析出错
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON解析出错", 0).show();
				break;

			}
		}

	};

	/**
	 * 检查更新
	 */
	private void checkUpdate() {

		new Thread() {
			public void run() {
				Message mes = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.serverurl).trim());
					// 联网
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if (code == 200) {
						// 联网成功
						InputStream is = conn.getInputStream();
						// 把流转成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功了" + result);
						// 使用gson解析
						UpdateJson object = GsonUtils.jsonToBean(result,
								UpdateJson.class);
						String version = object.version;
						description = object.description;
						apkurl = object.apkurl;
						// json解析
						// JSONObject obj = new JSONObject(result);
						// // 得到服务器的版本信息
						// String version = (String) obj.get("version");
						// description = (String) obj.get("description");
						// apkurl = (String) obj.get("apkurl");
						// 校验是否有新版本
						if (getVersionName().equals(version)) {
							// 版本一致，没有新版本，进入主页面
							mes.what = ENTER_HOME;
						} else {
							// 有新版本，弹出一升级对话框
							mes.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					mes.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
					// } catch (JSONException e) {
					// e.printStackTrace();
					// mes.what = JSON_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					// 我们花了多少时间
					long dTime = endTime - startTime;
					// 2000
					if (dTime < 3000) {
						try {
							Thread.sleep(3000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(mes);

				}
			};
		}.start();
	}

	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("新版本");
		builder.setMessage(description);
		// builder.setCancelable(false);//返回键不起作用
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// 进入主页面
				dialog.dismiss();
				enterHome();
			}
		});
		builder.setPositiveButton("下载升级", new OnClickListener() {

			private HttpHandler hh;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载apk，并且安装
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sdcard存在
					// afnal
					FinalHttp finalHttp = new FinalHttp();
					hh = finalHttp.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						private int progress = 0;
						private MyAlertDialog alertDialog;

						// 下载失败回调
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(), "下载失败", 1)
									.show();
							enterHome();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onStart() {
							updateDialog();
							super.onStart();
						}

						// 下载进度回调
						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							progress = (int) (current * 100 / count);
							alertDialog.setMessage("正在下载，已完成" + progress + "%");
							alertDialog.setProgress(progress);
						}

						// 下载成功回调
						@Override
						public void onSuccess(final File t) {
							super.onSuccess(t);
							installAPK(t);
							alertDialog.setSingleButton(View.GONE);
							alertDialog.setPositiveButton("安装",
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											installAPK(t);
										}
									});
							alertDialog.setNegativeButton("取消",
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											alertDialog.setDismiss();
											enterHome();
										}
									});
						}

						// 升级进度对话框
						private void updateDialog() {
							alertDialog = new MyAlertDialog(SplashActivity.this);
							alertDialog.setCancelable(false);
							alertDialog.setProgressBarVisibility(View.VISIBLE);
							alertDialog.setMessage("正在下载，已完成" + progress + "%");
							alertDialog.setSingleButton("取消下载",
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											hh.stop();
											alertDialog.setDismiss();
											enterHome();
										}
									});
							alertDialog.show();
						}

						// 安装APK
						private void installAPK(File t) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}
					});

				} else {
					Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试",
							0).show();
					return;
				}
			}

		});
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();// 进入主页面
			}
		});
		builder.show();

	}

	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();

	}

	/**
	 * 得到应用程序的版本
	 */
	private String getVersionName() {
		// 管理手机的APP
		PackageManager packageManager = getPackageManager();
		try {
			// 得到APP功能清单文件
			PackageInfo info = packageManager.getPackageInfo(getPackageName(),
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "最新版";
		}

	}

}
