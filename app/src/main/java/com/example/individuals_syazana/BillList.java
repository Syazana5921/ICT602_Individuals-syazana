package com.example.individuals_syazana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BillList extends ArrayAdapter<ElectricityBill> {
    private final Activity context;
    private final List<ElectricityBill> billList;

    public BillList(Activity context, List<ElectricityBill> billList) {
        super(context, R.layout.list_layout, billList);
        this.context = context;
        this.billList = billList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.list_layout, parent, false);
        }

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        ElectricityBill bill = billList.get(position);
        textViewName.setText(bill.getMonth() + ": RM " + String.format("%.2f", bill.getFnlCost()));
        return listViewItem;
    }
}
