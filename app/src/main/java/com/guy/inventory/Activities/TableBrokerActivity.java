package com.guy.inventory.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
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
import com.guy.inventory.Adapters.BrokerAdapter;
import com.guy.inventory.Adapters.MemoAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.BrokerSort;
import com.guy.inventory.Tables.Memo;
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;
import java.util.List;

public class TableBrokerActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private String kind;
    private boolean memo;

    final DataQueryBuilder brokerBuilder = DataQueryBuilder.create();
    final DataQueryBuilder memoBuilder = DataQueryBuilder.create();
    private ListView lvBrokerList;
    private BrokerAdapter brokerAdapter;
    private MemoAdapter memoAdapter;
    private int selectedItem = -1;
    private final String emailClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    private final String ALL = "כל המיונים";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_broker);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvBrokerList = findViewById(R.id.lvBrokerList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        kind = getIntent().getStringExtra("kind");
        memo = getIntent().getBooleanExtra("memo", false);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(memo ? kind + " - ממו" : kind + " - ניב");
        actionBar.setDisplayHomeAsUpEnabled(true);

        brokerBuilder.setWhereClause(emailClause);
        memoBuilder.setWhereClause(emailClause);
        int PAGE_SIZE = 100;
        brokerBuilder.setPageSize(PAGE_SIZE);
        memoBuilder.setPageSize(PAGE_SIZE);
        if (!kind.equals(ALL)) {
            brokerBuilder.setHavingClause("kind = '" + kind + "'");
            memoBuilder.setHavingClause("kind = '" + kind + "'");
        }

        // Broker Activity
        if (!memo) {
            showProgress(true);
            Backendless.Data.of(BrokerSort.class).find(brokerBuilder, new AsyncCallback<List<BrokerSort>>() {
                @Override
                public void handleResponse(List<BrokerSort> response) {
                    InventoryApp.brokerSorts = response;
                    brokerAdapter = new BrokerAdapter(TableBrokerActivity.this, InventoryApp.brokerSorts);
                    lvBrokerList.setAdapter(brokerAdapter);
                    brokerAdapter.setAll(kind.equals(ALL));
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        // Memo Activity
        } else {
            showProgress(true);
            Backendless.Data.of(Memo.class).find(memoBuilder, new AsyncCallback<List<Memo>>() {
                @Override
                public void handleResponse(List<Memo> response) {
                    InventoryApp.memos = response;
                    memoAdapter = new MemoAdapter(TableBrokerActivity.this, InventoryApp.memos);
                    lvBrokerList.setAdapter(memoAdapter);
                    memoAdapter.setAll(kind.equals(ALL));
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        lvBrokerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;

                if (memo) {
                    memoAdapter.setSelectedPosition(position);
                    memoAdapter.notifyDataSetChanged();

                } else {
                    brokerAdapter.setSelectedPosition(position);
                    brokerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(memo ? R.menu.table_memo_action_bar : R.menu.table_broker_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                if (!kind.equals(ALL)) {
                    Intent newBrokerSort = new Intent(TableBrokerActivity.this, NewBrokerSortActivity.class);
                    newBrokerSort.putExtra("kind", kind);
                    newBrokerSort.putExtra("add", false);
                    newBrokerSort.putExtra("memo", memo);
                    startActivityForResult(newBrokerSort, 1);
                } else {
                    Toast.makeText(this, "לא ניתן ליצור מיון מתוך עמוד כלל המיונים", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.addIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור חבילה להוספה", Toast.LENGTH_SHORT).show();

                } else {
                    Intent newBrokerSort = new Intent(TableBrokerActivity.this, NewBrokerSortActivity.class);
                    if (memo) {
                        newBrokerSort.putExtra("kind", InventoryApp.memos.get(selectedItem).getKind());
                    } else {
                        newBrokerSort.putExtra("kind", InventoryApp.brokerSorts.get(selectedItem).getKind());
                    }

                    newBrokerSort.putExtra("add", true);
                    newBrokerSort.putExtra("index", selectedItem);
                    newBrokerSort.putExtra("memo", memo);
                    startActivityForResult(newBrokerSort, 1);
                }
                break;

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור חבילה למחיקה", Toast.LENGTH_SHORT).show();

                } else {
                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(TableBrokerActivity.this);
                    deleteAlert.setTitle("התראת מחיקה");
                    deleteAlert.setMessage("האם אתה בטוח שברצונך למחוק את החבילה המסומנת?");
                    deleteAlert.setNegativeButton(android.R.string.no, null);
                    deleteAlert.setIcon(android.R.drawable.ic_dialog_alert);
                    deleteAlert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");

                            final DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                            if (memo) {
                                sortInfoBuilder.setWhereClause("toId = '" + InventoryApp.memos.get(selectedItem).getObjectId() + "'");

                            } else {
                                sortInfoBuilder.setWhereClause("toId = '" + InventoryApp.brokerSorts.get(selectedItem).getObjectId() + "'");
                            }
                            Backendless.Data.of(SortInfo.class).find(sortInfoBuilder, new AsyncCallback<List<SortInfo>>() {
                                @Override
                                public void handleResponse(List<SortInfo> response) {
                                    // Finding all the sortInfo and deleting them after collecting the data of the weight and sum
                                    double sum = 0;
                                    double weight = 0;
                                    String sortName = "";

                                    for (SortInfo sortInfo : response) {
                                        sum += sortInfo.getSum();
                                        weight += sortInfo.getWeight();
                                        sortName = sortInfo.getFromName();
                                    }

                                    final double allSum = sum;
                                    final double allWeight = weight;
                                    final String name = sortName;

                                    String whereClause;
                                    if (memo) {
                                        whereClause = "toId = '" + InventoryApp.memos.get(selectedItem).getObjectId() + "'";

                                    } else {
                                        whereClause = "toId = '" + InventoryApp.brokerSorts.get(selectedItem).getObjectId() + "'";
                                    }

                                    Backendless.Data.of("SortInfo").remove(whereClause, new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {
                                            DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                                            sortBuilder.setWhereClause("name = '" + name + "'");
                                            sortBuilder.setHavingClause("last = true");
                                            Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
                                                @Override
                                                public void handleResponse(List<Sort> response) {
                                                    final Sort sort = response.get(0);

                                                    // Adding to the original sort the weight and sum
                                                    sort.setSum(sort.getSum() + allSum);
                                                    sort.setWeight(sort.getWeight() + allWeight);
                                                    sort.setPrice(sort.getSum() / sort.getWeight());
                                                    Backendless.Data.of(Sort.class).save(sort, new AsyncCallback<Sort>() {
                                                        @Override
                                                        public void handleResponse(Sort response) {

                                                            // Deleting the Memo
                                                            if (memo) {
                                                                Backendless.Persistence.of(Memo.class).remove(InventoryApp.memos.get(selectedItem), new AsyncCallback<Long>() {
                                                                    @Override
                                                                    public void handleResponse(Long response) {
                                                                        showProgress(false);
                                                                        Toast.makeText(TableBrokerActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                                                        setResult(RESULT_OK);
                                                                        finishActivity(1);
                                                                        TableBrokerActivity.this.finish();
                                                                    }

                                                                    @Override
                                                                    public void handleFault(BackendlessFault fault) {
                                                                        Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        showProgress(false);
                                                                    }
                                                                });

                                                            // Deleting the brokerSort
                                                            } else {
                                                                Backendless.Persistence.of(BrokerSort.class).remove(InventoryApp.brokerSorts.get(selectedItem), new AsyncCallback<Long>() {
                                                                    @Override
                                                                    public void handleResponse(Long response) {
                                                                        showProgress(false);
                                                                        Toast.makeText(TableBrokerActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                                                        setResult(RESULT_OK);
                                                                        finishActivity(1);
                                                                        TableBrokerActivity.this.finish();
                                                                    }

                                                                    @Override
                                                                    public void handleFault(BackendlessFault fault) {
                                                                        Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        showProgress(false);
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {
                                                            Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                            showProgress(false);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                    showProgress(false);
                                                }
                                            });
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            });
                        }
                    });
                    deleteAlert.show();
                }
                break;

            case R.id.memoIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור חבילה", Toast.LENGTH_SHORT).show();
                } else {
                    boolean memo = !InventoryApp.brokerSorts.get(selectedItem).getMemo();
                    InventoryApp.brokerSorts.get(selectedItem).setMemo(memo);
                    showProgress(true);
                    Backendless.Persistence.of(BrokerSort.class).save(InventoryApp.brokerSorts.get(selectedItem), new AsyncCallback<BrokerSort>() {
                        @Override
                        public void handleResponse(BrokerSort response) {
                            Toast.makeText(TableBrokerActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            brokerAdapter.notifyDataSetChanged();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!memo) {
                showProgress(true);
                Backendless.Data.of(BrokerSort.class).find(brokerBuilder, new AsyncCallback<List<BrokerSort>>() {
                    @Override
                    public void handleResponse(List<BrokerSort> response) {
                        InventoryApp.brokerSorts = response;
                        brokerAdapter = new BrokerAdapter(TableBrokerActivity.this, InventoryApp.brokerSorts);
                        lvBrokerList.setAdapter(brokerAdapter);
                        brokerAdapter.setAll(kind.equals(ALL));
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                showProgress(true);
                Backendless.Data.of(Memo.class).find(brokerBuilder, new AsyncCallback<List<Memo>>() {
                    @Override
                    public void handleResponse(List<Memo> response) {
                        InventoryApp.memos = response;
                        memoAdapter = new MemoAdapter(TableBrokerActivity.this, InventoryApp.memos);
                        lvBrokerList.setAdapter(memoAdapter);
                        memoAdapter.setAll(kind.equals(ALL));
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(TableBrokerActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
