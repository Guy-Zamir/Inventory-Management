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

public class NewSale extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private DatePicker dpSaleDate;
    private Switch swSalePolish;
    private EditText etSaleID, etSaleSum, etSaleWeight, etSaleDays;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView acClients;
    private int chosenClient = -1;
    private boolean isExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        swSalePolish = findViewById(R.id.swSalePolish);
        dpSaleDate = findViewById(R.id.dpSaleDate);
        etSaleID = findViewById(R.id.etSaleID);
        etSaleSum = findViewById(R.id.etSaleSum);
        etSaleWeight = findViewById(R.id.etSaleWeight);
        etSaleDays = findViewById(R.id.etSaleDays);
        acClients = findViewById(R.id.acClients);
        Button btnSaleSubmit = findViewById(R.id.btnSaleSubmit);

        isExport = getIntent().getBooleanExtra("export", false);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if (isExport) {
            actionBar.setTitle("יצוא חדש");
            swSalePolish.setVisibility(View.GONE);
        } else {
            actionBar.setTitle("מכירה חדשה");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setPageSize(100);
        queryBuilder.setGroupBy("objectId");

        showProgress(true);

        Backendless.Data.of(Client.class).find(queryBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                ArrayList<String> clientNames = new ArrayList<>();
                for (Client client : response) {
                    clientNames.add(client.getName());
                }
                InventoryApp.clients = response;
                adapter = new ArrayAdapter<>(NewSale.this, android.R.layout.select_dialog_singlechoice, clientNames);
                acClients.setAdapter(adapter);
                acClients.setThreshold(1);
                acClients.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(NewSale.this, "טרם הוגדרו לקוחות, עליך להגדיר לקוח חדש לפני שמירת המכירה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        swSalePolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swSalePolish.isChecked()) {
                    swSalePolish.setText(" גלם  ");
                } else {
                    swSalePolish.setText("  מלוטש  ");
                }
            }
        });

        acClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < InventoryApp.clients.size(); i++) {
                    if (InventoryApp.clients.get(i).getName().equals(selection)) {
                        chosenClient = i;
                        break;
                    }
                }
            }
        });

        acClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acClients.showDropDown();
            }
        });

        btnSaleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenClient == -1) {

                    Toast.makeText(NewSale.this, "יש לבחור שם חברה קיים בלבד", Toast.LENGTH_SHORT).show();

                } else if (acClients.getText().toString().isEmpty() || etSaleID.getText().toString().isEmpty() ||
                        etSaleWeight.getText().toString().isEmpty() || etSaleSum.getText().toString().isEmpty() ||
                        etSaleDays.getText().toString().isEmpty()) {

                    Toast.makeText(NewSale.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();

                } else {
                    final String clientName = InventoryApp.clients.get(chosenClient).getName();
                    final String id = etSaleID.getText().toString();
                    final double weight = Double.parseDouble(etSaleWeight.getText().toString());
                    final double saleSum = Double.parseDouble(etSaleSum.getText().toString());
                    final int days = Integer.valueOf(etSaleDays.getText().toString().trim());
                    final boolean polish = !swSalePolish.isChecked();

                    // An Export sale
                    if (isExport) {

                        final Export export = new Export();
                        export.setClientName(clientName);
                        export.setSaleDate(getDateFromDatePicker(dpSaleDate));
                        export.setClientName(InventoryApp.clients.get(chosenClient).getName());
                        export.setId(id);
                        export.setSaleSum(saleSum);
                        export.setWeight(weight);
                        export.setDays(days);
                        export.setPolish(polish);

                        Calendar cAddedDays = Calendar.getInstance();
                        cAddedDays.setTime(export.getSaleDate());
                        cAddedDays.add(Calendar.DATE, days);
                        Date addedDays = new Date();
                        addedDays.setTime(cAddedDays.getTimeInMillis());
                        Date now = new Date();
                        if (now.after(addedDays)) {
                            export.setPaid(true);
                        }
                        export.setPayDate(addedDays);
                        export.setPrice((saleSum / weight));
                        export.setUserEmail(InventoryApp.user.getEmail());

                        showProgress(true);
                        Backendless.Persistence.save(export, new AsyncCallback<Export>() {
                            @Override
                            public void handleResponse(Export response) {
                                InventoryApp.exports.add(export);
                                Toast.makeText(NewSale.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                showProgress(false);
                                NewSale.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(NewSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Not an Export sale
                    } else {

                        final Sale sale = new Sale();
                        sale.setClientName(clientName);
                        sale.setSaleDate(getDateFromDatePicker(dpSaleDate));
                        sale.setClientName(InventoryApp.clients.get(chosenClient).getName());
                        sale.setId(id);
                        sale.setSaleSum(saleSum);
                        sale.setWeight(weight);
                        sale.setDays(days);
                        sale.setPolish(polish);

                        Calendar cAddedDays = Calendar.getInstance();
                        cAddedDays.setTime(sale.getSaleDate());
                        cAddedDays.add(Calendar.DATE, days);
                        Date addedDays = new Date();
                        addedDays.setTime(cAddedDays.getTimeInMillis());
                        Date now = new Date();
                        if (now.after(addedDays)) {
                            sale.setPaid(true);
                        }
                        sale.setPayDate(addedDays);
                        sale.setPrice((saleSum / weight));
                        sale.setUserEmail(InventoryApp.user.getEmail());

                        showProgress(true);
                        Backendless.Persistence.save(sale, new AsyncCallback<Sale>() {
                            @Override
                            public void handleResponse(Sale response) {
                                InventoryApp.sales.add(sale);
                                Toast.makeText(NewSale.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                showProgress(false);
                                NewSale.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(NewSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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

    private Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return c.getTime();
    }
}
