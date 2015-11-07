package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;

public class B1_07_1_GeRenXinXi extends BaseActivity {
	private ImageView im_b107_1_back;//返回
	private ImageView im_touxiang;//头像
	private EditText et_nick;//姓名
	private TextView tv_mobile;//电话
	private ImageView im_nan;//男
	private ImageView im_nv;//女
//	private DatePicker datePicker1;//年龄
	private EditText et_age;//年龄
	private EditText et_sign;//签名
	private TextView tv_queding;//确定
	private int sex=0;//0：男     1：女
	private String nick,age,sign,sex1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_07_1_gerenxinxi);
		initView();
	}

	private void initView() {
		im_b107_1_back = (ImageView)findViewById(R.id.im_b107_1_back);
		im_touxiang = (ImageView)findViewById(R.id.im_touxiang);
		et_nick = (EditText)findViewById(R.id.et_nick);
		tv_mobile = (TextView)findViewById(R.id.tv_mobile);
		im_nan = (ImageView)findViewById(R.id.im_nan);
		im_nv = (ImageView)findViewById(R.id.im_nv);
//		datePicker1 = (DatePicker)findViewById(R.id.datePicker1);
		et_age = (EditText)findViewById(R.id.et_age);
		et_sign = (EditText)findViewById(R.id.et_sign);
		tv_queding = (TextView)findViewById(R.id.tv_queding);
		
		ImageLoader.getInstance().displayImage(BaseApp.getModel().getAvatar(), im_touxiang);
		et_nick.setText(BaseApp.getModel().getNick());
		tv_mobile.setText(BaseApp.getModel().getMobile());
		if (BaseApp.getModel().getSex().equals("男")) {
			sex=0;
			im_nan.setImageResource(R.drawable.xuanzhong);
			im_nv.setImageResource(R.drawable.buxuanzhong);
		}else {
			sex=1;
			im_nan.setImageResource(R.drawable.buxuanzhong);
			im_nv.setImageResource(R.drawable.xuanzhong);
		}
		et_age.setText(BaseApp.getModel().getAge());
		et_sign.setText(BaseApp.getModel().getSign());

		 setListener(im_b107_1_back,im_nan,im_nv,tv_queding);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b107_1_back:
			this.finish();
			break;
		case R.id.im_nan:
			sex=0;
			im_nan.setImageResource(R.drawable.xuanzhong);
			im_nv.setImageResource(R.drawable.buxuanzhong);
			break;
		case R.id.im_nv:
			sex=1;
			im_nan.setImageResource(R.drawable.buxuanzhong);
			im_nv.setImageResource(R.drawable.xuanzhong);
			break;
		case R.id.tv_queding:
			RequestParams params = new RequestParams();
			nick = et_nick.getText().toString();
			age = et_age.getText().toString();
			sign = et_sign.getText().toString();
			params.put("id", BaseApp.getModel().getUserid());
			params.put("nick", nick);
			if (sex>0) {
				sex1 = "女";
				params.put("sex",sex1);
			}else {
				sex1 = "男";
				params.put("sex",sex1);
			}
			params.put("age", age);
			params.put("sign", et_sign);
			HttpUtils.getXiuGaiInfo(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					Result();					
				}

				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					super.onRecevieFailed(status, json);
					Tools.toast(B1_07_1_GeRenXinXi.this, "修改资料失败！");

				}
			}, params);
			break;
		// case R.id.im_b1nvshi:
//		 intent = new Intent(this, Restaura.class);
//		 startActivity(intent);
		// break;
		default:
			break;
		}
	}
	public void Result(){
		BaseApp.getModel().setNick(nick);
		BaseApp.getModel().setSex(sex1);
		BaseApp.getModel().setAge(age);
		BaseApp.getModel().setSign(sign);					
		Tools.toast(B1_07_1_GeRenXinXi.this, "资料修改成功！");
		this.finish();
	}
}
