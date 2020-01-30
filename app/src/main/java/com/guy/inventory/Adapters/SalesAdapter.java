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
import android.widget.Toast;

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

        final TextView tvSaleDetailsSortN1 = convertView.findViewById(R.id.tvSaleDetailsSortN1);
        final TextView tvSaleDetailsSortN2 = convertView.findViewById(R.id.tvSaleDetailsSortN2);
        final TextView tvSaleDetailsSortN3 = convertView.findViewById(R.id.tvSaleDetailsSortN3);
        final TextView tvSaleDetailsSortN4 = convertView.findViewById(R.id.tvSaleDetailsSortN4);
        final TextView tvSaleDetailsSortN5 = convertView.findViewById(R.id.tvSaleDetailsSortN5);
        final TextView tvSaleDetailsSortN6 = convertView.findViewById(R.id.tvSaleDetailsSortN6);
        final TextView tvSaleDetailsSortN7 = convertView.findViewById(R.id.tvSaleDetailsSortN7);
        final TextView tvSaleDetailsSortN8 = convertView.findViewById(R.id.tvSaleDetailsSortN8);
        final TextView tvSaleDetailsSortN9 = convertView.findViewById(R.id.tvSaleDetailsSortN9);
        final TextView tvSaleDetailsSortN10 = convertView.findViewById(R.id.tvSaleDetailsSortN10);
        final TextView tvSaleDetailsSortN11 = convertView.findViewById(R.id.tvSaleDetailsSortN11);
        final TextView tvSaleDetailsSortN12 = convertView.findViewById(R.id.tvSaleDetailsSortN12);
        final TextView tvSaleDetailsSortN13 = convertView.findViewById(R.id.tvSaleDetailsSortN13);
        final TextView tvSaleDetailsSortN14 = convertView.findViewById(R.id.tvSaleDetailsSortN14);
        final TextView tvSaleDetailsSortN15 = convertView.findViewById(R.id.tvSaleDetailsSortN15);
        final TextView tvSaleDetailsSortN16 = convertView.findViewById(R.id.tvSaleDetailsSortN16);
        final TextView tvSaleDetailsSortN17 = convertView.findViewById(R.id.tvSaleDetailsSortN17);
        final TextView tvSaleDetailsSortN18 = convertView.findViewById(R.id.tvSaleDetailsSortN18);
        final TextView tvSaleDetailsSortN19 = convertView.findViewById(R.id.tvSaleDetailsSortN19);
        final TextView tvSaleDetailsSortN20 = convertView.findViewById(R.id.tvSaleDetailsSortN20);

        final TextView tvSaleDetailsSortW1 = convertView.findViewById(R.id.tvSaleDetailsSortW1);
        final TextView tvSaleDetailsSortW2 = convertView.findViewById(R.id.tvSaleDetailsSortW2);
        final TextView tvSaleDetailsSortW3 = convertView.findViewById(R.id.tvSaleDetailsSortW3);
        final TextView tvSaleDetailsSortW4 = convertView.findViewById(R.id.tvSaleDetailsSortW4);
        final TextView tvSaleDetailsSortW5 = convertView.findViewById(R.id.tvSaleDetailsSortW5);
        final TextView tvSaleDetailsSortW6 = convertView.findViewById(R.id.tvSaleDetailsSortW6);
        final TextView tvSaleDetailsSortW7 = convertView.findViewById(R.id.tvSaleDetailsSortW7);
        final TextView tvSaleDetailsSortW8 = convertView.findViewById(R.id.tvSaleDetailsSortW8);
        final TextView tvSaleDetailsSortW9 = convertView.findViewById(R.id.tvSaleDetailsSortW9);
        final TextView tvSaleDetailsSortW10 = convertView.findViewById(R.id.tvSaleDetailsSortW10);
        final TextView tvSaleDetailsSortW11 = convertView.findViewById(R.id.tvSaleDetailsSortW11);
        final TextView tvSaleDetailsSortW12 = convertView.findViewById(R.id.tvSaleDetailsSortW12);
        final TextView tvSaleDetailsSortW13 = convertView.findViewById(R.id.tvSaleDetailsSortW13);
        final TextView tvSaleDetailsSortW14 = convertView.findViewById(R.id.tvSaleDetailsSortW14);
        final TextView tvSaleDetailsSortW15 = convertView.findViewById(R.id.tvSaleDetailsSortW15);
        final TextView tvSaleDetailsSortW16 = convertView.findViewById(R.id.tvSaleDetailsSortW16);
        final TextView tvSaleDetailsSortW17 = convertView.findViewById(R.id.tvSaleDetailsSortW17);
        final TextView tvSaleDetailsSortW18 = convertView.findViewById(R.id.tvSaleDetailsSortW18);
        final TextView tvSaleDetailsSortW19 = convertView.findViewById(R.id.tvSaleDetailsSortW19);
        final TextView tvSaleDetailsSortW20 = convertView.findViewById(R.id.tvSaleDetailsSortW20);

        final TextView tvSaleDetailsSortP1 = convertView.findViewById(R.id.tvSaleDetailsSortP1);
        final TextView tvSaleDetailsSortP2 = convertView.findViewById(R.id.tvSaleDetailsSortP2);
        final TextView tvSaleDetailsSortP3 = convertView.findViewById(R.id.tvSaleDetailsSortP3);
        final TextView tvSaleDetailsSortP4 = convertView.findViewById(R.id.tvSaleDetailsSortP4);
        final TextView tvSaleDetailsSortP5 = convertView.findViewById(R.id.tvSaleDetailsSortP5);
        final TextView tvSaleDetailsSortP6 = convertView.findViewById(R.id.tvSaleDetailsSortP6);
        final TextView tvSaleDetailsSortP7 = convertView.findViewById(R.id.tvSaleDetailsSortP7);
        final TextView tvSaleDetailsSortP8 = convertView.findViewById(R.id.tvSaleDetailsSortP8);
        final TextView tvSaleDetailsSortP9 = convertView.findViewById(R.id.tvSaleDetailsSortP9);
        final TextView tvSaleDetailsSortP10 = convertView.findViewById(R.id.tvSaleDetailsSortP10);
        final TextView tvSaleDetailsSortP11 = convertView.findViewById(R.id.tvSaleDetailsSortP11);
        final TextView tvSaleDetailsSortP12 = convertView.findViewById(R.id.tvSaleDetailsSortP12);
        final TextView tvSaleDetailsSortP13 = convertView.findViewById(R.id.tvSaleDetailsSortP13);
        final TextView tvSaleDetailsSortP14 = convertView.findViewById(R.id.tvSaleDetailsSortP14);
        final TextView tvSaleDetailsSortP15 = convertView.findViewById(R.id.tvSaleDetailsSortP15);
        final TextView tvSaleDetailsSortP16 = convertView.findViewById(R.id.tvSaleDetailsSortP16);
        final TextView tvSaleDetailsSortP17 = convertView.findViewById(R.id.tvSaleDetailsSortP17);
        final TextView tvSaleDetailsSortP18 = convertView.findViewById(R.id.tvSaleDetailsSortP18);
        final TextView tvSaleDetailsSortP19 = convertView.findViewById(R.id.tvSaleDetailsSortP19);
        final TextView tvSaleDetailsSortP20 = convertView.findViewById(R.id.tvSaleDetailsSortP20);

        ///////// No need for now //////////
        tvSaleDetailsDays.setVisibility(View.GONE);
        tvSaleDetailsPayDate.setVisibility(View.GONE);
        ///////////////////////////////////

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
        tvSaleDetailsWeight.setText("משקל חבילה: " + numberFormat.format(InventoryApp.sales.get(position).getWeight()) + " קראט ");
        tvSaleDetailsDays.setText("מספר ימים: " + numberFormat.format(InventoryApp.sales.get(position).getDays()));

        ////////// No need for now ////////
        tvSaleDetailsDays.setVisibility(View.GONE);
        tvSaleDetailsPayDate.setVisibility(View.GONE);
        ///////////////////////////

        // Setting all gone first before the calculation
        Headline.setVisibility(View.GONE);

        tvSaleDetailsSortP1.setVisibility(View.GONE);
        tvSaleDetailsSortP2.setVisibility(View.GONE);
        tvSaleDetailsSortP3.setVisibility(View.GONE);
        tvSaleDetailsSortP4.setVisibility(View.GONE);
        tvSaleDetailsSortP5.setVisibility(View.GONE);
        tvSaleDetailsSortP6.setVisibility(View.GONE);
        tvSaleDetailsSortP7.setVisibility(View.GONE);
        tvSaleDetailsSortP8.setVisibility(View.GONE);
        tvSaleDetailsSortP9.setVisibility(View.GONE);
        tvSaleDetailsSortP10.setVisibility(View.GONE);
        tvSaleDetailsSortP11.setVisibility(View.GONE);
        tvSaleDetailsSortP12.setVisibility(View.GONE);
        tvSaleDetailsSortP13.setVisibility(View.GONE);
        tvSaleDetailsSortP14.setVisibility(View.GONE);
        tvSaleDetailsSortP15.setVisibility(View.GONE);
        tvSaleDetailsSortP16.setVisibility(View.GONE);
        tvSaleDetailsSortP17.setVisibility(View.GONE);
        tvSaleDetailsSortP18.setVisibility(View.GONE);
        tvSaleDetailsSortP19.setVisibility(View.GONE);
        tvSaleDetailsSortP20.setVisibility(View.GONE);

        tvSaleDetailsSortW1.setVisibility(View.GONE);
        tvSaleDetailsSortW2.setVisibility(View.GONE);
        tvSaleDetailsSortW3.setVisibility(View.GONE);
        tvSaleDetailsSortW4.setVisibility(View.GONE);
        tvSaleDetailsSortW5.setVisibility(View.GONE);
        tvSaleDetailsSortW6.setVisibility(View.GONE);
        tvSaleDetailsSortW7.setVisibility(View.GONE);
        tvSaleDetailsSortW8.setVisibility(View.GONE);
        tvSaleDetailsSortW9.setVisibility(View.GONE);
        tvSaleDetailsSortW10.setVisibility(View.GONE);
        tvSaleDetailsSortW11.setVisibility(View.GONE);
        tvSaleDetailsSortW12.setVisibility(View.GONE);
        tvSaleDetailsSortW13.setVisibility(View.GONE);
        tvSaleDetailsSortW14.setVisibility(View.GONE);
        tvSaleDetailsSortW15.setVisibility(View.GONE);
        tvSaleDetailsSortW16.setVisibility(View.GONE);
        tvSaleDetailsSortW17.setVisibility(View.GONE);
        tvSaleDetailsSortW18.setVisibility(View.GONE);
        tvSaleDetailsSortW19.setVisibility(View.GONE);
        tvSaleDetailsSortW20.setVisibility(View.GONE);

        tvSaleDetailsSortN1.setVisibility(View.GONE);
        tvSaleDetailsSortN2.setVisibility(View.GONE);
        tvSaleDetailsSortN3.setVisibility(View.GONE);
        tvSaleDetailsSortN4.setVisibility(View.GONE);
        tvSaleDetailsSortN5.setVisibility(View.GONE);
        tvSaleDetailsSortN6.setVisibility(View.GONE);
        tvSaleDetailsSortN7.setVisibility(View.GONE);
        tvSaleDetailsSortN8.setVisibility(View.GONE);
        tvSaleDetailsSortN9.setVisibility(View.GONE);
        tvSaleDetailsSortN10.setVisibility(View.GONE);
        tvSaleDetailsSortN11.setVisibility(View.GONE);
        tvSaleDetailsSortN12.setVisibility(View.GONE);
        tvSaleDetailsSortN13.setVisibility(View.GONE);
        tvSaleDetailsSortN14.setVisibility(View.GONE);
        tvSaleDetailsSortN15.setVisibility(View.GONE);
        tvSaleDetailsSortN16.setVisibility(View.GONE);
        tvSaleDetailsSortN17.setVisibility(View.GONE);
        tvSaleDetailsSortN18.setVisibility(View.GONE);
        tvSaleDetailsSortN19.setVisibility(View.GONE);
        tvSaleDetailsSortN20.setVisibility(View.GONE);

        // When the sale is sorted
        if (InventoryApp.sales.get(position).isSorted()) {
            Headline.setVisibility(View.VISIBLE);

            DataQueryBuilder sortBuilder = DataQueryBuilder.create();
            String whereClause2 = "saleId = '" + InventoryApp.sales.get(position).getObjectId() + "'";
            sortBuilder.setWhereClause(whereClause2);
            sortBuilder.setPageSize(100);

            Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {
                    for (int i = 0; i < response.size(); i++) {
                        switch (i) {

                            case 0:
                                tvSaleDetailsSortP1.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW1.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN1.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP1.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW1.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN1.setVisibility(View.VISIBLE);
                                break;

                            case 1:
                                tvSaleDetailsSortP2.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW2.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN2.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP2.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW2.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN2.setVisibility(View.VISIBLE);
                                break;

                            case 2:
                                tvSaleDetailsSortP3.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW3.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN3.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP3.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW3.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN3.setVisibility(View.VISIBLE);
                                break;

                            case 3:
                                tvSaleDetailsSortP4.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW4.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN4.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP4.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW4.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN4.setVisibility(View.VISIBLE);
                                break;

                            case 4:
                                tvSaleDetailsSortP5.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW5.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN5.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP5.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW5.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN5.setVisibility(View.VISIBLE);
                                break;

                            case 5:
                                tvSaleDetailsSortP6.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW6.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN6.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP6.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW6.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN6.setVisibility(View.VISIBLE);
                                break;

                            case 6:
                                tvSaleDetailsSortP7.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW7.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN7.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP7.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW7.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN7.setVisibility(View.VISIBLE);
                                break;

                            case 7:
                                tvSaleDetailsSortP8.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW8.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN8.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP8.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW8.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN8.setVisibility(View.VISIBLE);
                                break;

                            case 8:
                                tvSaleDetailsSortP9.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW9.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN9.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP9.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW9.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN9.setVisibility(View.VISIBLE);
                                break;

                            case 9:
                                tvSaleDetailsSortP10.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW10.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN10.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP10.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW10.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN10.setVisibility(View.VISIBLE);
                                break;

                            case 10:
                                tvSaleDetailsSortP11.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW11.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN11.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP11.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW11.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN11.setVisibility(View.VISIBLE);
                                break;

                            case 11:
                                tvSaleDetailsSortP12.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW12.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN12.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP12.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW12.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN12.setVisibility(View.VISIBLE);
                                break;

                            case 12:
                                tvSaleDetailsSortP13.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW13.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN13.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP13.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW13.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN13.setVisibility(View.VISIBLE);
                                break;

                            case 13:
                                tvSaleDetailsSortP14.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW14.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN14.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP14.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW14.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN14.setVisibility(View.VISIBLE);
                                break;

                            case 14:
                                tvSaleDetailsSortP15.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW15.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN15.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP15.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW15.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN15.setVisibility(View.VISIBLE);
                                break;

                            case 15:
                                tvSaleDetailsSortP16.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW16.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN16.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP16.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW16.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN16.setVisibility(View.VISIBLE);
                                break;

                            case 16:
                                tvSaleDetailsSortP17.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW17.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN17.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP17.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW17.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN17.setVisibility(View.VISIBLE);
                                break;

                            case 17:
                                tvSaleDetailsSortP18.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW18.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN18.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP18.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW18.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN18.setVisibility(View.VISIBLE);
                                break;

                            case 18:
                                tvSaleDetailsSortP19.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW19.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN19.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP19.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW19.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN19.setVisibility(View.VISIBLE);
                                break;

                            case 19:
                                tvSaleDetailsSortP20.setText("מחיר: " + (numberFormat.format(response.get(i).get("soldPrice"))) + " $ ");
                                tvSaleDetailsSortW20.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));
                                tvSaleDetailsSortN20.setText((String) response.get(i).get("name"));

                                tvSaleDetailsSortP20.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortW20.setVisibility(View.VISIBLE);
                                tvSaleDetailsSortN20.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
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

