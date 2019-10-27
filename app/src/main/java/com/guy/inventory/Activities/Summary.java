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

    TextView tvSummaryBalanceSum, tvSummaryBalanceWeight, tvSummarySaleSum, tvSummarySaleWeight, tvSummaryBuySum, tvSummaryBuyWeight, tvSummaryBuyNoSum, tvSummaryBuyNoWeight, tvSummaryBuyPolishSum, tvSummaryBuyPolishWeight,
            tvSummaryWageSum, tvSummaryWageWeight, tvSummaryWagePer, tvSummaryWagePrice;

    double balanceSum = 0;
    double balanceWeight = 0;
    double saleSum = 0;
    double saleWeight = 0;
    double buySum = 0;
    double buyWeight = 0;
    double buyPolishSum = 0;
    double buyPolishWeight = 0;
    double buyNoSum = 0;
    double buyNoWeight = 0;
    double wageSum = 0;
    double wageWeight = 0;
    double wagePer = 0;
    double wagePrice = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSummaryBalanceSum = findViewById(R.id.tvSummaryBalanceSum);
        tvSummaryBalanceWeight = findViewById(R.id.tvSummaryBalanceWeight);
        tvSummarySaleSum = findViewById(R.id.tvSummarySaleSum);
        tvSummarySaleWeight = findViewById(R.id.tvSummarySaleWeight);
        tvSummaryBuySum = findViewById(R.id.tvSummaryBuySum);
        tvSummaryBuyWeight = findViewById(R.id.tvSummaryBuyWeight);
        tvSummaryBuyPolishSum = findViewById(R.id.tvSummaryBuyPolishSum);
        tvSummaryBuyPolishWeight = findViewById(R.id.tvSummaryBuyPolishWeight);
        tvSummaryBuyNoSum = findViewById(R.id.tvSummaryBuyNoSum);
        tvSummaryBuyNoWeight = findViewById(R.id.tvSummaryBuyNoWeight);
        tvSummaryWageSum = findViewById(R.id.tvSummaryWageSum);
        tvSummaryWageWeight = findViewById(R.id.tvSummaryWageWeight);
        tvSummaryWagePer = findViewById(R.id.tvSummaryWagePer);
        tvSummaryWagePrice = findViewById(R.id.tvSummaryWagePrice);


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
                saleSum += sale.getSaleSum();
                saleWeight += sale.getWeight();
            }
        }

        if (InventoryApp.buys != null) {
            for (Buy buy : InventoryApp.buys) {
                if (!buy.isPolish()) {
                    if (!buy.isDone()) {
                        double buyDoneSum = 0;
                        double buyDoneWeight = 0;
                        buyDoneWeight += buy.getDoneWeight();
                        buyDoneSum += buy.getSum();
                    } else {
                        buySum += buy.getSum();
                        buyWeight += buy.getWeight();
                        buyWeight += buy.getWeight();
                        wageSum += (buy.getWage() * buy.getWeight());
                    }
                } else{
                        buyPolishSum += buy.getSum();
                        buyPolishWeight += buy.getWeight();
                    }
            }
        }
        wageWeight = buyWeight - buyDoneWeight;

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        double balanceSum = 0;
        double balanceWeight = 0;
        double saleSum = 0;
        double saleWeight = 0;
        double buySum = 0;
        double buyWeight = 0;
        double buyPolishSum = 0;
        double buyPolishWeight = 0;
        double buyNoSum = 0;
        double buyNoWeight = 0;
        double wageSum = 0;
        double wageWeight = 0;
        double wagePer = 0;
        double wagePrice = 0;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
