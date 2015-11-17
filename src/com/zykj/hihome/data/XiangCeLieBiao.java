package com.zykj.hihome.data;

import java.io.Serializable;

public class XiangCeLieBiao implements Serializable{

	private String id;   // 相册ID编号
	private String title;   // 相册名字
	private String intro;  // 相册描述
	private String addtime;  // 相册添加时间
	private String permission;  //是否有权限查看相册，0所有人有权限，1当前用户有权限，-1没有权限
	private String photos;  // 相册内相片数量
	private String imgsrc;  // 相册内第一张相片地址
	private boolean isChecked;//是否选中
	
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
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}