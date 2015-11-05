package com.zykj.hihome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_23_LocationActivity extends BaseActivity implements OnGeocodeSearchListener,OnCameraChangeListener{

	private ProgressDialog progDialog = null;
	private MyCommonTitle myCommonTitle;
	private AMap aMap;
	private MapView mapView;
	private GeocodeSearch geocoderSearch;
	private String addressName;
//	private Marker geoMarker;
	private Marker regeoMarker;
	private LatLonPoint latLonPoint;
	private RegeocodeQuery query;
	private float zoom = 15f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b3_1_location);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		//new LatLng(35.063112, 118.344225);// 临沂市经纬度
		//new LatLng(39.90403, 116.407525);// 北京市经纬度
		float latitude = Float.valueOf(StringUtil.toString(BaseApp.getModel().getLatitude(), "35.063112"));
		float longitude = Float.valueOf(StringUtil.toString(BaseApp.getModel().getLongitude(), "118.344225"));
		latLonPoint = new LatLonPoint(latitude, longitude);
		
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("位置");
		myCommonTitle.setEditTitle("完成");

		if (aMap == null) {
			aMap = mapView.getMap();
//			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			regeoMarker.showInfoWindow();// 设置默认显示一个infowinfow
			regeoMarker.setPositionByPixels(Tools.M_SCREEN_WIDTH/2, Tools.M_SCREEN_HEIGHT/2-Tools.dp2px(this, 50));
//			aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//			aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
			aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
		}
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
		
		getAddress(latLonPoint);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
//				GeocodeAddress address = result.getGeocodeAddressList().get(0);
//				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//						new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()), 15));
				//geoMarker.setPosition(new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()));
//				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//						+ address.getFormatAddress();
//				Tools.toast(B3_1_LocationActivity.this, addressName);
			} else {
				Tools.toast(B3_23_LocationActivity.this, "对不起，没有搜索到相关数据！");
			}
		} else if (rCode == 27) {
			Tools.toast(B3_23_LocationActivity.this, "搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			Tools.toast(B3_23_LocationActivity.this, "key验证无效！");
		} else {
			Tools.toast(B3_23_LocationActivity.this, "未知错误，请稍后重试!错误码为" + rCode);
		}
	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), zoom));
				//regeoMarker.setPosition(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
				Tools.toast(B3_23_LocationActivity.this, addressName);
			} else {
				Tools.toast(B3_23_LocationActivity.this, "对不起，没有搜索到相关数据！");
			}
		} else if (rCode == 27) {
			Tools.toast(B3_23_LocationActivity.this, "搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			Tools.toast(B3_23_LocationActivity.this, "key验证无效！");
		} else {
			Tools.toast(B3_23_LocationActivity.this, "未知错误，请稍后重试!错误码为" + rCode);
		}
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
//		mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());
	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		if(Math.abs(cameraPosition.target.latitude-latLonPoint.getLatitude())>0.0001
				|| Math.abs(cameraPosition.target.longitude-latLonPoint.getLongitude())>0.0001){
			latLonPoint.setLatitude(cameraPosition.target.latitude);
			latLonPoint.setLongitude(cameraPosition.target.longitude);
			zoom = aMap.getCameraPosition().zoom;
			geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
		}
		
//		mCameraTextView.setText("onCameraChangeFinish:" + cameraPosition.toString());
//		VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、

//		LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
//		boolean isContain = latLngBounds.contains(Constants.SHANGHAI);// 判断上海经纬度是否包括在当前地图可见区域
//		if (isContain) {
//			ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//		} else {
//			ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_edit_btn:
			setResult(Activity.RESULT_OK, getIntent()
					.putExtra("latitude", latLonPoint.getLatitude()+"")//经度
					.putExtra("longitude", latLonPoint.getLongitude()+"")//纬度
					.putExtra("address", addressName.length()>2?addressName.substring(0, addressName.length()-2):""));//地址
			finish();
			break;
		default:
			break;
		}
	}
}
