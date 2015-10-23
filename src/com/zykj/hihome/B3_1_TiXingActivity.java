package com.zykj.hihome;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_TiXingActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private GridView gridView;
	private ListView mListView;
	private List<HashMap<String,String>> clocksList=new ArrayList<HashMap<String,String>>();
	private List<String> remindList = new ArrayList<String>();
	private String[] strs = new String[]{"正点","五分钟前","十分钟前","一小时之前","一天前","三天前","不提醒"};
	private CommonAdapter<HashMap<String,String>> clockAdapter;
	private CommonAdapter<String> addClockAdapter;
	private long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_tixing);
		startTime = System.currentTimeMillis();

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("提醒");
		myCommonTitle.setEditTitle("完成");
		
		gridView=(GridView) findViewById(R.id.clock_gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		mListView=(ListView) findViewById(R.id.list_tixing);
		mListView.setDividerHeight(0);
		
		for (int i = 0; i < 7; i++) {
			HashMap<String,String> map =new HashMap<String,String>();
			map.put("title", strs[i]);
			map.put("show", i==0?"1":"0");
			clocksList.add(map);
		}
		
		clockAdapter = new CommonAdapter<HashMap<String,String>>(B3_1_TiXingActivity.this, R.layout.ui_b3_check_item, clocksList) {
			@Override
			public void convert(final ViewHolder holder, final HashMap<String,String> ckockStr) {
				final TextView mTextView = holder.getView(R.id.check_item);
				if(Tools.M_SCREEN_WIDTH < 800){
					LayoutParams checkboxParms = mTextView.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 9;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 9;
				}
				
				mTextView.setSelected("1".equals(ckockStr.get("show")));
				mTextView.setText(ckockStr.get("title"));

				mTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(holder.getPosition() == clocksList.size()-1){
							for (int i = 0; i < clocksList.size()-1; i++) {
								clocksList.get(i).put("show", "0");
							}
							clocksList.get(clocksList.size()-1).put("show", "1");
						}else{
							ckockStr.put("show", "1".equals(ckockStr.get("show"))?"0":"1");
							clocksList.get(clocksList.size()-1).put("show", "1");
							for (int i = 0; i < clocksList.size()-1; i++) {
								if("1".equals(clocksList.get(i).get("show"))){
									clocksList.get(clocksList.size()-1).put("show", "0");
								}
							}
						}
						notifyDataSetChanged();
						notifyDataForListView();
					}
				});
			}
		};
		gridView.setAdapter(clockAdapter);
		
		addClockAdapter=new CommonAdapter<String>(B3_1_TiXingActivity.this, R.layout.ui_b3_1_item_add_clock, remindList) {
			@Override
			public void convert(ViewHolder holder, String str) {
				holder.setText(R.id.tv_tixing_time, StringUtil.toString(str.split(",")[0]))
						.setText(R.id.tv_tixing_datetime, StringUtil.toString(str.split(",")[1]));
			}
		};
		mListView.setAdapter(addClockAdapter);
		notifyDataForListView();
	}

	private void notifyDataForListView() {
		remindList.clear();
		for (int i = 0; i < clocksList.size()-1; i++) {
			if("1".equals(clocksList.get(i).get("show"))){
				try {
					long remindTime = i==0?startTime:i==1?startTime-300000:i==2?
							startTime-600000:i==3?startTime-3600000:i==4?startTime-86400000:startTime-259200000;
					String dateStr = DateUtil.longToString(remindTime, "yyyy-MM-dd HH:mm");
					remindList.add(clocksList.get(i).get("title")+"提醒"+","+dateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		addClockAdapter.notifyDataSetChanged();
	}
}
