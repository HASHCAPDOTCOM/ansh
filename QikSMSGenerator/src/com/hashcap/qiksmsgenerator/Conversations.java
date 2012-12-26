/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;
import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;

public class Conversations {
	private Context mContext;
	private boolean mEnabled;
	private EditText mEditTextConversations;
	private EditText mEditTextMessages;
	private ImageView mImageViewSettings;
	private int mTag;
	private DataSettings mDataSettings;

	public Conversations(Context context, EditText converstions,
			EditText messages, ImageView settings) {
		mContext = context;
		mTag = TagIndex.CONVERSATION;
		mEditTextConversations = converstions;
		mEditTextMessages = messages;

		mEditTextConversations
				.setFilters(new InputFilter[] { new InputFilterMinMax(mContext,
						"0", "5000") });
		mEditTextMessages.setFilters(new InputFilter[] { new InputFilterMinMax(
				mContext, "0", "5000") });

		mImageViewSettings = settings;
		mDataSettings = new DataSettings(mContext, TagName.getName(mTag));
		mImageViewSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DataSettingsActivity.class);
				intent.putExtra("TAG", mTag);
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
		mDataSettings = dataSettings;
	}

}
