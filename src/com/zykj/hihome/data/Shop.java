package com.zykj.hihome.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop implements Serializable {

	private String tbid;

	private String shopName;

	private Long typeid;

	private String address;

	private String phone;

	private String mobile;

	private Long lat;

	private Long lon;

	private String imei;

	private String username;

	private String pwd;

	private String fTime;

	private String lTime;

	private String parentid;

	private Byte recommend;

	private String versionname;

	private Byte isdistribution;

	private String nick;

	private String createtime;

	private String shopImg;

	private String sex;

	private String ldpi;

	private String mdpi;

	private List<Shop> children = new ArrayList<Shop>();

	public String getTbid() {
		return tbid;
	}

	public Shop setTbid(String tbid) {
		this.tbid = tbid;
		return this;
	}

	public String getShopName() {
		return shopName;
	}

	public Shop setShopName(String shopName) {
		this.shopName = shopName;
		return this;
	}

	public Long getTypeid() {
		return typeid;
	}

	public Shop setTypeid(Long typeid) {
		this.typeid = typeid;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Shop setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Shop setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public Shop setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public Long getLat() {
		return lat;
	}

	public Shop setLat(Long lat) {
		this.lat = lat;
		return this;
	}

	public Long getLon() {
		return lon;
	}

	public Shop setLon(Long lon) {
		this.lon = lon;
		return this;
	}

	public String getImei() {
		return imei;
	}

	public Shop setImei(String imei) {
		this.imei = imei;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public Shop setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPwd() {
		return pwd;
	}

	public Shop setPwd(String pwd) {
		this.pwd = pwd;
		return this;
	}

	public String getfTime() {
		return fTime;
	}

	public Shop setfTime(String fTime) {
		this.fTime = fTime;
		return this;
	}

	public String getlTime() {
		return lTime;
	}

	public Shop setlTime(String lTime) {
		this.lTime = lTime;
		return this;
	}

	public String getParentid() {
		return parentid;
	}

	public Shop setParentid(String parentid) {
		this.parentid = parentid;
		return this;
	}

	public Byte getRecommend() {
		return recommend;
	}

	public Shop setRecommend(Byte recommend) {
		this.recommend = recommend;
		return this;
	}

	public String getVersionname() {
		return versionname;

	}

	public Shop setVersionname(String versionname) {
		this.versionname = versionname;
		return this;
	}

	public Byte getIsdistribution() {
		return isdistribution;
	}

	public Shop setIsdistribution(Byte isdistribution) {
		this.isdistribution = isdistribution;
		return this;
	}

	public String getNick() {
		return nick;
	}

	public Shop setNick(String nick) {
		this.nick = nick;
		return this;
	}

	public String getCreatetime() {
		return createtime;
	}

	public Shop setCreatetime(String createtime) {
		this.createtime = createtime;
		return this;
	}

	public String getShopImg() {
		return shopImg;
	}

	public Shop setShopImg(String shopImg) {
		this.shopImg = shopImg;
		return this;
	}

	public String getSex() {
		return sex;
	}

	public Shop setSex(String sex) {
		this.sex = sex;
		return this;
	}

	public String getLdpi() {
		return ldpi;
	}

	public Shop setLdpi(String ldpi) {
		this.ldpi = ldpi;
		return this;
	}

	public String getMdpi() {
		return mdpi;
	}

	public Shop setMdpi(String mdpi) {
		this.mdpi = mdpi;
		return this;
	}

	public List<Shop> getChildren() {
		return children;
	}

	public void setChildren(List<Shop> children) {
		this.children = children;
	}

}