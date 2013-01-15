/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */
package com.hashcap.qiksmsgenerator;

import com.hashcap.qiksmsgenerator.GeneratorUtils.FolderName;

import android.content.Context;


public class ConversationsGenerator extends Generator {
	private int mConversations;
	private int mType;
	private Context mContext;

	public ConversationsGenerator(Context context, int type) {
		super(context, type);
		mContext = context;
		mType = type;
	}

	public int getConversations() {
		return mConversations;
	}

	public void setConversations(int conversations) {
		this.mConversations = conversations;
	}

	@Override
	public String toString() {
		return "Generator = " + FolderName.getName(mType) + "mConversations = "
				+ mConversations + "mMessages =  "
				+ super.getDataSettings().getMessages() + "mUri = " + getUri();
	}

}
