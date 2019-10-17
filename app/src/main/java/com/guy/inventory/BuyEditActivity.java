package com.guy.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BuyEditActivity extends AppCompatActivity {
    DatePicker dpBuyEditDate;
    EditText etBuyEditSupplier, etBuyEditID, etBuyEditPrice, etBuyEditWeight, etBuyEditDoneWeight, etBuyEditWage;
    CheckBox cbBuyEditPolish, cbBuyEditDone;
    Button btnBuyEditSubmit;
    TextView tvBuyEditDoneWeight, tvBuyEditWage;
    int position;

    String supplier = "", id = "", date = "";
    double price = 0, weight = 0, doneWeight = 0, wage = 0;
    boolean done, polish;
    boolean toast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_edit);
        dpBuyEditDate = findViewById(R.id.dpBuyEditDate);
        etBuyEditSupplier = findViewById(R.id.etBuyEditSupplier);
        etBuyEditID = findViewById(R.id.etBuyEditID);
        etBuyEditPrice = findViewById(R.id.etBuyEditPrice);
        etBuyEditWeight = findViewById(R.id.etBuyEditWeight);
        etBuyEditDoneWeight = findViewById(R.id.etBuyEditDoneWeight);
        etBuyEditWage = findViewById(R.id.etBuyEditWage);
        cbBuyEditPolish = findViewById(R.id.cbBuyEditPolish);
        cbBuyEditDone = findViewById(R.id.cbBuyEditDone);
        btnBuyEditSubmit = findViewById(R.id.btnBuyEditSubmit);
        tvBuyEditDoneWeight = findViewById(R.id.tvBuyEditDoneWeight);
        tvBuyEditWage = findViewById(R.id.tvBuyEditWage);

        position = getIntent().getIntExtra("position", 0);
        etBuyEditSupplier.setText(MainActivity.buyArray.get(position).getSupplier());
        etBuyEditID.setText(MainActivity.buyArray.get(position).getId());
        etBuyEditPrice.setText(String.valueOf(MainActivity.buyArray.get(position).getPrice()));
        etBuyEditWeight.setText(String.valueOf(MainActivity.buyArray.get(position).getWeight()));
        etBuyEditDoneWeight.setText(String.valueOf(MainActivity.buyArray.get(position).getDoneWeight()));
        etBuyEditWage.setText(String.valueOf(MainActivity.buyArray.get(position).getWage()));
        cbBuyEditDone.setChecked(MainActivity.buyArray.get(position).isDone());
        cbBuyEditPolish.setChecked(MainActivity.buyArray.get(position).isPolish());

        int day = Integer.parseInt(MainActivity.buyArray.get(position).getBuyDate().substring(0,2));
        int month = Integer.parseInt(MainActivity.buyArray.get(position).getBuyDate().substring(2,4))-1;
        int year = Integer.parseInt(MainActivity.buyArray.get(position).getBuyDate().substring(4,8));
        dpBuyEditDate.updateDate(year, month, day);

        cbBuyEditPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyEditPolish.isChecked()) {
                    cbBuyEditDone.setChecked(true);
                }
            }
        });

        btnBuyEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = dpBuyEditDate.getDayOfMonth();
                int month = dpBuyEditDate.getMonth();
                int year = dpBuyEditDate.getYear();
                @SuppressLint("DefaultLocale") String dayText = String.format("%02d", day);
                @SuppressLint("DefaultLocale") String monthText = String.format("%02d", (month + 1));
                @SuppressLint("DefaultLocale") String yearText = String.format("%02d", year);
                date = dayText + monthText + yearText;

                if (etBuyEditSupplier.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    supplier = etBuyEditSupplier.getText().toString();
                }

                if (etBuyEditID.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    id = etBuyEditID.getText().toString();
                }

                if (etBuyEditPrice.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    price = Double.parseDouble(etBuyEditPrice.getText().toString());
                }

                if (etBuyEditWeight.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    weight = Double.parseDouble(etBuyEditWeight.getText().toString());
                }

                polish = cbBuyEditPolish.isChecked();
                done = cbBuyEditDone.isChecked();

                if (polish) {
                    wage = 0;
                    doneWeight = weight;
                } else if (done){
                    if (etBuyEditWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyEditWage.getText().toString());
                    }

                    if (etBuyEditDoneWeight.getText().toString().isEmpty()) {
                        toast = true;
                    } else {
                        doneWeight = Double.valueOf(etBuyEditDoneWeight.getText().toString());
                    }
                } else {
                    if (etBuyEditWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyEditWage.getText().toString());
                    }
                    if (etBuyEditDoneWeight.getText().toString().isEmpty()) {
                        doneWeight = 0;
                    } else {
                        done = true;
                        doneWeight = Double.valueOf(etBuyEditDoneWeight.getText().toString());
                    }
                }

                if (toast) {
                    Toast.makeText(BuyEditActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(BuyEditActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.buyArray.get(position).setBuyDate(date);
                            MainActivity.buyArray.get(position).setSupplier(supplier);
                            MainActivity.buyArray.get(position).setId(id);
                            MainActivity.buyArray.get(position).setPrice(price);
                            MainActivity.buyArray.get(position).setWeight(weight);
                            MainActivity.buyArray.get(position).setPolish(polish);
                            MainActivity.buyArray.get(position).setDone(done);
                            MainActivity.buyArray.get(position).setDoneWeight(doneWeight);
                            MainActivity.buyArray.get(position).setWage(wage);
                            setResult(RESULT_OK);
                            finishActivity(1);
                            finish();
                        }
                    });
                    alert.show();
                }
            }
        });
    }
}
