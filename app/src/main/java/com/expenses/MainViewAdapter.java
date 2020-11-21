package com.expenses;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class MainViewAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<String> mHeader;
    HashMap<String, List<ExpenseType>> mChildData;
    IAdapterItemClickListener iAdapterItemClickListener;
    ExpenseOpType mExpenseOpType;

    public MainViewAdapter(Context context, List<String> header, HashMap<String, List<ExpenseType>> childData, IAdapterItemClickListener iAdapterItemClickListener) {
        this.mContext = context;
        this.mHeader = header;
        this.mChildData = childData;
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @Override
    public int getGroupCount() {
        return mHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildData.get(mHeader.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mHeader.get(groupPosition);
    }

    @Override
    public ExpenseType getChild(int groupPosition, int childPosition) {
        return mChildData.get(mHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView addExpenseIv = (ImageView) convertView
                .findViewById(R.id.addExpense_iv);
        addExpenseIv.setTag(headerTitle);

        addExpenseIv.setVisibility(mExpenseOpType == ExpenseOpType.ADD ? View.VISIBLE : View.GONE);
        if (mExpenseOpType == ExpenseOpType.ADD) {

        }

        addExpenseIv.setOnClickListener(mExpenseOpType == ExpenseOpType.ADD ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterItemClickListener.addExpense(headerTitle);
            }
        } : null);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpenseType expenseType = (ExpenseType) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView expenseName = (TextView) convertView
                .findViewById(R.id.expense_name_tv);
        TextView expenseValue = (TextView) convertView
                .findViewById(R.id.expense_value_tv);
        TextView updateOn = (TextView) convertView
                .findViewById(R.id.expense_date_tv);

        updateOn.setText("Updated on : " + new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new Date(Long.parseLong(expenseType.getCurrentTimeStamp()))));
        CheckBox expenseCheckBox = (CheckBox) convertView
                .findViewById(R.id.expense_item_cb);

        expenseName.setText(expenseType.getExpenseName());
        expenseValue.setText(expenseType.getExpenseAmount() + " $");
        expenseCheckBox.setChecked(false);
        if (mExpenseOpType == ExpenseOpType.NONE || mExpenseOpType == ExpenseOpType.ADD)
            expenseCheckBox.setVisibility(View.GONE);
        else
            expenseCheckBox.setVisibility(View.VISIBLE);
        expenseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    iAdapterItemClickListener.updateExpenseInfo(isChecked, mExpenseOpType, expenseType);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public void deleteData(ExpenseType expenseType) {
        List<ExpenseType> expenseTypes = mChildData.get(expenseType.getExpenseType());
        expenseTypes.remove(expenseType);
        mChildData.put(expenseType.getExpenseType(), expenseTypes);
        notifyDataSetChanged();
    }

    public void updateCheckValue(ExpenseOpType expenseOpType) {
        this.mExpenseOpType = expenseOpType;
        notifyDataSetChanged();
    }
}
