package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.os.Bundle;
import android.widget.GridView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_1_TiXingActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private GridView gridView;
	private List<String> clocksList=new ArrayList<String>();
	private CommonAdapter<String > clockAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_1_tixing);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("提醒");
		myCommonTitle.setEditTitle("完成");
		
		clocksList.add("正点");
		clocksList.add("五分钟前");
		clocksList.add("十分钟前");
		clocksList.add("一小时之前");
		clocksList.add("一天前");
		clocksList.add("三天前");
		clocksList.add("不提醒");
		gridView=(GridView) findViewById(R.id.clock_gridview);
		
		clockAdapter=new CommonAdapter<String>(B3_1_1_TiXingActivity.this,R.layout.ui_b3_1_item_clock ,clocksList) {
			
			@Override
			public void convert(ViewHolder holder,String t) {
				
				holder.setText(R.id.textView1, StringUtil.toString(clocksList.get(holder.getPosition())));
			}
		};
		gridView.setAdapter(clockAdapter);
		
	}

}
