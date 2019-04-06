package com.example.stockquotes_fergusonn;

import android.util.Log;

import org.json.JSONObject;

import java.net.*;
import java.io.*;


public class Stock implements Serializable
{
    private static final boolean DEBUG = true;

    private static final String TAG_PREFIX = "stockquotes";

    private String symbol;
    private String lastTradeTime;
    private String lastTradePrice;
    private String change;
    private String range;
    private String name;


    public Stock(String symbol)
    {
        this.symbol = symbol.toUpperCase();

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock()", "symbol = " + symbol);
    }


    public void load() throws MalformedURLException, IOException {

        System.out.println("opneing url");
        URL url = new URL("https://api.iextrading.com/1.0/stock/" + symbol + "/book");
        System.out.println(url);

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock.load()", "url = " + url);

        URLConnection connection = url.openConnection();

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock.load()", "url connection opened");

        InputStreamReader isr = new InputStreamReader((connection.getInputStream()));

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock.load()", "input stream reader created");

        BufferedReader in = new
                BufferedReader(isr);

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock.load()", "buffered reader created");

        String line = in.readLine();

        if (DEBUG)
            Log.i(TAG_PREFIX + "Stock.load()", "line = " + line);


        // consume any data remaining in the input stream
        while (in.readLine() != null)
            ;

        in.close();

        if (line != null && line.length() > 0)
        {
            // parse the JSON
            JSONObject stock = JsonUtils.parseStockQuoteJson(line);

            try {
                symbol = stock.getString("symbol");
                lastTradeTime = stock.getString("latestTime");
                lastTradePrice = stock.getString("latestPrice");
                change = stock.getString("change");
                range = stock.getString("week52Low") + " - " + stock.getString("week52High");
                name = stock.getString("companyName");
            } catch (Exception ex) {
                Log.e(TAG_PREFIX, "Error retrieving data from JSON");
            }

            if (DEBUG)
                Log.i(TAG_PREFIX + "Stock.load()", "name = " + name);

        }
    }


    /**
     * Returns the stock's last trade time.
     */
    public String getLastTradeTime()
    {
        return  lastTradeTime;
    }


    /**
     * Returns the stock's last trade price.
     */
    public String getLastTradePrice()
    {
        return lastTradePrice;
    }


    /**
     * Returns the stock's .
     */
    public String getChange()
    {
        return change;
    }


    /**
     * Returns the stock's 52-week range.
     */
    public String getRange()
    {
        return range;
    }


    /**
     * Returns the stock's name; e.g., Google, Inc.
     */
    public String getName()
    {
        return name;
    }


    /**
     * Returns the stock's symbol; e.g., GOOG.
     */
    public String getSymbol()
    {
        return symbol;
    }
}