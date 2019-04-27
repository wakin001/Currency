package com.example.currency.WorkTask;

import android.os.Handler;

import com.example.currency.BaseActivity;
import com.example.currency.Common.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Load the file from local.
 */
public class LoadFileTask extends BaseTask
{
    private String fileName;
    private BaseActivity activity;

    public LoadFileTask(String fileName, BaseActivity activity)
    {
        super(activity.getMainHandler());
        messageType = Constants.MSG_LOAD_FILE;
        this.fileName = fileName;
        this.activity = activity;
    }

    @Override
    public void execute()
    {
        try
        {
            File file = new File(activity.getDir("data", activity.MODE_PRIVATE), fileName);
            if(!file.exists())
            {
                CallMainThread(Constants.MSG_LOAD_FILE, null);
                return;
            }

            FileInputStream is = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object obj = ois.readObject();
            ois.close();
            is.close();

            CallMainThread(Constants.MSG_LOAD_FILE, obj);
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
    }

}
