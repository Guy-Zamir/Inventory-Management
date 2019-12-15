package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Tables.Client;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientAdapter extends ArrayAdapter<Client> {

    private Context context;
    private List<Client> clients;
    private int selectedPosition = -1;

    private boolean client = true;
    private final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    private double weightSum = 0;
    private double saleSum = 0;
    private double price = 0;

    public ClientAdapter(Context context, List<Client> list) {
        super(context, R.layout.client_row_layout, list);
        this.clients = list;
        this.context = context;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.client_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvClientName = convertView.findViewById(R.id.tvClientName);
        TextView tvClientDetails = convertView.findViewById(R.id.tvClientDetails);
        TextView tvClientDetailsLocation = convertView.findViewById(R.id.tvClientDetailsLocation);
        TextView tvClientDetailsPhoneNumber = convertView.findViewById(R.id.tvClientDetailsPhoneNumber);
        TextView tvClientDetailsInsidePhone = convertView.findViewById(R.id.tvClientDetailsInsidePhone);
        TextView tvClientDetailsFax = convertView.findViewById(R.id.tvClientDetailsFax);
        TextView tvClientDetailsWebSite = convertView.findViewById(R.id.tvClientDetailsWebSite);
        final TextView tvClientSum = convertView.findViewById(R.id.tvClientSum);
        final TextView tvClientWeight = convertView.findViewById(R.id.tvClientWeight);
        final TextView tvClientPrice = convertView.findViewById(R.id.tvClientPrice);

        LinearLayout llClientDetails = convertView.findViewById(R.id.llClientDetails);

        // Setting the values to the views
        tvClientName.setText(InventoryApp.clients.get(position).getName());
        tvClientDetailsLocation.setText("כתובת: " + InventoryApp.clients.get(position).getLocation());
        tvClientDetailsPhoneNumber.setText("טלפון: " + InventoryApp.clients.get(position).getPhoneNumber());
        tvClientDetailsInsidePhone.setText("טלפון פנימי: " + InventoryApp.clients.get(position).getInsidePhone());
        tvClientDetailsFax.setText("פקס: " + InventoryApp.clients.get(position).getFax());
        tvClientDetailsWebSite.setText("כתובת אתר אינטרנט: " + InventoryApp.clients.get(position).getWebsite());
        tvClientDetails.setText("פרטים נוספים: " + clients.get(position).getDetails());

        // When the client/supplier is selected from the list
        if (position == selectedPosition) {
            llClientDetails.setVisibility(View.VISIBLE);

            //Client
            if (client) {
                final DataQueryBuilder saleBuilder = DataQueryBuilder.create();
                saleBuilder.setPageSize(100);
                saleBuilder.setWhereClause("userEmail = '" + InventoryApp.user.getEmail() + "'");
                saleBuilder.setProperties("Sum(saleSum)");
                saleBuilder.setGroupBy("Client");
                saleBuilder.addRelated("Client");

                // Getting the sum of sales and exports for the client
                Backendless.Data.of("Sale").find(saleBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {
                            HashMap supplier = (HashMap) response.get(i).get("Client");
                            if (supplier != null) {
                                if (Objects.equals(supplier.get("objectId"), InventoryApp.clients.get(selectedPosition).getObjectId())) {
                                    if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                        saleSum = (int) response.get(i).get("sum");
                                    } else {
                                        saleSum = (double) response.get(i).get("sum");
                                        break;
                                    }
                                }
                            }
                        }

                        saleBuilder.setProperties("Sum(weight)");

                        // Getting the sum of weights (sales and exports) for the client
                        Backendless.Data.of("Sale").find(saleBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                for (int i = 0; i < response.size(); i++) {
                                    HashMap supplier = (HashMap) response.get(i).get("Client");
                                    if (supplier != null) {
                                        if (Objects.equals(supplier.get("objectId"), InventoryApp.clients.get(selectedPosition).getObjectId())) {
                                            if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                                weightSum = (int) response.get(i).get("sum");
                                            } else {
                                                weightSum = (double) response.get(i).get("sum");
                                                break;
                                            }
                                        }
                                    }
                                }

                                double price = (weightSum > 0.0) ? (saleSum / weightSum) : 0;
                                tvClientSum.setText("סה\"כ סכום שנמכר: " + numberFormat.format(saleSum) + "$");
                                tvClientWeight.setText("סה\"כ משקל שנמכר: " + numberFormat.format(weightSum) + " קראט ");
                                tvClientPrice.setText("מחיר ממוצע: " + numberFormat.format((price)) + "$");
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            // Supplier
            } else {
            final DataQueryBuilder buyBuilder = DataQueryBuilder.create();
            buyBuilder.setPageSize(100);
            buyBuilder.setWhereClause("userEmail = '" + InventoryApp.user.getEmail() + "'");
            buyBuilder.addRelated("Supplier");
            buyBuilder.setGroupBy("Supplier");
            buyBuilder.setProperties("Sum(sum)");

            Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {
                    for (int i = 0; i < response.size(); i++) {
                        HashMap supplier = (HashMap) response.get(i).get("Supplier");
                        if (supplier != null) {
                            if (Objects.equals(supplier.get("objectId"), InventoryApp.clients.get(selectedPosition).getObjectId())) {
                                if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                    saleSum = (int) response.get(i).get("sum");
                                } else {
                                    saleSum = (double) response.get(i).get("sum");
                                    break;
                                }
                            }
                        }
                    }

                    buyBuilder.setProperties("Sum(weight)", "Supplier");
                    buyBuilder.setGroupBy("Supplier");
                    Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                        @Override
                        public void handleResponse(List<Map> response) {
                            for (int i = 0; i < response.size(); i++) {
                                HashMap supplier = (HashMap) response.get(i).get("Supplier");
                                if (supplier != null) {
                                    if (Objects.equals(supplier.get("objectId"), InventoryApp.clients.get(selectedPosition).getObjectId())) {
                                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                            weightSum = (int) response.get(i).get("sum");
                                        } else {
                                            weightSum = (double) response.get(i).get("sum");
                                            break;
                                        }
                                    }
                                }
                            }

                            price = (weightSum > 0) ? (saleSum / weightSum) : 0;
                            tvClientSum.setText("סה\"כ סכום שנקנה: " + numberFormat.format(saleSum) + "$");
                            tvClientWeight.setText("סה\"כ משקל שנקנה: " + numberFormat.format(weightSum) + " קראט ");
                            tvClientPrice.setText("מחיר ממוצע: " + numberFormat.format((price)) + "$");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Not selected
        } else {
            llClientDetails.setVisibility(View.GONE);
        }

        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    public void setClient(boolean aClient) {
        client = aClient;
    }
}
