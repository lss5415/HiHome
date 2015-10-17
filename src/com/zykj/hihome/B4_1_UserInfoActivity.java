package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B4_1_UserInfoActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private EditText ed_username, ed_userAge, ed_userSign;
	private String username,userAge,userSign;
	private CheckBox cb_man, cb_woman;
	private Button btn_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b4_1_userinfo);
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("个人资料");

		ed_username = (EditText) findViewById(R.id.user_name);
		ed_userAge = (EditText) findViewById(R.id.user_age);
		ed_userSign = (EditText) findViewById(R.id.user_sign);

		btn_confirm = (Button) findViewById(R.id.positive);

		setListener(btn_confirm);
	}

	@Override
	public void onClick(View v) {
		 username = ed_username.getText().toString().trim();
		 userAge = ed_userAge.getText().toString().trim();
		 userSign = ed_userSign.getText().toString().trim();
		switch (v.getId()) {
		case R.id.positive:
			if (StringUtil.isEmpty(username)) {
				Tools.toast(B4_1_UserInfoActivity.this, "名称不能为空");
				return;
			}
			if (StringUtil.isEmpty(userAge)) {
				Tools.toast(B4_1_UserInfoActivity.this, "年龄不能为空");
				return;
			}
			if (StringUtil.isEmpty(userSign)) {
				Tools.toast(B4_1_UserInfoActivity.this, "签名不能为空");
				return;
			}
			submitData();

			break;
		default:
			break;
		}
	}

	private void submitData() {
		RequestParams params=new RequestParams();
		params.put("", username);
		params.put("", userAge);
		params.put("", userSign);
		
	}
}
