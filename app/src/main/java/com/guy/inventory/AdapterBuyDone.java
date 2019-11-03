package com.guy.inventory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterBuyDone extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;

    public AdapterBuyDone(Context context, List<Buy> list) {
        super(context, R.layout.buy_done_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.buy_done_row_layout, parent, false);
        ImageView ivBuyTableDone = convertView.findViewById(R.id.ivBuyTableDone);

        if (buys.get(position).isDone()) {
            ivBuyTableDone.setImageResource(R.drawable.done_icon);
        } else {
            ivBuyTableDone.setImageResource(R.drawable.not_done_icon);
        }

        return convertView;
    }
}