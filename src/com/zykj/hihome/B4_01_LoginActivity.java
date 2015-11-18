package com.zykj.hihome;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.util.HashMap;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.MyRequestDailog;

/**
 * @author LSS 2015年9月29日 上午9:19:36
 * 
 */
public class B4_01_LoginActivity extends BaseActivity implements Callback,
		PlatformActionListener {
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;

	private MyCommonTitle myCommonTitle;
	private TextView tv_regist, tv_forgetPassWord;
	private EditText et_username, et_passWord;
	private Button btn_login;
	private ImageView img_qq, img_weixin;
	private String username, passWord;
	private String token, uid;
	private Handler mHandler;
	private int thirdLogin = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_1_login);

		mHandler = new Handler(this.getMainLooper(), this);
		ShareSDK.initSDK(this);
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
			startActivity(new Intent(B4_01_LoginActivity.this,
					B4_02_RegisterActivity.class));
			break;
		case R.id.tv_forgetPassWord:// 忘记密码
			startActivity(new Intent(B4_01_LoginActivity.this,
					B4_04_ForgetPassWordActivity.class));
			break;
		case R.id.btn_login:
			username = et_username.getText().toString().trim();
			passWord = et_passWord.getText().toString().trim();

			if (StringUtil.isEmpty("15006598533")) {
				Tools.toast(B4_01_LoginActivity.this, "用户名不能为空");
			} else if (StringUtil.isEmpty("111111")) {
				Tools.toast(B4_01_LoginActivity.this, "密码不能为空");
			} else {
				RequestParams params = new RequestParams();
				params.put("mob", "15006598533");
				params.put("pass", "111111");
				HttpUtils.login(new HttpErrorHandler() {

					@Override
					public void onRecevieSuccess(JSONObject json) {
						JSONArray data = json.getJSONArray(UrlContants.jsonData);
						Tools.toast(B4_01_LoginActivity.this, "登录成功");
						uid = data.getJSONObject(0).getString("id");
						BaseApp.getModel().setUserid(StringUtil.toStringOfObject(uid));
						BaseApp.getModel().setMobile(StringUtil.toStringOfObject(data.getJSONObject(0).getString("mobile")));
						BaseApp.getModel().setNick(StringUtil.toStringOfObject(data.getJSONObject(0).getString("nick")));
						BaseApp.getModel().setAvatar(StringUtil.toStringOfObject(data.getJSONObject(0).getString("avatar")));
						BaseApp.getModel().setSex(StringUtil.toStringOfObject(data.getJSONObject(0).getString("sex")));
						BaseApp.getModel().setAge(StringUtil.toStringOfObject(data.getJSONObject(0).getString("age")));
						BaseApp.getModel().setSign(StringUtil.toStringOfObject(data.getJSONObject(0).getString("sign")));
						
						Log.d("jpush---------------", uid);
						JPushInterface.setAlias(B4_01_LoginActivity.this, uid, new TagAliasCallback() {
							@Override
							public void gotResult(int code, String alias, Set<String> tags) {
								Log.d("jpush---------------", "code="+code+"alias="+alias);
								Tools.toast(B4_01_LoginActivity.this, "code="+code+"alias="+alias+"tags="+tags.toString());
							}
						});
						
						requestData();
					}

					@Override
					public void onRecevieFailed(String status, JSONObject json) {
						super.onRecevieFailed(status, json);
						Tools.toast(B4_01_LoginActivity.this, "用户名不存在或者密码不正确");

					}
				}, params);
			}
			// startActivity(new Intent(B4_1_LoginActivity.this,
			// B0_MainActivity.class));
			break;
		case R.id.img_qq:
			thirdLogin = 1;
			MyRequestDailog.showDialog(this, "");
			Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
			qq.setPlatformActionListener(this);
			qq.SSOSetting(true);
			qq.authorize();
			break;
		case R.id.img_weixin:
			thirdLogin = 1;
			MyRequestDailog.showDialog(this, "");
			Platform weixin = ShareSDK.getPlatform(this, Wechat.NAME);
			weixin.setPlatformActionListener(this);
			weixin.SSOSetting(true);
			weixin.authorize();
			break;
		default:
			break;

		}
	}

	private void requestData() {
		HttpUtils.getToken(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				token = json.getString("token");
				connect(token);
			}

			@Override
			public void onRecevieFailed(String status, JSONObject json) {

			}
		}, uid);
	}

	/**
	 * 建立与融云服务器的连接
	 * 
	 * @param token
	 */
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(BaseApp
				.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				@Override
				public void onTokenIncorrect() {

					Log.d("LoginActivity", "--onTokenIncorrect");
				}

				/**
				 * 连接融云成功
				 * 
				 * @param userid
				 *            当前 token
				 */
				@Override
				public void onSuccess(String userid) {

					Log.d("LoginActivity", "--onSuccess" + userid);
					// startActivity(new Intent(LoginActivity.this,
					// MainActivity.class));
					Toast.makeText(B4_01_LoginActivity.this, "融云连接成功",
							Toast.LENGTH_LONG).show();
					putSharedPreferenceValue("uid", uid);
					BaseApp.getModel().setUserid(
							StringUtil.toStringOfObject(uid));
					startActivity(new Intent(B4_01_LoginActivity.this,
							B0_MainActivity.class));
					finish();
				}

				/**
				 * 连接融云失败
				 * 
				 * @param errorCode
				 *            错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

					Log.d("LoginActivity", "--onError" + errorCode);
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (thirdLogin == 1) {
			MyRequestDailog.showDialog(this, "");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyRequestDailog.closeDialog();
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.what = MSG_AUTH_COMPLETE;
		msg.obj = platform;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		mHandler.sendEmptyMessage(MSG_AUTH_ERROR);
	}

	@Override
	public void onCancel(Platform platform, int action) {
		mHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
	}

	@Override
	public boolean handleMessage(Message msg) {
		thirdLogin = 0;
		switch (msg.what) {
		case MSG_AUTH_CANCEL: {
			// 取消授权
			MyRequestDailog.closeDialog();
			Tools.toast(this, "取消登录");
		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			MyRequestDailog.closeDialog();
			Tools.toast(this, "登录失败");
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			Tools.toast(this, "登录成功");
			Platform platform = (Platform) msg.obj;
			// String icon = platform.getDb().getUserIcon();
			String userid = platform.getDb().getUserId();
			String nickname = platform.getDb().getUserName()
					+ userid.substring(userid.length() - 4, userid.length());
			// String gender = platform.getDb().getUserGender();
			RequestParams params = new RequestParams();
			params.put("mob", nickname);
			params.put("pass", userid);
			HttpUtils.register(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
				}
			}, params);
			HttpUtils.login(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					JSONArray data = json
							.getJSONArray(UrlContants.jsonData);
					uid = data.getJSONObject(0).getString("id");
					requestData();
					finish();
				}
			}, params);

		}
			break;
		}
		return false;
	}
}
