package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Anniversary;
import com.zykj.hihome.data.XiangCeLieBiao;
import com.zykj.hihome.data.ZuiJinXiangPian;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

/**
 * @author LSS 2015年10月19日 下午5:15:28
 *
 */
public class B1_04_XiangCe extends BaseActivity implements IXListViewListener, OnItemClickListener{
	private ImageView im_b104_back;//返回
	private TextView tv_zuijinxiangpian;//最近相片
	private TextView tv_xiangpianliebiao;//相册列表
	private LinearLayout ll_xclb;//相册列表
	private LinearLayout ll_zjxp;//最近相片
	private XListView lv_zjxp,lv_xclb;//最近相片列表
	private static int PERPAGE1=5;//perpage默认每页显示10条信息
	private int nowpage1=1;//当前显示的页面 
	private static int PERPAGE=5;//perpage默认每页显示10条信息
	private int nowpage=1;//当前显示的页面 
	private CommonAdapter<ZuiJinXiangPian> adapter;
	private CommonAdapter<XiangCeLieBiao> adapter1;
	private List<ZuiJinXiangPian> listzjxp = new ArrayList<ZuiJinXiangPian>();
	private List<XiangCeLieBiao> listxclb = new ArrayList<XiangCeLieBiao>();
	private Handler mHandler = new Handler();//异步加载或刷新
	private int statexc=0;//0是最近相片，1是相册列表
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_04_xiangce);
//		if (statexc>0) {
			adapter1 = new CommonAdapter<XiangCeLieBiao>(B1_04_XiangCe.this, R.layout.ui_xiangceliebiao, listxclb) {
				@Override
				public void convert(ViewHolder holder, XiangCeLieBiao xclb) {
					holder.setText(R.id.tv_xcm, xclb.getTitle())
							.setImageUrl(R.id.im_xc, HttpUtils.IMAGE_URL + xclb.getImgsrc())
							.setText(R.id.tv_date, xclb.getAddtime());
				}
			};
//		}else {
			adapter = new CommonAdapter<ZuiJinXiangPian>(B1_04_XiangCe.this, R.layout.ui_zuijinxiangpian, listzjxp) {
				@Override
				public void convert(ViewHolder holder, ZuiJinXiangPian zjxp) {
					holder.setText(R.id.tv_time, zjxp.getAddtime())
							.setImageUrl(R.id.im_xp, HttpUtils.IMAGE_URL + zjxp.getImgsrc())
							.setText(R.id.tv_miaoshu, zjxp.getIntro());
				}
			};
//		}
		initView();
		requestData();
	}

	private void initView() {
		im_b104_back = (ImageView)findViewById(R.id.im_b104_back);
		tv_zuijinxiangpian = (TextView)findViewById(R.id.tv_zuijinxiangpian);
		tv_xiangpianliebiao = (TextView)findViewById(R.id.tv_xiangpianliebiao);
		ll_xclb = (LinearLayout)findViewById(R.id.ll_xclb);
		ll_zjxp = (LinearLayout)findViewById(R.id.ll_zjxp);
		
		lv_zjxp = (XListView)findViewById(R.id.lv_zjxp);
		lv_xclb = (XListView)findViewById(R.id.lv_xclb);
			lv_xclb.setDividerHeight(0);
	        lv_xclb.setPullLoadEnable(true);
	        lv_xclb.setXListViewListener(this);
			lv_xclb.setOnItemClickListener(this);
			lv_xclb.setAdapter(adapter1);
			lv_zjxp.setDividerHeight(0);
	        lv_zjxp.setPullLoadEnable(true);
	        lv_zjxp.setXListViewListener(this);
			lv_zjxp.setOnItemClickListener(this);
			lv_zjxp.setAdapter(adapter);
//		lv_xclb.setDividerHeight(0);
//        lv_xclb.setPullLoadEnable(true);
//        lv_xclb.setXListViewListener(this);
//		lv_xclb.setOnItemClickListener(this);
//		lv_xclb.setAdapter(adapter1);
		setListener(im_b104_back,tv_zuijinxiangpian,tv_xiangpianliebiao);
	}

	private void requestData() {
		if (statexc>0) {
			/** 相册列表 */
			RequestParams params = new RequestParams();
			params.put("fid", BaseApp.getModel().getUserid());
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("nowpage", nowpage1);
			params.put("perpage", PERPAGE1);
			HttpUtils.getXiangCeList(res_xiangceList, params);
		}else {
			/** 最近相片 */
			RequestParams params = new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("nowpage", nowpage);
			params.put("perpage", PERPAGE);
			HttpUtils.geZuiJinXiangPian(res_zuijinxiangpianList, params);
		}
	}
	
/**
 * 最近相片
 */
private AsyncHttpResponseHandler res_zuijinxiangpianList = new HttpErrorHandler() {
	@Override
	public void onRecevieSuccess(JSONObject json) {
		JSONObject jsonObject = json.getJSONObject(UrlContants.jsonData);
		String strArray = jsonObject.getString("list");
		List<ZuiJinXiangPian> list = JSONArray.parseArray(strArray, ZuiJinXiangPian.class);
		if(nowpage == 1){listzjxp.clear();}
		listzjxp.addAll(list);
		adapter.notifyDataSetChanged();
	}
};

/**
* 相册列表
*/
private AsyncHttpResponseHandler res_xiangceList = new HttpErrorHandler() {
@Override
public void onRecevieSuccess(JSONObject json) {
	JSONObject jsonObject = json.getJSONObject(UrlContants.jsonData);
	String strArray = jsonObject.getString("list");
	List<XiangCeLieBiao> list = JSONArray.parseArray(strArray, XiangCeLieBiao.class);
	if(nowpage1 == 1){listxclb.clear();}
	listxclb.addAll(list);
	adapter1.notifyDataSetChanged();
}
};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b104_back:
			this.finish();
			break;
		case R.id.tv_zuijinxiangpian://最近相片
			statexc=0;
			tv_zuijinxiangpian.setTextColor(getResources().getColor(R.color.theme_color));
			tv_xiangpianliebiao.setTextColor(Color.BLACK);
			ll_zjxp.setVisibility(View.VISIBLE);
			ll_xclb.setVisibility(View.GONE);
			requestData();
			break;
		case R.id.tv_xiangpianliebiao:
			statexc=1;
			tv_zuijinxiangpian.setTextColor(Color.BLACK);
			tv_xiangpianliebiao.setTextColor(getResources().getColor(R.color.theme_color));
			ll_zjxp.setVisibility(View.GONE);
			ll_xclb.setVisibility(View.VISIBLE);
			requestData();
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}

	@Override
	public void onRefresh() {
		/**下拉刷新 重建*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (statexc>0) {
					nowpage1 = 1;
					requestData();
					onLoad();
				}else {
					nowpage = 1;
					requestData();
					onLoad();
				}
			}
		}, 1000);		
	}

	@Override
	public void onLoadMore() {
		/**上拉加载分页*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (statexc>0) {
					nowpage1 += 1;
					requestData();
					onLoad();
				}else {
					nowpage += 1;
					requestData();
					onLoad();
				}
			}
		}, 1000);
	}

	private void onLoad() {
		if (statexc>0) {
			lv_xclb.stopRefresh();
			lv_xclb.stopLoadMore();
			lv_xclb.setRefreshTime("刚刚");
		}else {
			lv_zjxp.stopRefresh();
			lv_zjxp.stopLoadMore();
			lv_zjxp.setRefreshTime("刚刚");
		}
	}
	
}