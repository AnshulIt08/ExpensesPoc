package com.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

class DialogUtil {
    public static void showExpensePromptDialog(final Context context, final String expenseTypeValue, final String expenseDate, final boolean isUpdate, final ExpenseType expenseType, final IExpenseDialogResultCallBack iExpenseDialogResultCallBack) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.prompt_input_expense, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);

        final EditText expenseNameEt = (EditText) promptsView
                .findViewById(R.id.expense_name);
        if(isUpdate)
            expenseNameEt.setText(expenseType.getExpenseName());
        final EditText expenseValueEt = (EditText) promptsView
                .findViewById(R.id.expense_value);
        if(isUpdate)
            expenseValueEt.setText(String.valueOf(expenseType.getExpenseAmount()));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExpenseType expenseType1 = isValidData(context, expenseNameEt.getText().toString().trim(), expenseValueEt.getText().toString().trim(), expenseTypeValue, expenseDate,getTimeStamp(expenseType));
                        if (expenseType1 != null) {
                            iExpenseDialogResultCallBack.saveExpense(isUpdate, expenseType1);
                            dialog.dismiss();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private static String getTimeStamp(ExpenseType expenseType) {
        if(expenseType != null)
         return expenseType.getCurrentTimeStamp() == null ? String.valueOf(Calendar.getInstance().getTimeInMillis()):expenseType.getCurrentTimeStamp();
     return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    private static ExpenseType isValidData(Context context, String name, String value, String expenseType, String expenseDate,String createTime) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value)) {
            Toast.makeText(context, "Please Enter Valid data", Toast.LENGTH_LONG).show();
            return null;
        }

        return createData(name, value, expenseDate, expenseType,createTime);
    }

    private static ExpenseType createData(String name, String value, String date, String expenseTypevalue,String createTime) {
        ExpenseType expenseType = new ExpenseType();
        expenseType.setExpenseName(name);
        expenseType.setExpenseAmount(Integer.parseInt(value));
        expenseType.setExpenseType(expenseTypevalue);
        expenseType.setExpenseDate(date);
        expenseType.setCurrentTimeStamp(createTime);
        return expenseType;
    }


    public static void showStatDialog(Context context, final IShowStatType iShowStatType) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.show_state_dialo, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Which expense type's Stat you want to show");
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.parent_state);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                if(radioButton.isChecked())
                    iShowStatType.stateType(radioButton.getText().toString());
                alertDialog.dismiss();
            }
        });

        // show it
        alertDialog.show();
    }
}
