package com.zykj.hihome;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import android.app.Activity;
import android.content.Intent;
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
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author csh 2015-11-07
 * 好友资料
 */
public class B2_FriendDetailActivity extends BaseActivity {

	private MyCommonTitle aci_mytitle;
	private CircularImage rv_me_avatar;
	private TextView aci_title,aci_nick,aci_sign,aci_type;
	private Button button1,button2;
	private LinearLayout aci_relation,aci_task,aci_photo;
	private String uid, type;
	private Friend friend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b3_friend_detail);
		uid = (String) getIntent().getSerializableExtra("uid");
		type = getIntent().getStringExtra("type");
		
		initView();
		requestData();
	}

	private void initView() {
		aci_mytitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		aci_mytitle.setTitle("好友资料");
		
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);
		aci_title = (TextView)findViewById(R.id.aci_title);
		aci_nick = (TextView)findViewById(R.id.aci_nick);
		aci_sign = (TextView)findViewById(R.id.aci_sign);
		aci_relation = (LinearLayout)findViewById(R.id.aci_relation);
		aci_type = (TextView)findViewById(R.id.aci_type);
		aci_task = (LinearLayout)findViewById(R.id.aci_task);
		aci_photo=(LinearLayout) findViewById(R.id.aci_photo);
		button1 = (Button)findViewById(R.id.aci_button_left);
		button2 = (Button)findViewById(R.id.aci_button_right);
		button1.setVisibility(StringUtil.isEmpty(type)?View.VISIBLE:View.GONE);
		button2.setVisibility(View.VISIBLE);
		
		setListener(aci_relation, aci_task, button1, button2);
	}

	private void requestData() {
		
		HttpUtils.getInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray(UrlContants.jsonData).getJSONObject(0);
				friend = JSONObject.parseObject(jsonObject.toString(), Friend.class);
//				if(!StringUtil.isEmpty(friend.getAvatar())){
//					ImageLoader.getInstance().displayImage(friend.getAvatar(), rv_me_avatar);
//				}
				ImageLoader.getInstance().displayImage(StringUtil.toString(HttpUtils.IMAGE_URL+jsonObject.getString("avatar"),"http://"), rv_me_avatar);
				friend.setType(type);
				aci_title.setText(friend.getNick()+"    "+friend.getSex()+"    "+friend.getAge());
				aci_nick.setText(friend.getNick());
				aci_sign.setText(friend.getSign());
				aci_type.setText(StringUtil.isEmpty(type)?"无":"0".equals(type)?"普通好友":"1".equals(type)?"星标好友":"配偶");
			}
		}, uid);
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
			RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, friend.getId(), "会话");
			break;
		case R.id.aci_relation:
			startActivityForResult(new Intent(this, B2_RelationFriend.class).putExtra("friend", friend), 6);
			break;
		case R.id.aci_task://查看好友任务
			Tools.toast(this, "任务");
			break;
		case R.id.aci_photo://查看好友相册
			
//			Tools.toast(this, "任务");
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == 6){
			String type = data.getStringExtra("type");
			aci_type.setText("0".equals(type)?"普通好友":"1".equals(type)?"星标好友":"配偶");
			friend.setType(type);
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
