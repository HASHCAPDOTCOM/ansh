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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView mImageViewSettingsConversation;
	private ImageView mImageViewSettingsInbox;
	private ImageView mImageViewSettingsSent;
	private ImageView mImageViewSettingsDraft;
	private ImageView mImageViewSettingsOutbox;
	private ImageView mImageViewSettingsFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSettingsImageViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void initSettingsImageViews() {
		mImageViewSettingsConversation = (ImageView) findViewById(R.id.imageView_setting_conversation);
		mImageViewSettingsInbox = (ImageView) findViewById(R.id.imageView_setting_inbox);
		mImageViewSettingsSent = (ImageView) findViewById(R.id.imageView_setting_sent);
		mImageViewSettingsDraft = (ImageView) findViewById(R.id.imageView_setting_draft);
		mImageViewSettingsOutbox = (ImageView) findViewById(R.id.imageView_setting_outbox);
		mImageViewSettingsFailed = (ImageView) findViewById(R.id.imageView_setting_failed);
		mImageViewSettingsConversation
				.setOnClickListener(mImageViewOnClickListener);
		mImageViewSettingsInbox.setOnClickListener(mImageViewOnClickListener);
		mImageViewSettingsSent.setOnClickListener(mImageViewOnClickListener);
		mImageViewSettingsDraft.setOnClickListener(mImageViewOnClickListener);
		mImageViewSettingsOutbox.setOnClickListener(mImageViewOnClickListener);
		mImageViewSettingsFailed.setOnClickListener(mImageViewOnClickListener);
	}

	private OnClickListener mImageViewOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					DataSettingsActivity.class);
			switch (v.getId()) {
			case R.id.imageView_setting_conversation: {
				intent.putExtra("TAG", Util.SettingsTag.CONVERSATION);
				break;
			}
			case R.id.imageView_setting_inbox: {
				intent.putExtra("TAG", Util.SettingsTag.INBOX);
				break;
			}
			case R.id.imageView_setting_sent: {
				intent.putExtra("TAG", Util.SettingsTag.SENT);
				break;
			}
			case R.id.imageView_setting_draft: {
				intent.putExtra("TAG", Util.SettingsTag.DRAFT);
				break;
			}
			case R.id.imageView_setting_outbox: {
				intent.putExtra("TAG", Util.SettingsTag.OUTBOX);
				break;
			}
			case R.id.imageView_setting_failed: {
				intent.putExtra("TAG", Util.SettingsTag.FAILED);
				break;
			}
			}

			startActivity(intent);
		}
	};
}
