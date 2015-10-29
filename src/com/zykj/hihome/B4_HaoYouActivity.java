package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_HaoYouActivity extends BaseActivity implements IXListViewListener,OnItemClickListener{
	
	private CircularImage rv_me_avatar;
	private ImageView add_friend;
//	private EditText firend_search;
	private XListView friend_list;
	private List<Friend> friends = new ArrayList<Friend>();
	private CommonAdapter<Friend> adapter;
	private String[] strType = new String[]{"普通好友","星标好友","配偶"};
	private Handler mHandler = new Handler();//异步加载或刷新
	
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
//		firend_search = (EditText)findViewById(R.id.firend_search);//搜索
		
		friend_list = (XListView)findViewById(R.id.friend_list);//搜索
		friend_list.setPullLoadEnable(false);
		friend_list.setXListViewListener(this);
		adapter = new CommonAdapter<Friend>(B4_HaoYouActivity.this, R.layout.ui_b4_haoyou_item, friends) {
			@Override
			public void convert(ViewHolder holder, Friend friend) {
				if(holder.getPosition() == 0){
					holder.setImageView(R.id.aci_image, R.drawable.tongxunlu);
				}else{
					holder.setImageUrl(R.id.aci_image, friend.getAvatar(), 10f);
				}
				if(!StringUtil.isEmpty(friend.getCategory())){
					holder.setText(R.id.friend_type, friend.getCategory());
					holder.setVisibility(R.id.friend_type, true);
				}
				holder.setText(R.id.aci_name, friend.getNick());
			}
		};
		friend_list.setAdapter(adapter);
		friend_list.setDividerHeight(0);
		friend_list.setOnItemClickListener(this);
		setListener(rv_me_avatar, add_friend);
	}

	private void requestData() {
		friends.clear();
		friends.add(0, new Friend("通讯录"));
		HttpUtils.getFriendsList(res_getFriendsList, BaseApp.getModel().getUserid());
	}

	private AsyncHttpResponseHandler res_getFriendsList = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				int code = response.getInt("code");
				if(code == 200){
					JSONObject jsonObject = response.getJSONObject(UrlContants.jsonData);
					for (int i = 0; i < strType.length; i++) {
						JSONArray JSONArray = jsonObject.getJSONArray("list"+i);
						List<Friend> datas = JSON.parseArray(JSONArray.toString(), Friend.class);
						if(datas.size()>0){
							datas.get(0).setCategory(strType[i]);
						}
						friends.addAll(datas);
					}
				}
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

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
		if(position == 1){
			startActivity(new Intent(this, B4_2_TongXunLuActivity.class));
		}else{
			startActivity(new Intent(this, B2_FriendDetailActivity.class)
					.putExtra("uid", friends.get(position-1).getFid()).putExtra("type", friends.get(position-1).getType()));
		}
	}

	@Override
	public void onRefresh() {
		/**下拉刷新 重建*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				requestData();
				friend_list.stopRefresh();
				friend_list.setRefreshTime("刚刚");
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
	}
}