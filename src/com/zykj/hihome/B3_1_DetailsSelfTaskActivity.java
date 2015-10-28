package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_DetailsSelfTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private Task task;
	private TextView task_state, task_name, task_excutor, task_content,
			task_starttime, task_finishtime;
	private Button btn_delete;
	private GridView button_gridview;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType=new ArrayList<String>();
	private int[] imgResource = new int[]{R.drawable.ic_clock,R.drawable.ic_repeat,R.drawable.ic_dingwei};
	private boolean[] flags = new boolean[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_details_selftask);
		task = (Task) getIntent().getSerializableExtra("task");
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
		
		button_gridview=(GridView) findViewById(R.id.button_gridview);
		button_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		taskType.add("提醒");
		taskType.add("重复");
		taskType.add("位置");
		btnAdapter = new CommonAdapter<String>(this, R.layout.ui_b3_addtask_check, taskType) {
			@Override
			public void convert(ViewHolder holder, String type) {
				RelativeLayout mLayout = holder.getView(R.id.check_relative);
				TextView mTextView = holder.getView(R.id.check_item);
				if(Tools.M_SCREEN_WIDTH < 800){
					LayoutParams checkboxParms = mLayout.getLayoutParams();
					checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 9;
					checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 9;
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
		task_state.setText(state==0?"未接受":state==1?"已接受":state==2?"待执行":state==3?"已执行":state==4?"已完成":"已取消");
		task_name.setText(task.getTitle());
		task_excutor.setText("自己");
		task_content.setText(task.getContent());
		task_starttime.setText(task.getStart());
		task_finishtime.setText(task.getEnd());		
	}

}
