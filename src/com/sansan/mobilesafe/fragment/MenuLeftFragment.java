package com.sansan.mobilesafe.fragment;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sansan.mobilesafe.R;

public class MenuLeftFragment extends Fragment {
	
	private View mView;  
    private ListView mCategories;  
    private List<String> mDatas = Arrays.asList("清理", "通讯", "软件", "防盗", "工具");  
    private ListAdapter mAdapter;  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        if (mView == null)  
        {  
            initView(inflater, container);  
        }  
        return mView;  
    }  
  
    private void initView(LayoutInflater inflater, ViewGroup container)  
    {  
        mView = inflater.inflate(R.layout.left_menu_content, container, false);  
        mCategories = (ListView) mView.findViewById(R.id.lv_categories);  
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas);  
        mCategories.setAdapter(mAdapter); 
        mCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), mDatas.get(position), Toast.LENGTH_SHORT).show();
				
			}
		});
    }  
}
