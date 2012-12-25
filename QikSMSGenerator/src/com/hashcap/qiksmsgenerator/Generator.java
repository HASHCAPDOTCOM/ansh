package com.hashcap.qiksmsgenerator;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;

import android.net.Uri;

public class Generator {
	public static int TOTAL = 0;
	private Uri mUri;
	private int mType;
	private int mCount;
	private int mPosition;
	private boolean mSingleRecipient;
	private String mBody;

	public Generator(int type) {
		mType = type;
	}

	public boolean isSingleRecipient() {
		return mSingleRecipient;
	}

	public void setSingleRecipient(boolean singleRecipient) {
		this.mSingleRecipient = singleRecipient;
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

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		this.mCount = count;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		this.mPosition = position;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		this.mBody = body;
	}

	@Override
	public String toString() {
		return "Generator = " + TagName.getName(mType) + "mCount =  " + mCount
				+ "mUri = " + mUri;
	}

}
