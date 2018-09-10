package com.seffyo.kandaapptesting.activities.adapter.adapters;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.seffyo.kandaapptesting.activities.adapter.adapters.Grocery;
import com.seffyo.kandaapptesting.activities.adapter.adapters.GroceryAdapter;
import com.seffyo.kandaapptesting.R;

import java.util.ArrayList;

public class AdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_acitivty);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("ADAPTER");
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Create a list of words
        ArrayList<Grocery> groceries = new ArrayList<>();
        groceries.add(new Grocery("Bread", "2"));
        groceries.add(new Grocery("Juice", "4"));
        groceries.add(new Grocery("Ketchup", "2"));
        groceries.add(new Grocery("Salad", "5"));
        groceries.add(new Grocery("Pizza", "3"));
        groceries.add(new Grocery("Coke", "2"));
        groceries.add(new Grocery("Pineapple", "5"));
        groceries.add(new Grocery("Ice Cream", "6"));
        groceries.add(new Grocery("Tomato", "1"));
        groceries.add(new Grocery("Pepper", "4"));
        groceries.add(new Grocery("Diapers", "4"));
        groceries.add(new Grocery("Beef", "5"));
        groceries.add(new Grocery("Taco", "1"));

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        GroceryAdapter groceriesAdapter = new GroceryAdapter(this, groceries);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list_grocery);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(groceriesAdapter);
    }
}
