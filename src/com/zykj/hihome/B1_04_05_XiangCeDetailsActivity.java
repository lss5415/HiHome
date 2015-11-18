package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.XiangCeLieBiao;
import com.zykj.hihome.data.ZuiJinXiangPian;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;

public class B1_04_05_XiangCeDetailsActivity extends BaseActivity {
	private XiangCeLieBiao photo;
	private MyCommonTitle myCommonTitle;
	private TextView tv_add_pic;
	private GridView gv_pic;
	private CommonAdapter<ZuiJinXiangPian> picAdapter;
	private List<ZuiJinXiangPian> images = new ArrayList<ZuiJinXiangPian>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_04_05_photo_details);
		photo = (XiangCeLieBiao) getIntent().getSerializableExtra("photo");
		initView();
		requeatData();

	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle(photo.getTitle());

		tv_add_pic = (TextView) findViewById(R.id.tv_add_picture);
		gv_pic = (GridView) findViewById(R.id.gv_picture);

		gv_pic.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		setListener(tv_add_pic);
	}

	private void requeatData() {
		RequestParams params = new RequestParams();
		params.put("uid", BaseApp.getModel().getUserid());// uid必须，用户ID编号
		params.put("aid", photo.getId());// aid必须，相册ID编号
		// params.put("nowpage", value);
		// params.put("perpage", value);
		HttpUtils.getXiangPianList(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				String jsonArray = json.getJSONObject(UrlContants.jsonData)
						.getString("list");
				List<ZuiJinXiangPian> list = JSON.parseArray(jsonArray,
						ZuiJinXiangPian.class);
				images.addAll(list);
				picAdapter = new CommonAdapter<ZuiJinXiangPian>(
						B1_04_05_XiangCeDetailsActivity.this,
						R.layout.ui_b1_04_05_item_pic, images) {

					@Override
					public void convert(ViewHolder holder, ZuiJinXiangPian zjxp) {
						holder.setImageUrl(
								R.id.details_image,
								StringUtil.toString(HttpUtils.IMAGE_URL
										+ zjxp.getImgsrc()));
					}
				};
				gv_pic.setAdapter(picAdapter);
				picAdapter.notifyDataSetChanged();
			}

		}, params);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_add_picture:
			startActivityForResult(new Intent(
					B1_04_05_XiangCeDetailsActivity.this,
					B1_04_01_AddPictureActivity.class).putExtra("photo", photo), 11);

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 11:
			if (data != null) {
				requeatData();
			}
			break;
		default:
			break;
		}
	}
}
