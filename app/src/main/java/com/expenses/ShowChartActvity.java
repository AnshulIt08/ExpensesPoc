package com.expenses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ShowChartActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart_actvity);
        BarChart chart = findViewById(R.id.barchart);


        List<ExpenseType> mList = getIntent().getParcelableArrayListExtra(MainListViewScreen.LIST_DATA);
        String header = mList.get(0).getExpenseType();
        BarDataSet bardataset = new BarDataSet( getAmountList(mList), header);
        chart.animateY(5000);
        BarData data = new BarData(getExpenseName(mList),bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }

    private List getAmountList(List<ExpenseType> mList)
    {
        List expenseNameList = new ArrayList();

        int count = 0;
        for(ExpenseType expenseType : mList) {
            expenseNameList.add(new BarEntry(expenseType.getExpenseAmount(), count));
            count += 1;
        }
        return expenseNameList;
    }

    private List<String> getExpenseName(List<ExpenseType> mList) {
        ArrayList mlist = new ArrayList();
        for(ExpenseType expenseType : mList)
            mlist.add(expenseType.getExpenseName());
        return mlist;
    }
}