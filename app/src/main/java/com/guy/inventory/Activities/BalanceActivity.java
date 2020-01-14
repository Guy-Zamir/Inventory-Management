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
import com.guy.inventory.Tables.BrokerSort;
import com.guy.inventory.Tables.Memo;
import com.guy.inventory.Tables.Sort;

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

    Button btnBalanceBuy, btnBalanceSale, btnBalanceGoods, btnBalanceOpen, btnBalanceSorts, btnBalanceMemo;

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

    private double brokerMemoPrice;
    private double memoPrice;

    private double allMemoSum, allMemoWeight, allMemoPrice;

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
    private double allSortSum = 0;
    private double allSortWeight = 0;
    private double allSortPrice = 0;

    private double openStockPolishSum = 0;
    private double openStockPolishWeight = 0;
    private double openStockRoughSum = 0;
    private double openStockRoughWeight = 0;

    private double brokerSum = 0;
    private double brokerWeightSum = 0;
    private double brokerPrice = 0;
    private double brokerMemoSum = 0;
    private double brokerMemoWeight = 0;

    private double memoSum = 0;
    private double memoWeight = 0;

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
        btnBalanceSorts = findViewById(R.id.btnBalanceSorts);
        btnBalanceMemo = findViewById(R.id.btnBalanceMemo);

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
        buyBuilder.setProperties("Sum(sum) as sum", "Sum(weight) as weight", "Sum(doneWeight) as doneWeight", "done", "polish");
        buyBuilder.setGroupBy("done", "polish");

        showProgress(true);
        Backendless.Data.of("Buy").find(buyBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                for (int i = 0; i < response.size(); i++) {
                    // polish = false / done = false
                    if ((!(boolean) response.get(i).get("polish")) && (!(boolean) response.get(i).get("done"))) {

                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                            allNotDoneRoughBuySum += (int) response.get(i).get("sum");
                        } else {
                            allNotDoneRoughBuySum += (double) response.get(i).get("sum");
                        }

                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                            allNotDoneRoughBuyWeight += (int) response.get(i).get("weight");
                        } else {
                            allNotDoneRoughBuyWeight += (double) response.get(i).get("weight");
                        }
                    }

                    // Not sort buy polish
                    // polish = true / done = false
                    else if (((boolean) response.get(i).get("polish")) && (!(boolean) response.get(i).get("done"))) {

                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                            allPolishBuySum += (int) response.get(i).get("sum");
                        } else {
                            allPolishBuySum += (double) response.get(i).get("sum");
                        }

                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                            allPolishBuyWeight += (int) response.get(i).get("weight");
                        } else {
                            allPolishBuyWeight += (double) response.get(i).get("weight");
                        }
                    }

                    // polish = false / done = true
                    else if ((!(boolean) response.get(i).get("polish")) && ((boolean) response.get(i).get("done"))) {

                        if (Objects.requireNonNull(response.get(i).get("doneWeight")).getClass().equals(Integer.class)) {
                            allRoughBuyDoneWeight += (int) response.get(i).get("doneWeight");
                        } else {
                            allRoughBuyDoneWeight += (double) response.get(i).get("doneWeight");
                        }
                    }

                    // polish = true / done = true
                    else if (((boolean) response.get(i).get("polish")) && ((boolean) response.get(i).get("done"))) {

                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                            allPolishBuySum += (int) response.get(i).get("sum");
                        } else {
                            allPolishBuySum += (double) response.get(i).get("sum");
                        }

                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                            allPolishBuyWeight += (int) response.get(i).get("weight");
                        } else {
                            allPolishBuyWeight += (double) response.get(i).get("weight");
                        }
                    }
                }

                // Getting the sale sum of exports
                DataQueryBuilder saleBuilder = DataQueryBuilder.create();
                saleBuilder.setWhereClause(EMAIL_CLAUSE);
                saleBuilder.setProperties("Sum(saleSum) as sum", "Sum(weight) as weight", "kind", "polish");
                saleBuilder.setGroupBy("kind", "polish");

                Backendless.Data.of("Sale").find(saleBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {

                            // export  - polish
                            if (Objects.equals(response.get(i).get("kind"), "export") && (boolean) response.get(i).get("polish")) {

                                if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                    allPolishExportSum += (int) response.get(i).get("sum");
                                } else {
                                    allPolishExportSum += (double) response.get(i).get("sum");
                                }

                                if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                    allPolishExportWeight += (int) response.get(i).get("weight");
                                } else {
                                    allPolishExportWeight += (double) response.get(i).get("weight");
                                }

                                // sale  - polish
                            } else if (Objects.equals(response.get(i).get("kind"), "sale") && (boolean) response.get(i).get("polish")) {

                                if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                    allPolishSaleSum += (int) response.get(i).get("sum");
                                } else {
                                    allPolishSaleSum += (double) response.get(i).get("sum");
                                }

                                if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                    allPolishSaleWeight += (int) response.get(i).get("weight");
                                } else {
                                    allPolishSaleWeight += (double) response.get(i).get("weight");
                                }

                                // sale  - rough
                            } else if (Objects.equals(response.get(i).get("kind"), "sale") && (!(boolean) response.get(i).get("polish"))) {

                                if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                    allRoughSaleSum += (int) response.get(i).get("sum");
                                } else {
                                    allRoughSaleSum += (double) response.get(i).get("sum");
                                }

                                if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                    allRoughSaleWeight += (int) response.get(i).get("weight");
                                } else {
                                    allRoughSaleWeight += (double) response.get(i).get("weight");
                                }
                            }
                        }

                        // Getting all the sorts info
                        DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                        sortBuilder.setWhereClause(EMAIL_CLAUSE);
                        sortBuilder.setGroupBy("sale", "last");
                        sortBuilder.setProperties("Sum(saleSum) as saleSum", "Sum(sum) as sum", "Sum(weight) as weight", "Count(objectId) as count", "sale", "last");

                        Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {

                                for (int i = 0; i < response.size(); i++) {

                                    if ((boolean) response.get(i).get("sale")) {
                                        if (Objects.requireNonNull(response.get(i).get("saleSum")).getClass().equals(Integer.class)) {
                                            sortSaleSum += (int) response.get(i).get("saleSum");
                                        } else {
                                            sortSaleSum += (double) response.get(i).get("saleSum");
                                        }

                                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                            sortSum += (int) response.get(i).get("sum");
                                        } else {
                                            sortSum += (double) response.get(i).get("sum");
                                        }
                                    }

                                    if ((boolean) response.get(i).get("last")) {
                                        if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                            allSortSum += (int) response.get(i).get("sum");
                                        } else {
                                            allSortSum += (double) response.get(i).get("sum");
                                        }

                                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                            allSortWeight += (int) response.get(i).get("weight");
                                        } else {
                                            allSortWeight += (double) response.get(i).get("weight");
                                        }
                                    }
                                    sortNum += (int) response.get(i).get("count");
                                }

                                DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                                sortInfoBuilder.setWhereClause(EMAIL_CLAUSE);
                                sortInfoBuilder.setGroupBy("kind");
                                sortInfoBuilder.setProperties("Sum(sum) as sum", "Sum(weight) as weight", "kind");

                                Backendless.Data.of("sortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                                    @Override
                                    public void handleResponse(List<Map> response) {
                                        for (int i = 0; i < response.size(); i++) {

                                            if (Objects.equals(response.get(i).get("kind"), "open")) {
                                                if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                                    openStockPolishSum += (int) response.get(i).get("sum");
                                                } else {
                                                    openStockPolishSum += (double) response.get(i).get("sum");
                                                }

                                                if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                                    openStockPolishWeight += (int) response.get(i).get("weight");
                                                } else {
                                                    openStockPolishWeight += (double) response.get(i).get("weight");
                                                }
                                            }
                                        }

                                        DataQueryBuilder brokerSortBuilder = DataQueryBuilder.create();
                                        brokerSortBuilder.setWhereClause(EMAIL_CLAUSE);
                                        brokerSortBuilder.setGroupBy("memo");
                                        brokerSortBuilder.setProperties("Sum(sumINV) as sumINV", "Sum(weight) as weight", "memo");
                                        Backendless.Data.of("BrokerSort").find(brokerSortBuilder, new AsyncCallback<List<Map>>() {
                                            @Override
                                            public void handleResponse(List<Map> response) {
                                                for (int i = 0; i < response.size(); i++) {

                                                    if (!(boolean) response.get(i).get("memo")) {

                                                        if (Objects.requireNonNull(response.get(i).get("sumINV")).getClass().equals(Integer.class)) {
                                                            brokerSum += (int) response.get(i).get("sumINV");
                                                        } else {
                                                            brokerSum += (double) response.get(i).get("sumINV");
                                                        }

                                                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                                            brokerWeightSum += (int) response.get(i).get("weight");
                                                        } else {
                                                            brokerWeightSum += (double) response.get(i).get("weight");
                                                        }

                                                    } else if ((boolean) response.get(i).get("memo")) {

                                                        if (Objects.requireNonNull(response.get(i).get("sumINV")).getClass().equals(Integer.class)) {
                                                            brokerMemoSum += (int) response.get(i).get("sumINV");
                                                        } else {
                                                            brokerMemoSum += (double) response.get(i).get("sumINV");
                                                        }

                                                        if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                                            brokerMemoWeight += (int) response.get(i).get("weight");
                                                        } else {
                                                            brokerMemoWeight += (double) response.get(i).get("weight");
                                                        }
                                                    }
                                                }

                                                DataQueryBuilder memoBuilder = DataQueryBuilder.create();
                                                memoBuilder.setWhereClause(EMAIL_CLAUSE);
                                                memoBuilder.setGroupBy("kind");
                                                memoBuilder.setProperties("Sum(sumINV) as sumINV", "Sum(weight) as weight", "kind");
                                                Backendless.Data.of("Memo").find(memoBuilder, new AsyncCallback<List<Map>>() {
                                                    @Override
                                                    public void handleResponse(List<Map> response) {
                                                        for (int i = 0; i < response.size(); i++) {

                                                            if ((Objects.requireNonNull(response.get(i).get("sumINV"))).getClass().equals(Integer.class)) {
                                                                memoSum += (int) response.get(i).get("sumINV");
                                                            } else {
                                                                memoSum += (double) response.get(i).get("sumINV");
                                                            }

                                                            if (Objects.requireNonNull(response.get(i).get("weight")).getClass().equals(Integer.class)) {
                                                                memoWeight += (int) response.get(i).get("weight");
                                                            } else {
                                                                memoWeight += (double) response.get(i).get("weight");
                                                            }
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

                                                        brokerPrice = brokerWeightSum == 0 ? 0 : brokerSum / brokerWeightSum;
                                                        brokerMemoPrice = brokerMemoWeight == 0 ? 0 : brokerMemoSum / brokerMemoWeight;

                                                        memoPrice = memoWeight == 0 ? 0 : memoSum / memoWeight;

                                                        allMemoSum = brokerMemoSum + memoSum;
                                                        allMemoWeight = brokerMemoWeight + memoWeight;
                                                        allMemoPrice = allMemoWeight == 0 ? 0 : allMemoSum / allMemoWeight;

                                                        allSortPrice = allSortWeight == 0 ? 0 : allSortSum / allSortWeight;

                                                        setTheText("Goods");
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

        btnBalanceGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Goods");
                btnBalanceGoods.setSelected(true);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
                btnBalanceSorts.setSelected(false);
                btnBalanceMemo.setSelected(false);
            }
        });

        btnBalanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Buys");
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(true);
                btnBalanceSorts.setSelected(false);
                btnBalanceMemo.setSelected(false);
            }
        });

        btnBalanceSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Sales");
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(true);
                btnBalanceBuy.setSelected(false);
                btnBalanceSorts.setSelected(false);
                btnBalanceMemo.setSelected(false);
            }
        });

        btnBalanceOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Open");
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(true);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
                btnBalanceSorts.setSelected(false);
                btnBalanceMemo.setSelected(false);
            }
        });

        btnBalanceSorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Sorts");
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
                btnBalanceSorts.setSelected(true);
                btnBalanceMemo.setSelected(false);
            }
        });

        btnBalanceMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheText("Memo");
                btnBalanceGoods.setSelected(false);
                btnBalanceOpen.setSelected(false);
                btnBalanceSale.setSelected(false);
                btnBalanceBuy.setSelected(false);
                btnBalanceSorts.setSelected(false);
                btnBalanceMemo.setSelected(true);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setTheText(String head) {

        switch (head) {
            case "Goods":
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

            case "Buys":
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

            case "Sales":
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

            case "Open":
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

            case "Sorts":
                tvBalanceHeadline.setText("מיונים אצל ניב");
                tvPolishHeadline.setText("מיונים במשרד");
                tvRoughHeadline.setText("מיונים בממו");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(brokerSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(brokerWeightSum) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(brokerPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(allSortSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(allSortWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(allSortPrice) + "$");
                tvBalanceRoughSum.setText("סכום:  " + numberFormat.format(allMemoSum) + "$");
                tvBalanceRoughWeight.setText("משקל:  " + numberFormat.format(allMemoWeight) + " קראט ");
                tvBalanceRoughPrice.setText("מחיר ממוצע:  " + numberFormat.format(allMemoPrice) + "$");

                llBalanceSort.setVisibility(View.GONE);
                break;

            case "Memo":
                tvBalanceHeadline.setText("ממו אצל ניב");
                tvPolishHeadline.setText("ממו מהמשרד");
                tvRoughHeadline.setText("");

                tvBalanceSum.setText("סכום:  " + numberFormat.format(brokerMemoSum) + "$");
                tvBalanceWeight.setText("משקל:  " + numberFormat.format(brokerMemoWeight) + " קראט ");
                tvBalancePrice.setText("מחיר ממוצע:  " + numberFormat.format(brokerMemoPrice) + "$");
                tvBalancePolishSum.setText("סכום:  " + numberFormat.format(memoSum) + "$");
                tvBalancePolishWeight.setText("משקל:  " + numberFormat.format(memoWeight) + " קראט ");
                tvBalancePolishPrice.setText("מחיר ממוצע:  " + numberFormat.format(memoPrice) + "$");
                tvBalanceRoughSum.setText(" ");
                tvBalanceRoughWeight.setText(" ");
                tvBalanceRoughPrice.setText(" ");

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
