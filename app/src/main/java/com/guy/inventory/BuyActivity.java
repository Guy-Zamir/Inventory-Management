package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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

public class BuyActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_buyactivity);

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
                    Toast.makeText(BuyActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    Buy buy = new Buy();
                    buy.setBuyDate(getDateFromDatePicker(dpBuyDate));
                    buy.setSupplier(supplier);
                    buy.setId(id);
                    buy.setPrice(price);
                    buy.setWeight(weight);
                    buy.setDays(days);
                    buy.setPolish(polish);
                    buy.setSum(price*weight);
                    buy.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(buy, new AsyncCallback<Buy>() {
                        @Override
                        public void handleResponse(Buy response) {
                            Toast.makeText(BuyActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            BuyActivity.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(BuyActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String getDateFromDatePicker(DatePicker datePicker){
        @SuppressLint("DefaultLocale") String day = String.format("%02d", datePicker.getDayOfMonth());
        @SuppressLint("DefaultLocale") String month = String.format("%02d", (datePicker.getMonth()+1));
        @SuppressLint("DefaultLocale") String year = String.format("%02d", datePicker.getYear());
        return day+"/"+month+"/"+year;
    }
}
