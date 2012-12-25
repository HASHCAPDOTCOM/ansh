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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class DataSettings implements Parcelable {

	private int mParts;
	private int mMessages;
	private boolean mSingleRecipient;
	private boolean mText;
	private int mTextType;
	private boolean mPhone;
	private boolean mEmail;
	private boolean mWeb;
	private boolean mSmiley;
	private Context mContext;

	private SharedPreferences mPreferences;

	public DataSettings(Context context, String name) {
		mContext = context;
		mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		mParts = mPreferences.getInt("part", 1);
		mMessages = mPreferences.getInt("messages", 1);
		mSingleRecipient = mPreferences.getBoolean("single_recipient", true);
		mText = mPreferences.getBoolean("text", true);
		mTextType = mPreferences.getInt("text_type", 0);
		mPhone = mPreferences.getBoolean("phone", true);
		mEmail = mPreferences.getBoolean("email", true);
		mWeb = mPreferences.getBoolean("web", true);
		mSmiley = mPreferences.getBoolean("smiley", true);
	}

	public DataSettings(Parcel source) {
		mParts = source.readInt();
		mMessages = source.readInt();
		mSingleRecipient = source.readByte() == 1;
		mText = source.readByte() == 1;
		mTextType = source.readInt();
		mPhone = source.readByte() == 1;
		mEmail = source.readByte() == 1;
		mWeb = source.readByte() == 1;
		mSmiley = source.readByte() == 1;
	}

	public boolean isText() {
		return mText;
	}

	public void setText(boolean text) {
		this.mText = text;
	}

	public int getTextType() {
		return mTextType;
	}

	public void setTextType(int textType) {
		this.mTextType = textType;
	}

	public boolean isSingleRecipient() {
		return mSingleRecipient;
	}

	public void setSingleRecipient(boolean singleRecipient) {
		this.mSingleRecipient = singleRecipient;
	}

	public int getParts() {
		return mParts;
	}

	public void setParts(int parts) {
		this.mParts = parts;
	}

	public int getMessages() {
		return mMessages;
	}

	public void setMessages(int messages) {
		this.mMessages = messages;
	}

	public boolean isPhone() {
		return mPhone;
	}

	public void setPhone(boolean phone) {
		this.mPhone = phone;
	}

	public boolean isEmail() {
		return mEmail;
	}

	public void setEmail(boolean email) {
		this.mEmail = email;
	}

	public boolean isWeb() {
		return mWeb;
	}

	public void setWeb(boolean web) {
		this.mWeb = web;
	}

	public boolean isSmiley() {
		return mSmiley;
	}

	public void setSmiley(boolean smiley) {
		this.mSmiley = smiley;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mParts);
		dest.writeInt(mMessages);
		dest.writeByte((byte) (mSingleRecipient ? 1 : 0));
		dest.writeByte((byte) (mText ? 1 : 0));
		dest.writeInt(mTextType);
		dest.writeByte((byte) (mPhone ? 1 : 0));
		dest.writeByte((byte) (mEmail ? 1 : 0));
		dest.writeByte((byte) (mWeb ? 1 : 0));
		dest.writeByte((byte) (mSmiley ? 1 : 0));
	}

	public static final Parcelable.Creator<DataSettings> CREATOR = new Creator<DataSettings>() {

		@Override
		public DataSettings[] newArray(int size) {
			return new DataSettings[size];
		}

		@Override
		public DataSettings createFromParcel(Parcel source) {
			return new DataSettings(source);
		}
	};

	public void save(Context context, String name) {
		mPreferences = context
				.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt("part", mParts);
		editor.putInt("part", mParts);
		editor.putBoolean("single_recipient", mSingleRecipient);
		editor.putBoolean("text", mText);
		editor.putInt("text_type", mTextType);
		editor.putBoolean("phone", mPhone);
		editor.putBoolean("email", mEmail);
		editor.putBoolean("web", mWeb);
		editor.putBoolean("smiley", mSmiley);
		editor.apply();

	}

}