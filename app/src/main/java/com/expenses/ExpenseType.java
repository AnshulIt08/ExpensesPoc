package com.expenses;

import android.os.Parcel;
import android.os.Parcelable;

class ExpenseType implements Parcelable {
    private int expenseId;
    private String expenseName;
    private int expenseAmount;
    private String expenseType;
    private String expenseDate;
    private String currentTimeStamp;

    protected ExpenseType(Parcel in) {
        expenseId = in.readInt();
        expenseName = in.readString();
        expenseAmount = in.readInt();
        expenseType = in.readString();
        expenseDate = in.readString();
        currentTimeStamp = in.readString();
    }
    public ExpenseType()
    {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(expenseId);
        dest.writeString(expenseName);
        dest.writeInt(expenseAmount);
        dest.writeString(expenseType);
        dest.writeString(expenseDate);
        dest.writeString(currentTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpenseType> CREATOR = new Creator<ExpenseType>() {
        @Override
        public ExpenseType createFromParcel(Parcel in) {
            return new ExpenseType(in);
        }

        @Override
        public ExpenseType[] newArray(int size) {
            return new ExpenseType[size];
        }
    };

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    public void setCurrentTimeStamp(String currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }
}
