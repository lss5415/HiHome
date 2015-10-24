package com.zykj.hihome;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_SelectExecutorActivity extends BaseActivity implements OnItemClickListener{

	private MyCommonTitle myCommonTitle;
	private ListView friend_list;
	private List<Friend> friends;
	
	private CommonAdapter<Friend> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_select_executor);

		initView();
		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setEditTitle("确定");
		myCommonTitle.setLisener(this, null);
		
		friend_list = (ListView)findViewById(R.id.excute_lvexcutor);//搜索
		friend_list.setDividerHeight(0);
		friend_list.setOnItemClickListener(this);
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
						adapter = new CommonAdapter<Friend>(B3_1_SelectExecutorActivity.this, R.layout.ui_b4_haoyou_item, friends) {
							@Override
							public void convert(ViewHolder holder, Friend friend) {
								holder.setImageUrl(R.id.aci_image, StringUtil.toString(friend.getAvatar(), "http://"), 10f);
								holder.setText(R.id.aci_name, friend.getNick());
								CheckBox mCheckBox = holder.getView(R.id.cb_choice);
								mCheckBox.setVisibility(View.VISIBLE);
								mCheckBox.setChecked(friend.isChecked());
							}
						};
						friend_list.setAdapter(adapter);
					}else{
						Tools.toast(B3_1_SelectExecutorActivity.this, "没有好友");
					}
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
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < friends.size(); i++) {
				if(friends.get(i).isChecked()){
					str.append(friends.get(i).getId()+",");
				}
			}
			Tools.toast(this, str.substring(0, str.length()-1));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		Friend friend = friends.get(position);
		friend.setChecked(!friend.isChecked());
        adapter.notifyDataSetChanged();
	}
}
