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
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Classes.Client;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Sale;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewSale extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private DatePicker dpSaleDate;
    private EditText etSaleID, etSaleSum, etSaleWeight, etSaleDays;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView acClients;
    private int chosenClient = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        dpSaleDate = findViewById(R.id.dpSaleDate);
        etSaleID = findViewById(R.id.etSaleID);
        etSaleSum = findViewById(R.id.etSaleSum);
        etSaleWeight = findViewById(R.id.etSaleWeight);
        etSaleDays = findViewById(R.id.etSaleDays);
        acClients = findViewById(R.id.acClients);
        Button btnSaleSubmit = findViewById(R.id.btnSaleSubmit);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("name");


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
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(NewSale.this, fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        acClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenClient = position;
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
                    String clientName = InventoryApp.clients.get(chosenClient).getName();
                    String id = etSaleID.getText().toString();
                    double weight = Double.parseDouble(etSaleWeight.getText().toString());
                    double saleSum = Double.parseDouble(etSaleSum.getText().toString());
                    int days = Integer.valueOf(etSaleDays.getText().toString().trim());

                    Sale sale = new Sale();
                    sale.setClientName(clientName);
                    sale.setSaleDate(getDateFromDatePicker(dpSaleDate));
                    sale.setClientName(InventoryApp.clients.get(chosenClient).getName());
                    sale.setId(id);
                    sale.setSaleSum(saleSum);
                    sale.setWeight(weight);
                    sale.setDays(days);

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
                            Toast.makeText(NewSale.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
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
        });
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
