package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Anniversary;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_AnniversaryDetailsActivity extends BaseActivity {

	private MyCommonTitle myCommonTitle;
	private Task task;
	private CircularImage img_anni_avator;
	private ImageView anniversary_img;
	private List<Task> tasks = new ArrayList<Task>();
	private CommonAdapter<Task> adapter;
	private TextView anniversary_title, anniversary_date, anniversary_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_deails_anniversary);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
	}

	private void initView() {

		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("纪念日详情");

		img_anni_avator = (CircularImage) findViewById(R.id.img_anni_avator);
		anniversary_title = (TextView) findViewById(R.id.anniversary_title);
		anniversary_date = (TextView) findViewById(R.id.anniversary_date);
		anniversary_content = (TextView) findViewById(R.id.anniversary_content);
		anniversary_img = (ImageView) findViewById(R.id.anniversary_picture);

		// initializationDate();
		requestData();// 给详情传值
	}

	private void requestData() {
		RequestParams params=new RequestParams();
		
		params.put("id",task.getId());
		HttpUtils.getAnnversaryInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONObject jsonObject=json.getJSONArray("datas").getJSONObject(0);
				anniversary_title.setText(jsonObject.getString("title"));
				anniversary_date.setText(jsonObject.getString("mdate"));
				anniversary_content.setText(jsonObject.getString("content"));
				ImageLoader.getInstance().displayImage(
						StringUtil.toString(HttpUtils.IMAGE_URL + task.getImgsrc(),
								"http://"), img_anni_avator);
			}
		}, params);
	}

	private void initializationDate() {
		anniversary_title.setText(task.getTitle());
		anniversary_date.setText(task.getMdate());
		anniversary_content.setText(task.getContent());
		ImageLoader.getInstance().displayImage(
				StringUtil.toString(HttpUtils.IMAGE_URL + task.getImgsrc(),
						"http://"), img_anni_avator);

	}
}
