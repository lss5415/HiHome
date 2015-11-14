package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.MyCommonTitle;

public class B1_04_03_AddPhotoActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private EditText photo_name, photo_descrip;
	private LinearLayout ly_photoPermission;
	private TextView tv_user_permission;
	private List<String> permissionType=new ArrayList<String>();
	private int permission=-2;//-1为自己可见,0为所有文可见,1,2,3,4为指定好友可见
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_04_03_add_photo);

		initView();
	}

	private void initView() {
		myCommonTitle=(MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("新建相册");
		myCommonTitle.setEditTitle("新建");
		myCommonTitle.setLisener(this, null);
		
		photo_name=(EditText) findViewById(R.id.et_photo_name);
		photo_descrip=(EditText) findViewById(R.id.et_photo_descrip);
		ly_photoPermission=(LinearLayout) findViewById(R.id.ly_photo_permission);
		tv_user_permission=(TextView) findViewById(R.id.tv_photo_permission);
		setListener(ly_photoPermission);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.ly_photo_permission:
			/*选择相册权限*/
			startActivityForResult(new Intent(B1_04_03_AddPhotoActivity.this,
					B1_04_04_PermissionActivity.class).putExtra("permission", permission<-1?0:permission),11);
			break;

		case R.id.aci_edit_btn:
			String photoName=photo_name.getText().toString().trim();
			String photoDescrip=photo_descrip.getText().toString().trim();
			if(StringUtil.isEmpty(photoName)){
				Tools.toast(B1_04_03_AddPhotoActivity.this, "相册名字不能为空");
			}else if (StringUtil.isEmpty(photoDescrip)) {
				Tools.toast(B1_04_03_AddPhotoActivity.this, "相册描述不能为空");
			}else if (StringUtil.isEmpty(userId)) {
				Tools.toast(B1_04_03_AddPhotoActivity.this, "相册权限不能为空");
			}
			RequestParams params= new RequestParams();
			
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("title", photoName);
			params.put("intro", photoDescrip);
			params.put("pm", tv_user_permission);
			HttpUtils.addPhoto(new HttpErrorHandler() {
				
				@Override
				public void onRecevieSuccess(JSONObject json) {
					Tools.toast(B1_04_03_AddPhotoActivity.this, "相册创建成功");
					setResult(RESULT_OK);
					finish();
				}
			}, params);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 11:
			if(data!=null){
//				permission=data.getIntExtra("position", 0);
				userId = data.getStringExtra("permission");
//				permissionType.set(0, Integer.parseInt(userId)==-1?"仅自己":Integer.parseInt(userId)==0?"所有人":tv_user_permission.toString());
//				tv_user_permission.setText(data.getStringExtra("strName"));
				
//				tv_user_permission.setTag(permission);
				if("-10".contains(userId)){
					userId=userId.equals("0")?"所有人":"仅自己";
				}
				tv_user_permission.setText(userId);
			}
			break;

		default:
			break;
		}
		
	}
}
