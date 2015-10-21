package com.zykj.hihome.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zykj.hihome.R;
import com.zykj.hihome.data.Task;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.utils.StringUtil;

/**
 * Created by ss on 15-4-23.
 */
public class TaskAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<Task> tasks;
    private int mType;
    private boolean flag = false;

    public TaskAdapter(Context ctx, List<Task> tasks, int type) {
        inflater=LayoutInflater.from(ctx);
        this.mType = type;
        this.tasks=tasks;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(StringUtil.isEmpty(tasks.get(position).getState())){
            ViewHolder1 holder;
            if(convertView == null){
                holder=new ViewHolder1();
            	convertView = inflater.inflate(R.layout.ui_b3_item_anniversay, parent, false);
                holder.rv_me_avatar = (CircularImage)convertView.findViewById(R.id.rv_me_avatar);//图片
                holder.aci_title = (TextView)convertView.findViewById(R.id.aci_title);//标题
                holder.aci_time = (TextView)convertView.findViewById(R.id.aci_time);//时间
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder1)convertView.getTag();
            }
        	Task task = tasks.get(position);
			//String time = StringUtil.isEmpty(task.getAddtime())?"00-00":task.getStart().substring(0, 10);
			//holder.rv_me_avatar.setText(task.getTitle());
			holder.aci_title.setText(task.getTitle());
			holder.aci_time.setText(task.getAddtime());
            return convertView;
        }else{
	        ViewHolder2 holder2;
	        if(!flag){
	            holder2=new ViewHolder2();
	        	convertView = inflater.inflate(R.layout.ui_b3_item_task, parent, false);
	        	holder2.date = (TextView)convertView.findViewById(R.id.date);//图片
	        	holder2.task_title = (TextView)convertView.findViewById(R.id.task_title);//标题
	        	holder2.task_time = (TextView)convertView.findViewById(R.id.task_time);//时间
	        	holder2.task_repeat = (TextView)convertView.findViewById(R.id.task_repeat);//时间
	        	holder2.task_tasker = (TextView)convertView.findViewById(R.id.task_tasker);//时间
	        	holder2.task_num = (TextView)convertView.findViewById(R.id.task_num);//时间
	        	holder2.task_state = (TextView)convertView.findViewById(R.id.task_state);//时间
	            convertView.setTag(holder2);
	    	}else{
	    		holder2= (ViewHolder2)convertView.getTag();
	    	}
	    	Task task = tasks.get(position);
			String datastart = StringUtil.isEmpty(task.getStart())?"00-00":task.getStart().substring(5, 10);
			String dataend = StringUtil.isEmpty(task.getEnd())?"00-00":task.getEnd().substring(5, 10);
			int tip = Integer.valueOf(task.getTip());
			int repeat = Integer.valueOf(task.getRepeat());
			int state = Integer.valueOf(task.getState());
			holder2.date.setText(Html.fromHtml("<big><font color=#EA5414>"+datastart+"</font></big><br>-"+dataend));
			holder2.task_title.setText(task.getTitle());
			holder2.task_time.setText(tip==0?"不提醒":tip==1?"正点":tip==2?"五分钟":tip==3?"十分钟":tip==4?"一小时":tip==5?"一天":"三天");
			holder2.task_repeat.setText(repeat==0?"不重复":repeat==1?"每天":repeat==2?"每周":repeat==3?"每月":"每年");
			holder2.task_tasker.setText("发布人："+task.getTasker());
			holder2.task_tasker.setVisibility(mType==2?View.VISIBLE:View.GONE);
			holder2.task_num.setText(mType==2?task.getTasker()+"人":"张三");
			holder2.task_num.setVisibility(mType==1?View.GONE:View.VISIBLE);
			holder2.task_state.setText(state==0?"未接受":state==1?"已接受":state==2?"待执行":state==3?"执行中":state==4?"已完成":"已取消");
	        return convertView;
        }
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
