package com.example.currency;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The currency information structure.
 */
class CurrencyInfo implements Serializable
{
    private String countryShortName = "";
    private String countryFullName = "";
    private boolean isChecked = false;
    private double currency = 0f;

    public CurrencyInfo(String shortName, String fullName, boolean isSelected, double currency)
    {
        this.countryShortName = shortName;
        this.countryFullName = fullName;
        this.isChecked = isSelected;
        this.currency = currency;
    }

    public String getCountryShortName()
    {
        return countryShortName;
    }

    public String getCountryFullName()
    {
        return countryFullName;
    }

    public void setIsChecked(boolean value)
    {
        isChecked = value;
    }

    public boolean getIsChecked()
    {
        return isChecked;
    }

    public double getCurrency()
    {
        return currency;
    }

    public void setCurrency(double value)
    {
        currency = value;
    }
}

/**
 * The model to handle currency information in MainActivity and CountryListActivity.
 */
public class MainModel extends BaseModel
{
    private HashMap<String, CurrencyInfo> currencyInfos = new HashMap<String, CurrencyInfo>();

    public final String baseCountry = "USD";

    public MainModel()
    {
    }

    public HashMap<String, CurrencyInfo> getCurrencyInfos()
    {
        return currencyInfos;
    }

    public void AddCountryInfoes(HashMap<String, CurrencyInfo> infoes)
    {
        this.currencyInfos.putAll(infoes);
    }

    /**
     * Parse country list from json data.
     * @param jsonObject
     */
    public void parseJSONObjectCountryList(JSONObject jsonObject)
    {
        try
        {
            JSONObject stateList = jsonObject.getJSONObject("currencies");
            if (stateList != null)
            {
                Map<String, String> map = new HashMap<String, String>();

                Iterator<String> keysItr = stateList.keys();
                while (keysItr.hasNext())
                {
                    String key = keysItr.next();
                    String value = stateList.getString(key);
                    CurrencyInfo info = new CurrencyInfo(key, value, false, 0f);
                    if (!currencyInfos.containsKey(key))
                    {
                        currencyInfos.put(key, info);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Parse currency status information from json data.
     * @param jsonObject
     */
    public void parseJSONObjectCurrencyStatus(JSONObject jsonObject)
    {
        try
        {
            JSONObject stateList = jsonObject.getJSONObject("quotes");
            if (stateList != null)
            {
                Map<String, String> map = new HashMap<String, String>();

                Iterator<String> keysItr = stateList.keys();
                while (keysItr.hasNext())
                {
                    String key = keysItr.next();
                    double value = stateList.getDouble(key);
                    String countryShortName = key.replace(baseCountry, "");
                    if (currencyInfos.containsKey(countryShortName))
                    {
                        CurrencyInfo info = currencyInfos.get(countryShortName);
                        info.setCurrency(value);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
