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
import com.guy.inventory.Tables.SortInfo;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class SortHistoryAdapter extends ArrayAdapter<SortInfo> {
    private Context context;
    private List<SortInfo> sortHistory;
    private int selectedPosition = -1;

    public SortHistoryAdapter(Context context, List<SortInfo> list) {
        super(context, R.layout.sort_history_row_layout, list);
        this.sortHistory = list;
        this.context = context;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sort_history_row_layout, parent, false);

        // Defining the views in the layouts
        final TextView tvSortHistoryNameOrg = convertView.findViewById(R.id.tvSortHistoryNameOrg);
        TextView tvSortHistoryDate = convertView.findViewById(R.id.tvSortHistoryDate);
        final TextView tvSortHistorySaleName = convertView.findViewById(R.id.tvSortHistorySaleName);
        TextView tvSortHistoryWeight = convertView.findViewById(R.id.tvSortHistoryWeight);
        TextView tvSortHistoryPrice = convertView.findViewById(R.id.tvSortHistoryPrice);
        TextView tvSortHistorySum = convertView.findViewById(R.id.tvSortHistorySum);
        TextView tvSortHistoryPL = convertView.findViewById(R.id.tvSortHistoryPL);
        TextView tvSortHistoryPriceSold = convertView.findViewById(R.id.tvSortHistoryPriceSold);
        TextView tvSortHistorySumSold = convertView.findViewById(R.id.tvSortHistorySumSold);

        ImageView ivOutIn = convertView.findViewById(R.id.ivOutIn);

        Calendar sortDate = Calendar.getInstance();
        sortDate.setTime(sortHistory.get(position).getCreated());
        @SuppressLint("DefaultLocale") String sortDays = String.format("%02d", sortDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String sortMonth = String.format("%02d", sortDate.get(Calendar.MONTH) + 1);
        @SuppressLint("DefaultLocale") String sortYear = String.format("%02d", sortDate.get(Calendar.YEAR));

        tvSortHistoryNameOrg.setText(sortHistory.get(position).getFromName());
        tvSortHistorySaleName.setText("נמכר ל: " + sortHistory.get(position).getToName());
        tvSortHistoryDate.setText("תאריך: " + sortDays + "/" + sortMonth + "/" + sortYear);
        tvSortHistoryWeight.setText("משקל: " + numberFormat.format(sortHistory.get(position).getWeight()) + " קראט ");
        tvSortHistoryPrice.setText("מחיר: " + numberFormat.format(sortHistory.get(position).getPrice()) + " $ ");
        tvSortHistorySum.setText("סכום: " + numberFormat.format(sortHistory.get(position).getSum()) + " $ ");
        tvSortHistoryPL.setText("רווח: " + numberFormat.format(((sortHistory.get(position).getSoldPrice()) - sortHistory.get(position).getPrice())*sortHistory.get(position).getWeight()) + "$");
        tvSortHistoryPriceSold.setText("מחיר מכירה: " + numberFormat.format(sortHistory.get(position).getSoldPrice()) + "$");
        tvSortHistorySumSold.setText("סכום מכירה: " + numberFormat.format(sortHistory.get(position).getSoldPrice() * sortHistory.get(position).getWeight()));

        tvSortHistoryPriceSold.setVisibility((sortHistory.get(position).getKind().equals("sale")) ? View.VISIBLE : View.GONE);
        tvSortHistorySumSold.setVisibility(sortHistory.get(position).getKind().equals("sale") ? View.VISIBLE : View.GONE);
        tvSortHistorySaleName.setVisibility(sortHistory.get(position).getKind().equals("sale") ? View.VISIBLE : View.GONE);
        tvSortHistoryPL.setVisibility(sortHistory.get(position).getKind().equals("sale") ? View.VISIBLE : View.GONE);

        if (sortHistory.get(position).getKind().equals("sale")) {
            ivOutIn.setImageResource(R.drawable.dollar_icon);

        } else if (sortHistory.get(position).getKind().equals("broker")) {
            ivOutIn.setImageResource(R.drawable.broker_icon);

        } else if (sortHistory.get(position).getKind().equals("memo")) {
            ivOutIn.setImageResource(R.drawable.m_icon);

        } else if (sortHistory.get(position).isOut()) {
            ivOutIn.setImageResource(R.drawable.out_icon);

        } else {
            ivOutIn.setImageResource(R.drawable.in_icon);
        }

        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}