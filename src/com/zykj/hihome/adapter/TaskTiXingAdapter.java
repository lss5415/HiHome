package com.zykj.hihome.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.hihome.R;
import com.zykj.hihome.ViewHolder;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskTiXingAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Task> dataList;
	private Context mContext;
	
	private List<Boolean> flags1 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags2 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的
	private List<Boolean> flags3 = new ArrayList<Boolean>();// 默认false是隐藏的，true是显示的

	public TaskTiXingAdapter(Context mContext,List<Task> dataList) {
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		this.mContext = mContext;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHold holder;
		if (convertView == null) {
			holder = new ViewHold();
			convertView = mLayoutInflater.inflate(R.layout.ui_b2_item_tasktx,null);
			holder.ly_taskitem=(LinearLayout) convertView.findViewById(R.id.ly_item_onclick);
			holder.rl_button=(RelativeLayout) convertView.findViewById(R.id.rl_button);
			
			
			holder.img_task_publisher_avatar = (ImageView) convertView
					.findViewById(R.id.tx_task_publisher_avator);
			holder.tv_task_publisher_name = (TextView) convertView
					.findViewById(R.id.tx_task_publisher_name);
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
			flags1.add(true);flags2.add(true);flags3.add(false);
		} else {
			holder = (ViewHold) convertView.getTag();
		}
		
		holder.tv_task_state.setVisibility(flags1.get(position)?View.VISIBLE:View.GONE);
		holder.tv_task_publish_date.setVisibility(flags2.get(position)?View.VISIBLE:View.GONE);
		holder.rl_button.setVisibility( flags3.get(position)?View.VISIBLE:View.GONE);
		
		
		
		holder.ly_taskitem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.tv_task_state.setVisibility(View.GONE);
				holder.tv_task_publish_date.setVisibility(View.GONE);
				holder.rl_button.setVisibility(View.VISIBLE);
			}
		});
		
		
		Task task=dataList.get(position);
		int state=Integer.parseInt(task.getState());
		int tip=Integer.parseInt(task.getTip());
		int repeat=Integer.parseInt(task.getRepeat());
		String datastart = StringUtil.isEmpty(task.getStart()) ? "00-00" : dataList.get(position).getStart().substring(5, 10);
		String dataend = StringUtil.isEmpty(task.getEnd()) ? "00-00": dataList.get(position).getEnd().substring(5, 10);
		ImageLoader.getInstance().displayImage(
				StringUtil.toString(HttpUtils.IMAGE_URL + task.getAvatar(),
						"http://"), holder.img_task_publisher_avatar);
		holder.tv_task_publisher_name.setText(task.getNick());
		holder.tv_task_publish_date.setText(task.getAddtime());
		holder.tv_task_state.setText(state == 0 ? "未接受": state == 1 ? "已接受" : state == 2 ? "待执行": state == 3 ? "执行中" : state == 4 ? "已完成": "已取消");
		holder.tv_task_date.setText(Html.fromHtml("<big><font color=#EA5414>" + datastart+ "</font></big><br>-" + dataend));
		holder.tv_task_tip.setText(tip == 0 ? "正点": tip == 1 ? "五分钟" : tip == 2 ? "十分钟" : tip == 3 ? "一小时": tip == 4 ? "一天" : tip==5?"三天":"不提醒");
		holder.tv_task_repeat.setText(repeat == 0 ? "不重复": repeat == 1 ? "每天" : repeat == 2 ? "每周": repeat == 3 ? "每月" : "每年");
		holder.tv_task_title.setText(task.getTitle());
		holder.tv_task_tasker.setText(Integer.parseInt(task.getTasker())==1?"仅自己":Integer.parseInt(task.getTasker())+"人");
		holder.btn_leftButton.setText("接受任务");
		holder.btn_rightButton.setText("拒绝任务");


			
			
			
		return convertView;
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
		
	}

}
