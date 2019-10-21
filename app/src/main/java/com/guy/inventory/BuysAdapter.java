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

public class BuysAdapter extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;

    public BuysAdapter(Context context, List<Buy> list) {
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
        TextView tvBuyWeight = convertView.findViewById(R.id.tvBuyWeight);
        TextView tvBuyPayDate = convertView.findViewById(R.id.tvBuyPayDate);
        ImageView ivBuyTablePolish = convertView.findViewById(R.id.ivBuyTablePolish);
        ImageView ivBuyTablePaid = convertView.findViewById(R.id.ivBuyTablePaid);
        ImageView ivBuyTableDone = convertView.findViewById(R.id.ivBuyTableDone);


        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(buys.get(position).getBuyDate());
        String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(buys.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);


        tvBuySupplier.setText(buys.get(position).getSupplier());
        tvBuyDate.setText("תאריך קניה:  " + buyDays + "/" + buyMonth);
        tvBuyWeight.setText("משקל:  " + nf.format(buys.get(position).getWeight()));
        tvBuyPrice.setText("מחיר:  " + nf.format(buys.get(position).getPrice()) + "$");
        tvBuyPayDate.setText("תאריך פקיעה:  " + payDays + "/" + payMonth);

        if (buys.get(position).isPolish()) {
            ivBuyTablePolish.setVisibility(View.VISIBLE);
        } else {
            ivBuyTablePolish.setVisibility(View.GONE);
        }

        if (buys.get(position).isPaid()) {
            ivBuyTablePaid.setVisibility(View.VISIBLE);
        } else {
            ivBuyTablePaid.setVisibility(View.GONE);
        }

        if (buys.get(position).isDone()) {
            ivBuyTableDone.setVisibility(View.VISIBLE);
        } else {
            ivBuyTableDone.setVisibility(View.GONE);
        }

        return convertView;
    }
}
