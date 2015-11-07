package com.zykj.hihome;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author csh 2015-11-06
 * 功能介绍
 */
public class B1_05_Setting_AppInfo extends BaseActivity {
	
	private MyCommonTitle myCommonTitle;
	private TextView aci_content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting_appinfo);
		
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);//标题
		
		aci_content = (TextView) findViewById(R.id.aci_content);// 内容
		HttpUtils.getAppInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				myCommonTitle.setTitle(json.getJSONObject(UrlContants.jsonData).getString("title"));
				aci_content.setText(json.getJSONObject(UrlContants.jsonData).getString("content"));
			}
		});
	}
}
