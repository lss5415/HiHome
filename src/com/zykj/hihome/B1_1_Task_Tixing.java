package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

public class B1_1_Task_Tixing extends BaseActivity implements IXListViewListener{
	private int PERPAGE=10;//一页显示的条数
	private int nowpage=1;//当前页
	private MyCommonTitle  myCommonTitle;
//	private RelativeLayout rl_button;
//	private TextView tv_state;
//	private LinearLayout ly_pub_time;
	private XListView mListView;
	private CommonAdapter<JSONObject> txTaskAdapter;
	private Handler mHandler;
	private List<Task> tasks=new ArrayList<Task>();
	private String isMySelf = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b2_task_tixing);
		
		initView();
		getTaskListData();
	}



	private void initView() {
		mHandler=new Handler();
		myCommonTitle=(MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("任务提醒");
		
//		rl_button=(RelativeLayout) findViewById(R.id.rl_button);
//		ly_pub_time=(LinearLayout) findViewById(R.id.ly_tx_task_time);
//		tv_state=(TextView) findViewById(R.id.tx_mytask_state);
		
		mListView=(XListView) findViewById(R.id.list_tasktx);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setDividerHeight(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setXListViewListener(this);

	}
	/**
	 * 请求服务器数据
	 */
	private void getTaskListData() {
		RequestParams params=new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("my", "1");//为1时是自己的任务 为空时是接受的任务
		
		HttpUtils.getMyTasks(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray=json.getJSONObject(UrlContants.jsonData).getJSONArray("list");
				List<JSONObject> list=new ArrayList<JSONObject>();
				for(int i=0;i<jsonArray.size();i++){
					list.add(jsonArray.getJSONObject(i));
				}
				txTaskAdapter=new CommonAdapter<JSONObject>(B1_1_Task_Tixing.this,R.layout.ui_b2_item_tasktx,list) {

					@Override
					public void convert(final ViewHolder holder, JSONObject jsonObject) {
						holder.getView(R.id.onclick).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								holder.getView(R.id.rl_button).setVisibility(View.VISIBLE);
								holder.getView(R.id.tx_mytask_state).setVisibility(View.GONE);
								holder.getView(R.id.ly_tx_task_time).setVisibility(View.GONE);
							}
							
						});
						String datastart = StringUtil.isEmpty(jsonObject.getString("start")) ? "00-00": jsonObject.getString("start").substring(5, 10);
						String dataend = StringUtil.isEmpty(jsonObject.getString("end")) ? "00-00" : jsonObject.getString("end").substring(5, 10);
						 int state=Integer.parseInt(jsonObject.getString("state"));
						int tip=Integer.parseInt(jsonObject.getString("tip"));
						int repeat=Integer.parseInt(jsonObject.getString("repeat"));
						holder.setText(R.id.tx_publisher_name, jsonObject.getString("nick"))
						.setText(R.id.tx_publisher_time, jsonObject.getString("addtime"))
						.setText(R.id.tx_mytask_state, state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消")
						.setText(R.id.tx_task_title, jsonObject.getString("title"))
						.setText(R.id.tx_task_time,tip == 0 ? "正点": tip == 1 ? "五分钟" : tip == 2 ? "十分钟" : tip == 3 ? "一小时": tip == 4 ? "一天" : tip==5?"三天":"不提醒")
						.setText(R.id.tx_task_repeat, repeat == 0 ? "不重复": repeat == 1 ? "每天" : repeat == 2 ? "每周": repeat == 3 ? "每月" : "每年")
						.setText(R.id.tx_task_num, (Integer.parseInt(jsonObject.getString("tasker")))==1?"仅自己":jsonObject.getString("tasker")+"人")
						.setText(R.id.tx_task_date, Html.fromHtml("<big><font color=#EA5414>"
								+ datastart + "</font></big><br>-" + dataend))
						.setImageUrl(R.id.tx_publisher_avator, StringUtil.toString(HttpUtils.IMAGE_URL+jsonObject.getString("avatar"), "htp://"), 15f);
					}
				};
				mListView.setAdapter(txTaskAdapter);
			}
		}, params);
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				nowpage=1;
				getTaskListData();
				onLoad();
			}
		}, 1000);
	}



	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				nowpage+=1;
				getTaskListData();
				onLoad();
			}
		}, 1000);
		
	}
	private void onLoad(){
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		rl_button.setVisibility(View.VISIBLE);
//		tv_state.setVisibility(View.GONE);
//		ly_pub_time.setVisibility(View.GONE);
//	}
	
}
