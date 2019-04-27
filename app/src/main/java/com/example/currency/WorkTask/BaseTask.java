package com.example.currency.WorkTask;

import android.os.Handler;

/**
 * The base class of all tasks which can be added into the work thread.
 */
public abstract class BaseTask
{
    protected Handler mainHandler;
    protected int messageType;

    public BaseTask(Handler handler)
    {
        this.mainHandler = handler;
    }

    /**
     * Execute this task.
     * Override the function and write your main functionality in it.
     */
    public abstract void execute();

    public int getMessageType()
    {
        return messageType;
    }

    /**
     * Pass data to the main UI thread.
     * @param messageType The message type.
     * @param data The data to pass.
     */
    public void CallMainThread(int messageType, Object data)
    {
        android.os.Message message = mainHandler.obtainMessage();
        message.what = messageType;
        message.obj = data;
        mainHandler.sendMessage(message);
    }

}
