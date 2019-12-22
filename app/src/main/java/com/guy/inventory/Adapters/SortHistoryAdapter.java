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
        TextView tvSortHistoryWeightOrg = convertView.findViewById(R.id.tvSortHistoryWeightOrg);
        TextView tvSortHistoryPriceOrg = convertView.findViewById(R.id.tvSortHistoryPriceOrg);
        TextView tvSortHistorySumOrg = convertView.findViewById(R.id.tvSortHistorySumOrg);
        TextView tvSortHistoryWeight = convertView.findViewById(R.id.tvSortHistoryWeight);
        TextView tvSortHistoryPrice = convertView.findViewById(R.id.tvSortHistoryPrice);
        TextView tvSortHistorySum = convertView.findViewById(R.id.tvSortHistorySum);
        TextView tvSortHistoryPL = convertView.findViewById(R.id.tvSortHistoryPL);
        ImageView ivOutIn = convertView.findViewById(R.id.ivOutIn);

        final String inOut = (sortHistory.get(position).isFromBuy() ? "נכנס: " : "יוצא: ");

        Calendar sortDate = Calendar.getInstance();
        sortDate.setTime(sortHistory.get(position).getCreated());
        @SuppressLint("DefaultLocale") String sortDays = String.format("%02d", sortDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String sortMonth = String.format("%02d", sortDate.get(Calendar.MONTH) + 1);
        @SuppressLint("DefaultLocale") String sortYear = String.format("%02d", sortDate.get(Calendar.YEAR));

        String name = (sortHistory.get(position).isFromBuy() ? sortHistory.get(position).getName() : sortHistory.get(position).getName() + " - " + sortHistory.get(position).getSortCount());
        tvSortHistoryNameOrg.setText(name);
        tvSortHistoryDate.setText("תאריך כניסה: " + sortDays + "/" + sortMonth + "/" + sortYear);
        tvSortHistoryWeight.setText("משקל " + inOut + numberFormat.format(sortHistory.get(position).getWeight()) + " קראט ");
        tvSortHistoryPrice.setText("מחיר " + inOut + numberFormat.format(sortHistory.get(position).getPrice()) + " $ ");
        tvSortHistorySum.setText("סכום " + inOut + numberFormat.format(sortHistory.get(position).getSum()) + " $ ");

        //////////////////////
        tvSortHistoryWeightOrg.setVisibility(View.GONE);
        tvSortHistoryPriceOrg.setVisibility(View.GONE);
        tvSortHistorySumOrg.setVisibility(View.GONE);
        ////////////////////////

        tvSortHistoryPL.setVisibility(sortHistory.get(position).isFromSale() ? View.VISIBLE : View.GONE);
        ivOutIn.setImageResource((sortHistory.get(position).isFromSale()) ? R.drawable.out_icon : R.drawable.in_icon);
        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}