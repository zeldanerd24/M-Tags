package com.kevinrothenberger.m_tags.utils;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kevinrothenberger on 11/20/14.
 */
public class API {

    static final String base_url = "http://cis-linux2.temple.edu/~tud14472/M-Tags/api/";

    private static String callAPI(Context context, String api) throws Exception {

        AndroidHttpClient client = AndroidHttpClient.newInstance("Android", context);
        HttpResponse response;

        HttpGet method = new HttpGet(base_url + api);
        response = client.execute(method);

        String responseJSON = extractHttpResponse(response);

        Log.i("API Response", responseJSON);

        client.close();

        return responseJSON;

    }

    private static String extractHttpResponse(HttpResponse response) throws Exception {

        InputStream stream = response.getEntity().getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder responseJSON = new StringBuilder();
        String line = "";

        while((line = reader.readLine()) != null) {
            responseJSON.append(line);
        }

        return responseJSON.toString();

    }

    public static JSONArray getMuseumItems(Context context) throws Exception {

        String responseJSON = callAPI(context, "listItems.php");

        try {
            return new JSONArray(responseJSON);

        } catch (JSONException e) {
            Log.i("JSON Error", responseJSON);
            e.printStackTrace();
        }

        return null;

    }

}
