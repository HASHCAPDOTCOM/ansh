/*
 * Copyright (C) 2012-2013 Hashcap Pvt. Ltd.
 */
package com.hashcap.qiksmsgenerator.support;

public interface OnGeneratorStatusChangedListener {
	void onStartStatusChanged();
	void onStopStatusChnaged(boolean enabled);
}
