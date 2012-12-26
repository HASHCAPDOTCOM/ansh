/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;

public class MessageBoxList {
	private ArrayList<MessageBox> mMessageBoxs = new ArrayList<MessageBox>();

	private boolean mEnabled;
	private Context mContext;

	public MessageBoxList(Context context) {
		mContext = context;
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

	public void setSettingsData(int requestCode, DataSettings dataSettings) {
		mMessageBoxs.get(requestCode).setSettingsData(dataSettings);

	}
}
