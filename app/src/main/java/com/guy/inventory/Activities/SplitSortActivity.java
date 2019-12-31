package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SplitSortActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnSplitSubmit;
    private ThreadPoolExecutor mPool;

    EditText etWeightSort1, etPriceSort1, etWeightSort2, etPriceSort2, etWeightSort3, etPriceSort3, etWeightSort4, etPriceSort4, etWeightSort5, etPriceSort5;
    AutoCompleteTextView acSort1, acSort2, acSort3, acSort4, acSort5;

    final String LEFT_OVER_NAME = "עודפים";
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String whereClauseEmail = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    ArrayAdapter<String> sortAdapter;

    List<SortInfo> sortCheck;

    int chosenSort1 = -1, chosenSort2 = -1, chosenSort3 = -1, chosenSort4 = -1, chosenSort5 = -1;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnSplitSubmit = findViewById(R.id.btnSplitSubmit);

        etWeightSort1 = findViewById(R.id.etWeightSort1);
        etPriceSort1 = findViewById(R.id.etPriceSort1);
        acSort1 = findViewById(R.id.acSort1);

        etWeightSort2 = findViewById(R.id.etWeightSort2);
        etPriceSort2 = findViewById(R.id.etPriceSort2);
        acSort2 = findViewById(R.id.acSort2);

        etWeightSort3 = findViewById(R.id.etWeightSort3);
        etPriceSort3 = findViewById(R.id.etPriceSort3);
        acSort3 = findViewById(R.id.acSort3);

        etWeightSort4 = findViewById(R.id.etWeightSort4);
        etPriceSort4 = findViewById(R.id.etPriceSort4);
        acSort4 = findViewById(R.id.acSort4);

        etWeightSort5 = findViewById(R.id.etWeightSort5);
        etPriceSort5 = findViewById(R.id.etPriceSort5);
        acSort5 = findViewById(R.id.acSort5);

        index = getIntent().getIntExtra("index", 0);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("פיצול מיון");
        actionBar.setDisplayHomeAsUpEnabled(true);

        sortBuilder.setWhereClause(whereClauseEmail);
        sortBuilder.setHavingClause("last = true");
        sortBuilder.setSortBy("created DESC");

        showProgress(true);
        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                ArrayList<String> sortNames = new ArrayList<>();
                for (Sort sort : response) {
                    if (!(sort.getName().equals(LEFT_OVER_NAME) || sort.getName().equals(InventoryApp.sorts.get(index).getName()))) {
                        sortNames.add(sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C");
                    }
                }

                InventoryApp.sorts = response;
                sortAdapter = new ArrayAdapter<>(SplitSortActivity.this, android.R.layout.select_dialog_singlechoice, sortNames);
                acSort1.setThreshold(1);
                acSort1.setAdapter(sortAdapter);
                acSort2.setThreshold(1);
                acSort2.setAdapter(sortAdapter);
                acSort3.setThreshold(1);
                acSort3.setAdapter(sortAdapter);
                acSort4.setThreshold(1);
                acSort4.setAdapter(sortAdapter);
                acSort5.setThreshold(1);
                acSort5.setAdapter(sortAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(SplitSortActivity.this, "טרם הוגדרו מיונים, עליך להגדיר מיון חדש לפני שמירת הקניה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplitSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        acSort1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort1 = i;
                        break;
                    }
                }
            }
        });

        acSort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSort1.showDropDown();
            }
        });

        acSort2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort2 = i;
                        break;
                    }
                }
            }
        });

        acSort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSort2.showDropDown();
            }
        });

        acSort3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort3 = i;
                        break;
                    }
                }
            }
        });

        acSort3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSort3.showDropDown();
            }
        });

        acSort4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort4 = i;
                        break;
                    }
                }
            }
        });

        acSort4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSort4.showDropDown();
            }
        });

        acSort5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " : " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenSort5 = i;
                        break;
                    }
                }
            }
        });

        acSort5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSort5.showDropDown();
            }
        });

        btnSplitSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCheck = new ArrayList<>();

                final Sort splitSort = InventoryApp.sorts.get(index);

                // Checking the sorts and assigning the values;
                sortCheck(etPriceSort1, etWeightSort1, chosenSort1);
                sortCheck(etPriceSort2, etWeightSort2, chosenSort2);
                sortCheck(etPriceSort3, etWeightSort3, chosenSort3);
                sortCheck(etPriceSort4, etWeightSort4, chosenSort4);
                sortCheck(etPriceSort5, etWeightSort5, chosenSort5);

                double sortWeightSum = 0;
                double sortValueSum = 0;

                for (int i=0; i < sortCheck.size(); i++) {
                    sortWeightSum += sortCheck.get(i).getWeight();
                    sortValueSum += sortCheck.get(i).getSum();
                }

                if (sortValueSum > splitSort.getSum()) {
                    Toast.makeText(SplitSortActivity.this, "סכום שווי המיונים גבוהה משווי המיון ממנו מבוצע הפיצול, יש להזין מחירים מתאימים", Toast.LENGTH_LONG).show();

                } else if (sortWeightSum > splitSort.getWeight()) {
                    Toast.makeText(SplitSortActivity.this, "סכום משקל המיונים גבוהה ממשקל המיון ממנו מבוצע הפיצול, יש לזין משקלים מתאימים", Toast.LENGTH_SHORT).show();

                } else if ((sortWeightSum == splitSort.getWeight() && sortValueSum != splitSort.getSum())) {
                    Toast.makeText(SplitSortActivity.this, "סכום שווי המיונים לא שווה לשווי המיון ממנו מבוצע הפיצול, יש להזין מחירים מתאימים", Toast.LENGTH_SHORT).show();

                } else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(SplitSortActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            double weightSum = 0;
                            double valueSum = 0;

                            for (int i=0; i < sortCheck.size(); i++) {
                                weightSum += sortCheck.get(i).getWeight();
                                valueSum += sortCheck.get(i).getSum();
                            }
                            // Saving the changes to the sort

                            splitSort.setWeight(splitSort.getWeight()- weightSum);
                            splitSort.setSum(splitSort.getSum()-valueSum);
                            splitSort.setPrice(splitSort.getWeight() == 0 ? 0 : splitSort.getSum()/splitSort.getWeight());

                            showProgress(true);
                            submitRunnableTask(new Runnable() {
                                @Override
                                public void run() {
                                    Backendless.Persistence.save(InventoryApp.sorts.get(index));

                                    // Checking the sorts and assigning the values if the sort was chosen
                                    Sort sort1 = sortSave(etPriceSort1, etWeightSort1, chosenSort1);
                                    Sort sort2 = sortSave(etPriceSort2, etWeightSort2, chosenSort2);
                                    Sort sort3 = sortSave(etPriceSort3, etWeightSort3, chosenSort3);
                                    Sort sort4 = sortSave(etPriceSort4, etWeightSort4, chosenSort4);
                                    Sort sort5 = sortSave(etPriceSort5, etWeightSort5, chosenSort5);

                                    if (sort1 != null) {
                                        sort1.save();

                                        SortInfo sortInfo1 = sortInfoSave(etPriceSort1, etWeightSort1, chosenSort1);
                                        sortInfo1.save();
                                    }

                                    if (sort2 != null) {
                                        sort2.save();

                                        SortInfo sortInfo2 = sortInfoSave(etPriceSort2, etWeightSort2, chosenSort2);
                                        sortInfo2.save();
                                    }

                                    if (sort3 != null) {
                                        sort3.save();

                                        SortInfo sortInfo3 = sortInfoSave(etPriceSort3, etWeightSort3, chosenSort3);
                                        sortInfo3.save();
                                    }

                                    if (sort4 != null) {
                                        sort4.save();

                                        SortInfo sortInfo4 = sortInfoSave(etPriceSort4, etWeightSort4, chosenSort4);
                                        sortInfo4.save();
                                    }

                                    if (sort5 != null) {
                                        sort5.save();

                                        SortInfo sortInfo5 = sortInfoSave(etPriceSort5, etWeightSort5, chosenSort5);
                                        sortInfo5.save();
                                    }
                                }

                            });
                            showProgress(false);
                            Toast.makeText(SplitSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finishActivity(1);
                            SplitSortActivity.this.finish();
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    public SortInfo sortInfoSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            SortInfo sortInfo = new SortInfo();
            sortInfo.setFromName(InventoryApp.sorts.get(index).getName());
            sortInfo.setToName(InventoryApp.sorts.get(chosenSort).getName());
            sortInfo.setToId(InventoryApp.sorts.get(chosenSort).getObjectId());
            sortInfo.setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount());
            sortInfo.setFromId(InventoryApp.sorts.get(index).getObjectId());
            sortInfo.setPrice(sortPrice);
            sortInfo.setWeight(sortWeight);
            sortInfo.setSum(sortPrice*sortWeight);
            sortInfo.setBuy(false);
            sortInfo.setSale(false);
            sortInfo.setSplit(true);
            sortInfo.setUserEmail(InventoryApp.user.getEmail());

            return sortInfo;
        } else {
            return null;
        }
    }

    public Sort sortSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            if (sortWeight <= 0) {
                return null;
            }

            Sort sort = InventoryApp.sorts.get(chosenSort);
            sort.setSum(sort.getSum() + (sortPrice*sortWeight));
            sort.setWeight(sort.getWeight() + sortWeight);
            sort.setPrice(sort.getSum()/sort.getWeight());

            return sort;
        } else {
            return null;
        }
    }

    public void sortCheck(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            SortInfo sortInfo = new SortInfo();
            sortInfo.setPrice(sortPrice);
            sortInfo.setWeight(sortWeight);
            sortInfo.setSum(sortPrice*sortWeight);
            sortCheck.add(sortInfo);
        }
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