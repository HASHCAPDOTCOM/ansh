/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;
import com.hashcap.qiksmsgenerator.support.ConversationsGenerator;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStatusChangedListener;
import com.hashcap.qiksmsgenerator.ui.DataSettingsActivity;
import com.hashcap.qiksmsgenerator.ui.MainActivity;

public class Conversations {
	private static final String TAG = "Conversations";
	private static final boolean DEBUG = true;
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

		mEditTextConversations
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager inputMethodManager = (InputMethodManager) mContext
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(
									mEditTextConversations.getWindowToken(), 0);
						}
						return false;
					}
				});

		mEditTextMessages
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager inputMethodManager = (InputMethodManager) mContext
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(
									mEditTextMessages.getWindowToken(), 0);
						}
						return false;
					}
				});
		mEditTextConversations.addTextChangedListener(new TextWatcher() {

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
				String conversations = s.toString();
				if (TextUtils.isEmpty(conversations)) {
					mDataSettings.setConversations(0);
				} else {
					mDataSettings.setConversations(Integer
							.parseInt(conversations));
				}
				final OnGeneratorStatusChangedListener activeListener = Generator
						.getGeneratorActiveListener();
				if (activeListener != null) {
					mEditTextConversations.getHandler().post(new Runnable() {
						@Override
						public void run() {
							activeListener.onStartStatusChanged();
						}
					});

				}
			}
		});
		mEditTextMessages.addTextChangedListener(new TextWatcher() {

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
					mEditTextMessages.getHandler().post(new Runnable() {
						@Override
						public void run() {
							activeListener.onStartStatusChanged();
						}
					});

				}
			}
		});
		mImageViewSettings = settings;
		mDataSettings = new ConversationsDataSettings(mContext,
				TagName.getName(mTag));

		mEditTextMessages
				.setText(mDataSettings.getMessages() > 0 ? mDataSettings
						.getMessages() + "" : "");

		mEditTextConversations
				.setText(mDataSettings.getConversations() > 0 ? mDataSettings
						.getConversations() + "" : "");

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
		ConversationsGenerator generator = new ConversationsGenerator(mContext,
				TagIndex.CONVERSATION);
		generator.setDataSettings(mDataSettings);

		return generator;
	}

	public boolean canActivateGenerator() {
		if (mDataSettings.getConversations() * mDataSettings.getMessages() > 0) {
			return true;
		}
		return false;
	}

}
