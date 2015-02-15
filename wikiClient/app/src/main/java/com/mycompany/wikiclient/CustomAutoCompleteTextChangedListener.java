package com.mycompany.wikiclient;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    List<String> suggestions;
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    public void getWiki(CharSequence userInput, MainActivity mainActivity) {
         try {
            String url = getWikiUrl(userInput.toString());
            if (url != null) {
                // get request using Volley
                //RequestQueue queue = Volley.newRequestQueue(this);
                AsyncTask<String, String, String> output = new WikiGet().execute(url);
                //mainActivity.item = MainActivity.convertListToArray(suggestions);
            } else {
                mainActivity.item = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // if you want to see in the logcat what the user types
        Log.e(TAG, "User input: " + userInput);

        MainActivity mainActivity = ((MainActivity) context);
        // query the database based on the user input
        //mainActivity.item = MainActivity.getItemsFromList(userInput.toString(), mainActivity.getItemList());
        mainActivity.item = mainActivity.getItemsFromTrie(userInput.toString());
        mainActivity.myAdapter.notifyDataSetChanged();
        mainActivity.myAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);
    }

    private String getWikiUrl(String userInput) throws IOException, JSONException {
        /*if (userInput.length() < 2) {
            return null;
        } */
        String url = "http://en.wikipedia.org/w/api.php?action=opensearch&search=" + userInput + "&format=json&callback=spellcheck";
        return url;
        //return new WikiGet().execute(url);
        //return MainActivity.convertListToArray(suggestions);
    }

    class WikiGet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient wikiClient = new DefaultHttpClient();
            HttpGet wikiGet = new HttpGet(url);
            String response = null;
            try {
                response = wikiClient.execute(wikiGet, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            suggestions = new ArrayList<String>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            if (jsonArray != null) {
                JSONArray firstElement = null;
                try {
                    firstElement = jsonArray.getJSONArray(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int numb_elements = firstElement.length();
                int current_index = 0;
                StringBuilder sb = new StringBuilder("Number of items returned: ").append(numb_elements);
                Log.e(TAG, sb.toString());
                while (current_index < numb_elements) {
                    String currentSuggestion = null;
                    try {
                        currentSuggestion = firstElement.getString(current_index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    suggestions.add(currentSuggestion);
                }
            }
            return null;
        }

    }

}
