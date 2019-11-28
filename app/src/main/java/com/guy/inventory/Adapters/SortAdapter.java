package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;

import java.text.DecimalFormat;
import java.util.List;

public class SortAdapter extends ArrayAdapter<Sort> {
    private Context context;
    private List<Sort> sorts;
    private int selectedPosition = -1;

    public SortAdapter(Context context, List<Sort> list) {
        super(context, R.layout.sort_row_layout, list);
        this.sorts = list;
        this.context = context;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sort_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvSortName = convertView.findViewById(R.id.tvSortName);
        TextView tvSortPrice = convertView.findViewById(R.id.tvSortPrice);
        TextView tvSortWeight = convertView.findViewById(R.id.tvSortWeight);
        TextView tvSortSum = convertView.findViewById(R.id.tvSortSum);
        TextView tvSortShape = convertView.findViewById(R.id.tvSortShape);
        TextView tvSortSize = convertView.findViewById(R.id.tvSortSize);
        TextView tvSortColor = convertView.findViewById(R.id.tvSortColor);
        TextView tvSortClarity = convertView.findViewById(R.id.tvSortClarity);

        LinearLayout llSortDetails = convertView.findViewById(R.id.llSortDetails);

        // Setting the values to the views
        tvSortName.setText(InventoryApp.sorts.get(position).getName());
        tvSortPrice.setText("מחיר ממוצע: " + numberFormat.format(sorts.get(position).getPrice()) + "$");
        tvSortWeight.setText("משקל: " + numberFormat.format(sorts.get(position).getWeight()) + " קראט ");
        tvSortSum.setText("סכום: " + numberFormat.format(sorts.get(position).getSum()) + "$");
        tvSortShape.setText("צורה: " + sorts.get(position).getShape());
        tvSortSize.setText("גודל: " + sorts.get(position).getSize());
        tvSortColor.setText("צבע: " + sorts.get(position).getColor());
        tvSortClarity.setText("נקיון: " + sorts.get(position).getClarity());

        // When the sort is selected from the list
        llSortDetails.setVisibility((position == selectedPosition) ? View.VISIBLE : View.GONE);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}