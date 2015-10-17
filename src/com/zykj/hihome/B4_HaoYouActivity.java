package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpUtils;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_HaoYouActivity extends BaseActivity {
	
	private CircularImage rv_me_avatar;
	private ImageView add_friend;
	private EditText firend_search;
	private ListView friend_list;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_haoyou);
		
		initView();
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
						final List<Friend> friends = JSON.parseArray(JSONArray.toString(), Friend.class);
						friend_list.setAdapter(new BaseAdapter() {
							@Override
							public int getCount() {
								return friends.size();
							}
							@Override
							public Friend getItem(int position) {
								return friends.get(position);
							}
							@Override
							public long getItemId(int checkedId) {
								return 0;
							}
							@Override
							public View getView(int arg0, View arg1, ViewGroup arg2) {
								return null;
							}
						});
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
			break;
		default:
			break;
		}
	}
}