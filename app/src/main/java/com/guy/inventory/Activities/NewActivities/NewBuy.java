package com.guy.inventory.Activities.NewActivities;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Buy;
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
    private ToggleButton tbBuyPolish;

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView acSuppliers;
    private int chosenSupplier = -1;

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
        tbBuyPolish = findViewById(R.id.tbBuyPolish);
        Button btnBuySubmit = findViewById(R.id.btnBuySubmit);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setPageSize(100);
        queryBuilder.setGroupBy("objectId");


        Backendless.Data.of(Supplier.class).find(queryBuilder, new AsyncCallback<List<Supplier>>() {
            @Override
            public void handleResponse(List<Supplier> response) {
                ArrayList<String> supplierNames = new ArrayList<>();
                for (Supplier supplier : response) {
                    supplierNames.add(supplier.getName());
                }
                InventoryApp.suppliers = response;
                adapter = new ArrayAdapter<>(NewBuy.this,android.R.layout.select_dialog_singlechoice, supplierNames);
                acSuppliers.setAdapter(adapter);
                acSuppliers.setThreshold(1);
                acSuppliers.setAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(NewBuy.this, fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        acSuppliers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenSupplier = position;
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

                    String supplierName = InventoryApp.suppliers.get(chosenSupplier).getName();
                    String id = etBuyID.getText().toString().trim();
                    double price = Double.parseDouble(etBuyPrice.getText().toString().trim());
                    double weight = Double.parseDouble(etBuyWeight.getText().toString().trim());
                    int days = Integer.valueOf(etBuyDays.getText().toString().trim());
                    boolean polish = tbBuyPolish.isChecked();

                    Buy buy = new Buy();
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
                            Toast.makeText(NewBuy.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
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
