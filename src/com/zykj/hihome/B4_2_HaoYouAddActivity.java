package com.zykj.hihome;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.HttpUtils;

/**
 * @author lss 2015年8月8日	我的
 *
 */
public class B4_2_HaoYouAddActivity extends BaseActivity {

	private EditText aci_edittext;
	private Button aci_button;
	private ListView aci_listview;
	private LinearLayout search_none;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_haoyou_add);
		
		initView();
	}

	/**
	 * 初始化页面
	 */
	public void initView() {
		aci_edittext = (EditText) findViewById(R.id.aci_edittext);//查询字段
		aci_button = (Button) findViewById(R.id.aci_button);//查询按钮
		aci_listview = (ListView) findViewById(R.id.aci_listview);//查询有数据
		search_none = (LinearLayout) findViewById(R.id.aci_button);//查询没有数据
		setListener(aci_button);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aci_button:
			/*查找*/
			HttpUtils.getSearchUser(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
				}
			}, null);
			break;
		default:
			break;
		}
	}
}