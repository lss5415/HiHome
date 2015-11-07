package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.adapter.TaskTiXingAdapter;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

public class B1_1_Task_Tixing extends BaseActivity implements
		IXListViewListener ,OnItemClickListener{
	private int PERPAGE = 10;//PERPAGE为每页显示的条数为10
	private int nowpage = 1;// 当前现实的页
	private MyCommonTitle myCommonTitle;
	private XListView mListView;
	private TaskTiXingAdapter txTaskAdapter;
	private Handler mHandler;
	private List<Task> tasks = new ArrayList<Task>();
	private List<Boolean> flags1 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags2 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags3 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b2_task_tixing);

		initView();
		getTaskListData();
	}

	private void initView() {
		mHandler = new Handler();
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("任务提醒");

		mListView = (XListView) findViewById(R.id.list_tasktx);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setDividerHeight(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setXListViewListener(this);
		
		mListView.setAdapter(txTaskAdapter);

	}

	/**
	 * 请求服务器数据
	 */
	private void getTaskListData() {
		RequestParams params = new RequestParams();

		params.put("nowpage", nowpage);
		params.put("perpage", PERPAGE);
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("my", "");// 为1时是自己的任务 为空时是接受的任务

		HttpUtils.getMyTasks(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				String strTask=json.getJSONObject(UrlContants.jsonData).getString("list");
				List<Task> list=JSONObject.parseArray(strTask, Task.class);
				if(nowpage==1){tasks.clear();}
				tasks.addAll(list);
				txTaskAdapter=new TaskTiXingAdapter(B1_1_Task_Tixing.this,tasks);
				mListView.setAdapter(txTaskAdapter);
				txTaskAdapter.notifyDataSetChanged();
				
			}
		}, params);
	}

	/**
	 * 更改任务状态
	 */
	private void modTaskState(String ssid, String state) {
		RequestParams params = new RequestParams();
		params.put("sid", ssid);
		params.put("state", state);

		HttpUtils.modTaskState(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
			}
		}, params);
	}

	/**
	 * 删除任务
	 * 
	 * @param tid
	 */
	private void deleteTask(String tid) {
		RequestParams params = new RequestParams();
		params.put("id", tid);
		HttpUtils.delTaskInfo(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B1_1_Task_Tixing.this, "此任务已删除");
				getTaskListData();
			}
		}, params);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		
	}
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				nowpage = 1;
				getTaskListData();
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
				getTaskListData();
				onLoad();
			}
		}, 1000);

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}



}
