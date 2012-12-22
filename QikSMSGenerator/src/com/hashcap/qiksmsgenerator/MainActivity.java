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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class MainActivity extends Activity {

	private RadioButton mRadioButtonCoversations;
	private RadioButton mRadioButtonMessageBox;
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
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

	private void initViews() {
		mRadioButtonCoversations = (RadioButton) findViewById(R.id.radioButton_conversation_title);
		mRadioButtonMessageBox = (RadioButton) findViewById(R.id.radioButton_message_box_title);

		MessageBox messageBoxInbox = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_inbox),
				(EditText) findViewById(R.id.editText_inbox),
				(ImageView) findViewById(R.id.imageView_setting_inbox),
				GeneratorUtils.SettingsTag.INBOX);

		MessageBox messageBoxSent = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_sent),
				(EditText) findViewById(R.id.editText_sent),
				(ImageView) findViewById(R.id.imageView_setting_sent),
				GeneratorUtils.SettingsTag.SENT);

		MessageBox messageBoxDraft = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_draft),
				(EditText) findViewById(R.id.editText_draft),
				(ImageView) findViewById(R.id.imageView_setting_draft),
				GeneratorUtils.SettingsTag.DRAFT);

		MessageBox messageBoxOutbox = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_outbox),
				(EditText) findViewById(R.id.editText_outbox),
				(ImageView) findViewById(R.id.imageView_setting_outbox),
				GeneratorUtils.SettingsTag.OUTBOX);

		MessageBox messageBoxFailed = new MessageBox(this,
				(CheckBox) findViewById(R.id.checkBox_failed),
				(EditText) findViewById(R.id.editText_failed),
				(ImageView) findViewById(R.id.imageView_setting_failed),
				GeneratorUtils.SettingsTag.FAILED);

		MessageBoxList boxList = new MessageBoxList(this);
		boxList.add(messageBoxInbox);
		boxList.add(messageBoxSent);
		boxList.add(messageBoxDraft);
		boxList.add(messageBoxOutbox);
		boxList.add(messageBoxFailed);

		mRadioButtonMessageBox.setTag(boxList);

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
}
