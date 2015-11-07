package com.zykj.hihome;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zykj.hihome.base.BaseActivity;
import com.zykj.hihome.utils.CircularImage;

/**
 * @author LSS 2015年9月29日 上午8:55:57
 */
public class B2_LiaoTianActivity extends BaseActivity {
	private Button text1,button1,button2;
//	private String token = "RVKmhSh9cjE+S8xHITqi2/2FrtFjveZncyRDTlKNAWQW+/Uh8E8AkcKhhFFyjd4yKMVMLWlf/q69JzvOEBjKOXiI8lwTftVs";
	private String token;
	private CircularImage rv_me_avatar;
//	private EditText firend_search;
//	private ListView chat_list;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b2_liaotian);
		String uid = getSharedPreferenceValue("uid");
		initView();
//		requestData();
	}
	
	private void initView() {
		rv_me_avatar = (CircularImage)findViewById(R.id.rv_me_avatar);//头像
		text1 = (Button)findViewById(R.id.text1);
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
//		firend_search = (EditText)findViewById(R.id.firend_search);//搜索
		
//		chat_list = (ListView)findViewById(R.id.chat_list);//搜索
//		chat_list.setDividerHeight(0);
//		chat_list.setOnItemClickListener(this);
		setListener(rv_me_avatar,text1,button1,button2);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.text1:
			RongIM.connect(token, new ConnectCallback() {

				@Override
				public void onError(ErrorCode arg0) {
					Toast.makeText(B2_LiaoTianActivity.this, "connect onError", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(String arg0) {
					Toast.makeText(B2_LiaoTianActivity.this, "connect onSuccess", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case R.id.button1:
			RongIM.getInstance().startConversationList(B2_LiaoTianActivity.this);
			break;
		case R.id.button2:
			RongIM.getInstance().startConversation(B2_LiaoTianActivity.this, Conversation.ConversationType.PRIVATE, "5", "会话");
			break;
		default:
			break;
		}
	}

	/*private void requestData() {
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
	}*/

	/*@Override
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
	}*/
}
