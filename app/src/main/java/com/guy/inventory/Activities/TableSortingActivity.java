package com.guy.inventory.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Adapters.SortAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableSortingActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ListView lvSortList;
    private SortAdapter sortAdapter;

    private int selectedItem = -1;

    final int PAGE_SIZE = 100;

    private final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    private final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_sorting);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvSortList = findViewById(R.id.lvSortList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מיונים");
        actionBar.setDisplayHomeAsUpEnabled(true);
        sortBuilder.setWhereClause(EMAIL_CLAUSE);
        sortBuilder.setPageSize(PAGE_SIZE);

        showProgress(true);
        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                InventoryApp.sorts = response;

                DataQueryBuilder sortInfoBuyBuilder = DataQueryBuilder.create();
                sortInfoBuyBuilder.setWhereClause(EMAIL_CLAUSE);
                sortInfoBuyBuilder.setWhereClause("fromBuy = true");
                sortInfoBuyBuilder.setPageSize(100);
                sortInfoBuyBuilder.setGroupBy("toId");
                sortInfoBuyBuilder.setProperties("Sum(sum) as sum");
                sortInfoBuyBuilder.addProperty("Sum(weight) as weight");
                sortInfoBuyBuilder.addProperty("toId");

                Backendless.Data.of("SortInfo").find(sortInfoBuyBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {
                            String toId = (String) response.get(i).get("toId");
                            for (int y = 0; y < InventoryApp.sorts.size(); y++) {
                                assert toId != null;
                                if (toId.equals(InventoryApp.sorts.get(y).getObjectId())) {
                                    double buySum;
                                    double buyWeight;
                                    if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                        buySum = (int) response.get(i).get("sum");
                                        buyWeight = (int) response.get(i).get("weight");
                                    } else {
                                        buySum = (double) response.get(i).get("sum");
                                        buyWeight = (double) response.get(i).get("weight");
                                    }

                                    InventoryApp.sorts.get(y).setSum(buySum);
                                    InventoryApp.sorts.get(y).setWeight(buyWeight);
                                    InventoryApp.sorts.get(y).setPrice(buySum / buyWeight);
                                }
                            }
                        }

                        DataQueryBuilder sortInfoSaleBuilder = DataQueryBuilder.create();
                        sortInfoSaleBuilder.setWhereClause(EMAIL_CLAUSE);
                        sortInfoSaleBuilder.setPageSize(100);
                        sortInfoSaleBuilder.setGroupBy("toId");
                        sortInfoSaleBuilder.setProperties("Sum(sum) as sum");
                        sortInfoSaleBuilder.addProperty("Sum(weight) as weight");
                        sortInfoSaleBuilder.addProperty("toId");
                        sortInfoSaleBuilder.setWhereClause("fromSale = true");

                        Backendless.Data.of("SortInfo").find(sortInfoSaleBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                for (int i = 0; i < response.size(); i++) {
                                    String toId = (String) response.get(i).get("toId");
                                    for (int y = 0; y < InventoryApp.sorts.size(); y++) {
                                        assert toId != null;
                                        if (toId.equals(InventoryApp.sorts.get(y).getObjectId())) {
                                            double saleSum;
                                            double saleWeight;
                                            if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                                saleSum = (int) response.get(i).get("sum");
                                                saleWeight = (int) response.get(i).get("weight");
                                            } else {
                                                saleSum = (double) response.get(i).get("sum");
                                                saleWeight = (double) response.get(i).get("weight");
                                            }
                                            InventoryApp.sorts.get(y).setSum(InventoryApp.sorts.get(y).getSum()-saleSum);
                                            InventoryApp.sorts.get(y).setWeight(InventoryApp.sorts.get(y).getWeight()-saleWeight);
                                            InventoryApp.sorts.get(y).setPrice(InventoryApp.sorts.get(y).getSum() / InventoryApp.sorts.get(y).getWeight());
                                        }
                                    }
                                }
                                sortAdapter = new SortAdapter(TableSortingActivity.this, InventoryApp.sorts);
                                lvSortList.setAdapter(sortAdapter);
                                showProgress(false);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(TableSortingActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSortingActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSortingActivity.this, "טרם נשרמו מיונים", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSortingActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvSortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;
                sortAdapter.setSelectedPosition(position);
                sortAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_sort_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                Intent intent = new Intent(TableSortingActivity.this, NewSortActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editSort = new Intent(TableSortingActivity.this, EditSortActivity.class);
                    editSort.putExtra("index", selectedItem);
                    startActivityForResult(editSort, 1);
                }
                break;

            case R.id.detailsIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sortHistory = new Intent(TableSortingActivity.this, TableSortHistoryActivity.class);
                    sortHistory.putExtra("index", selectedItem);
                    startActivityForResult(sortHistory, 1);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            sortAdapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}