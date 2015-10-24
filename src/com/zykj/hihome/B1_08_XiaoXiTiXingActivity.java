package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月24日 上午10:14:59
 *
 */
public class B1_08_XiaoXiTiXingActivity extends BaseActivity {
	private ImageView im_b108_back;//返回
//	private TextView tv_bianji;//编辑资料

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_08_xiaoxitixing);
		initView();
	}

	private void initView() {
		im_b108_back = (ImageView)findViewById(R.id.im_b108_back);
		 setListener(im_b108_back);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b108_back:
			this.finish();
			break;
		 /*case R.id.tv_bianji:
			 Intent intent = new Intent(this, B1_07_1_GeRenXinXi.class);
			 startActivity(intent);
		 break;*/
		default:
			break;
		}
	}
}
