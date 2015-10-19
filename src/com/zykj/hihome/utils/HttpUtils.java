package com.zykj.hihome.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * @author LSS 2015年9月29日 上午9:58:40
 *
 */
public class HttpUtils {
	public static final String base_url = "http://hihome.zhongyangjituan.com/api.php?";
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
	 * @param userId 用户Id
	 */
	public static void getFriendsList(AsyncHttpResponseHandler res, String userId) {
		client.get(base_url + "c=friend&a=getList&uid=" + userId, res);/*&state=1*/
	}

	/**
	 * 2 添加好友
	 * @param params参数(fid 要添加好友的ID编号,uid用户ID编号,intro 备注信息)
	 */
	public static void addFriend(AsyncHttpResponseHandler res, RequestParams params) {
		client.post(base_url + "c=friend&a=addFriend", params, res);
	}

	/**
	 * 3 搜索联系人
	 * @param params参数(keys 搜索关键词,uid(非必须),nowpage(非必须),perpage(非必须))
	 */
	public static void getSearchUser(AsyncHttpResponseHandler res, RequestParams params) {
		client.post(base_url + "c=user&a=getSearchUser", params, res);
	}
}
