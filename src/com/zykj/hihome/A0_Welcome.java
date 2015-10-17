package com.zykj.hihome;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.data.AppValue;
import com.zykj.hihome.utils.Tools;

public class A0_Welcome extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_a0_welcome);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// Intent intent = new Intent(Welcome.this, MainActivity.class);
				// startActivity(intent);

				String is_intro = getSharedPreferenceValue(AppValue.IS_INTRO);
				boolean should_intro = false;
				int version = Tools.getAppVersion(A0_Welcome.this);
				String save_version = getSharedPreferenceValue(AppValue.VERSION);
				int save_version_int = save_version.equals("") ? -1 : Integer
						.parseInt(save_version);

				if (is_intro.length() > 0 && version == save_version_int) {// �Ѿ����й�ָ��,�Ұ汾�ŷ���
					should_intro = false;
				} else {
					should_intro = true;
				}

				if (should_intro) {
					Intent intent = new Intent(A0_Welcome.this,A1_IntroActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(A0_Welcome.this, B0_MainActivity.class);
					startActivity(intent);
				}
				finish();

			}
		};
		timer.schedule(task, 2000);
	}
}
