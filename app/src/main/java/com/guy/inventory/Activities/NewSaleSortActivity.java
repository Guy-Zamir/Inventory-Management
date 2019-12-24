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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NewSaleSortActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private ThreadPoolExecutor mPool;

    Button btnNewSaleSortSubmit;

    EditText etWeightSortS1, etProfitSortS1, etPriceSortS1,
            etWeightSortS2, etProfitSortS2, etPriceSortS2,
            etWeightSortS3, etProfitSortS3, etPriceSortS3,
            etWeightSortS4, etProfitSortS4, etPriceSortS4,
            etWeightSortS5, etProfitSortS5, etPriceSortS5;

    AutoCompleteTextView acSortS1, acSortS2, acSortS3, acSortS4, acSortS5;

    List<SortInfo> sortCheck;

    final String LEFT_OVER_NAME = "X";
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String whereClauseLast = "last = true";
    ArrayAdapter<String> sortAdapter;

    int chosenSort1 = -1, chosenSort2 = -1, chosenSort3 = -1, chosenSort4 = -1, chosenSort5 = -1;
    int index;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnNewSaleSortSubmit = findViewById(R.id.btnNewSaleSortSubmit);

        etWeightSortS1 = findViewById(R.id.etWeightSortS1);
        acSortS1 = findViewById(R.id.acSortS1);
        etProfitSortS1 = findViewById(R.id.etProfitSort1);
        etPriceSortS1 = findViewById(R.id.etPriceSortS1);

        etWeightSortS2 = findViewById(R.id.etWeightSortS2);
        acSortS2 = findViewById(R.id.acSortS2);
        etProfitSortS2 = findViewById(R.id.etProfitSort2);
        etPriceSortS2 = findViewById(R.id.etPriceSortS2);

        etWeightSortS3 = findViewById(R.id.etWeightSortS3);
        acSortS3 = findViewById(R.id.acSortS3);
        etProfitSortS3 = findViewById(R.id.etProfitSort3);
        etPriceSortS3 = findViewById(R.id.etPriceSortS3);

        etWeightSortS4 = findViewById(R.id.etWeightSortS4);
        acSortS4 = findViewById(R.id.acSortS4);
        etProfitSortS4 = findViewById(R.id.etProfitSort4);
        etPriceSortS4 = findViewById(R.id.etPriceSortS4);

        etWeightSortS5 = findViewById(R.id.etWeightSortS5);
        acSortS5 = findViewById(R.id.acSortS5);
        etProfitSortS5 = findViewById(R.id.etProfitSort5);
        etPriceSortS5 = findViewById(R.id.etPriceSortS5);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("בחירת מיונים מהמכירה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        sortBuilder.setWhereClause(EMAIL_CLAUSE);
        sortBuilder.setWhereClause(whereClauseLast);
        sortBuilder.setPageSize(100);
        sortBuilder.setSortBy("name");

        showProgress(true);

        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                ArrayList<String> sortNames = new ArrayList<>();
                for (Sort sort : response) {
                    if (!sort.getName().equals(LEFT_OVER_NAME)) {
                        sortNames.add(sort.getName() + " - " + sort.getSortCount());
                    }
                }

                InventoryApp.sorts = response;
                sortAdapter = new ArrayAdapter<>(NewSaleSortActivity.this, android.R.layout.select_dialog_singlechoice, sortNames);
                acSortS1.setThreshold(1);
                acSortS1.setAdapter(sortAdapter);
                acSortS2.setThreshold(1);
                acSortS2.setAdapter(sortAdapter);
                acSortS3.setThreshold(1);
                acSortS3.setAdapter(sortAdapter);
                acSortS4.setThreshold(1);
                acSortS4.setAdapter(sortAdapter);
                acSortS5.setThreshold(1);
                acSortS5.setAdapter(sortAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(NewSaleSortActivity.this, "טרם הוגדרו מיונים, עליך להגדיר מיון חדש לפני שמירת הקניה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewSaleSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        acSortS1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    String name = (InventoryApp.sorts.get(i).getName() + " - " + InventoryApp.sorts.get(i).getSortCount());
                    if (name.equals(selection)) {
                        chosenSort1 = i;
                        break;
                    }
                }
            }
        });

        acSortS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSortS1.showDropDown();
            }
        });

        acSortS2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    String name = (InventoryApp.sorts.get(i).getName() + " - " + InventoryApp.sorts.get(i).getSortCount());
                    if (name.equals(selection)) {
                        chosenSort2 = i;
                        break;
                    }
                }
            }
        });

        acSortS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSortS2.showDropDown();
            }
        });

        acSortS3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    String name = (InventoryApp.sorts.get(i).getName() + " - " + InventoryApp.sorts.get(i).getSortCount());
                    if (name.equals(selection)) {
                        chosenSort3 = i;
                        break;
                    }
                }
            }
        });

        acSortS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSortS3.showDropDown();
            }
        });

        acSortS4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    String name = (InventoryApp.sorts.get(i).getName() + " - " + InventoryApp.sorts.get(i).getSortCount());
                    if (name.equals(selection)) {
                        chosenSort4 = i;
                        break;
                    }
                }
            }
        });

        acSortS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSortS4.showDropDown();
            }
        });

        acSortS5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    String name = (InventoryApp.sorts.get(i).getName() + " - " + InventoryApp.sorts.get(i).getSortCount());
                    if (name.equals(selection)) {
                        chosenSort5 = i;
                        break;
                    }
                }
            }
        });

        acSortS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSortS5.showDropDown();
            }
        });


        btnNewSaleSortSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCheck = new ArrayList<>();

                boolean allGood = true;

                // Checking the sorts and assigning the values
                if (!sortCheck(etProfitSortS1, etWeightSortS1, chosenSort1, etProfitSortS1)) {
                    allGood = false;
                }
                if (!sortCheck(etProfitSortS2, etWeightSortS2, chosenSort2, etProfitSortS2)) {
                    allGood = false;
                }
                if (!sortCheck(etProfitSortS3, etWeightSortS3, chosenSort3, etProfitSortS3)) {
                    allGood = false;
                }
                if (!sortCheck(etProfitSortS4, etWeightSortS4, chosenSort4, etProfitSortS4)) {
                    allGood = false;
                }
                if (!sortCheck(etProfitSortS5, etWeightSortS5, chosenSort5, etProfitSortS5)) {
                    allGood = false;
                }

                double sortWeightSum = 0;
                double sortValueSum = 0;

                for (int i = 0; i < sortCheck.size(); i++) {
                    sortWeightSum += sortCheck.get(i).getWeight();
                    sortValueSum += sortCheck.get(i).getSum();
                }

                if (!allGood) {
                    Toast.makeText(NewSaleSortActivity.this, "משקל אחד המיונים גדול ממשקל המיון במלאי", Toast.LENGTH_LONG).show();

                } else if (sortWeightSum > InventoryApp.sales.get(index).getWeight()) {
                    Toast.makeText(NewSaleSortActivity.this, "סכום המשקל של המיונים גבוהה ממשקל המכירה, יש להזין משקלים מתאמים", Toast.LENGTH_LONG).show();

                } else {

                    // Assigning what's left from the weight to the left over sort
                    final double sortWeightLeftOver = InventoryApp.sales.get(index).getWeight() - sortWeightSum;
                    final double sortPriceLeftOver = (sortWeightLeftOver != 0) ? (InventoryApp.sales.get(index).getSaleSum() - sortValueSum) / sortWeightLeftOver : 0;

                    AlertDialog.Builder alert = new AlertDialog.Builder(NewSaleSortActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Saving the changes to the sale
                            InventoryApp.sales.get(index).setSorted(true);

                            showProgress(true);
                            submitRunnableTask(new Runnable() {
                                @Override
                                public void run() {
                                    Backendless.Persistence.save(InventoryApp.sales.get(index));

                                    // Checking the sorts and assigning the values if the sort was chosen
                                    Sort oldSort1 = oldSortSave(etProfitSortS1, etWeightSortS1, chosenSort1, etProfitSortS1);
                                    Sort oldSort2 = oldSortSave(etProfitSortS2, etWeightSortS2, chosenSort2, etProfitSortS2);
                                    Sort oldSort3 = oldSortSave(etProfitSortS3, etWeightSortS3, chosenSort3, etProfitSortS3);
                                    Sort oldSort4 = oldSortSave(etProfitSortS4, etWeightSortS4, chosenSort4, etProfitSortS4);
                                    Sort oldSort5 = oldSortSave(etProfitSortS5, etWeightSortS5, chosenSort5, etProfitSortS5);

                                    if (oldSort1 != null) {
                                        oldSort1.save();

                                        Sort newSort = newSortSave(etPriceSortS1, etWeightSortS1, chosenSort1);
                                        newSort.save();
                                        String newSortObjectId = newSort.getObjectId();

                                        Sort newSaleSort = newSaleSortSave(etPriceSortS1, etWeightSortS1, chosenSort1, etProfitSortS1);
                                        newSaleSort.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> parentObject = new HashMap<>();
                                        parentObject.put("objectId", newSortObjectId);

                                        HashMap<String, Object> newSaleSortObject = new HashMap<>();
                                        HashMap<String, Object> oldSortObject = new HashMap<>();
                                        newSaleSortObject.put("objectId", newSaleSort.getObjectId());
                                        oldSortObject.put("objectId", oldSort1.getObjectId());

                                        ArrayList<Map> children = new ArrayList<>();
                                        children.add(newSaleSortObject);
                                        children.add(oldSortObject);

                                        Backendless.Data.of("Sort").addRelation(parentObject, "Sorts", children);
                                    }

                                    if (oldSort2 != null) {
                                        oldSort2.save();
                                    }

                                    if (oldSort3 != null) {
                                        oldSort3.save();
                                    }

                                    if (oldSort4 != null) {
                                        oldSort4.save();
                                    }

                                    if (oldSort5 != null) {
                                        oldSort5.save();
                                    }

                                    // Saving left over old sort
                                    Sort oldLeftOverSort = findLeftOver();
                                    oldLeftOverSort.setLast(false);
                                    oldLeftOverSort.save();

                                    // Saving new left over new sort
                                    Sort newLeftOverSort = new Sort();
                                    newLeftOverSort.setName(oldLeftOverSort.getName());
                                    newLeftOverSort.setSortCount(oldLeftOverSort.getSortCount()+1);

                                    newLeftOverSort.setClarity(oldLeftOverSort.getClarity());
                                    newLeftOverSort.setColor(oldLeftOverSort.getColor());
                                    newLeftOverSort.setShape(oldLeftOverSort.getShape());
                                    newLeftOverSort.setSize(oldLeftOverSort.getSize());

                                    newLeftOverSort.setSum(oldLeftOverSort.getSum() - (sortPriceLeftOver*sortWeightLeftOver));
                                    newLeftOverSort.setWeight(oldLeftOverSort.getWeight() - sortWeightLeftOver);
                                    newLeftOverSort.setPrice(newLeftOverSort.getSum()/newLeftOverSort.getWeight());
                                    newLeftOverSort.setUserEmail(InventoryApp.user.getEmail());
                                    newLeftOverSort.setLast(true);
                                    newLeftOverSort.save();
                                    String newLeftOverSortObjectId = newLeftOverSort.getObjectId();

                                    // Saving left over sale sort
                                    Sort leftOverSaleSort = new Sort();
                                    leftOverSaleSort.setName(oldLeftOverSort.getName() + "S");
                                    leftOverSaleSort.setSortCount(oldLeftOverSort.getSortCount()+1);
                                    leftOverSaleSort.setSaleId(InventoryApp.sales.get(index).getObjectId());
                                    leftOverSaleSort.setSaleName(InventoryApp.sales.get(index).getClientName());

                                    leftOverSaleSort.setClarity(oldLeftOverSort.getClarity());
                                    leftOverSaleSort.setColor(oldLeftOverSort.getColor());
                                    leftOverSaleSort.setShape(oldLeftOverSort.getShape());
                                    leftOverSaleSort.setSize(oldLeftOverSort.getSize());

                                    leftOverSaleSort.setSum(sortPriceLeftOver*sortWeightLeftOver);
                                    leftOverSaleSort.setWeight(sortWeightLeftOver);
                                    leftOverSaleSort.setPrice(leftOverSaleSort.getSum()/leftOverSaleSort.getWeight());
                                    leftOverSaleSort.setSoldPrice(sortPriceLeftOver);
                                    leftOverSaleSort.setLast(false);
                                    leftOverSaleSort.setSale(true);
                                    leftOverSaleSort.setUserEmail(InventoryApp.user.getEmail());
                                    leftOverSaleSort.save();

                                    // Setting the relation to the Sort
                                    HashMap<String, Object> parentObject = new HashMap<>();
                                    parentObject.put("objectId", newLeftOverSortObjectId);

                                    HashMap<String, Object> newSaleSortObject = new HashMap<>();
                                    HashMap<String, Object> oldLeftOverObject = new HashMap<>();
                                    newSaleSortObject.put("objectId", leftOverSaleSort.getObjectId());
                                    oldLeftOverObject.put("objectId", oldLeftOverSort.getObjectId());

                                    ArrayList<Map> children = new ArrayList<>();
                                    children.add(newSaleSortObject);
                                    children.add(oldLeftOverObject);

                                    Backendless.Data.of("Sort").addRelation(parentObject, "Sorts", children);
                              }
                          });
                            showProgress(false);
                            Toast.makeText(NewSaleSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finishActivity(1);
                            NewSaleSortActivity.this.finish();
                      }
                  });
                  alert.show();
              }
            }
        });
    }

    public Sort oldSortSave(EditText sortPriceText, EditText sortWeightText, int chosenSort, EditText etProfitSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1
        || etProfitSort.getText().toString().isEmpty())) {

            Sort sort = InventoryApp.sorts.get(chosenSort);
            sort.setLast(false);

            return sort;
        } else {
            return null;
        }
    }

    public Sort newSortSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            Sort oldSort = InventoryApp.sorts.get(chosenSort);
            Sort newSort = new Sort();
            newSort.setName(oldSort.getName());
            newSort.setSortCount(oldSort.getSortCount()+1);

            newSort.setClarity(oldSort.getClarity());
            newSort.setColor(oldSort.getColor());
            newSort.setShape(oldSort.getShape());
            newSort.setSize(oldSort.getSize());

            newSort.setSum(oldSort.getSum() - (sortPrice*sortWeight));
            newSort.setWeight(oldSort.getWeight() - sortWeight);
            newSort.setPrice(newSort.getSum()/newSort.getWeight());
            newSort.setUserEmail(InventoryApp.user.getEmail());
            newSort.setLast(true);

            return newSort;
        } else {
            return null;
        }
    }

    public Sort newSaleSortSave(EditText sortProfitText, EditText sortWeightText, int chosenSort, EditText etProfitSort) {
        if (!(sortProfitText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortProfitText.getText().toString());
            double soldWeight = Double.valueOf(sortWeightText.getText().toString());
            double soldPrice =  Double.valueOf(etProfitSort.getText().toString());

            Sort oldSort = InventoryApp.sorts.get(chosenSort);
            Sort newSaleSort = new Sort();
            newSaleSort.setName(oldSort.getName() + "S");
            newSaleSort.setSortCount(oldSort.getSortCount()+1);
            newSaleSort.setSaleId(InventoryApp.sales.get(index).getObjectId());
            newSaleSort.setSaleName(InventoryApp.sales.get(index).getClientName());

            newSaleSort.setClarity(oldSort.getClarity());
            newSaleSort.setColor(oldSort.getColor());
            newSaleSort.setShape(oldSort.getShape());
            newSaleSort.setSize(oldSort.getSize());

            newSaleSort.setSum(sortPrice*soldWeight);
            newSaleSort.setWeight(soldWeight);
            newSaleSort.setPrice(sortPrice);
            newSaleSort.setSoldPrice(soldPrice);
            newSaleSort.setLast(false);
            newSaleSort.setSale(true);
            newSaleSort.setUserEmail(InventoryApp.user.getEmail());

            return newSaleSort;
        } else {
            return null;
        }
    }

    public boolean sortCheck(EditText sortPriceText, EditText sortWeightText, final int chosenSort,  EditText sortProfitText) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1 || sortProfitText.getText().toString().isEmpty())) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());
            double sortProfit = Double.valueOf(sortProfitText.getText().toString());

            if (sortProfit < 0) {
                return false;

            } else if (sortWeight > InventoryApp.sorts.get(chosenSort).getWeight()) {
                return false;

            } else {
                SortInfo sortInfo = new SortInfo();
                sortInfo.setWeight(sortWeight);
                sortInfo.setSum(sortPrice*sortWeight);
                sortCheck.add(sortInfo);
                return true;
            }
        }
        return true;
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

