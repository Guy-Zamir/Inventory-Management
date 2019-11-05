package com.guy.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import java.text.DecimalFormat;
import java.util.List;

public class Balance extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llBalanceWage;

    TextView tvBalanceSum, tvBalanceWeight, tvBalancePrice,
            tvBalancePolishSum, tvBalancePolishWeight, tvBalancePolishPrice,
            tvBalanceRoughSum, tvBalanceRoughWeight, tvBalanceRoughPrice,
            tvBalanceWagePrice, tvBalanceWagePre, tvBalanceWageWeight, tvBalanceWageSum;

    Button btnBalanceBuy, btnBalanceSale, btnBalanceGoods, btnBalanceTax;

    double balanceSum, balanceWeight, balancePrice;
    double balancePolishSum, balancePolishWeight, balancePolishPrice;
    double balanceRoughSum, balanceRoughWeight, balanceRoughPrice;

    double saleSum, saleWeight, salePrice;
    double saleRoughSum, saleRoughWeight, saleRoughPrice;
    double salePolishSum, salePolishWeight, salePolishPrice;

    double buySum, buyWeight, buyPrice;
    double buyRoughSum, buyRoughWeight, buyRoughPrice;
    double buyPolishSum, buyPolishWeight, buyPolishPrice;

    double taxSum, taxWeight, taxPrice;
    double taxRoughSum, taxRoughWeight, taxRoughPrice;
    double taxPolishSum, taxPolishWeight, taxPolishPrice;

    double wageSum, wageWeight, wagePer, wagePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnBalanceBuy = findViewById(R.id.btnBalanceBuy);
        btnBalanceSale = findViewById(R.id.btnBalanceSale);
        btnBalanceGoods = findViewById(R.id.btnBalanceGoods);
        btnBalanceTax = findViewById(R.id.btnBalanceTax);

        llBalanceWage = findViewById(R.id.llBalanceWage);

        tvBalanceSum = findViewById(R.id.tvBalanceSum);
        tvBalanceWeight = findViewById(R.id.tvBalanceWeight);
        tvBalancePrice = findViewById(R.id.tvBalancePrice);
        tvBalanceRoughSum = findViewById(R.id.tvBalanceRoughSum);
        tvBalanceRoughWeight = findViewById(R.id.tvBalanceRoughWeight);
        tvBalanceRoughPrice = findViewById(R.id.tvBalanceRoughPrice);
        tvBalancePolishSum = findViewById(R.id.tvBalancePolishSum);
        tvBalancePolishWeight = findViewById(R.id.tvBalancePolishWeight);
        tvBalancePolishPrice = findViewById(R.id.tvBalancePolishPrice);
        tvBalanceWageSum = findViewById(R.id.tvBalanceWageSum);
        tvBalanceWageWeight = findViewById(R.id.tvBalanceWageWeight);
        tvBalanceWagePre = findViewById(R.id.tvBalanceWagePre);
        tvBalanceWagePrice = findViewById(R.id.tvBalanceWagePrice);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מאזן סחורות");
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                                        getInfo();
                                        btnBalanceGoods.setSelected(true);
                                        display(balanceSum, balanceWeight, balancePrice, balanceRoughSum, balanceRoughWeight, balanceRoughPrice, balancePolishSum, balancePolishWeight, balancePolishPrice, true);
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        if (fault.getCode().equals("1009")) {
                                            Toast.makeText(Balance.this, "טרם הוגדרו מכירות", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        showProgress(false);
                                    }
                                });

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                if (fault.getCode().equals("1009")) {
                                    Toast.makeText(Balance.this, "טרם הוגדרו קניות", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                showProgress(false);
                            }
                        });

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (fault.getCode().equals("1009")) {
                            Toast.makeText(Balance.this, "טרם הוגדרו ספקים", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(Balance.this, "טרם הוגדרו לקוחות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });


        btnBalanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(buySum, buyWeight, buyPrice, buyRoughSum, buyRoughWeight, buyRoughPrice, buyPolishSum, buyPolishWeight, buyPolishPrice, false);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(true);
            }
        });

        btnBalanceSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(saleSum, saleWeight, salePrice, saleRoughSum, saleRoughWeight, saleRoughPrice, salePolishSum, salePolishWeight, salePolishPrice, false);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(true);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(balanceSum, balanceWeight, balancePrice, balanceRoughSum, balanceRoughWeight, balanceRoughPrice, balancePolishSum, balancePolishWeight, balancePolishPrice, true);
                btnBalanceGoods.setSelected(true);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(taxSum, taxWeight, taxPrice, taxRoughSum, taxRoughWeight, taxRoughPrice, taxPolishSum, taxPolishWeight, taxPolishPrice, true);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(true);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getInfo() {

        double allPolishSaleSum = 0;
        double allPolishSaleWeight = 0;

        double allRoughSaleSum = 0;
        double allRoughSaleWeight = 0;

        double allNotDoneRoughBuySum = 0;
        double allNotDoneRoughBuyWeight = 0;

        double allRoughBuyDoneSum = 0;
        double allRoughBuyDoneWeight = 0;
        double allRoughBuyDoneOrgWeight = 0;

        double allPolishBuySum = 0;
        double allPolishBuyWeight = 0;

        double allWageSum = 0;

        if (InventoryApp.sales != null) {
            for (Sale sale : InventoryApp.sales) {
                if (sale.isPolish()) {
                    allPolishSaleSum += sale.getSaleSum();
                    allPolishSaleWeight += sale.getWeight();
                } else {
                    allRoughSaleSum += sale.getSaleSum();
                    allRoughSaleWeight += sale.getWeight();
                }
            }
        }

        if (InventoryApp.buys != null) {
            for (Buy buy : InventoryApp.buys) {
                if (!buy.isPolish()) {

                    // Buy Rough Not Done
                    if (!buy.isDone()) {
                        allNotDoneRoughBuySum += buy.getWeight();
                        allNotDoneRoughBuyWeight += buy.getSum();

                        // Buy Done Rough
                    } else {
                        allRoughBuyDoneSum += buy.getSum();
                        allRoughBuyDoneOrgWeight += buy.getWeight();
                        allRoughBuyDoneWeight += buy.getDoneWeight();
                        allWageSum += (buy.getWage() * buy.getWeight());
                    }

                    // Buy Polish
                } else {
                    allPolishBuySum += buy.getSum();
                    allPolishBuyWeight += buy.getWeight();
                }
            }
        }

        saleRoughSum = allRoughSaleSum;
        saleRoughWeight = allRoughSaleWeight;
        saleRoughPrice = saleRoughSum/saleRoughWeight;

        salePolishSum = allPolishSaleSum;
        salePolishWeight = allPolishSaleWeight;
        salePolishPrice = salePolishSum/salePolishWeight;

        saleSum = salePolishSum + saleRoughSum;
        saleWeight = saleRoughWeight + salePolishWeight;
        salePrice = saleSum/saleWeight;



        buyRoughSum = allNotDoneRoughBuySum + allRoughBuyDoneSum;
        buyRoughWeight = allRoughBuyDoneOrgWeight + allNotDoneRoughBuyWeight;
        buyRoughPrice = buyRoughSum/buyRoughWeight;

        buyPolishSum = allPolishBuySum;
        buyPolishWeight = allPolishBuyWeight;
        buyPolishPrice = buyPolishSum/buyPolishWeight;

        buySum = buyRoughSum + buyPolishSum;
        buyWeight = buyRoughWeight + buyPolishWeight;
        buyPrice = buySum/buyWeight;



        wageSum = allWageSum;
        wageWeight = allRoughBuyDoneOrgWeight - allRoughBuyDoneWeight;
        wagePer = 1-(allRoughBuyDoneWeight/allRoughBuyDoneOrgWeight);
        wagePrice = wageSum/allRoughBuyDoneOrgWeight;



        balanceRoughSum = allNotDoneRoughBuySum - saleRoughSum;
        balanceRoughWeight = allNotDoneRoughBuyWeight - saleRoughWeight;
        if (balanceRoughWeight == 0) {
            balanceRoughPrice = 0;
        } else {
            balanceRoughPrice = balanceRoughSum / balanceRoughWeight;
        }

        balancePolishSum = allRoughBuyDoneSum + buyPolishSum - salePolishSum + wageSum;
        balancePolishWeight = allRoughBuyDoneWeight + buyPolishWeight - salePolishWeight;
        if (balancePolishWeight == 0) {
            balancePolishPrice = 0;
        } else {
            balancePolishPrice = balancePolishSum / balancePolishWeight;
        }

        balanceSum = balancePolishSum + balanceRoughSum;
        balanceWeight = balanceRoughWeight + balancePolishWeight;
        if (balanceWeight == 0) {
            balancePrice = 0;
        } else {
            balancePrice = balanceSum / balanceWeight;
        }



        if (allNotDoneRoughBuyWeight == 0) {
            taxRoughPrice = 0;
        } else {
            taxRoughPrice = allNotDoneRoughBuySum / allNotDoneRoughBuyWeight;
        }
        taxRoughWeight = balanceRoughWeight;
        taxRoughSum = taxRoughPrice * taxRoughWeight;
        if ((allRoughBuyDoneWeight + buyPolishWeight) == 0) {
            taxPolishPrice = 0;
        } else {
            taxPolishPrice = (allRoughBuyDoneSum + buyPolishSum) / (allRoughBuyDoneWeight + buyPolishWeight);
        }
        taxPolishWeight = balancePolishWeight;
        taxPolishSum = taxPolishWeight * taxPolishPrice;

        taxSum = taxRoughSum + taxPolishSum;
        taxWeight = taxRoughWeight + taxPolishWeight;
        if (taxWeight == 0) {
            taxPrice = 0;
        } else {
            taxPrice = taxSum / taxWeight;
        }

    }

    @SuppressLint("SetTextI18n")
    public void display(double sum, double weight, double price, double roughSum, double roughWeight, double roughPrice, double polishSum, double polishWeight, double polishPrice, boolean wage) {
        DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
        tvBalanceSum.setText("סכום:  " + nf.format(sum) + "$");
        tvBalanceWeight.setText("משקל:  " + nf.format(weight) + " קראט ");
        tvBalancePrice.setText("מחיר ממוצע:  " + nf.format(price) + "$");
        tvBalanceRoughSum.setText("סכום:  " + nf.format(roughSum) + "$");
        tvBalanceRoughWeight.setText("משקל:  " + nf.format(roughWeight)+ " קראט ");
        tvBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(roughPrice) + "$");
        tvBalancePolishSum.setText("סכום:  " + nf.format(polishSum) + "$");
        tvBalancePolishWeight.setText("משקל:  " + nf.format(polishWeight)+ " קראט ");
        tvBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(polishPrice) + "$");

        if (wage) {
            llBalanceWage.setVisibility(View.VISIBLE);
            tvBalanceWageSum.setText("סכום:  " + nf.format(wageSum) + "$");
            tvBalanceWageWeight.setText("משקל:  " + nf.format(wageWeight)+ " קראט ");
            tvBalanceWagePre.setText("אחוז פחת ממוצע:  " + nf.format((wagePer)*100) + "%");
            tvBalanceWagePrice.setText("מחיר ממוצע:  " + nf.format(wagePrice) + "$");
        } else {
            llBalanceWage.setVisibility(View.GONE);
        }
}
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}