package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    EditText etWeightSortS1, etProfitSortS1, etPriceSortS1, etSortName1,
             etWeightSortS2, etProfitSortS2, etPriceSortS2, etSortName2,
             etWeightSortS3, etProfitSortS3, etPriceSortS3, etSortName3,
             etWeightSortS4, etProfitSortS4, etPriceSortS4, etSortName4,
             etWeightSortS5, etProfitSortS5, etPriceSortS5, etSortName5;

    EditText etWeightSortS6, etProfitSortS6, etPriceSortS6, etSortName6,
             etWeightSortS7, etProfitSortS7, etPriceSortS7, etSortName7,
             etWeightSortS8, etProfitSortS8, etPriceSortS8, etSortName8,
             etWeightSortS9, etProfitSortS9, etPriceSortS9, etSortName9,
             etWeightSortS10, etProfitSortS10, etPriceSortS10, etSortName10;

    EditText etWeightSortS11, etProfitSortS11, etPriceSortS11, etSortName11,
             etWeightSortS12, etProfitSortS12, etPriceSortS12, etSortName12,
             etWeightSortS13, etProfitSortS13, etPriceSortS13, etSortName13,
             etWeightSortS14, etProfitSortS14, etPriceSortS14, etSortName14,
             etWeightSortS15, etProfitSortS15, etPriceSortS15, etSortName15;

    EditText etWeightSortS16, etProfitSortS16, etPriceSortS16, etSortName16,
             etWeightSortS17, etProfitSortS17, etPriceSortS17, etSortName17,
             etWeightSortS18, etProfitSortS18, etPriceSortS18, etSortName18,
             etWeightSortS19, etProfitSortS19, etPriceSortS19, etSortName19,
             etWeightSortS20, etProfitSortS20, etPriceSortS20, etSortName20;

    LinearLayout vlSort1, vlSort2, vlSort3, vlSort4, vlSort5, vlSort6 ,vlSort7, vlSort8, vlSort9, vlSort10,
            vlSort11, vlSort12, vlSort13, vlSort14, vlSort15, vlSort16 ,vlSort17, vlSort18, vlSort19, vlSort20;

    TextView tvSaleInfo;
    ImageView ivPlus;

    AutoCompleteTextView acMainSort;

    List<SortInfo> sortCheck;
    int sortCount = 1;

    final double VALUE_MARGIN = 10.0;
    final double CARAT_MARGIN = 0.01;
    final String LEFT_OVER_NAME = "עודפים";
    private String newMainSortObjectId;
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
    ArrayAdapter<String> sortAdapter;

    int chosenMainSort = -1;
    int index;

    @SuppressLint("SetTextI18n")
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
        acMainSort = findViewById(R.id.acMainSort);
        tvSaleInfo = findViewById(R.id.tvSaleInfo);
        ivPlus = findViewById(R.id.ivPlus);

        vlSort1 = findViewById(R.id.vlSort1);
        vlSort2 = findViewById(R.id.vlSort2);
        vlSort3 = findViewById(R.id.vlSort3);
        vlSort4 = findViewById(R.id.vlSort4);
        vlSort5 = findViewById(R.id.vlSort5);
        vlSort6 = findViewById(R.id.vlSort6);
        vlSort7 = findViewById(R.id.vlSort7);
        vlSort8 = findViewById(R.id.vlSort8);
        vlSort9 = findViewById(R.id.vlSort9);
        vlSort10 = findViewById(R.id.vlSort10);
        vlSort11 = findViewById(R.id.vlSort11);
        vlSort12 = findViewById(R.id.vlSort12);
        vlSort13 = findViewById(R.id.vlSort13);
        vlSort14 = findViewById(R.id.vlSort14);
        vlSort15 = findViewById(R.id.vlSort15);
        vlSort16 = findViewById(R.id.vlSort16);
        vlSort17 = findViewById(R.id.vlSort17);
        vlSort18 = findViewById(R.id.vlSort18);
        vlSort19 = findViewById(R.id.vlSort19);
        vlSort20 = findViewById(R.id.vlSort20);

        etWeightSortS1  = findViewById(R.id.etWeightSortS1);
        etWeightSortS2  = findViewById(R.id.etWeightSortS2);
        etWeightSortS3  = findViewById(R.id.etWeightSortS3);
        etWeightSortS4  = findViewById(R.id.etWeightSortS4);
        etWeightSortS5  = findViewById(R.id.etWeightSortS5);
        etWeightSortS6  = findViewById(R.id.etWeightSortS6);
        etWeightSortS7  = findViewById(R.id.etWeightSortS7);
        etWeightSortS8  = findViewById(R.id.etWeightSortS8);
        etWeightSortS9  = findViewById(R.id.etWeightSortS9);
        etWeightSortS10 = findViewById(R.id.etWeightSortS10);
        etWeightSortS11 = findViewById(R.id.etWeightSortS11);
        etWeightSortS12 = findViewById(R.id.etWeightSortS12);
        etWeightSortS13 = findViewById(R.id.etWeightSortS13);
        etWeightSortS14 = findViewById(R.id.etWeightSortS14);
        etWeightSortS15 = findViewById(R.id.etWeightSortS15);
        etWeightSortS16 = findViewById(R.id.etWeightSortS16);
        etWeightSortS17 = findViewById(R.id.etWeightSortS17);
        etWeightSortS18 = findViewById(R.id.etWeightSortS18);
        etWeightSortS19 = findViewById(R.id.etWeightSortS19);
        etWeightSortS20 = findViewById(R.id.etWeightSortS20);

        etSortName1  = findViewById(R.id.etSortName1);
        etSortName2  = findViewById(R.id.etSortName2);
        etSortName3  = findViewById(R.id.etSortName3);
        etSortName4  = findViewById(R.id.etSortName4);
        etSortName5  = findViewById(R.id.etSortName5);
        etSortName6  = findViewById(R.id.etSortName6);
        etSortName7  = findViewById(R.id.etSortName7);
        etSortName8  = findViewById(R.id.etSortName8);
        etSortName9  = findViewById(R.id.etSortName9);
        etSortName10 = findViewById(R.id.etSortName10);
        etSortName11 = findViewById(R.id.etSortName11);
        etSortName12 = findViewById(R.id.etSortName12);
        etSortName13 = findViewById(R.id.etSortName13);
        etSortName14 = findViewById(R.id.etSortName14);
        etSortName15 = findViewById(R.id.etSortName15);
        etSortName16 = findViewById(R.id.etSortName16);
        etSortName17 = findViewById(R.id.etSortName17);
        etSortName18 = findViewById(R.id.etSortName18);
        etSortName19 = findViewById(R.id.etSortName19);
        etSortName20 = findViewById(R.id.etSortName20);

        etProfitSortS1  = findViewById(R.id.etProfitSort1);
        etProfitSortS2  = findViewById(R.id.etProfitSort2);
        etProfitSortS3  = findViewById(R.id.etProfitSort3);
        etProfitSortS4  = findViewById(R.id.etProfitSort4);
        etProfitSortS5  = findViewById(R.id.etProfitSort5);
        etProfitSortS6  = findViewById(R.id.etProfitSort6);
        etProfitSortS7  = findViewById(R.id.etProfitSort7);
        etProfitSortS8  = findViewById(R.id.etProfitSort8);
        etProfitSortS9  = findViewById(R.id.etProfitSort9);
        etProfitSortS10 = findViewById(R.id.etProfitSort10);
        etProfitSortS11 = findViewById(R.id.etProfitSort11);
        etProfitSortS12 = findViewById(R.id.etProfitSort12);
        etProfitSortS13 = findViewById(R.id.etProfitSort13);
        etProfitSortS14 = findViewById(R.id.etProfitSort14);
        etProfitSortS15 = findViewById(R.id.etProfitSort15);
        etProfitSortS16 = findViewById(R.id.etProfitSort16);
        etProfitSortS17 = findViewById(R.id.etProfitSort17);
        etProfitSortS18 = findViewById(R.id.etProfitSort18);
        etProfitSortS19 = findViewById(R.id.etProfitSort19);
        etProfitSortS20 = findViewById(R.id.etProfitSort20);

        etPriceSortS1  = findViewById(R.id.etPriceSortS1);
        etPriceSortS2  = findViewById(R.id.etPriceSortS2);
        etPriceSortS3  = findViewById(R.id.etPriceSortS3);
        etPriceSortS4  = findViewById(R.id.etPriceSortS4);
        etPriceSortS5  = findViewById(R.id.etPriceSortS5);
        etPriceSortS6  = findViewById(R.id.etPriceSortS6);
        etPriceSortS7  = findViewById(R.id.etPriceSortS7);
        etPriceSortS8  = findViewById(R.id.etPriceSortS8);
        etPriceSortS9  = findViewById(R.id.etPriceSortS9);
        etPriceSortS10 = findViewById(R.id.etPriceSortS10);
        etPriceSortS11 = findViewById(R.id.etPriceSortS11);
        etPriceSortS12 = findViewById(R.id.etPriceSortS12);
        etPriceSortS13 = findViewById(R.id.etPriceSortS13);
        etPriceSortS14 = findViewById(R.id.etPriceSortS14);
        etPriceSortS15 = findViewById(R.id.etPriceSortS15);
        etPriceSortS16 = findViewById(R.id.etPriceSortS16);
        etPriceSortS17 = findViewById(R.id.etPriceSortS17);
        etPriceSortS18 = findViewById(R.id.etPriceSortS18);
        etPriceSortS19 = findViewById(R.id.etPriceSortS19);
        etPriceSortS20 = findViewById(R.id.etPriceSortS20);

        // Setting all the sorts visibility
        vlSort2.setVisibility(View.GONE);
        vlSort3.setVisibility(View.GONE);
        vlSort4.setVisibility(View.GONE);
        vlSort5.setVisibility(View.GONE);
        vlSort6.setVisibility(View.GONE);
        vlSort7.setVisibility(View.GONE);
        vlSort8.setVisibility(View.GONE);
        vlSort9.setVisibility(View.GONE);
        vlSort10.setVisibility(View.GONE);
        vlSort11.setVisibility(View.GONE);
        vlSort12.setVisibility(View.GONE);
        vlSort13.setVisibility(View.GONE);
        vlSort14.setVisibility(View.GONE);
        vlSort15.setVisibility(View.GONE);
        vlSort16.setVisibility(View.GONE);
        vlSort17.setVisibility(View.GONE);
        vlSort18.setVisibility(View.GONE);
        vlSort19.setVisibility(View.GONE);
        vlSort20.setVisibility(View.GONE);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("בחירת מיונים מהמכירה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        tvSaleInfo.setText(InventoryApp.sales.get(index).getClientName() + " = " + numberFormat.format(InventoryApp.sales.get(index).getSaleSum()/InventoryApp.sales.get(index).getWeight()) +"P : " +
                numberFormat.format(InventoryApp.sales.get(index).getWeight()) + "C");

        sortBuilder.setWhereClause(EMAIL_CLAUSE);
        sortBuilder.setHavingClause("last = true");
        sortBuilder.setSortBy("name");
        sortBuilder.setPageSize(100);

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
                sortAdapter = new ArrayAdapter<>(NewSaleSortActivity.this, android.R.layout.select_dialog_singlechoice, sortNames);
                acMainSort.setThreshold(1);
                acMainSort.setAdapter(sortAdapter);
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

        acMainSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.sorts.size(); i++) {
                    Sort sort = InventoryApp.sorts.get(i);
                    String name = sort.getName() + "-" + sort.getSortCount() + " = " + numberFormat.format(sort.getPrice()) + "P : " + numberFormat.format(sort.getWeight()) + "C";
                    if (name.equals(selection)) {
                        chosenMainSort = i;
                        break;
                    }
                }
            }
        });

        acMainSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acMainSort.showDropDown();
            }
        });

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCount += 1;

                switch (sortCount) {

                    case 2:
                        vlSort2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        vlSort3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        vlSort4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        vlSort5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        vlSort6.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        vlSort7.setVisibility(View.VISIBLE);
                        break;
                    case 8:
                        vlSort8.setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        vlSort9.setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        vlSort10.setVisibility(View.VISIBLE);
                        break;
                    case 11:
                        vlSort11.setVisibility(View.VISIBLE);
                        break;
                    case 12:
                        vlSort12.setVisibility(View.VISIBLE);
                        break;
                    case 13:
                        vlSort13.setVisibility(View.VISIBLE);
                        break;
                    case 14:
                        vlSort14.setVisibility(View.VISIBLE);
                        break;
                    case 15:
                        vlSort15.setVisibility(View.VISIBLE);
                        break;
                    case 16:
                        vlSort16.setVisibility(View.VISIBLE);
                        break;
                    case 17:
                        vlSort17.setVisibility(View.VISIBLE);
                        break;
                    case 18:
                        vlSort18.setVisibility(View.VISIBLE);
                        break;
                    case 19:
                        vlSort19.setVisibility(View.VISIBLE);
                        break;
                    case 20:
                        vlSort20.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnNewSaleSortSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String massage = checking();

                if (!massage.equals("OK")) {
                    Toast.makeText(NewSaleSortActivity.this, massage, Toast.LENGTH_LONG).show();

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(NewSaleSortActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            showProgress(true);
                            submitRunnableTask(new Runnable() {
                                @Override
                                public void run() {
                                    // Saving the changes to the sale
                                    InventoryApp.sales.get(index).setSorted(true);
                                    Backendless.Persistence.save(InventoryApp.sales.get(index));

                                    double sortWeightSum = 0;
                                    double sortInvValueSum = 0;

                                    for (int i = 0; i < sortCheck.size(); i++) {
                                        sortWeightSum += sortCheck.get(i).getWeight();
                                        sortInvValueSum += sortCheck.get(i).getSum();
                                    }

                                    // Saving the main sort
                                    Sort newMainSort = newMainSortSave(sortInvValueSum, sortWeightSum);
                                    newMainSort.save();

                                    newMainSortObjectId = newMainSort.getObjectId();

                                    Sort oldMainSort = oldMainSortSave(etProfitSortS1, etWeightSortS1, etProfitSortS1);
                                    oldMainSort.save();

                                    HashMap<String, Object> parentObject = new HashMap<>();
                                    parentObject.put("objectId", newMainSortObjectId);

                                    HashMap<String, Object> oldMainSortObject = new HashMap<>();
                                    oldMainSortObject.put("objectId", oldMainSort.getObjectId());

                                    ArrayList<Map> children = new ArrayList<>();

                                    children.add(oldMainSortObject);

                                    Sort newSaleSort1 = newSaleSortSave(etPriceSortS1 ,etWeightSortS1, etProfitSortS1, etSortName1);
                                    Sort newSaleSort2 = newSaleSortSave(etPriceSortS2 ,etWeightSortS2, etProfitSortS2, etSortName2);
                                    Sort newSaleSort3 = newSaleSortSave(etPriceSortS3 ,etWeightSortS3, etProfitSortS3, etSortName3);
                                    Sort newSaleSort4 = newSaleSortSave(etPriceSortS4 ,etWeightSortS4, etProfitSortS4, etSortName4);
                                    Sort newSaleSort5 = newSaleSortSave(etPriceSortS5 ,etWeightSortS5, etProfitSortS5, etSortName5);
                                    Sort newSaleSort6 = newSaleSortSave(etPriceSortS6 ,etWeightSortS6, etProfitSortS6, etSortName6);
                                    Sort newSaleSort7 = newSaleSortSave(etPriceSortS7 ,etWeightSortS7, etProfitSortS7, etSortName7);
                                    Sort newSaleSort8 = newSaleSortSave(etPriceSortS8 ,etWeightSortS8, etProfitSortS8, etSortName8);
                                    Sort newSaleSort9 = newSaleSortSave(etPriceSortS9 ,etWeightSortS9, etProfitSortS9, etSortName9);
                                    Sort newSaleSort10 = newSaleSortSave(etPriceSortS10 ,etWeightSortS10, etProfitSortS10, etSortName10);
                                    Sort newSaleSort11 = newSaleSortSave(etPriceSortS11 ,etWeightSortS11, etProfitSortS11, etSortName11);
                                    Sort newSaleSort12 = newSaleSortSave(etPriceSortS12 ,etWeightSortS12, etProfitSortS12, etSortName12);
                                    Sort newSaleSort13 = newSaleSortSave(etPriceSortS13 ,etWeightSortS13, etProfitSortS13, etSortName13);
                                    Sort newSaleSort14 = newSaleSortSave(etPriceSortS14 ,etWeightSortS14, etProfitSortS14, etSortName14);
                                    Sort newSaleSort15 = newSaleSortSave(etPriceSortS15 ,etWeightSortS15, etProfitSortS15, etSortName15);
                                    Sort newSaleSort16 = newSaleSortSave(etPriceSortS16 ,etWeightSortS16, etProfitSortS16, etSortName16);
                                    Sort newSaleSort17 = newSaleSortSave(etPriceSortS17 ,etWeightSortS17, etProfitSortS17, etSortName17);
                                    Sort newSaleSort18 = newSaleSortSave(etPriceSortS18 ,etWeightSortS18, etProfitSortS18, etSortName18);
                                    Sort newSaleSort19 = newSaleSortSave(etPriceSortS19 ,etWeightSortS19, etProfitSortS19, etSortName19);
                                    Sort newSaleSort20 = newSaleSortSave(etPriceSortS20 ,etWeightSortS20, etProfitSortS20, etSortName20);

                                    if (newSaleSort1 != null) {
                                        newSaleSort1.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject1 = new HashMap<>();
                                        newSaleSortObject1.put("objectId", newSaleSort1.getObjectId());
                                        children.add(newSaleSortObject1);
                                    }

                                    if (newSaleSort2 != null) {
                                        newSaleSort2.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject2 = new HashMap<>();
                                        newSaleSortObject2.put("objectId", newSaleSort2.getObjectId());
                                        children.add(newSaleSortObject2);
                                    }

                                    if (newSaleSort3 != null) {
                                        newSaleSort3.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject3 = new HashMap<>();
                                        newSaleSortObject3.put("objectId", newSaleSort3.getObjectId());
                                        children.add(newSaleSortObject3);
                                    }

                                    if (newSaleSort4 != null) {
                                        newSaleSort4.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject4 = new HashMap<>();
                                        newSaleSortObject4.put("objectId", newSaleSort4.getObjectId());
                                        children.add(newSaleSortObject4);
                                    }

                                    if (newSaleSort5 != null) {
                                        newSaleSort5.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject5 = new HashMap<>();
                                        newSaleSortObject5.put("objectId", newSaleSort5.getObjectId());
                                        children.add(newSaleSortObject5);
                                    }

                                    if (newSaleSort6 != null) {
                                        newSaleSort6.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject6 = new HashMap<>();
                                        newSaleSortObject6.put("objectId", newSaleSort6.getObjectId());
                                        children.add(newSaleSortObject6);
                                    }

                                    if (newSaleSort7 != null) {
                                        newSaleSort7.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject7 = new HashMap<>();
                                        newSaleSortObject7.put("objectId", newSaleSort7.getObjectId());
                                        children.add(newSaleSortObject7);
                                    }

                                    if (newSaleSort8 != null) {
                                        newSaleSort8.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject8 = new HashMap<>();
                                        newSaleSortObject8.put("objectId", newSaleSort8.getObjectId());
                                        children.add(newSaleSortObject8);
                                    }

                                    if (newSaleSort9 != null) {
                                        newSaleSort9.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject9 = new HashMap<>();
                                        newSaleSortObject9.put("objectId", newSaleSort9.getObjectId());
                                        children.add(newSaleSortObject9);
                                    }

                                    if (newSaleSort10 != null) {
                                        newSaleSort10.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject10 = new HashMap<>();
                                        newSaleSortObject10.put("objectId", newSaleSort10.getObjectId());
                                        children.add(newSaleSortObject10);
                                    }

                                    if (newSaleSort11 != null) {
                                        newSaleSort11.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject11 = new HashMap<>();
                                        newSaleSortObject11.put("objectId", newSaleSort11.getObjectId());
                                        children.add(newSaleSortObject11);
                                    }

                                    if (newSaleSort12 != null) {
                                        newSaleSort12.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject12 = new HashMap<>();
                                        newSaleSortObject12.put("objectId", newSaleSort12.getObjectId());
                                        children.add(newSaleSortObject12);
                                    }

                                    if (newSaleSort13 != null) {
                                        newSaleSort13.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject13 = new HashMap<>();
                                        newSaleSortObject13.put("objectId", newSaleSort13.getObjectId());
                                        children.add(newSaleSortObject13);
                                    }


                                    if (newSaleSort14 != null) {
                                        newSaleSort14.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject14 = new HashMap<>();
                                        newSaleSortObject14.put("objectId", newSaleSort14.getObjectId());
                                        children.add(newSaleSortObject14);
                                    }

                                    if (newSaleSort15 != null) {
                                        newSaleSort15.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject15 = new HashMap<>();
                                        newSaleSortObject15.put("objectId", newSaleSort15.getObjectId());
                                        children.add(newSaleSortObject15);
                                    }

                                    if (newSaleSort16 != null) {
                                        newSaleSort16.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject16 = new HashMap<>();
                                        newSaleSortObject16.put("objectId", newSaleSort16.getObjectId());
                                        children.add(newSaleSortObject16);
                                    }

                                    if (newSaleSort17 != null) {
                                        newSaleSort17.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject17 = new HashMap<>();
                                        newSaleSortObject17.put("objectId", newSaleSort17.getObjectId());
                                        children.add(newSaleSortObject17);
                                    }

                                    if (newSaleSort18 != null) {
                                        newSaleSort18.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject18 = new HashMap<>();
                                        newSaleSortObject18.put("objectId", newSaleSort18.getObjectId());
                                        children.add(newSaleSortObject18);
                                    }

                                    if (newSaleSort19 != null) {
                                        newSaleSort19.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject19 = new HashMap<>();
                                        newSaleSortObject19.put("objectId", newSaleSort19.getObjectId());
                                        children.add(newSaleSortObject19);
                                    }

                                    if (newSaleSort20 != null) {
                                        newSaleSort20.save();

                                        // Setting the relation to the Sort
                                        HashMap<String, Object> newSaleSortObject20 = new HashMap<>();
                                        newSaleSortObject20.put("objectId", newSaleSort20.getObjectId());
                                        children.add(newSaleSortObject20);
                                    }

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

    public String checking() {
        sortCheck = new ArrayList<>();
        String massage;

        if (chosenMainSort == -1) {
            massage = "יש לבחור מיון המלאי";
            return massage;
        }

        massage = sortCheck(etPriceSortS1, etWeightSortS1, etSortName1, etProfitSortS1);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS2, etWeightSortS2, etSortName2, etProfitSortS2);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS3, etWeightSortS3, etSortName3, etProfitSortS3);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS4, etWeightSortS4, etSortName4, etProfitSortS4);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS5, etWeightSortS5, etSortName5, etProfitSortS5);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS6, etWeightSortS6, etSortName6, etProfitSortS6);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS7, etWeightSortS7, etSortName7, etProfitSortS7);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS8, etWeightSortS8, etSortName8, etProfitSortS8);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS9, etWeightSortS9, etSortName9, etProfitSortS9);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS10, etWeightSortS10, etSortName10, etProfitSortS10);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS11, etWeightSortS11, etSortName11, etProfitSortS11);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS12, etWeightSortS12, etSortName12, etProfitSortS12);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS13, etWeightSortS13, etSortName13, etProfitSortS13);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS14, etWeightSortS14, etSortName14, etProfitSortS14);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS15, etWeightSortS15, etSortName15, etProfitSortS15);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS16, etWeightSortS16, etSortName16, etProfitSortS16);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS17, etWeightSortS17, etSortName17, etProfitSortS17);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS18, etWeightSortS18, etSortName18, etProfitSortS18);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS19, etWeightSortS19, etSortName19, etProfitSortS19);
        if (!massage.equals("OK")) {
            return massage;
        }

        massage = sortCheck(etPriceSortS20, etWeightSortS20, etSortName20, etProfitSortS20);
        if (!massage.equals("OK")) {
            return massage;
        }

        double sortWeightSum = 0;
        double sortInvVauleSum = 0;
        double sortSoldValueSum = 0;

        for (int i = 0; i < sortCheck.size(); i++) {
            sortWeightSum += sortCheck.get(i).getWeight();
            sortInvVauleSum += sortCheck.get(i).getSum();
            sortSoldValueSum += (sortCheck.get(i).getSoldPrice()*sortCheck.get(i).getWeight());
        }

        if (sortWeightSum < InventoryApp.sales.get(index).getWeight() - CARAT_MARGIN || sortWeightSum > InventoryApp.sales.get(index).getWeight() + CARAT_MARGIN) {
            massage = "משקל המיונים לא שווה למשקל המכירה, יש להזין משקלים מתאמים";
            return massage;

        }

        if (sortSoldValueSum < InventoryApp.sales.get(index).getSaleSum() - VALUE_MARGIN || sortSoldValueSum > InventoryApp.sales.get(index).getSaleSum() + VALUE_MARGIN) {
            massage = "סכום שווי המיונים לא שווה שווי המכירה";
            return massage;
        }

        if (sortInvVauleSum > InventoryApp.sorts.get(chosenMainSort).getSum()) {
            massage = "סכום המיונים גבוהה מהסכום שקיים במיון המלאי שנבחר";
            return massage;
        }

        if (sortWeightSum > InventoryApp.sorts.get(chosenMainSort).getWeight()) {
            massage = "משקל המיונים גבוהה מהמשקל שקיים במיון המלאי שנבחר";
            return massage;
        }

        return "OK";
    }

    public Sort oldMainSortSave (EditText sortPriceText, EditText sortWeightText, EditText etProfitSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty()
        || etProfitSort.getText().toString().isEmpty())) {

            Sort sort = InventoryApp.sorts.get(chosenMainSort);
            sort.setTheDate(InventoryApp.sales.get(index).getSaleDate());
            sort.setFromId(newMainSortObjectId);
            sort.setLast(false);

            return sort;
        } else {
            return null;
        }
    }

    public Sort newMainSortSave(double valueSum, double weight) {
            Sort oldSort = InventoryApp.sorts.get(chosenMainSort);

            Sort newSort = new Sort();
            newSort.setName(oldSort.getName());
            newSort.setSortCount(oldSort.getSortCount()+1);

            newSort.setClarity(oldSort.getClarity());
            newSort.setColor(oldSort.getColor());
            newSort.setShape(oldSort.getShape());
            newSort.setSize(oldSort.getSize());

            newSort.setSum(oldSort.getSum() - valueSum);
            newSort.setWeight(oldSort.getWeight() - weight);
            newSort.setPrice(newSort.getWeight() == 0 ? 0 : newSort.getSum()/newSort.getWeight());
            newSort.setUserEmail(InventoryApp.user.getEmail());
            newSort.setLast(true);

            return newSort;
    }

    public Sort newSaleSortSave(EditText sortPriceText, EditText sortWeightText, EditText etProfitSort, EditText etSortName) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || etSortName.getText().toString().isEmpty())) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());
            double profit =  Double.valueOf(etProfitSort.getText().toString());

            Sort oldSort = InventoryApp.sorts.get(chosenMainSort);
            Sort newSaleSort = new Sort();
            newSaleSort.setName(etSortName.getText().toString().trim());
            newSaleSort.setSortCount(oldSort.getSortCount()+1);
            newSaleSort.setSaleId(InventoryApp.sales.get(index).getObjectId());
            newSaleSort.setSaleName(InventoryApp.sales.get(index).getClientName());

            newSaleSort.setClarity(oldSort.getClarity());
            newSaleSort.setColor(oldSort.getColor());
            newSaleSort.setShape(oldSort.getShape());
            newSaleSort.setSize(oldSort.getSize());

            newSaleSort.setPrice(sortPrice - profit);
            newSaleSort.setSoldPrice(sortPrice);
            newSaleSort.setSaleSum(sortPrice*sortWeight);
            newSaleSort.setSum(newSaleSort.getPrice()*sortWeight);
            newSaleSort.setWeight(sortWeight);
            newSaleSort.setLast(false);
            newSaleSort.setSale(true);
            newSaleSort.setTheDate(InventoryApp.sales.get(index).getSaleDate());
            newSaleSort.setFromId(newMainSortObjectId);
            newSaleSort.setUserEmail(InventoryApp.user.getEmail());

            return newSaleSort;
        } else {
            return null;
        }
    }

    public String sortCheck(EditText sortPriceText, EditText sortWeightText, EditText sortNameText,  EditText sortProfitText) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() ||
                sortProfitText.getText().toString().isEmpty() || sortNameText.getText().toString().isEmpty())) {

            String sortName = sortNameText.getText().toString().trim();
            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());
            double sortProfit = Double.valueOf(sortProfitText.getText().toString());

            Sort sort = InventoryApp.sorts.get(chosenMainSort);

            if (sortWeight == sort.getWeight() && sortPrice < sort.getPrice()) {
                return "לא ניתן למכור את כל המיון במחיר נמוך ממחיר העלות";
            }

            if (sortProfit < 0) {
                return "לא ניתן למכור בהפסד";
            }

            if (sortProfit > sortPrice) {
                return "לא ניתן לקבוע רווח גבוהה ממחיר המכירה";
            }

            SortInfo sortInfo = new SortInfo();
            sortInfo.setToName(sortName);
            sortInfo.setWeight(sortWeight);
            sortInfo.setSoldPrice(sortPrice);
            sortInfo.setPrice(sortPrice-sortProfit);
            sortInfo.setSum(sortInfo.getPrice()*sortWeight);
            sortCheck.add(sortInfo);
            return "OK";
        }
        return "OK";
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