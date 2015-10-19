package com.zykj.hihome;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.DateTimePickDialogUtil;
import com.zykj.hihome.view.MyCommonTitle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class B3_1_RenWuPublishTaskActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ImageView img_read_contacts, img_input_content, img_camere,
			img_photo;
	private ToggleButton toggleButton;
	private LinearLayout ly_clock, ly_repeat, ly_location, ly_add_img;
	private TextView ed_taskexcutor;
	private EditText ed_taskname,  ed_taskcontent;
	private EditText tv_starttime, tv_finishtime;
    private String initStartDateTime = "2013年9月3日 14:44"; // 初始化开始时间  
    private String initEndDateTime = "2014年8月23日 17:44"; // 初始化结束时间  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_punlishtask);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("创建任务");
		myCommonTitle.setEditTitle("完成");

		ed_taskname = (EditText) findViewById(R.id.input_taskname);
		ed_taskexcutor = (TextView) findViewById(R.id.input_taskexcutor);
		img_read_contacts = (ImageView) findViewById(R.id.img_read_contacts);
		ed_taskcontent = (EditText) findViewById(R.id.input_taskcontent);
		img_camere = (ImageView) findViewById(R.id.img_camere);
		img_photo = (ImageView) findViewById(R.id.img_photo);
		img_input_content = (ImageView) findViewById(R.id.img_input_content);
		toggleButton = (ToggleButton) findViewById(R.id.toggle_on_off);
		tv_starttime = (EditText) findViewById(R.id.input_task_starttime);
		tv_finishtime = (EditText) findViewById(R.id.input_task_finishtime);
		ly_add_img = (LinearLayout) findViewById(R.id.ly_add_img);
		ly_clock = (LinearLayout) findViewById(R.id.ly_clock);
		ly_repeat = (LinearLayout) findViewById(R.id.ly_repeat);
		ly_location = (LinearLayout) findViewById(R.id.ly_dingwei);

		setListener(tv_starttime, tv_finishtime, ly_clock, ly_repeat,
				ly_location);
	}

	/**
	 * 当设置全天，开始时间只显示是日期，否则显示日期和时间
	 * 
	 * 
	 */
	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.img_read_contacts:// 执行人，读取通讯录

			break;
		case R.id.img_input_content:// 为任务内容添加图片，因在布局文件中添加图片的控件隐藏了，如需要先设置ly_add_img可见

			break;

		case R.id.img_camere:// 启动手机拍照

			break;
		case R.id.img_photo:// 添加手机本地相册图片

			break;
		case R.id.toggle_on_off:// 是否创建全天日程

			break;
		case R.id.input_task_starttime:// 开始时间
			 DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
                     B3_1_RenWuPublishTaskActivity.this, initEndDateTime);  
             dateTimePicKDialog.dateTimePicKDialog( tv_starttime);  
			break;
		case R.id.input_task_finishtime:// 结束时间
			 DateTimePickDialogUtil dateTimePicKDialog2 = new DateTimePickDialogUtil(  
					 B3_1_RenWuPublishTaskActivity.this, initEndDateTime);  
             dateTimePicKDialog2.dateTimePicKDialog(tv_finishtime); 
			
			
			break;
		case R.id.ly_clock:// 设置提醒
			startActivity(new Intent(B3_1_RenWuPublishTaskActivity.this,
					B3_1_1_TiXingActivity.class));
			break;
		case R.id.ly_repeat:// 设置重复
			startActivity(new Intent(B3_1_RenWuPublishTaskActivity.this,
					B3_1_1_RepeatActivity.class));
			break;
		case R.id.ly_dingwei:// 设置定位

			break;

		case R.id.aci_edit_btn:// 创建任务

			break;
		default:
			break;
		}
	}
}
