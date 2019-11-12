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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class EditSupplier extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etSupplierEditName, etSupplierEditAddress, etSupplierEditPhone, etSupplierEditInsidePhone, etSupplierEditFax, etSupplierEditWebSite, etSupplierEditDetails;
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

        etSupplierEditName = findViewById(R.id.etSupplierEditName);
        etSupplierEditAddress = findViewById(R.id.etSupplierEditAddress);
        etSupplierEditPhone = findViewById(R.id.etSupplierEditPhone);
        etSupplierEditInsidePhone = findViewById(R.id.etSupplierEditInsidePhone);
        etSupplierEditFax = findViewById(R.id.etSupplierEditFax);
        etSupplierEditWebSite = findViewById(R.id.etSupplierEditWebSite);
        etSupplierEditDetails = findViewById(R.id.etSupplierEditDetails);


        btnSupplierEditSubmit = findViewById(R.id.btnSupplierEditSubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("נתוני ספק");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        etSupplierEditName.setText(InventoryApp.suppliers.get(index).getName());
        etSupplierEditAddress.setText(String.valueOf(InventoryApp.suppliers.get(index).getLocation()));
        etSupplierEditPhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getPhoneNumber()));
        etSupplierEditInsidePhone.setText(String.valueOf(InventoryApp.suppliers.get(index).getInsidePhone()));
        etSupplierEditFax.setText(String.valueOf(InventoryApp.suppliers.get(index).getFax()));
        etSupplierEditWebSite.setText(String.valueOf(InventoryApp.suppliers.get(index).getWebsite()));
        etSupplierEditDetails.setText(String.valueOf(InventoryApp.suppliers.get(index).getDetails()));

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

                            showProgress(true);
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
}