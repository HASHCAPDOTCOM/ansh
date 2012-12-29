/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hashcap.qiksmsgenerator.Conversations;
import com.hashcap.qiksmsgenerator.DataSettings;
import com.hashcap.qiksmsgenerator.GeneratorServeice;
import com.hashcap.qiksmsgenerator.GeneratorServeice.GeneratorBinder;
import com.hashcap.qiksmsgenerator.GeneratorUtils;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.MessageBox;
import com.hashcap.qiksmsgenerator.MessageBoxList;
import com.hashcap.qiksmsgenerator.R;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.MaxGeneratorException;
import com.hashcap.qiksmsgenerator.support.OnGeneratorProgressUpdateListener;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStartListener;

public class MainActivity extends Activity implements OnGeneratorStartListener {
	private static final String TAG = "MainActivity";
	private RadioButton mRadioButtonCoversations;
	private RadioButton mRadioButtonMessageBox;
	private SharedPreferences mPreferences;
	private MessageBoxList mMessageBoxList;
	private LinearLayout mLinearLayoutProgressBar;
	private ProgressBar mProgressBar;
	private TextView mTextViewProgress;

	private boolean mBound;
	private GeneratorServeice mGeneratorServeice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextViewProgress = (TextView) findViewById(R.id.textView_progress);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mLinearLayoutProgressBar = (LinearLayout) findViewById(R.id.linearLayout_progress);
		mLinearLayoutProgressBar.setVisibility(View.GONE);

		initViews();

		mPreferences = getSharedPreferences("GEN_DATA", Context.MODE_PRIVATE);
		mRadioButtonCoversations.setChecked(mPreferences.getBoolean(
				"conversations", false));
		Conversations conversations = (Conversations) mRadioButtonCoversations
				.getTag();
		if (conversations != null) {
			conversations.setEnabled(mRadioButtonCoversations.isChecked());
		}

		mRadioButtonMessageBox.setChecked(mPreferences.getBoolean(
				"message_box", false));
		MessageBoxList boxList = (MessageBoxList) mRadioButtonMessageBox
				.getTag();
		if (boxList != null) {
			boxList.setEnabled(mRadioButtonMessageBox.isChecked());
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onResume();
	}

	@Override
	protected void onStart() {
		Intent intent = new Intent(this, GeneratorServeice.class);
		startService(intent);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGeneratorServeice.unregisterGeneratorProgressUpdateListener();
		unbindService(mServiceConnection);
		if (mGeneratorServeice.getStatus()) {

		}
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			DataSettings dataSettings = (DataSettings) data.getExtras().get(
					"data");
			switch (requestCode) {
			case TagIndex.CONVERSATION:
				((Conversations) mRadioButtonCoversations.getTag())
						.setSettingsData(dataSettings);
				break;
			case TagIndex.INBOX:
			case TagIndex.SENT:
			case TagIndex.DRAFT:
			case TagIndex.OUTBOX:
			case TagIndex.FAILED:
				mMessageBoxList.setSettingsData(requestCode - 1, dataSettings);
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean("conversations", mRadioButtonCoversations.isChecked());
		editor.putBoolean("message_box", mRadioButtonMessageBox.isChecked());
		editor.apply();
		((Conversations) mRadioButtonCoversations.getTag()).destroy();
		((MessageBoxList) mRadioButtonMessageBox.getTag()).destroy();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_start: {
			if (mRadioButtonCoversations.isChecked()) {
				Conversations conversations = (Conversations) mRadioButtonCoversations
						.getTag();
				Generator generator = conversations.getGenerator();
				generator.setOnGeneratorStartListener(this);
				generator.start();
			}
			if (mRadioButtonMessageBox.isChecked()) {
				MessageBoxList messageBoxList = (MessageBoxList) mRadioButtonMessageBox
						.getTag();
				messageBoxList.ensureGenerator(this);
				messageBoxList.generate();
			}
			break;
		}
		case R.id.menu_stop: {
			if(mBound){
				mGeneratorServeice.cancel();
			}
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGeneratorStart(Generator generator) {
		if (mBound) {
			try {
				mGeneratorServeice.add(generator);
			} catch (MaxGeneratorException e) {
				Toast.makeText(
						this,
						"Maximum "
								+ Generator.MAX_GENERATOR
								+ " generator process can be execute at a time.",
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, "Error " + e);
			}
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			GeneratorBinder binder = (GeneratorBinder) service;
			mGeneratorServeice = binder.getService();
			mBound = true;
			mGeneratorServeice
					.registerGeneratorProgressUpdateListener(mOnGeneratorProgressUpdateListener);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	private void initViews() {
		mRadioButtonCoversations = (RadioButton) findViewById(R.id.radioButton_conversation_title);
		mRadioButtonMessageBox = (RadioButton) findViewById(R.id.radioButton_message_box_title);

		MessageBox messageBoxInbox = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_inbox),
				(EditText) findViewById(R.id.editText_inbox),
				(ImageView) findViewById(R.id.imageView_setting_inbox),
				GeneratorUtils.TagIndex.INBOX);

		MessageBox messageBoxSent = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_sent),
				(EditText) findViewById(R.id.editText_sent),
				(ImageView) findViewById(R.id.imageView_setting_sent),
				GeneratorUtils.TagIndex.SENT);

		MessageBox messageBoxDraft = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_draft),
				(EditText) findViewById(R.id.editText_draft),
				(ImageView) findViewById(R.id.imageView_setting_draft),
				GeneratorUtils.TagIndex.DRAFT);

