package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月19日 下午5:23:41
 *
 */
public class B1_05_Setting extends BaseActivity {
	private ImageView im_b105_back;//返回

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting);
		initView();
	}

	private void initView() {
		im_b105_back = (ImageView)findViewById(R.id.im_b105_back);
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		 setListener(im_b105_back);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b105_back:
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
