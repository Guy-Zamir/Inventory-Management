package com.guy.inventory;

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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterBuys extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;

    public AdapterBuys(Context context, List<Buy> list) {
        super(context, R.layout.buy_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.buy_row_layout, parent, false);

        TextView tvBuyPrice = convertView.findViewById(R.id.tvBuyPrice);
        TextView tvBuySupplier = convertView.findViewById(R.id.tvBuySupplier);
        TextView tvBuyDate = convertView.findViewById(R.id.tvBuyDate);
        ImageView ivDone = convertView.findViewById(R.id.ivDone);

        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(buys.get(position).getBuyDate());
        String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(buys.get(position).getPayDate());

        tvBuySupplier.setText(buys.get(position).getSupplierName());
        tvBuyDate.setText("תאריך קניה:  " + buyDays + "/" + buyMonth);
        tvBuyPrice.setText("מחיר:  " + nf.format(buys.get(position).getPrice()) + "$");

        if (InventoryApp.buys.get(position).isDone()) {
            ivDone.setImageResource(R.drawable.done1_icon);
        } else {
            ivDone.setImageResource(R.drawable.not_done1_icon);
        }

        return convertView;
    }
}
