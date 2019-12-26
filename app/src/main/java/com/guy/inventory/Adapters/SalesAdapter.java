package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sale;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SalesAdapter extends ArrayAdapter<Sale> {
    private Context context;
    private List<Sale> sales;
    private int selectedPosition = -1;

    public SalesAdapter(Context context, List<Sale> list) {
        super(context, R.layout.sale_row_layout, list);
        this.sales = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sale_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvSaleClientName = convertView.findViewById(R.id.tvSaleClientName);
        TextView tvSaleDate = convertView.findViewById(R.id.tvSaleDate);
        TextView tvSaleSum = convertView.findViewById(R.id.tvSaleSum);

        ImageView ivRough = convertView.findViewById(R.id.ivRough);
        ImageView ivSorted = convertView.findViewById(R.id.ivSorted);
        TextView tvSaleDetailsPayDate = convertView.findViewById(R.id.tvSaleDetailsPayDate);
        TextView tvSaleDetailsID = convertView.findViewById(R.id.tvSaleDetailsID);
        TextView tvSaleDetailsPrice = convertView.findViewById(R.id.tvSaleDetailsPrice);
        TextView tvSaleDetailsWeight = convertView.findViewById(R.id.tvSaleDetailsWeight);
        TextView tvSaleDetailsDays = convertView.findViewById(R.id.tvSaleDetailsDays);

        LinearLayout llSaleDetails = convertView.findViewById(R.id.llSaleDetails);

        final TextView Headline = convertView.findViewById(R.id.Headline);

        final TextView tvSaleDetailsSortLeftN = convertView.findViewById(R.id.tvSaleDetailsSortLeftN);
        final TextView tvSaleDetailsSortN1 = convertView.findViewById(R.id.tvSaleDetailsSortN1);
        final TextView tvSaleDetailsSortN2 = convertView.findViewById(R.id.tvSaleDetailsSortN2);
        final TextView tvSaleDetailsSortN3 = convertView.findViewById(R.id.tvSaleDetailsSortN3);
        final TextView tvSaleDetailsSortN4 = convertView.findViewById(R.id.tvSaleDetailsSortN4);
        final TextView tvSaleDetailsSortN5 = convertView.findViewById(R.id.tvSaleDetailsSortN5);

        final TextView tvSaleDetailsSortLeftW = convertView.findViewById(R.id.tvSaleDetailsSortLeftW);
        final TextView tvSaleDetailsSortW1 = convertView.findViewById(R.id.tvSaleDetailsSortW1);
        final TextView tvSaleDetailsSortW2 = convertView.findViewById(R.id.tvSaleDetailsSortW2);
        final TextView tvSaleDetailsSortW3 = convertView.findViewById(R.id.tvSaleDetailsSortW3);
        final TextView tvSaleDetailsSortW4 = convertView.findViewById(R.id.tvSaleDetailsSortW4);
        final TextView tvSaleDetailsSortW5 = convertView.findViewById(R.id.tvSaleDetailsSortW5);

        final TextView tvSaleDetailsSortLeftP = convertView.findViewById(R.id.tvSaleDetailsSortLeftP);
        final TextView tvSaleDetailsSortP1 = convertView.findViewById(R.id.tvSaleDetailsSortP1);
        final TextView tvSaleDetailsSortP2 = convertView.findViewById(R.id.tvSaleDetailsSortP2);
        final TextView tvSaleDetailsSortP3 = convertView.findViewById(R.id.tvSaleDetailsSortP3);
        final TextView tvSaleDetailsSortP4 = convertView.findViewById(R.id.tvSaleDetailsSortP4);
        final TextView tvSaleDetailsSortP5 = convertView.findViewById(R.id.tvSaleDetailsSortP5);

        // When the goods are done
        ivSorted.setImageResource((InventoryApp.sales.get(position).isSorted()) ? R.drawable.done1_icon : R.drawable.not_done1_icon);

        // Setting the values to the views
        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(InventoryApp.sales.get(position).getSaleDate());
        String saleDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String saleMonth = String.format("%02d", saleDate.get(Calendar.MONTH) + 1);
        String saleYear = String.format("%02d", saleDate.get(Calendar.YEAR));

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(InventoryApp.sales.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
        String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

        tvSaleClientName.setText(sales.get(position).getClientName());
        tvSaleDate.setText("תאריך מכירה: " + saleDays + "/" + saleMonth + "/" + saleYear);
        tvSaleSum.setText("סכום עסקה: " + numberFormat.format(sales.get(position).getSaleSum()) + "$");
        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth + "/" + payYear);
        tvSaleDetailsID.setText("מספר חשבונית: " + InventoryApp.sales.get(position).getId());
        tvSaleDetailsPrice.setText("מחיר ממוצע: " + numberFormat.format(InventoryApp.sales.get(position).getPrice()) + "$");
        tvSaleDetailsWeight.setText("משקל חבילה: " + numberFormat.format(InventoryApp.sales.get(position).getWeight()));
        tvSaleDetailsDays.setText("מספר ימים: " + numberFormat.format(InventoryApp.sales.get(position).getDays()));

        //////////No need for now////////
        tvSaleDetailsDays.setVisibility(View.GONE);
        tvSaleDetailsPayDate.setVisibility(View.GONE);
        ///////////////////////////

        // Setting all gone first before the calculation
        Headline.setVisibility(View.GONE);

        tvSaleDetailsSortLeftP.setVisibility(View.GONE);
        tvSaleDetailsSortP1.setVisibility(View.GONE);
        tvSaleDetailsSortP2.setVisibility(View.GONE);
        tvSaleDetailsSortP3.setVisibility(View.GONE);
        tvSaleDetailsSortP4.setVisibility(View.GONE);
        tvSaleDetailsSortP5.setVisibility(View.GONE);

        tvSaleDetailsSortLeftW.setVisibility(View.GONE);
        tvSaleDetailsSortW1.setVisibility(View.GONE);
        tvSaleDetailsSortW2.setVisibility(View.GONE);
        tvSaleDetailsSortW3.setVisibility(View.GONE);
        tvSaleDetailsSortW4.setVisibility(View.GONE);
        tvSaleDetailsSortW5.setVisibility(View.GONE);

        tvSaleDetailsSortLeftN.setVisibility(View.GONE);
        tvSaleDetailsSortN1.setVisibility(View.GONE);
        tvSaleDetailsSortN2.setVisibility(View.GONE);
        tvSaleDetailsSortN3.setVisibility(View.GONE);
        tvSaleDetailsSortN4.setVisibility(View.GONE);
        tvSaleDetailsSortN5.setVisibility(View.GONE);

        // When the sale is sorted
        if (InventoryApp.sales.get(position).isSorted()) {
            Headline.setVisibility(View.VISIBLE);

            DataQueryBuilder sortBuilder = DataQueryBuilder.create();
            String whereClause1 = "userEmail = '" + InventoryApp.user.getEmail() + "'";
            String whereClause2 = "saleId = '" + InventoryApp.sales.get(position).getObjectId() + "'";
            sortBuilder.setWhereClause(whereClause1);
            sortBuilder.setWhereClause(whereClause2);

            Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {
                    for (int i = 0; i < response.size(); i++) {
                        switch (i) {

                            case 0:
                                tvSaleDetailsSortLeftP.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortLeftW.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortLeftN.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortLeftP.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortLeftW.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortLeftN.setVisibility(View.VISIBLE);
                                break;

                            case 1:
                                tvSaleDetailsSortP1.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW1.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN1.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortP1.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW1.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN1.setVisibility(View.VISIBLE);
                                break;

                            case 2:
                                tvSaleDetailsSortP2.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW2.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN2.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortP2.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW2.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN2.setVisibility(View.VISIBLE);
                                break;

                            case 3:
                                tvSaleDetailsSortP3.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW3.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN3.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortP3.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW3.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN3.setVisibility(View.VISIBLE);
                                break;

                            case 4:
                                tvSaleDetailsSortP4.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW4.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN4.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortP4.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW4.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN4.setVisibility(View.VISIBLE);
                                break;

                            case 5:
                                tvSaleDetailsSortP5.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW5.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN5.setText(response.get(i).get("name") + " - " + response.get(i).get("sortCount"));

                                // Setting the visibility
                                tvSaleDetailsSortP5.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW5.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN5.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }

            // When the sale is selected from the list
            llSaleDetails.setVisibility((position == selectedPosition) ? View.VISIBLE : View.GONE);
            // When the sale is a rough sale
            ivRough.setVisibility((InventoryApp.sales.get(position).isPolish()) ? View.INVISIBLE : View.VISIBLE);

            convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
            return convertView;
        }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}

