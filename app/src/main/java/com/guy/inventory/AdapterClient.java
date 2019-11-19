package com.guy.inventory;

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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdapterClient extends ArrayAdapter<Client> {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private Context context;
    private List<Client> clients;
    private int selectedPosition = -1;
    private boolean client = true;
    private final DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
    private double weightSum = 0.0;
    private double saleSum = 0.0;

    AdapterClient(Context context, List<Client> list) {
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

        mLoginFormView = convertView.findViewById(R.id.login_form);
        mProgressView = convertView.findViewById(R.id.login_progress);
        tvLoad = convertView.findViewById(R.id.tvLoad);


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

        if (position == selectedPosition) {
            llClientDetails.setVisibility(View.VISIBLE);

            //Client
            if (client) {
                final DataQueryBuilder saleBuilder = DataQueryBuilder.create();
                saleBuilder.setPageSize(100);
                saleBuilder.setWhereClause("userEmail = '" + InventoryApp.user.getEmail() + "'");
                saleBuilder.setProperties("Sum(saleSum)", "clientName");
                saleBuilder.setGroupBy("clientName");

                Backendless.Data.of("Sale").find(saleBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i=0; i<response.size(); i++) {
                            if (response.get(i).get("clientName") != null) {
                                if (Objects.equals(response.get(i).get("clientName"), InventoryApp.clients.get(selectedPosition).getName())) {
                                    if (response.get(i).get("sum") != null) {
                                        saleSum = (double) response.get(i).get("sum");
                                        break;
                                    }
                                }
                            }
                        }

                        saleBuilder.setProperties("Sum(weight)", "clientName");
                        saleBuilder.setGroupBy("clientName");
                        Backendless.Data.of("Sale").find(saleBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                for (int i=0; i<response.size(); i++) {
                                    if (response.get(i).get("clientName") != null) {
                                        if (Objects.equals(response.get(i).get("clientName"), InventoryApp.clients.get(selectedPosition).getName())) {
                                            if (response.get(i).get("sum") != null) {
                                                weightSum = (double) response.get(i).get("sum");
                                                break;
                                            }
                                        }
                                    }
                                }
                                double price = (weightSum > 0.0) ? (saleSum/weightSum) : 0;
                                tvClientSum.setText("סה\"כ סכום שנמכר: " + nf.format(saleSum) + "$");
                                tvClientWeight.setText("סה\"כ משקל שנמכר: " + nf.format(weightSum) + " קראט ");
                                tvClientPrice.setText("מחיר ממוצע: " + nf.format((price))  + "$");
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
                buyBuilder.setProperties("Sum(sum)", "supplierName");
                buyBuilder.setGroupBy("supplierName");

                Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {
                            if (response.get(i).get("supplierName") != null) {
                                if (Objects.equals(response.get(i).get("supplierName"), InventoryApp.clients.get(selectedPosition).getName())) {
                                    if (response.get(i).get("sum") != null) {
                                        saleSum = (double) response.get(i).get("sum");
                                        break;
                                    }
                                }
                            }
                        }

                        buyBuilder.setProperties("Sum(weight)", "supplierName");
                        buyBuilder.setGroupBy("supplierName");
                        Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                for (int i = 0; i < response.size(); i++) {
                                    if (response.get(i).get("supplierName") != null) {
                                        if (Objects.equals(response.get(i).get("supplierName"), InventoryApp.clients.get(selectedPosition).getName())) {
                                            if (response.get(i).get("sum") != null) {
                                                weightSum = (double) response.get(i).get("sum");
                                                break;
                                            }
                                        }
                                    }
                                }
                                double price = (weightSum > 0.0) ? (saleSum/weightSum) : 0;
                                tvClientSum.setText("סה\"כ סכום שנקנה: " + nf.format(saleSum) + "$");
                                tvClientWeight.setText("סה\"כ משקל שנקנה: " + nf.format(weightSum) + " קראט ");
                                tvClientPrice.setText("מחיר ממוצע: " + nf.format((price))  + "$");
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

        return convertView;
    }

    void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    void setClient(boolean aClient) {
        client = aClient;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
