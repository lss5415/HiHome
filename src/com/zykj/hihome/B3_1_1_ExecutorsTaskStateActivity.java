package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_1_ExecutorsTaskStateActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ListView mListView;
	private List<Task> tasks = new ArrayList<Task>();
	private CommonAdapter<Task> executorAdapter;
	private List<Friend> friends;
	private Friend friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_1_executor_taskstate);
		friends = JSONArray.parseArray(getIntent().getStringExtra("tasker"),
				Friend.class);
		// JSON.parseArray(getIntent().getStringExtra("tasker"));
		// task = getIntent().getStringExtra("tasker");
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.list_excutor_taskstate);
		int state = Integer.valueOf(friend.getState());
		final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
				: state == 2 ? "待执行" : state == 3 ? "已执行" : state == 4 ? "已完成"
						: "已取消";
		RequestParams params = new RequestParams();
		params.put("id", friend.getId());
		HttpUtils.getTasksInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				org.json.JSONArray jsonObject;
				try {
					jsonObject = response.getJSONArray("datas")
							.getJSONObject(0).getJSONArray("tasker");
					friends = JSON.parseArray(jsonObject.toString(),Friend.class);

					executorAdapter = new CommonAdapter<Task>(
							B3_1_1_ExecutorsTaskStateActivity.this,
							R.layout.ui_b3_item_executortaskstate, tasks) {
						@Override
						public void convert(ViewHolder holder, Task task) {
							holder.setText(R.id.item_excutor_name,friend.getNick())
									.setText(R.id.item_excutor_state, statu)
									.setImageUrl(R.id.item_excutor_avator,StringUtil.toString(HttpUtils.IMAGE_URL+ friend.getAvatar(),"http://"), 10f);
						}
					};
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, params);
	}
}
