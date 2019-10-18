package com.guy.inventory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class SalesAdapter extends ArrayAdapter<Sale> {
    private Context context;
    private List<Sale> sales;

    public SalesAdapter(Context context, List<Sale> list) {
        super(context, R.layout.sale_row_layout, list);
        this.sales = list;
        this.context = context;
        SaleShowActivity.checkBoxes = new boolean[list.size()];
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.sale_row_layout, parent, false);

        TextView tvSalePrice = convertView.findViewById(R.id.tvSalePrice);
        TextView tvSaleCompany = convertView.findViewById(R.id.tvSaleCompany);
        TextView tvSaleDate = convertView.findViewById(R.id.tvSaleDate);
        TextView tvSaleSum = convertView.findViewById(R.id.tvSaleSum);
        TextView tvSaleWeight = convertView.findViewById(R.id.tvSaleWeight);
        CheckBox cbSales = convertView.findViewById(R.id.cbSales);
        SaleShowActivity.checkBoxes[position] = false;

        tvSaleCompany.setText(sales.get(position).getCompany());
        tvSaleDate.setText("תאריך:  " + String.valueOf(sales.get(position).getSaleDate()).substring(0, 2) + "/" + String.valueOf(sales.get(position).getSaleDate()).substring(2, 4));
        tvSaleSum.setText("סכום:  " + nf.format(sales.get(position).getSaleSum()) + "$");
        tvSaleWeight.setText("משקל:  " + nf.format(sales.get(position).getWeight()));
        tvSalePrice.setText("מחיר:  " + nf.format(sales.get(position).getPrice()));

        cbSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleShowActivity.checkBoxes[position] = !SaleShowActivity.checkBoxes[position];
            }
        });

        return convertView;
    }
}
