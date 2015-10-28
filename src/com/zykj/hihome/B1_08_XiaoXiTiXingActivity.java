package com.zykj.hihome;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

/**
 * @author LSS 2015年10月24日 上午10:14:59
 *
 */
public class B1_08_XiaoXiTiXingActivity extends BaseActivity implements OnItemClickListener{
	private ImageView im_b108_back;//返回
	private ListView applyList;
	private List<Friend> friends;
	private CommonAdapter<Friend> adapter;
//	private TextView tv_bianji;//编辑资料
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_08_xiaoxitixing);
		
		initView();
		requestData();
	}
	
	private void initView() {
		im_b108_back = (ImageView)findViewById(R.id.im_b108_back);
		applyList = (ListView)findViewById(R.id.applyList);
		applyList.setDivider(new ColorDrawable(0xffeeeeee));
		applyList.setDividerHeight(1);
		applyList.setOnItemClickListener(this);
		setListener(im_b108_back);
	}
	
	private void requestData() {
		HttpUtils.getApplyList(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONObject(UrlContants.jsonData).getJSONArray("list");
				friends = JSON.parseArray(jsonArray.toString(), Friend.class);
				adapter = new CommonAdapter<Friend>(B1_08_XiaoXiTiXingActivity.this, R.layout.ui_b1_08_message_item, friends) {
					@Override
					public void convert(final ViewHolder holder, final Friend friend) {
						holder.setImageUrl(R.id.aci_avatar, friend.getAvatar()).setText(R.id.aci_nick, friend.getNick());
						TextView button = holder.getView(R.id.aci_state);
						button.setBackgroundColor(Color.parseColor("0".equals(friend.getState())?"#01CF97":"#FFFFFF"));
						button.setText("0".equals(friend.getState())?"同意":"1".equals(friend.getState())?"已同意":"已拒绝");
						button.setTextColor(Color.parseColor("0".equals(friend.getState())?"#FFFFFF":"#888888"));
						if("0".equals(friend.getState())){
							button.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									operatApplyFriend(holder.getPosition(), friend.getId());
								}
							});
						}else{
							button.setOnClickListener(null);
						}
					}
				};
				applyList.setAdapter(adapter);
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				
			}
		}, BaseApp.getModel().getUserid());
	}

	private void operatApplyFriend(final int position, String id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("state", "1");
		HttpUtils.applyFriend(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B1_08_XiaoXiTiXingActivity.this, json.getString("message"));
				friends.get(position).setState("1");
				adapter.notifyDataSetChanged();
			}
		}, params);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b108_back:
			this.finish();
			break;
		 /*case R.id.tv_bianji:
			 Intent intent = new Intent(this, B1_07_1_GeRenXinXi.class);
			 startActivity(intent);
		 break;*/
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		Friend friend = friends.get(position);
		if("0".equals(friend.getState())){
			startActivityForResult(new Intent(this, B1_08_ApplyFriend.class).putExtra("friend", friend).putExtra("position", position), 6);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 6 && resultCode == Activity.RESULT_OK){
			int position =  data.getIntExtra("position", -1);
			String state = data.getStringExtra("state");
			friends.get(position).setState(state);
			adapter.notifyDataSetChanged();
		}
	}
}