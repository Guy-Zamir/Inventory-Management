package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.BrokerSort;
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NewBrokerSortActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ThreadPoolExecutor mPool;

    private EditText etBrokerWeight, etBrokerPrice, etBrokerPriceINV, etBrokerName;
    private ArrayAdapter<String> sortAdapter;
    private AutoCompleteTextView acSorts;

    private int chosenSort = -1;
    final int PAGE_SIZE = 100;

    private String kind;
    private boolean add;
    private int index;

    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String LEFT_OVER_NAME = "עודפים";
    final String whereClauseEmail = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String whereClauseLast = "last = true";
    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");

    private String sortName;
    private double price, priceINV, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_broker_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etBrokerWeight = findViewById(R.id.etBrokerWeight);
        etBrokerPrice = findViewById(R.id.etBrokerPrice);
        etBrokerPriceINV = findViewById(R.id.etBrokerPriceINV);
        etBrokerName = findViewById(R.id.etBrokerName);
        acSorts = findViewById(R.id.acSorts);
        Button btnBrokerSubmit = findViewById(R.id.btnBrokerSubmit);

        mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        kind = getIntent().getStringExtra("kind");
        add = getIntent().getBooleanExtra("add", false);
        index = getIntent().getIntExtra("index", -1);

        etBrokerName.setVisibility(add ? View.GONE : View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(add ? "הוספה לחבילת מתווך - " + kind : "חבילת מתווך חדש - " + kind);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sortBuilder.setWhereClause(whereClauseEmail);
        sortBuilder.setHavingClause(whereClauseLast);
        sortBuilder.setPageSize(PAGE_SIZE);
        sortBuilder.setSortBy("name");

        showProgress(true);
        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                ArrayList<String> sortNames = new ArrayList<>();
                for (Sort sort : response) {
                    if (!sort.getName().equals(LEFT_OVER_NAME)) {
                        sortNames.add(sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C");
                    }
                }

                InventoryApp.sorts = response;
                sortAdapter = new ArrayAdapter<>(NewBrokerSortActivity.this, android.R.layout.select_dialog_singlechoice, sortNames);
                acSorts.setThreshold(1);
                acSorts.setAdapter(sortAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(NewBrokerSortActivity.this, "טרם הוגדרו מיונים, עליך להגדיר מיון חדש לפני שמירת הקניה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewBrokerSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        acSorts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort = i;
                        break;
                    }
                }
            }
        });

        acSorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSorts.showDropDown();
            }
        });

        btnBrokerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check().equals("OK")) {
                    showProgress(true);
                    BrokerSort brokerSort = brokerSortSave();
                    Backendless.Data.of(BrokerSort.class).save(brokerSort, new AsyncCallback<BrokerSort>() {
                        @Override
                        public void handleResponse(BrokerSort response) {
                            String brokerSortId = response.getObjectId();
                            Backendless.Data.of(SortInfo.class).save(sortInfoSave(brokerSortId), new AsyncCallback<SortInfo>() {
                                @Override
                                public void handleResponse(SortInfo response) {
                                    Backendless.Data.of(Sort.class).save(sortSave(), new AsyncCallback<Sort>() {
                                        @Override
                                        public void handleResponse(Sort response) {
                                            showProgress(false);
                                            Toast.makeText(NewBrokerSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                            setResult(RESULT_OK);
                                            finishActivity(1);
                                            NewBrokerSortActivity.this.finish();
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            showProgress(false);
                                            Toast.makeText(NewBrokerSortActivity.this, check(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(NewBrokerSortActivity.this, check(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(NewBrokerSortActivity.this, check(), Toast.LENGTH_LONG).show();
                        }
                    });

                    // Saving all at once (No need for now)
                    //submitRunnableTask(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        BrokerSort brokerSort = brokerSortSave();
                    //        brokerSort.save();
                    //        String brokerSortId = brokerSort.getObjectId();
                    //        sortInfoSave(brokerSortId).save();
                    //        sortSave().save();
                    //    }
                    //});

                } else {
                    showProgress(false);
                    Toast.makeText(NewBrokerSortActivity.this, check(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String check() {
        if ((etBrokerName.getText().toString().isEmpty() && !add) || etBrokerPrice.getText().toString().isEmpty()
                || etBrokerPriceINV.getText().toString().isEmpty() || etBrokerWeight.getText().toString().isEmpty() ||
        chosenSort == -1) {
            return "יש להזין את כל הנתונים";

        } else if (Double.valueOf(etBrokerPrice.getText().toString()) < Double.valueOf(etBrokerPriceINV.getText().toString())) {
            return "מחיר המלאי צריך להיות נמוך או שווה למחיר המתווך, יש להזין נתונים נכונים";

        } else if (Double.valueOf(etBrokerWeight.getText().toString()) > InventoryApp.sorts.get(chosenSort).getWeight()) {
            return "משקל החבילה גבוה מהמשקל שקיים במלאי, יש להזין נתנונים נכונים";

        } else if ((Double.valueOf(etBrokerWeight.getText().toString()) * Double.valueOf(etBrokerPriceINV.getText().toString())) > InventoryApp.sorts.get(chosenSort).getSum()) {
            return "סכום החבילה גבוה מהסכום שמקיים במלאי, יש להזין נתנונים נכונים";

        } else {
            price = Double.valueOf(etBrokerPrice.getText().toString());
            priceINV = Double.valueOf(etBrokerPriceINV.getText().toString());
            sortName = (add ? InventoryApp.brokerSorts.get(index).getName() : etBrokerName.getText().toString().trim());
            weight = Double.valueOf(etBrokerWeight.getText().toString());
            return "OK";
        }
    }

    public SortInfo sortInfoSave(String toId) {
        SortInfo sortInfo = new SortInfo();
        sortInfo.setFromName(InventoryApp.sorts.get(chosenSort).getName());
        sortInfo.setToName(kind + " - " + sortName);
        sortInfo.setToId(toId);
        sortInfo.setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount());
        sortInfo.setFromId(InventoryApp.sorts.get(chosenSort).getObjectId());
        sortInfo.setPrice(priceINV);
        sortInfo.setWeight(weight);
        sortInfo.setSum(priceINV * weight);
        sortInfo.setKind("broker");
        sortInfo.setOut(true);
        sortInfo.setUserEmail(InventoryApp.user.getEmail());
        return sortInfo;
    }

    public BrokerSort brokerSortSave() {
        if (add) {
            BrokerSort brokerSort = InventoryApp.brokerSorts.get(index);
            brokerSort.setSum(brokerSort.getSum() + (weight*price));
            brokerSort.setWeight(brokerSort.getWeight() + weight);
            brokerSort.setPrice(brokerSort.getSum() / brokerSort.getPrice());
            brokerSort.setSumINV(brokerSort.getSumINV() + (weight*priceINV));
            brokerSort.setPriceINV(brokerSort.getSumINV() / brokerSort.getWeight());
            return brokerSort;

        } else {
            BrokerSort brokerSort = new BrokerSort();
            brokerSort.setMemo(false);
            brokerSort.setFromSortId(InventoryApp.sorts.get(chosenSort).getObjectId());
            brokerSort.setKind(kind);
            brokerSort.setName(sortName);
            brokerSort.setPrice(price);
            brokerSort.setPriceINV(priceINV);
            brokerSort.setWeight(weight);
            brokerSort.setSum(weight * price);
            brokerSort.setSumINV(weight * priceINV);
            brokerSort.setUserEmail(InventoryApp.user.getEmail());
            return brokerSort;
        }
    }

    public Sort sortSave() {
        Sort sort = InventoryApp.sorts.get(chosenSort);
        sort.setSum(sort.getSum() - (priceINV * weight));
        sort.setWeight(sort.getWeight() - weight);
        sort.setPrice(sort.getSum() / sort.getWeight());
        return sort;
    }

    public void submitRunnableTask(Runnable task){
        if(!mPool.isShutdown() && mPool.getActiveCount() != mPool.getMaximumPoolSize()){
            mPool.submit(task);
        } else {
            new Thread(task).start(); // Actually this should never happen, just in case...
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}