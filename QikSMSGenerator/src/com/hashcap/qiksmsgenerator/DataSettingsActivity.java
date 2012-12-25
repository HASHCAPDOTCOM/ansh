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

import com.hashcap.qiksmsgenerator.support.InputFilterMinMax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class DataSettingsActivity extends Activity {
	private RadioButton mCheckBoxSingleRecipient;
	private RadioButton mCheckBoxMultipleRecipients;
	private CheckBox mCheckBoxText;
	private CheckBox mCheckBoxPhone;
	private CheckBox mCheckBoxEmail;
	private CheckBox mCheckBoxWeb;
	private CheckBox mCheckBoxSmiley;
	private EditText mEditTextParts;
	private Spinner mSpinnerTextType;
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
		mEditTextParts = (EditText) findViewById(R.id.editText_message_parts);
		mEditTextParts.setFilters(new InputFilter[] { new InputFilterMinMax(
				this, "0", "15") });

		mCheckBoxText = (CheckBox) findViewById(R.id.checkBox_message_text);
		mSpinnerTextType = (Spinner) findViewById(R.id.spinner_message_text_type);
		mCheckBoxPhone = (CheckBox) findViewById(R.id.checkBox_message_phone_number);
		mCheckBoxEmail = (CheckBox) findViewById(R.id.checkBox_message_email_address);
		mCheckBoxWeb = (CheckBox) findViewById(R.id.checkBox_message_web_address);
		mCheckBoxSmiley = (CheckBox) findViewById(R.id.checkBox_message_smiley);

		if (intent == null) {
			return;
		}

		Bundle bundle = intent.getExtras();
		mDataSettings = (DataSettings) bundle.get("data");

		initData(mDataSettings);

	}

	private void initData(DataSettings dataSettings) {
		if (dataSettings == null) {
			return;
		}
		if (dataSettings.isSingleRecipient()) {
			mCheckBoxSingleRecipient.setChecked(true);
		} else {
			mCheckBoxMultipleRecipients.setChecked(true);
		}
		mEditTextParts.setText(dataSettings.getParts() + "");
		mCheckBoxText.setChecked(dataSettings.isText());
		mSpinnerTextType.setSelection(dataSettings.getTextType());
		mCheckBoxPhone.setChecked(dataSettings.isPhone());
		mCheckBoxEmail.setChecked(dataSettings.isEmail());
		mCheckBoxWeb.setChecked(dataSettings.isWeb());
		mCheckBoxSmiley.setChecked(dataSettings.isSmiley());
	}

	private void populateData() {
		mDataSettings.setSingleRecipient(mCheckBoxSingleRecipient.isChecked());
		mDataSettings.setParts(Integer.parseInt(mEditTextParts.getText()
				.toString()));
		mDataSettings.setText(mCheckBoxText.isChecked());
		mDataSettings.setTextType(mSpinnerTextType.getSelectedItemPosition());
		mDataSettings.setPhone(mCheckBoxPhone.isChecked());
		mDataSettings.setEmail(mCheckBoxEmail.isChecked());
		mDataSettings.setWeb(mCheckBoxWeb.isChecked());
		mDataSettings.setSmiley(mCheckBoxSmiley.isChecked());
	}
}
