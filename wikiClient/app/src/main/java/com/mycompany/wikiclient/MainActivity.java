package com.mycompany.wikiclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

    /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
    public static final String TAG = "MainActivity";
    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // option 2, just a linear list of items
    private List<String> itemList;

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

    // TODO: remove this
    private static ArrayList<String> COUNTRIES = new ArrayList<String>(
            Arrays.asList("united states", "india", "mexico", "ireland", "sri lanka"));

    private Map<String, String> postalCodeToCityName;
    PatriciaTrie<String, String> trie = new PatriciaTrie<String, String>(new CharSequenceKeyAnalyzer());

    public List<String> getItemList() {
        return itemList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            // instantiate database handler
            itemList = new ArrayList<String>();
            postalCodeToCityName = new HashMap<String, String>();

            // put sample data to database
            //insertSampleData();
            insertDataFromFile();

            // autocompletetextview is in activity_main.xml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read data from raw resource and load internal data structure
     * File is assumed to be csv, with postal_code, city name format
     */
    public void insertDataFromFile() throws IOException {
        //int resourceId = getResources().getIdentifier("cities", "raw", getPackageName());
        InputStream inputStream = getResources().openRawResource(R.raw.city_zip);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Log.i(TAG, "loading file ...");
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(",");
            StringBuilder sb = new StringBuilder(words[0]).append("  -  ").append(words[1]).append(", ").append(words[2]);
            postalCodeToCityName.put(words[0], words[1]);
            trie.put(words[0], sb.toString());
            itemList.add(words[0]);
        }
        Log.i(TAG, "Finished loading file");
    }

    public void insertSampleData(){

        // for trie do a basic analyzer to split the keywords
        for (String country : COUNTRIES) {
            itemList.add(country);
            String[] countryTerms = country.split(" ");
        }

    }

    public static String[] convertListToArray(List<String> outputItems) {
        String[] output = new String[outputItems.size()];
        int outputIndex = 0;
        for (String outputItem : outputItems) {
            output[outputIndex] = outputItem;
            ++outputIndex;
        }
        return output;
    }

    public static String[] getItemsFromList(String searchTerm, List<String> itemList) {
        List<String> outputItems = new ArrayList<String>();
        for (String item : itemList) {
            if (item.contains(searchTerm)) {
                outputItems.add(item);
            }
        }
        String[] output = convertListToArray(outputItems);
        return output;
    }

    public String[] getItemsFromTrie(String searchTerm) {
        Map<String, String> itemsFromTrie = trie.getPrefixedBy(searchTerm);
        List<String> matches = new ArrayList<String>();
        for (Map.Entry<String, String> stringStringEntry : itemsFromTrie.entrySet()) {
            matches.add(stringStringEntry.getValue());
        }
        return convertListToArray(matches);
    }



}
