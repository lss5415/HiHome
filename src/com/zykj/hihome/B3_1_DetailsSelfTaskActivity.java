package com.zykj.hihome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsSelfTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private TextView task_state, task_name, task_excutor, task_content,
			task_starttime, task_finishtime;
	private Button btn_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_selftask);

		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setTitle("任务名字");

		task_state = (TextView) findViewById(R.id.details_selftask_state);//任务状态
		task_name = (TextView) findViewById(R.id.details_selftaskname);//任务名称
		task_excutor = (TextView) findViewById(R.id.details_selftaskexcutor);//任务执行人
		task_content = (TextView) findViewById(R.id.details_selftaskcontent);//任务内容
		task_starttime = (TextView) findViewById(R.id.details_selftask_starttime);//开始时间
		task_finishtime = (TextView) findViewById(R.id.details_selftask_finishtime);//结束时间
		
		
	}

}
