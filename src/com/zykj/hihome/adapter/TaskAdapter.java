package com.zykj.hihome.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.R;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;

/**
 * Created by ss on 15-4-23.
 */
public class TaskAdapter extends BaseAdapter {

	private static final int TYPE_COUNT = 2;// item类型的总数
	private static final int TYPE_TASK = 0;// 任务类型
	private static final int TYPE_ANNIVER = 1;// 纪念日类型
	private LayoutInflater inflater;
	private List<Task> tasks;
	private int mType;

	public TaskAdapter(Context ctx, List<Task> tasks, int type) {
		inflater = LayoutInflater.from(ctx);
		this.mType = type;
		this.tasks = tasks;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Task getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (!StringUtil.isEmpty(tasks.get(position).getMdate())) {
			return TYPE_ANNIVER;// 纪念日类型
		} else {
			return TYPE_TASK;// 任务类型
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View anniverView = null;// 纪念日类型
		View taskView = null;// 任务类型
		int currentType = getItemViewType(position);
		if (currentType == TYPE_ANNIVER) {
			ViewHolder1 holder = null;
			if (convertView == null) {
				holder = new ViewHolder1();
				anniverView = inflater.inflate(R.layout.ui_b3_item_anniversay,
						parent, false);
				holder.rv_me_avatar = (CircularImage) anniverView
						.findViewById(R.id.rv_me_avatar);// 图片
				holder.aci_title = (TextView) anniverView
						.findViewById(R.id.aci_title);// 标题
				holder.aci_time = (TextView) anniverView
						.findViewById(R.id.aci_time);// 时间
				convertView = anniverView;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder1) convertView.getTag();
			}
			Task task = tasks.get(position);
			// String time =
			// StringUtil.isEmpty(task.getAddtime())?"00-00":task.getStart().substring(0,
			// 10);
			// holder.rv_me_avatar.set
			ImageLoader.getInstance().displayImage(
					StringUtil.toString(HttpUtils.IMAGE_URL + task.getImgsrc(),
							"http://"), holder.rv_me_avatar);
			holder.aci_title.setText(task.getTitle());
			holder.aci_time.setText(task.getMdate().substring(0, 11));

		} else if (currentType == TYPE_TASK) {
			ViewHolder2 holder2 = null;
			if (convertView == null) {
				holder2 = new ViewHolder2();
				taskView = inflater.inflate(R.layout.ui_b3_item_task, parent,
						false);
				holder2.date = (TextView) taskView.findViewById(R.id.task_date);// 日期
				holder2.task_title = (TextView) taskView.findViewById(R.id.task_title);// 标题
				holder2.task_time = (TextView) taskView.findViewById(R.id.task_time);// 提醒时间
				holder2.task_repeat = (TextView) taskView.findViewById(R.id.task_repeat);// 是否重复
				holder2.task_tasker = (TextView) taskView.findViewById(R.id.task_tasker);// 发布人
				holder2.task_num = (TextView) taskView.findViewById(R.id.task_num);// 执行人数
				holder2.task_state = (TextView) taskView.findViewById(R.id.task_state);// 执行状态
				convertView = taskView;
				convertView.setTag(holder2);
			} else {
				holder2 = (ViewHolder2) convertView.getTag();
			}
			Task task = tasks.get(position);
			String datastart = StringUtil.isEmpty(task.getStart()) ? "00-00": task.getStart().substring(5, 10);
			String dataend = StringUtil.isEmpty(task.getEnd()) ? "00-00" : task.getEnd().substring(5, 10);
			int tip = Integer.valueOf(task.getTip());
			int repeat = Integer.valueOf(task.getRepeat());
			int state = 0;
			if (StringUtil.isEmpty(task.getTasker().toString())) {
				if (task.getTaskerList().size() > 0) {
					state = Integer.valueOf(task.getTaskerList().get(0).get("tasker_state"));
				}
			}
			holder2.date.setText(Html.fromHtml("<big><font color=#EA5414>"
					+ datastart + "</font></big><br>-" + dataend));
			holder2.task_title.setText(task.getTitle());
			holder2.task_time.setText(tip == 0 ? "不提醒" : tip == 1 ? "正点": tip == 2 ? "五分钟" : tip == 3 ? "十分钟" : tip == 4 ? "一小时": tip == 5 ? "一天" : "三天");
			holder2.task_repeat.setText(repeat == 0 ? "不重复": repeat == 1 ? "每天" : repeat == 2 ? "每周": repeat == 3 ? "每月" : "每年");
			holder2.task_tasker.setText("发布人：" + task.getNick());
			holder2.task_tasker.setVisibility(mType == 2 ? View.VISIBLE: View.GONE);
			if (StringUtil.isEmpty(task.getTasker().toString())) {
				if (task.getTaskerList().size() == 1) {// 如果执行人只有一个
					holder2.task_num.setText(mType == 2 ? "仅自己" : (mType == 3)&& (StringUtil.isEmpty(task.getNick())) ? "张三": task.getNick());
				} else {
					holder2.task_num.setText(task.getTaskerList().size() + "人");
				}
			}
			holder2.task_num.setVisibility(mType == 1 ? View.GONE
					: View.VISIBLE);
			if (StringUtil.isEmpty(task.getTasker().toString())) {
				if (task.getTaskerList().size() == 1) {// 如果执行人只有一个
					holder2.task_state.setText(state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消");
				} else {
					holder2.task_state.setText("多人任务");
				}
			}
		}
		return convertView;
	}

	class ViewHolder1 {
		CircularImage rv_me_avatar;
		TextView aci_title;
		TextView aci_time;
	}

	class ViewHolder2 {
		TextView date;
		TextView task_title;
		TextView task_time;
		TextView task_repeat;
		TextView task_tasker;
		TextView task_num;
		TextView task_state;
	}
}
