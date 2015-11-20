package com.zykj.hihome;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.EntityHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

/**
 * @author LSS 2015年10月19日 下午4:32:56 周任务总览
 */
public class B1_04_RiRenWuZongLan extends BaseActivity implements IXListViewListener, OnItemClickListener {
	
	private static int PERPAGE = 10;// 每页显示的任务数量为10
	private int nowpage = 1;// 当前显示的页
	private MyCommonTitle myCommonTitle;
	private TextView tv_zijirenwu, tv_jieshourenwu, aci_date;// 接受的任务
	private XListView task_listview;// 任务列表
	private Handler mHandler;
	private String isMySelfTask = "1";// 为1时是自己的任务,为空时是接受的任务
	private String sdate = "";// 获取时间段任务的开始时间
	private CommonAdapter<Task> adapter;
	private List<Task> tasks = new ArrayList<Task>();
	private String[] stateStr = new String[] { "未接受", "已接受", "待执行", "执行中", "已完成", "已取消" };
	private String[] tipStr = new String[] { "不提醒", "正点", "五分钟", "十分钟", "一小时", "一天", "三天" };
	private String[] repeatStr = new String[] { "不重复", "每天", "每周", "每月", "每年" };
	private Friend friend;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_02_zhourenwuzonglan);
		friend = (Friend)getIntent().getSerializableExtra("friend");
		mHandler = new Handler();
		try {
			long nowTime = System.currentTimeMillis();// 获取当前时间
			sdate = DateUtil.longToString(nowTime, "yyyy-MM-dd");// 当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		initView();
		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("今日任务");

		tv_zijirenwu = (TextView) findViewById(R.id.tv_zijirenwu);// 自己的任务
		tv_jieshourenwu = (TextView) findViewById(R.id.tv_jieshourenwu);// 接受的任务
		aci_date = (TextView) findViewById(R.id.aci_date);// 日期
		aci_date.setText(sdate);
		task_listview = (XListView) findViewById(R.id.aci_listview);// 任务列表

		adapter = new CommonAdapter<Task>(this, R.layout.ui_b1_03_zonglan_item, tasks) {
			@Override
			public void convert(ViewHolder holder, Task task) {
				String datastart = StringUtil.isEmpty(task.getStart()) ? "00-00" : task.getStart().substring(5, 10);
				String dataend = StringUtil.isEmpty(task.getEnd()) ? "00-00" : task.getEnd().substring(5, 10);
				int position1 = Integer.valueOf(task.getState());
				int position2 = Integer.valueOf(task.getTip());
				int position3 = Integer.valueOf(task.getRepeat());
				holder.setText(R.id.date, Html.fromHtml("<font color=#EA5414>" + datastart + "</font><br><small>~" + dataend + "</small>"))
						.setText(R.id.title, task.getTitle())
						.setText(R.id.aci_state, stateStr[position1])
						.setText(R.id.aci_tip, tipStr[position2])
						.setText(R.id.aci_repeat, repeatStr[position3]);
			}
		};
		task_listview.setAdapter(adapter);
		task_listview.setOnItemClickListener(this);
		task_listview.setDividerHeight(0);
		task_listview.setPullRefreshEnable(true);
		task_listview.setPullLoadEnable(true);
		task_listview.setXListViewListener(this);
		task_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		setListener(tv_zijirenwu, tv_jieshourenwu);
	}

	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("nowpage", nowpage);
		params.put("perpage", PERPAGE);
		params.put("uid", friend.getId());
		params.put("my", isMySelfTask);// 为1时是自己的任务,为空时是接受的任务
		params.put("sdate", sdate);// 获取时间段任务的开始时间
		params.put("edate", sdate);// 获取时间段任务的结束时间
		HttpUtils.getMyTasks(res_getMyTasks, params);
	}

	private AsyncHttpResponseHandler res_getMyTasks = new EntityHandler<Task>(
			Task.class) {
		@Override
		public void onReadSuccess(List<Task> list) {
			if(nowpage==1){
				tasks.clear();
			}
			tasks.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_zijirenwu:
			if (StringUtil.isEmpty(isMySelfTask)) {
				tv_jieshourenwu.setTextColor(Color.BLACK);
				tv_zijirenwu.setTextColor(getResources().getColor(
						R.color.theme_color));
				isMySelfTask = "1";
				requestData();
			}
			break;
		case R.id.tv_jieshourenwu:
			if (!StringUtil.isEmpty(isMySelfTask)) {
				tv_zijirenwu.setTextColor(Color.BLACK);
				tv_jieshourenwu.setTextColor(getResources().getColor(
						R.color.theme_color));
				isMySelfTask = "";
				requestData();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View converView,
			int position, long id) {
		Task task = tasks.get(position - 1);
		if (!StringUtil.isEmpty(isMySelfTask)) {
			startActivity(new Intent(this, B3_31_DetailsSelfTaskActivity.class)
					.putExtra("task", task));// 1 自己的任务
		} else {
			startActivity(new Intent(this,
					B3_32_DetailsReceiveTaskActivity.class).putExtra("task",
					task));// 2 接受的任务
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				nowpage = 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				nowpage += 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	public void onLoad() {
		task_listview.stopRefresh();
		task_listview.stopLoadMore();
		task_listview.setRefreshTime("刚刚");
	}
}