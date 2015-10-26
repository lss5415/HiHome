package com.zykj.hihome.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * @author LSS 2015年9月29日 上午9:58:40
 * 
 */
public class HttpUtils {
	public static final String base_url = "http://hihome.zhongyangjituan.com/api.php?";
	public static final String IMAGE_URL = "http://hihome.zhongyangjituan.com/Uploads/";
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	static {
		client.setTimeout(5000); // 设置链接超时，如果不设置，默认为15s
		client.setMaxRetriesAndTimeout(3, 5000);
		// client.setEnableRedirects(true);

	}

	public static void initClient(Context c) {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(c);
		client.setCookieStore(myCookieStore);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	/**
	 * 1 获取好友列表
	 * 
	 * @param userId
	 *            用户Id
	 */
	public static void getFriendsList(AsyncHttpResponseHandler res,
			String userId) {
		client.get(base_url + "c=friend&a=getList&uid=" + userId, res);/*
																		 * &state
																		 * =1
																		 */
	}

	/**
	 * 2 添加好友
	 * 
	 * @param params参数
	 *            (fid 要添加好友的ID编号,uid用户ID编号,intro 备注信息)
	 */
	public static void addFriend(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=friend&a=addFriend", params, res);
	}

	/**
	 * 3 搜索联系人
	 * 
	 * @param params参数
	 *            (keys 搜索关键词,uid(非必须),nowpage(非必须),perpage(非必须))
	 */
	public static void getSearchUser(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=user&a=getSearchUser", params, res);
	}

	/**
	 * 4登陆
	 * 
	 * @param params参数
	 */
	public static void login(AsyncHttpResponseHandler res, RequestParams params) {
		client.post(base_url + "c=user&a=login", params, res);
	}

	/**
	 * 5 注册
	 * 
	 * @param params参数
	 */
	public static void register(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=user&a=reg", params, res);
	}

	/**
	 * 6 创建纪念日
	 * 
	 * @param params参数
	 */
	public static void addAnniversary(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=addInfo", params, res);
	}

	/**
	 * 7 获取纪念日列表
	 * 
	 * @param params参数
	 */
	public static void getAnnversaryList(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=getList", params, res);
	}

	/**
	 * 8 获取纪念日详情
	 * 
	 * @param params参数
	 */
	public static void getAnnversaryInfo(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=getInfo", params, res);
	}
	/**
	 * 10 创建任务
	 * 
	 * @param params
	 */
	public static void addTask(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=addInfo", params, res);
	}
	/**
	 * 9 获取任务列表
	 * 
	 * @param params参数uid
	 *            ----用户id
	 */
	public static void getMyTasks(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=getMyTask", params, res);
	}

	/**
	 * 10获取任务详情
	 * 
	 * @param params参数id
	 *            ----任务id
	 */
	public static void getTasksInfo(
			JsonHttpResponseHandler jsonHttpResponseHandler,
			RequestParams params) {
		client.post(base_url + "c=task&a=getInfo", params,
				jsonHttpResponseHandler);
	}

	/**
	 * 11上传图片
	 * 
	 * @param params
	 */
	public static void upLoad(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=public&a=upload", params, res_upLoad);
	}


}
