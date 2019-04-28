package com.example.currency.Common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.example.currency.WorkTask.BaseTask;

/**
 * Work thread to handle http request, load/save file and so on.
 */
public class WorkThread extends HandlerThread
{
    private static WorkThread instance;

    public static WorkThread getInstance()
    {
        if (instance == null)
        {
            instance = new WorkThread("Work Thread");
        }
        return instance;
    }

    protected Handler workHandler = null;

    private WorkThread(String name)
    {
        super(name);
    }

    /**
     * Call this function to start the work thread.
     */
    public void startThread()
    {
        Log.d("thread", "start work thread");
        if (!isAlive())
        {
            start();
        }
        workHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {

                BaseTask task = (BaseTask) msg.obj;
                task.execute();
            }
        };
    }

    @Override
    public boolean quit()
    {
        instance = null;
        return super.quit();
    }

    /**
     * Add a task into message queue.
     * @param task The task to add.
     */
    public void AddTask(BaseTask task)
    {
        if (!isAlive())
        {
            startThread();
        }
        Message message = workHandler.obtainMessage();
        message.what = task.getMessageType();
        message.obj = task;
        workHandler.sendMessage(message);
    }

    public Handler getWorkHandler()
    {
        return workHandler;
    }
}
