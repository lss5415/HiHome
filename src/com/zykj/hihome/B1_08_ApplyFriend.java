package com.zykj.hihome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B1_08_ApplyFriend extends BaseActivity{

	private MyCommonTitle aci_mytitle;
	private CircularImage rv_me_avatar;
	private TextView aci_info,aci_remark;
	private Button button1,button2;
	private Friend friend;
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b1_08_apply_friend);
		friend = (Friend) getIntent().getSerializableExtra("friend");
		position = getIntent().getIntExtra("position", -1);
		
		initView();
		requestData();
	}

	private void initView() {
		aci_mytitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		aci_mytitle.setTitle("好友申请");
		
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);
		aci_info = (TextView)findViewById(R.id.aci_info);
		aci_remark = (TextView)findViewById(R.id.aci_remark);
		button1 = (Button)findViewById(R.id.aci_button_left);
		button2 = (Button)findViewById(R.id.aci_button_right);
		
		setListener(button1, button2);
	}

	private void requestData() {
		if(!StringUtil.isEmpty(friend.getAvatar())){
			ImageLoader.getInstance().displayImage(friend.getAvatar(), rv_me_avatar);
		}
		aci_info.setText(friend.getNick()+"    "+friend.getSex());
		aci_remark.setText("备注信息：无");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_button_left:
			/*拒绝*/
			asyncActiveService("2");
			break;
		case R.id.aci_button_right:
			asyncActiveService("1");
			/*同意*/
			break;
		default:
			break;
		}
	}
	
	private void asyncActiveService(final String state){
		RequestParams params = new RequestParams();
		params.put("id", friend.getId());
		params.put("state", state);
		HttpUtils.applyFriend(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B1_08_ApplyFriend.this, json.getString("message"));
				setResult(Activity.RESULT_OK, getIntent().putExtra("state", state).putExtra("position", position));
				finish();
			}
		}, params);
	}
}
