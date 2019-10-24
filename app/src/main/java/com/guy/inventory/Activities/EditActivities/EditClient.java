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
import com.guy.inventory.Classes.Client;
import com.guy.inventory.R;

public class EditClient extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ImageView ivClientDelete, ivClientHome, ivClientEdit, ivClientDetails;
    EditText etClientDetailsName, etClientDetailsAddress, etClientDetailsPhone, etClientDetailsInsidePhone, etClientDetailsFax, etClientDetailsWebSite, etClientDetailsDetails;
    Button btnClientEditSubmit;
    int index;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivClientDelete = findViewById(R.id.ivClientDelete);
        ivClientHome = findViewById(R.id.ivClientHome);
        ivClientEdit = findViewById(R.id.ivClientEdit);
        ivClientDetails = findViewById(R.id.ivClientDetails);

        etClientDetailsName = findViewById(R.id.etClientDetailsName);
        etClientDetailsAddress = findViewById(R.id.etClientDetailsAddress);
        etClientDetailsPhone = findViewById(R.id.etClientDetailsPhone);
        etClientDetailsInsidePhone = findViewById(R.id.etClientDetailsInsidePhone);
        etClientDetailsFax = findViewById(R.id.etClientDetailsFax);
        etClientDetailsWebSite = findViewById(R.id.etClientDetailsWebSite);
        etClientDetailsDetails = findViewById(R.id.etClientDetailsDetails);

        btnClientEditSubmit = findViewById(R.id.btnClientEditSubmit);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.clients.get(index).isHome()) {
            ivClientHome.setImageResource(R.drawable.export_icon);
        } else {
            ivClientHome.setImageResource(R.drawable.home_icon);
        }
        etClientDetailsName.setText(InventoryApp.clients.get(index).getName());
        etClientDetailsAddress.setText(String.valueOf(InventoryApp.clients.get(index).getLocation()));
        etClientDetailsPhone.setText(String.valueOf(InventoryApp.clients.get(index).getPhoneNumber()));
        etClientDetailsInsidePhone.setText(String.valueOf(InventoryApp.clients.get(index).getInsidePhone()));
        etClientDetailsFax.setText(String.valueOf(InventoryApp.clients.get(index).getFax()));
        etClientDetailsWebSite.setText(String.valueOf(InventoryApp.clients.get(index).getWebsite()));
        etClientDetailsDetails.setText(String.valueOf(InventoryApp.clients.get(index).getDetails()));


        ivClientHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditClient.this);
                alert.setTitle("התראת שינוי");
                if (InventoryApp.clients.get(index).isHome()) {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הלקוח ליצוא?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הלקוח למקומי?");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.clients.get(index).isHome()) {
                            InventoryApp.clients.get(index).setHome(false);
                        } else {
                            InventoryApp.clients.get(index).setHome(true);
                        }
                        showProgress(true);
                        tvLoad.setText("מעדכן את הנתונים...");
                        Backendless.Persistence.save(InventoryApp.clients.get(index), new AsyncCallback<Client>() {
                            @Override
                            public void handleResponse(Client response) {
                                Toast.makeText(EditClient.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                EditClient.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
            }
        });

        ivClientDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditClient.this);
                alert.setTitle("התראת מחיקה");
                alert.setMessage("האם אתה בטוח שברצונך למחוק את הלקוח המסומן?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("מוחק את הנתונים אנא המתן...");
                        Backendless.Persistence.of(Client.class).remove(InventoryApp.clients.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                showProgress(false);
                                InventoryApp.clients.remove(index);
                                Toast.makeText(EditClient.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                finishActivity(1);
                                setResult(RESULT_OK);
                                EditClient.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
            }
        });

        btnClientEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etClientDetailsName.getText().toString().isEmpty()) {
                    Toast.makeText(EditClient.this, "יש למלא את שם הלקוח", Toast.LENGTH_SHORT).show();
                } else {
                    final String name = etClientDetailsName.getText().toString().trim();
                    final String location = etClientDetailsAddress.getText().toString().trim();
                    final String phone = etClientDetailsPhone.getText().toString().trim();
                    final String insidePhone = etClientDetailsInsidePhone.getText().toString().trim();
                    final String fax = etClientDetailsFax.getText().toString().trim();
                    final String webSite = etClientDetailsWebSite.getText().toString().trim();
                    final String details = etClientDetailsDetails.getText().toString().trim();

                    AlertDialog.Builder alert = new AlertDialog.Builder(EditClient.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.clients.get(index).setName(name);
                            InventoryApp.clients.get(index).setLocation(location);
                            InventoryApp.clients.get(index).setPhoneNumber(phone);
                            InventoryApp.clients.get(index).setInsidePhone(insidePhone);
                            InventoryApp.clients.get(index).setFax(fax);
                            InventoryApp.clients.get(index).setWebsite(webSite);
                            InventoryApp.clients.get(index).setDetails(details);
                            Backendless.Persistence.save(InventoryApp.clients.get(index), new AsyncCallback<Client>() {
                                @Override
                                public void handleResponse(Client response) {
                                    Toast.makeText(EditClient.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    EditClient.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(EditClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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