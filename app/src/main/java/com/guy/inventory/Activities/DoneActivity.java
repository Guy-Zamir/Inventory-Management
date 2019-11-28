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
import java.util.List;


public class DoneActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnDoneSubmit;
    EditText etDoneWage, etDoneWeight;
    Switch swDoneWeight;

    EditText etWeightSort1, etPriceSort1, etWeightSort2, etPriceSort2, etWeightSort3, etPriceSort3, etWeightSort4, etPriceSort4, etWeightSort5, etPriceSort5;
    AutoCompleteTextView acSort1, acSort2, acSort3, acSort4, acSort5;

    final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    ArrayAdapter<String> sortAdapter;

    int chosenSort1 = -1, chosenSort2 = -1, chosenSort3 = -1, chosenSort4 = -1, chosenSort5 = -1;

    double sortPrice1 = 0, sortPrice2 = 0, sortPrice3 = 0, sortPrice4 = 0, sortPrice5 = 0, sortPriceDef = 0,
            sortWeight1 = 0, sortWeight2 = 0, sortWeight3 = 0, sortWeight4 = 0, sortWeight5 = 0, sortWeightDef = 0;

    String sortName1, sortName2, sortName3, sortName4, sortName5;

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
                    sortNames.add(sort.getName());
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

                // Checking the sorts and assigning the values;
                sortCheck(etPriceSort1, etWeightSort1, chosenSort1, 1);
                sortCheck(etPriceSort2, etWeightSort2, chosenSort2, 2);
                sortCheck(etPriceSort3, etWeightSort3, chosenSort3, 3);
                sortCheck(etPriceSort4, etWeightSort4, chosenSort4, 4);
                sortCheck(etPriceSort5, etWeightSort5, chosenSort5, 5);

                double sortWeightSum = sortWeight1 + sortWeight2 + sortWeight3 + sortWeight4 + sortWeight5;
                double sortValueSum = sortWeight1*sortPrice1 + sortWeight2*sortPrice2 + sortWeight3*sortPrice3 + sortWeight4*sortPrice4 + sortWeight5*sortPrice5;

                // Checking whether the user entered done weight right
                if (etDoneWage.getText().toString().isEmpty() || etDoneWeight.getText().toString().isEmpty() || doneWeight > InventoryApp.buys.get(index).getWeight()) {
                    Toast.makeText(DoneActivity.this, "סכום המשקל הגמור ושכר העבודה אינם נכונים, יש להזין את הנתונים הנכונים", Toast.LENGTH_SHORT).show();
                }

                wage = Double.parseDouble(etDoneWage.getText().toString().trim());
                if (swDoneWeight.isChecked()) {
                    doneWeight = (Double.parseDouble(etDoneWeight.getText().toString().trim())/100)*InventoryApp.buys.get(index).getWeight();
                } else {
                    doneWeight = Double.parseDouble(etDoneWeight.getText().toString().trim());
                }

                if (sortValueSum > InventoryApp.buys.get(index).getSum()) {
                    Toast.makeText(DoneActivity.this, "סכום המיונים גבוהה מסכום הסחורה, יש להזין מחירים מתאימים", Toast.LENGTH_SHORT).show();

                } else if (sortWeightSum > doneWeight) {
                    Toast.makeText(DoneActivity.this, "סכום המשקל של המיונים גבוהה מהמשקל הגמור, יש להזין משקלים מתאמים", Toast.LENGTH_SHORT).show();

                } else {

                    // Assigning what's left from the done weight to the default sort
                    sortWeightDef = doneWeight - sortWeightSum;
                    sortPriceDef = (sortWeightDef != 0) ? (InventoryApp.buys.get(index).getSum() - sortValueSum) / sortWeightDef : 0;

                    AlertDialog.Builder alert = new AlertDialog.Builder(DoneActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.buys.get(index).setSortPrice1(sortPrice1);
                            InventoryApp.buys.get(index).setSortPrice2(sortPrice2);
                            InventoryApp.buys.get(index).setSortPrice3(sortPrice3);
                            InventoryApp.buys.get(index).setSortPrice4(sortPrice4);
                            InventoryApp.buys.get(index).setSortPrice5(sortPrice5);
                            InventoryApp.buys.get(index).setSortWeight1(sortWeight1);
                            InventoryApp.buys.get(index).setSortWeight2(sortWeight2);
                            InventoryApp.buys.get(index).setSortWeight3(sortWeight3);
                            InventoryApp.buys.get(index).setSortWeight4(sortWeight4);
                            InventoryApp.buys.get(index).setSortWeight5(sortWeight5);
                            InventoryApp.buys.get(index).setSortName1(sortName1);
                            InventoryApp.buys.get(index).setSortName2(sortName2);
                            InventoryApp.buys.get(index).setSortName3(sortName3);
                            InventoryApp.buys.get(index).setSortName4(sortName4);
                            InventoryApp.buys.get(index).setSortName5(sortName5);
                            InventoryApp.buys.get(index).setSortPriceDef(sortPriceDef);
                            InventoryApp.buys.get(index).setSortWeightDef(sortWeightDef);

                            InventoryApp.buys.get(index).setDone(true);
                            InventoryApp.buys.get(index).setDoneWeight(doneWeight);
                            InventoryApp.buys.get(index).setWage(wage);
                            InventoryApp.buys.get(index).setWorkDepreciation(doneWeight / InventoryApp.buys.get(index).getWeight());

                            showProgress(true);
                            Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                                @Override
                                public void handleResponse(Buy response) {
                                    if (chosenSort1 != -1) {
                                        InventoryApp.sorts.get(chosenSort1).setSum(InventoryApp.sorts.get(chosenSort1).getSum() + (sortWeight1*sortPrice1));
                                        InventoryApp.sorts.get(chosenSort1).setWeight(InventoryApp.sorts.get(chosenSort1).getWeight() + sortWeight1);
                                        InventoryApp.sorts.get(chosenSort1).setPrice(InventoryApp.sorts.get(chosenSort1).getSum() / InventoryApp.sorts.get(chosenSort1).getWeight());
                                        Backendless.Persistence.save(InventoryApp.sorts.get(chosenSort1), new AsyncCallback<Sort>() {
                                            @Override
                                            public void handleResponse(Sort response) {

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    if (chosenSort2 != -1) {
                                        InventoryApp.sorts.get(chosenSort2).setSum(InventoryApp.sorts.get(chosenSort2).getSum() + (sortWeight2*sortPrice2));
                                        InventoryApp.sorts.get(chosenSort2).setWeight(InventoryApp.sorts.get(chosenSort2).getWeight() + sortWeight2);
                                        InventoryApp.sorts.get(chosenSort2).setPrice(InventoryApp.sorts.get(chosenSort2).getSum() / InventoryApp.sorts.get(chosenSort2).getWeight());
                                        Backendless.Persistence.save(InventoryApp.sorts.get(chosenSort2), new AsyncCallback<Sort>() {
                                            @Override
                                            public void handleResponse(Sort response) {

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    if (chosenSort3 != -1) {
                                        InventoryApp.sorts.get(chosenSort3).setSum(InventoryApp.sorts.get(chosenSort3).getSum() + (sortWeight3*sortPrice3));
                                        InventoryApp.sorts.get(chosenSort3).setWeight(InventoryApp.sorts.get(chosenSort3).getWeight() + sortWeight3);
                                        InventoryApp.sorts.get(chosenSort3).setPrice(InventoryApp.sorts.get(chosenSort3).getSum() / InventoryApp.sorts.get(chosenSort3).getWeight());
                                        Backendless.Persistence.save(InventoryApp.sorts.get(chosenSort3), new AsyncCallback<Sort>() {
                                            @Override
                                            public void handleResponse(Sort response) {

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    if (chosenSort4 != -1) {
                                        InventoryApp.sorts.get(chosenSort4).setSum(InventoryApp.sorts.get(chosenSort4).getSum() + (sortWeight4*sortPrice4));
                                        InventoryApp.sorts.get(chosenSort4).setWeight(InventoryApp.sorts.get(chosenSort4).getWeight() + sortWeight4);
                                        InventoryApp.sorts.get(chosenSort4).setPrice(InventoryApp.sorts.get(chosenSort4).getSum() / InventoryApp.sorts.get(chosenSort4).getWeight());
                                        Backendless.Persistence.save(InventoryApp.sorts.get(chosenSort4), new AsyncCallback<Sort>() {
                                            @Override
                                            public void handleResponse(Sort response) {

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    if (chosenSort5 != -1) {
                                        InventoryApp.sorts.get(chosenSort5).setSum(InventoryApp.sorts.get(chosenSort5).getSum() + (sortWeight5*sortPrice5));
                                        InventoryApp.sorts.get(chosenSort5).setWeight(InventoryApp.sorts.get(chosenSort5).getWeight() + sortWeight5);
                                        InventoryApp.sorts.get(chosenSort5).setPrice(InventoryApp.sorts.get(chosenSort5).getSum() / InventoryApp.sorts.get(chosenSort5).getWeight());
                                        Backendless.Persistence.save(InventoryApp.sorts.get(chosenSort5), new AsyncCallback<Sort>() {
                                            @Override
                                            public void handleResponse(Sort response) {

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    InventoryApp.sorts.get(InventoryApp.sorts.size()-1).setSum(InventoryApp.sorts.get(InventoryApp.sorts.size()-1).getSum() + (sortWeightDef*sortPriceDef));
                                    InventoryApp.sorts.get(InventoryApp.sorts.size()-1).setWeight(InventoryApp.sorts.get(InventoryApp.sorts.size()-1).getWeight() + sortWeightDef);
                                    InventoryApp.sorts.get(InventoryApp.sorts.size()-1).setPrice(InventoryApp.sorts.get(InventoryApp.sorts.size()-1).getSum() / InventoryApp.sorts.get(InventoryApp.sorts.size()-1).getWeight());
                                    Backendless.Persistence.save(InventoryApp.sorts.get(InventoryApp.sorts.size()-1), new AsyncCallback<Sort>() {
                                        @Override
                                        public void handleResponse(Sort response) {

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(DoneActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

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
                    });
                    alert.show();
                }
            }
        });
    }

    public void sortCheck(EditText sortPrice, EditText sortWeight, int chosenSort, int sortNumber) {
        if (!(sortPrice.getText().toString().isEmpty() || sortWeight.getText().toString().isEmpty() || chosenSort == -1)) {
            switch (sortNumber) {
                case 1:
                    sortPrice1 = Double.valueOf(sortPrice.getText().toString());
                    sortWeight1 = Double.valueOf(sortWeight.getText().toString());
                    sortName1 = InventoryApp.sorts.get(chosenSort).getName();
                    break;

                case 2:
                    sortPrice2 = Double.valueOf(sortPrice.getText().toString());
                    sortWeight2 = Double.valueOf(sortWeight.getText().toString());
                    sortName2 = InventoryApp.sorts.get(chosenSort).getName();
                    break;

                case 3:
                    sortPrice3 = Double.valueOf(sortPrice.getText().toString());
                    sortWeight3 = Double.valueOf(sortWeight.getText().toString());
                    sortName3 = InventoryApp.sorts.get(chosenSort).getName();
                    break;

                case 4:
                    sortPrice4 = Double.valueOf(sortPrice.getText().toString());
                    sortWeight4 = Double.valueOf(sortWeight.getText().toString());
                    sortName4 = InventoryApp.sorts.get(chosenSort).getName();
                    break;

                case 5:
                    sortPrice5 = Double.valueOf(sortPrice.getText().toString());
                    sortWeight5 = Double.valueOf(sortWeight.getText().toString());
                    sortName5 = InventoryApp.sorts.get(chosenSort).getName();
                    break;
            }
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
