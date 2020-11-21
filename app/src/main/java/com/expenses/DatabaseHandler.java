package com.expenses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHandler extends SQLiteOpenHelper {


    public static final String DATE_FIELD = "DATE_FIELD";
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "ExpenseDataBase";
    public static final String TABLE_NAME = "ExpenseTable";
    public static final String EXPENSE_INFO_TABLE_NAME = "ExpenseInfoTable";

    public static String NAME_FIELD = "name";
    public static String CURRENT_TIMESTAMP = "currentTimeStamp";
    public static String VALUE_FIELD = "value";
    public static String TOTAL_INCOME = "total_income";
    public static String EXPENSE_TYPE = "expense_type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +TABLE_NAME+
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_FIELD+" TEXT, " +
                TOTAL_INCOME+" TEXT ) ";

        String sql_for_expense = "CREATE TABLE " +EXPENSE_INFO_TABLE_NAME+
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_FIELD+" TEXT, " +
                VALUE_FIELD+" TEXT, " +
                DATE_FIELD+" TEXT, " +
                CURRENT_TIMESTAMP+" TEXT, " +
                EXPENSE_TYPE+" TEXT ) ";
        db.execSQL(sql);
        db.execSQL(sql_for_expense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public SQLiteDatabase getReadableObject()
    {
       return this.getReadableDatabase();
    }
}
