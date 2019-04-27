package com.example.currency.WorkTask;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The http request task
 */
public class HttpRequestTask extends BaseTask
{
    private String urlPath;

    public HttpRequestTask(int messageType, String urlPath, Handler mainHandler)
    {
        super(mainHandler);
        this.urlPath = urlPath;
        this.messageType = messageType;
    }

    @Override
    public void execute()
    {
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(urlPath);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            CallMainThread(messageType, readStream(in));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * Read data from http and parse into Json type.
     * @param in The InputStream object.
     * @return
     * @throws IOException
     */
    public JSONObject readStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            JSONObject jsonObj = new JSONObject(sb.toString());
            Log.d("json: ", jsonObj.toString());
            return jsonObj;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }
        return null;
    }
}
