package com.zykj.hihome;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.AppValue;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

public class A0_Welcome extends BaseActivity {
	public static boolean isForeground = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_a0_welcome);
		JPushInterface.init(getApplicationContext());
		
		mLocationManger=LocationManagerProxy.getInstance(this);
		//进行一次定位
		mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, mLocationListener);
		
		checkLogin();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// Intent intent = new Intent(Welcome.this, MainActivity.class);
				// startActivity(intent);

				String is_intro = getSharedPreferenceValue(AppValue.IS_INTRO);
				boolean should_intro = false;
				int version = Tools.getAppVersion(A0_Welcome.this);
				String save_version = getSharedPreferenceValue(AppValue.VERSION);
				int save_version_int = save_version.equals("") ? -1 : Integer
						.parseInt(save_version);

				if (is_intro.length() > 0 && version == save_version_int) {// �Ѿ����й�ָ��,�Ұ汾�ŷ���
					should_intro = false;
				} else {
					should_intro = true;
				}

				if (should_intro) {
					Intent intent = new Intent(A0_Welcome.this,A1_IntroActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(A0_Welcome.this, B4_01_LoginActivity.class);
					startActivity(intent);
				}
				finish();

			}
		};
		timer.schedule(task, 2000);
	}
	
	private void checkLogin(){
		if(StringUtil.isEmpty(BaseApp.getModel().getUsername())){
			return;
		}
        RequestParams params = new RequestParams();
        params.put("mob", BaseApp.getModel().getMobile());
        params.put("pass", BaseApp.getModel().getPassword());
        HttpUtils.login(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject data = json.getJSONArray(UrlContants.jsonData).getJSONObject(0);
				String avatar = StringUtil.toStringOfObject(data.getString("avatar"));
//				BaseApp.getModel().setAvatar(avatar.replace("app.do", UrlContants.SERVERIP));//头像
				BaseApp.getModel().setAvatar(StringUtil.toStringOfObject(data.getString("avatar")));
				BaseApp.getModel().setUserid(StringUtil.toStringOfObject(data.getString("id")));
				BaseApp.getModel().setMobile(StringUtil.toStringOfObject(data.getString("mobile")));
				BaseApp.getModel().setPassword(BaseApp.getModel().getPassword());//登录密码
				BaseApp.getModel().setNick(StringUtil.toStringOfObject(data.getString("nick")));
				BaseApp.getModel().setSex(StringUtil.toStringOfObject(data.getString("sex")));
				BaseApp.getModel().setAge(StringUtil.toStringOfObject(data.getString("age")));
				BaseApp.getModel().setSign(StringUtil.toStringOfObject(data.getString("sign")));
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				BaseApp.getModel().clear();
				Tools.toast(A0_Welcome.this, "登录失效!");
			}
		}, params);
	}	
	
	//定位
	private LocationManagerProxy mLocationManger;
	private AMapLocationListener mLocationListener=new AMapLocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onProviderDisabled(String provider) {}
		
		@Override
		public void onLocationChanged(Location location) {}
		
		@Override
		public void onLocationChanged(AMapLocation location) {
			Tools.CURRENTCITY = location.getCity().replace("市", "").replace("区", "");
			if (location != null && location.getAMapException().getErrorCode() == 0) {
				BaseApp.getModel().setLatitude(location.getLatitude()+"");
				BaseApp.getModel().setLongitude(location.getLongitude()+"");
				Tools.toast(A0_Welcome.this, "城市="+location.getCity()+"lat="+location.getLatitude()+"long="+location.getLongitude());
			}else{
				Tools.toast(A0_Welcome.this, "定位出现异常");
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		isForeground = true;
		JPushInterface.onResume(this);
	}


	@Override
	protected void onPause() {
		super.onPause();
		isForeground = false;
		JPushInterface.onPause(this);
	}

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!StringUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              //setCostomMsg(showMsg.toString());
			}
		}
	}
//	private void setCostomMsg(String msg){
//		 if (null != msgText) {
//			 msgText.setText(msg);
//			 msgText.setVisibility(android.view.View.VISIBLE);
//        }
//	}
}
