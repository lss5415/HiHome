package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Anniversary;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

/**
 * @author LSS 2015年10月19日 下午4:22:17 
 *
 */
public class B1_01_JiNianRiZongLan extends BaseActivity implements IXListViewListener, OnItemClickListener{
	private ImageView im_b101_back;//返回
	private XListView lv_jinianri;//纪念日列表
	private static int PERPAGE=10;//perpage默认每页显示10条信息
	private int nowpage=1;//当前显示的页面 
	private List<Task> listanniversary = new ArrayList<Task>();
	private CommonAdapter<Task> adapter;
	private Handler mHandler = new Handler();//异步加载或刷新

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_01_jinianrizonglan);
		adapter = new CommonAdapter<Task>(B1_01_JiNianRiZongLan.this, R.layout.ui_jinianrizonglan, listanniversary) {
			@Override
			public void convert(ViewHolder holder, Task task) {
				holder.setText(R.id.tv_name, task.getTitle())
						.setImageUrl(R.id.im_tu, HttpUtils.IMAGE_URL + task.getImgsrc())
						.setText(R.id.tv_shijian, task.getMdate().substring(0, 11));
			}
		};
		initView();
		requestData();
	}

	private void initView() {
		im_b101_back = (ImageView)findViewById(R.id.im_b101_back);
		lv_jinianri = (XListView)findViewById(R.id.lv_jinianri);
		lv_jinianri.setDividerHeight(0);
        lv_jinianri.setPullLoadEnable(true);
        lv_jinianri.setXListViewListener(this);
		lv_jinianri.setOnItemClickListener(this);
		lv_jinianri.setAdapter(adapter);
		
		 setListener(im_b101_back);
	}

	private void requestData() {
		/** 纪念日总览 */
		RequestParams params = new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("nowpage", nowpage);
		params.put("perpage", PERPAGE);
		HttpUtils.getAnnversaryList(res_getAnnversaryList, params);
	}
		
	/**
	 * 纪念日总览
	 */
	private AsyncHttpResponseHandler res_getAnnversaryList = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			JSONObject jsonObject = json.getJSONObject(UrlContants.jsonData);
			String strArray = jsonObject.getString("list");
			List<Task> list = JSONArray.parseArray(strArray, Task.class);
			if(nowpage == 1){listanniversary.clear();}
			listanniversary.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b101_back:
			this.finish();
			break;
		// case R.id.im_b1nvshi:
		// intent = new Intent(this, Restaura.class);
		// startActivity(intent);
		// break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		/**下拉刷新 重建*/
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
		/**上拉加载分页*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				nowpage += 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	private void onLoad() {
		lv_jinianri.stopRefresh();
		lv_jinianri.stopLoadMore();
		lv_jinianri.setRefreshTime("刚刚");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String jnrid = listanniversary.get(arg2 - 1).getId();
		Intent itmrhd = new Intent();
		itmrhd.setClass(B1_01_JiNianRiZongLan.this,B3_1_AnniversaryDetailsActivity.class);
		itmrhd.putExtra("id", jnrid);
		startActivity(itmrhd);
	}

}