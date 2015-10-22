package com.zykj.hihome.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.B3_1_DetailsPublishTaskActivity;
import com.zykj.hihome.B3_1_DetailsReceiveTaskActivity;
import com.zykj.hihome.B3_1_DetailsSelfTaskActivity;
import com.zykj.hihome.adapter.TaskAdapter;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;

public class TaskFragment extends Fragment implements OnItemClickListener{

    private ListView mListView;
	private int mType=1;//1 自己的任务 2 接受的任务 3 发布的任务
	private TaskAdapter adapter;
	private List<Task> tasks = new ArrayList<Task>();
    
	public static TaskFragment getInstance(int type){
		TaskFragment fragment=new TaskFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("type", type);//1 自己的任务 2 接受的任务 3 发布的任务
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView = new ListView(getActivity(), null);
        mListView.setLayoutParams(params);
		mListView.setDividerHeight(0);
        return mListView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle arguments = getArguments();
		mType=arguments.getInt("type");
		if(mType == 1){
			tasks.add(0, new Task("圣诞节", "2015年12月25日"));
			tasks.add(0, new Task("元旦", "2016年1月1日"));
		}
		adapter = new TaskAdapter(getActivity(), tasks, mType);
//        adapter = new CommonAdapter<Task>(getActivity(), R.layout.ui_b3_item_task, tasks){
//			@Override
//			public void convert(ViewHolder holder, Task task) {
//				String datastart = StringUtil.isEmpty(task.getStart())?"00-00":task.getStart().substring(5, 10);
//				String dataend = StringUtil.isEmpty(task.getEnd())?"00-00":task.getEnd().substring(5, 10);
//				int tip = Integer.valueOf(task.getTip());
//				int repeat = Integer.valueOf(task.getRepeat());
//				int state = Integer.valueOf(task.getState());
//				holder.setText(R.id.date, Html.fromHtml("<big><font color=#EA5414>"+datastart+"</font></big><br>-"+dataend))
//						.setText(R.id.task_title, task.getTitle())
//						.setText(R.id.task_time, tip==0?"不提醒":tip==1?"正点":tip==2?"五分钟":
//													tip==3?"十分钟":tip==4?"一小时":tip==5?"一天":"三天")
//						.setText(R.id.task_repeat, repeat==0?"不重复":repeat==1?"每天":repeat==2?"每周":repeat==3?"每月":"每年")
//						.setText(R.id.task_tasker, "发布人："+task.getTasker())
//						.setVisibility(R.id.task_tasker, mType==2)
//						.setText(R.id.task_num, mType==2?task.getTasker()+"人":"张三")
//						.setVisibility(R.id.task_num, mType!=1)
//						.setText(R.id.task_state, state==0?"未接受":state==1?"已接受":state==2?"待执行":state==3?"执行中":state==4?"已完成":"已取消");
//			}
//        };
        mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
        requestData();
	}

	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("uid", "1");
//		params.put("nowpage", nowpage);
//		params.put("perpage", PERPAGE);
		HttpUtils.getTasks(res_getTasks, params);
	}
	
	private JsonHttpResponseHandler res_getTasks = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				int code = response.getInt("code");
				if(code == 200){
					JSONArray JSONArray = response.getJSONObject("datas").getJSONArray("list");
					tasks.addAll(JSON.parseArray(JSONArray.toString(), Task.class));
					adapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Tools.toast(getActivity(), "网络有问题!");
		}
	};
	
	/**
	 * listview 点击事件 
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Task task = tasks.get(position);
		if(mType == 1){
			if(!StringUtil.isEmpty(task.getState())){
				startActivity(new Intent(getActivity(), B3_1_DetailsSelfTaskActivity.class).putExtra("task", task));//1 自己的任务
			}else{
				Tools.toast(getActivity(), task.getTitle());
			}
		}else if(mType == 2){
			startActivity(new Intent(getActivity(), B3_1_DetailsReceiveTaskActivity.class).putExtra("task", task));//2 接受的任务
		}else{
			startActivity(new Intent(getActivity(), B3_1_DetailsPublishTaskActivity.class).putExtra("task", task));//3 发布的任务
		}
	}
}

