package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月23日 下午3:24:05
 *
 */
public class B1_07_WoDeZiLiao extends BaseActivity {
	private ImageView im_b107_back;//返回
	private TextView tv_bianji;//编辑资料

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_1_wodeziliao);
		initView();
	}

	private void initView() {
		im_b107_back = (ImageView)findViewById(R.id.im_b107_back);
		tv_bianji = (TextView)findViewById(R.id.tv_bianji);
		 setListener(im_b107_back,tv_bianji);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b107_back:
			this.finish();
			break;
		 case R.id.tv_bianji:
		 Intent intent = new Intent(this, B1_07_1_GeRenXinXi.class);
		 startActivity(intent);
		 break;
		default:
			break;
		}
	}
}
