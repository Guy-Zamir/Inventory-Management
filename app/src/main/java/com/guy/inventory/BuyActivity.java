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
    EditText etBuySupplier, etBuyID, etBuyKaratPrice, etBuyKaratWeight, etBuyDoneWeight, etBuyWage;
    CheckBox cbBuyPolish, cbBuyDone;
    Button btnBuySubmit;
    String supplier, id, date;
    boolean polish, done;
    double price, weight, doneWeight, wage;

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
        etBuyKaratPrice = findViewById(R.id.etBuyKaratPrice);
        etBuyKaratWeight = findViewById(R.id.etBuyKaratWeight);
        etBuyDoneWeight = findViewById(R.id.etBuyDoneWeight);
        etBuyWage = findViewById(R.id.etBuyWage);
        cbBuyPolish = findViewById(R.id.cbBuyPolish);
        cbBuyDone = findViewById(R.id.cbBuyDone);
        btnBuySubmit = findViewById(R.id.btnBuySubmit);

        cbBuyPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyPolish.isChecked()) {
                    cbBuyDone.setChecked(true);
                    etBuyWage.setVisibility(View.GONE);
                    etBuyDoneWeight.setVisibility(View.GONE);
                } else {
                    etBuyWage.setVisibility(View.VISIBLE);
                    etBuyDoneWeight.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;

                @SuppressLint("DefaultLocale") String day = String.format("%02d", dpBuyDate.getDayOfMonth());
                @SuppressLint("DefaultLocale") String month = String.format("%02d", (dpBuyDate.getMonth()+1));
                @SuppressLint("DefaultLocale") String year = String.format("%02d", dpBuyDate.getYear());
                date = day+month+year;

                if (etBuySupplier.getText().toString().isEmpty() || etBuyID.getText().toString().isEmpty() ||
                        etBuyKaratPrice.getText().toString().isEmpty() || etBuyKaratWeight.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    supplier = etBuySupplier.getText().toString();
                    id = etBuyID.getText().toString();
                    price = Double.parseDouble(etBuyKaratPrice.getText().toString());
                    weight = Double.parseDouble(etBuyKaratWeight.getText().toString());
                }

                polish = cbBuyPolish.isChecked();
                done = cbBuyDone.isChecked();

                if (polish) {
                    wage = 0;
                    doneWeight = weight;
                } else if (done){
                    if (etBuyWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyWage.getText().toString());
                    }

                    if (etBuyDoneWeight.getText().toString().isEmpty()) {
                        toast = true;
                    } else {
                        doneWeight = Double.valueOf(etBuyDoneWeight.getText().toString());
                    }
                } else {
                    if (etBuyWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyWage.getText().toString());
                    }
                    if (etBuyDoneWeight.getText().toString().isEmpty()) {
                        doneWeight = 0;
                    } else {
                        done = true;
                        doneWeight = Double.valueOf(etBuyDoneWeight.getText().toString());
                    }
                }

                if (toast) {
                    Toast.makeText(BuyActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    Buy buy = new Buy();
                    buy.setBuyDate(date);
                    buy.setSupplier(supplier);
                    buy.setId(id);
                    buy.setPrice(price);
                    buy.setWeight(weight);
                    buy.setPolish(polish);
                    buy.setDone(done);
                    buy.setDoneWeight(doneWeight);
                    buy.setWage(wage);
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
}
