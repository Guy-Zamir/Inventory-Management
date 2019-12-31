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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Adapters.SortHistoryAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.SortInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSortHistoryActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ListView lvSortHistoryList;
    private SortHistoryAdapter sortHistoryAdapter;
    public List<SortInfo> filterSorts = new ArrayList<>();

    private int index;
    final int PAGE_SIZE = 100;
    private int selectedItem = -1;

    final DataQueryBuilder sortHistoryBuilder = DataQueryBuilder.create();
    final DataQueryBuilder sortSalesBuilder = DataQueryBuilder.create();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_sort_history);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvSortHistoryList = findViewById(R.id.lvSortHistoryList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        index = getIntent().getIntExtra("index", 0);
        boolean sales = getIntent().getBooleanExtra("sales", false);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        sortHistoryBuilder.setWhereClause("objectId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
        sortHistoryBuilder.setSortBy("created DESC");
        sortHistoryBuilder.addRelated("Sorts");

        sortSalesBuilder.setWhereClause("sale = true");
        sortSalesBuilder.setPageSize(PAGE_SIZE);
        sortSalesBuilder.setSortBy("created DESC");

        showProgress(true);
        if (sales) {
            // Getting all the sales in the sorts as SORTS (sale = true)
            Backendless.Data.of("Sort").find(sortSalesBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {
                    if (response.size() > 0) {
                        for (int i = 0; i < response.size(); i++) {
                            Map sort = response.get(i);
                            SortInfo sortInfo = new SortInfo();
                            sortInfo.setSale((boolean) sort.get("sale"));
                            sortInfo.setBuy(false);
                            sortInfo.setSplit(false);
                            sortInfo.setSortCount((Integer) sort.get("sortCount"));
                            sortInfo.setFromName(sort.get("name") + " - " + sort.get("sortCount"));
                            sortInfo.setCreated((Date) sort.get("created"));
                            sortInfo.setToName((String) sort.get("saleName"));
                            sortInfo.setSoldPrice(sort.get("soldPrice").getClass().equals(Integer.class) ? (int) sort.get("soldPrice") : (double) sort.get("soldPrice"));
                            sortInfo.setObjectId((String) sort.get("objectId"));
                            sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                            sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                            sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                            filterSorts.add(sortInfo);
                        }
                    }
                    sortHistoryAdapter = new SortHistoryAdapter(TableSortHistoryActivity.this, filterSorts);
                    lvSortHistoryList.setAdapter(sortHistoryAdapter);
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        // Getting only 1 sort from the selected sort object id and displaying all the sorts that related to it (NO sortInfo!)
        } else {
            Backendless.Data.of("Sort").find(sortHistoryBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {
                    Object[] sorts;
                    if (response.size() > 0) {
                        sorts = (Object[]) response.get(0).get("Sorts");
                        for (Object object : sorts) {
                            HashMap sort = (HashMap) object;
                            SortInfo sortInfo = new SortInfo();
                            sortInfo.setSale((boolean) sort.get("sale"));
                            sortInfo.setBuy(false);
                            sortInfo.setSplit(false);
                            sortInfo.setSortCount((Integer) sort.get("sortCount"));
                            sortInfo.setFromName(sort.get("name") + " - " + sort.get("sortCount"));
                            sortInfo.setCreated((Date) sort.get("created"));
                            sortInfo.setToName((String) sort.get("saleName"));
                            sortInfo.setSoldPrice(sort.get("soldPrice").getClass().equals(Integer.class) ? (int) sort.get("soldPrice") : (double) sort.get("soldPrice"));
                            sortInfo.setObjectId((String) sort.get("objectId"));
                            sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                            sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                            sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                            filterSorts.add(sortInfo);
                        }
                    }

                    // Getting all the sortInfo that toId = "selected sort objectId" (IN the Sort)
                    // Buys and Splits
                    DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                    sortInfoBuilder.setWhereClause("toId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
                    sortInfoBuilder.setPageSize(PAGE_SIZE);
                    Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                        @Override
                        public void handleResponse(List<Map> response) {
                            if (response.size() > 0) {
                                for (int i = 0; i < response.size(); i++) {
                                    HashMap sort = (HashMap) response.get(i);
                                    SortInfo sortInfo = new SortInfo();
                                    sortInfo.setSale((boolean) sort.get("sale"));
                                    sortInfo.setBuy((boolean) sort.get("buy"));
                                    sortInfo.setSplit((boolean) sort.get("split"));
                                    sortInfo.setOpen((boolean) sort.get("open"));
                                    sortInfo.setSortCount((int) sort.get("sortCount"));

                                    if (sortInfo.isBuy() || sortInfo.isOpen()) {
                                        sortInfo.setFromName((String) sort.get("fromName"));
                                    } else {
                                        sortInfo.setFromName(sort.get("fromName") + " - " + sort.get("sortCount"));
                                    }

                                    sortInfo.setToName((String) sort.get("toName"));
                                    sortInfo.setObjectId((String) sort.get("fromId"));
                                    sortInfo.setCreated((Date) sort.get("created"));
                                    sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                    sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                    sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));

                                    // Setting all to false to show the IN value
                                    sortInfo.setSplit(false);
                                    filterSorts.add(sortInfo);
                                }
                            }

                            // Getting all the sortInfo that fromId = "selected sort objectId" (OUT in sort)
                            // Only splits for now!
                            DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                            sortInfoBuilder.setWhereClause("fromId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
                            sortInfoBuilder.setHavingClause("split = true");
                            sortInfoBuilder.setPageSize(PAGE_SIZE);
                            Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                                @Override
                                public void handleResponse(List<Map> response) {
                                    if (response.size() > 0) {
                                        for (int i = 0; i < response.size(); i++) {
                                            HashMap sort = (HashMap) response.get(i);
                                            SortInfo sortInfo = new SortInfo();
                                            sortInfo.setSale(false);
                                            sortInfo.setBuy(false);
                                            sortInfo.setSplit(true);
                                            sortInfo.setSortCount((int) sort.get("sortCount"));
                                            sortInfo.setFromName(sort.get("toName") + " - " + sort.get("sortCount"));
                                            sortInfo.setToName((String) sort.get("toName"));
                                            sortInfo.setObjectId((String) sort.get("toId"));
                                            sortInfo.setCreated((Date) sort.get("created"));
                                            sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                            sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                            sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                                            filterSorts.add(sortInfo);
                                        }
                                    }

                                    sortHistoryAdapter = new SortHistoryAdapter(TableSortHistoryActivity.this, filterSorts);
                                    lvSortHistoryList.setAdapter(sortHistoryAdapter);
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            lvSortHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    sortHistoryAdapter.setSelectedPosition(position);
                    sortHistoryAdapter.notifyDataSetChanged();
                    selectedItem = position;
                }
            });
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_sort_history_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.detailsIcon) {
            if (selectedItem == -1) {
                Toast.makeText(this, "יש לבחור פריט", Toast.LENGTH_SHORT).show();

            } else if (filterSorts.get(selectedItem).isBuy() || filterSorts.get(selectedItem).isSale() || filterSorts.get(selectedItem).isOpen()) {
                Toast.makeText(this, "אין עוד פירוט", Toast.LENGTH_SHORT).show();

            } else {
                ActionBar actionBar = getSupportActionBar();
                assert actionBar != null;
                actionBar.setTitle("תנועות:  " + filterSorts.get(selectedItem).getFromName());
                actionBar.setDisplayHomeAsUpEnabled(true);

                final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                sortBuilder.setPageSize(PAGE_SIZE);

                sortBuilder.setWhereClause("objectId = '" + filterSorts.get(selectedItem).getObjectId() + "'");
                sortBuilder.addRelated("Sorts");

                final int offset = filterSorts.size();

                showProgress(true);

                // Getting only 1 sort from the selected sort object id and displaying all the sorts that related to it (NO sortInfo!)
                Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        Object[] sorts;
                        if (response.size() > 0) {
                            sorts = (Object[]) response.get(0).get("Sorts");
                            for (Object object : sorts) {
                                HashMap sort = (HashMap) object;
                                SortInfo sortInfo = new SortInfo();
                                sortInfo.setSale((boolean) sort.get("sale"));
                                sortInfo.setBuy(false);
                                sortInfo.setSplit(false);
                                sortInfo.setSortCount((Integer) sort.get("sortCount"));
                                sortInfo.setFromName(sort.get("name") + " - " + sort.get("sortCount"));
                                sortInfo.setCreated((Date) sort.get("created"));
                                sortInfo.setToName((String) sort.get("saleName"));
                                sortInfo.setSoldPrice(sort.get("soldPrice").getClass().equals(Integer.class) ? (int) sort.get("soldPrice") : (double) sort.get("soldPrice"));
                                sortInfo.setObjectId((String) sort.get("objectId"));
                                sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                                filterSorts.add(sortInfo);
                            }
                        }

                        // Getting all the sortInfo that toId = "selected sort objectId" (IN the Sort)
                        // Buys and Splits
                        final DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                        sortInfoBuilder.setWhereClause("toId = '" + filterSorts.get(selectedItem).getObjectId() + "'");
                        sortInfoBuilder.setPageSize(PAGE_SIZE);
                        Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                if (response.size() > 0) {
                                    for (int i = 0; i < response.size(); i++) {
                                        HashMap sort = (HashMap) response.get(i);
                                        SortInfo sortInfo = new SortInfo();
                                        sortInfo.setSale((boolean) sort.get("sale"));
                                        sortInfo.setBuy((boolean) sort.get("buy"));
                                        sortInfo.setSplit((boolean) sort.get("split"));
                                        sortInfo.setSortCount((int) sort.get("sortCount"));
                                        sortInfo.setFromName(sortInfo.isBuy() ? (String) sort.get("fromName") : sort.get("fromName") + " - " + sort.get("sortCount"));
                                        sortInfo.setToName((String) sort.get("toName"));
                                        sortInfo.setObjectId((String) sort.get("fromId"));
                                        sortInfo.setCreated((Date) sort.get("created"));
                                        sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                        sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                        sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));

                                        // Setting all to false to show the IN value
                                        sortInfo.setSplit(false);
                                        filterSorts.add(sortInfo);
                                    }
                                }

                                // Getting all the sortInfo that fromId = "selected sort objectId" (OUT in sort)
                                // Only splits for now!
                                DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                                sortInfoBuilder.setWhereClause("fromId = '" + filterSorts.get(selectedItem).getObjectId() + "'");
                                sortInfoBuilder.setHavingClause("split = true");
                                sortInfoBuilder.setPageSize(PAGE_SIZE);
                                Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                                    @Override
                                    public void handleResponse(List<Map> response) {
                                        if (response.size() > 0) {
                                            for (int i = 0; i < response.size(); i++) {
                                                HashMap sort = (HashMap) response.get(i);
                                                SortInfo sortInfo = new SortInfo();
                                                sortInfo.setSale(false);
                                                sortInfo.setBuy(false);
                                                sortInfo.setSplit(true);
                                                sortInfo.setSortCount((int) sort.get("sortCount"));
                                                sortInfo.setFromName(sort.get("toName") + " - " + sort.get("sortCount"));
                                                sortInfo.setToName((String) sort.get("toName"));
                                                sortInfo.setObjectId((String) sort.get("toId"));
                                                sortInfo.setCreated((Date) sort.get("created"));
                                                sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                                sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                                sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                                                filterSorts.add(sortInfo);
                                            }
                                        }

                                        filterSorts.removeAll(filterSorts.subList(0, offset));
                                        sortHistoryAdapter.notifyDataSetChanged();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
            sortHistoryAdapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
