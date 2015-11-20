package com.zykj.hihome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
	private XListView friend_list;
	private List<Friend> friends = new ArrayList<Friend>();
	private CommonAdapter<Friend> adapter;
	private String[] strType = new String[]{"普通好友","星标好友","配偶"};
	private Handler mHandler = new Handler();//异步加载或刷新
	private DisplayImageOptions options;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_haoyou);
		
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.xinagcetouxiang)
			.showImageForEmptyUri(R.drawable.xinagcetouxiang)
			.showImageOnFail(R.drawable.xinagcetouxiang)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(5))
			.build();
		
		initView();
		requestData();
	}
	
	/**
	 * 初始化页面
	 */
	public void initView() {
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);//头像
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(BaseApp.getModel().getAvatar(), rv_me_avatar);
		add_friend = (ImageView)findViewById(R.id.add_friend);//添加好友
		
		friend_list = (XListView)findViewById(R.id.friend_list);//搜索
		friend_list.setPullLoadEnable(false);
		friend_list.setXListViewListener(this);
//		adapter = new LoaderAdapter(this, friends);
		adapter = new CommonAdapter<Friend>(B4_HaoYouActivity.this, R.layout.ui_b4_haoyou_item, friends) {
			private ImageLoader imageLoader = ImageLoader.getInstance();
			private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
			@Override
			public void convert(ViewHolder holder, Friend friend) {
				ImageView imageView = holder.getView(R.id.aci_image);
				imageView.setImageResource(holder.getPosition() == 0?R.drawable.tongxunlu:R.drawable.xinagcetouxiang);
				if(holder.getPosition() > 0){
					imageLoader.displayImage(HttpUtils.IMAGE_URL+friend.getAvatar(), imageView, options, animateFirstListener);
				}
				holder.setText(R.id.aci_name, friend.getNick())
						.setText(R.id.friend_type, friend.getCategory())
						.setVisibility(R.id.friend_type, !StringUtil.isEmpty(friend.getCategory()));
			}
		};
		friend_list.setAdapter(adapter);
		friend_list.setDividerHeight(0);
		friend_list.setOnItemClickListener(this);
		setListener(rv_me_avatar, add_friend);
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
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
					for (int i = strType.length-1; i >= 0; i--) {
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
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
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