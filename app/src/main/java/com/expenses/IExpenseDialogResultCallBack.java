package com.expenses;

interface IExpenseDialogResultCallBack {
    void saveExpense(boolean isUpdate, ExpenseType expenseType);
}
