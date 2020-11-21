package com.expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.expenses.databinding.ActivityMainListViewScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainListViewScreen extends AppCompatActivity implements IExpenseDialogResultCallBack, IAdapterItemClickListener, IShowStatType {

    public static final String LIST_DATA = "LIST_DATA";
    ActivityMainListViewScreenBinding activityMainListViewScreenBinding;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainListViewScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_list_view_screen);
        dbManager = new DBManager(this);
        dbManager.open();
        createHeader();
    }

    List<ExpenseInfo> mHeaderList;

    private void createHeader() {
        mHeaderList = dbManager.fetchCompleteData();
        if (mHeaderList != null && mHeaderList.size() > 0) {
            if (!SharedPreferenceManager.read(SharedPreferenceManager.IS_ANY_RECORD, false))
                SharedPreferenceManager.write(SharedPreferenceManager.IS_ANY_RECORD, true);
            activityMainListViewScreenBinding.incomeAmount.setText("Income " + mHeaderList.get(0).getTotalIncome() + "$");
            activityMainListViewScreenBinding.yearTv.setText(mHeaderList.get(0).getExpensedate());
            createData();
            setAdapterData(mHeader, mChildData);
        }
    }

    List<String> mHeader;
    HashMap<String, List<ExpenseType>> mChildData;

    private void createData() {
        String expenseDate = mHeaderList.get(0).getExpensedate();

        if (mHeader == null)
            mHeader = new ArrayList<>();
        mHeader.clear();
        mHeader.add(getString(R.string.regular));
        mHeader.add(getString(R.string.non_regular));
        if (mChildData == null)
            mChildData = new HashMap<>();
        mChildData.clear();
        List<ExpenseType> mExpenseTypeList = dbManager.fetchCompleteExpenseBasedOnType(expenseDate, mHeader.get(0));
        if (mExpenseTypeList == null)
            mExpenseTypeList = new ArrayList<>();

        mChildData.put(mHeader.get(0), mExpenseTypeList);

        List<ExpenseType> mExpenseTypeListForNonRegular = dbManager.fetchCompleteExpenseBasedOnType(expenseDate, mHeader.get(1));
        if (mExpenseTypeListForNonRegular == null)
            mExpenseTypeListForNonRegular = new ArrayList<>();

        mChildData.put(mHeader.get(1), mExpenseTypeListForNonRegular);
    }

    MainViewAdapter mainViewAdapter;

    private void setAdapterData(List<String> mHeader, HashMap<String, List<ExpenseType>> mChildData) {
        mainViewAdapter = new MainViewAdapter(MainListViewScreen.this, mHeader, mChildData, this);
        mainViewAdapter.updateCheckValue(ExpenseOpType.NONE);
        activityMainListViewScreenBinding.activityMainExpandableListView.setAdapter(mainViewAdapter);
    }


    Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        menu.findItem(R.id.doneMenu).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                mainViewAdapter.updateCheckValue(ExpenseOpType.ADD);
                break;
            case R.id.menu_update:
                if(isThereIsAnyData()) {
                    mainViewAdapter.updateCheckValue(ExpenseOpType.UPDATE);
                }
                break;
            case R.id.menu_delete:
                if(isThereIsAnyData()) {
                    mainViewAdapter.updateCheckValue(ExpenseOpType.DELETE);
                    mMenu.findItem(R.id.doneMenu).setVisible(true);
                }
                break;
            case R.id.menu_calculate_expense:
                if ( isThereIsAnyData()){
                String totalSpendAmount = dbManager.calculateAmount();
                Toast.makeText(MainListViewScreen.this, " Total expense is " + totalSpendAmount + " $", Toast.LENGTH_LONG).show();
            }
            break;
            case R.id.menu_check_stats:
                if (isThereIsAnyData()) {
                    DialogUtil.showStatDialog(this, this);
                }
                break;
            case R.id.doneMenu:
                deleteData();
                break;
        }
        return true;
    }

    private boolean isThereIsAnyData() {
        if (mChildData.get(mHeader.get(0)).size() == 0 && mChildData.get(mHeader.get(1)).size() == 0)
            return false;
        return true;
    }

    private void deleteData() {
        mainViewAdapter.updateCheckValue(ExpenseOpType.NONE);
        if (mListToDelete.size() > 0) {
            for (ExpenseType expenseType : mListToDelete) {
                dbManager.deleteData(expenseType);
                mainViewAdapter.deleteData(expenseType);
            }

        }
        mMenu.findItem(R.id.doneMenu).setVisible(false);
    }


    @Override
    public void saveExpense(boolean isUpdate, ExpenseType expenseType) {
        if (isUpdate)
            dbManager.updateExpense(expenseType);
        else
            dbManager.insertExpenseType(expenseType);

        createData();
        mainViewAdapter.updateCheckValue(ExpenseOpType.NONE);
        mMenu.findItem(R.id.doneMenu).setVisible(false);
    }

    List<ExpenseType> mListToDelete = new ArrayList<>();


    @Override
    public void addExpense(String header) {
        DialogUtil.showExpensePromptDialog(this, header, mHeaderList.get(0).getExpensedate(), false, null, this);
    }

    @Override
    public void updateExpenseInfo(boolean isChecked, ExpenseOpType mExpenseOpType, ExpenseType expenseType) {
        if (mExpenseOpType == ExpenseOpType.DELETE) {
            if (!isChecked) {
                if (mListToDelete.contains(expenseType))
                    mListToDelete.remove(expenseType);
            } else {
                if (!mListToDelete.contains(expenseType))
                    mListToDelete.add(expenseType);
            }
        } else {
            DialogUtil.showExpensePromptDialog(MainListViewScreen.this, expenseType.getExpenseType(), expenseType.getExpenseDate(), true, expenseType, this);
        }
    }

    @Override
    public void stateType(String value) {
        if(mChildData.get(value).size()>0) {
            Intent intent = new Intent(MainListViewScreen.this, ShowChartActvity.class);
            intent.putParcelableArrayListExtra(LIST_DATA, (ArrayList) mChildData.get(value));
            startActivity(intent);
        }
    }
}