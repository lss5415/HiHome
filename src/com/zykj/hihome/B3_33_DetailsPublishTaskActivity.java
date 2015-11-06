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

public class B3_33_DetailsPublishTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private LinearLayout ly_single_excutor, ly_multi_excutor;
	private RelativeLayout rl_pub_self_button;
	private Task task;
	private List<Task> tasks;
	private GridView gv_tasker, button_gridview;
	private TextView task_publisher_name, single_tasker_name, pub_task_state,
			single_tasker_state, task_name, task_content, task_starttime,
			task_finishtime, mul_tasker_num, mul_tasker_name, mul_tasker_state;
	private CommonAdapter<Object> taskAdapter;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType = new ArrayList<String>();
	private int[] imgResource = new int[] { R.drawable.ic_clock,
			R.drawable.ic_repeat, R.drawable.ic_dingwei };
	private boolean[] flags = new boolean[3];
	private ImageView task_publisher_avator, single_tasker_avator,
			mul_tasker_avator, task_pic1, task_pic2, task_pic3;
	private Button leftButton, rightButon, topRightButton;
	private JSONArray tasker_list;
	private int state = 0;
	private String single_taskerstate, single_taskername;
	private List<Object> tasker;
	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_publishtask);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle(task.getTitle());
		// 任务的顶部按钮
		topRightButton = (Button) findViewById(R.id.btn_topRightButton);// 顶部按钮，取消删除任务
		topRightButton.setText("取消任务");
		// 当发布的任务里含有自己的时候，任务执行状态和执行按钮可见
		pub_task_state = (TextView) findViewById(R.id.pub_task_state);
		rl_pub_self_button = (RelativeLayout) findViewById(R.id.rl_pub_self_button);
		// 任务的图片信息
		task_pic1 = (ImageView) findViewById(R.id.task_pic_1);
		task_pic2 = (ImageView) findViewById(R.id.task_pic_2);
		task_pic3 = (ImageView) findViewById(R.id.task_pic_3);
		// 为单人任务时的显示
		ly_single_excutor = (LinearLayout) findViewById(R.id.ly_single_excutor);// 执行人为单人时候
		single_tasker_avator = (ImageView) findViewById(R.id.single_tasker_avator);// 执行人为单人时候的任务执行人头像
		single_tasker_name = (TextView) findViewById(R.id.single_tasker_name);// 执行人为单人时候的任务执行人名字
		single_tasker_state = (TextView) findViewById(R.id.single_tasker_state);// 执行人为单人时候的任务执行状态
		ly_single_excutor.setVisibility(View.VISIBLE);// 当只有一个执行人时，多人的不显示，反之依然

		leftButton = (Button) findViewById(R.id.btn_leftButton);
		rightButon = (Button) findViewById(R.id.btn_rightButton);
		// 任务的信息
		task_name = (TextView) findViewById(R.id.details_publishtaskname);// 任务名称
		task_content = (TextView) findViewById(R.id.details_publishtaskcontent);// 任务内容
		// 任务发布人的信息
		task_publisher_name = (TextView) findViewById(R.id.details_publishtask_publish_username);// 任务发布人头像
		task_publisher_avator = (ImageView) findViewById(R.id.details_publishtask_publish_avator);// 任务发布人姓名
		// 为多人任务时的显示
		ly_multi_excutor = (LinearLayout) findViewById(R.id.ly_multi_excutor);
		mul_tasker_name = (TextView) findViewById(R.id.tasker_excutor_name);// 多人时任务执行人姓名
		mul_tasker_avator = (ImageView) findViewById(R.id.tasker_excutor_avator);// 多人时任务执行人头像
		mul_tasker_state = (TextView) findViewById(R.id.tasker_excutor_state);// 多人时任务执行人的任务状态
		mul_tasker_num = (TextView) findViewById(R.id.pub_multasker_excutor_num);// 执行人数量
		ly_multi_excutor.setVisibility(View.GONE);

		gv_tasker = (GridView) findViewById(R.id.gv_tasker);
		gv_tasker.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉点击的产生的背景色
		// 任务开始、结束时间
		task_starttime = (TextView) findViewById(R.id.details_publishtask_starttime);// 开始时间
		task_finishtime = (TextView) findViewById(R.id.details_publishtask_finishtime);// 结束时间

		// 设置任务详情里的监听事件
		setListener(ly_multi_excutor, topRightButton);// 设置监听事件
		// 任务的提醒、重复及定位信息
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
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData)
						.getJSONObject(0);
				tasker_list = jsonObject.getJSONArray("taskerlist");// 执行人列表
				if (tasker_list.size() == 1) {// 执行人列表为一人的时候
					JSONObject taskerInfo = tasker_list.getJSONObject(0);// 执行人信息
					single_taskerstate = taskerInfo.getString("tasker_state");
					single_taskername = taskerInfo.getString("nick");
					single_tasker_name.setText(single_taskername);
					ImageLoader.getInstance().displayImage(
							StringUtil.toString(
									HttpUtils.IMAGE_URL + task.getAvatar(),
									"http://"), single_tasker_avator);
					state = Integer.valueOf(single_taskerstate);
					final String statu = state == 0 ? "未接受"
							: state == 1 ? "已接受" : state == 2 ? "待执行"
									: state == 3 ? "执行中" : state == 4 ? "已完成"
											: "已取消";
					single_tasker_state.setText(statu);
				} else {// 执行人为多人时的信息
					ly_single_excutor.setVisibility(View.GONE);// 单人任务隐藏
					ly_multi_excutor.setVisibility(View.VISIBLE);// 多人任务显示
					initializationDate();

					mul_tasker_num.setText(tasker_list.size() + "人");
				}
				// 发布人信息
				task_publisher_name.setText(jsonObject.getString("nick"));
				ImageLoader.getInstance().displayImage(
						StringUtil.toString(
								HttpUtils.IMAGE_URL + task.getAvatar(),
								"http://"), task_publisher_avator);
				// 任务信息
				task_name.setText(jsonObject.getString("title"));
				task_content.setText(jsonObject.getString("content"));
				// 任务的时间信息
				if (jsonObject.getString("isday").equals("1")) {
					task_starttime.setText(jsonObject.getString("start")
							.substring(0, 11));
					task_finishtime.setText(jsonObject.getString("end")
							.substring(0, 11));
				} else {
					task_starttime.setText(jsonObject.getString("start"));
					task_finishtime.setText(jsonObject.getString("end"));
				}
				// 任务的图片信息
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
				// 获得任务的提醒和重复
				String tip1 = jsonObject.getString("tip");
				String repeat1 = jsonObject.getString("repeat");
				int tip = Integer.parseInt(tip1);
				int repeat = Integer.parseInt(repeat1);
				taskType.set(0, tip == 0 ? "正点": tip == 1 ? "五分钟" : tip == 2 ? "十分钟"
								: tip == 3 ? "一小时" : tip == 4 ? "一天" :tip == 5 ? "三天" :  "不提醒");
				taskType.set(1, repeat == 0 ? "不重复" : repeat == 1 ? "每天"
						: repeat == 2 ? "每周" : "每年");
				btnAdapter.notifyDataSetChanged();
			}
		}, params);
	}

	private void initializationDate() {
		tasker = tasker_list.subList(0, tasker_list.size());
		// tasks = JSON.parseArray(tasker_list.toString(), Task.class);
		taskAdapter = new CommonAdapter<Object>(
				B3_33_DetailsPublishTaskActivity.this,
				R.layout.ui_b3_1_item_multi_excutor, tasker) {
			@Override
			public void convert(ViewHolder holder, Object task) {
				state = Integer.valueOf(((JSONObject) task)
						.getString("tasker_state"));
				final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
						: state == 2 ? "待执行" : state == 3 ? "执行中"
								: state == 4 ? "已完成" : "已取消";

				final LinearLayout mLinearLayout = holder
						.getView(R.id.ly_item_excutor);
				if (Tools.M_SCREEN_WIDTH < 800) {
					LayoutParams checkboxParms = mLinearLayout
							.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 3 / 10;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 3 / 10;
				}
				holder.setText(R.id.tasker_excutor_name,
						((JSONObject) task).getString("nick"))
						.setText(R.id.tasker_excutor_state, statu)
						.setImageUrl(R.id.tasker_excutor_avator,
								((JSONObject) task).getString("avatar"), 10f);
			}
		};
		gv_tasker.setAdapter(taskAdapter);
		// taskAdapter.notifyDataSetChanged();
	}

	// state == 0 ? "未接受" : state == 1 ? "已接受"
	// : state == 2 ? "待执行" : state == 3 ? "执行中" : state == 4 ? "已完成"
	// : "已取消";
	// private void stateAndButtonChange() {
	// switch (state) {
	// case 0:// 未接受
	// leftButton.setText("接受任务");
	// rightButon.setText("删除任务");
	// state += 2;
	// modTaskState();
	// break;
	// case 2:// 待执行
	// task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
	// : state == 2 ? "待执行" : state == 3 ? "执行中"
	// : state == 4 ? "已完成" : "已取消");
	// leftButton.setText("开始执行");
	// rightButon.setText("取消任务");
	// state += 1;
	// modTaskState();
	// break;
	// case 3:// 执行中
	// task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
	// : state == 2 ? "待执行" : state == 3 ? "执行中"
	// : state == 4 ? "已完成" : "已取消");
	// leftButton.setText("标记完成");
	// rightButon.setText("取消任务");
	// state += 1;
	// modTaskState();
	// break;
	// case 4:// 已完成
	// task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
	// : state == 2 ? "待执行" : state == 3 ? "执行中"
	// : state == 4 ? "已完成" : "已取消");
	// leftButton.setText("删除任务");
	// rightButon.setVisibility(View.GONE);
	// modTaskState();
	// break;
	// default:
	// break;
	// }
	// }
	/**
	 * 获取任务状态并判定是否为0
	 */
	private void modTaskState() {
		params = new RequestParams();
		params.put("id", task.getId());
		HttpUtils.getTaskState(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				String state = json.getJSONArray(UrlContants.jsonData)
						.getJSONArray(0).getJSONObject(0).getString("state");
				int statu = Integer.parseInt(state);
				if (statu != 0) {
					changeTaskState();
				} else {
					deleteTask();
				}
			}

			/**
			 * 删除任务
			 */
			private void deleteTask() {
				RequestParams params = new RequestParams();
				params.put("id", task.getId());
				HttpUtils.delTaskInfo(new HttpErrorHandler() {

					@Override
					public void onRecevieSuccess(JSONObject json) {

					}
				}, params);
			}

			/**
			 * 更改任务状态
			 */
			private void changeTaskState() {
				params = new RequestParams();
				params.put("sid", task.getSid());
				params.put("state", "0");
				HttpUtils.modTaskState(new HttpErrorHandler() {

					@Override
					public void onRecevieSuccess(
							com.alibaba.fastjson.JSONObject json) {

						topRightButton.setText("删除任务");

					}
				}, params);

			}
		}, params);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_topRightButton:
			modTaskState();

			break;
		case R.id.btn_rightButton:

			break;
		case R.id.ly_multi_excutor:
			startActivity(new Intent(this,
					B3_32_1_ExecutorsTaskStateActivity.class).putExtra("tasker",
					tasker_list.toString()));
			break;
		default:
			break;
		}
	}

}
