package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_RepeatActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private GridView gridView;
	private List<String> repeatList=new ArrayList<String>();
	private ListView mListView;
	private CommonAdapter<String > repeatAdapter,addRepeatAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_tixing);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("重复");
		myCommonTitle.setEditTitle("完成");
		
		gridView=(GridView) findViewById(R.id.clock_gridview);
		
		mListView=(ListView) findViewById(R.id.list_tixing);
		
		repeatList.add("不重复");
		repeatList.add("每天");
		repeatList.add("每周");
		repeatList.add("每月");
		repeatList.add("每年");
		
		
		repeatAdapter=new CommonAdapter<String>(B3_1_RepeatActivity.this,R.layout.ui_b3_1_item_clock ,repeatList) {
			
			@Override
			public void convert(ViewHolder holder,String t) {
				
				holder.setText(R.id.textView1, StringUtil.toString(repeatList.get(holder.getPosition())));
			}
		};
		gridView.setAdapter(repeatAdapter);
		
		addRepeatAdapter=new CommonAdapter<String>(B3_1_RepeatActivity.this,R.layout.ui_b3_1_item_add_clock,getData()) {
			
			@Override
			public void convert(ViewHolder holder, String t) {
				holder.setText(R.id.tv_tixing_time, StringUtil.toString(repeatList.get(holder.getPosition())+"提醒："));
//				.setText(R.id.tv_tixing_datetime,StringUtil.toString(obj);
			}
		};
	}

	private List<String> getData() {
		return null;
	}

}
