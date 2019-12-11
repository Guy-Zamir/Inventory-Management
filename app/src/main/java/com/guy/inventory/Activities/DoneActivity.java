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
import com.guy.inventory.Tables.Buy;
import com.guy.inventory.Tables.Sort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DoneActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnDoneSubmit;
    EditText etDoneWage, etDoneWeight;
    Switch swDoneWeight;

    EditText etWeightSort1, etPriceSort1, etWeightSort2, etPriceSort2, etWeightSort3, etPriceSort3, etWeightSort4, etPriceSort4, etWeightSort5, etPriceSort5;
    AutoCompleteTextView acSort1, acSort2, acSort3, acSort4, acSort5;

    List<Map> sortInfos;

    final String LEFT_OVER_ID = "0DAF3785-3207-041B-FF57-E78E656A2500";
    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    ArrayAdapter<String> sortAdapter;

    int chosenSort1 = -1, chosenSort2 = -1, chosenSort3 = -1, chosenSort4 = -1, chosenSort5 = -1;

    double doneWeight, wage;
    int index;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("סרייה גמורה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        sortBuilder.setWhereClause(whereClause);
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                    if (InventoryApp.sorts.get(i).getName().equals(selection)) {
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
                sortInfos = new ArrayList<>();

                // Checking the sorts and assigning the values;
                sortCheck(etPriceSort1, etWeightSort1, chosenSort1);
                sortCheck(etPriceSort2, etWeightSort2, chosenSort2);
                sortCheck(etPriceSort3, etWeightSort3, chosenSort3);
                sortCheck(etPriceSort4, etWeightSort4, chosenSort4);
                sortCheck(etPriceSort5, etWeightSort5, chosenSort5);

                double sortWeightSum = 0;
                double sortValueSum = 0;

                for (int i=0; i<sortInfos.size(); i++) {
                    sortWeightSum += (double) sortInfos.get(i).get("weight");
                    sortValueSum += (double) sortInfos.get(i).get("sum");
                }

                wage = (etDoneWage.getText().toString().isEmpty()) ? 0 : Double.parseDouble(etDoneWage.getText().toString().trim());

                if (swDoneWeight.isChecked()) {
                    doneWeight = (etDoneWeight.getText().toString().isEmpty()) ? -1 : ((Double.parseDouble(etDoneWeight.getText().toString().trim()) / 100) * InventoryApp.buys.get(index).getWeight());
                } else {
                    doneWeight = (etDoneWeight.getText().toString().isEmpty()) ? -1 : (Double.parseDouble(etDoneWeight.getText().toString().trim()));
                }

                // Checking whether the user entered done weight right
                if (doneWeight > InventoryApp.buys.get(index).getWeight()) {
                    Toast.makeText(DoneActivity.this, "סכום המשקל הגמור גבוהה ממשקל החבילה, יש להזין משקל שווה או נמוך למשקל החבילה", Toast.LENGTH_LONG).show();

                } else if (doneWeight < 0) {
                    Toast.makeText(DoneActivity.this, "לא הוזן משקל גמור, יש להזין את המשקל הגמור של החבילה", Toast.LENGTH_LONG).show();

                } else {

                    if (sortValueSum > InventoryApp.buys.get(index).getSum()) {
                        Toast.makeText(DoneActivity.this, "סכום שווי המיונים גבוהה משווי הסחורה, יש להזין מחירים מתאימים", Toast.LENGTH_LONG).show();

                    } else if (sortWeightSum > doneWeight) {
                        Toast.makeText(DoneActivity.this, "סכום המשקל של המיונים גבוהה מהמשקל הגמור, יש להזין משקלים מתאמים", Toast.LENGTH_LONG).show();

                    } else {

                        // Assigning what's left from the done weight to the left over sort
                        double sortWeightLeftOver = doneWeight - sortWeightSum;
                        double sortPriceLeftOver = (sortWeightLeftOver != 0) ? (InventoryApp.buys.get(index).getSum() - sortValueSum) / sortWeightLeftOver : 0;

                        // Saving the left over and adding to the list
                        Map<String, Object> sortInfoLeftOver = new HashMap<>();
                        sortInfoLeftOver.put("fromId", InventoryApp.buys.get(index).getObjectId());
                        sortInfoLeftOver.put("fromBuy", true);
                        sortInfoLeftOver.put("fromSale", false);
                        sortInfoLeftOver.put("leftOver", true);
                        sortInfoLeftOver.put("toId", LEFT_OVER_ID);
                        sortInfoLeftOver.put("price", sortPriceLeftOver);
                        sortInfoLeftOver.put("weight", sortWeightLeftOver);
                        sortInfoLeftOver.put("sum", (sortWeightLeftOver * sortPriceLeftOver));
                        sortInfos.add(sortInfoLeftOver);

                        AlertDialog.Builder alert = new AlertDialog.Builder(DoneActivity.this);
                        alert.setTitle("שינוי נתונים");
                        alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                        alert.setNegativeButton(android.R.string.no, null);
                        alert.setIcon(android.R.drawable.ic_dialog_alert);
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InventoryApp.buys.get(index).setDone(true);
                                InventoryApp.buys.get(index).setDoneWeight(doneWeight);
                                InventoryApp.buys.get(index).setWage(wage);
                                InventoryApp.buys.get(index).setWorkDepreciation(doneWeight / InventoryApp.buys.get(index).getWeight());

                                showProgress(true);
                                Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                                    @Override
                                    public void handleResponse(Buy response) {

                                        Backendless.Data.of("SortInfo").create(sortInfos, new AsyncCallback<List<String>>() {
                                            @Override
                                            public void handleResponse(List<String> response) {
                                                showProgress(false);
                                                Toast.makeText(DoneActivity.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                                setResult(RESULT_OK);
                                                finishActivity(1);
                                                DoneActivity.this.finish();
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                showProgress(false);
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
            String sortId = InventoryApp.sorts.get(chosenSort).getObjectId();

            Map<String, Object> sortInfo = new HashMap<>();
            sortInfo.put("fromId", InventoryApp.buys.get(index).getObjectId());
            sortInfo.put("fromBuy", true);
            sortInfo.put("fromSale", false);
            sortInfo.put("leftOver", false);
            sortInfo.put("toId", sortId);
            sortInfo.put("price", sortPrice);
            sortInfo.put("weight", sortWeight);
            sortInfo.put("sum", (sortPrice * sortWeight));
            sortInfos.add(sortInfo);
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

