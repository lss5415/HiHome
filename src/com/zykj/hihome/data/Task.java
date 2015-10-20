package com.zykj.hihome.data;

import java.io.Serializable;

public class Task implements Serializable{
	/**
	 * 任务
	 */
	private static final long serialVersionUID = 1L;
	private String id;//编号ID
	private String title;//任务名称
	private String content;//任务内容
	private String isday;//是否是全天任务
	private String start;//任务开始时间
	private String end;//任务结束时间
	private String tip;//任务提醒0不提醒1正点2五分钟3十分钟4一小时5一天6三天
	private String repeat;//任务重复0不重复1每天2每周3每月4每年
	private String state;//任务状态
	private String addtime;//任务添加时间
	private String tasker;//任务执行人数
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
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getTasker() {
		return tasker;
	}
	public void setTasker(String tasker) {
		this.tasker = tasker;
	}
}
