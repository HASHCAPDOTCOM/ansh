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

import java.util.ArrayList;

public class GeneratorUtils {
	public static class TagIndex {
		public static final int CONVERSATION = 0;
		public static final int INBOX = 1;
		public static final int SENT = 2;
		public static final int DRAFT = 3;
		public static final int OUTBOX = 4;
		public static final int FAILED = 5;
	}

	public static class TagName {
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
