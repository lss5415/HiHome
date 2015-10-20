package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月19日 下午4:22:17
 *
 */
public class B1_01_JiNianRiZongLan extends BaseActivity {
	private ImageView im_b101_back;//返回
	private ListView lv_jinianri;//纪念日列表
//	private B1_01_JiNianRiAdapter jnradapter;//纪念日adapter

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_01_jinianrizonglan);
		initView();
	}

	private void initView() {
		im_b101_back = (ImageView)findViewById(R.id.im_b101_back);
		lv_jinianri = (ListView)findViewById(R.id.lv_jinianri);
//		lv_jinianri
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		 setListener(im_b101_back);
	}

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
}