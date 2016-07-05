package com.example.karpurus.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "fromUpdate";
    public final static String ITEM = "itemName";
    public final static String PRIORITY = "itemPriority";
    public final static String DATE = "itemDate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        final String itemName = intent.getStringExtra(ListActivity.EXTRA_MESSAGE_IN);
        final String priority = intent.getStringExtra(ListActivity.EXTRA_MESSAGE_IP);
        final String dueDate = intent.getStringExtra(ListActivity.EXTRA_MESSAGE_ID);
        final TextView itemNameField = (TextView) findViewById(R.id.itemNameAfterRetrival);
        final TextView itemPriorityField = (TextView) findViewById(R.id.itemPriorityAfterRetrieval);
        final TextView itemDateField = (TextView) findViewById(R.id.itemDateAfterRetrieval);
        Button deleteButton = (Button) findViewById(R.id.delete);
        Button updateButton = (Button) findViewById(R.id.update);
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // populate the field with the item name
        itemNameField.setText(itemName);
        itemPriorityField.setText(priority+" priority");
        itemDateField.setText(dueDate);
        // call delete method if delete button is clicked
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(itemName, db);
                // List view is displayed to
                // show that the item has been deleted
                Intent intent = new Intent(TaskActivity.this, ListActivity.class);
                startActivity(intent);

            }
        });

        // call update method if update button is clicked
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  updateItem(itemField, db, message);
                // List view is displayed to
                // show that the item has been updated
                Intent intent = new Intent(TaskActivity.this, AddUpdateActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "update");
                intent.putExtra(ITEM, itemName);
                intent.putExtra(PRIORITY, priority);
                intent.putExtra(DATE, dueDate);
                startActivity(intent);

            }
        });

    }

    public void updateItem(EditText itemField, SQLiteDatabase db, String itemName){
        String newItem = itemField.getText().toString();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newItem);
        // Which row to update, based on the ID
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { itemName };

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void deleteItem(String itemName, SQLiteDatabase db){
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { itemName };
        // Issue SQL statement.
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

}
