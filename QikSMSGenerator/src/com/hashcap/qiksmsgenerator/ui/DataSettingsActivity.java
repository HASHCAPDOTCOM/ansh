/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator.ui;

import com.hashcap.qiksmsgenerator.DataSettings;
import com.hashcap.qiksmsgenerator.R;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.R.id;
import com.hashcap.qiksmsgenerator.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class DataSettingsActivity extends Activity {
	private RadioButton mCheckBoxSingleRecipient;
	private RadioButton mCheckBoxMultipleRecipients;
	private CheckBox mCheckBoxText;
	private CheckBox mCheckBoxPhone;
	private CheckBox mCheckBoxEmail;
	private CheckBox mCheckBoxWeb;
	private CheckBox mCheckBoxSmiley;
	private DataSettings mDataSettings;
	private int mTag;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_settings_activity);

		init(getIntent());

	}

	@Override
	public void onBackPressed() {
		populateData();
		Intent intent = new Intent();
		intent.putExtra("data", mDataSettings);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	private void init(Intent intent) {

		mCheckBoxSingleRecipient = (RadioButton) findViewById(R.id.radioButton_single_recipient);
		mCheckBoxMultipleRecipients = (RadioButton) findViewById(R.id.radioButton_multiple_recipients);
		mCheckBoxText = (CheckBox) findViewById(R.id.checkBox_message_text);
		mCheckBoxPhone = (CheckBox) findViewById(R.id.checkBox_message_phone_number);
		mCheckBoxEmail = (CheckBox) findViewById(R.id.checkBox_message_email_address);
		mCheckBoxWeb = (CheckBox) findViewById(R.id.checkBox_message_web_address);
		mCheckBoxSmiley = (CheckBox) findViewById(R.id.checkBox_message_smiley);

		if (intent == null) {
			return;
		}

		Bundle bundle = intent.getExtras();
		mTag = bundle.getInt("tag", 0);
		mDataSettings = (DataSettings) bundle.get("data");

		initData(mDataSettings);

	}

	private void initData(DataSettings dataSettings) {
		if (dataSettings == null) {
			return;
		}
		if (mTag == TagIndex.INBOX) {
			mCheckBoxSingleRecipient.setChecked(true);
			mCheckBoxMultipleRecipients.setChecked(false);
			mCheckBoxMultipleRecipients.setEnabled(false);
		} else {
			mCheckBoxMultipleRecipients.setEnabled(true);
			if (dataSettings.isSingleRecipient()) {
				mCheckBoxSingleRecipient.setChecked(true);
			} else {
				mCheckBoxMultipleRecipients.setChecked(true);
			}
		}

		mCheckBoxText.setChecked(dataSettings.isText());
		mCheckBoxPhone.setChecked(dataSettings.isPhone());
		mCheckBoxEmail.setChecked(dataSettings.isEmail());
		mCheckBoxWeb.setChecked(dataSettings.isWeb());
		mCheckBoxSmiley.setChecked(dataSettings.isSmiley());
	}

	private void populateData() {
		mDataSettings.setSingleRecipient(mCheckBoxSingleRecipient.isChecked());
		mDataSettings.setText(mCheckBoxText.isChecked());
		mDataSettings.setPhone(mCheckBoxPhone.isChecked());
		mDataSettings.setEmail(mCheckBoxEmail.isChecked());
		mDataSettings.setWeb(mCheckBoxWeb.isChecked());
		mDataSettings.setSmiley(mCheckBoxSmiley.isChecked());
	}
}
