package com.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class DBManager {
    private DatabaseHandler dbHandler;

    private Context context;

    private SQLiteDatabase mDatabase;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHandler = new DatabaseHandler(context);
        mDatabase = dbHandler.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHandler.close();
    }

    public void insert(ExpenseInfo expenseInfo) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHandler.DATE_FIELD, expenseInfo.getExpensedate());
        contentValue.put(DatabaseHandler.TOTAL_INCOME, expenseInfo.getTotalIncome());
        mDatabase.insert(DatabaseHandler.TABLE_NAME, null, contentValue);
    }

 /*   public List<ExpenseInfo> fetchExpenseInfoData()
    {
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursorExpense = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHandler.TABLE_NAME, null);
        List<ExpenseInfo> mList = new ArrayList<>();
        if (cursorExpense.moveToFirst()) {
            do {
                mList.add(createEmployeeData(cursorExpense));
            } while (cursorExpense.moveToNext());
        }
        //closing the cursor
        cursorExpense.close();
        return mList;
    }*/

   /* public List<ExpenseInfo> fetchCompleteData(String date) {
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursorExpense = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHandler.TABLE_NAME+ " WHERE "+DatabaseHandler.DATE_FIELD+ "= ? ", new String[]{date});
        List<ExpenseInfo> mList = new ArrayList<>();
        if (cursorExpense.moveToFirst()) {
            do {
                mList.add(createEmployeeData(cursorExpense));
            } while (cursorExpense.moveToNext());
        }
        //closing the cursor
        cursorExpense.close();
        return mList;
    }*/

    public List<ExpenseInfo> fetchCompleteData() {
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursorExpense = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_NAME, null);
        List<ExpenseInfo> mList = new ArrayList<>();
        if (cursorExpense.moveToFirst()) {
            do {
                mList.add(createExpenseData(cursorExpense));
            } while (cursorExpense.moveToNext());
        }
        //closing the cursor
        cursorExpense.close();
        return mList;
    }

    private ExpenseInfo createExpenseData(Cursor cursorExpense) {
        ExpenseInfo expenseInfo = new ExpenseInfo();
        expenseInfo.setExpensedate(cursorExpense.getString(1));
        expenseInfo.setTotalIncome(cursorExpense.getString(2));
        return expenseInfo;
    }

    public List<ExpenseType> fetchCompleteExpenseBasedOnType(String expenseDate, String expenseType) {
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursorExpense = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHandler.EXPENSE_INFO_TABLE_NAME + " WHERE " + DatabaseHandler.DATE_FIELD + "= ? " + " AND " + DatabaseHandler.EXPENSE_TYPE + "= ? ", new String[]{expenseDate, expenseType});
        List<ExpenseType> mList = new ArrayList<>();
        if (cursorExpense.moveToFirst()) {
            do {
                mList.add(createExpenseTypeData(cursorExpense));
            } while (cursorExpense.moveToNext());
        }
        //closing the cursor
        cursorExpense.close();
        return mList;
    }

    private ExpenseType createExpenseTypeData(Cursor cursorExpense) {
        ExpenseType expenseType = new ExpenseType();
        expenseType.setExpenseId(cursorExpense.getInt(0));
        expenseType.setExpenseName(cursorExpense.getString(1));
        expenseType.setExpenseAmount(cursorExpense.getInt(2));
        expenseType.setExpenseDate(cursorExpense.getString(3));
        expenseType.setCurrentTimeStamp(cursorExpense.getString(4));
        expenseType.setExpenseType(cursorExpense.getString(5));
        return expenseType;
    }

    public void insertExpenseType(ExpenseType expenseType) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHandler.NAME_FIELD, expenseType.getExpenseName());
        contentValue.put(DatabaseHandler.VALUE_FIELD, expenseType.getExpenseAmount());
        contentValue.put(DatabaseHandler.DATE_FIELD, expenseType.getExpenseDate());
        contentValue.put(DatabaseHandler.CURRENT_TIMESTAMP, ""+Calendar.getInstance().getTimeInMillis());
        contentValue.put(DatabaseHandler.EXPENSE_TYPE, expenseType.getExpenseType());
        mDatabase.insert(DatabaseHandler.EXPENSE_INFO_TABLE_NAME, null, contentValue);
    }

    public void deleteData(ExpenseType expenseType) {
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + DatabaseHandler.EXPENSE_INFO_TABLE_NAME + " WHERE id = " + expenseType.getExpenseId());
    }

    public void updateExpense(ExpenseType expenseType) {
        String timeStamp = expenseType.getCurrentTimeStamp();
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHandler.NAME_FIELD, expenseType.getExpenseName());
        contentValue.put(DatabaseHandler.VALUE_FIELD, expenseType.getExpenseAmount());
        contentValue.put(DatabaseHandler.DATE_FIELD, expenseType.getExpenseDate());
        contentValue.put(DatabaseHandler.CURRENT_TIMESTAMP, ""+Calendar.getInstance().getTimeInMillis());
        contentValue.put(DatabaseHandler.EXPENSE_TYPE, expenseType.getExpenseType());
        sqLiteDatabase.update(DatabaseHandler.EXPENSE_INFO_TABLE_NAME,contentValue,DatabaseHandler.CURRENT_TIMESTAMP+" =? ",new String[]{timeStamp});
    }

    public String calculateAmount() {
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ DatabaseHandler.VALUE_FIELD +") as Total FROM "+DatabaseHandler.EXPENSE_INFO_TABLE_NAME,null);
        if(cursor.moveToFirst())
          return String.valueOf( cursor.getInt(0));
        return "";
    }
    /*

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.SUBJECT, name);
        contentValues.put(DatabaseHandler.DESC, desc);
        int i = database.update(DatabaseHandler.TABLE_NAME, contentValues, DatabaseHandler._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHandler.TABLE_NAME, DatabaseHandler._ID + "=" + _id, null);
    }
*/

}
