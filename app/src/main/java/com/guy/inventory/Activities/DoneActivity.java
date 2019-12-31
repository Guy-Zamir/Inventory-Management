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
import android.widget.Switch;
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


public class DoneActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnDoneSubmit;
    EditText etDoneWage, etDoneWeight;
    Switch swDoneWeight;

    private ThreadPoolExecutor mPool;

    EditText etWeightSort1, etPriceSort1, etWeightSort2, etPriceSort2, etWeightSort3, etPriceSort3,
            etWeightSort4, etPriceSort4, etWeightSort5, etPriceSort5;

    AutoCompleteTextView acSort1, acSort2, acSort3, acSort4, acSort5;

    final double VALUE_MARGIN = 10.0;
    final double CARAT_MARGIN = 0.01;

    List<SortInfo> sortCheck;

    final String LEFT_OVER_NAME = "עודפים";
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String whereClauseEmail = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String whereClauseLast = "last = true";
    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    ArrayAdapter<String> sortAdapter;

    int chosenSort1 = -1, chosenSort2 = -1, chosenSort3 = -1, chosenSort4 = -1, chosenSort5 = -1;

    double doneWeight, wage;
    int index;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etDoneWage = findViewById(R.id.etDoneWage);
        etDoneWeight = findViewById(R.id.etDoneWeight);
        swDoneWeight = findViewById(R.id.swDoneWeight);
        btnDoneSubmit = findViewById(R.id.btnDoneSubmit);

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

        // No need for now
        TextView tvDoneWage = findViewById(R.id.tvDoneWage);
        tvDoneWage.setVisibility(View.GONE);
        etDoneWage.setVisibility(View.GONE);
        //////////////////

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("סרייה גמורה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        sortBuilder.setWhereClause(whereClauseEmail);
        sortBuilder.setHavingClause(whereClauseLast);
        sortBuilder.setPageSize(100);
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
                sortAdapter = new ArrayAdapter<>(DoneActivity.this, android.R.layout.select_dialog_singlechoice, sortNames);
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
                    Toast.makeText(DoneActivity.this, "טרם הוגדרו מיונים, עליך להגדיר מיון חדש לפני שמירת הקניה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
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
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
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
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
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
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
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
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
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

        swDoneWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swDoneWeight.setText(swDoneWeight.isChecked() ? " אחוז ליטוש  " : "  משקל גמור  ");
            }
        });

        btnDoneSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCheck = new ArrayList<>();

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

                wage = (etDoneWage.getText().toString().isEmpty()) ? 0 : Double.parseDouble(etDoneWage.getText().toString().trim());

                if (swDoneWeight.isChecked()) {
                    doneWeight = (etDoneWeight.getText().toString().isEmpty()) ? -1 : ((Double.parseDouble(etDoneWeight.getText().toString().trim()) / 100) * InventoryApp.buys.get(index).getWeight());
                } else {
                    doneWeight = (etDoneWeight.getText().toString().isEmpty()) ? -1 : (Double.parseDouble(etDoneWeight.getText().toString().trim()));
                }

                // Checking whether the user entered done weight right
                if (doneWeight > (InventoryApp.buys.get(index).getWeight() + CARAT_MARGIN)) {
                    Toast.makeText(DoneActivity.this, "סכום המשקל הגמור גבוהה ממשקל החבילה, יש להזין משקל שווה או נמוך למשקל החבילה", Toast.LENGTH_LONG).show();

                } else if (doneWeight < 0) {
                    Toast.makeText(DoneActivity.this, "לא הוזן משקל גמור, יש להזין את המשקל הגמור של החבילה", Toast.LENGTH_LONG).show();

                } else {
                    if (sortValueSum  < InventoryApp.buys.get(index).getSum() - VALUE_MARGIN || sortValueSum > InventoryApp.buys.get(index).getSum() + VALUE_MARGIN) {
                        Toast.makeText(DoneActivity.this, "סכום שווי המיונים לא שווה לשווי הסחורה, יש להזין מחירים מתאימים", Toast.LENGTH_LONG).show();

                    } else if (sortWeightSum < doneWeight - CARAT_MARGIN || sortWeightSum > doneWeight + CARAT_MARGIN) {
                        Toast.makeText(DoneActivity.this, "סכום המשקל של המיונים לא שווה למשקל הגמור, יש להזין משקלים מתאמים", Toast.LENGTH_LONG).show();

                    } else {

                        // Assigning what's left from the done weight to the left over sort
                        final double sortWeightLeftOver = doneWeight - sortWeightSum;
                        final double sortPriceLeftOver = (sortWeightLeftOver != 0) ? (InventoryApp.buys.get(index).getSum() - sortValueSum) / sortWeightLeftOver : 0;

                        AlertDialog.Builder alert = new AlertDialog.Builder(DoneActivity.this);
                        alert.setTitle("שינוי נתונים");
                        alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                        alert.setNegativeButton(android.R.string.no, null);
                        alert.setIcon(android.R.drawable.ic_dialog_alert);
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Saving the changes to the buy
                                InventoryApp.buys.get(index).setDone(true);
                                InventoryApp.buys.get(index).setDoneWeight(doneWeight);
                                InventoryApp.buys.get(index).setWage(wage);
                                InventoryApp.buys.get(index).setWorkDepreciation(doneWeight / InventoryApp.buys.get(index).getWeight());

                                showProgress(true);
                                submitRunnableTask(new Runnable() {
                                    @Override
                                    public void run() {
                                        Backendless.Persistence.save(InventoryApp.buys.get(index));

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

                                        if (sortWeightLeftOver > CARAT_MARGIN) {
                                            // Saving the left over
                                            Sort leftOverSort = findLeftOver();
                                            leftOverSort.setSum(leftOverSort.getSum() + (sortWeightLeftOver * sortPriceLeftOver));
                                            leftOverSort.setWeight(leftOverSort.getWeight() + sortWeightLeftOver);
                                            leftOverSort.setPrice(leftOverSort.getSum() / leftOverSort.getWeight());
                                            leftOverSort.save();

                                            SortInfo sortInfoLeftOver = new SortInfo();
                                            sortInfoLeftOver.setFromName(InventoryApp.buys.get(index).getSupplierName());
                                            sortInfoLeftOver.setToName(leftOverSort.getName());
                                            sortInfoLeftOver.setBuy(true);
                                            sortInfoLeftOver.setSale(false);
                                            sortInfoLeftOver.setToId(leftOverSort.getObjectId());
                                            sortInfoLeftOver.setSortCount(leftOverSort.getSortCount());
                                            sortInfoLeftOver.setFromId(InventoryApp.buys.get(index).getObjectId());
                                            sortInfoLeftOver.setPrice(sortPriceLeftOver);
                                            sortInfoLeftOver.setWeight(sortWeightLeftOver);
                                            sortInfoLeftOver.setSum(sortPriceLeftOver * sortWeightLeftOver);
                                            sortInfoLeftOver.setUserEmail(InventoryApp.user.getEmail());
                                            sortInfoLeftOver.save();
                                        }
                                    }

                                });
                                showProgress(false);
                                Toast.makeText(DoneActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                DoneActivity.this.finish();
                            }
                        });
                        alert.show();
                    }
                }
            }
        });
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
    public SortInfo sortInfoSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            SortInfo sortInfo = new SortInfo();
            sortInfo.setFromName(InventoryApp.buys.get(index).getSupplierName());
            sortInfo.setToName(InventoryApp.sorts.get(chosenSort).getName());
            sortInfo.setToId(InventoryApp.sorts.get(chosenSort).getObjectId());
            sortInfo.setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount());
            sortInfo.setFromId(InventoryApp.buys.get(index).getObjectId());
            sortInfo.setPrice(sortPrice);
            sortInfo.setWeight(sortWeight);
            sortInfo.setSum(sortPrice*sortWeight);
            sortInfo.setBuy(true);
            sortInfo.setSale(false);
            sortInfo.setOpen(false);
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

    public Sort findLeftOver() {
        for (Sort sort : InventoryApp.sorts) {
            if (sort.getName().equals(LEFT_OVER_NAME)) {
                return sort;
            }
        }
        return null;
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

