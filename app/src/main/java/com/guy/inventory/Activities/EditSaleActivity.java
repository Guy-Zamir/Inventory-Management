package com.guy.inventory.Activities;

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
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sale;
import java.util.Calendar;
import java.util.Date;

public class EditSaleActivity extends AppCompatActivity {
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

        tvSaleEditClientName.setText(InventoryApp.sales.get(index).getClientName());
        etSaleEditID.setText(InventoryApp.sales.get(index).getId());
        etSaleEditWeight.setText(String.valueOf(InventoryApp.sales.get(index).getWeight()));
        etSaleEditSum.setText(String.valueOf(InventoryApp.sales.get(index).getSaleSum()));
        etSaleEditDays.setText(String.valueOf(InventoryApp.sales.get(index).getDays()));
        Calendar date = Calendar.getInstance();
        date.setTime(InventoryApp.sales.get(index).getSaleDate());
        dpSaleEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        swEditSalePolish.setChecked(!InventoryApp.sales.get(index).isPolish());
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
                    Toast.makeText(EditSaleActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();

                } else {

                    id = etSaleEditID.getText().toString().trim();
                    weight = Double.parseDouble(etSaleEditWeight.getText().toString().trim());
                    saleSum = Double.parseDouble(etSaleEditSum.getText().toString().trim());
                    days = Integer.valueOf(etSaleEditDays.getText().toString().trim());

                    AlertDialog.Builder alert = new AlertDialog.Builder(EditSaleActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
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
                                    Toast.makeText(EditSaleActivity.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    EditSaleActivity.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(EditSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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