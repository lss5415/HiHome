package com.zykj.hihome;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zykj.hihome.view.MyCommonTitle;

/**
 * @author LSS 2015年9月29日 上午8:55:57
 */
public class B2_01_LiaoTianDetailActivity extends FragmentActivity {
	/**
	* 目标 Id
	*/
	private String mTargetId;
	
	/**
	 * 会话类型
	 */
    private Conversation.ConversationType mConversationType;
	private MyCommonTitle myCommonTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_b2_liaotian_detail);
        Intent intent = getIntent();

        getIntentDate(intent);
    }
	
    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("会话");

        mTargetId = intent.getData().getQueryParameter("targetId");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }
}
