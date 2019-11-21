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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import java.util.Calendar;
import java.util.Date;

public class EditSale extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private DatePicker dpSaleEditDate;
    EditText etSaleEditID, etSaleEditWeight, etSaleEditSum, etSaleEditDays;
    TextView tvSaleEditClientName, tvSaleEditID, tvSaleEditWeight, tvSaleEditSum, tvSaleEditDays;
    Switch swEditSalePolish;
    Button btnSaleEditSubmit;

    int index, days;
    String id;
    double weight, saleSum;
    boolean export = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sale);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        dpSaleEditDate = findViewById(R.id.dpSaleEditDate);
        etSaleEditID = findViewById(R.id.etSaleEditID);
        etSaleEditWeight = findViewById(R.id.etSaleEditWeight);
        etSaleEditSum = findViewById(R.id.etSaleEditSum);
        etSaleEditDays = findViewById(R.id.etSaleEditDays);
        btnSaleEditSubmit = findViewById(R.id.btnSaleEditSubmit);
        swEditSalePolish = findViewById(R.id.swEditSalePolish);

        tvSaleEditClientName = findViewById(R.id.tvSaleEditClientName);
        tvSaleEditID = findViewById(R.id.tvSaleEditID);
        tvSaleEditWeight = findViewById(R.id.tvSaleEditWeight);
        tvSaleEditSum = findViewById(R.id.tvSaleEditSum);
        tvSaleEditDays = findViewById(R.id.tvSaleEditDays);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("נתוני מכירה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);
        export = getIntent().getBooleanExtra("export", false);

        if (export) {

            tvSaleEditClientName.setText(InventoryApp.exports.get(index).getClientName());
            etSaleEditID.setText(InventoryApp.exports.get(index).getId());
            etSaleEditWeight.setText(String.valueOf(InventoryApp.exports.get(index).getWeight()));
            etSaleEditSum.setText(String.valueOf(InventoryApp.exports.get(index).getSaleSum()));
            etSaleEditDays.setText(String.valueOf(InventoryApp.exports.get(index).getDays()));
            Calendar date = Calendar.getInstance();
            date.setTime(InventoryApp.exports.get(index).getSaleDate());
            dpSaleEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        } else {

            tvSaleEditClientName.setText(InventoryApp.sales.get(index).getClientName());
            etSaleEditID.setText(InventoryApp.sales.get(index).getId());
            etSaleEditWeight.setText(String.valueOf(InventoryApp.sales.get(index).getWeight()));
            etSaleEditSum.setText(String.valueOf(InventoryApp.sales.get(index).getSaleSum()));
            etSaleEditDays.setText(String.valueOf(InventoryApp.sales.get(index).getDays()));
            Calendar date = Calendar.getInstance();
            date.setTime(InventoryApp.sales.get(index).getSaleDate());
            dpSaleEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }

        swEditSalePolish.setChecked((export) ? !InventoryApp.exports.get(index).isPolish() : !InventoryApp.sales.get(index).isPolish());
        swEditSalePolish.setText(swEditSalePolish.isChecked() ? " גלם  " : "  מלוטש  ");
        swEditSalePolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swEditSalePolish.setText(swEditSalePolish.isChecked() ? " גלם  " : "  מלוטש  ");
            }
        });

        btnSaleEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSaleEditID.getText().toString().isEmpty() ||
                        etSaleEditWeight.getText().toString().isEmpty()|| etSaleEditSum.getText().toString().isEmpty()||
                    etSaleEditDays.getText().toString().isEmpty()) {
                    Toast.makeText(EditSale.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();

                } else {

                    id = etSaleEditID.getText().toString().trim();
                    weight = Double.parseDouble(etSaleEditWeight.getText().toString().trim());
                    saleSum = Double.parseDouble(etSaleEditSum.getText().toString().trim());
                    days = Integer.valueOf(etSaleEditDays.getText().toString().trim());

                    AlertDialog.Builder alert = new AlertDialog.Builder(EditSale.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            if (export) {

                                InventoryApp.exports.get(index).setSaleDate(getDateFromDatePicker(dpSaleEditDate));
                                InventoryApp.exports.get(index).setId(id);
                                InventoryApp.exports.get(index).setWeight(weight);
                                InventoryApp.exports.get(index).setSaleSum(saleSum);
                                InventoryApp.exports.get(index).setDays(days);
                                InventoryApp.exports.get(index).setPolish(!swEditSalePolish.isChecked());

                                Calendar addedDays = Calendar.getInstance();
                                addedDays.setTime(InventoryApp.exports.get(index).getSaleDate());
                                addedDays.add(Calendar.DATE, days);
                                Date now = new Date();
                                InventoryApp.exports.get(index).setPayDate(addedDays.getTime());
                                if (now.after(addedDays.getTime())) {
                                    InventoryApp.exports.get(index).setPaid(true);
                                }

                                InventoryApp.exports.get(index).setPrice(saleSum / weight);
                                Backendless.Persistence.save(InventoryApp.exports.get(index), new AsyncCallback<Export>() {
                                    @Override
                                    public void handleResponse(Export response) {
                                        Toast.makeText(EditSale.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finishActivity(1);
                                        EditSale.this.finish();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(EditSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {

                                InventoryApp.sales.get(index).setSaleDate(getDateFromDatePicker(dpSaleEditDate));
                                InventoryApp.sales.get(index).setId(id);
                                InventoryApp.sales.get(index).setWeight(weight);
                                InventoryApp.sales.get(index).setSaleSum(saleSum);
                                InventoryApp.sales.get(index).setDays(days);
                                InventoryApp.sales.get(index).setPolish(!swEditSalePolish.isChecked());

                                Calendar addedDays = Calendar.getInstance();
                                addedDays.setTime(InventoryApp.sales.get(index).getSaleDate());
                                addedDays.add(Calendar.DATE, days);
                                Date now = new Date();
                                InventoryApp.sales.get(index).setPayDate(addedDays.getTime());
                                if (now.after(addedDays.getTime())) {
                                    InventoryApp.sales.get(index).setPaid(true);
                                }

                                InventoryApp.sales.get(index).setPrice(saleSum / weight);
                                showProgress(true);
                                Backendless.Persistence.save(InventoryApp.sales.get(index), new AsyncCallback<Sale>() {
                                    @Override
                                    public void handleResponse(Sale response) {
                                        showProgress(false);
                                        Toast.makeText(EditSale.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finishActivity(1);
                                        EditSale.this.finish();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(EditSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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