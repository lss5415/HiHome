package com.zykj.hihome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author csh 2015-11-06
 * 1任务权限/2消息通知/3声音通知/4聊天记录
 */
public class B1_05_Setting_Detail extends BaseActivity implements OnCheckedChangeListener{
	
	private MyCommonTitle myCommonTitle;
	private LinearLayout aci_part1,aci_part2;
	private TextView aci_date;
	private TextView aci_item1,aci_item2,aci_item3;
	private TextView aci_select1,aci_select2,aci_select3;
	private ToggleButton toggle1,toggle2,toggle3;
	private String[] titles = new String[]{"展示权限", "消息通知", "声音通知", "聊天记录"};
	private int position;// 1展示权限/2消息通知/3声音通知/4聊天记录

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting_detail);
		
		initView();
	}

	private void initView() {
		position = getIntent().getIntExtra("position", 0);
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle(titles[position-1]);
		
		aci_part1 = (LinearLayout) findViewById(R.id.aci_part1);// 任务权限/聊天记录
		aci_part2 = (LinearLayout) findViewById(R.id.aci_part2);// 消息通知/声音通知
		aci_date = (TextView) findViewById(R.id.aci_date);// 显示日期
		aci_item1 = (TextView) findViewById(R.id.aci_item1);// 只对家庭圈/清空消息列表
		aci_item2 = (TextView) findViewById(R.id.aci_item2);// 对所有人/清空所有聊天记录
		aci_item3 = (TextView) findViewById(R.id.aci_item3);// 对好友/清空缓存数据
		aci_select1 = (TextView) findViewById(R.id.aci_select1);// 消息内容开关/消息声音开关
		aci_select2 = (TextView) findViewById(R.id.aci_select2);// 通知时指示灯闪烁开关/振动开关
		aci_select3 = (TextView) findViewById(R.id.aci_select3);// 任务接受开关/任务提示音开关
		toggle1 = (ToggleButton) findViewById(R.id.toggle1);// 开关
		toggle2 = (ToggleButton) findViewById(R.id.toggle2);// 开关
		toggle3 = (ToggleButton) findViewById(R.id.toggle3);// 开关
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		switch (position) {
		case 1:
			/*任务权限*/
			aci_item1.setVisibility(View.GONE);
			aci_part1.setVisibility(View.VISIBLE);
			break;
		case 2:
			/*消息通知*/
			aci_part2.setVisibility(View.VISIBLE);
			break;
		case 3:
			/*声音通知*/
			aci_select1.setText("消息声音开关");
			aci_select2.setText("振动开关");
			aci_select3.setText("任务提示音开关");
			aci_part2.setVisibility(View.VISIBLE);
			break;
		case 4:
			/*聊天记录*/
			aci_item1.setText("清空消息列表");
			aci_item2.setText("清空所有聊天记录");
			aci_item3.setText("清空缓存数据");
			aci_date.setVisibility(View.GONE);
			aci_part1.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		setListener(aci_item1,aci_item2,aci_item3);
		toggle1.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件
		toggle2.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件
		toggle3.setOnCheckedChangeListener(this);// 设置ToggleBtoon的监听事件
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_item1:
			Tools.toast(this, position == 1?"只对家庭圈":"清空消息列表");
			break;
		case R.id.aci_item2:
//			Tools.toast(this, position == 1?"对所有人":"清空所有聊天记录");
			if(position == 1){
				/*对所有人*/
				RequestParams params = new RequestParams();
				params.put("uid", BaseApp.getModel().getUserid());
				params.put("pm", "0");
				HttpUtils.postUserTaskPM(res_postUserTaskPM, params);
			}else{
				/*清空所有聊天记录*/
			}
			break;
		case R.id.aci_item3:
			Tools.toast(this, position == 1?"对好友":"清空缓存数据");
			if(position == 1){
				/*对好友*/
				startActivityForResult(new Intent(this, B3_24_SelectExecutorActivity.class)
							.putExtra("uid", BaseApp.getModel().getUserid()), 10);
			}else{
				/*清空缓存数据*/
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean flag) {
		switch (view.getId()) {
		case R.id.toggle1:
			if(flag){
				Tools.toast(this, position == 2?"消息内容开":"消息声音开");
			}else{
				Tools.toast(this, position == 2?"消息内容关":"消息声音关");
			}
			break;
		case R.id.toggle2:
			if(flag){
				Tools.toast(this, position == 2?"通知时指示灯闪烁开":"振动开");
			}else{
				Tools.toast(this, position == 2?"通知时指示灯闪烁关":"振动关");
			}
			break;
		case R.id.toggle3:
			if(flag){
				Tools.toast(this, position == 2?"任务接受开":"任务提示音开");
			}else{
				Tools.toast(this, position == 2?"任务接受关":"任务提示音关");
			}
			break;
		default:
			break;
		}
	}
	
	private AsyncHttpResponseHandler res_postUserTaskPM = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			Tools.toast(B1_05_Setting_Detail.this, "设置成功!");
		}
		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			Tools.toast(B1_05_Setting_Detail.this, "设置失败!");
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case 10:
				/* 选择执行人 */
				if (data != null) {
					aci_item3.setText("对好友           "+data.getStringExtra("strName"));
					String strId = data.getStringExtra("strId");
					
					RequestParams params = new RequestParams();
					params.put("uid", BaseApp.getModel().getUserid());
					params.put("pm", strId);
					HttpUtils.postUserTaskPM(res_postUserTaskPM, params);
				}
				break;
			default:
				break;
			}
		}
	};
}
