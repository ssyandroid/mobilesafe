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
		tv_version.setText("�汾�ţ�" + getVersionName());

		sp = new SharedPreferencesUtils(this);
		boolean update = sp.getBoolean("update");

		installShortCut();

		// �������ݿ�
		// ��ʼ�����ݿ��ļ�
		// ��asset�µ����ݿ� ������ϵͳ��Ŀ¼����
		copyDB("address.db");
		copyDB("commonnum.db");
		copyDB("antivirus.db");

		if (update) {
			// �������
			checkUpdate();
		} else {
			// �Զ������Ѿ��ر�
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// ������ҳ��
					enterHome();

				}
			}, 2000);

		}

		// �Ƿ�������������
		if (sp.getBoolean("showAddress")) {
			boolean isServiceRunning = ServiceUtils.isServiceRunning(
					SplashActivity.this,
					"com.sansan.mobilesafe.service.AddressService");
			if (!isServiceRunning) {
				Intent showAddress = new Intent(this, AddressService.class);
				startService(showAddress);
			}
		}

		// ͸������
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(600);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}

	private void installShortCut() {
		boolean shortcut = sp.getBoolean("shortcut");
		if (shortcut) {
			return;
		}
		// ���͹㲥����ͼ�� ���һ���������棬Ҫ�������ͼ����
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// ��ݷ�ʽ Ҫ����3����Ҫ����Ϣ 1������ 2.ͼ�� 3.��ʲô����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ֻ���ʿ");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));
		// ������ͼ���Ӧ����ͼ��
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
	 * path ��address.db������ݿ⿽����data/data/��������/files/address.db
	 * 
	 * @param dbfilename
	 */
	private void copyDB(String dbfilename) {
		// ֻҪ�㿽����һ�Σ��ҾͲ�Ҫ���ٿ�����
		try {
			File file = new File(getFilesDir(), dbfilename);
			if (file.exists() && file.length() > 0) {
				// �����ˣ��Ͳ���Ҫ������
				// Log.i(TAG, "�����ˣ��Ͳ���Ҫ������");
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
			case SHOW_UPDATE_DIALOG:// ��ʾ�����ĶԻ���
				Log.i(TAG, "��ʾ�����ĶԻ���");
				showUpdateDialog();
				break;
			case ENTER_HOME:// ������ҳ��
				enterHome();
				break;

			case URL_ERROR:// URL����
				enterHome();
				Toast.makeText(getApplicationContext(), "URL����", 0).show();
				break;

			case NETWORK_ERROR:// �����쳣
				enterHome();
				Toast.makeText(SplashActivity.this, "�����쳣", 0).show();
				break;

			case JSON_ERROR:// JSON��������
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON��������", 0).show();
				break;

			}
		}

	};

	/**
	 * ������
	 */
	private void checkUpdate() {

		new Thread() {
			public void run() {
				Message mes = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.serverurl).trim());
					// ����
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if (code == 200) {
						// �����ɹ�
						InputStream is = conn.getInputStream();
						// ����ת��String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "�����ɹ���" + result);
						// ʹ��gson����
						UpdateJson object = GsonUtils.jsonToBean(result,
								UpdateJson.class);
						String version = object.version;
						description = object.description;
						apkurl = object.apkurl;
						// json����
						// JSONObject obj = new JSONObject(result);
						// // �õ��������İ汾��Ϣ
						// String version = (String) obj.get("version");
						// description = (String) obj.get("description");
						// apkurl = (String) obj.get("apkurl");
						// У���Ƿ����°汾
						if (getVersionName().equals(version)) {
							// �汾һ�£�û���°汾��������ҳ��
							mes.what = ENTER_HOME;
						} else {
							// ���°汾������һ�����Ի���
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
					// ���ǻ��˶���ʱ��
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
		builder.setTitle("�°汾");
		builder.setMessage(description);
		// builder.setCancelable(false);//���ؼ���������
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// ������ҳ��
				dialog.dismiss();
				enterHome();
			}
		});
		builder.setPositiveButton("��������", new OnClickListener() {

			private HttpHandler hh;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����apk�����Ұ�װ
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sdcard����
					// afnal
					FinalHttp finalHttp = new FinalHttp();
					hh = finalHttp.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						private int progress = 0;
						private MyAlertDialog alertDialog;

						// ����ʧ�ܻص�
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(), "����ʧ��", 1)
									.show();
							enterHome();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onStart() {
							updateDialog();
							super.onStart();
						}

						// ���ؽ��Ȼص�
						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							progress = (int) (current * 100 / count);
							alertDialog.setMessage("�������أ������" + progress + "%");
							alertDialog.setProgress(progress);
						}

						// ���سɹ��ص�
						@Override
						public void onSuccess(final File t) {
							super.onSuccess(t);
							installAPK(t);
							alertDialog.setSingleButton(View.GONE);
							alertDialog.setPositiveButton("��װ",
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											installAPK(t);
										}
									});
							alertDialog.setNegativeButton("ȡ��",
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											alertDialog.setDismiss();
											enterHome();
										}
									});
						}

						// �������ȶԻ���
						private void updateDialog() {
							alertDialog = new MyAlertDialog(SplashActivity.this);
							alertDialog.setCancelable(false);
							alertDialog.setProgressBarVisibility(View.VISIBLE);
							alertDialog.setMessage("�������أ������" + progress + "%");
							alertDialog.setSingleButton("ȡ������",
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

						// ��װAPK
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
					Toast.makeText(getApplicationContext(), "û��sdcard���밲װ������",
							0).show();
					return;
				}
			}

		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();// ������ҳ��
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
	 * �õ�Ӧ�ó���İ汾
	 */
	private String getVersionName() {
		// �����ֻ���APP
		PackageManager packageManager = getPackageManager();
		try {
			// �õ�APP�����嵥�ļ�
			PackageInfo info = packageManager.getPackageInfo(getPackageName(),
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "���°�";
		}

	}

}
