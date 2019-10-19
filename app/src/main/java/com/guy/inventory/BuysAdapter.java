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
import java.util.List;

public class BuysAdapter extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;

    public BuysAdapter(Context context, List<Buy> list) {
        super(context, R.layout.buy_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
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
        TextView tvBuySum = convertView.findViewById(R.id.tvBuySum);
        ImageView ivBuy = convertView.findViewById(R.id.ivBuy);

        tvBuySupplier.setText(buys.get(position).getSupplier());
        tvBuyDate.setText("תאריך קניה:  " + buys.get(position).getBuyDate().substring(0,5));
        tvBuyWeight.setText("משקל:  " + nf.format(buys.get(position).getWeight()));
        tvBuyPrice.setText("מחיר:  " + nf.format(buys.get(position).getPrice()));
        tvBuySum.setText("סכום:  " + nf.format(buys.get(position).getSum()) + "$");

        if (buys.get(position).isPolish()) {
           ivBuy.setImageResource(R.drawable.diamond_icon);
        } else if (buys.get(position).isDone()) {
            ivBuy.setImageResource(R.drawable.done_icon);
        } else {
            ivBuy.setVisibility(View.GONE);
        }
        return convertView;
    }
}
