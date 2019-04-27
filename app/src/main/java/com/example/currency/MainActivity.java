package com.example.currency;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.currency.Common.Constants;
import com.example.currency.Common.WorkThread;
import com.example.currency.WorkTask.HttpRequestTask;
import com.example.currency.WorkTask.LoadFileTask;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * The main activity which show currencies of selected countries.
 */
public class MainActivity extends BaseActivity
{
    protected MainModel mainModel;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(CountryListActivity.class, mainModel);
            }
        });

        if (model == null)
        {
            model = new MainModel();
            mainModel = (MainModel)model;
        }

        mainLayout = findViewById(R.id.main_layout);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        LoadFileTask loadFileTask = new LoadFileTask("country_list", this);
        WorkThread.getInstance().AddTask(loadFileTask);
    }

    @Override
    protected void updateView()
    {
        super.updateView();

        if (mainLayout == null)
        {
            return;
        }
        mainLayout.removeAllViews();
        for(Map.Entry<String, CurrencyInfo> entry : mainModel.getCurrencyInfos().entrySet())
        {
            final CurrencyInfo info = entry.getValue();

            if (info.getIsChecked())
            {
                TextView textView = new TextView(this);
                textView.setTextColor(Color.parseColor("#86AD33"));
                textView.setTextSize(20);
                textView.setText("  100  " + mainModel.baseCountry + "  :  " + info.getCountryShortName() + "  " + 100 * info.getCurrency());
                mainLayout.addView(textView);
            }
        }
    }

    @Override
    protected void handleMainThreadMessage(Message msg)
    {
        super.handleMainThreadMessage(msg);

        switch (msg.what)
        {
            // After loading currency information for local file.
            case Constants.MSG_LOAD_FILE:
            {
                HashMap<String, CurrencyInfo> currencyInfos = (HashMap<String, CurrencyInfo>) msg.obj;
                if (currencyInfos != null)
                {
                    mainModel.AddCountryInfoes(currencyInfos);
                }

                SharedPreferences settings = getSharedPreferences(Constants.Pref_Name, 0);
                long lastTime = settings.getLong(Constants.Pref_Http_Request_Time, 0);
                long currentTime = Calendar.getInstance().getTime().getTime();
                // request every 30 minutes
                if (currentTime - lastTime < 30 * 60 * 1000)
                {
                    updateView();
                    return;
                }

                HttpRequestTask requestTask = new HttpRequestTask(Constants.Msg_Http_Country_List, Constants.currencyListUrlPath, mainHandler);
                WorkThread.getInstance().AddTask(requestTask);
                break;
            }
            // After requesting country list
            case Constants.Msg_Http_Country_List:
            {
                JSONObject jsonObject = (JSONObject) msg.obj;
                mainModel.parseJSONObjectCountryList(jsonObject);

                String countryName = "";
                boolean isFirst = true;
                for (Map.Entry<String, CurrencyInfo> entry : mainModel.getCurrencyInfos().entrySet())
                {
                    CurrencyInfo info = entry.getValue();
                    if (isFirst)
                    {
                        isFirst = false;
                        countryName += info.getCountryShortName();
                    }
                    else
                    {
                        countryName += "," + info.getCountryShortName();
                    }
                }
                if (mainModel.getCurrencyInfos().size() != 0)
                {
                    String urlPath = "http://www.apilayer.net/api/live?access_key=d82ba2a0ca1decde625c0d080bc52041&currencies=" + countryName + "&source=USD&format=1";
                    HttpRequestTask requestTask = new HttpRequestTask(Constants.Msg_Http_Currency, urlPath, mainHandler);
                    WorkThread.getInstance().AddTask(requestTask);


                    // save http time
                    SharedPreferences settings = getSharedPreferences(Constants.Pref_Name, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    long currentTime = Calendar.getInstance().getTime().getTime();
                    editor.putLong(Constants.Pref_Http_Request_Time, currentTime);

                    // Commit the edits!
                    editor.commit();
                }
                break;
            }
            // After requesting currency information
            case Constants.Msg_Http_Currency:
            {
                JSONObject jsonObject = (JSONObject) msg.obj;
                mainModel.parseJSONObjectCurrencyStatus(jsonObject);
                updateView();
                break;
            }
        }
    }
}
