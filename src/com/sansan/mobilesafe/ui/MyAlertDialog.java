package com.sansan.mobilesafe.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sansan.mobilesafe.R;


public class MyAlertDialog implements OnClickListener{

	private Context context;
	private ProgressBar pb_update;
	private TextView tv_message;
	private Button bt_cancle;
	private AlertDialog d;
	private TextView tv_title;
	private Button bt_ok;
	private View view = null;
	private AlertDialog.Builder builder;
	private Button bt_singlebutton;
	
	public MyAlertDialog(Context context) {
		super();
		this.context = context;
		init();
	}
	
	public TextView getTv_title() {
		return tv_title;
	}

	public Button getBt_singlebutton() {
		return bt_singlebutton;
	}

	public Button getBt_ok() {
		return bt_ok;
	}

	public Context getContext() {
		return context;
	}

	public ProgressBar getPb_update() {
		return pb_update;
	}

	public TextView getTv_message() {
		return tv_message;
	}

	public Button getBt_cancle() {
		return bt_cancle;
	}

	public AlertDialog getD() {
		return d;
	}
	
	public View getView() {
		return view;
	}

	/**
	 * 设置对话框布局，如果设置了，只有对话框的方法可使用，控件的方法勿使用，为空则设置默认自定义的对话框
	 */
	public void setView(View view) {
		this.view = view;
	}

	private void init() {
		builder = new Builder(getContext());
		
		if(getView() == null){
			view = View.inflate(getContext(), R.layout.update_dialog, null);
			
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			pb_update = (ProgressBar) view.findViewById(R.id.pb_update);
			tv_message = (TextView) view.findViewById(R.id.tv_message);
			bt_singlebutton = (Button) view.findViewById(R.id.bt_cancle_update);
			bt_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
			bt_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
			
			bt_singlebutton.setOnClickListener(this);
			bt_ok.setOnClickListener(this);
			bt_cancle.setOnClickListener(this);
		}
		
		
	}
	
	/**
	 * 设置单个按钮是否显示状态,默认显示
	 */
	public void setSingleButton(int visibility ){
		bt_singlebutton.setVisibility(visibility);
	}
	/**
	 * 设置对话框单个按钮，设置监听器后要设置对话框dismiss
	 */
	public void setSingleButton(String text, final OnClickListener listener){
		if(text != null || text != ""){
			bt_singlebutton.setText(text);
		}
		if(listener != null){
			bt_singlebutton.setOnClickListener(listener);
		}
	}
	/**
	 * 设置对话框确定按钮
	 */
	public void setPositiveButton(String text, final OnClickListener listener){
		if(text != null || text != ""){
			bt_ok.setText(text);
		}
		if(listener != null){
			bt_ok.setOnClickListener(listener);
		}
	}
	/**
	 * 设置对话框取消按钮
	 */
	public void setNegativeButton(String text, final OnClickListener listener){
		if(text != null || text != ""){
			bt_cancle.setText(text);
		}
		if(listener != null){
			bt_cancle.setOnClickListener(listener);
		}
	}
	/**
	 * 设置升级进度条是否显示状态，默认gone
	 */
	public void setProgressBarVisibility(int visibility ){
		pb_update.setVisibility(visibility);
	}
	/**
	 * 设置升级进度条的进度
	 */
	public void setProgress(int p ){
		pb_update.setProgress(p);
	}
	/**
	 * 设置信息是否显示状态,默认显示
	 */
	public void setMessageVisibility(int visibility ){
		tv_message.setVisibility(visibility);
	}
	/**
	 * 设置信息
	 */
	public void setMessage(String mes){
		tv_message.setText(mes);
	}
	/**
	 * 设置对话框标题是否显示状态，默认显示
	 */
	public void setTitleVisibility(int visibility ){
		tv_title.setVisibility(visibility);
	}
	/**
	 * 设置对话框标题
	 */
	public void setTitle(String title ){
		tv_title.setText(title);
	}
	/**
	 * 设置对话框是否消失
	 */
	public void setDismiss(){
		d.dismiss();
	}
	/**
	 * 设置对话框填充的布局View
	 */
	public void setDialogView(View view){
		d.setView(view, 0, 0, 0, 0);
	}
	/**
	 * 设置返回键不可以取消对话框，默认为TRUE
	 */
	public void setCancelable(Boolean b){
		builder.setCancelable(b);
	}
	/**
	 * 设置对话框创建显示
	 */
	public void show(){
		d = builder.create();
		setDialogView(view);
		d.show();
	}

	@Override
	public void onClick(View v) {
		setDismiss();
		
	}
	
//	final Dialog dialog = new Dialog(SplashActivity.this, R.style.dialog);
//	dialog.setCancelable(false);  
//	// 设置普通对话框的布局   
//	dialog.setContentView(R.layout.update_dialog);
//	pb_update = (ProgressBar) dialog.findViewById(R.id.pb_update);
//	tv_update = (TextView) dialog.findViewById(R.id.tv_update);
////	tv_jindu = (TextView) dialog.findViewById(R.id.tv_jindu);
//	bt_cancle = (Button) dialog.findViewById(R.id.bt_cancle_update);
//	bt_cancle.setOnClickListener(new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			hh.stop();
//			dialog.dismiss();
//			enterHome();
//		}
//	});
//	dialog.show();
	

}
