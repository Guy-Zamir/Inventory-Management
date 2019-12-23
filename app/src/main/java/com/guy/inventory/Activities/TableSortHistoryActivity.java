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
import com.guy.inventory.Adapters.SortHistoryAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.SortInfo;
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

    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();

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

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("כניסות/יציאות: " + InventoryApp.sorts.get(index).getName() + " - " + InventoryApp.sorts.get(index).getSortCount());
        actionBar.setDisplayHomeAsUpEnabled(true);

        sortBuilder.setPageSize(PAGE_SIZE);
        sortBuilder.setWhereClause("userEmail = '" + InventoryApp.user.getEmail() + "'");
        sortBuilder.setWhereClause("objectId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
        sortBuilder.addRelated("Sorts");

        showProgress(true);
        Backendless.Data.of("Sort").find(sortBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        HashMap[] sorts = (HashMap[]) response.get(0).get("Sorts");
                        for (HashMap sort : sorts) {
                            SortInfo sortInfo = new SortInfo();
                            sortInfo.setFromName(sort.get("name") + " - " + sort.get("sortCount"));
                            sortInfo.setSortCount((Integer) sort.get("sortCount"));
                            sortInfo.setCreated((Date) sort.get("created"));
                            sortInfo.setSale((boolean) sort.get("sale"));
                            sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                            sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                            sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                            filterSorts.add(sortInfo);
                        }

                        DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                        sortInfoBuilder.setWhereClause("userEmail = '" + InventoryApp.user.getEmail() + "'");
                        sortInfoBuilder.setWhereClause("toId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
                        sortInfoBuilder.setPageSize(PAGE_SIZE);
                        Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                for (int i = 0; i < response.size(); i++) {
                                    HashMap sort = (HashMap) response.get(i);
                                    SortInfo sortInfo = new SortInfo();
                                    sortInfo.setFromName((String) sort.get("fromName"));
                                    sortInfo.setSortCount((int) sort.get("sortCount"));
                                    sortInfo.setCreated((Date) sort.get("created"));
                                    sortInfo.setPrice(sort.get("price").getClass().equals(Integer.class) ? (int) sort.get("price") : (double) sort.get("price"));
                                    sortInfo.setWeight(sort.get("weight").getClass().equals(Integer.class) ? (int) sort.get("weight") : (double) sort.get("weight"));
                                    sortInfo.setSum(sort.get("sum").getClass().equals(Integer.class) ? (int) sort.get("sum") : (double) sort.get("sum"));
                                    filterSorts.add(sortInfo);
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

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_sort_history_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.outIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לבחור פריט", Toast.LENGTH_SHORT).show();

                //} else if (sortHistory.get(selectedItem).isFromSale() || sortHistory.get(selectedItem).isFromBuy()) {
                    Toast.makeText(this, "אין עוד פירוט", Toast.LENGTH_SHORT).show();

                } else {
                    ActionBar actionBar = getSupportActionBar();
                    assert actionBar != null;
                    actionBar.setTitle("כניסות/יציאות:  " + filterSorts.get(selectedItem).getFromName() + "-" + (filterSorts.get(selectedItem).getSortCount()));
                    DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                 //   sortInfoBuilder.setWhereClause("toId = '" + sortHistory.get(selectedItem).getFromId() + "'");

                    Backendless.Data.of(SortInfo.class).find(sortInfoBuilder, new AsyncCallback<List<SortInfo>>() {
                        @Override
                        public void handleResponse(List<SortInfo> response) {
                            filterSorts = response;
                            sortHistoryAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(TableSortHistoryActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;

            case R.id.inIcon:
                break;
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
