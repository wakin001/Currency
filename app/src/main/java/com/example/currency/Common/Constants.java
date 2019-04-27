package com.example.currency.Common;

/**
 * Constants definition
 */
public class Constants
{
    // the url to request country list.
    public static final String currencyListUrlPath = "http://apilayer.net/api/list?access_key=d82ba2a0ca1decde625c0d080bc52041";

    // Used in passing data between activities.
    public static final String SerializeBaseModel = "SerializeBaseModel";
    // Used in passing data between activities.
    public static final String Bundler = "Bundler";

    // handler messages
    public static final int Msg_Http_Country_List = 1;
    public static final int Msg_Http_Currency = 2;
    public static final int MSG_SAVE_FILE = 3;
    public static final int MSG_LOAD_FILE = 4;

    // SharedPreferences name
    public static final String Pref_Name = "currency_pref";
    public static final String Pref_Http_Request_Time = "get_currency_status_time";
}
