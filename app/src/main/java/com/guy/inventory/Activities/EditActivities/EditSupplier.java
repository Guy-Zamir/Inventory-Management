package com.guy.inventory.Activities.EditActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Classes.Buy;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;
import java.text.DecimalFormat;
import java.util.List;

public class EditSupplier extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llSupplierEdit, llSupplierDetails;
    ImageView ivSupplierDelete, ivSupplierHome, ivSupplierEdit, ivSupplierDetails;
    EditText etSupplierEditName, etSupplierEditAddress, etSupplierEditPhone, etSupplierEditInsidePhone, etSupplierEditFax, etSupplierEditWebSite, etSupplierEditDetails;
    TextView tvSupplierDetailsName, tvSupplierDetailsSaleSum, tvSupplierDetailsWeightSum, tvSupplierDetailsPrice, tvSupplierDetailsSaleAVG, tvSupplierDetailsWeightAVG;
    Button btnSupplierEditSubmit;
    int index;
    boolean details = true, edit = false;

    List<Buy> buys;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        llSupplierEdit = findViewById(R.id.llSupplierEdit);
        llSupplierDetails = findViewById(R.id.llSupplierDetails);

        ivSupplierDelete = findViewById(R.id.ivSupplierDelete);
        ivSupplierHome = findViewById(R.id.ivSupplierHome);
        ivSupplierEdit = findViewById(R.id.ivSupplierEdit);
        ivSupplierDetails = findViewById(R.id.ivSupplierDetails);

        etSupplierEditName = findViewById(R.id.etSupplierEditName);
        etSupplierEditAddress = findViewById(R.id.etSupplierEditAddress);
        etSupplierEditPhone = findViewById(R.id.etSupplierEditPhone);
        etSupplierEditInsidePhone = findViewById(R.id.etSupplierEditInsidePhone);
        etSupplierEditFax = findViewById(R.id.etSupplierEditFax);
        etSupplierEditWebSite = findViewById(R.id.etSupplierEditWebSite);
        etSupplierEditDetails = findViewById(R.id.etSupplierEditDetails);

        tvSupplierDetailsName = findViewById(R.id.tvSupplierDetailsName);
        tvSupplierDetailsSaleSum = findViewById(R.id.tvSupplierDetailsSaleSum);
        tvSupplierDetailsWeightSum = findViewById(R.id.tvSupplierDetailsWeightSum);
        tvSupplierDetailsPrice = findViewById(R.id.tvSupplierDetailsPrice);
        tvSupplierDetailsSaleAVG = findViewById(R.id.tvSupplierDetailsSaleAVG);
        tvSupplierDetailsWeightAVG = findViewById(R.id.tvSupplierDetailsWeightAVG);

        btnSupplierEditSubmit = findViewById(R.id.btnSupplierEditSubmit);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.suppliers.get(index).isHome()) {
            ivSupplierHome.setImageResource(R.drawable.export_icon);
        } else {
            ivSupplierHome.setImageResource(R.drawable.home_icon);
        }

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");
        queryBuilder.setPageSize(100);

        showProgress(true);

        Backendless.Data.of(Buy.class).find(queryBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(EditSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

        double saleSum = 0;
        double weightSum = 0;
        double price;
//        double saleAvg;
//        double weightAvg;

        if (InventoryApp.buys != null) {
            for (Buy buy : InventoryApp.buys) {
                if (buy.getSupplierName().equals(InventoryApp.suppliers.get(index).getName())) {
                    saleSum += buy.getSum();
                    weightSum += buy.getWeight();
                }
            }
        }

        price = saleSum/weightSum;

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvSupplierDetailsName.setText(InventoryApp.suppliers.get(index).getName());
        tvSupplierDetailsSaleSum.setText("סכום קניות:  " + nf.format(saleSum) + "$");
        tvSupplierDetailsWeightSum.setText("סכום משקל: " + nf.format(weightSum));
        tvSupplierDetailsPrice.setText("מחיר ממוצע לקראט: " + nf.format(price) + "$");

        etSupplierEditName.setText(InventoryApp.suppliers.get(index).getName());
        etSupplierEditAddress.setText(String.valueOf(InventoryApp.suppliers.get(index).getLocation()));
        etSupplierEditPhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getPhoneNumber()));
        etSupplierEditInsidePhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getInsidePhone()));
        etSupplierEditFax.setText(String.valueOf(InventoryApp.suppliers.get(index).getFax()));
        etSupplierEditWebSite.setText(String.valueOf(InventoryApp.suppliers.get(index).getWebsite()));
        etSupplierEditDetails.setText(String.valueOf(InventoryApp.suppliers.get(index).getDetails()));

        ivSupplierHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSupplier.this);
                alert.setTitle("התראת שינוי");
                if (InventoryApp.suppliers.get(index).isHome()) {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הלקוח ליצוא?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הלקוח למקומי?");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.suppliers.get(index).isHome()) {
                            InventoryApp.suppliers.get(index).setHome(false);
                        } else {
                            InventoryApp.suppliers.get(index).setHome(true);
                        }
                        showProgress(true);
                        tvLoad.setText("מעדכן את הנתונים...");
                        Backendless.Persistence.save(InventoryApp.suppliers.get(index), new AsyncCallback<Supplier>() {
                            @Override
                            public void handleResponse(Supplier response) {
                                Toast.makeText(EditSupplier.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                EditSupplier.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
            }
        });

        ivSupplierDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!details) {
                    llSupplierDetails.setVisibility(View.VISIBLE);
                    llSupplierEdit.setVisibility(View.GONE);
                    details = true;
                    edit = false;
                }
            }
        });

        ivSupplierEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    llSupplierEdit.setVisibility(View.VISIBLE);
                    llSupplierDetails.setVisibility(View.GONE);
                    edit = true;
                    details = false;
                }
            }
        });

        ivSupplierDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSupplier.this);
                alert.setTitle("התראת מחיקה");
                alert.setMessage("האם אתה בטוח שברצונך למחוק את הלקוח המסומן?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("מוחק את הנתונים אנא המתן...");
                        Backendless.Persistence.of(Supplier.class).remove(InventoryApp.suppliers.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                showProgress(false);
                                InventoryApp.suppliers.remove(index);
                                Toast.makeText(EditSupplier.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                finishActivity(1);
                                setResult(RESULT_OK);
                                EditSupplier.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
            }
        });

        btnSupplierEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSupplierEditName.getText().toString().isEmpty()) {
                    Toast.makeText(EditSupplier.this, "יש למלא את שם הלקוח", Toast.LENGTH_SHORT).show();
                } else {
                    final String name = etSupplierEditName.getText().toString().trim();
                    final String location = etSupplierEditAddress.getText().toString().trim();
                    final String phone = etSupplierEditPhone.getText().toString().trim();
                    final String insidePhone = etSupplierEditInsidePhone.getText().toString().trim();
                    final String fax = etSupplierEditFax.getText().toString().trim();
                    final String webSite = etSupplierEditWebSite.getText().toString().trim();
                    final String details = etSupplierEditDetails.getText().toString().trim();

                    AlertDialog.Builder alert = new AlertDialog.Builder(EditSupplier.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.suppliers.get(index).setName(name);
                            InventoryApp.suppliers.get(index).setLocation(location);
                            InventoryApp.suppliers.get(index).setPhoneNumber(phone);
                            InventoryApp.suppliers.get(index).setInsidePhone(insidePhone);
                            InventoryApp.suppliers.get(index).setFax(fax);
                            InventoryApp.suppliers.get(index).setWebsite(webSite);
                            InventoryApp.suppliers.get(index).setDetails(details);
                            Backendless.Persistence.save(InventoryApp.suppliers.get(index), new AsyncCallback<Supplier>() {
                                @Override
                                public void handleResponse(Supplier response) {
                                    Toast.makeText(EditSupplier.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    EditSupplier.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(EditSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
}