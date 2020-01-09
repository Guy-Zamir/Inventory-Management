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
import com.guy.inventory.Tables.BrokerSort;
import com.guy.inventory.R;
import java.text.DecimalFormat;
import java.util.List;

public class BrokerAdapter extends ArrayAdapter<BrokerSort> {
    private Context context;
    private List<BrokerSort> brokerSorts;
    private int selectedPosition = -1;
    private boolean all;

    public BrokerAdapter(Context context, List<BrokerSort> list) {
        super(context, R.layout.broker_sort_row_layout, list);
        this.brokerSorts = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DecimalFormat numberFormat = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.broker_sort_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvSortName = convertView.findViewById(R.id.tvSortName);
        TextView tvSortPrice = convertView.findViewById(R.id.tvSortPrice);
        TextView tvSortPriceINV = convertView.findViewById(R.id.tvSortPriceINV);
        TextView tvSortWeight = convertView.findViewById(R.id.tvSortWeight);
        TextView tvSortSum = convertView.findViewById(R.id.tvSortSum);
        TextView tvSortSumINV = convertView.findViewById(R.id.tvSortSumINV);
        ImageView ivMemo = convertView.findViewById(R.id.ivMemo);

        BrokerSort brokerSort = brokerSorts.get(position);

        tvSortName.setText(all ? brokerSort.getKind() + " - "  + brokerSort.getName() : brokerSort.getName());
        tvSortPrice.setText("מחיר: " + numberFormat.format(brokerSort.getPrice()) + "$");
        tvSortPriceINV.setText("מחיר במלאי: " + numberFormat.format(brokerSort.getPriceINV()) + " $");
        tvSortWeight.setText("משקל: " + numberFormat.format(brokerSort.getWeight()) + " קראט ");
        tvSortSum.setText("סכום: " + numberFormat.format(brokerSort.getSum()) + "$");
        tvSortSumINV.setText("סכום במלאי: " + numberFormat.format(brokerSort.getSumINV()) + "$");

        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        ivMemo.setVisibility(brokerSort.getMemo() ? View.VISIBLE : View.GONE);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