		MessageBox messageBoxOutbox = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_outbox),
				(EditText) findViewById(R.id.editText_outbox),
				(ImageView) findViewById(R.id.imageView_setting_outbox),
				GeneratorUtils.TagIndex.OUTBOX);

		MessageBox messageBoxFailed = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_failed),
				(EditText) findViewById(R.id.editText_failed),
				(ImageView) findViewById(R.id.imageView_setting_failed),
				GeneratorUtils.TagIndex.FAILED);

		mMessageBoxList = new MessageBoxList(this);
		mMessageBoxList.add(messageBoxInbox);
		mMessageBoxList.add(messageBoxSent);
		mMessageBoxList.add(messageBoxDraft);
		mMessageBoxList.add(messageBoxOutbox);
		mMessageBoxList.add(messageBoxFailed);

		mRadioButtonMessageBox.setTag(mMessageBoxList);

		mRadioButtonCoversations.setTag(new Conversations(this,
				(EditText) findViewById(R.id.editText_conversation),
				(EditText) findViewById(R.id.editText_conversation_message),
				(ImageView) findViewById(R.id.imageView_setting_conversation)));

		mRadioButtonCoversations
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mRadioButtonMessageBox.setChecked(false);
						}
						Conversations conversations = (Conversations) mRadioButtonCoversations
								.getTag();
						if (conversations != null) {
							conversations.setEnabled(isChecked);
						}
					}
				});

		mRadioButtonMessageBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mRadioButtonCoversations.setChecked(false);
						}

						MessageBoxList boxList = (MessageBoxList) mRadioButtonMessageBox
								.getTag();
						if (boxList != null) {
							boxList.setEnabled(isChecked);
						}

					}
				});

	}

	private OnGeneratorProgressUpdateListener mOnGeneratorProgressUpdateListener = new OnGeneratorProgressUpdateListener() {

		@Override
		public void onGeneratorProgressUpdate(int total, int count) {
			mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setMax(total);
			mProgressBar.setProgress(count);
			mTextViewProgress.setText(count + " / " + total);

		}

		@Override
		public void onGeneratorProcessEnd() {
			mProgressBar.setMax(0);
			mProgressBar.setProgress(0);
			mTextViewProgress.setText("");
			mLinearLayoutProgressBar.setVisibility(View.GONE);

		}
	};

}
