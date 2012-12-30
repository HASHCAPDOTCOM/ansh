/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import java.util.ArrayList;

import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStartListener;

import android.content.Context;
import android.os.Handler;

public class MessageBoxList {
	private ArrayList<MessageBox> mMessageBoxs = new ArrayList<MessageBox>();
	private OnGeneratorStartListener mGeneratorStartListener;
	private ArrayList<Generator> mGenerators = new ArrayList<Generator>();
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

	public void ensureGenerator(
			OnGeneratorStartListener onGeneratorStartListener) {
		mGenerators.clear();
		for (MessageBox messageBox : mMessageBoxs) {
			if (messageBox.isChecked()) {
				mGenerators.add(messageBox
						.getGenerator(onGeneratorStartListener));
			}
		}
	}

	public void generate() {
		if (mGenerators != null && mGenerators.size() > 0) {
			for (Generator generator : mGenerators) {
				generator.start();
			}
			if (mGeneratorStartListener != null) {

			}
		}

	}

	public boolean canActivateGenerator() {
		for (MessageBox messageBox : mMessageBoxs) {

			if (messageBox.isChecked() && messageBox.getMessages() > 0) {
				return true;
			}
		}
		return false;
	}
}
