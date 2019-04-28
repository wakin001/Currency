package com.example.currency;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.currency.Common.WorkThread;
import com.example.currency.WorkTask.SaveFileTask;

import java.util.Map;

/**
 * The activity to show all countries.
 */
public class CountryListActivity extends BaseActivity
{
    private MainModel mainModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (model == null)
        {
            model = new MainModel();
        }
        mainModel = (MainModel) model;

        updateView();

        Toast.makeText(context, "Press back button after you select countries.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void updateView()
    {
        super.updateView();

        final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout parent = findViewById(R.id.country_list_layout);

        for(Map.Entry<String, CurrencyInfo> entry : mainModel.getCurrencyInfos().entrySet())
        {
            final CurrencyInfo info = entry.getValue();

            Log.d("country name", info.getCountryShortName() + ", full name: " + info.getCountryFullName());
            LinearLayout countryItem = (LinearLayout)inflater.inflate(R.layout.country_item, null);
            TextView shortNameText = countryItem.findViewById(R.id.textViewShortName);
            shortNameText.setText(info.getCountryShortName());

            TextView fullNameText = countryItem.findViewById(R.id.textViewFullName);
            fullNameText.setText(info.getCountryFullName());

            final CheckBox checkbox = countryItem.findViewById(R.id.country_checkbox);
            checkbox.setChecked(info.getIsChecked());
            checkbox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                Log.d("country onclick", "on CheckBox clicked: ");
                info.setIsChecked(checkbox.isChecked());
                }
            });

            parent.addView(countryItem);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // save currency locally.
        SaveFileTask saveFileTask = new SaveFileTask(mainModel.getCurrencyInfos(), this, "country_list");
        WorkThread.getInstance().AddTask(saveFileTask);
    }
}
