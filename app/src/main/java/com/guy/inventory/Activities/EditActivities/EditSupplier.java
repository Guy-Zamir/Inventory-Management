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
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;

public class EditSupplier extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ImageView ivSupplierDelete, ivSupplierHome, ivSupplierEdit, ivSupplierDetails;
    EditText etSupplierDetailsName, etSupplierDetailsAddress, etSupplierDetailsPhone, etSupplierDetailsInsidePhone, etSupplierDetailsFax, etSupplierDetailsWebSite, etSupplierDetailsDetails;
    Button btnSupplierEditSubmit;
    int index;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivSupplierDelete = findViewById(R.id.ivSupplierDelete);
        ivSupplierHome = findViewById(R.id.ivSupplierHome);
        ivSupplierEdit = findViewById(R.id.ivSupplierEdit);
        ivSupplierDetails = findViewById(R.id.ivSupplierDetails);

        etSupplierDetailsName = findViewById(R.id.etSupplierDetailsName);
        etSupplierDetailsAddress = findViewById(R.id.etSupplierDetailsAddress);
        etSupplierDetailsPhone = findViewById(R.id.etSupplierDetailsPhone);
        etSupplierDetailsInsidePhone = findViewById(R.id.etSupplierDetailsInsidePhone);
        etSupplierDetailsFax = findViewById(R.id.etSupplierDetailsFax);
        etSupplierDetailsWebSite = findViewById(R.id.etSupplierDetailsWebSite);
        etSupplierDetailsDetails = findViewById(R.id.etSupplierDetailsDetails);

        btnSupplierEditSubmit = findViewById(R.id.btnSupplierEditSubmit);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.suppliers.get(index).isHome()) {
            ivSupplierHome.setImageResource(R.drawable.export_icon);
        } else {
            ivSupplierHome.setImageResource(R.drawable.home_icon);
        }
        etSupplierDetailsName.setText(InventoryApp.suppliers.get(index).getName());
        etSupplierDetailsAddress.setText(String.valueOf(InventoryApp.suppliers.get(index).getLocation()));
        etSupplierDetailsPhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getPhoneNumber()));
        etSupplierDetailsInsidePhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getInsidePhone()));
        etSupplierDetailsFax.setText(String.valueOf(InventoryApp.suppliers.get(index).getFax()));
        etSupplierDetailsWebSite.setText(String.valueOf(InventoryApp.suppliers.get(index).getWebsite()));
        etSupplierDetailsDetails.setText(String.valueOf(InventoryApp.suppliers.get(index).getDetails()));


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
                if (etSupplierDetailsName.getText().toString().isEmpty()) {
                    Toast.makeText(EditSupplier.this, "יש למלא את שם הלקוח", Toast.LENGTH_SHORT).show();
                } else {
                    final String name = etSupplierDetailsName.getText().toString().trim();
                    final String location = etSupplierDetailsAddress.getText().toString().trim();
                    final String phone = etSupplierDetailsPhone.getText().toString().trim();
                    final String insidePhone = etSupplierDetailsInsidePhone.getText().toString().trim();
                    final String fax = etSupplierDetailsFax.getText().toString().trim();
                    final String webSite = etSupplierDetailsWebSite.getText().toString().trim();
                    final String details = etSupplierDetailsDetails.getText().toString().trim();

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