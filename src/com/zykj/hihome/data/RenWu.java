package com.zykj.hihome.data;

import java.io.Serializable;

public class RenWu implements Serializable {
/**
 * 任务
 */
	private static final long serialVersionUID = 1L;

	private String uid;// 用户ID编号
	private String id;
	private String title;// 任务名称
	private String content;// 任务内容
	private String isday;// 是否是全天任务
	private String start;// 任务开始时间
	private String end;// 任务结束时间
	private String tip;// 任务提醒：数值意义  0不提醒 1正点	2五分钟	3十分钟	4一小时	5一天	6三天
	private String repeat;// 任务重复：数值意义  0不重复	1每天	2每周	3每月	4每年
	private String state;//任务状态：数值意义   0未接受	   1已接受	2待执行	3执行中	4已完成	5已取消

	private String tasker;// 任务执行人数量
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIsday() {
		return isday;
	}
	public void setIsday(String isday) {
		this.isday = isday;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTasker() {
		return tasker;
	}
	public void setTasker(String tasker) {
		this.tasker = tasker;
	}
	
	

}
