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
import android.widget.Toast;

import com.hashcap.qiksmsgenerator.GeneratorUtils.TagIndex;
import com.hashcap.qiksmsgenerator.support.ConversationsGenerator;
import com.hashcap.qiksmsgenerator.support.Generator;
import com.hashcap.qiksmsgenerator.support.MaxGeneratorException;
import com.hashcap.qiksmsgenerator.support.OnGeneratorProgressUpdateListener;

public class GeneratorServeice extends Service {
	private static final String TAG = "GeneratorServeice";
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

	public void add(Generator generator) throws MaxGeneratorException {
		if (mGenerators.size() >= Generator.MAX_GENERATOR) {
			throw new MaxGeneratorException("Maximum "
					+ Generator.MAX_GENERATOR
					+ " Generator object can execute.");
		}
		mGenerators.add(generator);
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
					mGenerator = mGenerators.poll();
					if (mGenerator != null) {
						generateMessages();
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
		mStatus = false;
		if (mGeneratorProgressUpdateListener != null) {
			mGeneratorProgressUpdateListener.onGeneratorProcessEnd();
			Toast.makeText(GeneratorServeice.this,
					"SMS Generator Sucessfuly Completed.", Toast.LENGTH_SHORT)
					.show();
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
						if (generator.getDataSettings().getMessages() == result) {
							executeNextGenerator();
						}
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

						} else {
							for (int i = 0; i < generator.getDataSettings()
									.getMessages(); i++) {
								if (isCanceled()) {
									return i;
								}
								ContentValues values = generator.getSms();
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
}
