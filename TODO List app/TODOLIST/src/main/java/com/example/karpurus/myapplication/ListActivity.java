package com.example.karpurus.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "itemName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button addButton = (Button) findViewById(R.id.addButton);
        // mDbHelper must be passed to this activity
        displayItemList();
        /* If the button with "+" in ListActivity is clicked
        *  AddUpdateView must be called
        */
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to add item activity
                Intent intent = new Intent(ListActivity.this, AddUpdateActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "add");
                startActivity(intent);

            }
        });

    }

    /*
    * This method iterates over all the items in the
    * database, retrieves it and an arraylist is populated
    * The contents of the arraylist are displayed using ListView
    */
    public void displayItemList(){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        final ListView itemList = (ListView) findViewById(R.id.listView);
        final ArrayList<String> items = new ArrayList<String>();
        /*
         *Iterating through the rows in database
         */
        while(!c.isAfterLast()) {
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)
            );
            String item = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)

            );
            /* Adding to the arraylist items*/
            items.add(item);
            c.moveToNext();
        }

        /* The arraylist obtained from the database is populated as a list */
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                items);

        /*
         * If an item in the list is clicked
         * TaskActivity is called. This activity gives the user
         * to update or delete the task
         */
        itemList.setAdapter(arrayAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ListActivity.this, TaskActivity.class);
                String nameOfItemClicked = items.get(position);
                intent.putExtra(EXTRA_MESSAGE, nameOfItemClicked);
                startActivity(intent);
            }
        });

    }
}
