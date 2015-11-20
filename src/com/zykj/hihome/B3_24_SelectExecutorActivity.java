package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.AnimateFirstDisplayListener;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_24_SelectExecutorActivity extends BaseActivity implements OnItemClickListener{

	private MyCommonTitle myCommonTitle;
	private ListView friend_list;
	private List<Friend> friends = new ArrayList<Friend>();
	private String[] strType = new String[]{"普通好友","星标好友","配偶"};
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private String uid = "";
	
	private CommonAdapter<Friend> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_select_executor);
		uid = getIntent().getStringExtra("uid");
		
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

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("选择好友");
		myCommonTitle.setEditTitle("确定");
		myCommonTitle.setLisener(this, null);
		
		friend_list = (ListView)findViewById(R.id.excute_lvexcutor);//搜索
		adapter = new CommonAdapter<Friend>(B3_24_SelectExecutorActivity.this, R.layout.ui_b4_haoyou_item, friends) {
			@Override
			public void convert(ViewHolder holder, Friend friend) {
				ImageView imageView = holder.getView(R.id.aci_image);
				imageView.setImageResource(holder.getPosition() == 0?R.drawable.tongxunlu:R.drawable.xinagcetouxiang);
				if(holder.getPosition() > 0 && !StringUtil.isEmpty(friend.getAvatar(), "images/default.jpg")){
					imageLoader.displayImage(HttpUtils.IMAGE_URL+friend.getAvatar(), imageView, options, animateFirstListener);
				}
				if(!StringUtil.isEmpty(friend.getCategory())){
					holder.setText(R.id.friend_type, friend.getCategory());
					holder.setVisibility(R.id.friend_type, true);
				}
				holder.setText(R.id.aci_name, friend.getNick());
				CheckBox mCheckBox = holder.getView(R.id.cb_choice);
				mCheckBox.setVisibility(View.VISIBLE);
				mCheckBox.setChecked(friend.isChecked());
			}
		};
		friend_list.setAdapter(adapter);
		friend_list.setDividerHeight(0);
		friend_list.setOnItemClickListener(this);
	}

	private void requestData() {
		friends.clear();
		Friend friend = new Friend("自己");
		friend.setFid(BaseApp.getModel().getUserid());
		if(!StringUtil.isEmpty(uid)){
			friend.setChecked(true);
		}
		friends.add(0, friend);
		HttpUtils.getFriendsList(new JsonHttpResponseHandler(){
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
		}, "5");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_edit_btn:
			/*确定*/
			StringBuffer strName = new StringBuffer();
			StringBuffer strId = new StringBuffer();
			for (int i = 0; i < friends.size(); i++) {
				if(friends.get(i).isChecked()){
					strName.append(friends.get(i).getNick()+",");
					strId.append(friends.get(i).getFid()+",");
				}
			}
			if(strId.length()<1){
				Tools.toast(this, "未选择任何好友!");
			}else{
				setResult(Activity.RESULT_OK, getIntent()
						.putExtra("strName", strName.substring(0, strName.length()-1))
						.putExtra("strId", strId.substring(0, strId.length()-1)));
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		if(StringUtil.isEmpty(uid) || position != 0){
			Friend friend = friends.get(position);
			friend.setChecked(!friend.isChecked());
	        adapter.notifyDataSetChanged();
		}
	}
}
