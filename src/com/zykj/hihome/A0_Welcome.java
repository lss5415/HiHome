package com.zykj.hihome;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.AppValue;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

public class A0_Welcome extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_a0_welcome);
		
		checkLogin();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// Intent intent = new Intent(Welcome.this, MainActivity.class);
				// startActivity(intent);

				String is_intro = getSharedPreferenceValue(AppValue.IS_INTRO);
				boolean should_intro = false;
				int version = Tools.getAppVersion(A0_Welcome.this);
				String save_version = getSharedPreferenceValue(AppValue.VERSION);
				int save_version_int = save_version.equals("") ? -1 : Integer
						.parseInt(save_version);

				if (is_intro.length() > 0 && version == save_version_int) {// �Ѿ����й�ָ��,�Ұ汾�ŷ���
					should_intro = false;
				} else {
					should_intro = true;
				}

				if (should_intro) {
					Intent intent = new Intent(A0_Welcome.this,A1_IntroActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(A0_Welcome.this, B4_1_LoginActivity.class);
					startActivity(intent);
				}
				finish();

			}
		};
		timer.schedule(task, 2000);
	}
	
	private void checkLogin(){
		if(StringUtil.isEmpty(BaseApp.getModel().getUsername())){
			return;
		}
        RequestParams params = new RequestParams();
        params.put("mob", BaseApp.getModel().getMobile());
        params.put("pass", BaseApp.getModel().getPassword());
        HttpUtils.login(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject data = json.getJSONArray(UrlContants.jsonData).getJSONObject(0);
				String avatar = StringUtil.toStringOfObject(data.getString("avatar"));
//				BaseApp.getModel().setAvatar(avatar.replace("app.do", UrlContants.SERVERIP));//头像
				BaseApp.getModel().setAvatar(StringUtil.toStringOfObject(data.getString("avatar")));
				BaseApp.getModel().setUserid(StringUtil.toStringOfObject(data.getString("id")));
				BaseApp.getModel().setMobile(StringUtil.toStringOfObject(data.getString("mobile")));
				BaseApp.getModel().setPassword(BaseApp.getModel().getPassword());//登录密码
				BaseApp.getModel().setNick(StringUtil.toStringOfObject(data.getString("nick")));
				BaseApp.getModel().setSex(StringUtil.toStringOfObject(data.getString("sex")));
				BaseApp.getModel().setAge(StringUtil.toStringOfObject(data.getString("age")));
				BaseApp.getModel().setSign(StringUtil.toStringOfObject(data.getString("sign")));
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				BaseApp.getModel().clear();
				Tools.toast(A0_Welcome.this, "登录失效!");
			}
		}, params);
	}	
}
