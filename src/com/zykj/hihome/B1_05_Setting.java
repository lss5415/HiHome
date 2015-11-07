package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author LSS 2015年10月19日 下午5:23:41
 * 
 */
public class B1_05_Setting extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private LinearLayout ly_task_permission, ly_message_notice,
			ly_sound_notice, ly_chat_record, ly_version_update,
			ly_function_intro, ly_help_feedback, ly_logout, ly_cancle;
	private TextView ly_version;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting);
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("设置");
		
		ly_task_permission = (LinearLayout) findViewById(R.id.ly_task_permission);// 任务权限
		ly_message_notice = (LinearLayout) findViewById(R.id.ly_message_notice);// 消息通知
		ly_sound_notice = (LinearLayout) findViewById(R.id.ly_sound_notice);// 声音通知
		ly_chat_record = (LinearLayout) findViewById(R.id.ly_chat_record);// 聊天记录
		ly_version_update = (LinearLayout) findViewById(R.id.ly_version_update);// 版本更新
		ly_version = (TextView) findViewById(R.id.ly_version);// 版本号
		ly_function_intro = (LinearLayout) findViewById(R.id.ly_function_intro);// 功能介绍
		ly_help_feedback = (LinearLayout) findViewById(R.id.ly_help_feedback);// 帮助与反馈
		ly_logout = (LinearLayout) findViewById(R.id.ly_logout);// 注销登录
		ly_cancle = (LinearLayout) findViewById(R.id.ly_cancle);// 退出账号
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		setListener(ly_task_permission,ly_message_notice,ly_sound_notice,ly_chat_record,ly_version_update,ly_function_intro,ly_help_feedback,ly_logout,ly_cancle);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ly_task_permission:// 任务权限
			startActivity(new Intent(this, B1_05_Setting_Detail.class).putExtra("position", 1));
			break;
		case R.id.ly_message_notice:// 消息通知
			startActivity(new Intent(this, B1_05_Setting_Detail.class).putExtra("position", 2));
			break;
		case R.id.ly_sound_notice:// 声音通知
			startActivity(new Intent(this, B1_05_Setting_Detail.class).putExtra("position", 3));
			break;
		case R.id.ly_chat_record:// 聊天记录
			startActivity(new Intent(this, B1_05_Setting_Detail.class).putExtra("position", 4));
			break;
		case R.id.ly_version_update:// 版本更新
			String version = ly_version.getText().toString().trim();
			HttpUtils.getNew(res_getNew, version.substring(1));
			break;
		case R.id.ly_function_intro:// 功能介绍
			startActivity(new Intent(this, B1_05_Setting_AppInfo.class));
			break;
		case R.id.ly_help_feedback:// 帮助与反馈
			startActivity(new Intent(this, B1_05_Setting_Help.class));
			break;
		case R.id.ly_logout:// 注销登录
			BaseApp.getModel().clear();
			startActivity(new Intent(this, B4_01_LoginActivity.class));
			BaseApp.getInstance().finishAllActivity();
			break;
		case R.id.ly_cancle:// 退出账号
			break;
		default:
			break;
		}
	}
	
	private AsyncHttpResponseHandler res_getNew = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			String version = json.getJSONObject(UrlContants.jsonData).getString("version");
			String download = json.getJSONObject(UrlContants.jsonData).getString("downurl");
			Tools.toast(B1_05_Setting.this, "version="+version+",download="+download);
		}
		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			Tools.toast(B1_05_Setting.this, "最新版本");
		}
	};
}
