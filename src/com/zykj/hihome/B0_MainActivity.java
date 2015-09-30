package com.zykj.hihome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.zykj.hihome.base.BaseTabActivity;
import com.zykj.hihome.util.Tools;

/**
 * @author lss 2015年8月8日	控制底部栏 Activity
 *
 */
public class B0_MainActivity extends BaseTabActivity {
	public TabHost m_tab;
	private Intent intent_1;
	private Intent intent_2;
	private Intent intent_3;
	private Intent intent_4;

	// 单选按钮组
	private RadioGroup m_rgroup;
	// 5个单选按钮
	private RadioButton m_radio_shouye;
	private RadioButton m_radio_fujin;
	private RadioButton m_radio_faxian;
	private RadioButton m_radio_wode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_b0_layout);
		m_tab = getTabHost();
		initView();

    }

	private void initView() {
		// 设置圆角边线不启用
		// final TabWidget _widget = m_tab.getTabWidget();
		// _widget.setStripEnabled(false);
		intent_1 = new Intent(this, B1_ShouYeActivity.class);
		intent_2 = new Intent(this, B2_FuJinActivity.class);
		intent_3 = new Intent(this, B3_FaXianActivity.class);
		intent_4 = new Intent(this, B4_WoDeActivity.class);

		m_tab.addTab(buildTagSpec("test1", 0, intent_1));
		m_tab.addTab(buildTagSpec("test2", 1, intent_2));
		m_tab.addTab(buildTagSpec("test3", 2, intent_3));
		m_tab.addTab(buildTagSpec("test4", 3, intent_4));

		m_rgroup = (RadioGroup) findViewById(R.id.tab_rgroup);
		m_radio_shouye = (RadioButton) findViewById(R.id.tab_radio1);
		m_radio_fujin = (RadioButton) findViewById(R.id.tab_radio2);
		m_radio_faxian = (RadioButton) findViewById(R.id.tab_radio3);
		m_radio_wode = (RadioButton) findViewById(R.id.tab_radio4);

		m_rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == m_radio_shouye.getId()) {
					m_tab.setCurrentTabByTag("test1");
				} else if (checkedId == m_radio_fujin.getId()) {
					m_tab.setCurrentTabByTag("test2");
				} else if (checkedId == m_radio_faxian.getId()) {
					m_tab.setCurrentTabByTag("test3");
				} else if (checkedId == m_radio_wode.getId()) {
					m_tab.setCurrentTabByTag("test4");
				}
			}
		});
		m_tab.setCurrentTab(0);

	}

	private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
			Intent content) {
		return m_tab.newTabSpec(tagName).setIndicator(tagLable + "")
				.setContent(content);
	}

	@Override
	protected void onDestroy() {
		Tools.Log("当前tabActivity退出");
		super.onDestroy();
	}

}
