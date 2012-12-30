package com.hashcap.qiksmsgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ConversationsDataSettings extends DataSettings {
	private static final String TAG = "ConversationsDataSettings";
	private static final boolean DEBUG = true;
	private int mConversations;

	public ConversationsDataSettings(Parcel source) {
		super(source);
		mConversations = source.readInt();
	}

	public ConversationsDataSettings(Context context, String name) {
		super(context, name);
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		mConversations = preferences.getInt("conversations", 0);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(mConversations);
	}

	public static final Parcelable.Creator<ConversationsDataSettings> CREATOR = new Creator<ConversationsDataSettings>() {

		@Override
		public ConversationsDataSettings[] newArray(int size) {
			return new ConversationsDataSettings[size];
		}

		@Override
		public ConversationsDataSettings createFromParcel(Parcel source) {
			return new ConversationsDataSettings(source);
		}
	};

	public int getConversations() {
		return mConversations;
	}

	public void setConversations(int conversations) {
		this.mConversations = conversations;
	}

	@Override
	public String toString() {
		return "[ " + super.toString() + " mConversations = " + mConversations
				+ " ]";
	}

	public void save(Context context, String name) {
		super.save(context, name);
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("conversations", mConversations);
		editor.apply();
		if (DEBUG) {
			Log.v(TAG, "Save Sucessfuly : mDataSettings = " + this);
		}
	}

}
