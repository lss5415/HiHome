package com.zykj.hihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zykj.hihome.base.BaseActivity;

/**
 * @author LSS 2015年10月19日 下午5:23:41
 * 
 */
public class B1_05_Setting extends BaseActivity {
	private ImageView im_b105_back;// 返回
	private LinearLayout ly_task_permission, ly_message_notice,
			ly_sound_notice, ly_chat_record, ly_version_update,
			ly_function_intro, ly_help_feedback, ly_logout, ly_cancle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_05_setting);
		initView();
	}

	private void initView() {
		im_b105_back = (ImageView) findViewById(R.id.im_b105_back);//
		ly_task_permission = (LinearLayout) findViewById(R.id.ly_task_permission);// 任务权限
		ly_message_notice = (LinearLayout) findViewById(R.id.ly_message_notice);// 消息通知
		ly_sound_notice = (LinearLayout) findViewById(R.id.ly_sound_notice);// 声音通知
		ly_chat_record = (LinearLayout) findViewById(R.id.ly_chat_record);// 聊天记录
		ly_version_update = (LinearLayout) findViewById(R.id.ly_version_update);// 版本更新
		ly_function_intro = (LinearLayout) findViewById(R.id.ly_function_intro);// 功能介绍
		ly_help_feedback = (LinearLayout) findViewById(R.id.ly_help_feedback);// 帮助与反馈
		ly_logout = (LinearLayout) findViewById(R.id.ly_logout);// 注销登录
		ly_cancle = (LinearLayout) findViewById(R.id.ly_cancle);// 退出账号
		// tv_goodsid = (TextView)findViewById(R.id.tv_goodsid);
		setListener(im_b105_back);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_b105_back:
			this.finish();
			break;
		case R.id.ly_task_permission:// 任务权限

			break;
		case R.id.ly_message_notice:// 消息通知

			break;
		case R.id.ly_sound_notice:// 声音通知

			break;
		case R.id.ly_chat_record:// 聊天记录

			break;
		case R.id.ly_version_update:// 版本更新

			break;
		case R.id.ly_function_intro:// 功能介绍

			break;
		case R.id.ly_help_feedback:// 帮助与反馈

			break;
		case R.id.ly_logout:// 注销登录

			break;
		case R.id.ly_cancle:// 退出账号

			break;
		default:
			break;
		}
	}
}
