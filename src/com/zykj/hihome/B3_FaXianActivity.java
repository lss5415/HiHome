package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年9月29日 上午8:55:45
 *
 */
public class B3_FaXianActivity extends BaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_faxian);
		initView();
	}
	
	public void initView(){
//		tv_remen = (TextView)findViewById(R.id.tv_remen);
//		tv_zuixin = (TextView)findViewById(R.id.tv_zuixin);
//		tv_jinqi = (TextView)findViewById(R.id.tv_jinqi);
//		b3_hongdi_remen = (ImageView) findViewById(R.id.b3_hongdi_remen);//热门活动 --下方黄条
//		b3_hongdi_zuixin = (ImageView) findViewById(R.id.b3_hongdi_zuixin);//最新活动 --下方黄条
//		b3_hongdi_jinqi = (ImageView) findViewById(R.id.b3_hongdi_jinqi);//近期活动 --下方黄条
//		list_huodong = (ListView) findViewById(R.id.list_huodong);
//		lng = getSharedPreferenceValue("lng");
//		lat = getSharedPreferenceValue("lat");
//		
//		setListener(tv_remen,tv_zuixin,tv_jinqi);
	}
	
	@Override
	public void  onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
     /*   case R.id.tv_remen:
        	setVisible();
        	b3_hongdi_remen.setVisibility(View.VISIBLE);
        	setTextColor();
        	tv_remen.setTextColor(getResources().getColor(R.color.all_huang_color));

    		Map<String, String> map1 = new HashMap<String, String>();
    		map1.put("type", "0");
    		map1.put("pagenumber", "1");
    		map1.put("pagesize", "5");
    		map1.put("longitude", lng);
    		map1.put("latitude", lat);
    		json = JsonUtils.toJson(map1);
    		// 发现中活动
    		HuoDong();
        	break;*/
        default:
        	break;
        }
   }
	
}