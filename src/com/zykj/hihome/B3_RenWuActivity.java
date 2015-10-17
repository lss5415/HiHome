package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年9月29日 上午8:55:45
 * 
 */
public class B3_RenWuActivity extends BaseActivity {

	private ImageView img_create_anniversary, img_publish_task;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_renwu);
		initView();
	}

	public void initView() {

		img_publish_task = (ImageView) findViewById(R.id.img_publish_task);
		img_create_anniversary = (ImageView) findViewById(R.id.img_create_anniversary);
		//
		setListener(img_publish_task, img_create_anniversary);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_create_anniversary://创建纪念日
			startActivity(new Intent(B3_RenWuActivity.this,
					B3_1_RenWuCreateAnniversaryActivity.class));

			break;
		case R.id.img_publish_task://发布任务
			startActivity(new Intent(B3_RenWuActivity.this,
					B3_1_RenWuPublishTaskActivity.class));
			break;
		default:
			break;
		}
	}

}