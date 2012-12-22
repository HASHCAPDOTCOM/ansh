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

import com.hashcap.qiksmsgenerator.GeneratorUtils.SettingsTag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

public class MessageBox {
	private Context mContext;

	private CheckBox mCheckBox;
	private EditText mEditText;
	private ImageView mImageViewSettings;
	private SettingsTag mSettingsTag;
	private MessageData mMessageData;
	private boolean mEnabled;
	private SharedPreferences mPreferences;

	public MessageBox(Context context, CheckBox checkBox, EditText editText,
			ImageView imageView, GeneratorUtils.SettingsTag tag) {
		mContext = context;
		mCheckBox = checkBox;
		mEditText = editText;
		mImageViewSettings = imageView;
		mSettingsTag = tag;
		mPreferences = mContext.getSharedPreferences("GEN_DATA",
				Context.MODE_PRIVATE);
		boolean checked = mPreferences.getBoolean(mSettingsTag.toString(),
				false);
		if (mCheckBox != null) {
			mCheckBox.setChecked(checked);
			if (mEditText != null) {
				mEditText.setEnabled(mCheckBox.isChecked());
				mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
			}
			if (mImageViewSettings != null) {
				mImageViewSettings.setEnabled(mCheckBox.isChecked());
				mImageViewSettings
						.setOnClickListener(mImageViewOnClickListener);
			}
		}
	}

	private OnClickListener mImageViewOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, DataSettingsActivity.class);
			intent.putExtra("TAG", mSettingsTag);
			mContext.startActivity(intent);
		}
	};

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			mEditText.setEnabled(isChecked);
			mImageViewSettings.setEnabled(isChecked);
		}
	};

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
		if (mCheckBox != null) {
			mCheckBox.setEnabled(mEnabled);

			if (mEditText != null) {
				mEditText.setEnabled(mCheckBox.isChecked() && mEnabled);
			}
			if (mImageViewSettings != null) {
				mImageViewSettings
						.setEnabled(mCheckBox.isChecked() && mEnabled);
			}
		}
	}

	public void destroy() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(mSettingsTag.toString(), mCheckBox.isChecked());
		editor.apply();
	}
}
