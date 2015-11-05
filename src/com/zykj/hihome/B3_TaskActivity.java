package com.zykj.hihome;

import org.joda.time.LocalDate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.zykj.hihome.calendar.CalendarManager;
import com.zykj.hihome.calendar.CollapseCalendarView;
import com.zykj.hihome.fragment.TaskFragment;
import com.zykj.hihome.utils.CommonUtils;
import com.zykj.hihome.view.XListView;

/**
 * @author LSS 2015年9月29日 上午8:55:45
 * 
 */
public class B3_TaskActivity extends FragmentActivity implements OnClickListener{

	private ImageView img_create_anniversary, img_publish_task;
    private CollapseCalendarView mCalendarView;
	private RadioGroup tab_rwGroup;
    private TaskFragment taskFragment1,taskFragment2,taskFragment3;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_task);
		
		initView();
		requestData();
	}

	public void initView() {
		img_publish_task = (ImageView) findViewById(R.id.img_publish_task);
		img_create_anniversary = (ImageView) findViewById(R.id.img_create_anniversary);
		//加载日历控件
        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));
        mCalendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        mCalendarView.init(manager);
        //加载三个任务模块
		tab_rwGroup = (RadioGroup) findViewById(R.id.tab_rwGroup);
		taskFragment1 = TaskFragment.getInstance(1);//自己的任务
		taskFragment2 = TaskFragment.getInstance(2);//接受的任务
		taskFragment3 = TaskFragment.getInstance(3);//发布的任务

    	tab_rwGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_selfTask) {
					getSupportFragmentManager().beginTransaction().show(taskFragment1).hide(taskFragment2).hide(taskFragment3).commit();
				} else if (checkedId == R.id.radio_receiveTask) {
					getSupportFragmentManager().beginTransaction().hide(taskFragment1).show(taskFragment2).hide(taskFragment3).commit();
				} else if (checkedId == R.id.radio_publishTask) {
					getSupportFragmentManager().beginTransaction().hide(taskFragment1).hide(taskFragment2).show(taskFragment3).commit();
				}
			}
		});

		img_publish_task.setOnClickListener(this);
		img_create_anniversary.setOnClickListener(this);
	}

	/**
	 * 加载数据
	 */
	private void requestData(){
		getSupportFragmentManager().beginTransaction()
			.add(R.id.renwu_framelayout, taskFragment1).show(taskFragment1)
			.add(R.id.renwu_framelayout, taskFragment2).hide(taskFragment2)
			.add(R.id.renwu_framelayout, taskFragment3).hide(taskFragment3).commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_create_anniversary:// 创建纪念日
			startActivityForResult(new Intent(B3_TaskActivity.this,
					B3_TaskAddAnniversaryActivity.class),1);
			break;
		case R.id.img_publish_task:// 发布任务
			startActivityForResult(new Intent(B3_TaskActivity.this,
					B3_TaskAddTaskTaskActivity.class),2);
			break;
		default:
			break;
		}
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	super.onActivityResult(requestCode, resultCode, intent);
	if(requestCode==1){
		taskFragment1.reflush();
	}else if (requestCode==2) {
		taskFragment1.reflush();
		taskFragment2.reflush();
		taskFragment3.reflush();
	}
//	else if(requestCode==10){
//		taskFragment3.updatelist(getIntent().getIntExtra("position", 0), getIntent().getStringExtra("state"));
//	}
	
	
}
}