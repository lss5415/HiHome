package com.zykj.hihome;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_2_HaoYouAddActivity extends BaseActivity {

	private MyCommonTitle myCommonTitle;
	private EditText aci_edittext;
	private Button aci_button;
	private ListView aci_listview;
	private LinearLayout search_none;
	private List<Friend> friends;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_haoyou_add);
		
		initView();
	}

	/**
	 * 初始化页面
	 */
	public void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);//查询字段
		myCommonTitle.setTitle("添加联系人");
		
		aci_edittext = (EditText) findViewById(R.id.aci_edittext);//查询字段
		aci_button = (Button) findViewById(R.id.aci_button);//查询按钮
		aci_listview = (ListView) findViewById(R.id.aci_listview);//查询有数据
		search_none = (LinearLayout) findViewById(R.id.search_none);//查询没有数据
		setListener(aci_button, search_none);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aci_button:
			/*查找*/
			String searchStr = aci_edittext.getText().toString().trim();
			if(!StringUtil.isEmpty(searchStr)){
				RequestParams params = new RequestParams();
				params.put("keys", searchStr);//搜索关键词
				params.put("uid", "5");//用户Id
				HttpUtils.getSearchUser(res_getSearchUser, params);
			}else{
				Tools.toast(this, "请输入搜索内容!");
			}
			break;
		case R.id.search_none:
			/*邀请好友加入*/
			startActivity(new Intent(this, B4_2_TongXunLuActivity.class));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 异步获取搜索好友结果
	 */
	private JsonHttpResponseHandler res_getSearchUser = new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				int code = response.getInt("code");
				if(code == 200){
					search_none.setVisibility(View.GONE);
					aci_listview.setVisibility(View.VISIBLE);
					JSONArray JSONArray = response.getJSONObject("datas").getJSONArray("list");
					friends = JSON.parseArray(JSONArray.toString(), Friend.class);
					aci_listview.setAdapter(new CommonAdapter<Friend>(B4_2_HaoYouAddActivity.this, R.layout.ui_b4_haoyou_item, friends) {
						@Override
						public void convert(ViewHolder holder, Friend friend) {
							holder.setImageUrl(R.id.aci_image, StringUtil.toString(friend.getAvatar(), "http://"), 10f)
									.setText(R.id.aci_name, friend.getNick());
						}
					});
				}else{
					aci_listview.setVisibility(View.GONE);
					search_none.setVisibility(View.VISIBLE);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
	};
}