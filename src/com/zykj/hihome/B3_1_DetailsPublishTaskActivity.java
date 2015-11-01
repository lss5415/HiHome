package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsPublishTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private LinearLayout ly_multi_excutor;
	private Task task;
	private List<Task> tasks;
	private GridView gv_tasker, button_gridview;
	private TextView task_state, task_name, task_publish_name,
			task_excutor_name, task_content, task_starttime, task_finishtime,
			task_excutor_num;
	private CommonAdapter<Task> taskAdapter;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType = new ArrayList<String>();
	private int[] imgResource = new int[] { R.drawable.ic_clock,
			R.drawable.ic_repeat, R.drawable.ic_dingwei };
	private boolean[] flags = new boolean[3];
	private ImageView task_publish_avator, task_excutor_avator, task_pic1,
			task_pic2, task_pic3;
	private Button leftButton, rightButon;
	private JSONArray jsonArray;
	private int state=0;
	private String taskstate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_publishtask);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setTitle("任务名字");

		task_pic1 = (ImageView) findViewById(R.id.task_pic_1);
		task_pic2 = (ImageView) findViewById(R.id.task_pic_2);
		task_pic3 = (ImageView) findViewById(R.id.task_pic_3);
		task_state = (TextView) findViewById(R.id.details_publictask_state);// 任务状态
		leftButton = (Button) findViewById(R.id.btn_leftButton);
		rightButon = (Button) findViewById(R.id.btn_rightButton);
		task_name = (TextView) findViewById(R.id.details_publishtaskname);// 任务名称
		task_publish_name = (TextView) findViewById(R.id.details_publishtask_publish_username);// 任务发布人头像
		task_publish_avator = (ImageView) findViewById(R.id.details_publishtask_publish_avator);// 任务发布人姓名
		task_excutor_name = (TextView) findViewById(R.id.tasker_excutor_name);// 任务执行人姓名
		task_excutor_avator = (ImageView) findViewById(R.id.tasker_excutor_avator);// 任务执行人姓名
		task_content = (TextView) findViewById(R.id.details_publishtaskcontent);// 任务内容
		task_starttime = (TextView) findViewById(R.id.details_publishtask_starttime);// 开始时间
		task_finishtime = (TextView) findViewById(R.id.details_publishtask_finishtime);// 结束时间
		task_excutor_num = (TextView) findViewById(R.id.tasker_excutor_num);// 执行人数量
		ly_multi_excutor=(LinearLayout) findViewById(R.id.ly_multi_excutor);
		gv_tasker = (GridView) findViewById(R.id.gv_tasker);
		gv_tasker.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉点击的产生的背景色

		setListener(ly_multi_excutor, leftButton, rightButon);// 设置监听事件

		button_gridview = (GridView) findViewById(R.id.button_gridview);
		button_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		taskType.add("提醒");
		taskType.add("重复");
		taskType.add("位置");
		btnAdapter = new CommonAdapter<String>(this,
				R.layout.ui_b3_addtask_check, taskType) {
			@Override
			public void convert(ViewHolder holder, String type) {
				RelativeLayout mLayout = holder.getView(R.id.check_relative);
				TextView mTextView = holder.getView(R.id.check_item);
				if (Tools.M_SCREEN_WIDTH < 800) {
					LayoutParams checkboxParms = mLayout.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 9;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 9;
				}
				mTextView.setText(type);
				Drawable topDrawable = getResources().getDrawable(
						imgResource[holder.getPosition()]);
				topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
						topDrawable.getMinimumHeight());
				if (!flags[holder.getPosition()]) {
					mTextView.setCompoundDrawables(null, topDrawable, null,
							null);
				} else {
					mTextView.setCompoundDrawables(null, null, null, null);
				}
			}
		};
		button_gridview.setAdapter(btnAdapter);

		requestData();
	}

	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("id", task.getId());
		HttpUtils.getTasksInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData).getJSONObject(0);
				jsonArray = jsonObject.getJSONArray("taskerlist");
				taskstate = jsonObject.getJSONArray("taskerlist").getJSONObject(0).getString("tasker_state");
				state = Integer.valueOf(taskstate);
				final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
						: state == 2 ? "待执行" : state == 3 ? "执行中"
								: state == 4 ? "已完成" : "已取消";
				stateAndButtonChange();
				task_state.setText(statu);
				task_name.setText(jsonObject.getString("title"));
				task_content.setText(jsonObject.getString("content"));
				if(jsonObject.getString("isday").equals("1")){
					task_starttime.setText(jsonObject.getString("start").substring(0, 11));
					task_finishtime.setText(jsonObject.getString("end").substring(0, 11));
				}else{
					task_starttime.setText(jsonObject.getString("start"));
					task_finishtime.setText(jsonObject.getString("end"));
				}
				task_publish_name.setText(task.getNick());
				task_excutor_num.setText(task.getTasker() + "人");
				ImageLoader.getInstance().displayImage(
						StringUtil.toString(
								HttpUtils.IMAGE_URL + task.getAvatar(),
								"http://"), task_publish_avator);

				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc1"))) {
					ImageLoader.getInstance().displayImage(
							StringUtil.toString(HttpUtils.IMAGE_URL
									+ task.getImgsrc1()), task_pic1);
				}
				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc2"))) {
					ImageLoader.getInstance().displayImage(
							StringUtil.toString(HttpUtils.IMAGE_URL
									+ task.getImgsrc2()), task_pic2);
				}
				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc3"))) {
					ImageLoader.getInstance().displayImage(
							StringUtil.toString(HttpUtils.IMAGE_URL
									+ task.getImgsrc3()), task_pic3);
				}
				initializationDate();
			}
		}, params);
	}

	private void initializationDate() {
		state = Integer.valueOf(taskstate);
		final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
				: state == 2 ? "待执行" : state == 3 ? "执行中" : state == 4 ? "已完成"
						: "已取消";

		tasks = JSON.parseArray(jsonArray.toString(), Task.class);
		taskAdapter = new CommonAdapter<Task>(
				B3_1_DetailsPublishTaskActivity.this,
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
				holder.setText(R.id.tasker_excutor_name, task.getNick())
						.setText(R.id.tasker_excutor_state, statu)
						.setImageUrl(
								R.id.tasker_excutor_avator,
								StringUtil.toString(
										HttpUtils.IMAGE_URL + task.getAvatar(),
										"http://"), 10f);
			}
		};
		gv_tasker.setAdapter(taskAdapter);
		taskAdapter.notifyDataSetChanged();
	}

	// state == 0 ? "未接受" : state == 1 ? "已接受"
	// : state == 2 ? "待执行" : state == 3 ? "执行中" : state == 4 ? "已完成"
	// : "已取消";
	private void stateAndButtonChange() {
		switch (state) {
		case 0:// 未接受
			leftButton.setText("接受任务");
			rightButon.setText("删除任务");
			state += 2;
			modTaskState();
			break;
		case 2:// 待执行
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("开始执行");
			rightButon.setText("取消任务");
			state += 1;
			modTaskState();
			break;
		case 3:// 执行中
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("标记完成");
			rightButon.setText("取消任务");
			state += 1;
			modTaskState();
			break;
		case 4:// 已完成
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("删除任务");
			rightButon.setVisibility(View.GONE);
			modTaskState();
			break;
		default:
			break;
		}
	}

	private void modTaskState() {
		RequestParams params = new RequestParams();
		params.put("sid", task.getId());
		params.put("state", state);
		HttpUtils.modTaskState(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(com.alibaba.fastjson.JSONObject json) {

			}
		}, params);

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_leftButton:
			stateAndButtonChange();

			break;
		case R.id.btn_rightButton:

			break;
		case R.id.ly_multi_excutor:
			startActivity(new Intent(this,
					B3_1_1_ExecutorsTaskStateActivity.class).putExtra("tasker",
					jsonArray.toString()));
			break;
		default:
			break;
		}
	}

}
