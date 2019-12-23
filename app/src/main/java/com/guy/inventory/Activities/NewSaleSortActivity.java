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

    EditText etWeightSortS1, etPriceSortS1, etWeightSortS2, etPriceSortS2, etWeightSortS3, etPriceSortS3, etWeightSortS4, etPriceSortS4, etWeightSortS5, etPriceSortS5;
    AutoCompleteTextView acSortS1, acSortS2, acSortS3, acSortS4, acSortS5;

    List<SortInfo> sortCheck;

    final String LEFT_OVER_ID = "555D00E6-F4DC-4642-FF97-764A70C8AB00";
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";
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
        etPriceSortS1 = findViewById(R.id.etPriceSortS1);
        acSortS1 = findViewById(R.id.acSortS1);

        etWeightSortS2 = findViewById(R.id.etWeightSortS2);
        etPriceSortS2 = findViewById(R.id.etPriceSortS2);
        acSortS2 = findViewById(R.id.acSortS2);

        etWeightSortS3 = findViewById(R.id.etWeightSortS3);
        etPriceSortS3 = findViewById(R.id.etPriceSortS3);
        acSortS3 = findViewById(R.id.acSortS3);

        etWeightSortS4 = findViewById(R.id.etWeightSortS4);
        etPriceSortS4 = findViewById(R.id.etPriceSortS4);
        acSortS4 = findViewById(R.id.acSortS4);

        etWeightSortS5 = findViewById(R.id.etWeightSortS5);
        etPriceSortS5 = findViewById(R.id.etPriceSortS5);
        acSortS5 = findViewById(R.id.acSortS5);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("בחירת מיונים מהמכירה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        sortBuilder.setWhereClause(EMAIL_CLAUSE);
        sortBuilder.setPageSize(100);
        sortBuilder.setSortBy("name");

        showProgress(true);

        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                ArrayList<String> sortNames = new ArrayList<>();
                for (Sort sort : response) {
                    if (!sort.getObjectId().equals(LEFT_OVER_ID)) {
                        sortNames.add(sort.getName());
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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

                // Checking the sorts and assigning the values;
                sortCheck(etPriceSortS1, etWeightSortS1, chosenSort1);
                sortCheck(etPriceSortS2, etWeightSortS2, chosenSort2);
                sortCheck(etPriceSortS3, etWeightSortS3, chosenSort3);
                sortCheck(etPriceSortS4, etWeightSortS4, chosenSort4);
                sortCheck(etPriceSortS5, etWeightSortS5, chosenSort5);

                double sortWeightSum = 0;
                double sortValueSum = 0;

                for (int i = 0; i < sortCheck.size(); i++) {
                    sortWeightSum += sortCheck.get(i).getWeight();
                    sortValueSum += sortCheck.get(i).getSum();
                }

                if (sortValueSum > InventoryApp.sales.get(index).getSaleSum()) {
                    Toast.makeText(NewSaleSortActivity.this, "סכום שווי המיונים גבוהה משווי הסחורה, יש להזין מחירים מתאימים", Toast.LENGTH_LONG).show();

                } else if (sortWeightSum > InventoryApp.sales.get(index).getWeight()) {
                    Toast.makeText(NewSaleSortActivity.this, "סכום המשקל של המיונים גבוהה מהמשקל הגמור, יש להזין משקלים מתאמים", Toast.LENGTH_LONG).show();

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
                                    Sort oldSort1 = oldSortSave(etPriceSortS1, etWeightSortS1, chosenSort1);
                                    Sort oldSort2 = oldSortSave(etPriceSortS2, etWeightSortS2, chosenSort2);
                                    Sort oldSort3 = oldSortSave(etPriceSortS3, etWeightSortS3, chosenSort3);
                                    Sort oldSort4 = oldSortSave(etPriceSortS4, etWeightSortS4, chosenSort4);
                                    Sort oldSort5 = oldSortSave(etPriceSortS5, etWeightSortS5, chosenSort5);

                                    HashMap<String, Object> saleObjectId = new HashMap<>();
                                    saleObjectId.put("objectId", InventoryApp.sales.get(index).getObjectId());

                                    ArrayList<Map> sortInfoObjects = new ArrayList<>();

                                    if (oldSort1 != null) {

                                        oldSort1.save();

                                        Sort newSort = newSortSave(etPriceSortS1, etWeightSortS1, chosenSort1);
                                        newSort.save();
                                        String newSortObjectId = newSort.getObjectId();

                                        Sort newSaleSort = newSaleSortSave(etPriceSortS1, etWeightSortS1, chosenSort1);
                                        newSaleSort.save();


                                        // Setting the relation to the Sort
                                        HashMap<String, Object> parentObject = new HashMap<>();
                                        parentObject.put("objectId", oldSort1.getObjectId());

                                        HashMap<String, Object> newSaleSortObject = new HashMap<>();
                                        HashMap<String, Object> newSortObject = new HashMap<>();
                                        newSaleSortObject.put("objectId", newSaleSort.getObjectId());
                                        newSortObject.put("objectId", newSort.getObjectId());

                                        ArrayList<Map> children = new ArrayList<>();
                                        children.add(newSaleSortObject);
                                        children.add(newSortObject);

                                        HashMap<String, Object> sortInfo1ObjectId = new HashMap<>();
                                        sortInfo1ObjectId.put("objectId", newSortObjectId);
                                        sortInfoObjects.add(sortInfo1ObjectId);

                                        Backendless.Data.of("Sort").addRelation(parentObject, "Sorts", children, new AsyncCallback<Integer>() {
                                            @Override
                                            public void handleResponse(Integer response) {
                                                int x = response;
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {

                                            }
                                        });
                                    }

                                    if (oldSort2 != null) {

                                        oldSort2.save();
                                        String newSortObjectId = newSortSave(etPriceSortS2, etWeightSortS2, chosenSort2).save().getObjectId();
                                        String newSaleSortObjectId = newSaleSortSave(etPriceSortS2, etWeightSortS2, chosenSort2).save().getObjectId();

         //                               saleInfoSave(etPriceSortS2, etWeightSortS2, chosenSort2, newSaleSortObjectId).save();
             //                           sortInfoSave(etPriceSortS2, etWeightSortS2, chosenSort2, newSortObjectId).save();

                                        HashMap<String, Object> sortInfo1ObjectId = new HashMap<>();
                                        sortInfo1ObjectId.put("objectId", newSortObjectId);
                                        sortInfoObjects.add(sortInfo1ObjectId);
                                    }

                                    if (oldSort3 != null) {

                                        oldSort3.save();
                                        String newSortObjectId = newSortSave(etPriceSortS3, etWeightSortS3, chosenSort3).save().getObjectId();
                                        String newSaleSortObjectId = newSaleSortSave(etPriceSortS3, etWeightSortS3, chosenSort3).save().getObjectId();

          //                              saleInfoSave(etPriceSortS3, etWeightSortS3, chosenSort3, newSaleSortObjectId).save();
           //                             sortInfoSave(etPriceSortS3, etWeightSortS3, chosenSort3, newSortObjectId).save();

                                        HashMap<String, Object> sortInfo1ObjectId = new HashMap<>();
                                        sortInfo1ObjectId.put("objectId", newSortObjectId);
                                        sortInfoObjects.add(sortInfo1ObjectId);
                                    }

                                    if (oldSort4 != null) {

                                        oldSort4.save();
                                        String newSortObjectId = newSortSave(etPriceSortS4, etWeightSortS4, chosenSort4).save().getObjectId();
                                        String newSaleSortObjectId = newSaleSortSave(etPriceSortS4, etWeightSortS4, chosenSort4).save().getObjectId();

        //                                saleInfoSave(etPriceSortS4, etWeightSortS4, chosenSort4, newSaleSortObjectId).save();
      //                                  sortInfoSave(etPriceSortS4, etWeightSortS4, chosenSort4, newSortObjectId).save();

                                        HashMap<String, Object> sortInfo1ObjectId = new HashMap<>();
                                        sortInfo1ObjectId.put("objectId", newSortObjectId);
                                        sortInfoObjects.add(sortInfo1ObjectId);
                                    }

                                    if (oldSort5 != null) {

                                        oldSort5.save();
                                        String newSortObjectId = newSortSave(etPriceSortS5, etWeightSortS5, chosenSort5).save().getObjectId();
                                        String newSaleSortObjectId = newSaleSortSave(etPriceSortS5, etWeightSortS5, chosenSort5).save().getObjectId();

    //                                    saleInfoSave(etPriceSortS5, etWeightSortS5, chosenSort5, newSaleSortObjectId).save();
    //                                    sortInfoSave(etPriceSortS5, etWeightSortS5, chosenSort5, newSortObjectId).save();

                                        HashMap<String, Object> sortInfo1ObjectId = new HashMap<>();
                                        sortInfo1ObjectId.put("objectId", newSortObjectId);
                                        sortInfoObjects.add(sortInfo1ObjectId);
                                    }

                                    // Saving left over old sort
                                    Sort oldLeftOverSort = findLeftOver();
                                    oldLeftOverSort.setLast(false);
                                    oldLeftOverSort.save();

                                    // Saving new left over new sort
                                    Sort newLeftOverSort = new Sort();
                                    newLeftOverSort.setName(oldLeftOverSort.getName());
                                    newLeftOverSort.setSortCount(oldLeftOverSort.getSortCount() + 1);
                                    newLeftOverSort.setClarity(oldLeftOverSort.getClarity());
                                    newLeftOverSort.setColor(oldLeftOverSort.getColor());
                                    newLeftOverSort.setShape(oldLeftOverSort.getShape());
                                    newLeftOverSort.setSize(oldLeftOverSort.getSize());
                                    newLeftOverSort.setSum(oldLeftOverSort.getSum() - sortWeightLeftOver * sortPriceLeftOver);
                                    newLeftOverSort.setWeight(oldLeftOverSort.getWeight() - sortWeightLeftOver);
                                    newLeftOverSort.setPrice(newLeftOverSort.getSum() / newLeftOverSort.getWeight());
                                    newLeftOverSort.setLast(true);
                                    newLeftOverSort.save();

                                    // Saving left over sale sort
                                    Sort newSaleLeftOverSort = new Sort();
                                    newSaleLeftOverSort.setName(oldLeftOverSort.getName() + "S");
                                    newSaleLeftOverSort.setSortCount(oldLeftOverSort.getSortCount() + 1);
                                    newSaleLeftOverSort.setSaleId(InventoryApp.sales.get(index).getObjectId());
                                    newSaleLeftOverSort.setClarity(oldLeftOverSort.getClarity());
                                    newSaleLeftOverSort.setColor(oldLeftOverSort.getColor());
                                    newSaleLeftOverSort.setShape(oldLeftOverSort.getShape());
                                    newSaleLeftOverSort.setSize(oldLeftOverSort.getSize());
                                    newSaleLeftOverSort.setSum(sortPriceLeftOver * sortWeightLeftOver);
                                    newSaleLeftOverSort.setWeight(sortWeightLeftOver);
                                    newSaleLeftOverSort.setPrice(newSaleLeftOverSort.getSum() / newSaleLeftOverSort.getWeight());
                                    newSaleLeftOverSort.setLast(false);
                                    newSaleLeftOverSort.setSale(true);
                                    newSaleLeftOverSort.save();

    //                                // Saving left over sortInfo
    //                                SortInfo leftOverSortInfo = new SortInfo();
    //                                leftOverSortInfo.setName(oldLeftOverSort.getName());
    //                                leftOverSortInfo.setToId(newSaleLeftOverSort.getObjectId());
    //                                leftOverSortInfo.setFromId(oldLeftOverSort.getObjectId());
    //                                leftOverSortInfo.setFromSale(true);
    //                                leftOverSortInfo.setSortCount(oldLeftOverSort.getSortCount());
    //                                leftOverSortInfo.setFromBuy(false);
    //                                leftOverSortInfo.setPrice(sortPriceLeftOver);
    //                                leftOverSortInfo.setWeight(sortWeightLeftOver);
    //                                leftOverSortInfo.setSum(sortPriceLeftOver*sortWeightLeftOver);
    //                                leftOverSortInfo.save();
    //                                HashMap<String, Object> leftOverSortInfoObjectId = new HashMap<>();
    //                                leftOverSortInfoObjectId.put("objectId", leftOverSortInfo.getObjectId());
    //                                sortInfoObjects.add(leftOverSortInfoObjectId);
//
    //                                // Saving left over sale sortInfo
    //                                SortInfo saleLeftOverSortInfo = new SortInfo();
    //                                saleLeftOverSortInfo.setName(oldLeftOverSort.getName());
    //                                saleLeftOverSortInfo.setToName(InventoryApp.sales.get(index).getClientName());
    //                                saleLeftOverSortInfo.setToId(newSaleLeftOverSort.getObjectId());
    //                                saleLeftOverSortInfo.setFromId(oldLeftOverSort.getObjectId());
    //                                saleLeftOverSortInfo.setFromSale(true);
    //                                saleLeftOverSortInfo.setSortCount(oldLeftOverSort.getSortCount() + 1);
    //                                saleLeftOverSortInfo.setFromBuy(false);
    //                                saleLeftOverSortInfo.setPrice(sortPriceLeftOver);
    //                                saleLeftOverSortInfo.setWeight(sortWeightLeftOver);
    //                                saleLeftOverSortInfo.setSum(sortPriceLeftOver*sortWeightLeftOver);
    //                                saleLeftOverSortInfo.setUserEmail(InventoryApp.user.getEmail());
    //                                saleLeftOverSortInfo.save();
    //                                HashMap<String, Object> saleLeftOverSortInfoObjectId = new HashMap<>();
    //                                leftOverSortInfoObjectId.put("objectId", saleLeftOverSortInfo.getObjectId());
    //                                sortInfoObjects.add(saleLeftOverSortInfoObjectId);
//
    //                                Backendless.Data.of("Sale").addRelation(saleObjectId, "SortInfo", sortInfoObjects);
                                }
//
                            });
    //                        showProgress(false);
    //                        Toast.makeText(NewSaleSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
    //                        setResult(RESULT_OK);
    //                        finishActivity(1);
    //                        NewSaleSortActivity.this.finish();
                        }
                    });
                    alert.show();
                }
            }
        });
    }
