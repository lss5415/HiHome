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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

public class B4_02_RegisterNextActivity extends BaseActivity {
	private File file;
	private String timeString, imgs;// 上传头像的字段
	
	private MyCommonTitle myCommonTitle;
	private ImageView img_avatar;// 头像
	private EditText ed_nick;// 姓名
	private TextView tv_mobile;// 电话
	private ImageView img_nan;// 男
	private ImageView img_nv;// 女
	// private DatePicker datePicker1;//年龄
	private EditText ed_age;// 年龄
	private EditText ed_sign;// 签名
	private TextView tv_queding;// 确定
	private int sex = 0;// 0：男 1：女
	private String nick, age, sign, sex1,mobile;
	private RequestParams params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mobile=(String) getIntent().getSerializableExtra("mobile");
		setContentView(R.layout.ui_b1_07_1_gerenxinxi);
		
		initView();
	}

	private void initView() {
		myCommonTitle=(MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("个人资料");
		img_avatar = (ImageView) findViewById(R.id.img_avator);
		ed_nick = (EditText) findViewById(R.id.et_username);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		img_nan = (ImageView) findViewById(R.id.img_nan);
		img_nv = (ImageView) findViewById(R.id.img_nv);
		// datePicker1 = (DatePicker)findViewById(R.id.datePicker1);
		ed_age = (EditText) findViewById(R.id.et_user_age);
		ed_sign = (EditText) findViewById(R.id.et_user_sign);
		tv_queding = (TextView) findViewById(R.id.tv_queding);
		
		tv_mobile.setText(mobile);
		setListener(img_avatar, img_nan, img_nv, tv_queding);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_nan:
			sex = 0;
			img_nan.setImageResource(R.drawable.xuanzhong);
			img_nv.setImageResource(R.drawable.buxuanzhong);
			break;
		case R.id.img_nv:
			sex = 1;
			img_nan.setImageResource(R.drawable.buxuanzhong);
			img_nv.setImageResource(R.drawable.xuanzhong);
			break;
		case R.id.tv_queding:
			nick = ed_nick.getText().toString();
			age = ed_age.getText().toString();
			sign = ed_sign.getText().toString();
			if(StringUtil.isEmpty(nick)){
				Tools.toast(B4_02_RegisterNextActivity.this, "昵称不能为空");
			}else if (StringUtil.isEmpty(age)) {
				Tools.toast(B4_02_RegisterNextActivity.this, "年龄不能为空");
			}else if (StringUtil.isEmpty(sign)) {
				Tools.toast(B4_02_RegisterNextActivity.this, "个性签名不能为空");
			}
			params = new RequestParams();
			params.put("id", BaseApp.getModel().getUserid());
			params.put("nick", nick);
			params.put("age", age);
			params.put("sign", sign);
			if (sex > 0) {
				sex1 = "女";
				params.put("sex", sex1);
			} else {
				sex1 = "男";
				params.put("sex", sex1);
			}
			HttpUtils.modfyUserInfo(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					BaseApp.getModel().setNick(nick);
					BaseApp.getModel().setSex(sex1);
					BaseApp.getModel().setAge(age);
					BaseApp.getModel().setSign(sign);
//					BaseApp.getModel().setAvatar(imgAvtor);
					Tools.toast(B4_02_RegisterNextActivity.this, "注册成功！");
					setResult(RESULT_OK);
					startActivity(new Intent(B4_02_RegisterNextActivity.this,B0_MainActivity.class));
					finish();
				}

				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					super.onRecevieFailed(status, json);
					Tools.toast(B4_02_RegisterNextActivity.this, "注册个人资料失败！");

				}
			}, params);
			break;
		case R.id.img_avator:
			UIDialog.ForThreeBtn(this, new String[] { "拍照","从相册中选取", "取消" }, this);
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
		img_avatar.setImageBitmap(bitmap);
		upLoadAvatar(file);
		}
	
	
	private void upLoadAvatar(File file) {
			try {
				params=new RequestParams();
				params.put("imgsrc[]", file);
				HttpUtils.upLoad(upload1, params);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
private AsyncHttpResponseHandler upload1 =new HttpErrorHandler() {
	
	@Override
	public void onRecevieSuccess(JSONObject json) {
		final String imgAvtor=json.getJSONArray(UrlContants.jsonData).getJSONObject(0).getString("imgsrc");
		params=new RequestParams();
		params.put("id", BaseApp.getModel().getUserid());
		params.put("imgsrc", imgAvtor);
		HttpUtils.postUserAvatar(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B4_02_RegisterNextActivity.this, "头像上传成功");
				BaseApp.getModel().setAvatar(UrlContants.IMAGE_URL+imgAvtor);
				setResult(RESULT_OK);
				
			}
		}, params);
		
	}
};
}
