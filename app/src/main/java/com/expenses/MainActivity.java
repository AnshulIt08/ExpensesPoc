package com.expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.expenses.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mActivityMainBinding;
    private DBManager dbManager;
    private String[] monthArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dbManager = new DBManager(this);
        dbManager.open();
        monthArray = getResources().getStringArray(R.array.month);
        setMonthAdapter();
    }


    private void setMonthAdapter() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, monthArray);
        mActivityMainBinding.activityMainMonthSpinner.setAdapter(mAdapter);
        mActivityMainBinding.activityMainMonthSpinner.setSelection(0);
    }

    public void doneClick(View view) {
        if (validateData()) {
            saveDataToDb();
            startActivity(new Intent(MainActivity.this, MainListViewScreen.class));
            finish();
        }
    }

    private void saveDataToDb() {
        dbManager.insert(createExpenseInfo());
    }

    private ExpenseInfo createExpenseInfo() {
        ExpenseInfo expenseInfo = new ExpenseInfo();
        expenseInfo.setExpensedate(monthArray[mActivityMainBinding.activityMainMonthSpinner.getSelectedItemPosition()]+", "+mActivityMainBinding.activityMainYearEt.getText().toString().trim());
        expenseInfo.setTotalIncome(mActivityMainBinding.activityMainIncomeEt.getText().toString().trim());
        return expenseInfo;
    }

    private boolean validateData() {
        if (TextUtils.isEmpty(mActivityMainBinding.activityMainYearEt.getText().toString().trim())) {
            showMessage(getString(R.string.empty_year));
            return false;
        } else if (!isValidYear()) {
            showMessage(getString(R.string.not_valid_year));
            return false;
        } else if (TextUtils.isEmpty(mActivityMainBinding.activityMainIncomeEt.getText().toString().trim())) {
            showMessage(getString(R.string.empty_income));
            return false;
        } else
            return true;
    }

    private boolean isValidYear() {
        int year = Integer.parseInt(mActivityMainBinding.activityMainYearEt.getText().toString().trim());
        return 2020 <= year;

    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }
}