//
    //public SortInfo saleInfoSave(EditText sortPriceText, EditText sortWeightText, int chosenSort, String toId) {
    //    if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {
//
    //        double sortPrice = Double.valueOf(sortPriceText.getText().toString());
    //        double sortWeight = Double.valueOf(sortWeightText.getText().toString());
//
    //        SortInfo sortInfo = new SortInfo();
    //        sortInfo.setToName(InventoryApp.sales.get(index).getClientName());
    //        sortInfo.setName(InventoryApp.sorts.get(chosenSort).getName());
    //        sortInfo.setToId(toId);
    //        sortInfo.setFromId(InventoryApp.sorts.get(chosenSort).getObjectId());
    //        sortInfo.setFromSale(true);
    //        sortInfo.setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount() + 1);
    //        sortInfo.setFromBuy(false);
    //        sortInfo.setPrice(sortPrice);
    //        sortInfo.setWeight(sortWeight);
    //        sortInfo.setSum(sortPrice*sortWeight);
    //        sortInfo.setUserEmail(InventoryApp.user.getEmail());
//
    //        return sortInfo;
    //    } else {
    //        return null;
    //    }
    //}
//
    //public SortInfo sortInfoSave(EditText sortPriceText, EditText sortWeightText, int chosenSort, String toId) {
    //    if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {
