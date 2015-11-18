package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.data.Photo;
import com.zykj.hihome.data.XiangCeLieBiao;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

public class B1_04_02_SelectPhotoActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener {
	private static int PERPAGE = 10;
	private int nowpage = 1;
	private MyCommonTitle myCommonTitle;
	private XListView photo_list;
	private List<XiangCeLieBiao> photoes = new ArrayList<XiangCeLieBiao>();
	private CommonAdapter<XiangCeLieBiao> photoAdapter;
	private Handler mHandler;
	private List<Boolean> photoflag= new ArrayList<Boolean>();//默认false隐藏,true显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b1_04_02_select_photo);
		initView();

		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("选择相册");
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setEditTitle("确定");

		photo_list = (XListView) findViewById(R.id.lv_select_photo);

		photo_list.setDividerHeight(0);
		photo_list.setPullRefreshEnable(true);
		photo_list.setPullLoadEnable(true);
		photo_list.setXListViewListener(this);
		photo_list.setOnItemClickListener(this);
	}

	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("fid", BaseApp.getModel().getUserid());
		HttpUtils.getXiangCeList(new HttpErrorHandler() {

			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONObject(UrlContants.jsonData)
						.getJSONArray("list");
				List<XiangCeLieBiao> list = new ArrayList<XiangCeLieBiao>();
				list = JSON.parseArray(jsonArray.toString(),
						XiangCeLieBiao.class);
				if (nowpage == 1) {photoes.clear();}
				photoes.addAll(list);
				
				photoAdapter = new CommonAdapter<XiangCeLieBiao>(
						B1_04_02_SelectPhotoActivity.this, R.layout.ui_b4_haoyou_item,
						photoes) {

					@Override
					public void convert(ViewHolder holder, XiangCeLieBiao photo) {

						if (StringUtil.isEmpty(photo.getImgsrc())) {
							holder.setImageView(R.id.aci_image,
									R.drawable.xinagcetouxiang);
						} else {
							holder.setImageUrl(R.id.aci_image, StringUtil.toString(HttpUtils.IMAGE_URL
									+ photo.getImgsrc(),"http://"));
						}
						holder.setText(R.id.aci_name, photo.getTitle());
						CheckBox mCheckBox = holder.getView(R.id.cb_choice);
						mCheckBox.setVisibility(View.VISIBLE);
						mCheckBox.setChecked(photo.isChecked());
					}
				};
				photo_list.setAdapter(photoAdapter);
				
				photoAdapter.notifyDataSetChanged();
			}
		}, params);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.aci_edit_btn:
			StringBuffer photoName = new StringBuffer();
			StringBuffer photoId = new StringBuffer();
			for (int i = 0; i < photoes.size(); i++) {
				if(photoes.get(i).isChecked()){
					photoName.append(photoes.get(i).getTitle());
					photoId.append(photoes.get(i).getId());
				}
			}
			if(photoId.length()<1){
				Tools.toast(B1_04_02_SelectPhotoActivity.this, "至少选一个相册");
			}
			setResult(Activity.RESULT_OK, getIntent()
					.putExtra("photoName", photoName.substring(0, photoName.length()))
					.putExtra("photoId", photoId.substring(0, photoId.length())));
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView,
			int position, long id) {
		XiangCeLieBiao photo = photoes.get(position-1);
		if(!photo.isChecked()){
			for (int i = 0; i < photoes.size(); i++) {
				photoes.get(i).setChecked(false);
			}
		}
		photo.setChecked(true);
//		photo.setChecked(!photo.isChecked());
		photoAdapter.notifyDataSetChanged();
	}
/**
 * 上拉刷新
 */
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
/**
 * 下拉加载
 */
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				nowpage = 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	public void onLoad() {
		photo_list.stopRefresh();
		photo_list.stopLoadMore();
		photo_list.setRefreshTime("刚刚");

	}
}
