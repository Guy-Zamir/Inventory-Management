package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guy.inventory.R;
import com.guy.inventory.Classes.Sale;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class SalesAdapter extends ArrayAdapter<Sale> {
    private Context context;
    private List<Sale> sales;

    public SalesAdapter(Context context, List<Sale> list) {
        super(context, R.layout.sale_row_layout, list);
        this.sales = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sale_row_layout, parent, false);

        TextView tvSalePayDate = convertView.findViewById(R.id.tvSalePayDate);
        TextView tvSaleClientName = convertView.findViewById(R.id.tvSaleClientName);
        TextView tvSaleDate = convertView.findViewById(R.id.tvSaleDate);
        TextView tvSaleSum = convertView.findViewById(R.id.tvSaleSum);
        TextView tvSaleWeight = convertView.findViewById(R.id.tvSaleWeight);
        ImageView ivSaleTablePaid = convertView.findViewById(R.id.ivSaleTablePaid);

        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(sales.get(position).getSaleDate());
        String saleDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String saleMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(sales.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);

        tvSaleClientName.setText(sales.get(position).getCelint().getName());
        tvSaleDate.setText("תאריך מכירה:  " + saleDays + "/" + saleMonth);
        tvSaleSum.setText("סכום:  " + nf.format(sales.get(position).getSaleSum()) + "$");
        tvSaleWeight.setText("משקל:  " + nf.format(sales.get(position).getWeight()));
        tvSalePayDate.setText("תאריך פקיעה:  " + payDays + "/" + payMonth);

        if (sales.get(position).isPaid()) {
            ivSaleTablePaid.setVisibility(View.VISIBLE);
        } else {
            ivSaleTablePaid.setVisibility(View.GONE);
        }

        return convertView;
    }
}
