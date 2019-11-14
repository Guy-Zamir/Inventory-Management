package com.guy.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class EditBuy extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llBuyDone;
    DatePicker dpBuyEditDate;
    EditText etBuyEditID, etBuyEditPrice, etBuyEditWeight, etBuyEditDays, etBuyEditDoneWeight, etBuyEditWage;

    TextView tvBuyEditSupplier, tvBuyEditID, tvBuyEditPrice, tvBuyEditWeight, tvBuyEditDays, tvBuyEditWage;

    Switch swBuyEditDoneWeight;
    Button btnBuyEditSubmit;

    int index, days;
    String  id;
    double weight, doneWeight, wage, price;
    boolean toast = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        llBuyDone = findViewById(R.id.llBuyDone);

        dpBuyEditDate = findViewById(R.id.dpBuyEditDate);
        etBuyEditID = findViewById(R.id.etBuyEditID);
        etBuyEditPrice = findViewById(R.id.etBuyEditPrice);
        etBuyEditWeight = findViewById(R.id.etBuyEditWeight);
        etBuyEditDays = findViewById(R.id.etBuyEditDays);
        etBuyEditDoneWeight = findViewById(R.id.etBuyEditDoneWeight);
        etBuyEditWage = findViewById(R.id.etBuyEditWage);
        btnBuyEditSubmit = findViewById(R.id.btnBuyEditSubmit);

        tvBuyEditSupplier = findViewById(R.id.tvBuyEditSupplier);
        tvBuyEditID = findViewById(R.id.tvBuyEditID);
        tvBuyEditPrice = findViewById(R.id.tvBuyEditPrice);
        tvBuyEditWeight = findViewById(R.id.tvBuyEditWeight);
        tvBuyEditDays = findViewById(R.id.tvBuyEditDays);
        swBuyEditDoneWeight = findViewById(R.id.swBuyEditDoneWeight);
        tvBuyEditWage = findViewById(R.id.tvDoneWage);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("נתוני קניה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);
        tvBuyEditSupplier.setText(InventoryApp.buys.get(index).getSupplierName());
        final DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        if (!InventoryApp.buys.get(index).isDone()) {
            llBuyDone.setVisibility(View.GONE);
        }

        swBuyEditDoneWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swBuyEditDoneWeight.isChecked()) {
                    etBuyEditDoneWeight.setText(nf.format((InventoryApp.buys.get(index).getDoneWeight()/InventoryApp.buys.get(index).getWeight())*100));
                    swBuyEditDoneWeight.setText("אחוז ליטוש");
                } else {
                    etBuyEditDoneWeight.setText(nf.format(InventoryApp.buys.get(index).getDoneWeight()));
                    swBuyEditDoneWeight.setText("משקל גמור");
                }
            }
        });

        etBuyEditID.setText(InventoryApp.buys.get(index).getId());
        etBuyEditPrice.setText(String.valueOf(InventoryApp.buys.get(index).getPrice()));
        etBuyEditWeight.setText(String.valueOf(InventoryApp.buys.get(index).getWeight()));
        etBuyEditDays.setText(String.valueOf(InventoryApp.buys.get(index).getDays()));
        etBuyEditWage.setText(String.valueOf(InventoryApp.buys.get(index).getWage()));
        etBuyEditDoneWeight.setText(nf.format(InventoryApp.buys.get(index).getDoneWeight()));

        Calendar date = Calendar.getInstance();
        date.setTime(InventoryApp.buys.get(index).getBuyDate());
        dpBuyEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        btnBuyEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!InventoryApp.buys.get(index).isDone()) {
                    if (etBuyEditID.getText().toString().isEmpty() ||
                        etBuyEditWeight.getText().toString().isEmpty()|| etBuyEditPrice.getText().toString().isEmpty()||
                        etBuyEditDays.getText().toString().isEmpty()) {
                        toast = true;
                    }
                } else if (etBuyEditWage.getText().toString().isEmpty() || etBuyEditDoneWeight.getText().toString().isEmpty() ||
                        etBuyEditID.getText().toString().isEmpty() || etBuyEditWeight.getText().toString().isEmpty()||
                        etBuyEditPrice.getText().toString().isEmpty()|| etBuyEditDays.getText().toString().isEmpty()) {
                    toast = true;
                }

                id = etBuyEditID.getText().toString().trim();
                weight = Double.parseDouble(etBuyEditWeight.getText().toString().trim());
                price = Double.parseDouble(etBuyEditPrice.getText().toString().trim());
                days = Integer.valueOf(etBuyEditDays.getText().toString().trim());
                wage = Double.parseDouble(etBuyEditWage.getText().toString().trim());
                if (swBuyEditDoneWeight.isChecked()) {
                    doneWeight = (Double.parseDouble(etBuyEditDoneWeight.getText().toString().trim())/100)*InventoryApp.buys.get(index).getWeight();
                } else {
                    doneWeight = Double.parseDouble(etBuyEditDoneWeight.getText().toString().trim());
                }

                if (toast) {
                    Toast.makeText(EditBuy.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditBuy.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.buys.get(index).setBuyDate(getDateFromDatePicker(dpBuyEditDate));
                            InventoryApp.buys.get(index).setId(id);
                            InventoryApp.buys.get(index).setWeight(weight);
                            InventoryApp.buys.get(index).setPrice(price);
                            InventoryApp.buys.get(index).setDoneWeight(doneWeight);
                            InventoryApp.buys.get(index).setWage(wage);
                            InventoryApp.buys.get(index).setDays(days);

                            if (InventoryApp.buys.get(index).isDone()) {
                                InventoryApp.buys.get(index).setWorkDepreciation(doneWeight / weight);
                            }

                            Calendar addedDays = Calendar.getInstance();
                            addedDays.setTime(InventoryApp.buys.get(index).getBuyDate());
                            addedDays.add(Calendar.DATE, days);
                            Date now = new Date();
                            InventoryApp.buys.get(index).setPayDate(addedDays.getTime());
                            if (now.after(addedDays.getTime())) {
                                InventoryApp.buys.get(index).setPaid(true);
                            }

                            InventoryApp.buys.get(index).setSum(price*weight);
                            showProgress(true);
                            Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                                @Override
                                public void handleResponse(Buy response) {
                                    showProgress(false);
                                    Toast.makeText(EditBuy.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    EditBuy.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(EditBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
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
