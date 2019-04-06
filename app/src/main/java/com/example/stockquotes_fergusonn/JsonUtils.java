package com.example.stockquotes_fergusonn;

import android.util.Log;
import org.json.JSONObject;

/**
 * Created by kbriggs on 3/5/18.
 */

public class JsonUtils {

    public static JSONObject parseStockQuoteJson(String json) {
        JSONObject quote = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            quote = jsonObject.getJSONObject("quote");
            Log.i("JSON", quote.toString());

        } catch (Exception ex) {
            Log.e("JSON", "Error parsing JSON");
        }
        return quote;
    }
}
