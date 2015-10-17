package com.zykj.hihome;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
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
			// if (StringUtil.isEmpty(username)) {
			// Tools.toast(B4_1_LoginActivity.this, "用户名不能为空");
			// return;
			// }
			// if (StringUtil.isEmpty(passWord)) {
			// Tools.toast(B4_1_LoginActivity.this, "密码不能为空");
			// return;
			// }
			// HttpUtils.login(res_login, login_name, passWord);
			startActivity(new Intent(B4_1_LoginActivity.this,
					B0_MainActivity.class));
			break;
		case R.id.img_qq:

			break;
		case R.id.img_weixin:

			break;
		default:
			break;

		}
	}

	JsonHttpResponseHandler res_login = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			// RequestDailog.closeDialog();
			// Tools.Log("登陆"+response);
			// {"datas":{"error":"用户名密码错误"},"code":200}
			String error = null;
			JSONObject datas = null;
			// Tools.Log("res_login="+response);
			try {
				datas = response.getJSONObject("datas");
				error = response.getJSONObject("datas").getString("error");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (error == null)// 登陆成功
			{
				try {
					putSharedPreferenceValue("username",
							datas.getString("username"));
					putSharedPreferenceValue("mobile",
							datas.getString("mobile"));
					putSharedPreferenceValue("userid",
							datas.getString("userid"));
					putSharedPreferenceValue("key", datas.getString("key"));
					putSharedPreferenceValue("avatar",
							datas.getString("avatar"));
					// Tools.Log("image="+datas.getString("avatar"));
					putSharedPreferenceValue("member_points",
							datas.getString("member_points"));
					putSharedPreferenceValue("predeposit",
							datas.getString("predeposit"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Tools.toast(B4_1_LoginActivity.this, "登陆成功");
				Intent intent_tomainavtivity = new Intent(
						B4_1_LoginActivity.this, B0_MainActivity.class);
				startActivity(intent_tomainavtivity);
				B4_1_LoginActivity.this.finish();
			} else// 登陆失败
			{
				Tools.toast(B4_1_LoginActivity.this, error + "");
			}

		}

	};
}
