package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_WoDeActivity extends BaseActivity {
	private ImageView im_denglu;//登录
	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b4_wode);
		initView();
	}


	public void initView() {
		im_denglu = (ImageView) findViewById(R.id.im_denglu);
//		if (isLoged()) {
//			//请求头像，如果没有头像，提示用户上传头像
//			ll_yidenglu.setVisibility(View.VISIBLE);
//			im_denglu.setVisibility(View.GONE);
//			tv_invitecode.setText(getSharedPreferenceValue("invitecode"));
//			tv_username.setText(getSharedPreferenceValue("name"));
//		}else {
//			//未登陆，不做操作
//			ll_yidenglu.setVisibility(View.GONE);
//			im_denglu.setVisibility(View.VISIBLE);
//		}
		
		setListener(im_denglu);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_denglu:
				Intent itdenglu = new Intent();
				itdenglu.setClass(B4_WoDeActivity.this, B4_1_DengLuActivity.class);
				startActivity(itdenglu);
			break;
		
		default:
			break;

		}
	}
//	public  boolean isLoged() {
//		String isLoged = null;
//		if (getSharedPreferenceValue("isLoged")!=null) {
//			isLoged = getSharedPreferenceValue("isLoged");
//			if (isLoged.equals("1")) {
//				return true;
//			}else {
//				return false;
//			}
//		}else {
//			putSharedPreferenceValue("isLoged", "0");
//			return false;
//		}
//	}
//	
}