package com.guy.inventory.Activities;

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
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Buy;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BalanceActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    final String WHERE_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";

    LinearLayout llBalanceWage;

    TextView tvBalanceSum, tvBalanceWeight, tvBalancePrice,
            tvBalancePolishSum, tvBalancePolishWeight, tvBalancePolishPrice,
            tvBalanceRoughSum, tvBalanceRoughWeight, tvBalanceRoughPrice,
            tvBalanceWagePrice, tvBalanceWagePre, tvBalanceWageWeight, tvBalanceWageSum,
            tvWageHeadline, tvBalanceHeadline, tvPolishHeadline, tvRoughHeadline;

    Button btnBalanceBuy, btnBalanceSale, btnBalanceGoods, btnBalanceTax;

    final int PAGE_SIZE = 100;

    // All the calculated values
    private double balanceSum, balanceWeight, balancePrice;
    private double balancePolishSum, balancePolishWeight, balancePolishPrice;
    private double balanceRoughSum, balanceRoughWeight, balanceRoughPrice;

    private double saleSum, saleWeight, salePrice;
    private double saleRoughSum, saleRoughWeight, saleRoughPrice;
    private double salePolishSum, salePolishWeight, salePolishPrice;

    private double exportSum, exportWeight, exportPrice;

    private double buySum, buyWeight, buyPrice;
    private double buyRoughSum, buyRoughWeight, buyRoughPrice;
    private double buyPolishSum, buyPolishWeight, buyPolishPrice;

    private double taxSum, taxWeight, taxPrice;
    private double taxRoughSum, taxRoughWeight, taxRoughPrice;
    private double taxPolishSum, taxPolishWeight, taxPolishPrice;

    private double wageSum, wageWeight, wagePer, wagePrice;

    // All the original values
    private double allPolishSaleSum = 0;
    private double allPolishSaleWeight = 0;

    private double allPolishExportSum = 0;
    private double allPolishExportWeight = 0;

    private double allRoughSaleSum = 0;
    private double allRoughSaleWeight = 0;

    private double allNotDoneRoughBuySum = 0;
    private double allNotDoneRoughBuyWeight = 0;

    private double allRoughBuyDoneSum = 0;
    private double allRoughBuyDoneWeight = 0;
    private double allRoughBuyDoneOrgWeight = 0;

    private double allPolishBuySum = 0;
    private double allPolishBuyWeight = 0;

    private double allWageSum = 0;

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
        actionBar.setTitle("מאזנים");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get The Info//
        // Setting all the buys in the array
        DataQueryBuilder buyBuilder = DataQueryBuilder.create();
        buyBuilder.setWhereClause(WHERE_CLAUSE);
        buyBuilder.setPageSize(PAGE_SIZE);

        showProgress(true);
        Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                if (InventoryApp.buys != null) {
                    for (Buy buy : InventoryApp.buys) {
                        if (!buy.isPolish()) {

                            // Buy Rough Not DoneActivity
                            if (!buy.isDone()) {
                                allNotDoneRoughBuySum += buy.getSum();
                                allNotDoneRoughBuyWeight += buy.getWeight();

                                // Buy DoneActivity Rough
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

                // Getting the sale sum of exports
                DataQueryBuilder exportSumBuilder = DataQueryBuilder.create();
                exportSumBuilder.setWhereClause(WHERE_CLAUSE);
                exportSumBuilder.setProperties("Sum(saleSum)");
                Backendless.Data.of("Export").find(exportSumBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        if (response.get(0).get("sum") != null) {
                            if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                allPolishExportSum = (int) response.get(0).get("sum");
                            } else {
                                allPolishExportSum = (double) response.get(0).get("sum");
                            }
                        }

                        // Getting the weight sum of exports
                        DataQueryBuilder exportWeightBuilder = DataQueryBuilder.create();
                        exportWeightBuilder.setWhereClause(WHERE_CLAUSE);
                        exportWeightBuilder.setProperties("Sum(weight)");
                        Backendless.Data.of("Export").find(exportWeightBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                if (response.get(0).get("sum") != null) {
                                    if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                        allPolishExportWeight = (int) response.get(0).get("sum");
                                    } else {
                                        allPolishExportWeight = (double) response.get(0).get("sum");
                                    }
                                }

                                // Getting the sale sum of sales
                                DataQueryBuilder saleSumBuilder = DataQueryBuilder.create();
                                saleSumBuilder.setWhereClause(WHERE_CLAUSE);
                                saleSumBuilder.setProperties("Sum(saleSum)", "polish");
                                saleSumBuilder.setGroupBy("polish");
                                Backendless.Data.of("Sale").find(saleSumBuilder, new AsyncCallback<List<Map>>() {
                                    @Override
                                    public void handleResponse(List<Map> response) {
                                        if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                            allPolishSaleSum = (int) response.get(1).get("sum");
                                        } else {
                                            allPolishSaleSum = (double) response.get(1).get("sum");
                                        }
                                        if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                            allRoughSaleSum = (int) response.get(0).get("sum");
                                        } else {
                                            allRoughSaleSum = (double) response.get(0).get("sum");
                                        }

                                        // Getting the weight sum of sales
                                        DataQueryBuilder saleWeightBuilder = DataQueryBuilder.create();
                                        saleWeightBuilder.setWhereClause(WHERE_CLAUSE);
                                        saleWeightBuilder.setProperties("Sum(weight)", "polish");
                                        saleWeightBuilder.setGroupBy("polish");
                                        Backendless.Data.of("Sale").find(saleWeightBuilder, new AsyncCallback<List<Map>>() {
                                            @Override
                                            public void handleResponse(List<Map> response) {
                                                if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                    allPolishSaleWeight = (int) response.get(1).get("sum");
                                                } else {
                                                    allPolishSaleWeight = (double) response.get(1).get("sum");
                                                }

                                                if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                                    allRoughSaleWeight = (int) response.get(0).get("sum");
                                                } else {
                                                    allRoughSaleWeight = (double) response.get(0).get("sum");
                                                }

                                                showProgress(false);

                                                //Calculate The Values//
                                                exportSum = allPolishExportSum;
                                                exportWeight = allPolishExportWeight;
                                                exportPrice = (exportWeight > 0) ? (exportSum / exportWeight) : 0;

                                                saleRoughSum = allRoughSaleSum;
                                                saleRoughWeight = allRoughSaleWeight;
                                                saleRoughPrice = (saleRoughWeight > 0) ? (saleRoughSum / saleRoughWeight) : 0;

                                                salePolishSum = allPolishSaleSum;
                                                salePolishWeight = allPolishSaleWeight;
                                                salePolishPrice = (salePolishWeight > 0) ? (salePolishSum / salePolishWeight) : 0;

                                                saleSum = salePolishSum + saleRoughSum + exportSum;
                                                saleWeight = saleRoughWeight + salePolishWeight + exportWeight;
                                                salePrice = (saleWeight > 0) ? (saleSum / saleWeight) : 0;

                                                buyRoughSum = allNotDoneRoughBuySum + allRoughBuyDoneSum;
                                                buyRoughWeight = allRoughBuyDoneOrgWeight + allNotDoneRoughBuyWeight;
                                                buyRoughPrice = (buyRoughWeight > 0) ? (buyRoughSum / buyRoughWeight) : 0;

                                                buyPolishSum = allPolishBuySum;
                                                buyPolishWeight = allPolishBuyWeight;
                                                buyPolishPrice = (buyPolishWeight > 0) ? (buyPolishSum / buyPolishWeight) : 0;

                                                buySum = buyRoughSum + buyPolishSum;
                                                buyWeight = buyRoughWeight + buyPolishWeight;
                                                buyPrice = (buyWeight > 0) ? (buySum / buyWeight) : 0;

                                                wageSum = allWageSum;
                                                wageWeight = allRoughBuyDoneOrgWeight - allRoughBuyDoneWeight;
                                                wagePer = (allRoughBuyDoneOrgWeight > 0) ? (1 - (allRoughBuyDoneWeight / allRoughBuyDoneOrgWeight)) : 0;
                                                wagePrice = (allRoughBuyDoneOrgWeight > 0) ? (wageSum / allRoughBuyDoneOrgWeight) : 0;

                                                balanceRoughSum = allNotDoneRoughBuySum - saleRoughSum;
                                                balanceRoughWeight = allNotDoneRoughBuyWeight - saleRoughWeight;
                                                balanceRoughPrice = (balanceRoughWeight > 0) ? (balanceRoughSum / balanceRoughWeight) : 0;

                                                balancePolishSum = allRoughBuyDoneSum + buyPolishSum - salePolishSum - exportSum + wageSum;
                                                balancePolishWeight = allRoughBuyDoneWeight + buyPolishWeight - salePolishWeight - exportWeight;
                                                balancePolishPrice = (balancePolishWeight > 0) ? (balancePolishSum / balancePolishWeight) : 0;

                                                balanceSum = balancePolishSum + balanceRoughSum;
                                                balanceWeight = balanceRoughWeight + balancePolishWeight;
                                                balancePrice = (balanceWeight > 0) ? (balanceSum / balanceWeight) : 0;

                                                taxRoughPrice = (allNotDoneRoughBuyWeight > 0) ? (allNotDoneRoughBuySum / allNotDoneRoughBuyWeight) : 0;

                                                taxRoughWeight = balanceRoughWeight;
                                                taxRoughSum = taxRoughPrice * taxRoughWeight;
                                                taxPolishPrice = ((allRoughBuyDoneWeight + buyPolishWeight) > 0) ? (allRoughBuyDoneSum + buyPolishSum) / (allRoughBuyDoneWeight + buyPolishWeight) : 0;
                                                taxPolishWeight = balancePolishWeight;
                                                taxPolishSum = taxPolishWeight * taxPolishPrice;

                                                taxSum = taxRoughSum + taxPolishSum;
                                                taxWeight = taxRoughWeight + taxPolishWeight;
                                                taxPrice = (taxWeight > 0) ? (taxSum / taxWeight) : 0;

                                                setTheText(0);
                                                btnBalanceGoods.setSelected(true);

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                showProgress(false);
                                                Toast.makeText(BalanceActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(BalanceActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(BalanceActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(BalanceActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(BalanceActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        btnBalanceGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(0);
                btnBalanceGoods.setSelected(true);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(1);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(true);
            }
        });

        btnBalanceSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(2);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(false);
                btnBalanceSale.setSelected(true);
                btnBalanceBuy.setSelected(false);
            }
        });


        btnBalanceTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(3);
                btnBalanceGoods.setSelected(false);
                btnBalanceTax.setSelected(true);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setTheText(int head) {

        switch (head) {
            case 0:
                tvBalanceHeadline.setText("מאזן כולל");
                tvPolishHeadline.setText("מאזן מלוטש");
                tvRoughHeadline.setText("מאזן גלם");
                tvWageHeadline.setText("סיכום שכר עבודה");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(balanceSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(balanceWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(balancePrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(balanceRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(balanceRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(balanceRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(balancePolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(balancePolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(balancePolishPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("מחיר ממוצע:  " + numberFormat.format(wagePrice) + "$");
                tvBalanceWageWeight.setText("משקל:  " + numberFormat.format(wageWeight) + " קראט ");
                tvBalanceWagePre.setText("אחוז פחת ממוצע:  " + numberFormat.format((wagePer) * 100) + "%");
                tvBalanceWagePrice.setText("סכום:  " + numberFormat.format(wageSum) + "$");
                break;

            case 1:
                tvBalanceHeadline.setText("כלל הסחורות שניקנו");
                tvPolishHeadline.setText("קניית מלוטש");
                tvRoughHeadline.setText("קניית גלם");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(buySum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(buyWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(buyPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(buyRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(buyRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(buyRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(buyPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(buyPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(buyPolishPrice) + "$");

                llBalanceWage.setVisibility(View.GONE);
                break;

            case 2:
                tvBalanceHeadline.setText("סחורות שנמכרו");
                tvPolishHeadline.setText("מכירת מלוטש בארץ");
                tvRoughHeadline.setText("מכירת מלוטש ביצוא");
                tvWageHeadline.setText("מכירת גלם");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(saleSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(saleWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(salePrice) + "$");

                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(salePolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(salePolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(salePolishPrice) + "$");

                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(exportSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(exportWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(exportPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("סכום:  " + numberFormat.format(saleRoughSum) + "$");
                tvBalanceWageWeight.setText("משקל:  " + numberFormat.format(saleRoughWeight) + " קראט ");
                tvBalanceWagePre.setVisibility(View.GONE);
                tvBalanceWagePrice.setText("מחיר ממוצע:  " + numberFormat.format(saleRoughPrice) + "$");
                break;

            case 3:
                tvBalanceHeadline.setText("מאזן כולל");
                tvPolishHeadline.setText("מאזן מלוטש");
                tvRoughHeadline.setText("מאזן גלם");
                tvWageHeadline.setText("סיכום שכר עבודה");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(taxSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(taxWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(taxPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(taxRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(taxRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(taxRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(taxPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(taxPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(taxPolishPrice) + "$");

                llBalanceWage.setVisibility(View.VISIBLE);
                tvBalanceWageSum.setText("מחיר ממוצע:  " + numberFormat.format(wagePrice) + "$");
                tvBalanceWageWeight.setText("משקל:  " + numberFormat.format(wageWeight) + " קראט ");
                tvBalanceWagePre.setText("אחוז פחת ממוצע:  " + numberFormat.format((wagePer) * 100) + "%");
                tvBalanceWagePrice.setText("סכום:  " + numberFormat.format(wageSum) + "$");
                break;
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
