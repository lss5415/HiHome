package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_RenWuCreateAnniversaryActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ImageView img_photo, img_camere, img_input_contentimg;
	private LinearLayout ly_add_img;
	private TextView tv_anniversaryTitle, tv_selecttime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_createanniversaryl);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);

		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("创建纪念日");
		myCommonTitle.setEditTitle("完成");
		tv_anniversaryTitle = (TextView) findViewById(R.id.input_anniversaryl_title);
		tv_selecttime = (TextView) findViewById(R.id.input_selectdate);

		img_input_contentimg = (ImageView) findViewById(R.id.img_input_contentimg);
		img_photo = (ImageView) findViewById(R.id.img_photo);
		img_camere = (ImageView) findViewById(R.id.img_camere);
		ly_add_img = (LinearLayout) findViewById(R.id.ly_add_img);

		setListener(tv_selecttime, img_input_contentimg, img_photo, img_camere);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input_selectdate:// 选择时间

			break;
		case R.id.img_input_contentimg:// 为内容添加图片,因为添加图片的控件隐藏了，先设置ly_add_img可见

			break;
		case R.id.img_photo:// 调用手机相册

			break;
		case R.id.img_camere:// 调用手机拍照

			break;
		case R.id.aci_edit_btn://完成创建
			
			break;
		default:
			break;
		}
	}
}
