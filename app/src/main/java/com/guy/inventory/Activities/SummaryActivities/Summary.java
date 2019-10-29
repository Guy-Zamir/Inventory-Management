package com.guy.inventory.Activities.SummaryActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.InventoryApp;
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

    TextView tvSummaryBalanceSum, tvSummaryBalanceWeight, tvSummaryBalancePrice, tvSummaryBalanceRoughSum,
            tvSummaryBalanceRoughWeight, tvSummaryBalanceRoughPrice, tvSummaryBalancePolishSum,
            tvSummaryBalancePolishWeight, tvSummaryBalancePolishPrice;


    Button btnSummaryBuy, btnSummarySale, btnSummaryWage;
    double balanceSum;
    double balanceWeight;
    double balancePolishSum;
    double balancePolishWeight;
    double balanceRoughSum;
    double balanceRoughWeight;
    double saleSum;
    double saleWeight;
    double buySum;
    double buyWeight;
    double buyPolishSum;
    double buyPolishWeight;
    double wageSum;
    double wageWeight;
    double wagePer;
    double wagePrice;
    double balancePrice;
    double balancePolishPrice;
    double balanceRoughPrice;
    double buyPrice;
    double buyPolishPrice;
    double buyRoughPrice;
    double salePrice;
    double salePolishPrice;
    double saleRoughPrice;
    double buyRoughSum;
    double buyRoughWeight;
    double salePolishSum;
    double salePolishWeight;
    double saleRoughSum;
    double saleRoughWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnSummaryBuy = findViewById(R.id.btnSummaryBuy);
        btnSummarySale = findViewById(R.id.btnSummarySale);
        btnSummaryWage = findViewById(R.id.btnSummaryWage);

        tvSummaryBalanceSum = findViewById(R.id.tvSummaryBalanceSum);
        tvSummaryBalanceWeight = findViewById(R.id.tvSummaryBalanceWeight);
        tvSummaryBalancePrice = findViewById(R.id.tvSummaryBalancePrice);
        tvSummaryBalanceRoughSum = findViewById(R.id.tvSummaryBalanceRoughSum);
        tvSummaryBalanceRoughWeight = findViewById(R.id.tvSummaryBalanceRoughWeight);
        tvSummaryBalanceRoughPrice = findViewById(R.id.tvSummaryBalanceRoughPrice);
        tvSummaryBalancePolishSum = findViewById(R.id.tvSummaryBalancePolishSum);
        tvSummaryBalancePolishWeight = findViewById(R.id.tvSummaryBalancePolishWeight);
        tvSummaryBalancePolishPrice = findViewById(R.id.tvSummaryBalancePolishPrice);

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
                                        display();
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


        btnSummaryBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Summary.this, BuySummary.class);
                intent.putExtra("buySum", buySum);
                intent.putExtra("buyWeight", buyWeight);
                intent.putExtra("buyPrice", buyPrice);
                intent.putExtra("buyRoughSum", buyRoughSum);
                intent.putExtra("buyRoughWeight", buyRoughWeight);
                intent.putExtra("buyRoughPrice", buyRoughPrice);
                intent.putExtra("buyPolishSum", buyPolishSum);
                intent.putExtra("buyPolishWeight", buyPolishWeight);
                intent.putExtra("buyPolishPrice", buyPolishPrice);
                startActivity(intent);
            }
        });

        btnSummarySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Summary.this, SaleSummary.class);
                intent.putExtra("saleSum", saleSum);
                intent.putExtra("saleWeight", saleWeight);
                intent.putExtra("salePrice", salePrice);
                intent.putExtra("saleRoughSum", saleRoughSum);
                intent.putExtra("saleRoughWeight", saleRoughWeight);
                intent.putExtra("saleRoughPrice", saleRoughPrice);
                intent.putExtra("salePolishSum", salePolishSum);
                intent.putExtra("salePolishWeight", salePolishWeight);
                intent.putExtra("salePolishPrice", salePolishPrice);
                startActivity(intent);

            }
        });

        btnSummaryWage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Summary.this, WageSummary.class);
                intent.putExtra("wageSum", wageSum);
                intent.putExtra("wageWeight", wageWeight);
                intent.putExtra("wagePrice", wagePrice);
                intent.putExtra("wagePer", wagePer);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void display() {
        double doneOrgWeight = 0;
        double doneWeight = 0;
        double doneSum = 0;

        if (InventoryApp.sales != null) {
            for (Sale sale : InventoryApp.sales) {
                if (sale.isPolish()) {
                    salePolishSum += sale.getSaleSum();
                    salePolishWeight += sale.getWeight();
                } else {
                    saleRoughSum += sale.getSaleSum();
                    saleRoughWeight += sale.getWeight();
                }
            }
        }

        if (InventoryApp.buys != null) {
            for (Buy buy : InventoryApp.buys) {
                if (!buy.isPolish()) {
                    if (!buy.isDone()) {
                        balanceRoughWeight += buy.getWeight();
                        balanceRoughSum += buy.getSum();
                    } else {
                        doneSum += buy.getSum();
                        doneOrgWeight += buy.getWeight();
                        doneWeight += buy.getDoneWeight();
                        wageSum += (buy.getWage() * buy.getWeight());
                    }
                } else {
                    buyPolishSum += buy.getSum();
                    buyPolishWeight += buy.getWeight();
                }
            }
        }

        buySum = doneSum + balanceRoughSum + buyPolishSum;
        buyWeight = doneOrgWeight + balanceRoughWeight + buyPolishWeight;
        buyPrice = buySum/buyWeight;

        wageWeight = doneOrgWeight - doneWeight;
        wagePer = doneWeight/doneOrgWeight;
        wagePrice = wageSum/doneOrgWeight;

        saleSum = salePolishSum + saleRoughSum;
        saleWeight = salePolishWeight + saleRoughWeight;

        balancePolishSum = doneSum + buyPolishSum - saleSum - wageSum;
        balancePolishWeight = doneWeight + buyPolishWeight - saleWeight;

        balanceSum = balancePolishSum + balanceRoughSum;
        balanceWeight = balancePolishWeight + balanceRoughWeight;

        balancePrice = balanceSum/balanceWeight;
        balancePolishPrice = balancePolishSum/balancePolishWeight;
        balanceRoughPrice = balanceRoughSum/balanceRoughWeight;

        buyPrice = buySum/buyPrice;
        buyPolishPrice = buyPolishSum/buyPolishWeight;
        buyRoughPrice = buyRoughSum/buyRoughWeight;

        buyRoughSum = balanceRoughSum + doneSum;
        buyRoughWeight = balanceRoughWeight + doneOrgWeight;
        buyRoughPrice = buyRoughSum/buyRoughWeight;

        salePrice = saleSum/saleWeight;
        salePolishPrice = salePolishSum/salePolishWeight;
        saleRoughPrice = saleRoughSum/saleRoughWeight;

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvSummaryBalanceSum.setText("סכום:  " + nf.format(balanceSum) + "$");
        tvSummaryBalanceWeight.setText("משקל:  " + nf.format(balanceWeight));
        tvSummaryBalancePrice.setText("מחיר ממוצע:  " + nf.format(balancePrice) + "$");
        tvSummaryBalanceRoughSum.setText("סכום:  " + nf.format(balanceRoughSum) + "$");
        tvSummaryBalanceRoughWeight.setText("משקל:  " + nf.format(balanceRoughWeight));
        tvSummaryBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(balanceRoughPrice) + "$");
        tvSummaryBalancePolishSum.setText("סכום:  " + nf.format(balancePolishSum) + "$");
        tvSummaryBalancePolishWeight.setText("משקל:  " + nf.format(balancePolishWeight));
        tvSummaryBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(balancePolishPrice) + "$");
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
