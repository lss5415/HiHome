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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CommonUtils;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

public class B1_07_1_GeRenXinXi extends BaseActivity {
	private File file;
	private String timeString, imgs;// 上传头像的字段
	private MyCommonTitle myCommonTitle;
	private ImageView im_touxiang;// 头像
	private EditText et_nick;// 姓名
	private TextView tv_mobile;// 电话
	private ImageView im_nan;// 男
	private ImageView im_nv;// 女
	// private DatePicker datePicker1;//年龄
	private EditText et_age;// 年龄
	private EditText et_sign;// 签名
	private TextView tv_queding;// 确定
	private int sex = 0;// 0：男 1：女
	private String nick, age, sign, sex1;
	private RequestParams params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_1_gerenxinxi);
		initView();
	}

	private void initView() {
		myCommonTitle=(MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("个人资料");
		
		im_touxiang = (ImageView) findViewById(R.id.img_avator);
		et_nick = (EditText) findViewById(R.id.et_username);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		im_nan = (ImageView) findViewById(R.id.img_nan);
		im_nv = (ImageView) findViewById(R.id.img_nv);
		// datePicker1 = (DatePicker)findViewById(R.id.datePicker1);
		et_age = (EditText) findViewById(R.id.et_user_age);
		et_sign = (EditText) findViewById(R.id.et_user_sign);
		tv_queding = (TextView) findViewById(R.id.tv_queding);

		requestData();

		setListener(im_touxiang, et_age,im_nan, im_nv, tv_queding);
	}

	/**
	 * 请求数据
	 */
	private void requestData() {
		HttpUtils.getInfo(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData)
						.getJSONObject(0);
				ImageLoader.getInstance().displayImage(
						StringUtil.toString(
								HttpUtils.IMAGE_URL
										+ jsonObject.getString("avatar"),
								"htp://"), im_touxiang);
				et_nick.setText(jsonObject.getString("nick"));
				et_age.setText(jsonObject.getString("age"));
				tv_mobile.setText(BaseApp.getModel().getMobile());
				// tv_mobile.setText(jsonObject.getString("age"));
				et_sign.setText(jsonObject.getString("sign"));
				if (jsonObject.getString("sex").equals("男")) {
					sex = 0;
					im_nan.setImageResource(R.drawable.xuanzhong);
					im_nv.setImageResource(R.drawable.buxuanzhong);
				} else {
					sex = 1;
					im_nan.setImageResource(R.drawable.buxuanzhong);
					im_nv.setImageResource(R.drawable.xuanzhong);
				}

			}
		}, BaseApp.getModel().getUserid());

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
	
		case R.id.img_nan:
			sex = 0;
			im_nan.setImageResource(R.drawable.xuanzhong);
			im_nv.setImageResource(R.drawable.buxuanzhong);
			break;
		case R.id.img_nv:
			sex = 1;
			im_nan.setImageResource(R.drawable.buxuanzhong);
			im_nv.setImageResource(R.drawable.xuanzhong);
			break;
//		case R.id.et_user_age:
//			View startView = CommonUtils.showDateTimePicker(this, et_age);
//			startView.findViewById(R.id.hour).setVisibility(View.GONE);
//			startView.findViewById(R.id.mins).setVisibility(View.GONE);
//			break;
		case R.id.tv_queding:
			RequestParams params = new RequestParams();
			nick = et_nick.getText().toString();
			age = et_age.getText().toString();
			sign = et_sign.getText().toString();
			params.put("id", BaseApp.getModel().getUserid());
			params.put("nick", nick);
			if (sex > 0) {
				sex1 = "女";
				params.put("sex", sex1);
			} else {
				sex1 = "男";
				params.put("sex", sex1);
			}
			params.put("age", age);
			params.put("sign", sign);
			HttpUtils.modfyUserInfo(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					Result();
				}

				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					super.onRecevieFailed(status, json);
					Tools.toast(B1_07_1_GeRenXinXi.this, "修改资料失败！");

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
		im_touxiang.setImageBitmap(bitmap);
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
				Tools.toast(B1_07_1_GeRenXinXi.this, "头像上传成功");
				BaseApp.getModel().setAvatar(UrlContants.IMAGE_URL+imgAvtor);
				setResult(RESULT_OK);
			}
		}, params);
		
	}
};
	public void Result() {
		BaseApp.getModel().setNick(nick);
		BaseApp.getModel().setSex(sex1);
		BaseApp.getModel().setAge(age);
		BaseApp.getModel().setSign(sign);
		Tools.toast(B1_07_1_GeRenXinXi.this, "资料修改成功！");
		setResult(RESULT_OK);
		this.finish();
	}
}
