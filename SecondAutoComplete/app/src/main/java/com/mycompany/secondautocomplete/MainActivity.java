package com.mycompany.secondautocomplete;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;


public class MainActivity extends ActionBarActivity {

    private AutoCompleteTextView actv;
    private MultiAutoCompleteTextView mactv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // code for autocomplete
        String[] countries = getResources().
                getStringArray(R.array.list_of_countries);
        ArrayAdapter adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1,countries);


        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        mactv = (MultiAutoCompleteTextView) findViewById
                (R.id.multiAutoCompleteTextView1);

        actv.setAdapter(adapter);
        mactv.setAdapter(adapter);

        mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
