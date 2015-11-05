package com.zykj.hihome;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.TextUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.MyRequestDailog;

/**
 * @author LSS 2015年9月29日 上午9:19:36
 * 
 */
public class B4_1_ForgetPassWordActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private EditText et_mobile, et_code, et_newpass, et_confirmpass;
	private Button identifying_code, btn_confirm;
	private String mobile, mobilecode, newpass, confirmpass;

	private static String APPKEY = "b5174972a9ac";
	private static String APPKEYSECRET = "8536890596fff208c04a3e52c88a2060";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_1_forgetpassword);
		// 初始化短信验证
		SMSSDK.initSDK(this, APPKEY, APPKEYSECRET);
		EventHandler eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("忘记密码");

		et_mobile = (EditText) findViewById(R.id.user_mobile);
		et_code = (EditText) findViewById(R.id.user_mobile_code);
		et_newpass = (EditText) findViewById(R.id.user_new_password);
		et_confirmpass = (EditText) findViewById(R.id.user_comfirm_password);
		identifying_code = (Button) findViewById(R.id.idenfy_code);
		btn_confirm = (Button) findViewById(R.id.positive);

		setListener(identifying_code, btn_confirm);
	}

	@Override
	public void onClick(View view) {
		mobile = et_mobile.getText().toString().trim();
		mobilecode = et_code.getText().toString().trim();
		newpass = et_newpass.getText().toString().trim();
		confirmpass = et_confirmpass.getText().toString().trim();
		switch (view.getId()) {
		case R.id.idenfy_code:// 获取验证码
			if (!TextUtil.isMobile(mobile)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this, "手机号格式不正确");
				return;
			}
			/* 发送手机验证码 */
			identifying_code.setOnClickListener(null);
			new MyCount(60000, 1000).start();// 一分钟倒计时
			SMSSDK.getVerificationCode("86", mobile);
			break;

		case R.id.positive:// 确定
		
			if (!TextUtil.isCode(mobilecode,4)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this, "验证码不正确");
				return;
			}
			if (StringUtil.isEmpty(newpass)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this, "新密码不能为空");
				return;
			}
			if (!TextUtil.isPasswordLengthLegal(newpass)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this,"密码长度合法性校验6-20位任意字符");
				return;
			}
			if (StringUtil.isEmpty(confirmpass)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this, "再次输入的新密码不能为空");
				return;
			}
			if (!TextUtil.isPasswordLengthLegal(confirmpass)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this,"密码长度合法性校验6-20位任意字符");
				return;
			}
			if (!newpass.equals(confirmpass)) {
				Tools.toast(B4_1_ForgetPassWordActivity.this,"两次输入的密码不一致,请重新输入");
				return;
			}
              //提交修改
			
			MyRequestDailog.showDialog(this, "");
			SMSSDK.submitVerificationCode("86", mobile, mobilecode);
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event+" + event);
			Log.e("result", "result+" + result);
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					registerNewUser();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Tools.toast(B4_1_ForgetPassWordActivity.this, "验证码已发送");
				}
			} else {
				 ((Throwable) data).printStackTrace();
//				 int resId =
//				 getStringRes(B4_1_ForgetPassWordActivity.this,"smssdk_network_error");
//				 Tools.toast(
//				 B4_1_ForgetPassWordActivity.this,
//				 event == SMSSDK.EVENT_GET_VERIFICATION_CODE ? "验证码频繁，请稍后再试！"
//				 : "验证码错误");
//				 if (resId > 0) {
//				 Tools.toast(B4_1_ForgetPassWordActivity.this, resId + "");
//				 }
			}
		}

		private void registerNewUser() {
			RequestParams params = new RequestParams();
			params.put("mob", mobile);
			params.put("pass", newpass);
			HttpUtils.resetPassWord(new HttpErrorHandler() {
				
				@Override
				public void onRecevieSuccess(JSONObject json) {
					 MyRequestDailog.closeDialog();
					 Tools.toast(B4_1_ForgetPassWordActivity.this,"密码修改成");
					 finish();
				}
				 @Override
				 public void onRecevieFailed(String status, JSONObject json) {
				 MyRequestDailog.closeDialog();
				 Tools.toast(B4_1_ForgetPassWordActivity.this,"密码修失败");
				 }
			}, params);
			// HttpUtils.resetPassword(new HttpErrorHandler() {
			// @Override
			// public void onRecevieSuccess(JSONObject json) {
			// MyRequestDailog.closeDialog();
			// Tools.toast(UserResetPwdActivity.this,
			// json.getString("message"));
			// // Tools.toast(B4_1_ForgetPassWordActivity.this, "密码重置成功");
			// finish();
			// }
			//
			// @Override
			// public void onRecevieFailed(String status, JSONObject json) {
			// MyRequestDailog.closeDialog();
			// Tools.toast(B4_1_ForgetPassWordActivity.this,
			// json.getString("message"));
			// }
			// }, params);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			identifying_code.setText("点击获取验证码");
			identifying_code
					.setOnClickListener(B4_1_ForgetPassWordActivity.this);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			identifying_code.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
