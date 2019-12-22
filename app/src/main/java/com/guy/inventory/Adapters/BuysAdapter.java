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
import com.guy.inventory.Tables.Buy;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BuysAdapter extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;
    private int selectedPosition = -1;

    public BuysAdapter(Context context, List<Buy> list) {
        super(context, R.layout.buy_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DecimalFormat numberFormat = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.buy_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvBuyPrice = convertView.findViewById(R.id.tvBuyPrice);
        TextView tvBuySupplier = convertView.findViewById(R.id.tvBuySupplier);
        TextView tvBuyDate = convertView.findViewById(R.id.tvBuyDate);
        ImageView ivDone = convertView.findViewById(R.id.ivDone);
        ImageView ivPolish = convertView.findViewById(R.id.ivWorkPolish);

        TextView tvBuyDetailsPayDate = convertView.findViewById(R.id.tvBuyDetailsPayDate);
        TextView tvBuyDetailsID = convertView.findViewById(R.id.tvBuyDetailsID);
        TextView tvBuyDetailsWeight = convertView.findViewById(R.id.tvBuyDetailsWeight);
        TextView tvBuyDetailsDays = convertView.findViewById(R.id.tvBuyDetailsDays);
        TextView tvBuyDetailsSum = convertView.findViewById(R.id.tvBuyDetailsSum);
        TextView tvBuyDetailsDoneWeight = convertView.findViewById(R.id.tvBuyDetailsDoneWeight);
        TextView tvBuyDetailsWorkDe = convertView.findViewById(R.id.tvBuyDetailsWorkDe);
        TextView tvBuyDetailsWage = convertView.findViewById(R.id.tvBuyDetailsWage);

        LinearLayout llBuyDetailsDone = convertView.findViewById(R.id.llBuyDetailsDone);

        final TextView tvBuyDetailsSortLeftN = convertView.findViewById(R.id.tvBuyDetailsSortLeftN);
        final TextView tvBuyDetailsSortN1 = convertView.findViewById(R.id.tvBuyDetailsSortN1);
        final TextView tvBuyDetailsSortN2 = convertView.findViewById(R.id.tvBuyDetailsSortN2);
        final TextView tvBuyDetailsSortN3 = convertView.findViewById(R.id.tvBuyDetailsSortN3);
        final TextView tvBuyDetailsSortN4 = convertView.findViewById(R.id.tvBuyDetailsSortN4);
        final TextView tvBuyDetailsSortN5 = convertView.findViewById(R.id.tvBuyDetailsSortN5);

        final TextView tvBuyDetailsSortLeftW = convertView.findViewById(R.id.tvBuyDetailsSortLeftW);
        final TextView tvBuyDetailsSortW1 = convertView.findViewById(R.id.tvBuyDetailsSortW1);
        final TextView tvBuyDetailsSortW2 = convertView.findViewById(R.id.tvBuyDetailsSortW2);
        final TextView tvBuyDetailsSortW3 = convertView.findViewById(R.id.tvBuyDetailsSortW3);
        final TextView tvBuyDetailsSortW4 = convertView.findViewById(R.id.tvBuyDetailsSortW4);
        final TextView tvBuyDetailsSortW5 = convertView.findViewById(R.id.tvBuyDetailsSortW5);

        final TextView tvBuyDetailsSortLeftP = convertView.findViewById(R.id.tvBuyDetailsSortLeftP);
        final TextView tvBuyDetailsSortP1 = convertView.findViewById(R.id.tvBuyDetailsSortP1);
        final TextView tvBuyDetailsSortP2 = convertView.findViewById(R.id.tvBuyDetailsSortP2);
        final TextView tvBuyDetailsSortP3 = convertView.findViewById(R.id.tvBuyDetailsSortP3);
        final TextView tvBuyDetailsSortP4 = convertView.findViewById(R.id.tvBuyDetailsSortP4);
        final TextView tvBuyDetailsSortP5 = convertView.findViewById(R.id.tvBuyDetailsSortP5);

        LinearLayout llBuyDetails = convertView.findViewById(R.id.llBuyDetails);

        // Setting the values to the views
        final Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(buys.get(position).getBuyDate());
        String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);
        String buyYear = String.format("%02d", saleDate.get(Calendar.YEAR));

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(buys.get(position).getPayDate());
        payDate.setTime(InventoryApp.buys.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
        String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

        tvBuySupplier.setText(buys.get(position).getSupplierName());
        tvBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth+ "/" + buyYear);
        tvBuyPrice.setText("מחיר לקראט: " + numberFormat.format(buys.get(position).getPrice()) + "$");
        tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);
        tvBuyDetailsID.setText("מספר אסמכתא: " + InventoryApp.buys.get(position).getId());
        tvBuyDetailsWeight.setText("משקל חבילה: " + numberFormat.format(InventoryApp.buys.get(position).getWeight()) + " קראט ");
        tvBuyDetailsDays.setText("מספר ימים: " + InventoryApp.buys.get(position).getDays());
        tvBuyDetailsSum.setText("סכום עסקה: " + numberFormat.format(InventoryApp.buys.get(position).getSum()) + "$");
        tvBuyDetailsDoneWeight.setText("משקל גמור: " + numberFormat.format(InventoryApp.buys.get(position).getDoneWeight()) + " קראט ");
        tvBuyDetailsWage.setText("שכר עבודה: " + numberFormat.format(InventoryApp.buys.get(position).getWage()) + "$" + " , " +
                numberFormat.format(InventoryApp.buys.get(position).getWage() / InventoryApp.buys.get(position).getPrice() * 100) + "%" + " , " +
                numberFormat.format(InventoryApp.buys.get(position).getWage() * InventoryApp.buys.get(position).getWeight()) + "$");
        tvBuyDetailsWorkDe.setText("אחוז ליטוש: " + numberFormat.format(InventoryApp.buys.get(position).getWorkDepreciation() * 100) + "%");

        // When the goods are done
        ivDone.setImageResource((InventoryApp.buys.get(position).isDone()) ? R.drawable.done1_icon : R.drawable.not_done1_icon);
        // When the the buy is a polish buy
        ivPolish.setVisibility((InventoryApp.buys.get(position).isPolish()) ? View.VISIBLE : View.INVISIBLE);

        // When the user selected the position
        if (selectedPosition == position) {
            llBuyDetails.setVisibility(View.VISIBLE);

            // When the goods are done
            if (InventoryApp.buys.get(position).isDone()) {

                llBuyDetailsDone.setVisibility(View.VISIBLE);
                // When there is no wage
                tvBuyDetailsWage.setVisibility((InventoryApp.buys.get(position).getWage() == 0) ? View.GONE : View.VISIBLE);

                // Setting all gone first before the calculation
                tvBuyDetailsSortLeftP.setVisibility(View.GONE);
                tvBuyDetailsSortP1.setVisibility(View.GONE);
                tvBuyDetailsSortP2.setVisibility(View.GONE);
                tvBuyDetailsSortP3.setVisibility(View.GONE);
                tvBuyDetailsSortP4.setVisibility(View.GONE);
                tvBuyDetailsSortP5.setVisibility(View.GONE);

                tvBuyDetailsSortLeftW.setVisibility(View.GONE);
                tvBuyDetailsSortW1.setVisibility(View.GONE);
                tvBuyDetailsSortW2.setVisibility(View.GONE);
                tvBuyDetailsSortW3.setVisibility(View.GONE);
                tvBuyDetailsSortW4.setVisibility(View.GONE);
                tvBuyDetailsSortW5.setVisibility(View.GONE);

                tvBuyDetailsSortLeftN.setVisibility(View.GONE);
                tvBuyDetailsSortN1.setVisibility(View.GONE);
                tvBuyDetailsSortN2.setVisibility(View.GONE);
                tvBuyDetailsSortN3.setVisibility(View.GONE);
                tvBuyDetailsSortN4.setVisibility(View.GONE);
                tvBuyDetailsSortN5.setVisibility(View.GONE);

                DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                String whereClause1 = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                String whereClause2 = "fromId = '" + InventoryApp.buys.get(position).getObjectId() + "'";
                sortInfoBuilder.setWhereClause(whereClause1);
                sortInfoBuilder.setWhereClause(whereClause2);

                Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i=0; i < response.size(); i++) {

                            DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                            String whereClause1 = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                            String whereClause2 = "objectId = '" + response.get(i).get("toId") + "'";
                            sortBuilder.setWhereClause(whereClause1);
                            sortBuilder.setWhereClause(whereClause2);

                            switch (i) {

                                case 0:
                                    tvBuyDetailsSortLeftP.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                    tvBuyDetailsSortLeftW.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                    Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                        @Override
                                        public void handleResponse(List<Map> response) {
                                            tvBuyDetailsSortLeftN.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                            // Setting the visibility
                                            tvBuyDetailsSortLeftP.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortLeftW.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortLeftN.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                        }
                                    });
                                    break;

                                case 1:
                                tvBuyDetailsSortP1.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                tvBuyDetailsSortW1.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                    @Override
                                    public void handleResponse(List<Map> response) {
                                        tvBuyDetailsSortN1.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                        // Setting the visibility
                                        tvBuyDetailsSortP1.setVisibility(View.VISIBLE);
                                        tvBuyDetailsSortW1.setVisibility(View.VISIBLE);
                                        tvBuyDetailsSortN1.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                    }
                                });
                                    break;

                                case 2:
                                    tvBuyDetailsSortP2.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                    tvBuyDetailsSortW2.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                    Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                        @Override
                                        public void handleResponse(List<Map> response) {
                                            tvBuyDetailsSortN2.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                            // Setting the visibility
                                            tvBuyDetailsSortP2.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortW2.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortN2.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                        }
                                    });
                                    break;

                                case 3:
                                    tvBuyDetailsSortP3.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                    tvBuyDetailsSortW3.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                    Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                        @Override
                                        public void handleResponse(List<Map> response) {
                                            tvBuyDetailsSortN3.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                            // Setting the visibility
                                            tvBuyDetailsSortP3.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortW3.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortN3.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                        }
                                    });
                                    break;

                                case 4:
                                    tvBuyDetailsSortP4.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                    tvBuyDetailsSortW4.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                    Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                        @Override
                                        public void handleResponse(List<Map> response) {
                                            tvBuyDetailsSortN4.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                            // Setting the visibility
                                            tvBuyDetailsSortP4.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortW4.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortN4.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                        }
                                    });
                                    break;

                                case 5:
                                    tvBuyDetailsSortP5.setText("מחיר: " + (numberFormat.format(response.get(i).get("price"))) + " $ ");
                                    tvBuyDetailsSortW5.setText("משקל: " + (numberFormat.format(response.get(i).get("weight")) + " קראט "));

                                    Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                        @Override
                                        public void handleResponse(List<Map> response) {
                                            tvBuyDetailsSortN5.setText(response.get(0).get("name") + " - " + response.get(0).get("sortCount"));

                                            // Setting the visibility
                                            tvBuyDetailsSortP5.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortW5.setVisibility(View.VISIBLE);
                                            tvBuyDetailsSortN5.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });

            } else {
                llBuyDetailsDone.setVisibility(View.GONE);
            }
        } else {
            llBuyDetails.setVisibility(View.GONE);
        }

        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}
