package com.zykj.hihome;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.DateTimePickDialogUtil;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class B3_TaskAddTaskTaskActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private MyCommonTitle myCommonTitle;
	private ImageView img_read_contacts, img_input_contentimg, img_camere,
			img_photo;
	private Date date;
	private ToggleButton toggleButton;
	private LinearLayout ly_clock, ly_repeat, ly_location, ly_add_img;
	private TextView ed_taskexcutor;
	private EditText ed_taskname, ed_taskcontent;
	private TextView tv_starttime, tv_finishtime;
	private String initStartDateTime = "2013年9月3日 14:44"; // 初始化开始时间
	private String initEndDateTime = "2014年8月23日 17:44"; // 初始化结束时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_taskaddtask);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("创建任务");
		myCommonTitle.setEditTitle("完成");

		ed_taskname = (EditText) findViewById(R.id.input_taskname);// 任务名称
		ed_taskexcutor = (TextView) findViewById(R.id.input_taskexcutor);// 任务执行人
		img_read_contacts = (ImageView) findViewById(R.id.img_read_contacts);// 读取联系人
		ed_taskcontent = (EditText) findViewById(R.id.input_taskcontent);// 任务内容
		img_camere = (ImageView) findViewById(R.id.img_camere);// 启动摄像头拍照
		img_photo = (ImageView) findViewById(R.id.img_photo);// 读取相册
		img_input_contentimg = (ImageView) findViewById(R.id.img_input_contentimg);// 添加图片，显示添加图片栏
		toggleButton = (ToggleButton) findViewById(R.id.toggle_on_off);// 设置全天开关
		tv_starttime = (TextView) findViewById(R.id.input_task_starttime);// 设置开始时间
		tv_finishtime = (TextView) findViewById(R.id.input_task_finishtime);// 设置结束时间
		ly_add_img = (LinearLayout) findViewById(R.id.ly_task_input_content);// 添加图片栏
		ly_clock = (LinearLayout) findViewById(R.id.ly_clock);// 设置提醒
		ly_repeat = (LinearLayout) findViewById(R.id.ly_repeat);// 设置重复
		ly_location = (LinearLayout) findViewById(R.id.ly_dingwei);// 设置定位
		toggleButton.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件
		
		date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  hh:mm:ss");
		String time=format.format(date);
		tv_starttime.setText(time);
		tv_finishtime.setText(time);
		setListener(img_read_contacts, img_input_contentimg, img_photo,
				img_camere, tv_starttime, tv_finishtime, ly_clock, ly_repeat,
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
			startActivity(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_SelectExecutorActivity.class));
			break;
		case R.id.img_input_contentimg:// 为任务内容添加图片，因在布局文件中添加图片的控件隐藏了，如需要先设置ly_add_img可见
			img_input_contentimg.setVisibility(View.GONE);
			ly_add_img.setVisibility(View.VISIBLE);
			break;

		case R.id.img_camere:// 启动手机拍照
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
			String timeString = dateFormat.format(date);
			createSDCardDir();
			Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			shootIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory() + "/DCIM/Camera",
							timeString + ".jpg")));
			startActivityForResult(shootIntent, 2);
			break;
		case R.id.img_photo:// 添加手机本地相册图片
			Intent photoIntent = new Intent(Intent.ACTION_PICK, null);
			/**
			 * 下面这句话，与其它方式写是一样的效果，如果： intent.setData(MediaStore.Images
			 * .Media.EXTERNAL_CONTENT_URI); intent.setType(""image/*");设置数据类型
			 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如 ："image/jpeg 、 image/png等的类型"
			 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
			 */
			photoIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(photoIntent, 1);
			break;
		case R.id.toggle_on_off:// 是否创建全天日程

			break;
		case R.id.input_task_starttime:// 开始时间
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					B3_TaskAddTaskTaskActivity.this, initEndDateTime);
			dateTimePicKDialog.dateTimePicKDialog(tv_starttime);
			break;
		case R.id.input_task_finishtime:// 结束时间

			DateTimePickDialogUtil dateTimePicKDialog2 = new DateTimePickDialogUtil(
					B3_TaskAddTaskTaskActivity.this, initEndDateTime);
			dateTimePicKDialog2.dateTimePicKDialog(tv_finishtime);

			break;
		case R.id.ly_clock:// 设置提醒
			startActivity(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_TiXingActivity.class));
			break;
		case R.id.ly_repeat:// 设置重复
			startActivity(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_RepeatActivity.class));
			break;
		case R.id.ly_dingwei:// 设置定位

			break;

		case R.id.aci_edit_btn:// 创建任务

			break;
		default:
			break;
		}
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();

			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Tools.toast(B3_TaskAddTaskTaskActivity.this, "请先设置起始时间");
		String time1=StringUtil.toString(tv_starttime.getText());
		String time2=StringUtil.toString(tv_finishtime.getText());
		if (isChecked) {
			tv_starttime.setText(time1.substring(0, 12));
			tv_finishtime.setText(time2.substring(0, 12));
		}else{
			tv_starttime.setText(time1);
			tv_finishtime.setText(time2);
		}

	}

}
