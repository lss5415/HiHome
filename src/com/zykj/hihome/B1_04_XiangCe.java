package com.zykj.hihome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.base.BaseApp;
import com.zykj.hihome.data.XiangCeLieBiao;
import com.zykj.hihome.data.ZuiJinXiangPian;
import com.zykj.hihome.menuListView.SwipeMenu;
import com.zykj.hihome.menuListView.SwipeMenuCreator;
import com.zykj.hihome.menuListView.SwipeMenuItem;
import com.zykj.hihome.menuListView.SwipeMenuListView;
import com.zykj.hihome.menuListView.SwipeMenuListView.OnMenuItemClickListener;
import com.zykj.hihome.utils.HttpErrorHandler;
import com.zykj.hihome.utils.HttpUtils;
import com.zykj.hihome.utils.StringUtil;
import com.zykj.hihome.utils.UrlContants;
import com.zykj.hihome.view.MyCommonTitle;
import com.zykj.hihome.view.UIDialog;
import com.zykj.hihome.view.XListView;
import com.zykj.hihome.view.XListView.IXListViewListener;

/**
 * @author LSS 2015年10月19日 下午5:15:28
 * 
 */
public class B1_04_XiangCe extends BaseActivity implements IXListViewListener,
		OnItemClickListener ,OnMenuItemClickListener, com.zykj.hihome.menuListView.SwipeMenuListView.IXListViewListener{
	private MyCommonTitle myCommonTitle;
	private ImageView img_add_photo;
	private TextView tv_zuijinxiangpian;// 最近相片
	private TextView tv_xiangpianliebiao;// 相册列表
	private LinearLayout ll_xclb;// 相册列表
	private LinearLayout ll_zjxp;// 最近相片
	private LinearLayout ly_top;
	private RelativeLayout rl_main;
	private LinearLayout ly_add_pic, ly_add_camera, ly_add_photo;
	private XListView lv_zjxp;// 最近相片列表
	private SwipeMenuListView lv_xclb;
	private static int PERPAGE1 = 5;// perpage默认每页显示10条信息
	private int nowpage1 = 1;// 当前显示的页面
	private static int PERPAGE = 5;// perpage默认每页显示10条信息
	private int nowpage = 1;// 当前显示的页面
	private CommonAdapter<ZuiJinXiangPian> adapter;
	private CommonAdapter<XiangCeLieBiao> adapter1;
	private List<ZuiJinXiangPian> listzjxp = new ArrayList<ZuiJinXiangPian>();
	private List<XiangCeLieBiao> listxclb = new ArrayList<XiangCeLieBiao>();
	private Handler mHandler = new Handler();// 异步加载或刷新
	private int statexc = 0;// 0是最近相片，1是相册列表
	private String timeString, imgs, imgsrc1, fid;// 上传头像的字段
	private File file;
/**
 * 侧滑删除
 */
	private SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem openItem = new SwipeMenuItem(B1_04_XiangCe.this);
			// set item background
			openItem.setBackground(new ColorDrawable(Color
					.rgb(0xFF, 0x00, 0x00)));
			// set item width
			openItem.setWidth(dp2px(90));
			// set item title
			openItem.setTitle("删除");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);
		}
	};
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b1_04_xiangce);
		fid = getIntent().getStringExtra("fid");//相册主人的Id
		// if (statexc>0) {
		adapter1 = new CommonAdapter<XiangCeLieBiao>(B1_04_XiangCe.this,
				R.layout.ui_xiangceliebiao, listxclb) {
			@Override
			public void convert(ViewHolder holder, XiangCeLieBiao xclb) {
				holder.setText(R.id.tv_xcm,
						xclb.getTitle() + "(" + xclb.getPhotos() + ")")
						.setImageUrl(R.id.im_xc,
								StringUtil.toString(HttpUtils.IMAGE_URL + xclb.getImgsrc(),"http://"))
						.setText(R.id.tv_date, xclb.getAddtime());
			}
		};
		// }else {
		adapter = new CommonAdapter<ZuiJinXiangPian>(B1_04_XiangCe.this,
				R.layout.ui_zuijinxiangpian, listzjxp) {
			@Override
			public void convert(ViewHolder holder, ZuiJinXiangPian zjxp) {
				holder.setText(R.id.tv_time, zjxp.getAddtime().substring(0, 11))
						.setImageUrl(R.id.im_xp,
								HttpUtils.IMAGE_URL + zjxp.getImgsrc())
						.setText(R.id.tv_miaoshu, zjxp.getIntro());
			}
		};
		// }
		initView();
		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setLisener(null, this);
		myCommonTitle.setTitle("相册");
		img_add_photo = (ImageView) findViewById(R.id.aci_shared_btn);
		img_add_photo.setImageResource(R.drawable.ic_publish_task);
		ly_top = (LinearLayout) findViewById(R.id.ly_top);// 点击img_add_photo时候ly_top可见
		ly_add_pic = (LinearLayout) findViewById(R.id.ly_add_picture);
		ly_add_camera = (LinearLayout) findViewById(R.id.ly_add_camera);
		ly_add_photo = (LinearLayout) findViewById(R.id.ly_add_photo);
		rl_main = (RelativeLayout) findViewById(R.id.rl_main);
		tv_zuijinxiangpian = (TextView) findViewById(R.id.tv_zuijinxiangpian);
		tv_xiangpianliebiao = (TextView) findViewById(R.id.tv_xiangpianliebiao);
		ll_xclb = (LinearLayout) findViewById(R.id.ll_xclb);
		ll_zjxp = (LinearLayout) findViewById(R.id.ll_zjxp);

		lv_zjxp = (XListView) findViewById(R.id.lv_zjxp);
		lv_xclb = (SwipeMenuListView) findViewById(R.id.lv_xclb);
		
		lv_xclb.setDividerHeight(0);
		lv_xclb.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉点击item的背景色
		lv_xclb.setOnItemClickListener(this);
		lv_xclb.setAdapter(adapter1);
		lv_xclb.setPullLoadEnable(true);
		lv_xclb.setPullRefreshEnable(true);
		lv_xclb.setXListViewListener(this);
		lv_zjxp.setDividerHeight(0);
		lv_zjxp.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉点击item的背景色
		lv_zjxp.setXListViewListener(this);
		lv_zjxp.setOnItemClickListener(this);
		lv_zjxp.setAdapter(adapter);
		lv_zjxp.setPullLoadEnable(true);
		lv_zjxp.setPullRefreshEnable(true);
		
		lv_xclb.setMenuCreator(creator);
		lv_xclb.setOnMenuItemClickListener((OnMenuItemClickListener) this);
		setListener(tv_zuijinxiangpian, tv_xiangpianliebiao);
	}

	private void requestData() {
		if (statexc > 0) {
			/** 相册列表 */
			RequestParams params = new RequestParams();
			params.put("fid", fid);//fid为传过来的好友id
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("nowpage", nowpage1);
			params.put("perpage", PERPAGE1);
			HttpUtils.getXiangCeList(res_xiangceList, params);
		} else {
			/** 最近相片 */
			RequestParams params = new RequestParams();
			params.put("uid", fid);//fid为传过来的登录用户的id
			params.put("nowpage", nowpage);
			params.put("perpage", PERPAGE);
			HttpUtils.geZuiJinXiangPian(res_zuijinxiangpianList, params);
		}
	}

	/**
	 * 最近相片
	 */
	private AsyncHttpResponseHandler res_zuijinxiangpianList = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			JSONObject jsonObject = json.getJSONObject(UrlContants.jsonData);
			String strArray = jsonObject.getString("list");
			List<ZuiJinXiangPian> list = JSONArray.parseArray(strArray,
					ZuiJinXiangPian.class);
			if (nowpage == 1) {
				listzjxp.clear();
			}
			listzjxp.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * 相册列表
	 */
	private AsyncHttpResponseHandler res_xiangceList = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			JSONObject jsonObject = json.getJSONObject(UrlContants.jsonData);
			String strArray = jsonObject.getString("list");
			List<XiangCeLieBiao> list = JSONArray.parseArray(strArray,
					XiangCeLieBiao.class);
			if (nowpage1 == 1) {
				listxclb.clear();
			}
			listxclb.addAll(list);
			adapter1.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_zuijinxiangpian:// 最近相片
			statexc = 0;
			tv_zuijinxiangpian.setTextColor(getResources().getColor(
					R.color.theme_color));
			tv_xiangpianliebiao.setTextColor(Color.BLACK);
			ll_zjxp.setVisibility(View.VISIBLE);
			ll_xclb.setVisibility(View.GONE);
			requestData();
			break;
		case R.id.tv_xiangpianliebiao:
			statexc = 1;
			tv_zuijinxiangpian.setTextColor(Color.BLACK);
			tv_xiangpianliebiao.setTextColor(getResources().getColor(
					R.color.theme_color));
			ll_zjxp.setVisibility(View.GONE);
			ll_xclb.setVisibility(View.VISIBLE);
			requestData();
			break;
		case R.id.aci_shared_btn:
			UIDialog.ForHorizontaloThreeBtn(this, this);
			// ly_top.setVisibility(View.VISIBLE);
			// rl_main.setVisibility(View.GONE);
			break;
		case R.id.ly_add_picture:
			UIDialog.closeDialog();
			startActivityForResult(new Intent(B1_04_XiangCe.this,
					B1_04_01_AddPictureActivity.class), 55);
			// finish();
			break;
		case R.id.ly_add_camera:
			/* 拍照 */
			UIDialog.closeDialog();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
			timeString = dateFormat.format(date);
			createSDCardDir();
			Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			shootIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory() + "/DCIM/Camera",
							timeString + ".jpg")));
			startActivityForResult(shootIntent, 2);
			break;
		case R.id.ly_add_photo:
			UIDialog.closeDialog();
			startActivityForResult(new Intent(B1_04_XiangCe.this,
					B1_04_03_AddPhotoActivity.class), 55);

			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			/* 如果是直接从相册获取 */
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG).show();
			}
			break;
		case 2:
			/* 如果是调用相机拍照，图片设置名字和路径 */
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case 3:
			/* 取得裁剪后的图片 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 55:
			/* 创建相册后刷新列表 */
			requestData();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();

			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			// Drawable drawable = new BitmapDrawable(photo);
			/* 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似 */
			savaBitmap(photo);
			// avatar_head_image.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 将剪切后的图片保存到本地图片上！
	 */
	public void savaBitmap(Bitmap bitmap) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
		String cutnameString = dateFormat.format(date);
		String filename = Environment.getExternalStorageDirectory().getPath()
				+ "/" + cutnameString + ".jpg";
		file = new File(filename);
		FileOutputStream fOut = null;
		try {
			file.createNewFile();
			fOut = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			RequestParams params = new RequestParams();
			params.put("imgsrc[]", file);
			HttpUtils.upLoad(new HttpErrorHandler() {

				@Override
				public void onRecevieSuccess(JSONObject json) {
					imgsrc1 = json.getJSONArray(UrlContants.jsonData).getJSONObject(0).getString("imgsrc");
					startActivityForResult(new Intent(B1_04_XiangCe.this,B1_04_01_AddPictureActivity.class).putExtra("picture",imgsrc1), 55);
				}
			}, params);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 详情
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (statexc == 1) {
			XiangCeLieBiao xiangce = listxclb.get(position - 1);
			startActivity(new Intent(B1_04_XiangCe.this,
					B1_04_05_XiangCeDetailsActivity.class).putExtra("photo",
					xiangce));
		}
	}
	/**
	 *相册 item侧滑删除
	 */
	@Override
	public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
		if(statexc==1){
			XiangCeLieBiao xiangce = listxclb.get(position);
			RequestParams params=new RequestParams();
			params.put("id", xiangce.getId());
			HttpUtils.deleteXiangCe(new HttpErrorHandler() {
				
				@Override
				public void onRecevieSuccess(JSONObject json) {
					listxclb.remove(position);
					adapter1.notifyDataSetChanged();
				}
			}, params);
			
			
		}
		return false;
	}
	@Override
	public void onRefresh() {
		/** 下拉刷新 重建 */
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (statexc > 0) {
					nowpage1 = 1;
					requestData();
					onLoad();
				} else {
					nowpage = 1;
					requestData();
					onLoad();
				}
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		/** 上拉加载分页 */
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (statexc > 0) {
					nowpage1 += 1;
					requestData();
					onLoad();
				} else {
					nowpage += 1;
					requestData();
					onLoad();
				}
			}
		}, 1000);
	}

	private void onLoad() {
		if (statexc > 0) {
			lv_xclb.stopRefresh();
			lv_xclb.stopLoadMore();
			lv_xclb.setRefreshTime("刚刚");
		} else {
			lv_zjxp.stopRefresh();
			lv_zjxp.stopLoadMore();
			lv_zjxp.setRefreshTime("刚刚");
		}
	}
}