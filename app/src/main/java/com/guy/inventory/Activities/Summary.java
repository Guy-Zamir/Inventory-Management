package com.guy.inventory.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Classes.Buy;
import com.guy.inventory.Classes.Client;
import com.guy.inventory.Classes.Sale;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;

import java.text.DecimalFormat;
import java.util.List;

public class Summary extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvSummaryBalance, tvSummaryAllSales, tvSummaryAllBuys, tvSummaryAllWage;
    double salesSum = 0, saleWeight = 0, buyWeight = 0, buyDoneWeight = 0, buySum = 0, buyPolishWeight = 0, buyPolishSum = 0, wageSum = 0, wageWeight = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSummaryBalance = findViewById(R.id.tvSummaryBalance);
        tvSummaryAllSales = findViewById(R.id.tvSummaryAllSales);
        tvSummaryAllBuys = findViewById(R.id.tvSummaryAllBuys);
        tvSummaryAllWage = findViewById(R.id.tvSummaryAllWage);


        showProgress(true);
        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("objectId");
        queryBuilder.setPageSize(100);

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Client.class).find(queryBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                InventoryApp.clients = response;

                String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(whereClause);
                queryBuilder.setGroupBy("objectId");
                queryBuilder.setPageSize(100);

                Backendless.Data.of(Supplier.class).find(queryBuilder, new AsyncCallback<List<Supplier>>() {
                    @Override
                    public void handleResponse(List<Supplier> response) {
                        InventoryApp.suppliers = response;

                        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause(whereClause);
                        queryBuilder.setGroupBy("created");
                        queryBuilder.setPageSize(100);

                        Backendless.Data.of(Buy.class).find(queryBuilder, new AsyncCallback<List<Buy>>() {
                            @Override
                            public void handleResponse(List<Buy> response) {
                                InventoryApp.buys = response;

                                String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                                queryBuilder.setWhereClause(whereClause);
                                queryBuilder.setGroupBy("created");
                                queryBuilder.setPageSize(100);

                                Backendless.Data.of(Sale.class).find(queryBuilder, new AsyncCallback<List<Sale>>() {
                                    @Override
                                    public void handleResponse(List<Sale> response) {
                                        InventoryApp.sales = response;
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        if (fault.getCode().equals("1009")) {
                                            Toast.makeText(Summary.this, "טרם הוגדרו מכירות", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Summary.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        showProgress(false);
                                    }
                                });

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                if (fault.getCode().equals("1009")) {
                                    Toast.makeText(Summary.this, "טרם הוגדרו קניות", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Summary.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                showProgress(false);
                            }
                        });

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (fault.getCode().equals("1009")) {
                            Toast.makeText(Summary.this, "טרם הוגדרו ספקים", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Summary.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(Summary.this, "טרם הוגדרו לקוחות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Summary.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        if (InventoryApp.sales != null) {
            for (Sale sale : InventoryApp.sales) {
                salesSum += sale.getSaleSum();
                saleWeight += sale.getWeight();
            }
        }

        if (InventoryApp.buys != null) {
            for (Buy buy : InventoryApp.buys) {
                if (!buy.isPolish()) {
                    buySum += buy.getSum();
                    buyDoneWeight += buy.getDoneWeight();
                    buyWeight += buy.getWeight();
                    wageSum += (buy.getWage() * buy.getWeight());
                } else {
                    buyPolishSum += buy.getSum();
                    buyPolishWeight += buy.getWeight();
                }
            }
        }
        wageWeight = buyWeight - buyDoneWeight;

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        tvSummaryBalance.setText("מאזן: " + nf.format(salesSum - buySum - wageSum) + "$ / " + nf.format(buyDoneWeight - saleWeight));
        tvSummaryAllSales.setText("מכירות מתחילת השנה: " + nf.format(salesSum) + "$ / " + nf.format(saleWeight));
        tvSummaryAllBuys.setText("קניות מתחילת השנה: " + nf.format(buySum) + "$ / " + nf.format(buyWeight));
        tvSummaryAllWage.setText("שכר עבודה/פחת: " + nf.format(wageSum) + "$ / " + nf.format(wageWeight) + " / " + nf.format((wageWeight/ buyWeight)*100) + " % / " + nf.format(wageWeight/wageSum) + "$");
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
