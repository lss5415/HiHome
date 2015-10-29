package com.zykj.hihome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B2_RelationFriend extends BaseActivity {
	
	private MyCommonTitle aci_mytitle;
	private ImageView aci_select1,aci_select2,aci_select3;
	private LinearLayout layout1,layout2,layout3;
	private Friend friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b2_relation);
		friend = (Friend)getIntent().getSerializableExtra("friend");
		
		initView();
	}

	private void initView() {
		aci_mytitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		aci_mytitle.setTitle("选择与其关系");

		layout1 = (LinearLayout)findViewById(R.id.layout1);
		layout2 = (LinearLayout)findViewById(R.id.layout2);
		layout3 = (LinearLayout)findViewById(R.id.layout3);
		aci_select1 = (ImageView)findViewById(R.id.aci_select1);
		aci_select2 = (ImageView)findViewById(R.id.aci_select2);
		aci_select3 = (ImageView)findViewById(R.id.aci_select3);
		initSelect(StringUtil.toString(friend.getType(), "3"));
		
		setListener(layout1,layout2,layout3);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.layout1:
			/*配偶*/
			groupFriend("2");
			initSelect("2");
			break;
		case R.id.layout2:
			/*星标*/
			groupFriend("1");
			initSelect("1");
			break;
		case R.id.layout3:
			/*普通*/
			groupFriend("0");
			initSelect("0");
			break;
		default:
			break;
		}
	}

	private void initSelect(String type) {
		if("0".equals(type)){
			aci_select1.setVisibility(View.GONE);
			aci_select2.setVisibility(View.GONE);
			aci_select3.setVisibility(View.VISIBLE);
		}else if("1".equals(type)){
			aci_select1.setVisibility(View.GONE);
			aci_select2.setVisibility(View.VISIBLE);
			aci_select3.setVisibility(View.GONE);
		}else if("2".equals(type)){
			aci_select1.setVisibility(View.VISIBLE);
			aci_select2.setVisibility(View.GONE);
			aci_select3.setVisibility(View.GONE);
		}
	}
	
	private void groupFriend(final String type){
		RequestParams params = new RequestParams();
		params.put("fid", friend.getId());
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("type", type);
		HttpUtils.groupFriend(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B2_RelationFriend.this, "标记好友成功");
				setResult(Activity.RESULT_OK, getIntent().putExtra("type", type));
				finish();
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(B2_RelationFriend.this, "已经是"+("0".equals(type)?"普通好友":"1".equals(type)?"星标好友":"配偶"));
			}
		}, params);
	}
}
