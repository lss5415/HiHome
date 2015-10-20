package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.Anniversary;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_AnniversaryDetailsActivity extends BaseActivity {

	private MyCommonTitle myCommonTitle;
	private Anniversary anniversary;
	private ImageView anniversary_img;
	private List<Anniversary> anniversaries=new ArrayList<Anniversary>();
	private CommonAdapter<Anniversary> adapter;
	private TextView anniversary_title, anniversary_date, anniversary_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_b3_1_deails_anniversary);

		initView();
	}

	private void initView() {
		
		myCommonTitle=(MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("纪念日详情");
		
		anniversary_title = (TextView) findViewById(R.id.anniversary_title);
		anniversary_date = (TextView) findViewById(R.id.anniversary_date);
		anniversary_content = (TextView) findViewById(R.id.anniversary_content);
		anniversary_img=(ImageView) findViewById(R.id.anniversary_picture);
		
		initializationDate();
//		requestData();//给详情传值
	}

//	private void requestData() {
//		
//		HttpUtils.getAnnversaryInfo(new JsonHttpResponseHandler(){
//			
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONArray response) {
//				super.onSuccess(statusCode, headers, response);
//				adapter=new CommonAdapter<Anniversary>(B3_1_AnniversaryDetailsActivity.this,R.layout.ui_b3_1_anniversary_deails,anniversaries) {
//					@Override
//					public void convert(ViewHolder holder, Anniversary t) {
//					}
//				};
//			}
//		}, null);
//	}

	private void initializationDate() {
		anniversary_title.setText(anniversary.getTitle());
		anniversary_date.setText(anniversary.getDate());
		anniversary_content.setText(anniversary.getContent());
//		anniversary_img.setImageURI(StringUtil.toString(HttpUtils.IMAGE_URL+anniversary.getImgsrc(), "http://"));
	}

}