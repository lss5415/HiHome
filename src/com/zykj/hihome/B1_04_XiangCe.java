package com.zykj.hihome;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月19日 下午5:15:28
 *
 */
public class B1_04_XiangCe extends BaseActivity {
	private ImageView im_b104_back;//返回
	private TextView tv_zuijinxiangpian;//最近相片
	private TextView tv_xiangpianliebiao;//相册列表
	private LinearLayout ll_xiangce;//相册列表
	private LinearLayout ll_zuixin;//最近相片

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_04_xiangce);
		initView();
	}

	private void initView() {
		im_b104_back = (ImageView)findViewById(R.id.im_b104_back);
		tv_zuijinxiangpian = (TextView)findViewById(R.id.tv_zuijinxiangpian);
		tv_xiangpianliebiao = (TextView)findViewById(R.id.tv_xiangpianliebiao);
		ll_xiangce = (LinearLayout)findViewById(R.id.ll_xiangce);
		ll_zuixin = (LinearLayout)findViewById(R.id.ll_zuixin);
		setListener(im_b104_back,tv_zuijinxiangpian,tv_xiangpianliebiao);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b104_back:
			this.finish();
			break;
		case R.id.tv_zuijinxiangpian:
			tv_zuijinxiangpian.setTextColor(getResources().getColor(R.color.theme_color));
			tv_xiangpianliebiao.setTextColor(Color.BLACK);
			ll_zuixin.setVisibility(View.VISIBLE);
			ll_xiangce.setVisibility(View.GONE);
			break;
		case R.id.tv_xiangpianliebiao:
			tv_zuijinxiangpian.setTextColor(Color.BLACK);
			tv_xiangpianliebiao.setTextColor(getResources().getColor(R.color.theme_color));
			ll_zuixin.setVisibility(View.GONE);
			ll_xiangce.setVisibility(View.VISIBLE);
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