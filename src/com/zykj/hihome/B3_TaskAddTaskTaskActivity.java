package com.zykj.hihome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.DateTimePickDialogUtil;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;
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
	private File file;
	private String timeString;// 上传头像的字段
	private String title, content, isday, starttime, endtime, tip, repeat,
			tasker;

	// uid必须，用户ID编号title必须，任务名称content必须，任务内容isday必须，是否是全天任务start必须，任务开始时间
	// end必须，任务结束时间tip必须，任务提醒：数值意义见左侧repeat必须，任务重复：数值意义见左侧tasker必须，任务执行人ID编号，如1,2,3,4多个之间用英文,分隔

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
		img_photo = (ImageView) findViewById(R.id.img_photo);// 读取相册
		toggleButton = (ToggleButton) findViewById(R.id.toggle_on_off);// 设置全天开关
		tv_starttime = (TextView) findViewById(R.id.input_task_starttime);// 设置开始时间
		tv_finishtime = (TextView) findViewById(R.id.input_task_finishtime);// 设置结束时间
		ly_clock = (LinearLayout) findViewById(R.id.ly_clock);// 设置提醒
		ly_repeat = (LinearLayout) findViewById(R.id.ly_repeat);// 设置重复
		ly_location = (LinearLayout) findViewById(R.id.ly_dingwei);// 设置定位
		toggleButton.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件

		date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  hh:mm:ss");
		String time = format.format(date);
		tv_starttime.setText(time);
		tv_finishtime.setText(time);
		setListener(img_read_contacts, img_photo, tv_starttime, tv_finishtime,
				ly_clock, ly_repeat, ly_location);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.img_read_contacts:// 执行人，读取通讯录
			startActivity(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_SelectExecutorActivity.class));
			break;
		case R.id.img_photo:// 添加手机本地相册图片
			UIDialog.ForThreeBtn(this, new String[] { "拍照", "从相册中选取", "取消" },
					this);
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
			startActivityForResult(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_TiXingActivity.class),11);
			break;
		case R.id.ly_repeat:// 设置重复
			startActivityForResult(new Intent(B3_TaskAddTaskTaskActivity.this,
					B3_1_RepeatActivity.class),11);
			break;
		case R.id.ly_dingwei:// 设置定位

			break;

		case R.id.aci_edit_btn:// 创建任务
			title = ed_taskname.getText().toString().trim();
			content = ed_taskcontent.getText().toString().trim();
			// isday
			starttime = tv_starttime.getText().toString().trim();
			endtime = tv_finishtime.getText().toString().trim();
			// tip
			// repeat
			// tasker
			RequestParams params = new RequestParams();

			params.put("title", title);
			params.put("content", content);
			params.put("start", starttime);
			params.put("end", endtime);
			
			HttpUtils.addTask(new HttpErrorHandler() {
				
				@Override
				public void onRecevieSuccess(JSONObject json) {
					
					
					setResult(RESULT_OK);
				}
			}, params);
			break;
		case R.id.dialog_modif_1:
			/* 拍照 */
			UIDialog.closeDialog();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
			timeString = dateFormat.format(date);
			createSDCardDir();
			Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			shootIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory() + "/DCIM/Camera",
							timeString + ".jpg")));
			startActivityForResult(shootIntent, 2);
			break;
		case R.id.dialog_modif_2:
			/* 从相册中选取 */
			UIDialog.closeDialog();
			Intent photoIntent = new Intent(Intent.ACTION_PICK, null);
			photoIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(photoIntent, 1);
			break;
		case R.id.dialog_modif_3:
			UIDialog.closeDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			/* 如果是直接从相册获取 */
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG).show();
			}
			break;
		case 2:
			/* 如果是调用相机拍照，图片设置名字和路径 */
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case 3:
			/* 取得裁剪后的图片 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 0.6);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			// Drawable drawable = new BitmapDrawable(photo);
			/* 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似 */
			savaBitmap(photo);
			// avatar_head_image.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 将剪切后的图片保存到本地图片上！
	 */
	public void savaBitmap(Bitmap bitmap) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
		String cutnameString = dateFormat.format(date);
		String filename = Environment.getExternalStorageDirectory().getPath()
				+ "/" + cutnameString + ".jpg";
		file = new File(filename);
		FileOutputStream fOut = null;
		try {
			file.createNewFile();
			fOut = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		img_photo.setImageBitmap(bitmap);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Tools.toast(B3_TaskAddTaskTaskActivity.this, "请先设置起始时间");
		String time1 = StringUtil.toString(tv_starttime.getText());
		String time2 = StringUtil.toString(tv_finishtime.getText());
		if (isChecked) {
			tv_starttime.setText(time1.substring(0, 12));
			tv_finishtime.setText(time2.substring(0, 12));
		} else {
			tv_starttime.setText(time1);
			tv_finishtime.setText(time2);
		}
	}
}
