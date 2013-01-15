/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */

package com.hashcap.qiksmsgenerator;

import java.util.ArrayList;

public class GeneratorUtils {
	public static class FolderIndex {
		public static final int CONVERSATION = 0;
		public static final int INBOX = 1;
		public static final int SENT = 2;
		public static final int DRAFT = 3;
		public static final int OUTBOX = 4;
		public static final int FAILED = 5;
	}

	public static class FolderName {
		public static final String CONVERSATION = "conversation";
		public static final String INBOX = "inbox";
		public static final String SENT = "sent";
		public static final String DRAFT = "draft";
		public static final String OUTBOX = "outbox";
		public static final String FAILED = "failed";
		public static final ArrayList<String> nameList;
		static {
			nameList = new ArrayList<String>();
			nameList.add(CONVERSATION);
			nameList.add(INBOX);
			nameList.add(SENT);
			nameList.add(DRAFT);
			nameList.add(OUTBOX);
			nameList.add(FAILED);

		}

		public static String getName(int index) {
			return nameList.get(index);
		}
	}
}
