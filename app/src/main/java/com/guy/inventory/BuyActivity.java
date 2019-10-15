package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class BuyActivity extends AppCompatActivity {

    DatePicker dpBuyDate;
    EditText etBuySupplier, etBuyID, etBuyKaratPrice, etBuyKaratWeight, etBuyDoneWeight, etBuyWage;
    CheckBox cbBuyPolish, cbBuyDone;
    Button btnBuySubmit;
    String supplier, id;
    int day, month, year;
    boolean polish, done;
    double price, weight, doneWeight, wage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyactivity);

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

        etBuyDoneWeight.setVisibility(View.GONE);
        etBuyWage.setVisibility(View.GONE);

        cbBuyPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyPolish.isChecked()) {
                    cbBuyDone.setVisibility(View.GONE);
                    cbBuyDone.setChecked(false);
                } else {
                    cbBuyDone.setVisibility(View.VISIBLE);
                }
            }
        });

        cbBuyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyDone.isChecked()) {
                    etBuyDoneWeight.setVisibility(View.VISIBLE);
                    etBuyWage.setVisibility(View.VISIBLE);
                    cbBuyPolish.setChecked(false);
                } else {
                    etBuyDoneWeight.setVisibility(View.GONE);
                    etBuyWage.setVisibility(View.GONE);
                }
            }
        });

        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int toast = 0;

                day = dpBuyDate.getDayOfMonth();
                month = dpBuyDate.getMonth();
                year = dpBuyDate.getYear();

                if (etBuySupplier.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    supplier = etBuySupplier.getText().toString();
                }

                if (etBuyID.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    id = etBuyID.getText().toString();
                }

                if (etBuyKaratPrice.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    price = Double.parseDouble(etBuyKaratPrice.getText().toString());
                }

                if (etBuyKaratWeight.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    weight = Double.parseDouble(etBuyKaratWeight.getText().toString());
                }

                polish = cbBuyPolish.isChecked();
                done = cbBuyDone.isChecked();

                if (polish) {
                    doneWeight = Double.parseDouble(etBuyKaratWeight.getText().toString());
                    wage = 50;
                    done = true;

                } else {
                    if (done) {

                        if (etBuyDoneWeight.getText().toString().isEmpty()) {
                            toast = 3;
                        } else if (Double.valueOf(etBuyDoneWeight.getText().toString()) > Double.parseDouble(etBuyKaratWeight.getText().toString())) {
                            toast = 2;
                        } else {
                            doneWeight = Double.valueOf(etBuyDoneWeight.getText().toString());
                        }

                        if (etBuyWage.getText().toString().isEmpty()) {
                            wage = 50;
                        } else {
                            wage = Double.valueOf(etBuyWage.getText().toString());
                        }
                    } else {
                        doneWeight = 0;
                        wage = 0;
                    }
                }

                switch (toast) {
                    case 1:
                        Toast.makeText(BuyActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(BuyActivity.this, "לא ניתן להזין משקל גמור גבוה ממשקל החבילה", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        Toast.makeText(BuyActivity.this, "יש להזין את המקשל הגמור של החבליה", Toast.LENGTH_SHORT).show();
                        break;

                    case 0:
                        Intent intent = new Intent();
                        intent.putExtra("day", day);
                        intent.putExtra("month", month);
                        intent.putExtra("year", year);
                        intent.putExtra("supplier", supplier);
                        intent.putExtra("id", id);
                        intent.putExtra("price", price);
                        intent.putExtra("weight", weight);
                        intent.putExtra("polish", polish);
                        intent.putExtra("done", done);
                        intent.putExtra("doneWeight", doneWeight);
                        intent.putExtra("wage", wage);
                        setResult(RESULT_OK, intent);
                        finishActivity(MainActivity.buy);
                        finish();
                }
            }
        });
    }
}
