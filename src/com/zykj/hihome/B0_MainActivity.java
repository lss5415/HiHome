package com.zykj.hihome;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.zykj.hihome.ResideMenu.ResideMenu;
import com.zykj.hihome.ResideMenu.ResideMenuInfo;
import com.zykj.hihome.ResideMenu.ResideMenuItem;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.base.BaseTabActivity;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.Tools;

/**
 * @author lss 2015年8月8日 控制底部栏 Activity
 * 
 */
public class B0_MainActivity extends BaseTabActivity implements
		View.OnClickListener {
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

	private ResideMenu resideMenu;
	private ResideMenuItem itemJiNianRi;
	private ResideMenuItem itemZhouRenWu;
	private ResideMenuItem itemYueRenWu;
	private ResideMenuItem itemXiangCe;
	private ResideMenuItem itemSheZhi;
	private ResideMenuItem itemPeiOu;

	private ResideMenuInfo info;
	private boolean is_closed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_b0_layout);
		m_tab = getTabHost();
		initView();
		setUpMenu();
		setListener();

	}

	@SuppressWarnings("deprecation")
	private void setUpMenu() {
		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_background);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);
		// 禁止使用右侧菜单
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		// create menu items;
		itemJiNianRi = new ResideMenuItem(this, R.drawable.ic_launcher, "纪念日总览");
		itemZhouRenWu = new ResideMenuItem(this, R.drawable.ic_launcher, "周任务总览");
		itemYueRenWu = new ResideMenuItem(this, R.drawable.ic_launcher, "月任务总览");
		itemXiangCe = new ResideMenuItem(this, R.drawable.ic_launcher, "相册");
		itemSheZhi = new ResideMenuItem(this, R.drawable.ic_launcher, "设置");
		itemPeiOu = new ResideMenuItem(this, R.drawable.ic_launcher, "绑定配偶");

		resideMenu.addMenuItem(itemJiNianRi, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemZhouRenWu, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemYueRenWu, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemXiangCe, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemSheZhi, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemPeiOu, ResideMenu.DIRECTION_LEFT);

		String age = BaseApp.getModel().getAge();
		try {
			String year = age.substring(0, 4);
			age = (Calendar.getInstance().get(Calendar.YEAR)-Integer.valueOf(year))+"";
		} catch (Exception e) {
			age = StringUtil.toString(BaseApp.getModel().getAge());
		}
		String name = BaseApp.getModel().getNick();
		String sex = BaseApp.getModel().getSex();
		info = new ResideMenuInfo(this, HttpUtils.IMAGE_URL+BaseApp.getModel().getAvatar(), 
				Html.fromHtml("<big><big><big>"+name+"</big></big></big><br>年龄:"+age+" "+sex),BaseApp.getModel().getSign());
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			is_closed = false;
		}

		@Override
		public void closeMenu() {
			is_closed = true;
		}
	};

	private void setListener() {
		resideMenu.addMenuInfo(info);

		itemJiNianRi.setOnClickListener(this);
		itemZhouRenWu.setOnClickListener(this);
		itemYueRenWu.setOnClickListener(this);
		itemXiangCe.setOnClickListener(this);
		itemSheZhi.setOnClickListener(this);
		itemPeiOu.setOnClickListener(this);

		info.setOnClickListener(this);
	}

	private void initView() {
		// 设置圆角边线不启用
		// final TabWidget _widget = m_tab.getTabWidget();
		// _widget.setStripEnabled(false);
		intent_1 = new Intent(this, B1_TiXingActivity.class);
		intent_2 = new Intent(this, B2_LiaoTianActivity.class);
		intent_3 = new Intent(this, B3_0_TaskActivity.class);
		intent_4 = new Intent(this, B4_HaoYouActivity.class);

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

	@Override
	public void onClick(View view) {
		if (view == itemJiNianRi) {
			Intent intent = new Intent();
			intent.putExtra("flog", "纪念日总览");
			intent.setClass(getApplicationContext(), B1_01_JiNianRiZongLan.class);
			startActivity(intent);
		}else if (view == itemZhouRenWu) {
			Intent intent = new Intent();
			intent.putExtra("flog", "周任务总览");
			intent.setClass(getApplicationContext(), B1_02_ZhouRenWuZongLan.class);
			startActivity(intent);
		} else if (view == itemYueRenWu) {
			Intent intent = new Intent();
			intent.putExtra("flog", "月任务总览");
			intent.setClass(getApplicationContext(), B1_03_YueRenWuZongLan.class);
			startActivity(intent);
		} else if (view == itemXiangCe) {
			Intent intent = new Intent();
			intent.putExtra("flog", "相册");
			intent.setClass(getApplicationContext(), B1_04_XiangCe.class);
			startActivity(intent);
		} else if (view == itemSheZhi) {
			Intent intent = new Intent();
			intent.putExtra("flog", "设置");
			intent.setClass(getApplicationContext(), B1_05_Setting.class);
			startActivity(intent);
		} else if (view == itemPeiOu) {
			Intent intent = new Intent();
			intent.putExtra("flog", "绑定配偶");
			intent.setClass(getApplicationContext(), B1_06_BangDingPeiOu.class);
			startActivity(intent);
		} else if (view == info) {
			Intent intent = new Intent();
			intent.putExtra("flog", "个人信息");
			intent.setClass(getApplicationContext(), B1_07_WoDeZiLiao.class);
			startActivity(intent);
		}
	}
}
