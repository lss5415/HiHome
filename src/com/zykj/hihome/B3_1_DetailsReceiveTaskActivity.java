package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsReceiveTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private List<Task> tasks;
	private CommonAdapter<Task> taskAdapter;
	private GridView gv_tasker,button_gridview;
	private LinearLayout mLinearLayout;
	private TextView task_state, task_name, task_publish_name,
			task_excutor_name, task_content, task_starttime, task_finishtime,
			task_excutor_num;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType=new ArrayList<String>();
	private int[] imgResource = new int[]{R.drawable.ic_clock,R.drawable.ic_repeat,R.drawable.ic_dingwei};
	private boolean[] flags = new boolean[3];
	private ImageView task_excutor_avator, task_publish_avator;
	private Button btn_delete;
	private JSONArray jsonArray;

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
		task_excutor_num = (TextView) findViewById(R.id.task_excutor_num);
		gv_tasker = (GridView) findViewById(R.id.gv_tasker);

		mLinearLayout = (LinearLayout) findViewById(R.id.ly_list_executor);
		mLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(B3_1_DetailsReceiveTaskActivity.this,
						B3_1_1_ExecutorsTaskStateActivity.class).putExtra("tasker", jsonArray.toString()));
			}
		});
		
		gv_tasker = (GridView) findViewById(R.id.gv_tasker);
		gv_tasker.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		
		button_gridview=(GridView) findViewById(R.id.button_gridview);
		button_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		taskType.add("提醒");
		taskType.add("重复");
		taskType.add("位置");
		btnAdapter = new CommonAdapter<String>(this, R.layout.ui_b3_addtask_check, taskType) {
			@Override
			public void convert(ViewHolder holder, String type) {
				final TextView mTextView = holder.getView(R.id.check_item);
				if(Tools.M_SCREEN_WIDTH < 800){
					LayoutParams checkboxParms = mTextView.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 10;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 10;
				}
				mTextView.setText(type);
				Drawable topDrawable = getResources().getDrawable(imgResource[holder.getPosition()]);
				topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
				if(!flags[holder.getPosition()]){
		            mTextView.setCompoundDrawables(null, topDrawable, null, null);
				}else{
		            mTextView.setCompoundDrawables(null, null, null, null);
				}
			}
		};
		button_gridview.setAdapter(btnAdapter);
		initializationDate();

	}

	private void initializationDate() {

		int state = Integer.valueOf(task.getState());
		final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
				: state == 2 ? "待执行" : state == 3 ? "已执行" : state == 4 ? "已完成"
						: "已取消";
		task_state.setText(statu);
		task_name.setText(task.getTitle());
		task_content.setText(task.getContent());
		task_starttime.setText(task.getStart());
		task_finishtime.setText(task.getEnd());
		task_publish_name.setText(task.getNick());
		task_excutor_num.setText(task.getTasker() + "人");
		ImageLoader.getInstance().displayImage(
				StringUtil.toString(HttpUtils.IMAGE_URL + task.getAvatar(),
						"http://"), task_publish_avator);

		RequestParams params = new RequestParams();
		params.put("id", task.getId());
		HttpUtils.getTasksInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					jsonArray = response.getJSONArray("datas")
							.getJSONObject(0).getJSONArray("tasker");
					tasks = JSON.parseArray(jsonArray.toString(), Task.class);
					taskAdapter = new CommonAdapter<Task>(
							B3_1_DetailsReceiveTaskActivity.this,
							R.layout.ui_b3_1_item_multi_excutor, tasks) {
						@Override
						public void convert(ViewHolder holder, Task task) {
							final LinearLayout mLinearLayout = holder
									.getView(R.id.ly_item_excutor);
							if (Tools.M_SCREEN_WIDTH < 800) {
								LayoutParams checkboxParms = mLinearLayout
										.getLayoutParams();
								checkboxParms.width = Tools.M_SCREEN_WIDTH * 3 / 10;
								checkboxParms.height = Tools.M_SCREEN_WIDTH * 3 / 10;
							}
							holder.setText(R.id.tasker_excutor_name,task.getNick())
									.setText(R.id.tasker_excutor_state, statu)
									.setImageUrl(
											R.id.tasker_excutor_avator,
											StringUtil.toString(
													HttpUtils.IMAGE_URL
															+ task.getAvatar(),
													"http://"), 10f);
						}
					};
				} catch (JSONException e) {
					e.printStackTrace();
				}
				gv_tasker.setAdapter(taskAdapter);
				taskAdapter.notifyDataSetChanged();
			}
		}, params);

	}
}
