/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */
package com.hashcap.qiksmsgenerator.support;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagName;

public class ConversationsGenerator extends Generator {
	private int mConversations;
	private int mType;

	public ConversationsGenerator(int type) {
		super(type);
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
		return "Generator = " + TagName.getName(mType) + "mConversations = "
				+ mConversations + "mMessages =  " + getMessages() + "mUri = "
				+ getUri();
	}

}
