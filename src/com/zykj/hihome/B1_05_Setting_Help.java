package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author csh 2015-11-06
 * 帮助与反馈
 */
public class B1_05_Setting_Help extends BaseActivity {
	
	private MyCommonTitle myCommonTitle;
	private EditText aci_title,aci_content;
	private Button aci_submit;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting_help);
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("帮助与反馈");
		
		aci_title = (EditText) findViewById(R.id.aci_title);// 标题
		aci_content = (EditText) findViewById(R.id.aci_content);// 内容
		aci_submit = (Button) findViewById(R.id.aci_submit);// 内容
		setListener(aci_submit);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_submit:// 提交
			String title = aci_title.getText().toString().trim();// 任务名称
			String content = aci_content.getText().toString().trim();// 任务内容
			if(StringUtil.isEmpty(title)){
				Tools.toast(this, "标题不能为空!");
			}else if(StringUtil.isEmpty(content)){
				Tools.toast(this, "宝贵意见不能为空!");
			}else{
				RequestParams params = new RequestParams();
				params.put("uid", BaseApp.getModel().getUserid());
				params.put("title", title);
				params.put("content", content);
				HttpUtils.addInfo(new HttpErrorHandler() {
					@Override
					public void onRecevieSuccess(JSONObject json) {
						Tools.toast(B1_05_Setting_Help.this, json.getString("message"));
						B1_05_Setting_Help.this.finish();
					}
					@Override
					public void onRecevieFailed(String status, JSONObject json) {
						Tools.toast(B1_05_Setting_Help.this, json.getString("message"));
					}
				}, params);
			}
			break;
		default:
			break;
		}
	}
}
