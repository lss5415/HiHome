package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

public class B2_FriendDetailActivity extends BaseActivity {

	private MyCommonTitle aci_mytitle;
	private CircularImage rv_me_avatar;
	private TextView aci_title,aci_nick,aci_sign;
	private Button button1,button2;
	private LinearLayout aci_task;
	private String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b3_friend_detail);
		uid = getIntent().getStringExtra("uid");
		
		initView();
		requestData();
	}

	private void requestData() {
		HttpUtils.getInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData).getJSONObject(0);
				Friend friend = JSONObject.parseObject(jsonObject.toString(), Friend.class);
				ImageLoader.getInstance().displayImage(friend.getAvatar(), rv_me_avatar);
				aci_title.setText(friend.getNick()+"    "+friend.getSex()+"    "+friend.getAge());
				aci_nick.setText(jsonObject.getString("nick"));
				aci_sign.setText(jsonObject.getString("sign"));
			}
		}, uid);
	}

	private void initView() {
		aci_mytitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		aci_mytitle.setTitle("好友资料");
		
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);
		aci_title = (TextView)findViewById(R.id.aci_title);
		aci_nick = (TextView)findViewById(R.id.aci_nick);
		aci_sign = (TextView)findViewById(R.id.aci_sign);
		aci_task = (LinearLayout)findViewById(R.id.aci_task);
		button1 = (Button)findViewById(R.id.aci_button_left);
		button2 = (Button)findViewById(R.id.aci_button_right);
		
		setListener(aci_task, button1, button2);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_button_left:
			RequestParams params = new RequestParams();
			params.put("fid", uid);
			params.put("uid", BaseApp.getModel().getUserid());
			HttpUtils.addFriend(res_addFriend, params);
			break;
		case R.id.aci_button_right:
			Tools.toast(this, "聊天");
			break;
		case R.id.aci_task:
			Tools.toast(this, "任务");
			break;
		default:
			break;
		}
	}

	/**
	 * 异步获取搜索好友结果
	 */
	private AsyncHttpResponseHandler res_addFriend = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			Tools.toast(B2_FriendDetailActivity.this, json.getString("message"));
		}

		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			Tools.toast(B2_FriendDetailActivity.this, json.getString("message"));
		}
	};
}
