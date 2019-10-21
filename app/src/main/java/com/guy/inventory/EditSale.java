package com.guy.inventory;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    LinearLayout llSaleEdit, llSaleDetails;
    ImageView ivSaleDelete, ivSaleEdit, ivSalePaid, ivSaleDetails;
    DatePicker dpSaleEditDate;
    EditText etSaleEditCompany, etSaleEditID, etSaleEditWeight, etSaleEditSum, etSaleEditDays;
    TextView tvSaleEditCompany, tvSaleEditID, tvSaleEditWeight, tvSaleEditSum, tvSaleEditDays;
    Button btnSaleEditSubmit;

    int index, days;
    String company, id;
    double weight, saleSum;
    boolean toast = false, edit = false, details = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sale);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivSaleDelete = findViewById(R.id.ivSaleDelete);
        ivSaleEdit = findViewById(R.id.ivSaleEdit);
        ivSalePaid = findViewById(R.id.ivSalePaid);
        ivSaleDetails = findViewById(R.id.ivSaleDetails);

        llSaleEdit = findViewById(R.id.llSaleEdit);
        llSaleDetails = findViewById(R.id.llSaleDetails);

        dpSaleEditDate = findViewById(R.id.dpSaleEditDate);
        etSaleEditCompany = findViewById(R.id.etSaleEditCompany);
        etSaleEditID = findViewById(R.id.etSaleEditID);
        etSaleEditWeight = findViewById(R.id.etSaleEditWeight);
        etSaleEditSum = findViewById(R.id.etSaleEditSum);
        etSaleEditDays = findViewById(R.id.etSaleEditDays);
        btnSaleEditSubmit = findViewById(R.id.btnSaleEditSubmit);

        tvSaleEditCompany = findViewById(R.id.tvSaleEditCompany);
        tvSaleEditID = findViewById(R.id.tvSaleEditID);
        tvSaleEditWeight = findViewById(R.id.tvSaleEditWeight);
        tvSaleEditSum = findViewById(R.id.tvSaleEditSum);
        tvSaleEditDays = findViewById(R.id.tvSaleEditDays);

        llSaleEdit.setVisibility(View.GONE);
        llSaleDetails.setVisibility(View.VISIBLE);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.sales.get(index).isPaid()) {
            ivSalePaid.setImageResource(R.drawable.full_dollar);
        } else {
            ivSalePaid.setImageResource(R.drawable.empty_dollar);
        }
        etSaleEditCompany.setText(InventoryApp.sales.get(index).getCompany());
        etSaleEditID.setText(InventoryApp.sales.get(index).getId());
        etSaleEditWeight.setText(String.valueOf(InventoryApp.sales.get(index).getWeight()));
        etSaleEditSum.setText(String.valueOf(InventoryApp.sales.get(index).getSaleSum()));
        etSaleEditDays.setText(String.valueOf(InventoryApp.sales.get(index).getDays()));

        Calendar date = Calendar.getInstance();
        date.setTime(InventoryApp.sales.get(index).getSaleDate());
        dpSaleEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        ivSaleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details = !details;
                edit = !edit;
                if (details) {
                    llSaleDetails.setVisibility(View.VISIBLE);
                    llSaleEdit.setVisibility(View.GONE);
                } else {
                    llSaleDetails.setVisibility(View.GONE);
                }
            }
        });

        ivSaleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = !edit;
                details = !details;
                if (edit) {
                    llSaleEdit.setVisibility(View.VISIBLE);
                    llSaleDetails.setVisibility(View.GONE);
                } else {
                    llSaleEdit.setVisibility(View.GONE);
                }
            }
        });

        ivSalePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSale.this);
                alert.setTitle("התראת שינוי");
                if (InventoryApp.sales.get(index).isPaid()) {
                    alert.setMessage("האם אתה בטוח שברצונך לבטל את קבלת התשלום על המכירה?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לסמן את המכירה כשולמה?");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.sales.get(index).isPaid()) {
                            InventoryApp.sales.get(index).setPaid(false);
                        } else {
                            InventoryApp.sales.get(index).setPaid(true);
                        }
                        showProgress(true);
                        tvLoad.setText("מעדכן את הנתונים...");
                        Backendless.Persistence.save(InventoryApp.sales.get(index), new AsyncCallback<Sale>() {
                            @Override
                            public void handleResponse(Sale response) {
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
                });
                alert.show();
            }
        });

        ivSaleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSale.this);
                alert.setTitle("התראת מחיקה");
                alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("מוחק את הנתונים אנא המתן...");
                        Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                showProgress(false);
                                Toast.makeText(EditSale.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                finishActivity(1);
                                setResult(RESULT_OK);
                                EditSale.this.finish();
                            }
                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        }
                    });
                    alert.show();
                }
        });

        btnSaleEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSaleEditCompany.getText().toString().isEmpty() || etSaleEditID.getText().toString().isEmpty() ||
                        etSaleEditWeight.getText().toString().isEmpty()|| etSaleEditSum.getText().toString().isEmpty()||
                    etSaleEditDays.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    company = etSaleEditCompany.getText().toString().trim();
                    id = etSaleEditID.getText().toString().trim();
                    weight = Double.parseDouble(etSaleEditWeight.getText().toString().trim());
                    saleSum = Double.parseDouble(etSaleEditSum.getText().toString().trim());
                    days = Integer.valueOf(etSaleEditDays.getText().toString().trim());
                }

                if (toast) {
                    Toast.makeText(EditSale.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditSale.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.sales.get(index).setSaleDate(getDateFromDatePicker(dpSaleEditDate));
                            InventoryApp.sales.get(index).setCompany(company);
                            InventoryApp.sales.get(index).setId(id);
                            InventoryApp.sales.get(index).setWeight(weight);
                            InventoryApp.sales.get(index).setSaleSum(saleSum);
                            InventoryApp.sales.get(index).setDays(days);

                            Calendar addedDays = Calendar.getInstance();
                            addedDays.setTime(InventoryApp.sales.get(index).getSaleDate());
                            addedDays.add(Calendar.DATE, days);
                            Date now = new Date();
                            InventoryApp.sales.get(index).setPayDate(addedDays.getTime());
                            if (now.after(addedDays.getTime())) {
                                InventoryApp.sales.get(index).setPaid(true);
                            }

                            InventoryApp.sales.get(index).setPrice(saleSum/weight);
                            Backendless.Persistence.save(InventoryApp.sales.get(index), new AsyncCallback<Sale>() {
                                @Override
                                public void handleResponse(Sale response) {
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
                    });
                        alert.show();
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