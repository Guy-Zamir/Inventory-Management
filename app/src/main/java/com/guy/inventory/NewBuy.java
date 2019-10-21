package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import java.util.Calendar;
import java.util.Date;

public class NewBuy extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    DatePicker dpBuyDate;
    EditText etBuySupplier, etBuyID, etBuyPrice, etBuyWeight, etBuyDays;
    CheckBox cbBuyPolish;
    Button btnBuySubmit;

    String supplier, id;
    boolean polish;
    double price, weight;
    int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        dpBuyDate = findViewById(R.id.dpBuyDate);
        etBuySupplier = findViewById(R.id.etBuySupplier);
        etBuyID = findViewById(R.id.etBuyID);
        etBuyPrice = findViewById(R.id.etBuyPrice);
        etBuyWeight = findViewById(R.id.etBuyWeight);
        etBuyDays = findViewById(R.id.etBuyDays);
        cbBuyPolish = findViewById(R.id.cbBuyPolish);
        btnBuySubmit = findViewById(R.id.btnBuySubmit);


        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;

                if (etBuySupplier.getText().toString().isEmpty() || etBuyID.getText().toString().isEmpty() ||
                        etBuyPrice.getText().toString().isEmpty() || etBuyWeight.getText().toString().isEmpty() ||
                        etBuyDays.getText().toString().isEmpty()){
                    toast = true;
                } else {
                    supplier = etBuySupplier.getText().toString().trim();
                    id = etBuyID.getText().toString().trim();
                    price = Double.parseDouble(etBuyPrice.getText().toString().trim());
                    weight = Double.parseDouble(etBuyWeight.getText().toString().trim());
                    days = Integer.valueOf(etBuyDays.getText().toString().trim());
                    polish = cbBuyPolish.isChecked();
                }

                if (toast) {
                    Toast.makeText(NewBuy.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    Buy buy = new Buy();
                    buy.setBuyDate(getDateFromDatePicker(dpBuyDate));
                    buy.setSupplier(supplier);
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
