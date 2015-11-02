package com.zykj.hihome;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.Friend;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

/**
 * @author LSS 2015年9月29日 上午8:50:02
 * 
 */
public class B1_TiXingActivity extends BaseActivity {
	private TextView tv_xxtiaoshu, tv_lttiaoshu, tv_rwtiaoshu;// 消息，聊天，任务 条数
	private TextView tv_time, tv_date;
	private static String mMonth;
	private static String mDay;
	private static String mWay;
	private static final int msgKey1 = 1;
	private RelativeLayout rl_liaotiantixing,rl_xiaotitixing;//聊天提醒，消息提醒

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_tixing);
		initView();
	}

	public void initView() {
		tv_xxtiaoshu = (TextView) findViewById(R.id.tv_xxtiaoshu);
		tv_xxtiaoshu.setText("8");
		tv_xxtiaoshu.setBackgroundResource(R.drawable.background_new_info);

		tv_lttiaoshu = (TextView) findViewById(R.id.tv_lttiaoshu);
		tv_lttiaoshu.setText("9");
		tv_lttiaoshu.setBackgroundResource(R.drawable.background_new_info);

		tv_rwtiaoshu = (TextView) findViewById(R.id.tv_rwtiaoshu);
		tv_rwtiaoshu.setText("10");
		tv_rwtiaoshu.setBackgroundResource(R.drawable.background_new_info);

		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		rl_liaotiantixing = (RelativeLayout) findViewById(R.id.rl_liaotiantixing);
		rl_xiaotitixing = (RelativeLayout) findViewById(R.id.rl_xiaotitixing);
		
		Time time = new Time();
		time.setToNow();
		int minute = time.minute; int hour = time.hour; tv_time.setText(hour+"时 " + minute + "分 ");
		new TimeThread().start();

		String datezhou = StringData();
		tv_date.setText(datezhou);
		 setListener(rl_liaotiantixing,rl_xiaotitixing);
	}

	public class TimeThread extends Thread {
		@Override
		public void run() {
			do {
				try {
					Thread.sleep(60000);
					Message msg = new Message();
					msg.what = msgKey1;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (true);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case msgKey1:
				/*long sysTime = System.currentTimeMillis();
				CharSequence sysTimeStr = DateFormat
						.format("hh:mm:ss", sysTime);
				tv_time.setText(sysTimeStr);*/
				Time time = new Time();
				time.setToNow();
				int minute = time.minute; int hour = time.hour; tv_time.setText(hour+"时 " + minute + "分 ");
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
		 case R.id.rl_liaotiantixing:
			 Intent intent = new Intent();
			 intent.setClass(B1_TiXingActivity.this, B2_LiaoTianActivity.class);
			 startActivity(intent);
		 break;
		 case R.id.rl_xiaotitixing:
			 Intent intent1 = new Intent();
			 intent1.setClass(B1_TiXingActivity.this, B1_08_XiaoXiTiXingActivity.class);
			 startActivity(intent1);
			 break;
		default:
			break;
		}
	}
}
