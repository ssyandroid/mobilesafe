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
	 * ���öԻ��򲼾֣���������ˣ�ֻ�жԻ���ķ�����ʹ�ã��ؼ��ķ�����ʹ�ã�Ϊ��������Ĭ���Զ���ĶԻ���
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
	 * ���õ�����ť�Ƿ���ʾ״̬,Ĭ����ʾ
	 */
	public void setSingleButton(int visibility ){
		bt_singlebutton.setVisibility(visibility);
	}
	/**
	 * ���öԻ��򵥸���ť�����ü�������Ҫ���öԻ���dismiss
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
	 * ���öԻ���ȷ����ť
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
	 * ���öԻ���ȡ����ť
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
	 * ���������������Ƿ���ʾ״̬��Ĭ��gone
	 */
	public void setProgressBarVisibility(int visibility ){
		pb_update.setVisibility(visibility);
	}
	/**
	 * ���������������Ľ���
	 */
	public void setProgress(int p ){
		pb_update.setProgress(p);
	}
	/**
	 * ������Ϣ�Ƿ���ʾ״̬,Ĭ����ʾ
	 */
	public void setMessageVisibility(int visibility ){
		tv_message.setVisibility(visibility);
	}
	/**
	 * ������Ϣ
	 */
	public void setMessage(String mes){
		tv_message.setText(mes);
	}
	/**
	 * ���öԻ�������Ƿ���ʾ״̬��Ĭ����ʾ
	 */
	public void setTitleVisibility(int visibility ){
		tv_title.setVisibility(visibility);
	}
	/**
	 * ���öԻ������
	 */
	public void setTitle(String title ){
		tv_title.setText(title);
	}
	/**
	 * ���öԻ����Ƿ���ʧ
	 */
	public void setDismiss(){
		d.dismiss();
	}
	/**
	 * ���öԻ������Ĳ���View
	 */
	public void setDialogView(View view){
		d.setView(view, 0, 0, 0, 0);
	}
	/**
	 * ���÷��ؼ�������ȡ���Ի���Ĭ��ΪTRUE
	 */
	public void setCancelable(Boolean b){
		builder.setCancelable(b);
	}
	/**
	 * ���öԻ��򴴽���ʾ
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
//	// ������ͨ�Ի���Ĳ���   
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
