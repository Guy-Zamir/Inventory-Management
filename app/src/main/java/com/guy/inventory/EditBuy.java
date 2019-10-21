package com.guy.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Calendar;
import java.util.Date;

public class EditBuy extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ImageView ivBuyEditDelete, ivBuyEdit, ivBuyEditPaid, ivBuyEditDone;
    DatePicker dpBuyEditDate;
    EditText etBuyEditSupplier, etBuyEditID, etBuyEditPrice, etBuyEditWeight, etBuyEditDays, etBuyEditDoneWeight, etBuyEditWage;
    TextView tvBuyEditSupplier, tvBuyEditID, tvBuyEditPrice, tvBuyEditWeight, tvBuyEditDays, tvBuyEditDoneWeight, tvBuyEditWage;
    Button btnBuyEditSubmit;

    int index, days;
    String supplier, id;
    double weight, doneWeight, wage, price;
    boolean toast = false ,edit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivBuyEditDelete = findViewById(R.id.ivBuyEditDelete);
        ivBuyEdit = findViewById(R.id.ivBuyEdit);
        ivBuyEditPaid = findViewById(R.id.ivBuyEditPaid);
        ivBuyEditDone = findViewById(R.id.ivBuyEditDone);

        dpBuyEditDate = findViewById(R.id.dpBuyEditDate);
        etBuyEditSupplier = findViewById(R.id.etBuyEditSupplier);
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
        tvBuyEditDoneWeight = findViewById(R.id.tvBuyEditDoneWeight);
        tvBuyEditWage = findViewById(R.id.tvBuyEditWage);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.buys.get(index).isPaid()) {
            ivBuyEditPaid.setImageResource(R.drawable.full_dollar);
        } else {
            ivBuyEditPaid.setImageResource(R.drawable.empty_dollar);
        }

        if (InventoryApp.buys.get(index).isDone()) {
            etBuyEditDoneWeight.setVisibility(View.VISIBLE);
            tvBuyEditDoneWeight.setVisibility(View.VISIBLE);
            etBuyEditWage.setVisibility(View.VISIBLE);
            tvBuyEditWage.setVisibility(View.VISIBLE);
            ivBuyEditDone.setImageResource(R.drawable.done_icon);
        } else {
            etBuyEditDoneWeight.setVisibility(View.GONE);
            tvBuyEditDoneWeight.setVisibility(View.GONE);
            etBuyEditWage.setVisibility(View.GONE);
            tvBuyEditWage.setVisibility(View.GONE);
            ivBuyEditDone.setImageResource(R.drawable.not_done_icon);
        }

        etBuyEditSupplier.setText(InventoryApp.buys.get(index).getSupplier());
        etBuyEditID.setText(InventoryApp.buys.get(index).getId());
        etBuyEditPrice.setText(String.valueOf(InventoryApp.buys.get(index).getPrice()));
        etBuyEditWeight.setText(String.valueOf(InventoryApp.buys.get(index).getWeight()));
        etBuyEditDays.setText(String.valueOf(InventoryApp.buys.get(index).getDays()));
        etBuyEditDoneWeight.setText(String.valueOf(InventoryApp.buys.get(index).getDoneWeight()));
        etBuyEditWage.setText(String.valueOf(InventoryApp.buys.get(index).getWage()));

        Calendar date = Calendar.getInstance();
        date.setTime(InventoryApp.buys.get(index).getBuyDate());
        dpBuyEditDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        ivBuyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = !edit;
                if (edit) {
                    dpBuyEditDate.setVisibility(View.VISIBLE);
                    etBuyEditSupplier.setVisibility(View.VISIBLE);
                    etBuyEditID.setVisibility(View.VISIBLE);
                    etBuyEditPrice.setVisibility(View.VISIBLE);
                    etBuyEditWeight.setVisibility(View.VISIBLE);
                    etBuyEditDays.setVisibility(View.VISIBLE);
                    btnBuyEditSubmit.setVisibility(View.VISIBLE);
                    tvBuyEditSupplier.setVisibility(View.VISIBLE);
                    tvBuyEditID.setVisibility(View.VISIBLE);
                    tvBuyEditPrice.setVisibility(View.VISIBLE);
                    tvBuyEditWeight.setVisibility(View.VISIBLE);
                    tvBuyEditDays.setVisibility(View.VISIBLE);
                    if (InventoryApp.buys.get(index).isDone()) {
                        tvBuyEditDoneWeight.setVisibility(View.VISIBLE);
                        tvBuyEditWage.setVisibility(View.VISIBLE);
                        etBuyEditDoneWeight.setVisibility(View.VISIBLE);
                        etBuyEditWage.setVisibility(View.VISIBLE);
                    }
                } else {
                    dpBuyEditDate.setVisibility(View.GONE);
                    etBuyEditSupplier.setVisibility(View.GONE);
                    etBuyEditID.setVisibility(View.GONE);
                    etBuyEditPrice.setVisibility(View.GONE);
                    etBuyEditWeight.setVisibility(View.GONE);
                    etBuyEditDays.setVisibility(View.GONE);
                    btnBuyEditSubmit.setVisibility(View.GONE);
                    tvBuyEditSupplier.setVisibility(View.GONE);
                    tvBuyEditID.setVisibility(View.GONE);
                    tvBuyEditPrice.setVisibility(View.GONE);
                    tvBuyEditWeight.setVisibility(View.GONE);
                    tvBuyEditDays.setVisibility(View.GONE);
                    if (InventoryApp.buys.get(index).isDone()) {
                        tvBuyEditDoneWeight.setVisibility(View.GONE);
                        tvBuyEditWage.setVisibility(View.GONE);
                        etBuyEditDoneWeight.setVisibility(View.GONE);
                        etBuyEditWage.setVisibility(View.GONE);
                    }
                }
            }
        });

        ivBuyEditPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditBuy.this);
                alert.setTitle("התראת שינוי");
                if (InventoryApp.buys.get(index).isPaid()) {
                    alert.setMessage("נאם אתה בטוח שברצונך לבטל את קבלת התשלום על המכירה?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לסמן את המכירה כשולמה?");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.buys.get(index).isPaid()) {
                            InventoryApp.buys.get(index).setPaid(false);
                        } else {
                            InventoryApp.buys.get(index).setPaid(true);
                        }
                        showProgress(true);
                        tvLoad.setText("מעדכן את הנתונים...");
                        Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                            @Override
                            public void handleResponse(Buy response) {
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
        });

        ivBuyEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditBuy.this);
                alert.setTitle("התראת מחיקה");
                alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("מוחק את הנתונים אנא המתן...");
                        Backendless.Persistence.of(Buy.class).remove(InventoryApp.buys.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                showProgress(false);
                                Toast.makeText(EditBuy.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                finishActivity(1);
                                setResult(RESULT_OK);
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
        });

        ivBuyEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditBuy.this);
                alert.setTitle("התראת שינוי");
                if (InventoryApp.buys.get(index).isDone()) {
                    alert.setMessage("האם אתה בטוח שברצונך לסמן את החבילה כלא גמורה?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לסמן את החבילה כגמורה??");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.buys.get(index).isDone()) {
                            InventoryApp.buys.get(index).setDone(false);
                        } else {
                            InventoryApp.buys.get(index).setDone(true);
                        }
                        showProgress(true);
                        tvLoad.setText("מעדכן את הנתונים...");
                        Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                            @Override
                            public void handleResponse(Buy response) {
                                showProgress(false);
                                Toast.makeText(EditBuy.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                if (InventoryApp.buys.get(index).isDone()) {
                                    etBuyEditDoneWeight.setVisibility(View.VISIBLE);
                                    tvBuyEditDoneWeight.setVisibility(View.VISIBLE);
                                    etBuyEditWage.setVisibility(View.VISIBLE);
                                    tvBuyEditWage.setVisibility(View.VISIBLE);
                                    ivBuyEditDone.setImageResource(R.drawable.done_icon);
                                } else {
                                    etBuyEditDoneWeight.setVisibility(View.GONE);
                                    tvBuyEditDoneWeight.setVisibility(View.GONE);
                                    etBuyEditWage.setVisibility(View.GONE);
                                    tvBuyEditWage.setVisibility(View.GONE);
                                    ivBuyEditDone.setImageResource(R.drawable.not_done_icon);
                                }
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
        });

        btnBuyEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!InventoryApp.buys.get(index).isDone()) {
                    if (etBuyEditSupplier.getText().toString().isEmpty() || etBuyEditID.getText().toString().isEmpty() ||
                        etBuyEditWeight.getText().toString().isEmpty()|| etBuyEditPrice.getText().toString().isEmpty()||
                        etBuyEditDays.getText().toString().isEmpty()) {
                        toast = true;
                    }
                } else if (etBuyEditWage.getText().toString().isEmpty() || etBuyEditDoneWeight.getText().toString().isEmpty() ||
                        etBuyEditSupplier.getText().toString().isEmpty() || etBuyEditID.getText().toString().isEmpty() ||
                        etBuyEditWeight.getText().toString().isEmpty()|| etBuyEditPrice.getText().toString().isEmpty()||
                        etBuyEditDays.getText().toString().isEmpty()) {
                    toast = true;
                }

                supplier = etBuyEditSupplier.getText().toString().trim();
                id = etBuyEditID.getText().toString().trim();
                weight = Double.parseDouble(etBuyEditWeight.getText().toString().trim());
                price = Double.parseDouble(etBuyEditPrice.getText().toString().trim());
                days = Integer.valueOf(etBuyEditDays.getText().toString().trim());
                wage = Double.parseDouble(etBuyEditWage.getText().toString().trim());
                doneWeight = Double.parseDouble(etBuyEditDoneWeight.getText().toString().trim());

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
                            InventoryApp.buys.get(index).setSupplier(supplier);
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
                            Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                                @Override
                                public void handleResponse(Buy response) {
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
