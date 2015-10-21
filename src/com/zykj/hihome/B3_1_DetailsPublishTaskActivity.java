package com.zykj.hihome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsPublishTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private TextView task_state, task_name, task_publish_name,
			 task_excutor_name, 
			task_content, task_starttime, task_finishtime;
	private ImageView task_publish_avator,task_excutor_avator;
	private Button btn_delete;

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

		task_state = (TextView) findViewById(R.id.details_publishtask_state);// 任务状态
		task_name = (TextView) findViewById(R.id.details_publishtaskname);// 任务名称
		task_publish_name = (TextView) findViewById(R.id.details_publishtask_publish_username);// 任务发布人头像
		task_publish_avator = (ImageView) findViewById(R.id.details_publishtask_publish_avator);// 任务发布人姓名
		task_excutor_name = (TextView) findViewById(R.id.details_publishtask_excutor_name);// 任务执行人姓名
		task_excutor_avator= (ImageView) findViewById(R.id.details_publishtask_excutor_avator);// 任务执行人姓名
		task_content = (TextView) findViewById(R.id.details_publishtaskcontent);// 任务内容
		task_starttime = (TextView) findViewById(R.id.details_publishtask_starttime);// 开始时间
		task_finishtime = (TextView) findViewById(R.id.details_publishtask_finishtime);// 结束时间

		initializationDate();

	}

	private void initializationDate() {
		int state = Integer.valueOf(task.getState());
		task_state.setText(state==0?"未接受":state==1?"已接受":state==2?"待执行":state==3?"已执行":state==4?"已完成":"已取消");
		task_name.setText(task.getTitle());
		task_content.setText(task.getContent());
		task_starttime.setText(task.getStart());
		task_finishtime.setText(task.getEnd());		
	}

}
