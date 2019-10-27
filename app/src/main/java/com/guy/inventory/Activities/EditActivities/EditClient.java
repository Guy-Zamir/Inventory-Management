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
import com.guy.inventory.Classes.Client;
import com.guy.inventory.Classes.Sale;
import com.guy.inventory.R;

import java.text.DecimalFormat;
import java.util.List;

public class EditClient extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llClientEdit, llClientDetails;
    ImageView ivClientDelete, ivClientHome, ivClientEdit, ivClientDetails;
    EditText etClientEditName, etClientEditAddress, etClientEditPhone, etClientEditInsidePhone, etClientEditFax, etClientEditWebSite, etClientEditDetails;
    TextView tvClientDetailsName, tvClientDetailsSaleSum, tvClientDetailsWeightSum, tvClientDetailsPrice, tvClientDetailsSaleAVG, tvClientDetailsWeightAVG;
    Button btnClientEditSubmit;
    int index;
    boolean details = true, edit = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        llClientDetails = findViewById(R.id.llClientDetails);
        llClientEdit = findViewById(R.id.llClientEdit);

        ivClientDelete = findViewById(R.id.ivClientDelete);
        ivClientHome = findViewById(R.id.ivClientHome);
        ivClientEdit = findViewById(R.id.ivClientEdit);
        ivClientDetails = findViewById(R.id.ivClientDetails);

        etClientEditName = findViewById(R.id.etClientEditName);
        etClientEditAddress = findViewById(R.id.etClientEditAddress);
        etClientEditPhone = findViewById(R.id.etClientEditPhone);
        etClientEditInsidePhone = findViewById(R.id.etClientEditInsidePhone);
        etClientEditFax = findViewById(R.id.etClientEditFax);
        etClientEditWebSite = findViewById(R.id.etClientEditWebSite);
        etClientEditDetails = findViewById(R.id.etClientEditDetails);

        tvClientDetailsName = findViewById(R.id.tvClientDetailsName);
        tvClientDetailsSaleSum = findViewById(R.id.tvClientDetailsSaleSum);
        tvClientDetailsWeightSum = findViewById(R.id.tvClientDetailsWeightSum);
        tvClientDetailsPrice = findViewById(R.id.tvClientDetailsPrice);
        tvClientDetailsSaleAVG = findViewById(R.id.tvClientDetailsSaleAVG);
        tvClientDetailsWeightAVG = findViewById(R.id.tvClientDetailsWeightAVG);

        btnClientEditSubmit = findViewById(R.id.btnClientEditSubmit);

        index = getIntent().getIntExtra("index", 0);

        if (InventoryApp.clients.get(index).isHome()) {
            ivClientHome.setImageResource(R.drawable.export_icon);
        } else {
            ivClientHome.setImageResource(R.drawable.home_icon);
        }

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");
        queryBuilder.setPageSize(100);

        showProgress(true);

        Backendless.Data.of(Sale.class).find(queryBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales = response;
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(EditClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

        double saleSum = 0;
        double weightSum = 0;
        double price;
//        double saleAvg;
//        double weightAvg;
        if (InventoryApp.sales != null) {
            for (Sale sale : InventoryApp.sales) {
                if (sale.getClientName().equals(InventoryApp.clients.get(index).getName())) {
                    saleSum += sale.getSaleSum();
                    weightSum += sale.getWeight();
                }
            }
        }

        price = saleSum/weightSum;

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvClientDetailsName.setText(InventoryApp.clients.get(index).getName());
        tvClientDetailsSaleSum.setText("סכום מכירות:  " + nf.format(saleSum) + "$");
        tvClientDetailsWeightSum.setText("סכום משקל: " + nf.format(weightSum));
        tvClientDetailsPrice.setText("מחיר ממוצע לקראט: " + nf.format(price) + "$");

        etClientEditName.setText(InventoryApp.clients.get(index).getName());
        etClientEditAddress.setText(String.valueOf(InventoryApp.clients.get(index).getLocation()));
        etClientEditPhone.setText(String.valueOf(InventoryApp.clients.get(index).getPhoneNumber()));
        etClientEditInsidePhone.setText(String.valueOf(InventoryApp.clients.get(index).getInsidePhone()));
        etClientEditFax.setText(String.valueOf(InventoryApp.clients.get(index).getFax()));
        etClientEditWebSite.setText(String.valueOf(InventoryApp.clients.get(index).getWebsite()));
        etClientEditDetails.setText(String.valueOf(InventoryApp.clients.get(index).getDetails()));

        ivClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!details) {
                    llClientDetails.setVisibility(View.VISIBLE);
                    llClientEdit.setVisibility(View.GONE);
                    details = true;
                    edit = false;
                }
            }
        });

        ivClientEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    llClientEdit.setVisibility(View.VISIBLE);
                    llClientDetails.setVisibility(View.GONE);
                    edit = true;
                    details = false;
                }
            }
        });


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
                if (etClientEditName.getText().toString().isEmpty()) {
                    Toast.makeText(EditClient.this, "יש למלא את שם הלקוח", Toast.LENGTH_SHORT).show();
                } else {
                    final String name = etClientEditName.getText().toString().trim();
                    final String location = etClientEditAddress.getText().toString().trim();
                    final String phone = etClientEditPhone.getText().toString().trim();
                    final String insidePhone = etClientEditInsidePhone.getText().toString().trim();
                    final String fax = etClientEditFax.getText().toString().trim();
                    final String webSite = etClientEditWebSite.getText().toString().trim();
                    final String details = etClientEditDetails.getText().toString().trim();

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