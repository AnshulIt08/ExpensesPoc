package com.expenses;

interface IAdapterItemClickListener {
    void addExpense(String header);
    void updateExpenseInfo(boolean isChecked, ExpenseOpType mExpenseOpType, ExpenseType expenseType);

}
