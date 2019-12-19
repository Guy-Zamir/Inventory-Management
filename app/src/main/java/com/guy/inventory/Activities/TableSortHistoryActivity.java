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
import java.util.List;

public class TableSortHistoryActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ListView lvSortHistoryList;
    private SortHistoryAdapter sortHistoryAdapter;
    public List<SortInfo> fullSortHistory;
    public List<SortInfo> filteredHistory;

    private int index;
    final int PAGE_SIZE = 100;

    private final DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
    private final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";

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
        sortInfoBuilder.setWhereClause(EMAIL_CLAUSE);
        sortInfoBuilder.setSortBy("created DESC");
        sortInfoBuilder.setWhereClause("toId = '" + InventoryApp.sorts.get(index).getObjectId() + "'");
        sortInfoBuilder.setPageSize(PAGE_SIZE);

        showProgress(true);
        Backendless.Data.of(SortInfo.class).find(sortInfoBuilder, new AsyncCallback<List<SortInfo>>() {
            @Override
            public void handleResponse(List<SortInfo> response) {
                fullSortHistory = response;

                filteredHistory = filterSortHistory(true);
                sortHistoryAdapter = new SortHistoryAdapter(TableSortHistoryActivity.this, filteredHistory);
                sortHistoryAdapter.setSelectedSort(index);
                lvSortHistoryList.setAdapter(sortHistoryAdapter);
                showProgress(false);
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
                if (InventoryApp.sorts.get(index).getSortCount() != InventoryApp.sorts.get(index).getSortCount() + sortHistoryAdapter.getI()) {
                    sortHistoryAdapter.reduceI();

                    // Deleting all the sortInfo that doesn't matter
                    List<SortInfo> toDelete = new ArrayList<>();
                    for (SortInfo sortInfo : fullSortHistory) {
                        if (sortInfo.isFromSale() && (InventoryApp.sorts.get(index).getSortCount() - sortHistoryAdapter.getI() != sortInfo.getSortCount())) {
                            toDelete.add(sortInfo);
                        }
                    }
                    fullSortHistory.removeAll(toDelete);

                    sortHistoryAdapter.notifyDataSetChanged();
                    ActionBar actionBar = getSupportActionBar();
                    assert actionBar != null;
                    actionBar.setTitle("כניסות/יציאות:  " + InventoryApp.sorts.get(index).getName() + "-" + (InventoryApp.sorts.get(index).getSortCount() - sortHistoryAdapter.getI()));
                } else {
                    Toast.makeText(this, "אין עוד פירוט", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.inIcon:
                if (InventoryApp.sorts.get(index).getSortCount() - sortHistoryAdapter.getI() != 0) {
                    sortHistoryAdapter.addI();

                    filterSortHistory(false);
                    sortHistoryAdapter.notifyDataSetChanged();
                    ActionBar actionBar = getSupportActionBar();
                    assert actionBar != null;
                    actionBar.setTitle("כניסות/יציאות:  " + InventoryApp.sorts.get(index).getName() + "-" + (InventoryApp.sorts.get(index).getSortCount() - sortHistoryAdapter.getI()));
                } else {
                    Toast.makeText(this, "אין עוד פירוט", Toast.LENGTH_SHORT).show();
                }
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

    // Filtering all the sortInfo that doesn't matter
    private List<SortInfo> filterSortHistory(boolean first) {
        List<SortInfo> filteredHistory = new ArrayList<>(fullSortHistory);
        List<SortInfo> toDelete = new ArrayList<>();
        for (SortInfo sortInfo : fullSortHistory) {
            if (first) {
                if (sortInfo.isFromSale() && (InventoryApp.sorts.get(index).getSortCount() != sortInfo.getSortCount())) {
                    toDelete.add(sortInfo);
                }
            } else if (sortInfo.isFromSale() && (InventoryApp.sorts.get(index).getSortCount() - sortHistoryAdapter.getI() != sortInfo.getSortCount())) {
                toDelete.add(sortInfo);
            }
        }
        filteredHistory.removeAll(toDelete);
        return filteredHistory;
    }
}