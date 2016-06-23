package com.example.karpurus.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;

import java.sql.Date;

public class AddUpdateActivity extends AppCompatActivity {


    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        final EditText itemBar = (EditText) findViewById(R.id.itemAfterRetrieval);
        Button submit = (Button)findViewById(R.id.submit);

        /*
        *  Create database object
        */
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        /*
        * This decides if AddUpdateActivity was called for add
        * or update. Actions are taken accordingly
        */
        final Intent intent = getIntent();
        /*
        * This message tells AddUpdateActivity if it is called for add
        * or update. In case it is for update, the name of the item from
        * TaskActivity is passed. This name is used to populate the fields
        * and retrieve other details from database
        */
        String message = ""+intent.getStringExtra(ListActivity.EXTRA_MESSAGE);
        final String oldItem = intent.getStringExtra(TaskActivity.ITEM);;
        if(!message.equals("add")) {
            /* This populates the old Items
            * which are then updated by the user .*/
            setValues(itemBar, oldItem);
        }
        assert submit != null;
        /*
        * When the submit button is clicked
        * add or update is called
        */

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* All the values are
                 * obtained from the widgets and accordingly added or updated.
                 */
                String  newItem = itemBar.getText().toString();
                if(isUpdate == true){
                    updateItem(oldItem, db, newItem );
                }
                else
                    addItem(newItem, db);
                isUpdate = false;
                /*
                 * List view is displayed to
                 * show that the item has been added
                 */
                Intent intent = new Intent(AddUpdateActivity.this, ListActivity.class);
                startActivity(intent);

            }
        });

    }

    public void addItem(String itemName, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, itemName);
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void updateItem(String oldItem, SQLiteDatabase db, String itemName){
        /* New value for one column */
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, itemName);
        /* Which row to update, based on the ID */
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { oldItem };

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void setValues(EditText itemBar, String oldItem){
        isUpdate = true;
        itemBar.setText(oldItem);
        itemBar.setSelection(oldItem.length());
    }

    public void getValues(){

    }

}
