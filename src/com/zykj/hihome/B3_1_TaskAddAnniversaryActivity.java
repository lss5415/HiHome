package com.zykj.hihome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.CommonUtils;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

public class B3_1_TaskAddAnniversaryActivity extends BaseActivity implements
		OnItemClickListener {
	
	private MyCommonTitle myCommonTitle;
	private GridView img_photo;
	private CircularImage img_avator;
	private EditText anniversaryt_title, anniversaryt_content;
	private TextView anniversaryt_selecttime;
	private File file1,file;
	private String timeString, imgs;// 上传头像的字段
	private static String anni_title;
	private static String anni_time;
	private static String anni_content;
	private int type = 1, index;//1头像 2详情
	private List<File> files = new ArrayList<File>();
	private List<Bitmap> images = new ArrayList<Bitmap>();
	private CommonAdapter<Bitmap> imgAdapter;
	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_taskaddanniversary);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("创建纪念日");
		myCommonTitle.setEditTitle("完成");
		anniversaryt_title = (EditText) findViewById(R.id.input_anniversaryl_title);
		anniversaryt_content = (EditText) findViewById(R.id.input_taskcontent);
		anniversaryt_selecttime = (TextView) findViewById(R.id.input_selectdate);
		img_avator = (CircularImage) findViewById(R.id.img_avator);// 头像
		
		img_photo = (GridView) findViewById(R.id.img_photo_gridview);// 上传相册图片
		img_photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
		images.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_task_imgview));
		imgAdapter = new CommonAdapter<Bitmap>(this, R.layout.ui_b3_item_image,
				images) {
			@Override
			public void convert(ViewHolder holder, Bitmap bitmap) {
				LayoutParams pageParms = holder.getView(R.id.assess_image)
						.getLayoutParams();
				pageParms.width = (Tools.M_SCREEN_WIDTH - 40) / 10;
				pageParms.height = (Tools.M_SCREEN_WIDTH - 40) / 10;
				holder.setImageView(R.id.assess_image, bitmap);
			}
		};
		img_photo.setAdapter(imgAdapter);
		img_photo.setOnItemClickListener(this);// 设置GridView的监听事件

		String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd hh:mm");
		anniversaryt_selecttime.setText(time);

		setListener(anniversaryt_selecttime, img_avator);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_avator:
			//头像
			type = 1;
			UIDialog.ForThreeBtn(this, new String[] { "拍照", "从相册中选取", "取消" },
					this);
			break;
		case R.id.input_selectdate:// 选择时间
			View startView = CommonUtils.showDateTimePicker(this, anniversaryt_selecttime);
			break;
		case R.id.aci_edit_btn:// 完成创建
			anni_title = anniversaryt_title.getText().toString().trim();
			anni_time = anniversaryt_selecttime.getText().toString().trim();
			anni_content = anniversaryt_content.getText().toString().trim();
			if (file == null) {///////////////////////////////
				Tools.toast(B3_1_TaskAddAnniversaryActivity.this, "头像不能为空");
			} else if (StringUtil.isEmpty(anni_title)) {
				Tools.toast(B3_1_TaskAddAnniversaryActivity.this, "标题不能为空");
			} else if (StringUtil.isEmpty(anni_time)) {
				Tools.toast(B3_1_TaskAddAnniversaryActivity.this, "日期不能为空");
			} else if (StringUtil.isEmpty(anni_content)) {
				Tools.toast(B3_1_TaskAddAnniversaryActivity.this, "内容不能为空");
			} else {
					try {
						params = new RequestParams();
						params.put("imgsrc[]", file1);
						HttpUtils.upLoad(res_upLoad1, params);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
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
	private  final AsyncHttpResponseHandler res_upLoad1 = new HttpErrorHandler() {
		
		@Override
		public void onRecevieSuccess(JSONObject json) {
			String imgAvtor=json.getJSONArray(UrlContants.jsonData).getJSONObject(0).getString("imgsrc");
			params=new RequestParams();
			params.put("imgsrc", imgAvtor);
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("title", anni_title);
			params.put("mdate", anni_time.substring(0, 11));
			params.put("content", anni_content);
			
		
		index = 0;
		imgs = "";
		if (files.size() < 1) {///////////////////
			submitAnniData();
		} else {
			try {
				RequestParams paramsImage = new RequestParams();
				paramsImage.put("imgsrc[]", files.get(index));
				HttpUtils.upLoad(res_upLoad, paramsImage);// 上传图片
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}			
		}
	};
	/**
	 * 提交数据
	 */
	private void submitAnniData() {
		HttpUtils.addAnniversary(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B3_1_TaskAddAnniversaryActivity.this, "信息发布成功");
				setResult(RESULT_OK);
				finish();
			}
		}, params);
	}

	/**
	 * 上传图片
	 */
	private AsyncHttpResponseHandler res_upLoad = new HttpErrorHandler() {

		@Override
		public void onRecevieSuccess(JSONObject json) {
			try {
				String imgsrc = json.getJSONArray(UrlContants.jsonData)
						.getJSONObject(0).getString("imgsrc");
				params.put("imgsrc" + (++index), imgsrc);
				if (index < files.size()) {
					RequestParams paramsImage = new RequestParams();
					paramsImage.put("imgsrc[]", files.get(index));
					HttpUtils.upLoad(res_upLoad, paramsImage);// 上传图片
				} else {
					submitAnniData();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	};

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
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
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
		if (type == 1) {
			img_avator.setImageBitmap(bitmap);//头像
			file1=file;
		} else {
			images.add(bitmap);
			files.add(file);
			imgAdapter.notifyDataSetChanged();//详情
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.img_photo_gridview:
			if (position == 0) {
				if (files.size() < 3) {
					type = 2;
					UIDialog.ForThreeBtn(this, new String[] { "拍照", "从相册中选取",
							"取消" }, this);
				} else {
					Tools.toast(this, "最多上传三张图片");
				}
			}
		default:
			break;
		}
	}
}
