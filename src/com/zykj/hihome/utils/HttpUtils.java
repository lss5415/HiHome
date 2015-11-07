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
	 *  获取好友列表
	 * 
	 * @param userId
	 *            用户Id
	 */
	public static void getFriendsList(AsyncHttpResponseHandler res,
			String userId) {
		client.get(base_url + "c=friend&a=getListGroup&uid=" + userId, res);
	}

	/**
	 *  添加好友
	 * 
	 * @param params参数
	 *            (fid 要添加好友的ID编号,uid用户ID编号,intro 备注信息)
	 */
	public static void addFriend(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=friend&a=addFriend", params, res);
	}

	/**
	 *  搜索联系人
	 * 
	 * @param params参数
	 *            (keys 搜索关键词,uid(非必须),nowpage(非必须),perpage(非必须))
	 */
	public static void getSearchUser(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=user&a=getSearchUser", params, res);
	}

	/**
	 * 登陆
	 * 
	 * @param params参数
	 */
	public static void login(AsyncHttpResponseHandler res, RequestParams params) {
		client.post(base_url + "c=user&a=login", params, res);
	}

	/**
	 *  注册
	 * 
	 * @param params参数
	 */
	public static void register(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=user&a=reg", params, res);
	}

	/**
	 *  创建纪念日
	 * 
	 * @param params参数
	 */
	public static void addAnniversary(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=addInfo", params, res);
	}

	/**
	 *  获取纪念日列表
	 * 
	 * @param params参数
	 */
	public static void getAnnversaryList(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=getList", params, res);
	}

	/**
	 *  获取纪念日详情
	 * 
	 * @param params参数
	 */
	public static void getAnnversaryInfo(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=getInfo", params, res);
	}

	/**
	 *  创建任务
	 * 
	 * @param params
	 */
	public static void addTask(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=addInfo", params, res);
	}

	/**
	 *  获取我的任务列表
	 * 
	 * @param params参数uid
	 *            ----用户id
	 */
	public static void getMyTasks(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=getMyTask", params, res);
	}

	/**
	 *  获取我发布的任务列表
	 * 
	 * @param params参数uid
	 *            ----用户id
	 */
	public static void getPublishTaskList(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=getList", params, res);
	}

	/**
	 * 获取任务详情
	 * 
	 * @param params参数id
	 *            ----任务id
	 */
	public static void getTasksInfo(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=getInfo", params, res);
	}
	/**
	 * 更新任务详情
	 * 
	 * @param params参数id
	 *            ----任务id
	 */
	public static void upDateTasksInfo(AsyncHttpResponseHandler res,
			RequestParams params) {
		client.post(base_url + "c=task&a=editInfo", params, res);
	}

	/**
	 * 上传图片
	 * 
	 * @param params
	 */
	public static void upLoad(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=public&a=upload", params, res_upLoad);
	}

	/**
	 *  查看好友资料
	 * 
	 * @param params
	 */
	public static void getInfo(AsyncHttpResponseHandler res_upLoad, String uid) {
		client.get(base_url + "c=user&a=getInfo&id=" + uid, res_upLoad);
	}

	/**
	 *  查看好友资料
	 * 
	 * @param params
	 */
	public static void getApplyList(AsyncHttpResponseHandler res_upLoad,
			String uid) {
		client.get(base_url + "c=friend&a=getApplyList&uid=" + uid, res_upLoad);
	}

	/**
	 *  同意/拒绝好友申请
	 * 
	 * @param params
	 */
	public static void applyFriend(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=friend&a=applyFriend", params, res_upLoad);
	}

	/**
	 *  删除纪念日
	 * 
	 * @param params
	 */
	public static void delAnnversaryInfo(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=memorial&a=delInfo", params, res_upLoad);
	}

	/**
	 * 17 删除任务
	 * 
	 * @param params
	 */
	public static void delTaskInfo(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=task&a=delInfo", params, res_upLoad);
	}
	/**
	 *  更改任务状态
	 * 
	 * @param params
	 */
	public static void modTaskState(AsyncHttpResponseHandler res_upLoad,
			RequestParams params) {
		client.post(base_url + "c=task&a=applyTask", params, res_upLoad);
	}
	/**
	 *  标记好友关系
	 * 
	 * @param params
	 */
	public static void groupFriend(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=friend&a=groupFriend", params, res_upLoad);
	}
	/**
	 *  获取Token
	 * 
	 * @param params
	 */
	public static void getToken(AsyncHttpResponseHandler res_upLoad, String uid) {
		client.get(base_url + "c=chat&a=getToken&uid=" + uid, res_upLoad);
	}
	/**
	 *  匹配手机通讯录
	 * 
	 * @param params
	 */
	public static void mobFriend(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=friend&a=mobFriend", params, res_upLoad);
	}
	/**
	 * 忘记密码
	 */
	public static void resetPassWord(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=user&a=resetPassword", params, res_upLoad);
	}
	/**
	 * 获取任务接受状态
	 */
	public static void getTaskState(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=task&a=getTaskState", params, res_upLoad);
	}
	/**
	 * 发起聊天
	 */
	public static void getChat(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=chat&a=postChat", params, res_upLoad);
	}
	/**
	 * 创建相册
	 */
	public static void getAddXiangCe(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=album&a=addInfo", params, res_upLoad);
	}
	/**
	 * 删除相册
	 */
	public static void getDelXiangCe(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=album&a=delInfo", params, res_upLoad);
	}
	/**
	 * 获取相册列表
	 */
	public static void getXiangCeList(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=album&a=getList", params, res_upLoad);
	}
	/**
	 * 获取相册详情
	 */
	public static void getXiangCeInfo(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=album&a=getInfo", params, res_upLoad);
	}
	/**
	 * 删除相片
	 */
	public static void getDelXiangPian(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=photo&a=delInfo", params, res_upLoad);
	}
	/**
	 * 获取相片列表
	 */
	public static void getXiangPianInfo(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=photo&a=getList", params, res_upLoad);
	}
	/**
	 * 修改用户资料
	 */
	public static void getXiuGaiInfo(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=user&a=modInfo", params, res_upLoad);
	}
	/**
	 * 修改用户资料
	 */
	public static void geZuiJinXiangPian(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=photo&a=getLatelyList", params, res_upLoad);
	}
	/**
	 * 获取天气
	 */
	public static void getWeatherInfo(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.addHeader("apikey", "98381cf1c5996fa7c49c25bcac69a83c");
		client.post("http://apis.baidu.com/heweather/weather/free", params, res_upLoad);
	}
	/**
	 * 获取任务日历
	 */
	public static void getDateTaskList(AsyncHttpResponseHandler res_upLoad, String uid) {
		client.get(base_url + "c=task&a=getDateTaskList&uid=" + uid, res_upLoad);
	}
	/**
	 * 获取功能介绍(应用说明)
	 */
	public static void getAppInfo(AsyncHttpResponseHandler res_upLoad) {
		client.get(base_url + "c=article&a=getInfo", res_upLoad);
	}
	/**
	 * 帮助与反馈
	 */
	public static void addInfo(AsyncHttpResponseHandler res_upLoad, RequestParams params) {
		client.post(base_url + "c=gbook&a=addInfo", params, res_upLoad);
	}
	/**
	 * 获取新版本
	 */
	public static void getNew(AsyncHttpResponseHandler res_upLoad, String version) {
		client.get(base_url + "c=version&a=getInfo&version="+version, res_upLoad);
	}
	
}
