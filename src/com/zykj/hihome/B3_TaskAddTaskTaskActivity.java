package com.zykj.hihome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CommonUtils;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.MyRequestDailog;
import com.zykj.hihome.view.UIDialog;

public class B3_TaskAddTaskTaskActivity extends BaseActivity implements
		OnCheckedChangeListener, OnItemClickListener {
	private MyCommonTitle myCommonTitle;
	private ImageView img_read_contacts;
	private GridView img_photo, button_fridview;
	private ToggleButton toggleButton;
	private LinearLayout layout_starttime, layout_finishtime;
	private TextView ed_taskexcutor;
	private EditText ed_taskname, ed_taskcontent;
	private TextView tv_starttime, tv_finishtime;
	private File file;
	private String timeString;// 上传头像的字段
	private String title, content, starttime, endtime, strId;

	private List<File> files = new ArrayList<File>();
	private List<Bitmap> images = new ArrayList<Bitmap>();
	private CommonAdapter<Bitmap> imgAdapter;
	private CommonAdapter<String> btnAdapter;
	private List<String> taskType=new ArrayList<String>();
	private int[] imgResource = new int[]{R.drawable.ic_clock,R.drawable.ic_repeat,R.drawable.ic_dingwei};
	private boolean[] flags = new boolean[3];
	private int tixing = -1,chongfu = -1,isday,index;;
	private RequestParams params;

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
		img_photo = (GridView) findViewById(R.id.img_photo);// 读取相册
		img_photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
		images.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_task_imgview));
		imgAdapter = new CommonAdapter<Bitmap>(this, R.layout.ui_b3_item_image, images) {
			@Override
			public void convert(ViewHolder holder, Bitmap bitmap) {
				LayoutParams pageParms = holder.getView(R.id.assess_image).getLayoutParams();
				pageParms.width = (Tools.M_SCREEN_WIDTH - 40) / 10;
				pageParms.height = (Tools.M_SCREEN_WIDTH - 40) / 10;
				holder.setImageView(R.id.assess_image, bitmap);
			}
		};
		img_photo.setAdapter(imgAdapter);
		img_photo.setOnItemClickListener(this);// 设置GridView的监听事件
		toggleButton = (ToggleButton) findViewById(R.id.toggle_on_off);// 设置全天开关
		layout_starttime = (LinearLayout) findViewById(R.id.layout_starttime);// 设置开始时间
		tv_starttime = (TextView) findViewById(R.id.input_task_starttime);// 设置开始时间
		layout_finishtime = (LinearLayout) findViewById(R.id.layout_finishtime);// 设置结束时间
		tv_finishtime = (TextView) findViewById(R.id.input_task_finishtime);// 设置结束时间
		button_fridview = (GridView) findViewById(R.id.button_fridview);// 读取相册
		button_fridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
		button_fridview.setAdapter(btnAdapter);
		button_fridview.setOnItemClickListener(this);
		toggleButton.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件

		String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm");
		tv_starttime.setText(time);
		tv_finishtime.setText(time);
		setListener(img_read_contacts, ed_taskexcutor, layout_starttime, layout_finishtime);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.img_read_contacts:// 执行人，读取通讯录
			startActivityForResult(new Intent(this, B3_1_SelectExecutorActivity.class), 20);
			break;
		case R.id.input_taskexcutor:// 执行人，读取通讯录
			startActivityForResult(new Intent(this, B3_1_SelectExecutorActivity.class), 20);
			break;
		case R.id.layout_starttime:// 开始时间
			View startView = CommonUtils.showDateTimePicker(this, tv_starttime);
			startView.findViewById(R.id.hour).setVisibility(isday == 0?View.VISIBLE:View.GONE);
			startView.findViewById(R.id.mins).setVisibility(isday == 0?View.VISIBLE:View.GONE);
//			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(this, null);
//			dateTimePicKDialog.dateTimePicKDialog(tv_starttime);
			break;
		case R.id.layout_finishtime:// 结束时间
			View endView = CommonUtils.showDateTimePicker(this, tv_finishtime);
			endView.findViewById(R.id.hour).setVisibility(isday == 0?View.VISIBLE:View.GONE);
			endView.findViewById(R.id.mins).setVisibility(isday == 0?View.VISIBLE:View.GONE);
