/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;
import com.hashcap.qiksmsgenerator.support.ConversationsGenerator;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;
import com.hashcap.qiksmsgenerator.ui.DataSettingsActivity;
import com.hashcap.qiksmsgenerator.ui.MainActivity;

public class Conversations {
	private Context mContext;
	private boolean mEnabled;
	private EditText mEditTextConversations;
	private EditText mEditTextMessages;
	private ImageView mImageViewSettings;
	private int mTag;
	private ConversationsDataSettings mDataSettings;

	public Conversations(Context context, EditText converstions,
			EditText messages, ImageView settings) {
		mContext = context;
		mTag = TagIndex.CONVERSATION;
		mEditTextConversations = converstions;
		mEditTextMessages = messages;

		mEditTextConversations
				.setFilters(new InputFilter[] { new InputFilterMinMax(mContext,
						"0", "500") });
		mEditTextMessages.setFilters(new InputFilter[] { new InputFilterMinMax(
				mContext, "0", "5000") });

		mImageViewSettings = settings;
		mDataSettings = new ConversationsDataSettings(mContext,
				TagName.getName(mTag));
		mImageViewSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DataSettingsActivity.class);
				intent.putExtra("TAG", mTag);
				
				String conversations = mEditTextConversations.getText()
						.toString();
				String messages = mEditTextMessages.getText().toString();
				if (TextUtils.isEmpty(conversations)) {
					mDataSettings.setConversations(0);
				} else {
					mDataSettings.setConversations(Integer
							.parseInt(conversations));
				}
				if (TextUtils.isEmpty(messages)) {
					mDataSettings.setMessages(0);
				} else {
					mDataSettings.setMessages(Integer.parseInt(messages));
				}

				intent.putExtra("data", mDataSettings);
				if (mContext instanceof MainActivity) {
					((MainActivity) mContext).startActivityForResult(intent,
							mTag);
				}
			}
		});
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
		if (mEditTextConversations != null) {
			mEditTextConversations.setEnabled(mEnabled);
		}
		if (mEditTextMessages != null) {
			mEditTextMessages.setEnabled(mEnabled);
		}
		if (mImageViewSettings != null) {
			mImageViewSettings.setEnabled(mEnabled);
		}
	}

	public void destroy() {
		save();
	}

	private void save() {
		mDataSettings.save(mContext, TagName.getName(mTag));
	}

	public void setSettingsData(DataSettings dataSettings) {
		mDataSettings = (ConversationsDataSettings) dataSettings;
	}

	public Generator getGenerator() {
		ConversationsGenerator generator = new ConversationsGenerator(
				TagIndex.CONVERSATION);
		generator.setDataSettings(mDataSettings);

		return generator;
	}

}
