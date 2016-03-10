package com.sansan.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sansan.mobilesafe.R;

public class SettingTextButtonView extends RelativeLayout{

	private ImageView iv_tb;
	private TextView tv_desc;
	private TextView tv_title;
	public  String desc_on = "";
	private String desc_off = "";
	private boolean isChecked = false;
//	private int backgroundId = R.drawable.ug;
//	private int slideBtnId = R.drawable.wg;
	/**
	 * 初始化布局文件
	 * @param context
	 */
	private void iniView(Context context) {
		
		//把一个布局文件---》View 并且加载在SettingItemView
		View.inflate(context, R.layout.seting_textbutton, this);
//		iv_tb =  (ImageView) this.findViewById(R.id.iv_tb);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		
	}

	public SettingTextButtonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	public SettingTextButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		//获得自定义属性,这样可以不用初始化图片
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextView);
		int count = ta.getIndexCount();
		for (int i = 0; i < count; i++) {
			//获得某个属性的ID值
			int id = ta.getIndex(i);
			switch (id) {
			case R.styleable.TextView_title:
				tv_title.setText(ta.getString(id));
				break;
			case R.styleable.TextView_desc_on:
				desc_on = ta.getString(id);
				break;
//			case R.styleable.TextView_desc_off:
//				desc_off = ta.getString(id);
//				break;
//			case R.styleable.TextView_image_on:
//				backgroundId = ta.getResourceId(id, R.drawable.ug);
//				break;
//			case R.styleable.TextView_image_off:
//				slideBtnId = ta.getResourceId(id, R.drawable.wg);
//				break;
			case R.styleable.TextView_isCheck:
				isChecked = ta.getBoolean(id, false);
//				setChecked(isChecked);
				break;
			}
		}
		ta.recycle();
//		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.sansan.mobilesafe", "title");
//		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.sansan.mobilesafe", "desc_on");
	}

	public SettingTextButtonView(Context context) {
		super(context);
		iniView(context);
	}
	
	/**
	 * 校验组合控件是否选中
	 */
	
	public boolean isChecked(){
		return isChecked ;
	}
	
	/**
	 * 设置组合控件的状态
	 */
//	public void setChecked(boolean checked){
//		if(checked){
//			setDesc(desc_on);
////			setImageView(backgroundId);
//		}else{
//			setDesc(desc_off);
////			setImageView(slideBtnId);
//		}
//		isChecked = checked;
//	}
	
	private void setImageView(int id) {
		iv_tb.setBackgroundResource(id);
	}

	/**
	 * 设置 组合控件的描述信息
	 */
	
	public void setDesc(String text){
		if(text != ""){
			tv_desc.setText(text);
		}else{
			tv_desc.setVisibility(View.GONE);
		}
		
	}

}
