/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hashcap.qiksmsgenerator.GeneratorUtils.FolderName;
import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStartListener;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStatusChangedListener;
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
		mDataSettings = new DataSettings(mContext, FolderName.getName(mTag));

		mPreferences = mContext.getSharedPreferences("GEN_DATA",
				Context.MODE_PRIVATE);

		mEditText.setText(mDataSettings.getMessages() > 0 ? mDataSettings
				.getMessages() + "" : "");

		mEditText.setFilters(new InputFilter[] { new InputFilterMinMax(
				mContext, "0", "500") });

		mEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager inputMethodManager = (InputMethodManager) mContext
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							mEditText.getWindowToken(), 0);
				}
				return false;
			}
		});
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				String messages = s.toString();
				if (TextUtils.isEmpty(messages)) {
					mDataSettings.setMessages(0);
				} else {
					mDataSettings.setMessages(Integer.parseInt(messages));
				}
				final OnGeneratorStatusChangedListener activeListener = Generator
						.getGeneratorActiveListener();
				if (activeListener != null) {
					mEditText.getHandler().post(new Runnable() {
						@Override
						public void run() {
							activeListener.onStartStatusChanged();
						}
					});

				}

			}
		});
		boolean checked = mPreferences.getBoolean(FolderName.getName(mTag), false);
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
			intent.putExtra("tag", mTag);
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
			final OnGeneratorStatusChangedListener activeListener = Generator
					.getGeneratorActiveListener();
			if (activeListener != null) {
				buttonView.getHandler().post(new Runnable() {
					@Override
					public void run() {
						activeListener.onStartStatusChanged();
					}
				});

			}
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
		editor.putBoolean(FolderName.getName(mTag), mCheckBox.isChecked());
		editor.apply();
		mDataSettings.save(mContext, FolderName.getName(mTag));
	}

	public void setSettingsData(DataSettings dataSettings) {
		mDataSettings = dataSettings;
	}

	@Override
	public String toString() {

		return " MessageBox = " + FolderName.getName(mTag)
				+ " , mDataSettings = [ " + mDataSettings + " ]";
	}

	public Generator getGenerator(
			OnGeneratorStartListener onGeneratorStartListener) {
		Generator generator = new Generator(mContext, mTag);
		generator.setOnGeneratorStartListener(onGeneratorStartListener);
		generator.setDataSettings(mDataSettings);
		return generator;
	}

	public boolean isChecked() {
		return mCheckBox.isChecked();
	}

	public int getMessages() {
		return mDataSettings.getMessages();
	}

}
