package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.CircularImage;

/**
 * @author LSS 2015年10月23日 下午3:24:05
 *
 */
public class B1_07_WoDeZiLiao extends BaseActivity {
	private ImageView im_b107_back;//返回
	private TextView tv_bianji;//编辑资料
	private TextView tv_name;//姓名
	private TextView tv_sex;//性别
	private TextView tv_age;//年龄
	private TextView tv_mobile;//手机号
	private TextView tv_sign;//签名
	private CircularImage im_me_avatar;//头像


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_1_wodeziliao);
		initView();
	}

	private void initView() {
		im_b107_back = (ImageView)findViewById(R.id.im_b107_back);
		tv_bianji = (TextView)findViewById(R.id.tv_bianji);
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_sex = (TextView)findViewById(R.id.tv_sex);
		tv_age = (TextView)findViewById(R.id.tv_age);
		tv_mobile = (TextView)findViewById(R.id.tv_mobile);
		tv_sign = (TextView)findViewById(R.id.tv_sign);
		im_me_avatar = (CircularImage)findViewById(R.id.im_me_avatar);
		
		tv_name.setText(BaseApp.getModel().getNick());
		tv_sex.setText(BaseApp.getModel().getSex());
	
		tv_age.setText(BaseApp.getModel().getAge());
		tv_mobile.setText(BaseApp.getModel().getMobile());
		tv_sign.setText(BaseApp.getModel().getSign());
		ImageLoader.getInstance().displayImage(BaseApp.getModel().getAvatar(), im_me_avatar);
		


		 setListener(im_b107_back,tv_bianji);
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
		 startActivity(intent);
		 break;
		default:
			break;
		}
	}
}
