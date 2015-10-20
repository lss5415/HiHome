package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月20日 上午9:05:47
 *
 */
public class B1_06_BangDingPeiOu extends BaseActivity {
	private ImageView im_b106_back;//返回
	private EditText et_bangding;//绑定
	private TextView tv_queding;//确定

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_06_bangdingpeiou);
		initView();
	}

	private void initView() {
		im_b106_back = (ImageView)findViewById(R.id.im_b106_back);
		et_bangding = (EditText)findViewById(R.id.et_bangding);
		tv_queding = (TextView)findViewById(R.id.tv_queding);
		 setListener(im_b106_back,tv_queding);
	}

	/* (non-Javadoc)
	 * @see com.zykj.hihome.base.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b106_back:
			this.finish();
			break;
		 case R.id.tv_queding:
			 if (et_bangding.getText().toString()==null||et_bangding.getText().toString()=="") {
				Toast.makeText(getApplicationContext(), "请填写要绑定的配偶的账号或手机号", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(getApplicationContext(), "恭喜您成功发送邀请通知", Toast.LENGTH_LONG).show();
			}
//		 intent = new Intent(this, Restaura.class);
//		 startActivity(intent);
		 break;
		default:
			break;
		}
	}
}
