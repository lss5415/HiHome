package com.zykj.hihome;

import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.view.MyCommonTitle;

public class B3_1_1_ExecutorsTaskStateActivity extends BaseActivity {
	private MyCommonTitle myCommonTitle;
	private ListView mListView;
	private CommonAdapter<Object> executorAdapter;
	private List<Object> tasker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_1_executor_taskstate);
		JSONArray jsonArray = JSONArray.parseArray(getIntent().getStringExtra("tasker"));
		tasker = jsonArray.subList(0, jsonArray.size());
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("成员任务状态");
		mListView = (ListView) findViewById(R.id.list_excutor_taskstate);
		mListView.setDivider(new ColorDrawable(0xffeeeeee));
		mListView.setDividerHeight(3);

		executorAdapter = new CommonAdapter<Object>(this,R.layout.ui_b3_item_executortaskstate, tasker) {
			@Override
			public void convert(ViewHolder holder, Object task) {
				int state = Integer.valueOf(((JSONObject)task).getString("tasker_state"));
				final String statu = state == 0 ? "未接受" : state == 1 ? "已接受"
						: state == 2 ? "待执行" : state == 3 ? "已执行" : state == 4 ? "已完成" : "已取消";
				holder.setText(R.id.item_excutor_name, ((JSONObject)task).getString("nick"))
						.setText(R.id.item_excutor_state, statu)
						.setImageUrl(R.id.item_excutor_avator, ((JSONObject)task).getString("avatar"), 10f);
			}
		};
		mListView.setAdapter(executorAdapter);
	}
}
