package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortHistoryAdapter extends ArrayAdapter<SortInfo> {
    private Context context;
    private List<SortInfo> sortHistory;
    private int selectedPosition = -1;
    private String sortName = "";
    private int selectedSort = -1;
    private int i = 0;

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

        double sum = 0;
        double weight = 0;
        final String inOut = (sortHistory.get(position).isFromBuy() ? "נכנס: " : "יוצא: ");
        Sort chosenSort = InventoryApp.sorts.get(selectedSort);
        SortInfo sortInfo = sortHistory.get(position);

        // Getting the sum and weight
        for (SortInfo sortInfo2 : sortHistory) {
            if (sortInfo2.getCreated().before(sortHistory.get(position).getCreated())) {
                sum += sortInfo2.getSum();
                weight += sortInfo2.getWeight();
            }
        }

        // The sort selected direction is IN
        if (sortInfo.isFromBuy()) {

            // The sort selected origin is from buy
            if (sortInfo.getSortCount() - i == chosenSort.getSortCount() || chosenSort.getSortCount() - i <= 0) {
                DataQueryBuilder buyBuilder = DataQueryBuilder.create();
                final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                String ID_CLAUSE = "objectId = '" + sortHistory.get(position).getFromId() + "'";
                buyBuilder.setWhereClause(EMAIL_CLAUSE);
                buyBuilder.setWhereClause(ID_CLAUSE);
                buyBuilder.addRelated("Supplier");

                // Finding the name of the buy the sort came from
                Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        HashMap Supplier = (HashMap) response.get(0).get("Supplier");
                        assert Supplier != null;
                        sortName = (String) Supplier.get("name");
                        tvSortHistoryNameOrg.setText("מיון " + inOut + sortName);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            // The sort selected origin is from other sort
            } else {
                sortName = chosenSort.getName() + " - " + (chosenSort.getSortCount() - i - 1);
                tvSortHistoryNameOrg.setText("מיון " + inOut + sortName);
            }

        // The sort selected direction is OUT
        } else if (sortInfo.isFromSale()) {
            if (chosenSort.getSortCount() - i == sortInfo.getSortCount()) {
                sortName = (chosenSort.getName() + " - " + (chosenSort.getSortCount() - i + "S"));
                tvSortHistoryNameOrg.setText("מיון " + inOut + sortName);
            }

        }

        double price = (weight == 0) ? 0 :sum/weight;

        Calendar sortDate = Calendar.getInstance();
        sortDate.setTime(sortHistory.get(position).getCreated());
        @SuppressLint("DefaultLocale") String sortDays = String.format("%02d", sortDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String sortMonth = String.format("%02d", sortDate.get(Calendar.MONTH) + 1);
        @SuppressLint("DefaultLocale") String sortYear = String.format("%02d", sortDate.get(Calendar.YEAR));
        tvSortHistoryWeightOrg.setText("משקל לפני הפעולה: " + numberFormat.format(weight) + " קראט ");
        tvSortHistoryPriceOrg.setText("מחיר לפני הפעולה: " + numberFormat.format(price) + " $ ");
        tvSortHistorySumOrg.setText("סכום לפני הפעולה: " + numberFormat.format(sum) + " $ ");
        tvSortHistoryDate.setText("תאריך כניסה: " + sortDays + "/" + sortMonth + "/" + sortYear);
        tvSortHistoryWeight.setText("משקל " + inOut + numberFormat.format(sortHistory.get(position).getWeight()) + " קראט ");
        tvSortHistoryPrice.setText("מחיר " + inOut + numberFormat.format(sortHistory.get(position).getPrice()) + " $ ");
        tvSortHistorySum.setText("סכום " + inOut + numberFormat.format(sortHistory.get(position).getSum()) + " $ ");

        tvSortHistoryPL.setVisibility(sortHistory.get(position).isFromBuy() ? View.GONE : View.VISIBLE);
        ivOutIn.setImageResource(sortHistory.get(position).isFromBuy() ? R.drawable.in_icon : R.drawable.out_icon);
        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    public void setSelectedSort(int pos) {
        selectedSort = pos;
    }

    public void addI(){
        i += 1;
    }

    public void reduceI() {
        i -= 1;
    }

    public int getI() {
        return i;
    }
}