package com.zykj.hihome;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月19日 下午4:32:56
 *
 */
public class B1_02_ZhouRenWuZongLan extends BaseActivity {
	private ImageView im_b102_back;//返回
	private TextView tv_zijirenwu;//自己的任务
	private TextView tv_jieshourenwu;//接受的任务

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_02_zhourenwuzonglan);
		initView();
	}

	private void initView() {
		im_b102_back = (ImageView)findViewById(R.id.im_b102_back);
		tv_zijirenwu = (TextView)findViewById(R.id.tv_zijirenwu);
		tv_jieshourenwu = (TextView)findViewById(R.id.tv_jieshourenwu);
		 setListener(im_b102_back,tv_zijirenwu,tv_jieshourenwu);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b102_back:
			this.finish();
			break;
		case R.id.tv_zijirenwu:
			tv_zijirenwu.setTextColor(getResources().getColor(R.color.theme_color));
			tv_jieshourenwu.setTextColor(Color.BLACK);
			break;
		case R.id.tv_jieshourenwu:
			tv_zijirenwu.setTextColor(Color.BLACK);
			tv_jieshourenwu.setTextColor(getResources().getColor(R.color.theme_color));
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