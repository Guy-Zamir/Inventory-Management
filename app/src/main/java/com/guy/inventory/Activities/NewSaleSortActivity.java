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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSaleSortActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnNewSaleSortSubmit;

    EditText etWeightSortS1, etPriceSortS1, etWeightSortS2, etPriceSortS2, etWeightSortS3, etPriceSortS3, etWeightSortS4, etPriceSortS4, etWeightSortS5, etPriceSortS5;
    AutoCompleteTextView acSortS1, acSortS2, acSortS3, acSortS4, acSortS5;

    List<Map> sortInfos;

    final String LEFT_OVER_ID = "A19A4854-24E4-0305-FF7D-78282B68B900";
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
                sortInfos = new ArrayList<>();

                // Checking the sorts and assigning the values;
                sortCheck(etPriceSortS1, etWeightSortS1, chosenSort1);
                sortCheck(etPriceSortS2, etWeightSortS2, chosenSort2);
                sortCheck(etPriceSortS3, etWeightSortS3, chosenSort3);
                sortCheck(etPriceSortS4, etWeightSortS4, chosenSort4);
                sortCheck(etPriceSortS5, etWeightSortS5, chosenSort5);

                double sortWeightSum = 0;
                double sortValueSum = 0;

                for (int i = 0; i < sortInfos.size(); i++) {
                    sortWeightSum += (double) sortInfos.get(i).get("weight");
                    sortValueSum += (double) sortInfos.get(i).get("sum");
                }

                if (sortValueSum > InventoryApp.sales.get(index).getSaleSum()) {
                    Toast.makeText(NewSaleSortActivity.this, "סכום שווי המיונים גבוהה משווי הסחורה, יש להזין מחירים מתאימים", Toast.LENGTH_LONG).show();

                } else if (sortWeightSum > InventoryApp.sales.get(index).getWeight()) {
                    Toast.makeText(NewSaleSortActivity.this, "סכום המשקל של המיונים גבוהה מהמשקל הגמור, יש להזין משקלים מתאמים", Toast.LENGTH_LONG).show();

                } else {

                    // Assigning what's left from the weight to the left over sort
                    double sortWeightLeftOver = InventoryApp.sales.get(index).getWeight() - sortWeightSum;
                    double sortPriceLeftOver = (sortWeightLeftOver != 0) ? (InventoryApp.sales.get(index).getSaleSum() - sortValueSum) / sortWeightLeftOver : 0;

                    // Saving the left over and adding to the list
                    Map<String, Object> sortInfoLeftOver = new HashMap<>();
                    sortInfoLeftOver.put("fromId", InventoryApp.sales.get(index).getObjectId());
                    sortInfoLeftOver.put("fromBuy", false);
                    sortInfoLeftOver.put("fromSale", true);
                    sortInfoLeftOver.put("leftOver", true);
                    sortInfoLeftOver.put("toId", LEFT_OVER_ID);
                    sortInfoLeftOver.put("price", sortPriceLeftOver);
                    sortInfoLeftOver.put("weight", sortWeightLeftOver);
                    sortInfoLeftOver.put("sum", (sortWeightLeftOver * sortPriceLeftOver));
                    sortInfoLeftOver.put("userEmail", InventoryApp.user.getEmail());
                    for (final Sort sort : InventoryApp.sorts) {
                        if (sort.getObjectId().equals(LEFT_OVER_ID)) {
                            sortInfoLeftOver.put("sortCount", sort.getSortCount() + 1);
                            new Thread(new Runnable() {
                                public void run() {
                                    Backendless.Persistence.of(Sort.class).save(sort);
                                }
                            }).start();
                        }
                    }

                    sortInfos.add(sortInfoLeftOver);

                    AlertDialog.Builder alert = new AlertDialog.Builder(NewSaleSortActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            Backendless.Data.of("SortInfo").create(sortInfos, new AsyncCallback<List<String>>() {
                                @Override
                                public void handleResponse(List<String> response) {
                                    showProgress(false);
                                    Toast.makeText(NewSaleSortActivity.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    NewSaleSortActivity.this.finish();

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(NewSaleSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    public void sortCheck(EditText sortPriceText, EditText sortWeightText, final int chosenSort) {
        if (!(sortPriceText.getText().toString().isEmpty() || sortWeightText.getText().toString().isEmpty() || chosenSort == -1)) {

            double sortPrice = Double.valueOf(sortPriceText.getText().toString());
            double sortWeight = Double.valueOf(sortWeightText.getText().toString());
            String sortId = InventoryApp.sorts.get(chosenSort).getObjectId();

            Map<String, Object> sortInfo = new HashMap<>();
            sortInfo.put("fromId", InventoryApp.sales.get(index).getObjectId());
            sortInfo.put("fromBuy", false);
            sortInfo.put("fromSale", true);
            sortInfo.put("leftOver", false);
            sortInfo.put("toId", sortId);
            sortInfo.put("price", sortPrice);
            sortInfo.put("weight", sortWeight);
            sortInfo.put("sum", (sortPrice * sortWeight));
            sortInfo.put("sortCount", InventoryApp.sorts.get(chosenSort).getSortCount() + 1);
            sortInfo.put("userEmail", InventoryApp.user.getEmail());
            sortInfos.add(sortInfo);

            InventoryApp.sorts.get(chosenSort).setSortCount(InventoryApp.sorts.get(chosenSort).getSortCount()+1);
            new Thread(new Runnable() {
                public void run() {
                    Backendless.Persistence.of(Sort.class).save(InventoryApp.sorts.get(chosenSort));
                }
            }).start();
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

