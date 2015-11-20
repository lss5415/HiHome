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

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.XiangCeLieBiao;
import com.zykj.hihome.data.ZuiJinXiangPian;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;

public class B1_04_01_AddPictureActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private TextView tv_photo_name;
	private EditText tv_pic_descrip;
	private LinearLayout ly_photo_name;
	private GridView gv_add_pic;
	private File file;
	private RequestParams params;
	private List<File> files = new ArrayList<File>();
	private List<Bitmap> images = new ArrayList<Bitmap>();
	private CommonAdapter<Bitmap> picAdapter;
	private String imgs, timeString, photoName, photoId;// 上传头像的字段
	private int index;
	private XiangCeLieBiao  photo ;
	private String picture;
	private ImageView img_camera_pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		picture=(String) getIntent().getSerializableExtra("picture");//点击拍照,保存后的照片传过来
		photo = (XiangCeLieBiao) getIntent().getSerializableExtra("photo");//从相册里点击上传,photo为相册的信息
		setContentView(R.layout.ui_b1_04_01_add_picture);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("上传照片");
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setEditTitle("上传");

		tv_pic_descrip = (EditText) findViewById(R.id.tv_picture_descrip);
		tv_photo_name = (TextView) findViewById(R.id.tv_photo_name);
		ly_photo_name = (LinearLayout) findViewById(R.id.ly_select_photo_name);
		gv_add_pic = (GridView) findViewById(R.id.gv_add_pic);
		img_camera_pic=(ImageView) findViewById(R.id.img_camera_pic);
		/*从相册中添加图片*/
		if(photo!=null){
			tv_photo_name.setText(photo.getTitle());
			photoId=photo.getId();
		}
		gv_add_pic.setSelector(new ColorDrawable(Color.TRANSPARENT));
		/**
		 * 判断picture是否为空,不为空gv_add_pic为传过来的图片,不能再添加图片
		 */
		if(!StringUtil.isEmpty(picture)){
			img_camera_pic.setVisibility(View.VISIBLE);
			gv_add_pic.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(HttpUtils.IMAGE_URL+picture, img_camera_pic);
		}else{
		images.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.ico_photo_add_pic));
		picAdapter = new CommonAdapter<Bitmap>(this, R.layout.ui_b3_item_image,
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
		gv_add_pic.setAdapter(picAdapter);
	
		gv_add_pic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView,
					int position, long checkedid) {
				if (position == 0) {
					if (files.size() < 5) {
						UIDialog.ForThreeBtn(B1_04_01_AddPictureActivity.this,
								new String[] { "拍照", "从相册中选取", "取消" },
								B1_04_01_AddPictureActivity.this);
					} else {
						Tools.toast(B1_04_01_AddPictureActivity.this,
								"最多上传五张图片");
					}
				}
			}

		});// 设置GridView的监听事件
	}
		setListener(ly_photo_name);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ly_select_photo_name:
			/* 选择相册 */
			if(photo==null){
			startActivityForResult(new Intent(B1_04_01_AddPictureActivity.this,
					B1_04_02_SelectPhotoActivity.class), 11);
			}
			break;
		case R.id.aci_edit_btn:
			/* 上传 */
			if (StringUtil.isEmpty(tv_pic_descrip.getText().toString())) {
				Tools.toast(B1_04_01_AddPictureActivity.this, "照片信息描述不能为空");
			} else if (StringUtil.isEmpty(photoId)) {
				Tools.toast(B1_04_01_AddPictureActivity.this, "请选择相册");
			} else if (file == null&&StringUtil.isEmpty(picture)) {
				Tools.toast(B1_04_01_AddPictureActivity.this, "请选择要上传的图片");
			} else {
				try {
					index = 0;//索引
					imgs = "";//路径
					params = new RequestParams();
					params.put("uid", BaseApp.getModel().getUserid());// uid必须，用户ID编号
					params.put("intro", tv_pic_descrip.getText().toString());// // 照片描述
					params.put("aid", photoId);// aid必须，相册ID编号?????????????????
					if(file!=null){
						RequestParams paramsImg = new RequestParams();
						paramsImg.put("imgsrc[]", files.get(index));
						HttpUtils.upLoad(res_upLoad, paramsImg);
					}else{
						params.put("imgsrc", picture);
						addPicToPhoto();
					}

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
		}
	}

	private AsyncHttpResponseHandler res_upLoad = new HttpErrorHandler() {

		@Override
		public void onRecevieSuccess(JSONObject json) {
			try {
				String imgsrc = json.getJSONArray(UrlContants.jsonData)
						.getJSONObject(0).getString("imgsrc");
				
				if(StringUtil.isEmpty(imgs)){
					imgs+=imgsrc;
				}else{
					imgs +=","+imgsrc;
				}
				++index;
				if (index < files.size()) {
					RequestParams paramsImg = new RequestParams();
					paramsImg.put("imgsrc[]", files.get(index));
					HttpUtils.upLoad(res_upLoad, paramsImg);
				} else {
					params.put("imgsrc", imgs);// 照片
					addPicToPhoto();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	};

	private void addPicToPhoto() {
//		params = new RequestParams();
//		params.put("uid", BaseApp.getModel().getUserid());// uid必须，用户ID编号
//		params.put("intro", tv_pic_descrip.getText().toString());// // 照片描述
//		params.put("aid", photoId);// aid必须，相册ID编号?????????????????
//		params.put("imgsrc", imgs);// 照片
		HttpUtils.addPicToPhoto(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B1_04_01_AddPictureActivity.this, "照片添加成功");
				setResult(RESULT_OK);
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
		case 11:
			if (data != null) {
				photoName = data.getStringExtra("photoName");
				photoId = data.getStringExtra("photoId");
				tv_photo_name.setText(photoName);
			}
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
		picAdapter.notifyDataSetChanged();
	}

}
