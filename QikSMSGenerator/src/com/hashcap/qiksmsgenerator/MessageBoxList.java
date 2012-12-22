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

import java.util.HashSet;

import android.content.Context;
import android.os.Handler;

public class MessageBoxList {
	private HashSet<MessageBox> mMessageBoxs = new HashSet<MessageBox>();

	private boolean mEnabled;
	private Context mContext;
	private Handler mHandler;

	public MessageBoxList(Context context) {
		mContext = context;
		mHandler = new Handler();
	}

	public void add(MessageBox box) {
		mMessageBoxs.add(box);
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
		for (MessageBox box : mMessageBoxs) {
			box.setEnabled(mEnabled);
		}
	}

	public void destroy() {
		for (MessageBox box : mMessageBoxs) {
			box.destroy();
		}
		
	}
}
