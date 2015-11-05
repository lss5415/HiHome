package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.R.id;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author Administrator
 * 我自己的任务详情
 */
public class B3_1_DetailsSelfTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private Button leftButton, rightButton;
	private TextView task_state, task_name, task_excutor, task_content,
			task_starttime, task_finishtime;
	private ImageView task_img1, task_img2, task_img3;
	private GridView button_gridview;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType = new ArrayList<String>();
	private int[] imgResource = new int[] { R.drawable.ic_clock,
			R.drawable.ic_repeat, R.drawable.ic_dingwei};
	private boolean[] flags = new boolean[3];
	private int state = 0;
	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_selftask);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle(task.getTitle());
		myCommonTitle.setEditTitle("编辑");
		// 任务图片信息
		task_img1 = (ImageView) findViewById(R.id.task_pic_1);
		task_img2 = (ImageView) findViewById(R.id.task_pic_2);
		task_img3 = (ImageView) findViewById(R.id.task_pic_3);
		// 任务的Button
		leftButton = (Button) findViewById(R.id.btn_leftButton);
//		leftButton.setText("接受任务");
		rightButton = (Button) findViewById(R.id.btn_rightButton);
		
		setListener(leftButton, rightButton);
		// 任务的信息
		task_state = (TextView) findViewById(R.id.details_selftask_state);// 任务状态
		task_name = (TextView) findViewById(R.id.details_selftaskname);// 任务名称
		task_excutor = (TextView) findViewById(R.id.details_selftaskexcutor);// 任务执行人
		task_content = (TextView) findViewById(R.id.details_selftaskcontent);// 任务内容
		task_starttime = (TextView) findViewById(R.id.details_selftask_starttime);// 开始时间
		task_finishtime = (TextView) findViewById(R.id.details_selftask_finishtime);// 结束时间

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
		requstData();
		// initializationDate();

	}

	/**
	 * 请求服务器数据
	 */
	private void requstData() {

		params = new RequestParams();
		params.put("id", task.getId());
		/**
		 * 获取任务执行状态
		 */
		HttpUtils.getTaskState(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(com.alibaba.fastjson.JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData)
						.getJSONArray(0).getJSONObject(0);
				String taskstate = jsonObject.getString("state");

				state = Integer.valueOf(taskstate);
				final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
						: state == 2 ? "待执行" : state == 3 ? "已执行"
								: state == 4 ? "已完成" : "已取消";
				task_state.setText(statu);
				stateAndButtonChange();
			}
		}, params);
		/**
		 * 获取任务其他详情
		 */
		HttpUtils.getTasksInfo(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData)
						.getJSONObject(0);

				task_name.setText(jsonObject.getString("title"));
				task_content.setText(jsonObject.getString("content"));
				task_excutor.setText("仅自己");
				String statu = jsonObject.getJSONArray("taskerlist")
						.getJSONObject(0).getString("tasker_state");
				int state = Integer.parseInt(statu);
				task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
						: state == 2 ? "待执行" : state == 3 ? "已执行"
								: state == 4 ? "已完成" : "已取消");

				if (jsonObject.getString("isday").equals("1")) {
					task_starttime.setText(jsonObject.getString("start")
							.substring(0, 11));
					task_finishtime.setText(jsonObject.getString("end")
							.substring(0, 11));
				} else {
					task_starttime.setText(jsonObject.getString("start"));
					task_finishtime.setText(jsonObject.getString("end"));
				}
				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc1"))) {
					ImageLoader.getInstance().displayImage(
							HttpUtils.IMAGE_URL + task.getImgsrc1(), task_img1);
				}
				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc2"))) {
					ImageLoader.getInstance().displayImage(
							HttpUtils.IMAGE_URL + task.getImgsrc1(), task_img2);
				}
				if (!StringUtil.isEmpty(jsonObject.getString("imgsrc3"))) {
					ImageLoader.getInstance().displayImage(
							HttpUtils.IMAGE_URL + task.getImgsrc1(), task_img3);
				}
				String tip1 = jsonObject.getString("tip");
				String repeat1 = jsonObject.getString("repeat");
				int tip = Integer.parseInt(tip1);
				int repeat = Integer.parseInt(repeat1);
				taskType.set(0, tip == 0 ? "不提醒" : tip == 1 ? "五分钟前"
						: tip == 2 ? "十分钟前" : tip == 3 ? "一小时前"
								: tip == 4 ? "一天前" : "三天前");
				taskType.set(1, repeat == 0 ? "不重复" : repeat == 1 ? "每天"
						: repeat == 2 ? "每周" : "每年");
				btnAdapter.notifyDataSetChanged();
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
		case R.id.aci_edit_btn:
			startActivityForResult(new Intent(B3_1_DetailsSelfTaskActivity.this,
					B3_TaskAddTaskTaskActivity.class).putExtra("task", task), 20);
			break;
		default:
			break;
		}

	}

	/**
	 * Button点击事件改变状态
	 */
	private void stateAndButtonChange() {
		switch (state) {
		case 0:// 未接受
			leftButton.setText("接受任务");
			rightButton.setText("删除任务");
			state += 2;
			modTaskState();
			break;
		case 2:// 待执行
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("开始执行");
			rightButton.setText("取消任务");
			state += 1;
			modTaskState();
			break;
		case 3:// 执行中
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("标记完成");
			rightButton.setText("取消任务");
			state += 1;
			modTaskState();
			break;
		case 4:// 已完成
			task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
					: state == 2 ? "待执行" : state == 3 ? "执行中"
							: state == 4 ? "已完成" : "已取消");
			leftButton.setText("删除任务");
			rightButton.setVisibility(View.GONE);
			modTaskState();
			break;
		default:
			break;
		}
	}

	/**
	 * 更新任务状态
	 */
	private void modTaskState() {
		RequestParams params = new RequestParams();
		params.put("sid", task.getSid());
		params.put("state", state);
		HttpUtils.modTaskState(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {

			}
		}, params);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 20 && resultCode == Activity.RESULT_OK){
			finish();//编辑任务成功之后，直接返回任务列表
		}
	}
}
