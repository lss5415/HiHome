package com.zykj.hihome.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.B3_1_AnniversaryDetailsActivity;
import com.zykj.hihome.B3_1_DetailsPublishTaskActivity;
import com.zykj.hihome.B3_1_DetailsReceiveTaskActivity;
import com.zykj.hihome.B3_1_DetailsSelfTaskActivity;
import com.zykj.hihome.adapter.TaskAdapter;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.menuListView.SwipeMenu;
import com.zykj.hihome.menuListView.SwipeMenuCreator;
import com.zykj.hihome.menuListView.SwipeMenuItem;
import com.zykj.hihome.menuListView.SwipeMenuListView;
import com.zykj.hihome.menuListView.SwipeMenuListView.IXListViewListener;
import com.zykj.hihome.menuListView.SwipeMenuListView.OnMenuItemClickListener;
import com.zykj.hihome.utils.EntityHandler;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.XListView;

public class TaskFragment extends Fragment implements IXListViewListener,
		OnItemClickListener, OnMenuItemClickListener {

	private static int PERPAGE = 10;// perpager默认每页显示的条数
	private int nowpage = 0;// 当前显示的页面
	private int mType = 1;// 1 自己的任务 2 接受的任务 3 发布的任务
	private TaskAdapter adapter;
	private Task task;
	private List<Task> tasks = new ArrayList<Task>();
	private RequestParams params;
	private SwipeMenuListView mListView;
	private Handler mHandler;

	public static TaskFragment getInstance(int type) {
		TaskFragment fragment = new TaskFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);// 1 自己的任务 2 接受的任务 3 发布的任务
		fragment.setArguments(bundle);
		return fragment;
	}

	private SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
			// set item background
			openItem.setBackground(new ColorDrawable(Color
					.rgb(0xC9, 0xC9, 0xCE)));
			// set item width
			openItem.setWidth(dp2px(90));
			// set item title
			openItem.setTitle("删除");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);
		}
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mListView = new SwipeMenuListView(getActivity(), null);
		mListView.setLayoutParams(params);
		mListView.setDividerHeight(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setMenuCreator(creator);
		mListView.setXListViewListener(this);
		mListView.setOnMenuItemClickListener((OnMenuItemClickListener) this);
		return mListView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle arguments = getArguments();
		mType = arguments.getInt("type");
		mHandler = new Handler();
		if (mType == 1) {

		}
		adapter = new TaskAdapter(getActivity(), tasks, mType);
		// adapter = new CommonAdapter<Task>(getActivity(),
		// R.layout.ui_b3_item_task, tasks){
		// @Override
		// public void convert(ViewHolder holder, Task task) {
		// String datastart =
		// StringUtil.isEmpty(task.getStart())?"00-00":task.getStart().substring(5,
		// 10);
		// String dataend =
		// StringUtil.isEmpty(task.getEnd())?"00-00":task.getEnd().substring(5,
		// 10);
		// int tip = Integer.valueOf(task.getTip());
		// int repeat = Integer.valueOf(task.getRepeat());
		// int state = Integer.valueOf(task.getState());
		// holder.setText(R.id.date,
		// Html.fromHtml("<big><font color=#EA5414>"+datastart+"</font></big><br>-"+dataend))
		// .setText(R.id.task_title, task.getTitle())
		// .setText(R.id.task_time, tip==0?"不提醒":tip==1?"正点":tip==2?"五分钟":
		// tip==3?"十分钟":tip==4?"一小时":tip==5?"一天":"三天")
		// .setText(R.id.task_repeat,
		// repeat==0?"不重复":repeat==1?"每天":repeat==2?"每周":repeat==3?"每月":"每年")
		// .setText(R.id.task_tasker, "发布人："+task.getTasker())
		// .setVisibility(R.id.task_tasker, mType==2)
		// .setText(R.id.task_num, mType==2?task.getTasker()+"人":"张三")
		// .setVisibility(R.id.task_num, mType!=1)
		// .setText(R.id.task_state,
		// state==0?"未接受":state==1?"已接受":state==2?"待执行":state==3?"执行中":state==4?"已完成":"已取消");
		// }
		// };
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		requestData();
	}

	private void requestData() {
	
		if (mType == 1) {
			params = new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
			HttpUtils.getAnnversaryList(res_getAnnversaryList, params);// 获取纪念日列表
		} else if (mType == 2) {
			params = new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
//			params.put("nowpage", nowpage);
//			params.put("perpage", PERPAGE);
			params.put("my", "");
			HttpUtils.getMyTasks(res_getMyTasks, params);//获取自己接受任务列表
		} else if (mType == 3) {
			params = new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
//			params.put("nowpage", nowpage);
//			params.put("perpage", PERPAGE);
			HttpUtils.getPublishTaskList(res_getPublishTaskList, params);// 获取我发布的任务列表
		}

	}

	/**
	 * 获取纪念日列表
	 */
	private AsyncHttpResponseHandler res_getAnnversaryList = new EntityHandler<Task>(
			Task.class) {

		@Override
		public void onReadSuccess(List<Task> list) {
//			if (nowpage == 1) {
				tasks.clear();
//			}
			tasks.addAll(list);
			adapter.notifyDataSetChanged();
			
			params = new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
//			params.put("nowpage", nowpage);
//			params.put("perpage", PERPAGE);
			params.put("my", "1");
			HttpUtils.getMyTasks(res_getMyTasks, params);// 纪念日列表加载完成再加载自己的任务列表
		}
	};

	/**
	 * 获取我自己的任务
	 */
	private AsyncHttpResponseHandler res_getMyTasks = new EntityHandler<Task>(
			Task.class) {

		@Override
		public void onReadSuccess(List<Task> list) {
//			if (nowpage == 1) {
//				 tasks.clear();
//			}
			tasks.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};
	// private JsonHttpResponseHandler res_getMyTasks = new
	// JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// JSONObject response) {
	// super.onSuccess(statusCode, headers, response);
	// try {
	// int code = response.getInt("code");
	// if (code == 200) {
	// JSONArray JSONArray = response.getJSONObject("datas")
	// .getJSONArray("list");
	// tasks.addAll(JSON.parseArray(JSONArray.toString(),
	// Task.class));
	// adapter.notifyDataSetChanged();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers,
	// Throwable throwable, JSONObject errorResponse) {
	// super.onFailure(statusCode, headers, throwable, errorResponse);
	// Tools.toast(getActivity(), "网络有问题!");
	// }
	// };
	/**
	 * 获取我发布的任务
	 */
	private AsyncHttpResponseHandler res_getPublishTaskList = new EntityHandler<Task>(
			Task.class) {

		@Override
		public void onReadSuccess(List<Task> list) {
			if (nowpage == 1) {
				tasks.clear();
			}
			tasks.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * listview 点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Task task = tasks.get(position - 1);
		if (mType == 1) {
			if (!StringUtil.isEmpty(task.getState())) {
				startActivity(new Intent(getActivity(),
						B3_1_DetailsSelfTaskActivity.class).putExtra("task",
						task));// 1 自己的任务
			} else {
				startActivity(new Intent(getActivity(),
						B3_1_AnniversaryDetailsActivity.class).putExtra("task",
						task));
			}
		} else if (mType == 2) {
			startActivity(new Intent(getActivity(),
					B3_1_DetailsReceiveTaskActivity.class).putExtra("task",
					task));// 2 接受的任务
		} else {
			startActivity(new Intent(getActivity(),
					B3_1_DetailsPublishTaskActivity.class).putExtra("task",
					task));// 3 发布的任务
		}
	}

	@Override
	public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
		RequestParams params = new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("id", tasks.get(position).getId());
		HttpUtils.delAnnversaryInfo(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(com.alibaba.fastjson.JSONObject json) {
				tasks.remove(position);
				adapter.notifyDataSetChanged();
			}
		}, params);

		HttpUtils.delTaskInfo(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(com.alibaba.fastjson.JSONObject json) {
				// if (mType == 2||mType == 3) {// 提示接受的任务需要取消后才能删除
				// int state = Integer.valueOf(task.getState());
				// if (state == 1 || state == 2 || state == 3 || state == 4) {
				// Tools.toast(getActivity(), "请先取消任务再删除");
				// }
				// final String statu = state == 0 ? "未接受" : state == 1 ?
				// "已接受"
				// : state == 2 ? "待执行" : state == 3 ? "已执行" : state == 4 ?
				// "已完成": "已取消";
				if (mType == 3||mType==1) {
					tasks.remove(position);
					adapter.notifyDataSetChanged();
				}
			}
		}, params);

		return false;
	}

	/**
	 * 创建完成后 自动刷新列表
	 */
	public void reflush() {
		requestData();
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
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	// public void updatelist(int position, String state){
	// tasks.get(position).setState(state);
	// adapter.notifyDataSetChanged();
	// }

}
