package com.zykj.hihome;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_TaskAddAnniversaryActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ImageView img_photo, img_camere, img_input_contentimg,img_avator;
	private LinearLayout ly_add_img;
	private TextView tv_anniversaryTitle, tv_selecttime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_createanniversaryl);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("创建纪念日");
		myCommonTitle.setEditTitle("完成");
		tv_anniversaryTitle = (TextView) findViewById(R.id.input_anniversaryl_title);
		tv_selecttime = (TextView) findViewById(R.id.input_selectdate);
		
		img_avator=(ImageView) findViewById(R.id.img_avator);//上传头像
		img_input_contentimg = (ImageView) findViewById(R.id.img_input_contentimg);//添加图片
		img_photo = (ImageView) findViewById(R.id.img_photo);//上传相册图片
		img_camere = (ImageView) findViewById(R.id.img_camere);//启动相机上传图片
		ly_add_img = (LinearLayout) findViewById(R.id.ly_add_img);//上传图片的LinearLayout

		setListener(tv_selecttime, img_input_contentimg, img_photo, img_camere);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input_selectdate:// 选择时间

			break;
		case R.id.img_input_contentimg:// 为内容添加图片,因为添加图片的控件隐藏了，先设置ly_add_img可见
			ly_add_img.setVisibility(View.VISIBLE);
			img_input_contentimg.setVisibility(View.GONE);
			break;
		case R.id.img_photo:// 调用手机相册
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
		case R.id.img_camere:// 调用手机拍照
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
		case R.id.aci_edit_btn:// 完成创建

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
}
