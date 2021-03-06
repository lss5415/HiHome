package com.zykj.hihome;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.calendar.CalendarManager;
import com.zykj.hihome.calendar.CollapseCalendarView;
import com.zykj.hihome.calendar.CollapseCalendarView.OnDateSelect;
import com.zykj.hihome.fragment.TaskFragment;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;

/**
 * @author LSS 2015年9月29日 上午8:55:45
 * 
 */
public class B3_0_TaskActivity extends FragmentActivity implements
		OnClickListener {

	private ImageView img_create_anniversary, img_publish_task;
	private CircularImage img_avatar;
	private CollapseCalendarView mCalendarView;
	private CalendarManager manager;
	private RadioGroup tab_rwGroup;
	private TaskFragment taskFragment1, taskFragment2, taskFragment3;
	private ArrayList<String> taskDates = new ArrayList<String>();
	public static String clickdate = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_task);

		getDateTaskList();
	}

	public void getDateTaskList() {
		HttpUtils.getDateTaskList(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONObject(UrlContants.jsonData)
						.getJSONArray("list");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONArray dateArray = jsonArray.getJSONObject(i)
							.getJSONArray("date");
					for (int j = 0; j < dateArray.size(); j++) {
						if (!taskDates.contains(dateArray.getString(j))) {
							taskDates.add(dateArray.getString(j));
						}
					}
				}
				initView();
				requestData();
			}
		}, BaseApp.getModel().getUserid());
	}

	public void initView() {
		img_avatar = (CircularImage) findViewById(R.id.cimg_me_avatar);
		img_publish_task = (ImageView) findViewById(R.id.img_publish_task);
		img_create_anniversary = (ImageView) findViewById(R.id.img_create_anniversary);
		// 加载日历控件
		manager = new CalendarManager(LocalDate.now(),
				CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now()
						.plusYears(1));
		mCalendarView = (CollapseCalendarView) findViewById(R.id.calendar);
		mCalendarView.init(manager, taskDates);
		mCalendarView.setListener(new OnDateSelect() {
			@Override
			public void onDateSelected(LocalDate date) {
				clickdate = date.toString();
				taskFragment1.reflush();// 刷新任务列表
				taskFragment2.reflush();// 刷新任务列表
				taskFragment3.reflush();// 刷新任务列表
				// Tools.toast(B3_TaskActivity.this, date.toString());
			}
		});
		// 加载三个任务模块
		tab_rwGroup = (RadioGroup) findViewById(R.id.tab_rwGroup);
		taskFragment1 = TaskFragment.getInstance(1);// 自己的任务
		taskFragment2 = TaskFragment.getInstance(2);// 接受的任务
		taskFragment3 = TaskFragment.getInstance(3);// 发布的任务

		tab_rwGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_selfTask) {
					getSupportFragmentManager().beginTransaction()
							.show(taskFragment1).hide(taskFragment2)
							.hide(taskFragment3).commit();
				} else if (checkedId == R.id.radio_receiveTask) {
					getSupportFragmentManager().beginTransaction()
							.hide(taskFragment1).show(taskFragment2)
							.hide(taskFragment3).commit();
				} else if (checkedId == R.id.radio_publishTask) {
					getSupportFragmentManager().beginTransaction()
							.hide(taskFragment1).hide(taskFragment2)
							.show(taskFragment3).commit();
				}
			}
		});
		img_avatar.setOnClickListener(this);
		img_publish_task.setOnClickListener(this);
		img_create_anniversary.setOnClickListener(this);
		
	}

	/**
	 * 加载数据
	 */
	private void requestData() {
		ImageLoader.getInstance().displayImage(
				StringUtil.toString(HttpUtils.IMAGE_URL
						+ BaseApp.getModel().getAvatar(), "http://"),
				img_avatar);

		getSupportFragmentManager().beginTransaction()
				.add(R.id.renwu_framelayout, taskFragment1).show(taskFragment1)
				.add(R.id.renwu_framelayout, taskFragment2).hide(taskFragment2)
				.add(R.id.renwu_framelayout, taskFragment3).hide(taskFragment3)
				.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cimg_me_avatar:
			startActivity(new Intent(B3_0_TaskActivity.this,
					B1_07_WoDeZiLiao.class));
			break;
		case R.id.img_create_anniversary:// 创建纪念日
			startActivityForResult(new Intent(B3_0_TaskActivity.this,
					B3_1_TaskAddAnniversaryActivity.class), 1);
			break;
		case R.id.img_publish_task:// 发布任务
			startActivityForResult(new Intent(B3_0_TaskActivity.this,
					B3_2_TaskAddTaskTaskActivity.class), 2);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1) {
			taskFragment1.reflush();
		} else if (requestCode == 2 || requestCode == 55) {
			taskFragment1.reflush();
			taskFragment2.reflush();
			taskFragment3.reflush();
		}
		// else if(requestCode==10){
		// taskFragment3.updatelist(getIntent().getIntExtra("position", 0),
		// getIntent().getStringExtra("state"));
		// }

	}

	/**
	 * 刷新日历
	 */
	public void reflushCalendar() {
		HttpUtils.getDateTaskList(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				taskDates.clear();
				JSONArray jsonArray = json.getJSONObject(UrlContants.jsonData)
						.getJSONArray("list");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONArray dateArray = jsonArray.getJSONObject(i)
							.getJSONArray("date");
					for (int j = 0; j < dateArray.size(); j++) {
						if (!taskDates.contains(dateArray.getString(j))) {
							taskDates.add(dateArray.getString(j));
						}
					}
				}
				mCalendarView.init(manager, taskDates);
			}
		}, BaseApp.getModel().getUserid());
	}
}