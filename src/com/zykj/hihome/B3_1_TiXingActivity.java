package com.zykj.hihome;

import java.text.ParseException;
import java.util.ArrayList;
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
	private List<String> clocksList=new ArrayList<String>();
	private boolean[] flags = new boolean[7];//{false,false,false,false,false,false,false}
	private List<String> remindList = new ArrayList<String>();
	private String[] strs = new String[]{"正点","五分钟前","十分钟前","一小时之前","一天前","三天前","不提醒"};
	private CommonAdapter<String> clockAdapter;
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
			clocksList.add(strs[i]);
		}
		flags[0] = true;
		
		clockAdapter = new CommonAdapter<String>(B3_1_TiXingActivity.this, R.layout.ui_b3_check_item, clocksList) {
			@Override
			public void convert(final ViewHolder holder, final String ckockStr) {
				final TextView mTextView = holder.getView(R.id.check_item);
				if(Tools.M_SCREEN_WIDTH < 800){
					LayoutParams checkboxParms = mTextView.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 9;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 9;
				}
				
				mTextView.setSelected(flags[holder.getPosition()]);
				mTextView.setText(ckockStr);

				mTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						/*单选方案*/
						if(flags[holder.getPosition()]){
							flags[holder.getPosition()] = false;
							flags[6] = true;
						}else{
							for (int i = 0; i < flags.length; i++) {
								flags[i] = false;
							}
							flags[holder.getPosition()] = true;
						}
						/*多选方案*/
//						flags[holder.getPosition()] = !flags[holder.getPosition()];
//						if(holder.getPosition() == flags.length-1){
//							for (int i = 0; i < flags.length-1; i++) {
//								flags[i] = false;
//							}
//							flags[6] = true;
//						}
//						if(flags[0]||flags[1]||flags[2]||flags[3]||flags[4]||flags[5]){
//							flags[6] = false;
//						}else{
//							flags[6] = true;
//						}
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
			if(flags[i]){
				try {
					long remindTime = i==0?startTime:i==1?startTime-300000:i==2?
							startTime-600000:i==3?startTime-3600000:i==4?startTime-86400000:startTime-259200000;
					String dateStr = DateUtil.longToString(remindTime, "yyyy-MM-dd HH:mm");
					remindList.add(clocksList.get(i)+"提醒"+","+dateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		addClockAdapter.notifyDataSetChanged();
	}
}
