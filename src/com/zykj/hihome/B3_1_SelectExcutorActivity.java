package com.zykj.hihome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.zykj.hihome.adapter.SortAdapter;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.SortModel;
import com.zykj.hihome.utils.ConstactUtil;
import com.zykj.hihome.utils.Tools;
import com.zykj.hihome.view.CharacterParser;
import com.zykj.hihome.view.LoadingView;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.PinyinComparator;
import com.zykj.hihome.view.SideBar;
import com.zykj.hihome.view.SideBar.OnTouchingLetterChangedListener;

public class B3_1_SelectExcutorActivity extends BaseActivity {

	private MyCommonTitle myCommonTitle;
	private LinearLayout resultdata;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private Map<String, String> callRecords;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_1_select_excutor);

		initView();
		initData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(this, null);
		myCommonTitle.setTitle("选择人物执行人");
		myCommonTitle.setEditTitle("确定");

		resultdata = (LinearLayout) findViewById(R.id.resultdata);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sortListView = (ListView) findViewById(R.id.excute_lvexcutor);
	}

	private void initData() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@SuppressLint("NewApi")
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				String number = callRecords.get(((SortModel) adapter
						.getItem(position)).getName());
				Tools.toast(B3_1_SelectExcutorActivity.this, number);
			}
		});
//		new AsyncTaskConstact().execute(0);
	}

	private class AsyncTaskConstact extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... arg0) {
			int result = -1;
			callRecords = ConstactUtil
					.getAllCallRecords(B3_1_SelectExcutorActivity.this);
			result = 1;
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 1) {
				resultdata.setVisibility(View.VISIBLE);
				List<String> constact = new ArrayList<String>();
				for (Iterator<String> keys = callRecords.keySet().iterator(); keys
						.hasNext();) {
					String key = keys.next();
					constact.add(key);
				}
				String[] names = new String[] {};
				names = constact.toArray(names);
				SourceDateList = filledData(names);

				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(B3_1_SelectExcutorActivity.this,
						SourceDateList);
				sortListView.setAdapter(adapter);
			}
		}

		private List<SortModel> filledData(String[] date) {
			List<SortModel> mSortList = new ArrayList<SortModel>();

			for (int i = 0; i < date.length; i++) {
				SortModel sortModel = new SortModel();
				sortModel.setName(date[i]);
				// 汉字转换成拼音
				String pinyin = characterParser.getSelling(date[i]);
				String sortString = pinyin.substring(0, 1).toUpperCase(
						Locale.getDefault());

				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase(Locale
							.getDefault()));
				} else {
					sortModel.setSortLetters("#");
				}
				mSortList.add(sortModel);
			}
			return mSortList;
		}
	}
}