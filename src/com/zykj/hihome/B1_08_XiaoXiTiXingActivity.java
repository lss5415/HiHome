package com.zykj.hihome;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.menuListView.SwipeMenu;
import com.zykj.hihome.menuListView.SwipeMenuCreator;
import com.zykj.hihome.menuListView.SwipeMenuItem;
import com.zykj.hihome.menuListView.SwipeMenuListView;
import com.zykj.hihome.menuListView.SwipeMenuListView.IXListViewListener;
import com.zykj.hihome.menuListView.SwipeMenuListView.OnMenuItemClickListener;
import com.zykj.hihome.menuListView.SwipeMenuView;
import com.zykj.hihome.menuListView.SwipeMenuView.OnSwipeItemClickListener;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

/**
 * @author LSS 2015年10月24日 上午10:14:59
 * 
 */
public class B1_08_XiaoXiTiXingActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener, OnMenuItemClickListener {
	private ImageView im_b108_back;// 返回
	private SwipeMenuListView applyList;
	private List<Friend> friends;
	private CommonAdapter<Friend> adapter;
	private Handler mHandler = new Handler();
	private int nowpage = 1;
	private static int PERPAGE = 10;

	// private TextView tv_bianji;//编辑资料

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_08_xiaoxitixing);

		initView();
		requestData();
	}

	/**
	 * 侧滑删除
	 */
	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem openItem = new SwipeMenuItem(
					B1_08_XiaoXiTiXingActivity.this);
			/* openItem背景色 */
			openItem.setBackground(new ColorDrawable(Color
					.rgb(0xff, 0x00, 0x00)));
			/* openItem宽度 */
			openItem.setWidth(dp2px(90));
			/* openItem显示的文字 */
			openItem.setTitle("删除");
			/* openItem字体大小 */
			openItem.setTitleSize(18);
			/* openItem字体颜色 */
			openItem.setTitleColor(Color.WHITE);
			/* 添加到menu */
			menu.addMenuItem(openItem);
		}
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void initView() {
		im_b108_back = (ImageView) findViewById(R.id.im_b108_back);
		applyList = (SwipeMenuListView) findViewById(R.id.applyList);
		applyList.setDivider(new ColorDrawable(0xffeeeeee));
		applyList.setDividerHeight(0);
		applyList.setOnItemClickListener(this);
		applyList.setXListViewListener(this);
		applyList.setPullRefreshEnable(true);
		applyList.setPullLoadEnable(true);
		applyList.setMenuCreator(creator);
		applyList.setOnMenuItemClickListener(this);
		setListener(im_b108_back);
	}

	private void requestData() {
		HttpUtils.getApplyList(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONObject(UrlContants.jsonData)
						.getJSONArray("list");
				friends = JSON.parseArray(jsonArray.toString(), Friend.class);
				adapter = new CommonAdapter<Friend>(
						B1_08_XiaoXiTiXingActivity.this,
						R.layout.ui_b1_08_message_item, friends) {
					@Override
					public void convert(final ViewHolder holder,
							final Friend friend) {
						holder.setImageUrl(R.id.aci_avatar, friend.getAvatar())
								.setText(R.id.aci_nick, friend.getNick());
						TextView button = holder.getView(R.id.aci_state);
						button.setBackgroundColor(Color.parseColor("0"
								.equals(friend.getState()) ? "#01CF97"
								: "#FFFFFF"));
						button.setText("0".equals(friend.getState()) ? "同意"
								: "1".equals(friend.getState()) ? "已同意" : "已拒绝");
						button.setTextColor(Color.parseColor("0".equals(friend
								.getState()) ? "#FFFFFF" : "#888888"));
						if ("0".equals(friend.getState())) {
							button.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									operatApplyFriend(holder.getPosition(),
											friend.getId());
								}
							});
						} else {
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
				Tools.toast(B1_08_XiaoXiTiXingActivity.this,
						json.getString("message"));
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
		/*
		 * case R.id.tv_bianji: Intent intent = new Intent(this,
		 * B1_07_1_GeRenXinXi.class); startActivity(intent); break;
		 */
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView,
			int position, long id) {
		Friend friend = friends.get(position);
		if ("0".equals(friend.getState())) {
			startActivityForResult(new Intent(this, B1_08_ApplyFriend.class)
					.putExtra("friend", friend).putExtra("position", position),
					6);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
			int position = data.getIntExtra("position", -1);
			String state = data.getStringExtra("state");
			friends.get(position).setState(state);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
		Friend friend = friends.get(position);
		RequestParams params = new RequestParams();
		params.put("id", friend.getId());
		HttpUtils.delApplyList(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				applyList.removeViewAt(position);
				adapter.notifyDataSetChanged();
			}
		}, params);

		return false;
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				nowpage = 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				nowpage += 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	public void onLoad() {
		applyList.stopRefresh();
		applyList.stopLoadMore();
		applyList.setRefreshTime("刚刚");
	}

}