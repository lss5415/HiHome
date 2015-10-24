package com.zykj.hihome;

import org.apache.http.Header;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.CommonUtils;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.RequestDailog;

/**
 * @author LSS 2015年9月29日 上午9:19:36
 * 
 */
public class B4_1_LoginActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private TextView tv_regist, tv_forgetPassWord;
	private EditText et_username, et_passWord;
	private Button btn_login;
	private ImageView img_qq, img_weixin;
	private String username, passWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_1_login);

		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("登录");
		tv_regist = (TextView) findViewById(R.id.tv_new_user);// 新用户注册
		tv_forgetPassWord = (TextView) findViewById(R.id.tv_forgetPassWord); // 忘记密码
		et_username = (EditText) findViewById(R.id.user_name);// 用户名
		et_passWord = (EditText) findViewById(R.id.user_password);// 密码
		btn_login = (Button) findViewById(R.id.btn_login);// 登录

		img_qq = (ImageView) findViewById(R.id.img_qq);
		img_weixin = (ImageView) findViewById(R.id.img_weixin);

		setListener(tv_regist, tv_forgetPassWord, btn_login, img_qq, img_weixin);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_new_user:// 新用户注册
			startActivity(new Intent(B4_1_LoginActivity.this,
					B4_1_RegisterActivity.class));
			break;
		case R.id.tv_forgetPassWord:// 忘记密码
			startActivity(new Intent(B4_1_LoginActivity.this,
					B4_1_ForgetPassWordActivity.class));
			break;
		case R.id.btn_login:
			username = et_username.getText().toString().trim();
			passWord = et_passWord.getText().toString().trim();
			if (StringUtil.isEmpty(username)) {
				Tools.toast(B4_1_LoginActivity.this, "用户名不能为空");
			} else if (StringUtil.isEmpty(passWord)) {
				Tools.toast(B4_1_LoginActivity.this, "密码不能为空");
			} else {
				RequestParams params = new RequestParams();
				params.put("mob", username);
				params.put("pass", passWord);
				HttpUtils.login(new HttpErrorHandler() {

					@Override
					public void onRecevieSuccess(JSONObject json) {
						Tools.toast(B4_1_LoginActivity.this, "登录成功");
						startActivity(new Intent(B4_1_LoginActivity.this,
								B0_MainActivity.class));
						finish();
					}

					@Override
					public void onRecevieFailed(String status, JSONObject json) {
						super.onRecevieFailed(status, json);
						Tools.toast(B4_1_LoginActivity.this, "用户名不存在或者密码不正确");

					}
				}, params);
			}
			break;
		case R.id.img_qq:

			break;
		case R.id.img_weixin:

			break;
		default:
			break;

		}
	}
}
