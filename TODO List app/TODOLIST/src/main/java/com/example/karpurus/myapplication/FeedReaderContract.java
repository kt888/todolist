package com.example.karpurus.myapplication;

import android.provider.BaseColumns;

/**
 * Created by karpurus on 6/20/2016.
 */
public class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DATE = "due_date";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }

}
