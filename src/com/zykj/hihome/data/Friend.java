package com.zykj.hihome.data;

import java.io.Serializable;

/**
 * 好友----friend
 * @author csh 2015-10-17
 */
public class Friend implements Serializable{
	
	private static final long serialVersionUID = 2420342366118172089L;
	private String id;//信息ID编号
	private String type;//0普通、1星标、2配偶
	private String fid;//好友ID编号
	private String nick;//好友姓名
	private String avatar;//好友头像
	private String sex;//好友性别
	private String age;//好友年龄
	private String sign;//好友签名
	private String state;
	
	private String category;//分类{"普通好友","星标好友","配偶"}

	private boolean isChecked;//是否选中
	
	public Friend(){
	}
	public Friend(String nick) {
		this.nick = nick;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
