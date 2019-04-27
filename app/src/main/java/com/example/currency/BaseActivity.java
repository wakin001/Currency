package com.example.currency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.currency.Common.Constants;
import com.example.currency.Common.WorkThread;

/**
 * The base class of all activities.
 */
public class BaseActivity extends AppCompatActivity
{
    protected Context context = null;
    protected BaseModel model = null;

    protected Handler mainHandler = null;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		context = this;

		Bundle bundle = getIntent().getBundleExtra(Constants.Bundler);
		if (bundle != null)
		{
			model = (BaseModel) bundle.getSerializable(Constants.SerializeBaseModel);
		}

		if (!WorkThread.getInstance().isAlive())
		{
			WorkThread.getInstance().startThread();
		}

		mainHandler = new Handler(context.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				handleMainThreadMessage(msg);
			}
		};
	}

	/**
	 * Switch activity.
	 * @param changeClass The acitivity to switch.
	 * @param model Pass the model data to the next activity.
	 */
	public void changeActivity(Class<?> changeClass, BaseModel model)
	{
		//new Activity
		Intent intent = new Intent(this, changeClass);
		Bundle args = new Bundle();
		args.putSerializable(Constants.SerializeBaseModel, model);
		intent.putExtra(Constants.Bundler, args);
		startActivity(intent);
	}

	/**
	 * Update the views.
	 */
	protected void updateView()
	{

	}

	/**
	 * Handle message from work thread.
	 * @param msg The message to handle.
	 */
	protected void handleMainThreadMessage(Message msg)
	{
	}

	public Handler getMainHandler()
	{
		return mainHandler;
	}
}