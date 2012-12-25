/*
 * Copyright (C) 2008-2008 Hashcap Pvt. Ltd.
 * Copyright (C) 2006-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;

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
