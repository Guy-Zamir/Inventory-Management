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
    int toast = 0;

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

        int day = Integer.parseInt(MainActivity.buyArray.get(position).getDate().substring(0,2));
        int month = Integer.parseInt(MainActivity.buyArray.get(position).getDate().substring(2,4))-1;
        int year = Integer.parseInt(MainActivity.buyArray.get(position).getDate().substring(4,8));
        dpBuyEditDate.updateDate(year, month, day);

        cbBuyEditPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyEditPolish.isChecked()) {
                    etBuyEditDoneWeight.setVisibility(View.GONE);
                    etBuyEditWage.setVisibility(View.GONE);
                    cbBuyEditDone.setVisibility(View.GONE);
                    tvBuyEditDoneWeight.setVisibility(View.GONE);
                    tvBuyEditWage.setVisibility(View.GONE);
                } else {
                    etBuyEditDoneWeight.setVisibility(View.VISIBLE);
                    etBuyEditWage.setVisibility(View.VISIBLE);
                    cbBuyEditDone.setVisibility(View.VISIBLE);
                    tvBuyEditDoneWeight.setVisibility(View.VISIBLE);
                    tvBuyEditWage.setVisibility(View.VISIBLE);
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
                    toast = 1;
                } else {
                    supplier = etBuyEditSupplier.getText().toString();
                }

                if (etBuyEditID.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    id = etBuyEditID.getText().toString();
                }

                if (etBuyEditPrice.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    price = Double.parseDouble(etBuyEditPrice.getText().toString());
                }

                if (etBuyEditWeight.getText().toString().isEmpty()) {
                    toast = 1;
                } else {
                    weight = Double.parseDouble(etBuyEditWeight.getText().toString());
                }

                polish = cbBuyEditPolish.isChecked();
                done = cbBuyEditDone.isChecked();

                if (cbBuyEditPolish.isChecked()) {
                    done = true;
                    doneWeight = Double.parseDouble(etBuyEditWeight.getText().toString());
                    wage = 0;
                } else {
                    if (cbBuyEditDone.isChecked()) {

                        if (etBuyEditDoneWeight.getText().toString().isEmpty()) {
                            toast = 3;
                        } else if (Double.valueOf(etBuyEditDoneWeight.getText().toString()) > Double.parseDouble(etBuyEditPrice.getText().toString())) {
                            toast = 2;
                        } else {
                            doneWeight = Double.parseDouble(etBuyEditWeight.getText().toString());
                        }

                        if (etBuyEditWage.getText().toString().isEmpty()) {
                            wage = 50;
                        } else {
                            wage = Double.valueOf(etBuyEditWage.getText().toString());
                        }
                    } else {
                        doneWeight = 0;
                        wage = 0;
                    }
                }

                switch (toast) {
                    case 1:
                        Toast.makeText(BuyEditActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(BuyEditActivity.this, "לא ניתן להזין משקל גמור גבוה ממשקל החבילה", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        Toast.makeText(BuyEditActivity.this, "יש להזין את המקשל הגמור של החבליה", Toast.LENGTH_SHORT).show();
                        break;

                    case 0:

                        AlertDialog.Builder alert = new AlertDialog.Builder(BuyEditActivity.this);
                        alert.setTitle("שינוי נתונים");
                        alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                        alert.setNegativeButton(android.R.string.no, null);
                        alert.setIcon(android.R.drawable.ic_dialog_alert);

                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.buyArray.get(position).setDate(date);
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
