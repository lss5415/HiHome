package com.zykj.hihome;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_HaoYouActivity extends BaseActivity implements OnItemClickListener{
	
	private CircularImage rv_me_avatar;
	private ImageView add_friend;
	private EditText firend_search;
	private ListView friend_list;
	private List<Friend> friends;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_haoyou);
		
		initView();
		requestData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestData();
	}

	/**
	 * 初始化页面
	 */
	public void initView() {
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);//头像
		add_friend = (ImageView)findViewById(R.id.add_friend);//添加好友
		firend_search = (EditText)findViewById(R.id.firend_search);//搜索
		
		friend_list = (ListView)findViewById(R.id.friend_list);//搜索
		friend_list.setDividerHeight(0);
		friend_list.setOnItemClickListener(this);
		setListener(rv_me_avatar, add_friend);
	}

	private void requestData() {
		HttpUtils.getFriendsList(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					int code = response.getInt("code");
					if(code == 200){
						JSONArray JSONArray = response.getJSONObject("datas").getJSONArray("list");
						friends = JSON.parseArray(JSONArray.toString(), Friend.class);
						friends.add(new Friend("通讯录"));
						friend_list.setAdapter(new CommonAdapter<Friend>(B4_HaoYouActivity.this, R.layout.ui_b4_haoyou_item, friends) {
							@Override
							public void convert(ViewHolder holder, Friend friend) {
								if(holder.getPosition() == 0){
									holder.setImageView(R.id.aci_image, R.drawable.tongxunlu);
								}else{
									holder.setImageUrl(R.id.aci_image, StringUtil.toString(friend.getAvatar(), "http://"), 10f);
								}
								holder.setText(R.id.aci_name, friend.getNick());
							}
						});
					}else{
						Tools.toast(B4_HaoYouActivity.this, "没有好友");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, "5");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rv_me_avatar:
			/*点击头像侧滑*/
			break;
		case R.id.add_friend:
			/*点击添加好友*/
			startActivity(new Intent(this, B4_2_HaoYouAddActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View currentView, int position, long id) {
		if(position == 0){
			startActivity(new Intent(this, B4_2_TongXunLuActivity.class));
		}else{
			Tools.toast(this, friends.get(position).getNick());
		}
	}
}