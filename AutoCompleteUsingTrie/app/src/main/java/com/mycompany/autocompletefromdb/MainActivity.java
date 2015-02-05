package com.mycompany.autocompletefromdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

    /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // option 2, just a linear list of items
    List<String> itemList;

    PatriciaTrie<String, String> trie = new PatriciaTrie<String, String>(new CharSequenceKeyAnalyzer());

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

    private static ArrayList<String> COUNTRIES = new ArrayList<String>(
            Arrays.asList("united states", "india", "mexico", "ireland", "sri lanka"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            // instantiate database handler
            itemList = new ArrayList<String>();

            // put sample data to database
            insertSampleData();

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

    public void insertSampleData(){

        // for trie do a basic analyzer to split the keywords
        for (String country : COUNTRIES) {
            itemList.add(country);
            String[] countryTerms = country.split(" ");
            for (String countryTerm : countryTerms) {
                trie.put(countryTerm, country);
            }
        }

    }

    private String[] convertListToArray(List<String> outputItems) {
        String[] output = new String[outputItems.size()];
        int outputIndex = 0;
        for (String outputItem : outputItems) {
            output[outputIndex] = outputItem;
            ++outputIndex;
        }
        return output;
    }

    public String[] getItemsFromList(String searchTerm) {
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
