package com.guy.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewBuy extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private DatePicker dpBuyDate;
    private EditText etBuyID, etBuyPrice, etBuyWeight, etBuyDays;
    private Switch swBuyPolish;

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView acSuppliers;
    private int chosenSupplier = -1;

    private String aSupplier = "supplier";
    final DataQueryBuilder supplierBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String supplierClause = "supplier = '" + aSupplier + "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        dpBuyDate = findViewById(R.id.dpBuyDate);
        acSuppliers = findViewById(R.id.acSuppliers);
        etBuyID = findViewById(R.id.etBuyID);
        etBuyPrice = findViewById(R.id.etBuyPrice);
        etBuyWeight = findViewById(R.id.etBuyWeight);
        etBuyDays = findViewById(R.id.etBuyDays);
        swBuyPolish = findViewById(R.id.swBuyPolish);
        Button btnBuySubmit = findViewById(R.id.btnBuySubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("קניה חדשה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        supplierBuilder.setWhereClause(whereClause);
        supplierBuilder.setHavingClause(supplierClause);
        supplierBuilder.setPageSize(100);
        supplierBuilder.setSortBy("name");

        showProgress(true);

        Backendless.Data.of(Client.class).find(supplierBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                ArrayList<String> supplierNames = new ArrayList<>();
                for (Client supplier : response) {
                    supplierNames.add(supplier.getName());
                }
                InventoryApp.clients = response;
                adapter = new ArrayAdapter<>(NewBuy.this,android.R.layout.select_dialog_singlechoice, supplierNames);
                acSuppliers.setThreshold(1);
                acSuppliers.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(NewBuy.this, "טרם הוגדרו ספקים, עליך להגדיר ספק חדש לפני שמירת הקניה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        swBuyPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swBuyPolish.isChecked()) {
                    swBuyPolish.setText(" מלוטש  ");
                } else {
                    swBuyPolish.setText("  גלם  ");
                }
            }
        });

        acSuppliers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.clients.size(); i++) {
                    if (InventoryApp.clients.get(i).getName().equals(selection)) {
                        chosenSupplier = i;
                        break;
                    }
                }
            }
        });

        acSuppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acSuppliers.showDropDown();
            }
        });

        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenSupplier == -1) {
                    Toast.makeText(NewBuy.this, "יש לבחור שם ספק קיים בלבד", Toast.LENGTH_SHORT).show();

                } else if (etBuyID.getText().toString().isEmpty() ||
                        etBuyPrice.getText().toString().isEmpty() || etBuyWeight.getText().toString().isEmpty() ||
                        etBuyDays.getText().toString().isEmpty()){

                    Toast.makeText(NewBuy.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {

                    String supplierName = InventoryApp.clients.get(chosenSupplier).getName();
                    String id = etBuyID.getText().toString().trim();
                    double price = Double.parseDouble(etBuyPrice.getText().toString().trim());
                    double weight = Double.parseDouble(etBuyWeight.getText().toString().trim());
                    int days = Integer.valueOf(etBuyDays.getText().toString().trim());
                    boolean polish = swBuyPolish.isChecked();

                    final Buy buy = new Buy();
                    buy.setSupplierName(supplierName);
                    buy.setBuyDate(getDateFromDatePicker(dpBuyDate));
                    buy.setId(id);
                    buy.setPrice(price);
                    buy.setWeight(weight);
                    buy.setDays(days);

                    Calendar cAddedDays = Calendar.getInstance();
                    cAddedDays.setTime(buy.getBuyDate());
                    cAddedDays.add(Calendar.DATE, days);
                    Date addedDays = new Date();
                    addedDays.setTime(cAddedDays.getTimeInMillis());
                    Date now = new Date();
                    if (now.after(addedDays)) {
                        buy.setPaid(true);
                    }
                    buy.setPayDate(addedDays);

                    buy.setPolish(polish);
                    buy.setSum(price*weight);
                    buy.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(buy, new AsyncCallback<Buy>() {
                        @Override
                        public void handleResponse(Buy response) {
                            InventoryApp.buys.add(buy);
                            Toast.makeText(NewBuy.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finishActivity(1);
                            NewBuy.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(NewBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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

    private Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        return c.getTime();
    }
}
