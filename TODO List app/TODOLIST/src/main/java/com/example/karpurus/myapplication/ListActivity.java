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
    public final static String EXTRA_MESSAGE_IN = "itemName";
    public final static String EXTRA_MESSAGE_IP = "itemPriority";
    public final static String EXTRA_MESSAGE_ID = "itemDate";
    public final static String EXTRA_MESSAGE = "from";
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
    *This method iterates over all the items in the
    * database, retrieves it and an arraylist is populated
    * The contents of the arraylist are displayed using ListView
    */
    public void displayItemList(){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIORITY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
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
        final ArrayList<toDoItem> items = new ArrayList<toDoItem>();
        /*
         *Iterating through the rows in database
         */
        while(!c.isAfterLast()) {
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)
            );
            String item = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)

            );
            String priority = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PRIORITY)

            );
            int date = c.getInt(
                    c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE)
            );

            /* Adding to the arraylist items*/
            toDoItem itemObject = new toDoItem();
            itemObject.itemName = item;
            itemObject.priority = priority;
            itemObject.dueDate = convertDateToString(date);
            items.add(itemObject);
            c.moveToNext();
        }

        /* The arraylist obtained from the database is populated as a list */
        ItemAdapter arrayAdapter = new ItemAdapter(this, items);

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
                int positionOfItemClicked = itemList.getSelectedItemPosition();
                String nameOfItemClicked = items.get(position).itemName;
                String priorityOfItemClicked = items.get(position).priority;
                String dueDate = items.get(position).dueDate;
                intent.putExtra(EXTRA_MESSAGE_IN, nameOfItemClicked);
                intent.putExtra(EXTRA_MESSAGE_IP, priorityOfItemClicked);
                intent.putExtra(EXTRA_MESSAGE_ID, dueDate);
                startActivity(intent);
            }
        });

    }

    public String convertDateToString(int date){
        StringBuffer dateString = new StringBuffer(Integer.toString(date));
        dateString.insert(4,'/');
        dateString.insert(dateString.length()-2,'/');
        return dateString.toString();
    }
}
