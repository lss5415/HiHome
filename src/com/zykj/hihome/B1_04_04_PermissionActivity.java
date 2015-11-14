package com.zykj.hihome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.view.MyCommonTitle;

public class B1_04_04_PermissionActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private LinearLayout ly_permission_other, ly_permission_all,
			ly_permission_self;
	private ImageView img_permission_all, img_permission_self;
	private TextView tv_permission_other;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b1_04_04_permission);

		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("谁能看见");
		myCommonTitle.setEditTitle("完成");
		ly_permission_all = (LinearLayout) findViewById(R.id.ly_select_permission_all);
		ly_permission_self = (LinearLayout) findViewById(R.id.ly_select_permission_self);
		ly_permission_other = (LinearLayout) findViewById(R.id.ly_select_permission_other);
		img_permission_all = (ImageView) findViewById(R.id.img_select_allpermission_all);
		img_permission_self = (ImageView) findViewById(R.id.img_select_permission_self);
		tv_permission_other = (TextView) findViewById(R.id.tv_select_permission_other);

		setListener(ly_permission_all, ly_permission_self, ly_permission_other);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ly_select_permission_all:
			img_permission_all.setVisibility(View.VISIBLE);
			img_permission_self.setVisibility(View.GONE);

			break;
		case R.id.ly_select_permission_self:
			img_permission_self.setVisibility(View.VISIBLE);
			img_permission_all.setVisibility(View.GONE);
//			setResult(RESULT_OK,getIntent().putExtra("strName",BaseApp.getModel().getNick())
//									.putExtra("userId",BaseApp.getModel().getUserid()));
			break;
		case R.id.ly_select_permission_other:
			img_permission_all.setVisibility(View.GONE);
			img_permission_self.setVisibility(View.GONE);
			startActivityForResult(new Intent(this,
					B3_24_SelectExecutorActivity.class), 21);
			break;
		case R.id.aci_edit_btn:
			if (img_permission_all.getVisibility() == View.VISIBLE) {
				setResult(Activity.RESULT_OK, getIntent().putExtra("permission", "0"));
			} else if (img_permission_self.getVisibility() == View.VISIBLE) {
				setResult(Activity.RESULT_OK, getIntent().putExtra("permission", "-1"));
			} else {
				setResult(
						Activity.RESULT_OK,
						getIntent().putExtra("permission",
								tv_permission_other.getText().toString()));
			}
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 21:
			/* 选择执行人 */
			if (data != null) {
				tv_permission_other.setText(data.getStringExtra("strName"));
				userId = data.getStringExtra("strId");
				tv_permission_other.setTag(userId);
			}
			break;
		}
	}
}