package com.zykj.hihome.data;

import java.io.Serializable;

public class Photo implements Serializable{
	
	private static final long serialVersionUID = 2420342366118172089L;
	private String id;//相册ID编号
	private String photoName;//相册名
//	private String fid;//好友ID编号
//	private String nick;//好友姓名
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	
}
