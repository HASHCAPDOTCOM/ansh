/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */
package com.hashcap.qiksmsgenerator.support;

import android.content.Context;
import android.net.Uri;
import android.util.MonthDisplayHelper;

import com.hashcap.qiksmsgenerator.DataSettings;
import com.hashcap.qiksmsgenerator.GeneratorUtils;
import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;

public class Generator {
	public static int sCount = 0;
	public static int sPosition = 0;
	private OnGeneratorStartListener mGeneratorStartListener;
	private Uri mUri;
	private int mType;
	private int mMessages;
	private int mGenerated;
	private DataSettings mDataSettings;

	public Generator(int type) {
		mType = type;
	}

	public DataSettings getDataSettings() {
		return mDataSettings;
	}

	public void setDataSettings(DataSettings dataSettings) {
		this.mDataSettings = dataSettings;
	}

	public int getMessages() {
		return mMessages;
	}

	public void setMessages(int messages) {
		this.mMessages = messages;
	}

	public int getGenerated() {
		return mGenerated;
	}

	public void setGenerated(int generated) {
		this.mGenerated = generated;
	}

	public Uri getUri() {
		return mUri;
	}

	public void setUri(Uri uri) {
		this.mUri = uri;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		this.mType = type;
	}

	@Override
	public String toString() {
		return "Generator = " + TagName.getName(mType) + "mMessages =  "
				+ mMessages + "mUri = " + mUri;
	}

	public void start() {
		if(mGeneratorStartListener != null){
			mGeneratorStartListener.onGeneratorStart(this);
		}
	}

	public void setOnGeneratorStartListener(
			OnGeneratorStartListener generatorStartListener) {
		mGeneratorStartListener = generatorStartListener;
	}

}
