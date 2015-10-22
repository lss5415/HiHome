package com.zykj.hihome;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.ImageUtil;
import com.zykj.hihome.utils.SharedPreferenceUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsReceiveTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private List<Task> tasks;
	private CommonAdapter<Task> taskAdapter;
	private GridView gv_tasker;
	private TextView task_state, task_name, task_publish_name,
			task_excutor_name, task_content, task_starttime, task_finishtime,task_excutor_num;
	private ImageView task_excutor_avator, task_publish_avator;
	private Button btn_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_receivetask);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setTitle("任务名字");
		task_state = (TextView) findViewById(R.id.details_receivetask_state);
		task_name = (TextView) findViewById(R.id.details_receivetask_name);// 任务名称
		task_publish_name = (TextView) findViewById(R.id.details_receivetask_publisher_name);// 任务发布人头像
		task_publish_avator = (ImageView) findViewById(R.id.details_receivetask_publisher_avator);// 任务发布人姓名
		task_excutor_name = (TextView) findViewById(R.id.details_receivetask_excutor_name);// 任务执行人姓名
		task_excutor_avator = (ImageView) findViewById(R.id.details_receivetask_excutor_avator);// 任务执行人姓名
		task_content = (TextView) findViewById(R.id.details_receivetask_content);// 任务内容
		task_starttime = (TextView) findViewById(R.id.details_receivetask_starttime);// 开始时间
		task_finishtime = (TextView) findViewById(R.id.details_receivetask_finishtime);// 结束时间
		task_excutor_num=(TextView) findViewById(R.id.task_excutor_num);
		gv_tasker=(GridView) findViewById(R.id.gv_tasker);
		
		initializationDate();

	}

	private void initializationDate() {
		
		
		int state = Integer.valueOf(task.getState());
		task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
				: state == 2 ? "待执行" : state == 3 ? "已执行" : state == 4 ? "已完成"
						: "已取消");
		task_name.setText(task.getTitle());
		task_content.setText(task.getContent());
		task_starttime.setText(task.getStart());
		task_finishtime.setText(task.getEnd());
		task_publish_name.setText(task.getNick());
		task_excutor_num.setText(task.getTasker());
//		String pub_avator = task.getAvatar();
//		ImageLoader.getInstance().displayImage(StringUtil.isEmpty(SharedPreferenceUtils.getAvatar())?"":pub_avator, task_publish_avator);
	HttpUtils.getTasksInfo(new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);

			try {
				JSONArray jsonArray=response.getJSONArray("datas").getJSONObject(0).getJSONArray("tasker");
				tasks=JSON.parseArray(jsonArray.toString(), Task.class);
				taskAdapter=new CommonAdapter<Task>(B3_1_DetailsReceiveTaskActivity.this,R.layout.ui_b3_1_item_multi_excutor,tasks) {
					
					@Override
					public void convert(ViewHolder holder, Task task) {
						holder.setText(R.id.tasker_excutor_name, task.getNick())
						.setText(R.id.tasker_excutor_state, task.getState())
						.setImageUrl(R.id.tasker_excutor_avator, StringUtil.toString(task.getAvatar(),""), 10f);
					}
				};
			} catch (JSONException e) {
				e.printStackTrace();
			}
			gv_tasker.setAdapter(taskAdapter);
			taskAdapter.notifyDataSetChanged();
		}
		
	});
	
	}
}
