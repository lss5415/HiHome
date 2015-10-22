package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_1_ExecutorsTaskStateActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private ListView mListView;
	private List<Task> tasks=new ArrayList<Task>();
	private CommonAdapter<Task> executorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_1_executor_taskstate);
		
		initView();
	}

	private void initView() {
		mListView=(ListView) findViewById(R.id.list_excutor_taskstate);
		
		RequestParams params=new RequestParams();
		params.put("id", task.getId());
				HttpUtils.getTasksInfo(new JsonHttpResponseHandler(){

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						
						try {
							JSONArray jsonObject=response.getJSONArray("datas").getJSONObject(0).getJSONArray("tasker");
							tasks=JSON.parseArray(jsonObject.toString(),Task.class);
							executorAdapter=new CommonAdapter<Task>(B3_1_1_ExecutorsTaskStateActivity.this,R.layout.ui_b3_item_executortaskstate,tasks) {
								@Override
								public void convert(ViewHolder holder, Task task) {
									
								}
							};
						
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

			
					
					
				}, params);
	}

}
