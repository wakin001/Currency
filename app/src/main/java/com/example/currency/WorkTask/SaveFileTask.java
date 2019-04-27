package com.example.currency.WorkTask;

import com.example.currency.BaseActivity;
import com.example.currency.Common.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Save the file locally.
 */
public class SaveFileTask extends BaseTask
{
    private Object obj;
    private BaseActivity activity;
    private String fileName;

    public SaveFileTask(Object obj, BaseActivity activity, String fileName)
    {
        super(activity.getMainHandler());
        messageType = Constants.MSG_SAVE_FILE;
        this.activity = activity;
        this.obj = obj;
        this.fileName = fileName;
    }

    @Override
    public void execute()
    {
        File file = new File(activity.getDir("data", activity.MODE_PRIVATE), fileName);
        ObjectOutputStream outputStream = null;
        try
        {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(obj);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
