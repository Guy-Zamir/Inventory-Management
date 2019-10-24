package com.guy.inventory.Activities.EditActivities;

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
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Buy;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class EditBuy extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llBuyEdit, llBuyDone, llBuyDetails, llBuyDetailsDone;
    ImageView ivBuyEditDelete, ivBuyEdit, ivBuyEditPaid, ivBuyEditDone, ivBuyDetails;
    DatePicker dpBuyEditDate;
    EditText etBuyEditID, etBuyEditPrice, etBuyEditWeight, etBuyEditDays, etBuyEditDoneWeight, etBuyEditWage;
    TextView tvBuyEditSupplier, tvBuyEditID, tvBuyEditPrice, tvBuyEditWeight, tvBuyEditDays, tvBuyEditDoneWeight, tvBuyEditWage;
    TextView tvBuyDetailsSupplier, tvBuyDetailsBuyDate, tvBuyDetailsPayDate, tvBuyDetailsID,
            tvBuyDetailsPrice, tvBuyDetailsWeight, tvBuyDetailsDays, tvBuyDetailsSum, tvBuyDetailsDoneWeight, tvBuyDetailsWage, tvBuyDetailsWorkDe;
    Button btnBuyEditSubmit;

    int index, days;
    String  id;
    double weight, doneWeight, wage, price;
    boolean toast = false ,edit = false, details = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        llBuyEdit = findViewById(R.id.llBuyEdit);
        llBuyDone = findViewById(R.id.llBuyDone);
        llBuyDetails = findViewById(R.id.llBuyDetails);
        llBuyDetailsDone = findViewById(R.id.llBuyDetailsDone);

        ivBuyEditDelete = findViewById(R.id.ivBuyEditDelete);
        ivBuyEdit = findViewById(R.id.ivBuyEdit);
        ivBuyEditPaid = findViewById(R.id.ivBuyEditPaid);
        ivBuyEditDone = findViewById(R.id.ivBuyEditDone);
        ivBuyDetails = findViewById(R.id.ivBuyDetails);

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
        tvBuyEditDoneWeight = findViewById(R.id.tvBuyEditDoneWeight);
        tvBuyEditWage = findViewById(R.id.tvBuyEditWage);

        tvBuyDetailsSupplier = findViewById(R.id.tvBuyDetailsSupplier);
        tvBuyDetailsBuyDate = findViewById(R.id.tvBuyDetailsBuyDate);
        tvBuyDetailsPayDate = findViewById(R.id.tvBuyDetailsPayDate);
        tvBuyDetailsID = findViewById(R.id.tvBuyDetailsID);
        tvBuyDetailsPrice = findViewById(R.id.tvBuyDetailsPrice);
        tvBuyDetailsWeight = findViewById(R.id.tvBuyDetailsWeight);
        tvBuyDetailsDays = findViewById(R.id.tvBuyDetailsDays);
        tvBuyDetailsSum = findViewById(R.id.tvBuyDetailsSum);
        tvBuyDetailsDoneWeight = findViewById(R.id.tvBuyDetailsDoneWeight);
        tvBuyDetailsWage = findViewById(R.id.tvBuyDetailsWage);
        tvBuyDetailsWorkDe = findViewById(R.id.tvBuyDetailsWorkDe);

        llBuyEdit.setVisibility(View.GONE);
        llBuyDetails.setVisibility(View.VISIBLE);

        index = getIntent().getIntExtra("index", 0);

        tvBuyEditSupplier.setText(InventoryApp.buys.get(index).getSupplierName());

        if (InventoryApp.buys.get(index).isPaid()) {
            ivBuyEditPaid.setImageResource(R.drawable.empty_dollar);
        } else {
            ivBuyEditPaid.setImageResource(R.drawable.full_dollar);
        }

        if (InventoryApp.buys.get(index).isDone()) {
            llBuyDone.setVisibility(View.VISIBLE);
            ivBuyEditDone.setImageResource(R.drawable.not_done_icon);
        } else {
            llBuyDone.setVisibility(View.GONE);
            ivBuyEditDone.setImageResource(R.drawable.done_icon);
        }
        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(InventoryApp.buys.get(index).getBuyDate());
        @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(InventoryApp.buys.get(index).getPayDate());
        @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);

        tvBuyDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth);
        tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth);

        tvBuyDetailsSupplier.setText("שם הספק:  " + InventoryApp.buys.get(index).getSupplierName());
        tvBuyDetailsID.setText("מספר אסמכתא:  " + InventoryApp.buys.get(index).getId());
        tvBuyDetailsPrice.setText("מחיר לקראט:  " + nf.format(InventoryApp.buys.get(index).getPrice()) + "$");
        tvBuyDetailsWeight.setText("משקל החבילה:  " + nf.format(InventoryApp.buys.get(index).getWeight()));
        tvBuyDetailsDays.setText("מספר ימים:  " + InventoryApp.buys.get(index).getDays());
        tvBuyDetailsSum.setText("סכום העסקה:  " + nf.format(InventoryApp.buys.get(index).getSum()) + "$");
        tvBuyDetailsDoneWeight.setText("משקל גמור:  " + nf.format(InventoryApp.buys.get(index).getDoneWeight()));
        tvBuyDetailsWage.setText("שכר עבודה:  " + nf.format(InventoryApp.buys.get(index).getWage()) + "$" + " , " +
                nf.format(InventoryApp.buys.get(index).getWage()/InventoryApp.buys.get(index).getPrice()*100) + "%" + " , " +
                nf.format(InventoryApp.buys.get(index).getWage()*InventoryApp.buys.get(index).getWeight()) + "$");
        tvBuyDetailsWorkDe.setText("אחוז ליטוש:  " + nf.format(InventoryApp.buys.get(index).getWorkDepreciation()*100) + "%");

        if (InventoryApp.buys.get(index).isPolish()) {
            llBuyDetailsDone.setVisibility(View.GONE);
        }

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
                details = !details;
                if (edit) {
                    llBuyEdit.setVisibility(View.VISIBLE);
                    llBuyDetails.setVisibility(View.GONE);
                    if (InventoryApp.buys.get(index).isDone()) {
                        llBuyDone.setVisibility(View.VISIBLE);
                    }
                } else {
                    llBuyEdit.setVisibility(View.GONE);
                    llBuyDetails.setVisibility(View.VISIBLE);
                    if (InventoryApp.buys.get(index).isDone()) {
                        llBuyDone.setVisibility(View.GONE);
                    }
                }
            }
        });

        ivBuyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details = !details;
                edit = !edit;
                if (details) {
                    llBuyDetails.setVisibility(View.VISIBLE);
                    llBuyEdit.setVisibility(View.GONE);
                } else {
                    llBuyDetails.setVisibility(View.GONE);
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
                                InventoryApp.buys.remove(index);
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
                            InventoryApp.buys.get(index).setDoneWeight(0);
                            InventoryApp.buys.get(index).setWage(0);
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
                                    llBuyDone.setVisibility(View.VISIBLE);
                                    llBuyDetailsDone.setVisibility(View.VISIBLE);
                                    ivBuyEditDone.setImageResource(R.drawable.not_done_icon);
                                } else {
                                    llBuyDone.setVisibility(View.GONE);
                                    llBuyDetailsDone.setVisibility(View.GONE);
                                    ivBuyEditDone.setImageResource(R.drawable.done_icon);
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
