package com.example.karpurus.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.Calendar;

public class AddUpdateActivity extends AppCompatActivity {


    private boolean isUpdate = false;
    private int year, month, day;
    private Calendar calendar;
    private TextView dateView;
    int date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        final EditText itemBar = (EditText) findViewById(R.id.itemAfterRetrieval);
        Button submit = (Button) findViewById(R.id.submit);
        final Spinner spinner = (Spinner) findViewById(R.id.prioritySpinner);
        final TextView dueDate = (TextView) findViewById(R.id.dueDate);
        dateView = (TextView) findViewById(R.id.dateView);
        /* Spinner for priority*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        /* alert dialogue for due date */

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
        String message = "" + intent.getStringExtra(ListActivity.EXTRA_MESSAGE);
        final String oldItem = intent.getStringExtra(TaskActivity.ITEM);
        ;
        if (!message.equals("add")) {
            /* This populates the old Items
            * which are then updated by the user .*/
            final String priority = intent.getStringExtra(TaskActivity.PRIORITY);
            final String dateString = intent.getStringExtra(TaskActivity.DATE);
            ;
            setValues(itemBar, oldItem, spinner, priority,dateString);
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
                String newItem = itemBar.getText().toString();
                String priority = (String) spinner.getSelectedItem();
                if (isUpdate == true) {
                    updateItem(oldItem, db, newItem, priority);
                } else
                    addItem(newItem, db, priority);
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
    public void addItem(String itemName, SQLiteDatabase db, String priority) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, itemName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PRIORITY, priority);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void updateItem(String oldItem, SQLiteDatabase db, String itemName, String priority) {
        /* New value for one column */
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, itemName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PRIORITY, priority);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);

        /* Which row to update, based on the ID */
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {oldItem};

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void setValues(EditText itemBar, String oldItem, Spinner spinner, String priority, String dateString) {
        isUpdate = true;
        itemBar.setText(oldItem);
        itemBar.setSelection(oldItem.length());
        dateView.setText(dateString);
        int position = 0;
        switch (priority) {
            case "high":
                position = 0;
                break;
            case "low":
                position = 1;
                break;
            case "medium":
                position = 2;
                break;
        }
        spinner.setSelection(position);
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        String dayString = Integer.toString(day);
        if(dayString.length() == 1)
            dayString = 0+dayString;
        String dateString = year+""+month+""+dayString;
        date = Integer.parseInt(dateString);
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
}