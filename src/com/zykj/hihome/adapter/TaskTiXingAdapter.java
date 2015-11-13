package com.zykj.hihome.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.R;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;

public class TaskTiXingAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Task> dataList;
	private List<Boolean> flags1 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags2 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags3 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private String[] stateStr = new String[] { "未接受", "已接受", "待执行", "执行中","已完成", "已取消" };// 0-5任务状态（状态）
	private String[] leftbtn = new String[] { "接受任务", "开始执行", "开始执行", "标记完成","删除任务", "删除任务" };// 0-5任务状态（左边按钮）
	private String[] tipStr = new String[] { "正点", "五分钟", "十分钟", "一小时", "一天","三天", "不提醒" };//0-6提醒间隔
	private String[] repeatStr = new String[] { "不重复", "每天", "每周", "每月", "每年" };//0-4重复间隔

	public TaskTiXingAdapter(Context mContext, List<Task> dataList) {
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		/**
		 * 把每一条数据里的flags1,flags2,flags3进行封装
		 */
		for (int i = 0; i < dataList.size(); i++) {
			flags1.add(true);
			flags2.add(true);
			flags3.add(false);
		}
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHold holder;
		if (convertView == null) {
			holder = new ViewHold();
			convertView = mLayoutInflater.inflate(R.layout.ui_b2_item_tasktx,
					null);
			holder.ly_taskitem = (LinearLayout) convertView.findViewById(R.id.ly_item_onclick);
			holder.rl_button = (RelativeLayout) convertView.findViewById(R.id.rl_button);
			holder.ly_tx_task_time = (LinearLayout) convertView.findViewById(R.id.ly_tx_task_time);

			holder.img_task_publisher_avatar = (ImageView) convertView.findViewById(R.id.tx_task_publisher_avator);
			holder.tv_task_publisher_name = (TextView) convertView.findViewById(R.id.tx_task_publisher_name);
			holder.tv_task_publish_date = (TextView) convertView
					.findViewById(R.id.tx_task_publish_date);
			holder.tv_task_date = (TextView) convertView
					.findViewById(R.id.tx_task_date);
			holder.tv_task_title = (TextView) convertView
					.findViewById(R.id.tx_task_title);
			holder.tv_task_state = (TextView) convertView
					.findViewById(R.id.tx_task_mystate);
			holder.tv_task_tip = (TextView) convertView
					.findViewById(R.id.tx_task_tip);
			holder.tv_task_repeat = (TextView) convertView
					.findViewById(R.id.tx_task_repeat);
			holder.tv_task_tasker = (TextView) convertView
					.findViewById(R.id.tx_tasker_num);
			holder.btn_leftButton = (Button) convertView
					.findViewById(R.id.btn_receive_task);
			holder.btn_rightButton = (Button) convertView
					.findViewById(R.id.btn_refuse_task);

			convertView.setTag(holder);

		} else {
			holder = (ViewHold) convertView.getTag();
		}
		final Task task = dataList.get(position);
		final int state = Integer.parseInt(task.getState());
		final int sid = Integer.parseInt(task.getSid());
		int tip = Integer.parseInt(task.getTip());
		int repeat = Integer.parseInt(task.getRepeat());
		String datastart = StringUtil.isEmpty(task.getStart()) ? "00-00"
				: dataList.get(position).getStart().substring(5, 10);
		String dataend = StringUtil.isEmpty(task.getEnd()) ? "00-00" : dataList
				.get(position).getEnd().substring(5, 10);

		ImageLoader.getInstance().displayImage(
				StringUtil.toString(HttpUtils.IMAGE_URL + task.getAvatar(),
						"http://"), holder.img_task_publisher_avatar);//发布人头像
		holder.tv_task_publisher_name.setText(task.getNick());//发布人昵称
		holder.tv_task_publish_date.setText(task.getAddtime());//发布时间
		// holder.tv_task_state.setText(state == 0 ? "未接受" : state == 1 ? "已接受"
		// : state == 2 ? "待执行" : state == 3 ? "执行中" : state == 4 ? "已完成"
		// : "已取消");
		holder.tv_task_state.setText(stateStr[state]);// 状态,这样写优化代码,易读
		holder.tv_task_tip.setText(tipStr[tip]);//提醒
		holder.tv_task_repeat.setText(repeatStr[repeat]);//重复
		holder.tv_task_title.setText(task.getTitle());//标题
		holder.tv_task_date.setText(Html.fromHtml("<big><font color=#EA5414>"
				+ datastart + "</font></big><br>~" + dataend));//日期
		holder.tv_task_tasker.setText(Integer.parseInt(task.getTasker()) == 1 ? "仅自己"
						: Integer.parseInt(task.getTasker()) + "人");
		holder.btn_leftButton.setText(leftbtn[state]);// 左边按钮根据状态显示不同
		holder.btn_rightButton.setText(0 == state ? "拒绝任务" : "取消任务");// 右边按钮   当状态为0-拒绝，123-取消
		holder.btn_rightButton.setVisibility("45".contains(task.getState()) ? View.GONE : View.VISIBLE);// 右边按钮     当状态为4或5时隐藏

		holder.tv_task_state.setVisibility(flags1.get(position) ? View.VISIBLE: View.GONE);
		holder.tv_task_publish_date.setVisibility(flags2.get(position) ? View.VISIBLE : View.GONE);
		holder.rl_button.setVisibility(flags3.get(position) ? View.VISIBLE: View.GONE);
		// stateAndButton(holder, state, sid);
		/**
		 * 点击Item事件设置显示和隐藏的控件
		 */
		holder.ly_taskitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flags1.clear();
				flags2.clear();
				flags3.clear();
				for (int i = 0; i < dataList.size(); i++) {
					flags1.add(true);
					flags2.add(true);
					flags3.add(false);
				}
				flags1.set(position, false);
				flags2.set(position, false);
				flags3.set(position, true);
				notifyDataSetChanged();
			}
		});
		/**
		 * 接受任务
		 */
		holder.btn_leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stateAndButton(holder, task, sid);
			}
		});

		// 拒绝任务,将任务状态设置为已取消
		holder.btn_rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				modTaskState(task, 5);
			}
		});

		return convertView;

	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 */
	private void delTask(final Task task) {
		RequestParams params = new RequestParams();
		params.put("id", task.getId());
		HttpUtils.delTaskInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				dataList.remove(task);
				notifyDataSetChanged();
			}
		}, params);
	}

	/**
	 * 更改任务状态
	 * 
	 * @param sid
	 * @param state
	 */
	private void modTaskState(final Task task, final int updatestate) {
		RequestParams params = new RequestParams();
		params.put("sid", task.getSid());
		params.put("state", updatestate);
		HttpUtils.modTaskState(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				task.setState(updatestate + "");
				notifyDataSetChanged();
			}
		}, params);
	}

	public final class ViewHold {
		public ImageView img_task_publisher_avatar;// 发布者头像
		public TextView tv_task_publisher_name;// 发布者昵称
		public TextView tv_task_publish_date;// 任务发布的时间
		public TextView tv_task_date;// 任务日期
		public TextView tv_task_title;// 任务标题
		public TextView tv_task_state;// 任务的执行状态
		public TextView tv_task_tip;// 任务图形
		public TextView tv_task_repeat;// 任务重复
		public TextView tv_task_tasker;// 任务执行的人数
		public RelativeLayout rl_button;
		public LinearLayout ly_taskitem;
		public Button btn_leftButton;// 左侧按钮默认隐藏
		public Button btn_rightButton;// 右侧按钮默认隐藏
		public LinearLayout ly_tx_task_time;// 右侧按钮默认隐藏

	}

	private void stateAndButton(ViewHold holder, Task task, int sid) {
		int state = Integer.valueOf(task.getState());
		switch (state) {
		case 0:
			/* 修改 */
			modTaskState(task, state+=1);
			break;
		case 1:
			/* 修改 */
			modTaskState(task, state+=1);
			break;
		case 2:
			/* 修改 */
			modTaskState(task, state+=1);
			break;
		case 3:
			/* 修改 */
			modTaskState(task, state+=1);
			break;
		case 4:
			/* 删除 */
			delTask(task);
			break;
		case 5:
			/* 删除 */
			delTask(task);
			break;
		}
	}

}