//
    //        double sortPrice = Double.valueOf(sortPriceText.getText().toString());
    //        double sortWeight = Double.valueOf(sortWeightText.getText().toString());
//
    //        SortInfo sortInfo = new SortInfo();
    //        sortInfo.setName(InventoryApp.sorts.get(chosenSort).getName());
    //        sortInfo.setToName(InventoryApp.sorts.get(chosenSort).getName() + "S");
    //        sortInfo.setToId(toId);
    //        sortInfo.setFromId(InventoryApp.sorts.get(chosenSort).getObjectId());
    //        sortInfo.setFromSale(false);
    //        sortInfo.setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount() + 1);
    //        sortInfo.setFromBuy(false);
    //        sortInfo.setPrice(sortPrice);
    //        sortInfo.setWeight(sortWeight);
    //        sortInfo.setSum(sortPrice*sortWeight);
    //        sortInfo.setUserEmail(InventoryApp.user.getEmail());
//
    //        return sortInfo;
    //    } else {
    //        return null;
    //    }
    //}

    public Sort oldSortSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

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

    public Sort newSaleSortSave(EditText sortPriceText, EditText sortWeightText, int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            Sort oldSort = InventoryApp.sorts.get(chosenSort);
            Sort newSaleSort = new Sort();
            newSaleSort.setName(oldSort.getName() + "S");
            newSaleSort.setSortCount(oldSort.getSortCount()+1);
            newSaleSort.setSaleId(InventoryApp.sales.get(index).getObjectId());

            newSaleSort.setClarity(oldSort.getClarity());
            newSaleSort.setColor(oldSort.getColor());
            newSaleSort.setShape(oldSort.getShape());
            newSaleSort.setSize(oldSort.getSize());

            newSaleSort.setSum(sortPrice*sortWeight);
            newSaleSort.setWeight(sortWeight);
            newSaleSort.setPrice(newSaleSort.getSum()/newSaleSort.getWeight());
            newSaleSort.setLast(false);
            newSaleSort.setSale(true);
            newSaleSort.setUserEmail(InventoryApp.user.getEmail());

            return newSaleSort;
        } else {
            return null;
        }
    }

    public void sortCheck(EditText sortPriceText, EditText sortWeightText, final int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());

            SortInfo sortInfo = new SortInfo();
            sortInfo.setPrice(sortPrice);
            sortInfo.setWeight(sortWeight);
            sortInfo.setSum(sortPrice * sortWeight);
            sortCheck.add(sortInfo);

        }
    }

    public Sort findLeftOver() {
        for (Sort sort : InventoryApp.sorts) {
            if (sort.getObjectId().equals(LEFT_OVER_ID)) {
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

