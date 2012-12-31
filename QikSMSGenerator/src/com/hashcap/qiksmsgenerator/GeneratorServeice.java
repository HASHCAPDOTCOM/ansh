package com.hashcap.qiksmsgenerator;

import java.util.concurrent.ArrayBlockingQueue;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony.Sms;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.support.ConversationsGenerator;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.MaxGeneratorException;
import com.hashcap.qiksmsgenerator.support.OnGeneratorProgressUpdateListener;
import com.hashcap.qiksmsgenerator.support.OnGeneratorStatusChangedListener;

public class GeneratorServeice extends Service {
	private static final String TAG = "GeneratorServeice";
	private static final boolean DEBUG = true;
	ArrayBlockingQueue<Generator> mGenerators = new ArrayBlockingQueue<Generator>(
			Generator.MAX_GENERATOR);
	private Generator mGenerator;
	private boolean mIsCanceled = false;
	private boolean mStatus = false;
	private final IBinder mBinder = new GeneratorBinder();
	private OnGeneratorProgressUpdateListener mGeneratorProgressUpdateListener;

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class GeneratorBinder extends Binder {
		public GeneratorServeice getService() {
			return GeneratorServeice.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void add(Generator generator) throws MaxGeneratorException,
			Exception {
		if (mGenerators.size() >= Generator.MAX_GENERATOR) {
			throw new MaxGeneratorException("Maximum "
					+ Generator.MAX_GENERATOR
					+ " Generator object can execute.");
		}
		try {
			mGenerators.add(generator);
		} catch (Exception e) {
			throw e;
		}

		if (generator instanceof ConversationsGenerator) {
			int conversations = ((ConversationsDataSettings) generator
					.getDataSettings()).getConversations();
			int messages = generator.getDataSettings().getMessages();
			Generator.sTotal += (messages * conversations);
		} else {
			int messages = generator.getDataSettings().getMessages();
			Generator.sTotal += messages;
		}

		notifyGeneratorToExecute();
	}

	private void notifyGeneratorToExecute() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				if (mGenerator == null) {
					if (isCanceled()) {
						if (DEBUG) {
							Log.v(TAG, "Generator pocess stoped .");
						}
						resetGenerator();
						return;
					}
					mGenerator = mGenerators.poll();
					Generator.setGeneratorQueueFull(false);
					if (mGenerator != null) {
						mIsCanceled = false;
						generateMessages();
						final OnGeneratorStatusChangedListener activeListener = Generator
								.getGeneratorActiveListener();
						if (activeListener != null) {
							new Handler().post(new Runnable() {
								@Override
								public void run() {
									activeListener.onStopStatusChnaged(true);
								}
							});

						}
					} else {
						resetGenerator();
					}

				}
			}

		});

	}

	protected void resetGenerator() {
		Generator.sTotal = 0;
		Generator.sPosition = 0;
		mGenerators.clear();
		if (mGenerator != null) {
			Toast.makeText(GeneratorServeice.this,
					"Operation aborted by user.", Toast.LENGTH_SHORT).show();
			mGenerator = null;
		} else {
			Toast.makeText(GeneratorServeice.this,
					"SMS Generator Sucessfuly Completed.", Toast.LENGTH_SHORT)
					.show();
		}
		mStatus = false;
		mIsCanceled = false;
		if (mGeneratorProgressUpdateListener != null) {
			mGeneratorProgressUpdateListener.onGeneratorProcessEnd();
		}

		final OnGeneratorStatusChangedListener activeListener = Generator
				.getGeneratorActiveListener();
		if (activeListener != null) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					activeListener.onStopStatusChnaged(false);
				}
			});

		}
	}

	private void generateMessages() {
		if (mGenerator != null) {

			mStatus = true;
			new AsyncTask<Generator, Integer, Integer>() {

				@Override
				protected void onCancelled() {
					super.onCancelled();

				}

				@Override
				protected void onPostExecute(Integer result) {
					Generator generator = getGenerator();
					if (generator != null) {
						generator.setGenerated(0);
						executeNextGenerator();
					}

					super.onPostExecute(result);
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();

				}

				@Override
				protected void onProgressUpdate(Integer... values) {
					OnGeneratorProgressUpdateListener generatorProgressUpdateListener = getGeneratorProgressUpdateListener();
					if (generatorProgressUpdateListener != null) {
						generatorProgressUpdateListener
								.onGeneratorProgressUpdate(
										Generator.getTotal(),
										Generator.getPosition());
					}
					super.onProgressUpdate(values);
				}

				@Override
				protected Integer doInBackground(Generator... params) {
					Generator generator = params[0];

					if (generator != null) {
						if (generator.getType() == TagIndex.CONVERSATION) {
							if (generator instanceof ConversationsGenerator) {
								ConversationsGenerator conversationsGenerator = (ConversationsGenerator) generator;
								int conversations = ((ConversationsDataSettings) conversationsGenerator
										.getDataSettings()).getConversations();
								int messages = conversationsGenerator
										.getDataSettings().getMessages();
								if (conversations <= 0 || messages <= 0) {
									return 0;
								}
								for (int i = 0; i < conversations; i++) {

									String address = TextUtils.join(",",
											conversationsGenerator.getAddress()
													.toArray());
									for (int j = 0; j < messages; j++) {
										if (isCanceled()) {
											return i;
										}
										ContentValues values = generator
												.getSms(address);
										if (values.containsKey(Sms.ADDRESS)) {
											values.remove(Sms.ADDRESS);
										}
										values.put(Sms.ADDRESS, address);
										Uri uri = getContentResolver().insert(
												Sms.CONTENT_URI, values);
										if (uri != null) {
											generator.increment();
											publishProgress(0);
										}
									}
								}
								return messages;
							}

						} else {
							for (int i = 0; i < generator.getDataSettings()
									.getMessages(); i++) {
								if (isCanceled()) {
									return i;
								}
								ContentValues values = generator.getSms(null);
								Uri uri = getContentResolver().insert(
										Sms.CONTENT_URI, values);
								if (uri != null) {
									generator.increment();
									publishProgress(0);
								}
							}

						}
					}
					return generator.getDataSettings().getMessages();
				}
			}.execute(mGenerator);
		}

	}

	protected Generator getGenerator() {
		return mGenerator;
	}

	protected void executeNextGenerator() {
		if (isCanceled()) {
			if (DEBUG) {
				Log.v(TAG, "Generator pocess stoped .");
			}
			resetGenerator();
			return;
		}
		mGenerator = null;
		notifyGeneratorToExecute();
	}

	public void registerGeneratorProgressUpdateListener(
			OnGeneratorProgressUpdateListener onGeneratorProgressUpdateListener) {
		mGeneratorProgressUpdateListener = onGeneratorProgressUpdateListener;

	}

	public void unregisterGeneratorProgressUpdateListener() {
		mGeneratorProgressUpdateListener = null;
	}

	private OnGeneratorProgressUpdateListener getGeneratorProgressUpdateListener() {
		return mGeneratorProgressUpdateListener;
	}

	private boolean isCanceled() {
		return mIsCanceled;
	}

	public boolean getStatus() {
		return mStatus;
	}

	public void cancel() {
		mIsCanceled = true;
	}
}
