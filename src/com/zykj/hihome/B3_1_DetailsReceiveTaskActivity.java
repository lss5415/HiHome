package com.zykj.hihome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.ImageUtil;
import com.zykj.hihome.utils.SharedPreferenceUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsReceiveTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private TextView task_state, task_name, task_publish_name,
			task_excutor_name, task_content, task_starttime, task_finishtime;
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
//		String pub_avator = task.getAvatar();
//		ImageLoader.getInstance().displayImage(StringUtil.isEmpty(SharedPreferenceUtils.getAvatar())?"":pub_avator, task_publish_avator);
	}
}
