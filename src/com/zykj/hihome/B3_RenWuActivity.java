package com.zykj.hihome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.RenWu;

/**
 * @author LSS 2015年9月29日 上午8:55:45
 * 
 */
public class B3_RenWuActivity extends BaseActivity {

	private ImageView img_create_anniversary, img_publish_task;
	private RadioButton rb_selfTask, rb_receiveTask, rb_publishTask;
	private ListView mListView;
	private ArrayAdapter renWuAdapter;
	private RenWu renwu;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_renwu);
		initView();
	}

	public void initView() {

		mListView = (ListView) findViewById(R.id.list_renwu);
		img_publish_task = (ImageView) findViewById(R.id.img_publish_task);
		img_create_anniversary = (ImageView) findViewById(R.id.img_create_anniversary);

		rb_selfTask = (RadioButton) findViewById(R.id.radio_selfTask);
		rb_receiveTask = (RadioButton) findViewById(R.id.radio_receiveTask);
		rb_publishTask = (RadioButton) findViewById(R.id.radio_publishTask);
		//
		setListener(img_publish_task, img_create_anniversary);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_create_anniversary:// 创建纪念日
			startActivity(new Intent(B3_RenWuActivity.this,
					B3_RenWuCreateAnniversaryActivity.class));

			break;
		case R.id.img_publish_task:// 发布任务
			startActivity(new Intent(B3_RenWuActivity.this,
					B3_RenWuPublishTaskActivity.class));
			break;
		case R.id.radio_selfTask:// 自己发布的任务
			
			break;
		case R.id.radio_receiveTask:// 接受的任务
	
			break;
		case R.id.radio_publishTask:// 发布的任务
		
			break;
		default:
			break;
		}
	}

}