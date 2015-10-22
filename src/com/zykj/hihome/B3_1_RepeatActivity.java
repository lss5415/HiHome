package com.zykj.hihome;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_RepeatActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private GridView gridView;
	private List<String> repeatList=new ArrayList<String>();
	private boolean[] flags = new boolean[5];
	private TextView repeatType,repeatTime;
	private CommonAdapter<String > repeatAdapter;
	private long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_chongfu);
		startTime = System.currentTimeMillis();

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("重复");
		myCommonTitle.setEditTitle("完成");
		
		gridView=(GridView) findViewById(R.id.clock_gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		repeatType = (TextView) findViewById(R.id.tv_tixing_time);
		repeatTime = (TextView) findViewById(R.id.tv_tixing_datetime);
		
		repeatList.add("不重复");
		repeatList.add("每天");
		repeatList.add("每周");
		repeatList.add("每月");
		repeatList.add("每年");
		
		repeatAdapter=new CommonAdapter<String>(B3_1_RepeatActivity.this,R.layout.ui_b3_check_item ,repeatList) {
			@Override
			public void convert(final ViewHolder holder,String str) {
				final TextView mTextView = holder.getView(R.id.check_item);
				if(Tools.M_SCREEN_WIDTH < 800){
					LayoutParams checkboxParms = mTextView.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 9;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 9;
				}
				
				mTextView.setSelected(flags[holder.getPosition()]);
				mTextView.setText(str);

				mTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(!flags[holder.getPosition()]){
							for (int i = 0; i < flags.length; i++) {
								flags[i] = false;
							}
							flags[holder.getPosition()] = true;
							notifyDataSetChanged();
							notifyDataForListView();
						}
					}
				});
			}
		};
		gridView.setAdapter(repeatAdapter);
	}

	private void notifyDataForListView() {
		try {
			if(flags[0]){
				repeatType.setText("不重复");
				repeatTime.setVisibility(View.GONE);
			}else if(flags[1]){
				repeatType.setText("每天");
				repeatTime.setVisibility(View.VISIBLE);
				repeatTime.setText(DateUtil.longToString(startTime, "HH:mm")+"重复");
			}else if(flags[2]){
				repeatType.setText("每周");
				repeatTime.setVisibility(View.VISIBLE);
				repeatTime.setText(getWeekOfDate(new Date(startTime))+"重复");
			}else if(flags[3]){
				repeatType.setText("每月");
				repeatTime.setVisibility(View.VISIBLE);
				repeatTime.setText(DateUtil.longToString(startTime, "dd日")+"重复");
			}else {
				repeatType.setText("每年");
				repeatTime.setVisibility(View.VISIBLE);
				repeatTime.setText(DateUtil.longToString(startTime, "MM月dd日")+"重复");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
