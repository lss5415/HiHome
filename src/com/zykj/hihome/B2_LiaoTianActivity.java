package com.zykj.hihome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.CircularImage;
import com.zykj.hihome.view.BadgeView;

/**
 * @author LSS 2015年9月29日 上午8:55:57
 */
public class B2_LiaoTianActivity extends BaseActivity implements OnItemClickListener{

	private CircularImage rv_me_avatar;
//	private EditText firend_search;
	private ListView chat_list;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b2_liaotian);
		
		initView();
		requestData();
	}
	
	private void initView() {
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);//头像
//		firend_search = (EditText)findViewById(R.id.firend_search);//搜索
		
		chat_list = (ListView)findViewById(R.id.chat_list);//搜索
		chat_list.setDividerHeight(0);
		chat_list.setOnItemClickListener(this);
		setListener(rv_me_avatar);
	}

	private void requestData() {
		List<Chat> list = new ArrayList<Chat>();
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		list.add(new Chat("唐嫣", "下午15:30", "您有消息未读请查看"));
		chat_list.setAdapter(new CommonAdapter<Chat>(this, R.layout.ui_b2_item_chat, list) {
			@Override
			public void convert(ViewHolder holder, Chat chat) {
				holder.setText(R.id.chat_name, chat.name)
						.setText(R.id.chat_time, chat.time)
						.setText(R.id.chat_content, chat.content);
	            BadgeView badge = new BadgeView(B2_LiaoTianActivity.this, holder.getView(R.id.chat_content));
                badge.setBadgeBackgroundColor(Color.parseColor("#FF0000"));
                badge.setTextColor(Color.WHITE);
                if (holder.getPosition() % 2 == 0) {
                	badge.setText((new Random().nextInt(10)+10)+"");
                	badge.show();
                } else {
                	badge.hide();
                }
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View currentView, int position, long id) {
		
	}
	
	class Chat{
		String name;
		String time;
		String content;
		Chat(String name, String time, String content){
			this.name = name;
			this.time = time;
			this.content = content;
		}
	}
}
