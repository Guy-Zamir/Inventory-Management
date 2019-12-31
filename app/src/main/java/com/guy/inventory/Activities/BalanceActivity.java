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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BalanceActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    String SALE_KIND = "sale";

    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String SALE_CLAUSE = "kind = '" + SALE_KIND + "'";

    LinearLayout llBalanceSort;

    TextView tvBalanceSum, tvBalanceWeight, tvBalancePrice,
            tvBalancePolishSum, tvBalancePolishWeight, tvBalancePolishPrice,
            tvBalanceRoughSum, tvBalanceRoughWeight, tvBalanceRoughPrice,
            tvBalanceSortProfit, tvBalanceSortProfitPrice, tvBalanceSortProfitPre, tvBalanceSortNum,
            tvSortHeadline, tvBalanceHeadline, tvPolishHeadline, tvRoughHeadline;

    Button btnBalanceBuy, btnBalanceSale, btnBalanceGoods, btnBalanceOpen;

    // All the calculated values
    private double weight;
    private double PolishWeight;
    private double RoughWeight;

    private double saleSum, saleWeight, salePrice;
    private double saleRoughSum, saleRoughWeight, saleRoughPrice;
    private double salePolishSum, salePolishWeight, salePolishPrice;

    private double exportSum, exportWeight, exportPrice;

    private double buySum, buyWeight, buyPrice;
    private double buyRoughSum, buyRoughWeight, buyRoughPrice;
    private double buyPolishSum, buyPolishWeight, buyPolishPrice;

    private double costSum, costPrice;
    private double roughSum, roughCostPrice;
    private double polishSum, polishCostPrice;

    private double openStockRoughPrice, openStockPolishPrice;
    private double openStockSum, openStockWeight, openStockPrice;

    private double sortProfit, sortProfitPre, sortProfitPrice;
    private int sortNum;

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

    private double sortSaleSum = 0;
    private double sortSum = 0;

    private double openStockPolishSum = 0;
    private double openStockPolishWeight = 0;
    private double openStockRoughSum = 0;
    private double openStockRoughWeight = 0;

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
        btnBalanceOpen = findViewById(R.id.btnBalanceOpen);

        llBalanceSort = findViewById(R.id.llBalanceSort);

        tvBalanceSum = findViewById(R.id.tvBalanceSum);
        tvBalanceWeight = findViewById(R.id.tvBalanceWeight);
        tvBalancePrice = findViewById(R.id.tvBalancePrice);
        tvBalanceRoughSum = findViewById(R.id.tvBalanceRoughSum);
        tvBalanceRoughWeight = findViewById(R.id.tvBalanceRoughWeight);
        tvBalanceRoughPrice = findViewById(R.id.tvBalanceRoughPrice);
        tvBalancePolishSum = findViewById(R.id.tvBalancePolishSum);
        tvBalancePolishWeight = findViewById(R.id.tvBalancePolishWeight);
        tvBalancePolishPrice = findViewById(R.id.tvBalancePolishPrice);
        tvBalanceSortNum = findViewById(R.id.tvBalanceSortNum);
        tvBalanceSortProfitPre = findViewById(R.id.tvBalanceSortProfitPre);
        tvBalanceSortProfitPrice = findViewById(R.id.tvBalanceSortProfitPrice);
        tvBalanceSortProfit = findViewById(R.id.tvBalanceSortProfit);
        tvSortHeadline = findViewById(R.id.tvSortHeadline);
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
        buyBuilder.setWhereClause(EMAIL_CLAUSE);
        buyBuilder.setProperties("Sum(sum)", "done", "polish");
        buyBuilder.setGroupBy("done", "polish");

        showProgress(true);
        Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {
                if (response.size() >= 1) {
                    if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                        allNotDoneRoughBuySum += (int) response.get(0).get("sum");
                    } else {
                        allNotDoneRoughBuySum += (double) response.get(0).get("sum");
                    }

                    // Not sort buy polish
                    if (response.size() >= 2) {
                        if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                            allPolishBuySum += (int) response.get(1).get("sum");
                        } else {
                            allPolishBuySum += (double) response.get(1).get("sum");
                        }
                    }
                    if (response.size() >= 3) {
                        if (Objects.requireNonNull(response.get(2).get("sum")).getClass().equals(Integer.class)) {
                            allPolishBuySum += (int) response.get(2).get("sum");
                        } else {
                            allPolishBuySum += (double) response.get(2).get("sum");
                        }
                    }
                }

                DataQueryBuilder buyBuilder = DataQueryBuilder.create();
                buyBuilder.setWhereClause(EMAIL_CLAUSE);
                buyBuilder.setProperties("Sum(weight)", "done", "polish");
                buyBuilder.setGroupBy("done", "polish");

                Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        if (response.size() >= 1) {
                            if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                allNotDoneRoughBuyWeight += (int) response.get(0).get("sum");
                            } else {
                                allNotDoneRoughBuyWeight += (double) response.get(0).get("sum");
                            }

                            // Not sort buy polish
                            if (response.size() >= 2) {
                                if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                    allPolishBuyWeight += (int) response.get(1).get("sum");
                                } else {
                                    allPolishBuyWeight += (double) response.get(1).get("sum");
                                }
                            }
                            if (response.size() >= 3) {
                                if (Objects.requireNonNull(response.get(2).get("sum")).getClass().equals(Integer.class)) {
                                    allPolishBuyWeight += (int) response.get(2).get("sum");
                                } else {
                                    allPolishBuyWeight += (double) response.get(2).get("sum");
                                }
                            }
                        }

                        DataQueryBuilder buyBuilder = DataQueryBuilder.create();
                        buyBuilder.setWhereClause(EMAIL_CLAUSE);
                        buyBuilder.setProperties("Sum(doneWeight)");
                        buyBuilder.setHavingClause("polish = false");
                        buyBuilder.setHavingClause("done = true");

                        Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                if (response.size() >= 1) {
                                    if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                        allRoughBuyDoneWeight += (int) response.get(0).get("sum");
                                    } else {
                                        allRoughBuyDoneWeight += (double) response.get(0).get("sum");
                                    }
                                }

                                // Getting the sale sum of exports
                                DataQueryBuilder exportSumBuilder = DataQueryBuilder.create();
                                exportSumBuilder.setWhereClause(EMAIL_CLAUSE);
                                exportSumBuilder.setGroupBy("kind");
                                exportSumBuilder.setProperties("Sum(saleSum)");

                                Backendless.Data.of("Sale").find(exportSumBuilder, new AsyncCallback<List<Map>>() {
                                    @Override
                                    public void handleResponse(List<Map> response) {
                                        if (response.size() >= 1) {
                                            if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                                allPolishExportSum += (int) response.get(0).get("sum");
                                            } else {
                                                allPolishExportSum += (double) response.get(0).get("sum");
                                            }
                                        }

                                        // Getting the weight sum of exports
                                        DataQueryBuilder exportWeightBuilder = DataQueryBuilder.create();
                                        exportWeightBuilder.setWhereClause(EMAIL_CLAUSE);
                                        exportWeightBuilder.setGroupBy("kind");
                                        exportWeightBuilder.setProperties("Sum(weight)");

                                        Backendless.Data.of("Sale").find(exportWeightBuilder, new AsyncCallback<List<Map>>() {
                                            @Override
                                            public void handleResponse(List<Map> response) {
                                                if (response.size() >= 1) {
                                                    if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                                        allPolishExportWeight += (int) response.get(0).get("sum");
                                                    } else {
                                                        allPolishExportWeight += (double) response.get(0).get("sum");
                                                    }
                                                }

                                                // Getting the sale sum of sales
                                                DataQueryBuilder saleSumBuilder = DataQueryBuilder.create();
                                                saleSumBuilder.setWhereClause(EMAIL_CLAUSE);
                                                saleSumBuilder.setHavingClause(SALE_CLAUSE);
                                                saleSumBuilder.setGroupBy("polish");
                                                saleSumBuilder.setProperties("Sum(saleSum)");

                                                Backendless.Data.of("Sale").find(saleSumBuilder, new AsyncCallback<List<Map>>() {
                                                    @Override
                                                    public void handleResponse(List<Map> response) {
                                                        if (response.size() >= 2) {
                                                            if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                allPolishSaleSum += (int) response.get(1).get("sum");
                                                            } else {
                                                                allPolishSaleSum += (double) response.get(1).get("sum");
                                                            }
                                                            if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                                                allRoughSaleSum += (int) response.get(0).get("sum");
                                                            } else {
                                                                allRoughSaleSum += (double) response.get(0).get("sum");
                                                            }
                                                        }

                                                        // Getting the weight sum of sales
                                                        DataQueryBuilder saleWeightBuilder = DataQueryBuilder.create();
                                                        saleWeightBuilder.setWhereClause(EMAIL_CLAUSE);
                                                        saleWeightBuilder.setHavingClause(SALE_CLAUSE);
                                                        saleWeightBuilder.setProperties("Sum(weight)");
                                                        saleWeightBuilder.setGroupBy("polish");

                                                        Backendless.Data.of("Sale").find(saleWeightBuilder, new AsyncCallback<List<Map>>() {
                                                            @Override
                                                            public void handleResponse(List<Map> response) {
                                                                if (response.size() >= 2) {
                                                                    if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                        allPolishSaleWeight += (int) response.get(1).get("sum");
                                                                    } else {
                                                                        allPolishSaleWeight += (double) response.get(1).get("sum");
                                                                    }

                                                                    if (Objects.requireNonNull(response.get(0).get("sum")).getClass().equals(Integer.class)) {
                                                                        allRoughSaleWeight += (int) response.get(0).get("sum");
                                                                    } else {
                                                                        allRoughSaleWeight += (double) response.get(0).get("sum");
                                                                    }
                                                                }

                                                                // Getting all the sorts info
                                                                DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                                                                sortBuilder.setWhereClause(EMAIL_CLAUSE);
                                                                sortBuilder.setGroupBy("sale");
                                                                sortBuilder.setProperties("Sum(saleSum), sale");

                                                                Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                                                    @Override
                                                                    public void handleResponse(List<Map> response) {
                                                                        if (response.size() >= 2) {
                                                                            if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                                sortSaleSum += (int) response.get(1).get("sum");
                                                                            } else {
                                                                                sortSaleSum += (double) response.get(1).get("sum");
                                                                            }
                                                                        } else {
                                                                            sortSaleSum = 0;
                                                                        }

                                                                        DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                                                                        sortBuilder.setWhereClause(EMAIL_CLAUSE);
                                                                        sortBuilder.setGroupBy("sale");
                                                                        sortBuilder.setProperties("Sum(sum), sale");

                                                                        Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                                                            @Override
                                                                            public void handleResponse(List<Map> response) {
                                                                                if (response.size() >= 2) {
                                                                                    if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                                        sortSum += (int) response.get(1).get("sum");
                                                                                    } else {
                                                                                        sortSum += (double) response.get(1).get("sum");
                                                                                    }
                                                                                } else {
                                                                                    sortSum = 0;
                                                                                }


                                                                                DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                                                                                sortBuilder.setWhereClause(EMAIL_CLAUSE);
                                                                                sortBuilder.setProperties("Count(objectId)");

                                                                                Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                                                                                    @Override
                                                                                    public void handleResponse(List<Map> response) {
                                                                                        if (response.size() >= 1) {
                                                                                            sortNum += (int) response.get(0).get("count");
                                                                                        } else {
                                                                                            sortNum = 0;
                                                                                        }

                                                                                        DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                                                                                        sortInfoBuilder.setWhereClause(EMAIL_CLAUSE);
                                                                                        sortInfoBuilder.setGroupBy("open");
                                                                                        sortInfoBuilder.setProperties("Sum(sum), open");

                                                                                        Backendless.Data.of("sortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                                                                                            @Override
                                                                                            public void handleResponse(List<Map> response) {
                                                                                                if (response.size() >= 2) {
                                                                                                    if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                                                        openStockPolishSum += (int) response.get(1).get("sum");
                                                                                                    } else {
                                                                                                        openStockPolishSum += (double) response.get(1).get("sum");
                                                                                                    }
                                                                                                } else {
                                                                                                    openStockPolishSum = 0;
                                                                                                }

                                                                                                DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                                                                                                sortInfoBuilder.setWhereClause(EMAIL_CLAUSE);
                                                                                                sortInfoBuilder.setGroupBy("open");
                                                                                                sortInfoBuilder.setProperties("Sum(weight), open");

                                                                                                Backendless.Data.of("sortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                                                                                                    @Override
                                                                                                    public void handleResponse(List<Map> response) {
                                                                                                        if (response.size() >= 2) {
                                                                                                            if (Objects.requireNonNull(response.get(1).get("sum")).getClass().equals(Integer.class)) {
                                                                                                                openStockPolishWeight += (int) response.get(1).get("sum");
                                                                                                            } else {
                                                                                                                openStockPolishWeight += (double) response.get(1).get("sum");
                                                                                                            }
                                                                                                        } else {
                                                                                                            openStockPolishWeight = 0;
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

                                                                                                        RoughWeight = openStockRoughWeight + allNotDoneRoughBuyWeight - saleRoughWeight;
                                                                                                        PolishWeight = openStockPolishWeight + allRoughBuyDoneWeight + buyPolishWeight - salePolishWeight - exportWeight;
                                                                                                        weight = RoughWeight + PolishWeight;

                                                                                                        roughCostPrice = (openStockRoughWeight + allNotDoneRoughBuyWeight > 0) ? ((openStockRoughSum + allNotDoneRoughBuySum) / (openStockRoughWeight + allNotDoneRoughBuyWeight)) : 0;
                                                                                                        roughSum = roughCostPrice * RoughWeight;

                                                                                                        polishCostPrice = ((openStockPolishWeight + allRoughBuyDoneWeight + buyPolishWeight) > 0) ? (openStockPolishSum + allRoughBuyDoneSum + buyPolishSum) / (openStockPolishWeight + allRoughBuyDoneWeight + buyPolishWeight) : 0;
                                                                                                        polishSum = PolishWeight * polishCostPrice;

                                                                                                        costSum = roughSum + polishSum;
                                                                                                        costPrice = (weight > 0) ? (costSum / weight) : 0;

                                                                                                        openStockRoughPrice = openStockRoughWeight == 0 ? 0 : openStockRoughSum / openStockRoughWeight;
                                                                                                        openStockPolishPrice = openStockPolishWeight == 0 ? 0 : openStockPolishSum / openStockPolishWeight;

                                                                                                        openStockSum = openStockPolishSum + openStockRoughSum;
                                                                                                        openStockWeight = openStockPolishWeight + openStockRoughWeight;
                                                                                                        openStockPrice = openStockWeight == 0 ? 0 : (openStockSum / openStockWeight);

                                                                                                        sortProfit = sortSaleSum - sortSum;
                                                                                                        sortProfitPre = saleWeight != 0 ? (sortProfit / saleSum) : 0;
                                                                                                        sortProfitPrice = saleWeight != 0 ? (sortProfit / saleWeight) : 0;

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
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(1);
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(true);
            }
        });

        btnBalanceSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(2);
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(true);
                btnBalanceBuy.setSelected(false);
            }
        });

        btnBalanceOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText(3);
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(true);
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
                tvSortHeadline.setText("מיונים");

                tvBalanceSum.setText("סכום עלות שנותר:  " + numberFormat.format(costSum) + "$");
                tvBalanceWeight.setText("משקל שנותר:  " + numberFormat.format(weight) + " קראט ");
                tvBalancePrice.setText("מחיר עלות ממוצע:  " + numberFormat.format(costPrice) + "$");
                tvBalanceRoughSum.setText("סכום עלות שנותר:  " + numberFormat.format(roughSum) + "$");
                tvBalanceRoughWeight.setText("משקל שנותר:  " + numberFormat.format(RoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר עלות ממוצע:  " + numberFormat.format(roughCostPrice) + "$");
                tvBalancePolishSum.setText("סכום עלות שנותר:  " + numberFormat.format(polishSum) + "$");
                tvBalancePolishWeight.setText("משקל שנותר:  " + numberFormat.format(PolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר עלות ממוצע:  " + numberFormat.format(polishCostPrice) + "$");

                llBalanceSort.setVisibility(View.VISIBLE);
                tvBalanceSortNum.setText("מספר מיונים שנוצרו:  " + sortNum);
                tvBalanceSortProfit.setText("רווח:  " + numberFormat.format(sortProfit) + "$");
                tvBalanceSortProfitPre.setText("אחוז רווח מהמחזור:  " + numberFormat.format(sortProfitPre*100) + "%");
                tvBalanceSortProfitPrice.setText("רווח ממוצע לקראט:  " + numberFormat.format(sortProfitPrice) + "$");
                break;

            case 1:
                tvBalanceHeadline.setText("סחורות שניקנו");
                tvPolishHeadline.setText("קניית מלוטש");
                tvRoughHeadline.setText("קניית גלם");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(buySum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(buyWeight) + " קראט ");
                tvBalancePrice.setText("מחיר קניה ממוצע:  " + numberFormat.format(buyPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(buyRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(buyRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר קניה ממוצע:  " + numberFormat.format(buyRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(buyPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(buyPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר קניה ממוצע:  " + numberFormat.format(buyPolishPrice) + "$");

                llBalanceSort.setVisibility(View.GONE);
                break;

            case 2:
                tvBalanceHeadline.setText("סחורות שנמכרו");
                tvPolishHeadline.setText("מכירת מלוטש בארץ");
                tvRoughHeadline.setText("מכירת מלוטש ביצוא");
                tvSortHeadline.setText("מכירת גלם");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(saleSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(saleWeight) + " קראט ");
                tvBalancePrice.setText("מחיר מכירה ממוצע:  " + numberFormat.format(salePrice) + "$");

                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(salePolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(salePolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר מכירה ממוצע:  " + numberFormat.format(salePolishPrice) + "$");

                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(exportSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(exportWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר מכירה ממוצע:  " + numberFormat.format(exportPrice) + "$");

                llBalanceSort.setVisibility(View.GONE);
                break;

            case 3:
                tvBalanceHeadline.setText("פתיחת מלוטש וגלם");
                tvPolishHeadline.setText("פתיחת מלוטש");
                tvRoughHeadline.setText("פתיחת גלם");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(openStockSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(openStockWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(openStockPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(openStockRoughSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(openStockRoughWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(openStockRoughPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(openStockPolishSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(openStockPolishWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(openStockPolishPrice) + "$");

                llBalanceSort.setVisibility(View.GONE);
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
