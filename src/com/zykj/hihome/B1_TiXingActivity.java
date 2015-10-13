package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;

import com.zykj.hihome.base.BaseActivity;


/**
 * @author LSS 2015年9月29日 上午8:50:02
 *
 */
public class B1_TiXingActivity extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_tixing);
		initView();
	}

	public void initView() {
		
//		setListener(iv_erwei);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
//		case R.id.iv_erwei:
//			Intent intent = new Intent();
//			intent.setClass(B1_ShouYeActivity.this, MipcaActivityCapture.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//			break;
		default:
			break;
		}
	}

}
