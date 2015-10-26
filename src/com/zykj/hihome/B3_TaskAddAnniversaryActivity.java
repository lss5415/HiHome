package com.zykj.hihome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.DateTimePickDialogUtil;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

public class B3_TaskAddAnniversaryActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ImageView img_photo;
	private CircularImage img_avator;
	private EditText anniversaryt_title,anniversaryt_content;
	private TextView anniversaryt_selecttime;
	private File file;
	private String timeString;// 上传头像的字段
	private String anni_title, anni_time, anni_content;

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
		
		img_avator = (CircularImage) findViewById(R.id.img_avator);// 上传头像
		img_photo = (ImageView) findViewById(R.id.img_photo);// 上传相册图片

		String time = DateUtil.dateToString(new Date(), "yyyy-MM-dd hh:mm");
		anniversaryt_selecttime.setText(time);
		
		setListener(anniversaryt_selecttime, img_avator);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input_selectdate:// 选择时间
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					B3_TaskAddAnniversaryActivity.this, null);
			dateTimePicKDialog.dateTimePicKDialog(anniversaryt_selecttime);
			break;
		case R.id.img_avator:// 调用手机相册
			UIDialog.ForThreeBtn(this, new String[] { "拍照", "从相册中选取", "取消" },
					this);
			break;
		case R.id.aci_edit_btn:// 完成创建
			anni_title = anniversaryt_title.getText().toString().trim();
			anni_time = anniversaryt_selecttime.getText().toString().trim();
			anni_content = anniversaryt_content.getText().toString().trim();
			if (file == null) {
				Tools.toast(B3_TaskAddAnniversaryActivity.this, "头像不能为空");
			} else if (StringUtil.isEmpty(anni_title)) {
				Tools.toast(B3_TaskAddAnniversaryActivity.this, "标题不能为空");
			} else if (StringUtil.isEmpty(anni_time)) {
				Tools.toast(B3_TaskAddAnniversaryActivity.this, "日期不能为空");
			} else if (StringUtil.isEmpty(anni_content)) {
				Tools.toast(B3_TaskAddAnniversaryActivity.this, "内容不能为空");
			} else {
				try {
					RequestParams params = new RequestParams();
					params.put("imgsrc[]", file);
					HttpUtils.upLoad(res_upLoad, params);// 上传图片

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

	private AsyncHttpResponseHandler res_upLoad = new HttpErrorHandler() {

		@Override
		public void onRecevieSuccess(JSONObject json) {
			String imgsrc = json.getJSONArray(UrlContants.jsonData).getJSONObject(0)
					.getString("imgsrc");
			
			RequestParams params = new RequestParams();

			params.put("uid", BaseApp.getModel().getUserid());
			params.put("imgsrc", imgsrc);
			params.put("title", anni_title);
			params.put("mdate", anni_time);
			params.put("content", anni_content);
			HttpUtils.addAnniversary(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					Tools.toast(B3_TaskAddAnniversaryActivity.this, "信息发布成功");
					setResult(RESULT_OK);
					finish();
				}
			}, params);
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
		img_avator.setImageBitmap(bitmap);
	}

}
