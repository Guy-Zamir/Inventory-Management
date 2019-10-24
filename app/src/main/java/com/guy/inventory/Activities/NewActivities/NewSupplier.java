package com.guy.inventory.Activities.NewActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;

public class NewSupplier extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ToggleButton tbNewSupplierHome;
    private EditText etNewSupplierName, etNewSupplierLocation, etNewSupplierPhoneNumber, etNewSupplierInsidePhone, etNewSupplierFax, etNewSupplierWebsite, etNewSupplierDetails;
    private String name, location, phoneNumber, insidePhone, fax, website, details;
    private boolean home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_supplier);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tbNewSupplierHome = findViewById(R.id.tbNewSupplierHome);
        etNewSupplierName = findViewById(R.id.etNewSupplierName);
        etNewSupplierLocation = findViewById(R.id.etNewSupplierLocation);
        etNewSupplierPhoneNumber = findViewById(R.id.etNewSupplierPhoneNumber);
        etNewSupplierInsidePhone = findViewById(R.id.etNewSupplierInsidePhone);
        etNewSupplierFax = findViewById(R.id.etNewSupplierFax);
        etNewSupplierWebsite = findViewById(R.id.etNewSupplierWebsite);
        etNewSupplierDetails = findViewById(R.id.etNewSupplierDetails);
        Button btnNewSupplierSubmit = findViewById(R.id.btnNewSupplierSubmit);

        btnNewSupplierSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNewSupplierName.getText().toString().isEmpty()) {
                    Toast.makeText(NewSupplier.this, "יש להזין את שם החברה", Toast.LENGTH_SHORT).show();
                } else {
                    name = etNewSupplierName.getText().toString().trim();
                    location = etNewSupplierLocation.getText().toString().trim();
                    phoneNumber = etNewSupplierPhoneNumber.getText().toString().trim();
                    insidePhone = etNewSupplierInsidePhone.getText().toString().trim();
                    fax = etNewSupplierFax.getText().toString().trim();
                    website = etNewSupplierWebsite.getText().toString().trim();
                    details = etNewSupplierDetails.getText().toString().trim();
                    home = !tbNewSupplierHome.isChecked();

                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    supplier.setLocation(location);
                    supplier.setPhoneNumber(phoneNumber);
                    supplier.setInsidePhone(insidePhone);
                    supplier.setFax(fax);
                    supplier.setWebsite(website);
                    supplier.setDetails(details);
                    supplier.setHome(home);

                    supplier.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(supplier, new AsyncCallback<Supplier>() {
                        @Override
                        public void handleResponse(Supplier response) {
                            Toast.makeText(NewSupplier.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            NewSupplier.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(NewSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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