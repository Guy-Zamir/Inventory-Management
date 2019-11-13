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

    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final DataQueryBuilder saleBuilder = DataQueryBuilder.create();
    final DataQueryBuilder exportBuilder = DataQueryBuilder.create();
    final DataQueryBuilder buyBuilder = DataQueryBuilder.create();
    final DataQueryBuilder clientBuilder = DataQueryBuilder.create();

    LinearLayout llBalanceWage;

    TextView tvBalanceSum, tvBalanceWeight, tvBalancePrice,
            tvBalancePolishSum, tvBalancePolishWeight, tvBalancePolishPrice,
            tvBalanceRoughSum, tvBalanceRoughWeight, tvBalanceRoughPrice,
            tvBalanceWagePrice, tvBalanceWagePre, tvBalanceWageWeight, tvBalanceWageSum,
            tvWageHeadline, tvBalanceHeadline, tvPolishHeadline, tvRoughHeadline;

    Button btnBalanceBuy, btnBalanceSale, btnBalanceGoods, btnBalanceTax;

    int pageSize = 100;

    double balanceSum, balanceWeight, balancePrice;
    double balancePolishSum, balancePolishWeight, balancePolishPrice;
    double balanceRoughSum, balanceRoughWeight, balanceRoughPrice;

    double saleSum, saleWeight, salePrice;
    double saleRoughSum, saleRoughWeight, saleRoughPrice;
    double salePolishSum, salePolishWeight, salePolishPrice;

    double exportSum, exportWeight, exportPrice;

    double buySum, buyWeight, buyPrice;
    double buyRoughSum, buyRoughWeight, buyRoughPrice;
    double buyPolishSum, buyPolishWeight, buyPolishPrice;

    double taxSum, taxWeight, taxPrice;
    double taxRoughSum, taxRoughWeight, taxRoughPrice;
    double taxPolishSum, taxPolishWeight, taxPolishPrice;

    double wageSum, wageWeight, wagePer, wagePrice;

    double profit;

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
        tvWageHeadline = findViewById(R.id.tvWageHeadline);
        tvBalanceHeadline = findViewById(R.id.tvBalanceHeadline);
        tvPolishHeadline = findViewById(R.id.tvPolishHeadline);
        tvRoughHeadline = findViewById(R.id.tvRoughHeadline);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מאזן סחורות");
        actionBar.setDisplayHomeAsUpEnabled(true);

        showProgress(true);

        clientBuilder.setWhereClause(whereClause);
        clientBuilder.setSortBy("name");
        clientBuilder.setPageSize(pageSize);

        buyBuilder.setWhereClause(whereClause);
        buyBuilder.setSortBy("buyDate DESC");
        buyBuilder.setPageSize(pageSize);

        exportBuilder.setWhereClause(whereClause);
        exportBuilder.setSortBy("saleDate DESC");
        exportBuilder.setPageSize(pageSize);

        showProgress(true);

        Backendless.Data.of(Client.class).find(clientBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                InventoryApp.clients = response;

                Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
                    @Override
                    public void handleResponse(List<Buy> response) {
                        InventoryApp.buys = response;

                        Backendless.Data.of(Export.class).find(exportBuilder, new AsyncCallback<List<Export>>() {
                            @Override
                            public void handleResponse(List<Export> response) {
                                InventoryApp.exports = response;
                                getSales();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBalanceGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(0);
                btnBalanceGoods.setSelected(true);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(1);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(true);
            }
        });

        btnBalanceSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(2);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(true);
                btnBalanceBuy.setSelected(false);
            }
        });


        btnBalanceTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display(3);
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

        double allPolishExportSum = 0;
        double allPolishExportWeight = 0;

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

        if (InventoryApp.exports != null) {
            for (Export export : InventoryApp.exports) {
                if (export.isPolish()) {
                    allPolishExportSum += export.getSaleSum();
                    allPolishExportWeight += export.getWeight();
                }
            }
        }

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
                        allNotDoneRoughBuySum += buy.getSum();
                        allNotDoneRoughBuyWeight += buy.getWeight();

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

        exportSum = allPolishExportSum;
        exportWeight = allPolishExportWeight;
        if (allPolishExportWeight == 0) {
            exportPrice = 0;
        } else {
            exportPrice = exportSum / exportWeight;
        }


        saleRoughSum = allRoughSaleSum;
        saleRoughWeight = allRoughSaleWeight;
        if (saleRoughWeight == 0) {
            saleRoughPrice = 0;
        } else {
            saleRoughPrice = saleRoughSum / saleRoughWeight;
        }

        salePolishSum = allPolishSaleSum;
        salePolishWeight = allPolishSaleWeight;
        if (salePolishWeight == 0) {
            salePolishPrice = 0;
        } else {
            salePolishPrice = salePolishSum / salePolishWeight;
        }

        saleSum = salePolishSum + saleRoughSum + exportSum;
        saleWeight = saleRoughWeight + salePolishWeight + exportWeight;
        if (saleWeight == 0) {
            salePrice = 0;
        } else {
            salePrice = saleSum / saleWeight;
        }


        buyRoughSum = allNotDoneRoughBuySum + allRoughBuyDoneSum;
        buyRoughWeight = allRoughBuyDoneOrgWeight + allNotDoneRoughBuyWeight;
        if (buyRoughWeight == 0) {
            buyRoughPrice = 0;
        } else {
            buyRoughPrice = buyRoughSum / buyRoughWeight;
        }

        buyPolishSum = allPolishBuySum;
        buyPolishWeight = allPolishBuyWeight;
        if (buyPolishWeight == 0) {
            buyPolishPrice = 0;
        } else {
            buyPolishPrice = buyPolishSum / buyPolishWeight;
        }

        buySum = buyRoughSum + buyPolishSum;
        buyWeight = buyRoughWeight + buyPolishWeight;
        if (buyWeight == 0) {
            buyPrice = 0;
        } else {
            buyPrice = buySum / buyWeight;
        }


        wageSum = allWageSum;
        wageWeight = allRoughBuyDoneOrgWeight - allRoughBuyDoneWeight;
        if (allRoughBuyDoneOrgWeight == 0) {
            wagePer = 0;
        } else {
            wagePer = 1 - (allRoughBuyDoneWeight / allRoughBuyDoneOrgWeight);
        }
        if (allRoughBuyDoneOrgWeight == 0) {
            wagePrice = 0;
        } else {
            wagePrice = wageSum / allRoughBuyDoneOrgWeight;
        }


        balanceRoughSum = allNotDoneRoughBuySum - saleRoughSum;
        balanceRoughWeight = allNotDoneRoughBuyWeight - saleRoughWeight;
        if (balanceRoughWeight == 0) {
            balanceRoughPrice = 0;
        } else {
            balanceRoughPrice = balanceRoughSum / balanceRoughWeight;
        }

        balancePolishSum = allRoughBuyDoneSum + buyPolishSum - salePolishSum - exportSum + wageSum;
        balancePolishWeight = allRoughBuyDoneWeight + buyPolishWeight - salePolishWeight - exportWeight;
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

        profit = taxSum - buySum + saleSum;
    }

    @SuppressLint("SetTextI18n")
    public void display(int head) {

        DecimalFormat nf = new DecimalFormat("#,###,###,###.##");

        switch (head) {
            case 0:
                tvBalanceHeadline.setText("מאזן כולל");
                tvPolishHeadline.setText("מאזן מלוטש");
                tvRoughHeadline.setText("מאזן גלם");
                tvWageHeadline.setText("סיכום שכר עבודה");

                tvBalanceSum.setText("סכום:  " + nf.format(balanceSum) + "$");
                tvBalanceWeight.setText("משקל:  " + nf.format(balanceWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + nf.format(balancePrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + nf.format(balanceRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + nf.format(balanceRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(balanceRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + nf.format(balancePolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + nf.format(balancePolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(balancePolishPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("מחיר ממוצע:  " + nf.format(wagePrice) + "$");
                tvBalanceWageWeight.setText("משקל:  " + nf.format(wageWeight) + " קראט ");
                tvBalanceWagePre.setText("אחוז פחת ממוצע:  " + nf.format((wagePer) * 100) + "%");
                tvBalanceWagePrice.setText("סכום:  " + nf.format(wageSum) + "$");
                break;

            case 1:
                tvBalanceHeadline.setText("כלל הסחורות שניקנו");
                tvPolishHeadline.setText("קניית מלוטש");
                tvRoughHeadline.setText("קניית גלם");

                tvBalanceSum.setText("סכום:  " + nf.format(buySum) + "$");
                tvBalanceWeight.setText("משקל:  " + nf.format(buyWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + nf.format(buyPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + nf.format(buyRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + nf.format(buyRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(buyRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + nf.format(buyPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + nf.format(buyPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(buyPolishPrice) + "$");

                llBalanceWage.setVisibility(View.GONE);
                break;

            case 2:
                tvBalanceHeadline.setText("סחורות שנמכרו");
                tvPolishHeadline.setText("מכירת מלוטש בארץ");
                tvRoughHeadline.setText("מכירת מלוטש ביצוא");
                tvWageHeadline.setText("מכירת גלם");

                tvBalanceSum.setText("סכום:  " + nf.format(saleSum) + "$");
                tvBalanceWeight.setText("משקל:  " + nf.format(saleWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + nf.format(salePrice) + "$");

                tvBalancePolishSum.setText("סכום:  " + nf.format(salePolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + nf.format(salePolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(salePolishPrice) + "$");

                tvBalanceRoughSum.setText("סכום:  " + nf.format(exportSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + nf.format(exportWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(exportPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("סכום:  " + nf.format(saleRoughSum) + "$");
                tvBalanceWageWeight.setText("משקל:  " + nf.format(saleRoughWeight) + " קראט ");
                tvBalanceWagePre.setVisibility(View.GONE);
                tvBalanceWagePrice.setText("מחיר ממוצע:  " + nf.format(saleRoughPrice) + "$");
                break;

            case 3:
                tvBalanceHeadline.setText("מאזן כולל");
                tvPolishHeadline.setText("מאזן מלוטש");
                tvRoughHeadline.setText("מאזן גלם");
                tvWageHeadline.setText("סיכום שכר עבודה");

                tvBalanceSum.setText("סכום:  " + nf.format(taxSum) + "$");
                tvBalanceWeight.setText("משקל:  " + nf.format(taxWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + nf.format(taxPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + nf.format(taxRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + nf.format(taxRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + nf.format(taxRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + nf.format(taxPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + nf.format(taxPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + nf.format(taxPolishPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("מחיר ממוצע:  " + nf.format(wagePrice) + "$");
                tvBalanceWageWeight.setText("משקל:  " + nf.format(wageWeight) + " קראט ");
                tvBalanceWagePre.setText("אחוז פחת ממוצע:  " + nf.format((wagePer) * 100) + "%");
                tvBalanceWagePrice.setText("סכום:  " + nf.format(wageSum) + "$");
                break;
        }
    }

    private void getSales() {
        saleBuilder.setOffset(0);
        saleBuilder.setWhereClause(whereClause);
        saleBuilder.setSortBy("saleDate DESC");
        saleBuilder.setPageSize(pageSize);
        Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
            int offset = 0;
            @Override
            // First 100
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales = response;

                if (InventoryApp.sales.size() == pageSize) {
                    offset += InventoryApp.sales.size();
                    saleBuilder.setOffset(offset);

                    showProgress(true);
                    Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                        @Override
                        // 200
                        public void handleResponse(List<Sale> response) {
                            InventoryApp.sales.addAll(response);

                            if (InventoryApp.sales.size() == (pageSize * 2)) {
                                offset += InventoryApp.sales.size();
                                saleBuilder.setOffset(offset);

                                showProgress(true);
                                Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                                    @Override
                                    // 300
                                    public void handleResponse(List<Sale> response) {
                                        InventoryApp.sales.addAll(response);

                                        if (InventoryApp.sales.size() == pageSize * 3) {
                                            offset += InventoryApp.sales.size();
                                            saleBuilder.setOffset(offset);

                                            showProgress(true);
                                            Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                                                @Override
                                                // 400
                                                public void handleResponse(List<Sale> response) {
                                                    InventoryApp.sales.addAll(response);
                                                    getInfo();
                                                    display(0);
                                                    btnBalanceGoods.setSelected(true);
                                                    showProgress(false);
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    showProgress(false);
                                                    Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            getInfo();
                                            display(0);
                                            btnBalanceGoods.setSelected(true);
                                            showProgress(false);
                                        }
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getInfo();
                                display(0);
                                btnBalanceGoods.setSelected(true);
                                showProgress(false);
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getInfo();
                    display(0);
                    btnBalanceGoods.setSelected(true);
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(Balance.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
