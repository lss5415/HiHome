package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hihome.base.BaseActivity;

public class B1_07_GeRenXinXi extends BaseActivity {
	private ImageView im_b107_back;//返回

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_gerenxinxi);
		initView();
	}

	private void initView() {
		im_b107_back = (ImageView)findViewById(R.id.im_b107_back);
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		 setListener(im_b107_back);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b107_back:
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
