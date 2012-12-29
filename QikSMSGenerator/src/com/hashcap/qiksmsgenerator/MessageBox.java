/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStartListener;
import com.hashcap.qiksmsgenerator.ui.DataSettingsActivity;
import com.hashcap.qiksmsgenerator.ui.MainActivity;

public class MessageBox {

	public static final int CONVERSATION = 0;
	public static final int INBOX = 1;
	public static final int SENT = 2;
	public static final int DRAFT = 3;
	public static final int OUTBOX = 4;
	public static final int FAILED = 5;

	private Context mContext;

	private CheckBox mCheckBox;
	private EditText mEditText;
	private ImageView mImageViewSettings;
	private int mTag;
	private DataSettings mDataSettings;
	private boolean mEnabled;
	private SharedPreferences mPreferences;

	public MessageBox(Context context, CheckBox checkBox, EditText editText,
			ImageView imageView, int tag) {
		mContext = context;
		mCheckBox = checkBox;
		mEditText = editText;
		mImageViewSettings = imageView;
		mTag = tag;
		mDataSettings = new DataSettings(mContext, TagName.getName(mTag));
		mPreferences = mContext.getSharedPreferences("GEN_DATA",
				Context.MODE_PRIVATE);

		mEditText.setFilters(new InputFilter[] { new InputFilterMinMax(
				mContext, "0", "5000") });

		boolean checked = mPreferences.getBoolean(TagName.getName(mTag), false);
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
			intent.putExtra("TAG", mTag);
			String messages = mEditText.getText().toString();
			if (TextUtils.isEmpty(messages)) {
				mDataSettings.setMessages(0);
			} else {
				mDataSettings.setMessages(Integer.parseInt(messages));
			}
			intent.putExtra("data", mDataSettings);
			if (mContext instanceof MainActivity) {
				((MainActivity) mContext).startActivityForResult(intent, mTag);
			}
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
		save();
	}

	private void save() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(TagName.getName(mTag), mCheckBox.isChecked());
		editor.apply();
		mDataSettings.save(mContext, TagName.getName(mTag));
	}

	public void setSettingsData(DataSettings dataSettings) {
		mDataSettings = dataSettings;
	}

	@Override
	public String toString() {

		return TagName.getName(mTag) + " MessageBox";
	}

	public Generator getGenerator(
			OnGeneratorStartListener onGeneratorStartListener) {
		String messages = mEditText.getText().toString();
		if (TextUtils.isEmpty(messages)) {
			mDataSettings.setMessages(0);
		} else {
			mDataSettings.setMessages(Integer.parseInt(messages));
		}
		Generator generator = new Generator(mContext, mTag);
		generator.setOnGeneratorStartListener(onGeneratorStartListener);
		generator.setDataSettings(mDataSettings);
		return generator;
	}

	public boolean isChecked() {
		return mCheckBox.isChecked();
	}

}
