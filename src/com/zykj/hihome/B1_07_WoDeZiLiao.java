package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;

/**
 * @author LSS 2015年10月23日 下午3:24:05
 * 
 */
public class B1_07_WoDeZiLiao extends BaseActivity {
	private ImageView im_b107_back;// 返回
	private TextView tv_bianji;// 编辑资料
	private TextView tv_name;// 姓名
	private TextView tv_sex;// 性别
	private TextView tv_age;// 年龄
	private TextView tv_mobile;// 手机号
	private TextView tv_sign;// 签名
	private CircularImage im_me_avatar;// 头像
	private LinearLayout ly_photo, ly_todayTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_1_wodeziliao);
		initView();
	}

	private void initView() {
		im_b107_back = (ImageView) findViewById(R.id.im_b107_back);
		tv_bianji = (TextView) findViewById(R.id.tv_bianji);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		tv_sign = (TextView) findViewById(R.id.tv_sign);
		im_me_avatar = (CircularImage) findViewById(R.id.im_me_avatar);

		ly_photo = (LinearLayout) findViewById(R.id.ly_photo);
		ly_todayTask = (LinearLayout) findViewById(R.id.ly_todayTask);

		/**
		 * 请求数据
		 */
		requestData();

		setListener(im_b107_back, tv_bianji, ly_photo, ly_todayTask);
	}

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
								"http://"), im_me_avatar);
				tv_name.setText(jsonObject.getString("nick"));
				tv_sex.setText(jsonObject.getString("sex"));
				tv_age.setText(jsonObject.getString("age"));
				tv_mobile.setText(BaseApp.getModel().getMobile());
				// tv_mobile.setText(jsonObject.getString("age"));
				tv_sign.setText(jsonObject.getString("sign"));

			}
		}, BaseApp.getModel().getUserid());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 11:
			requestData();
			break;

		default:
			break;
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b107_back:
			this.finish();
			break;
		case R.id.tv_bianji:
			Intent intent = new Intent(this, B1_07_1_GeRenXinXi.class);
			startActivityForResult(intent, 11);
			requestData();
			break;
		case R.id.ly_photo:
			startActivity(new Intent(B1_07_WoDeZiLiao.this, B1_04_XiangCe.class));
			break;
		case R.id.ly_todayTask:
			startActivity(new Intent(B1_07_WoDeZiLiao.this,
					B1_09_TodayTaskActivity.class));
			break;
		default:
			break;
		}
	}
}
