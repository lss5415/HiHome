package com.zykj.hihome;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.EntityHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author LSS 2015年10月19日 下午4:33:47
 * 月任务总览
 */
public class B1_03_YueRenWuZongLan extends BaseActivity implements OnItemClickListener{
	
	private MyCommonTitle myCommonTitle;
	private TextView tv_zijirenwu,tv_jieshourenwu,aci_date;//接受的任务
	private ListView aci_listview;//任务列表
	private String isMySelfTask = "1";//为1时是自己的任务,为空时是接受的任务
	private String sdate = "";//获取时间段任务的开始时间
	private String edate = "";//获取时间段任务的结束时间
	private CommonAdapter<Task> adapter;
	private List<Task> tasks = new ArrayList<Task>();
	private String[] stateStr = new String[]{"未接受", "已接受", "待执行", "执行中", "已完成", "已取消"};
	private String[] tipStr = new String[]{"不提醒", "正点", "五分钟", "十分钟", "一小时", "一天", "三天"};
	private String[] repeatStr = new String[]{"不重复", "每天", "每周", "每月", "每年"};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_03_yuerenwuzonglan);
		
        Calendar cal=Calendar.getInstance();//获取当前日期 
        cal.add(Calendar.MONTH, 1);
        sdate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");//当前月
		edate = DateUtil.dateToString(cal.getTime(), "yyyy-MM")+sdate.substring(7);//下月
		
		initView();
		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("月任务总览");
		
		tv_zijirenwu = (TextView)findViewById(R.id.tv_zijirenwu);//自己的任务
		tv_jieshourenwu = (TextView)findViewById(R.id.tv_jieshourenwu);//接受的任务
		aci_date = (TextView)findViewById(R.id.aci_date);//日期
		aci_date.setText(sdate);
		aci_listview = (ListView)findViewById(R.id.aci_listview);//任务列表
		aci_listview.setDivider(new ColorDrawable(0xffb5884d));
		aci_listview.setDividerHeight(1);
		adapter = new CommonAdapter<Task>(this, R.layout.ui_b1_03_zonglan_item, tasks) {
			@Override
			public void convert(ViewHolder holder, Task task) {
				String datastart = StringUtil.isEmpty(task.getStart()) ? "00-00": task.getStart().substring(5, 10);
				String dataend = StringUtil.isEmpty(task.getEnd()) ? "00-00" : task.getEnd().substring(5, 10);
				int position1 = Integer.valueOf(task.getState());
				int position2 = Integer.valueOf(task.getTip());
				int position3 = Integer.valueOf(task.getRepeat());
				holder.setText(R.id.date, Html.fromHtml("<font color=#EA5414>"+datastart+"</font><br><small>-"+dataend+"</small>"))
						.setText(R.id.title, task.getTitle())
						.setText(R.id.aci_state, stateStr[position1])
						.setText(R.id.aci_tip, tipStr[position2])
						.setText(R.id.aci_repeat, repeatStr[position3]);
			}
		};
		aci_listview.setAdapter(adapter);
		aci_listview.setOnItemClickListener(this);
		setListener(tv_zijirenwu,tv_jieshourenwu);
	}

	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("my", isMySelfTask);//为1时是自己的任务,为空时是接受的任务
		params.put("sdate", sdate);//获取时间段任务的开始时间
		params.put("edate", edate);//获取时间段任务的结束时间
		HttpUtils.getMyTasks(res_getMyTasks, params);
	}
	
	private AsyncHttpResponseHandler res_getMyTasks = new EntityHandler<Task>(Task.class) {
		@Override
		public void onReadSuccess(List<Task> list) {
			tasks.clear();
			tasks.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_zijirenwu:
			if(StringUtil.isEmpty(isMySelfTask)){
				tv_jieshourenwu.setTextColor(Color.BLACK);
				tv_zijirenwu.setTextColor(getResources().getColor(R.color.theme_color));
				isMySelfTask = "1";
				requestData();
			}
			break;
		case R.id.tv_jieshourenwu:
			if(!StringUtil.isEmpty(isMySelfTask)){
				tv_zijirenwu.setTextColor(Color.BLACK);
				tv_jieshourenwu.setTextColor(getResources().getColor(R.color.theme_color));
				isMySelfTask = "";
				requestData();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View converView, int position, long id) {
		Task task = tasks.get(position);
		if(!StringUtil.isEmpty(isMySelfTask)){
			startActivity(new Intent(this, B3_1_DetailsSelfTaskActivity.class)
					.putExtra("task", task));// 1 自己的任务
		}else{
			startActivity(new Intent(this, B3_1_DetailsReceiveTaskActivity.class)
					.putExtra("task", task));// 2 接受的任务
		}
	}
}