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
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

public class B1_1_Task_Tixing1 extends BaseActivity implements IXListViewListener{
	private int PERPAGE=10;//一页显示的条数
	private int nowpage=1;//当前页
	private MyCommonTitle  myCommonTitle;
//	private RelativeLayout rl_button;
//	private TextView tv_state;
//	private LinearLayout ly_pub_time;
	private XListView mListView;
	private CommonAdapter<JSONObject> txTaskAdapter;
	private Handler mHandler;
	private List<Object> tasks=new ArrayList<Object>();
	private List<Boolean> flags1 = new ArrayList<Boolean>();//默认false是隐藏的，true是显示的
	private List<Boolean> flags2 = new ArrayList<Boolean>();//默认false是隐藏的，true是显示的
	private List<Boolean> flags3 = new ArrayList<Boolean>();//默认false是隐藏的，true是显示的
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
		
		params.put("nowpage", nowpage);
		params.put("perpage", PERPAGE);
		params.put("uid", BaseApp.getModel().getUserid());
		params.put("my", "");//为1时是自己的任务 为空时是接受的任务
		
		HttpUtils.getMyTasks(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				final JSONArray jsonArray=json.getJSONObject(UrlContants.jsonData).getJSONArray("list");
				List<JSONObject> list=new ArrayList<JSONObject>();
				for(int i=0;i<jsonArray.size();i++){
					list.add(jsonArray.getJSONObject(i));
					if(nowpage==1){tasks.clear();
					}
					tasks.addAll(list);
					flags1.add(true);flags2.add(true);flags3.add(false);//显示隐藏
				}
				txTaskAdapter=new CommonAdapter<JSONObject>(B1_1_Task_Tixing1.this,R.layout.ui_b2_item_tasktx,list) {

					@Override
					public void convert(final ViewHolder holder, final JSONObject jsonObject) {
						final int state=Integer.parseInt(jsonObject.getString("state"));//任务状态
						
						holder.setVisibility(R.id.tx_task_mystate, flags1.get(holder.getPosition()));
						holder.setVisibility(R.id.ly_tx_task_time, flags2.get(holder.getPosition()));
						holder.setVisibility(R.id.rl_button, flags3.get(holder.getPosition()));
						
						final String ssid=jsonObject.getString("sid");
						final String tid=jsonObject.getString("id");
						/**
						 * 点击item显示两个Button按钮，状态和发布时间隐藏
						 */
						holder.getView(R.id.ly_item_onclick).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								flags1.clear();flags2.clear();flags3.clear();
								for(int i=0;i<jsonArray.size();i++){
									flags1.add(true);flags2.add(true);flags3.add(false);//显示隐藏
								}
								flags1.set(holder.getPosition(), false);
								flags2.set(holder.getPosition(), false);
								flags3.set(holder.getPosition(), true);
								if(Integer.parseInt(jsonObject.getString("state"))==4||(Integer.parseInt(jsonObject.getString("state")))==5){
									holder.setText(R.id.btn_receive_task, "删除任务");
									holder.getView(R.id.btn_refuse_task).setVisibility(holder.getView(R.id.btn_refuse_task).GONE);
									holder.getView(R.id.btn_receive_task).setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											deleteTask(tid);
										}
									});
								}
//								holder.getView(R.id.rl_button).setVisibility(View.VISIBLE);
//								holder.getView(R.id.tx_mytask_state).setVisibility(View.GONE);
//								holder.getView(R.id.ly_tx_task_time).setVisibility(View.GONE);
								txTaskAdapter.notifyDataSetChanged();
							}
							
						});
						/**
						 * 点击接受任务，此按钮显示内容和任务状态变化
						 */
						holder.getView(R.id.btn_receive_task).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								stateAndButtonChange();
							}

							/**
							 * 点击button按钮更改任务状态
							 */
								private void stateAndButtonChange() {
									 int state=Integer.parseInt(jsonObject.getString("state"));//任务状态
									switch (state) {
									case 1:
										holder.setText(R.id.btn_receive_task, "接受任务");
										holder.setText(R.id.btn_refuse_task, "拒绝任务");
										state+=1;
										String statu1=StringUtil.toString(state);
										modTaskState(ssid,statu1);
										holder.setText(R.id.tx_task_mystate, state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消");
										break;
									case 2:
										holder.setText(R.id.btn_receive_task, "开始执行");
										holder.setText(R.id.btn_refuse_task, "取消任务");
										state+=1;
										String statu2=StringUtil.toString(state);
										modTaskState(ssid,statu2);
										holder.setText(R.id.tx_task_mystate, state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消");
										break;
									case 3:
										holder.setText(R.id.btn_receive_task, "标记完成");
										holder.setText(R.id.btn_refuse_task, "取消任务");
										state+=1;
										String statu3=StringUtil.toString(state);
										modTaskState(ssid,statu3);
										holder.setText(R.id.tx_task_mystate, state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消");
										break;
									case 4:
										holder.setText(R.id.btn_receive_task, "删除任务");
										holder.getView(R.id.btn_refuse_task).setVisibility(holder.getView(R.id.btn_refuse_task).GONE);
										holder.getView(R.id.btn_receive_task).setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												deleteTask(tid);
											}
										});
										break;
									default:
										break;
									}
								}
						});
						/**
						 * 点击拒绝任务，将任务状态置为已取消
						 */
						holder.getView(R.id.btn_refuse_task).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(state == 1){
									//拒绝所做的事情
								}else{
									//取消所做的事情
								}
								int state=Integer.parseInt(jsonObject.getString("state"));
								state=5;
								String statu=StringUtil.toString(state);
								modTaskState(ssid,statu);
								holder.setText(R.id.btn_receive_task, "删除任务");
								holder.getView(R.id.btn_refuse_task).setVisibility(holder.getView(R.id.btn_refuse_task).GONE);
								holder.getView(R.id.btn_receive_task).setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										deleteTask(tid);
									}
								});
							}
						});
						

						String datastart = StringUtil.isEmpty(jsonObject.getString("start")) ? "00-00": jsonObject.getString("start").substring(5, 10);
						String dataend = StringUtil.isEmpty(jsonObject.getString("end")) ? "00-00" : jsonObject.getString("end").substring(5, 10);
						int tip=Integer.parseInt(jsonObject.getString("tip"));
						int repeat=Integer.parseInt(jsonObject.getString("repeat"));
						String[] tags = new String[]{"接受任务","开始执行","开始执行","标记完成","删除任务","删除任务"};
						holder.setText(R.id.tx_task_publisher_name, jsonObject.getString("nick"))
						.setText(R.id.btn_receive_task, tags[state-1])
						.setText(R.id.btn_refuse_task, state == 1?"拒绝任务":"取消任务")//显示文字
						.setVisibility(R.id.btn_refuse_task, "1234".contains(state+""))//是否显示
						.setText(R.id.tx_task_publish_date, jsonObject.getString("addtime"))
						.setText(R.id.tx_task_mystate, state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消")
						.setText(R.id.tx_task_title, jsonObject.getString("title"))
						.setText(R.id.tx_task_tip,tip == 0 ? "正点": tip == 1 ? "五分钟" : tip == 2 ? "十分钟" : tip == 3 ? "一小时": tip == 4 ? "一天" : tip==5?"三天":"不提醒")
						.setText(R.id.tx_task_repeat, repeat == 0 ? "不重复": repeat == 1 ? "每天" : repeat == 2 ? "每周": repeat == 3 ? "每月" : "每年")
						.setText(R.id.tx_tasker_num, (Integer.parseInt(jsonObject.getString("tasker")))==1?"仅自己":jsonObject.getString("tasker")+"人")
						.setText(R.id.tx_task_date, Html.fromHtml("<big><font color=#EA5414>"
								+ datastart + "</font></big><br>-" + dataend))
						.setImageUrl(R.id.tx_task_publisher_avator, StringUtil.isEmpty(jsonObject.getString("avatar"))?jsonObject.getString("avatar"):"htp://", 15f);
					
					}
				};
			
				mListView.setAdapter(txTaskAdapter);
				txTaskAdapter.notifyDataSetChanged();
			}
		}, params);
	}

	/**
	 * 取消任务，将任务状态更改为已取消
	 */
	private void modTaskState(String ssid,String state) {
		RequestParams params=new RequestParams();
		params.put("sid", ssid);
		params.put("state", state);
		
		HttpUtils.modTaskState(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
			}
		}, params);
	}
	/**
	 * 删除任务
	 * @param tid
	 */
	private void deleteTask(String tid) {
		RequestParams params=new RequestParams();
		params.put("id", tid);
		HttpUtils.delTaskInfo(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				Tools.toast(B1_1_Task_Tixing1.this, "此任务已删除");
				getTaskListData();
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

	
}
