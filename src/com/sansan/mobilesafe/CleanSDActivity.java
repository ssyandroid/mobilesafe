package com.sansan.mobilesafe;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class CleanSDActivity extends Activity {
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textView = new TextView(getApplicationContext());
		File file = Environment.getExternalStorageDirectory();
		File[] files = file.listFiles();
		for(File f: files){
			String name = f.getName();
			if(f.isDirectory()){
				System.out.println(name);
				textView.append(name+"\n");
				//查询这个文件夹名称是否在数据库里面存在 如果存在 提示用户把文件夹删除了。
			}
		}
		setContentView(textView);
	}
}
