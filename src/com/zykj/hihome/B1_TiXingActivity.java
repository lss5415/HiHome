package com.zykj.hihome;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.AbstractHttpHandler;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.DateUtil;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyTextView;

/**
 * @author LSS 2015年9月29日 上午8:50:02
 */
public class B1_TiXingActivity extends BaseActivity {
	private ImageView im_tianqi;
	private CircularImage img_avatar;
	private TextView tv_wendu, tv_zgd, tv_warning, tv_xxtiaoshu;
	private TextView tv_lttiaoshu, tv_rwtiaoshu, tv_time, tv_date;
	private MyTextView tixing_bottom;
	private static String mMonth;
	private static String mDay;
	private static String mWay;
	private static final int msgKey1 = 1;
	private RelativeLayout rl_liaotiantixing,rl_xiaotitixing,rl_renwutixing;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_tixing);
		initView();
		requestData();
	}

	/**
	 * 获取天气预报
	 */
	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("city", Tools.CURRENTCITY);
		HttpUtils.getWeatherInfo(new AbstractHttpHandler() {
			@Override
			public void onJsonSuccess(JSONObject json) {
				JSONObject jsonObject = json.getJSONArray("HeWeather data service 3.0").getJSONObject(0);
				if("ok".equals(jsonObject.getString("status"))){
					String code = jsonObject.getJSONObject("now").getJSONObject("cond").getString("code");
					ImageLoader.getInstance().displayImage("http://files.heweather.com/cond_icon/"+code+".png", im_tianqi);
					tv_wendu.setText(jsonObject.getJSONObject("now").getString("tmp")+"℃");
					JSONObject tmp = jsonObject.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp");
					tv_zgd.setText("L"+tmp.getString("min")+"-H"+tmp.getString("max"));
					String warning = jsonObject.getJSONObject("suggestion").getJSONObject("drsg").getString("txt");
					tv_warning.setText(warning);
				}else{
					Tools.toast(B1_TiXingActivity.this, jsonObject.getString("status"));
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Tools.toast(B1_TiXingActivity.this, "获取天气失败");
			}
		}, params);
	}

	/**
	 * 初始化页面
	 */
	public void initView() {
		im_tianqi = (ImageView) findViewById(R.id.im_tianqi);//天气
		tv_wendu = (TextView) findViewById(R.id.tv_wendu);//温度
		tv_zgd = (TextView) findViewById(R.id.tv_zgd);//最低-最高温度
		tv_warning = (TextView) findViewById(R.id.tv_warning);//提醒
		
		tv_time = (TextView) findViewById(R.id.tv_time);//时间
		tv_date = (TextView) findViewById(R.id.tv_date);//日期

		rl_xiaotitixing = (RelativeLayout) findViewById(R.id.rl_xiaotitixing);
		tv_xxtiaoshu = (TextView) findViewById(R.id.tv_xxtiaoshu);//消息提醒数量
		rl_liaotiantixing = (RelativeLayout) findViewById(R.id.rl_liaotiantixing);
		tv_lttiaoshu = (TextView) findViewById(R.id.tv_lttiaoshu);//聊天提醒数量
		rl_renwutixing = (RelativeLayout) findViewById(R.id.rl_renwutixing);
		tv_rwtiaoshu = (TextView) findViewById(R.id.tv_rwtiaoshu);//任务提醒数量
		
		tixing_bottom = (MyTextView) findViewById(R.id.tixing_bottom);//倾斜字体
		
		img_avatar=(CircularImage) findViewById(R.id.user_avatar);
//		ImageLoader.getInstance().displayImage(StringUtil.toString(HttpUtils.IMAGE_URL+BaseApp.getModel().getAvatar(), "http://"), img_avatar);
		
		tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm"));
		mHandler.sendEmptyMessage(msgKey1);
		
		String datezhou = StringData();
		tv_date.setText(datezhou);
		setListener(rl_xiaotitixing, rl_liaotiantixing, rl_renwutixing);
	}

	/**
	 * 首页时间
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case msgKey1:
				tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm"));
				mHandler.sendEmptyMessageDelayed(1, 5000);
				break;
			default:
				break;
			}
		}
	};

	public static String StringData() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return mMonth + "月" + mDay + "日" + "（星期" + mWay + "）";
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		 case R.id.rl_xiaotitixing:
			 /*消息提醒*/
			 startActivity(new Intent(this, B1_08_XiaoXiTiXingActivity.class));
			 break;
		 case R.id.rl_liaotiantixing:
			 /*聊天提醒*/
			 startActivity(new Intent(this, B2_LiaoTianActivity.class));
		 break;
		 case R.id.rl_renwutixing:
			 /*任务提醒*/
			 startActivity(new Intent(B1_TiXingActivity.this, B1_1_Task_Tixing.class));
			 break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(msgKey1);
	}
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeMessages(msgKey1);
	}
}
