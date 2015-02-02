package com.mycompany.autocompletefromdb;

import java.util.ArrayList;
import java.util.List;

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

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

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

        itemList.add("indian coffee");
        itemList.add("new zealand");
        itemList.add("view coffee");

    }

    public String[] getItemsFromList(String searchTerm) {
        List<String> outputItems = new ArrayList<String>();
        for (String item : itemList) {
            if (item.contains(searchTerm)) {
                outputItems.add(item);
            }

        }
        String[] output = new String[outputItems.size()];
        int outputIndex = 0;
        for (String outputItem : outputItems) {
            output[outputIndex] = outputItem;
            ++outputIndex;
        }
        return output;
    }

}
