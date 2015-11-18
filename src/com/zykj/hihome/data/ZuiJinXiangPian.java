package com.zykj.hihome.data;

import java.io.Serializable;

public class ZuiJinXiangPian implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;   // 图片ID编号
	private String imgsrc;  // 图片地址
	private String uid;  // 用户ID编号
	private String aid;  // 相册ID编号
	private String intro;  // 相片描述
	private String addtime;  // 相片添加时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
}
