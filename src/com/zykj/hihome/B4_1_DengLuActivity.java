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
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.util.HttpUtils;
import com.zykj.hihome.util.Tools;
import com.zykj.hihome.view.RequestDailog;

/**
 * @author LSS 2015年9月29日 上午9:19:36
 *
 */
public class B4_1_DengLuActivity extends BaseActivity {
	private ImageButton login_back;
	private TextView tv_regist,tv_forgetPassWord;
	private EditText et_login_name,et_passWord;
	private Button btn_login;
	private String login_name,passWord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_login);
		
		login_back=(ImageButton)findViewById(R.id.login_back);
		tv_regist=(TextView) findViewById(R.id.tv_regist);
		tv_forgetPassWord=(TextView) findViewById(R.id.tv_forgetPassWord);
		et_login_name=(EditText) findViewById(R.id.et_login_name);
		et_passWord=(EditText) findViewById(R.id.et_passWord);
		btn_login=(Button) findViewById(R.id.btn_login);
		setListener(login_back,tv_regist,tv_forgetPassWord,btn_login);
	}
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.login_back:
//				Intent intent_B0_MainActivity=new Intent();
//				intent_B0_MainActivity.putExtra("TabTAG", 1);
//				intent_B0_MainActivity.setClass(this, B0_MainActivity.class);
//				startActivity(intent_B0_MainActivity);
				this.finish();
				break;
			case R.id.tv_regist://注册
//				Intent intent_regist=new Intent();
//				intent_regist.setClass(this, B5_1_1_RegistActivity.class);
//				intent_regist.putExtra("FIND_OR_REGIST", 1);
//				startActivity(intent_regist);
				break;
			case R.id.tv_forgetPassWord://忘记密码
//				Intent intent_regist2=new Intent();
//				intent_regist2.setClass(this, B5_1_1_RegistActivity.class);
//				intent_regist2.putExtra("FIND_OR_REGIST", 2);
//				startActivity(intent_regist2);
				break;
			case R.id.btn_login:
//				RequestDailog.showDialog(this, "正在登陆");
				login_name=et_login_name.getText().toString().trim();
				passWord=et_passWord.getText().toString().trim();
				HttpUtils.login(res_login, login_name, passWord);
				break;
			default:
				break;
			
		}
	}
//	public void onBackPressed() {
//		Intent intent_B0_MainActivity=new Intent();
//		intent_B0_MainActivity.setClass(this, B0_MainActivity.class);
//		startActivity(intent_B0_MainActivity);
//		this.finish();
//	};
	JsonHttpResponseHandler res_login = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
//			RequestDailog.closeDialog();
//			Tools.Log("登陆"+response);
//			{"datas":{"error":"用户名密码错误"},"code":200}
			String error=null;
			JSONObject datas=null;
//			Tools.Log("res_login="+response);
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getJSONObject("datas").getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (error==null)//登陆成功
			{
				try {
					putSharedPreferenceValue("username", datas.getString("username"));
					putSharedPreferenceValue("mobile", datas.getString("mobile"));
					putSharedPreferenceValue("userid", datas.getString("userid"));
					putSharedPreferenceValue("key", datas.getString("key"));
					putSharedPreferenceValue("avatar", datas.getString("avatar"));
//					Tools.Log("image="+datas.getString("avatar"));
					putSharedPreferenceValue("member_points", datas.getString("member_points"));
					putSharedPreferenceValue("predeposit", datas.getString("predeposit"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Tools.Notic(B4_1_DengLuActivity.this, "登陆成功", null);
				Intent  intent_tomainavtivity = new Intent(B4_1_DengLuActivity.this, B0_MainActivity.class);
				startActivity(intent_tomainavtivity);
				B4_1_DengLuActivity.this.finish();
			}
			else//登陆失败 
			{
				Tools.Notic(B4_1_DengLuActivity.this, error+"", null);
			}
			
		}
		
		
	};
}