//			DateTimePickDialogUtil dateTimePicKDialog2 = new DateTimePickDialogUtil(this, null);
//			dateTimePicKDialog2.dateTimePicKDialog(tv_finishtime);
			break;
		case R.id.aci_edit_btn:// 创建任务
			title = ed_taskname.getText().toString().trim();// 任务名称
			content = ed_taskcontent.getText().toString().trim();// 任务内容
			// 读取相册
			// 设置全天开关
			starttime = tv_starttime.getText().toString().trim();
			endtime = tv_finishtime.getText().toString().trim();
			long distance = 0;
			try {
				long startTime = DateUtil.stringToLong(starttime, isday == 0?"yyyy-MM-dd HH:mm":"yyyy-MM-dd");
				long endTime = DateUtil.stringToLong(endtime, isday == 0?"yyyy-MM-dd HH:mm":"yyyy-MM-dd");
				distance = startTime - endTime;
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if(StringUtil.isEmpty(title)){
				Tools.toast(this, "任务名字不能为空!");
			}else if(StringUtil.isEmpty(strId)){
				Tools.toast(this, "至少选择一个执行人!");
			}else if(StringUtil.isEmpty(strId)){
				Tools.toast(this, "任务内容不能为空!");
			}else if(StringUtil.isEmpty(strId)){
				Tools.toast(this, "至少选择一个执行人!");
			}else if(tixing < 0){
				Tools.toast(this, "请选择提醒状态!");
			}else if(chongfu < 0){
				Tools.toast(this, "请选择重复状态!");
			}else if(distance > 0){
				Tools.toast(this, "结束时间不能比开始时间早!");
			}else{
				MyRequestDailog.showDialog(this, "");
				params = new RequestParams();
				params.put("uid", BaseApp.getModel().getUserid());//用户Id
				params.put("title", title);//任务名称
				params.put("content", content);//任务内容
				params.put("isday", isday);//是否是全天任务 0不是 1是
				params.put("start", starttime);//任务开始时间
				params.put("end", endtime);//任务结束时间
				params.put("tip", tixing);//提醒
				params.put("repeat", chongfu);//重复
				params.put("tasker", strId);//任务执行人
				index = 0;
				if(files.size() < 1){
					submitTask();
				}else{
					try {
						RequestParams paramsImgage = new RequestParams();
						paramsImgage.put("imgsrc[]", files.get(index));
						HttpUtils.upLoad(res_upLoad, paramsImgage);//上传图片
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
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
	
	private AsyncHttpResponseHandler res_upLoad = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			try {
				String imgsrc = json.getJSONArray(UrlContants.jsonData).getJSONObject(0).getString("imgsrc");
				params.put("imgsrc"+(++index), imgsrc);//任务执行人
				if(index < files.size()){
					RequestParams paramsImgage = new RequestParams();
					paramsImgage.put("imgsrc[]", files.get(index));
					HttpUtils.upLoad(res_upLoad, paramsImgage);
				}else{
					submitTask();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	};

	private void submitTask() {
		HttpUtils.addTask(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				MyRequestDailog.closeDialog();
				Tools.toast(B3_TaskAddTaskTaskActivity.this, "任务添加成功!");
				finish();
			}
		}, params);
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
		case 20:
			/* 选择执行人 */
			if (data != null) {
				ed_taskexcutor.setText(data.getStringExtra("strName"));
				strId = data.getStringExtra("strId");
				ed_taskexcutor.setTag(strId);
			}
			break;
		case 21:
			/* 选择执行人 */
			if (data != null) {
				tixing = data.getIntExtra("position", 0);
				taskType.set(0, tixing==0?"正点":tixing==1?"五分钟前":tixing==2?"十分钟前":tixing==3?"一小时之前":tixing==4?"一天前":tixing==5?"三天前":"不提醒");
				flags[0] = true;
				btnAdapter.notifyDataSetChanged();
			}
			break;
		case 22:
			/* 选择执行人 */
			if (data != null) {
				chongfu = data.getIntExtra("position", 0);
				taskType.set(1, chongfu==0?"不重复":chongfu==1?"每天":chongfu==2?"每周":chongfu==3?"每月":"每年");
				flags[1] = true;
				btnAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
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
		images.add(bitmap);
		files.add(file);
		imgAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//Tools.toast(B3_TaskAddTaskTaskActivity.this, "请先设置起始时间");
		String time1 = StringUtil.toString(tv_starttime.getText());
		String time2 = StringUtil.toString(tv_finishtime.getText());
		if (isChecked) {
			isday = 1;
			tv_starttime.setText(time1.substring(0, 10));
			tv_starttime.setTag(time1);
			tv_finishtime.setText(time2.substring(0, 10));
			tv_finishtime.setTag(time2);
		} else {
			isday = 0;
			tv_starttime.setText((String)tv_starttime.getTag());
			tv_finishtime.setText((String)tv_finishtime.getTag());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		switch (parent.getId()) {
		case R.id.img_photo:
			if (position == 0) {
				if (files.size() < 3) {
					UIDialog.ForThreeBtn(this, new String[] { "拍照", "从相册中选取", "取消" }, this);
				} else {
					Tools.toast(this, "最多上传三张图片");
				}
			}
			break;
		case R.id.button_fridview:
			if (position == 0) {
				/*设置提醒*/
				startActivityForResult(new Intent(this, B3_1_TiXingActivity.class).putExtra("position", tixing<0?0:tixing),21);
			}else if(position == 1){
				/*设置重复*/
				startActivityForResult(new Intent(this, B3_1_RepeatActivity.class).putExtra("position", chongfu<0?0:chongfu),22);
			}else{
				/*设置位置*/
			}
			break;
		default:
			break;
		}
	}
}
