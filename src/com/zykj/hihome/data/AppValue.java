package com.zykj.hihome.data;

/**
 * 应用的数据
 * 
 * @author bin
 * 
 */
public class AppValue {
	public static Shop shop_info = null;

	/**
	 * 存储的sharePerfence
	 */
	public static final String config = "config";
	/**
	 * 用户名
	 */
	public static final String USER_ID = "user_id";
	/**
	 * 密码
	 */
	public static final String USER_PASS = "user_pass";
	/**
	 * 当前的是否已经进行过指引
	 */
	public static final String IS_INTRO = "is_intro";
	
	/**
	 * 当前应用中存储的版本号
	 */
	public static final String VERSION = "version";
	/**
	 * 当前帐号是否已经登录的标识
	 */
	public static boolean is_login = false;

	/**
	 * 当前应用的存放目录
	 */
	public static final String FILE_DIR = "heyi_dir";
	
	/**
	 * mob短信验证APP Key
	 * 
	 */
	public static final String APPKey_mob = "9bbbe6ec5a25";
	/**
	 * mob短信验证SECRE
	 */
	public static final String APP_SECRE = "834e08d5e03c143ab86af5ea85c5c249";
	/**
	 * 客户端类型
	 */
	public static final String CLIENT = "android";

}
