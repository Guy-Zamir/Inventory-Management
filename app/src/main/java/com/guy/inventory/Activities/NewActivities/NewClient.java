package com.guy.inventory.Activities.NewActivities;

import androidx.appcompat.app.ActionBar;
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
import com.guy.inventory.Classes.Client;
import com.guy.inventory.R;

public class NewClient extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ToggleButton tbNewClientHome;
    private EditText etNewClientName, etNewClientLocation, etNewClientPhoneNumber, etNewClientInsidePhone, etNewClientFax, etNewClientWebsite, etNewClientDetails;
    private String name, location, phoneNumber, insidePhone, fax, website, details;
    private boolean home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tbNewClientHome = findViewById(R.id.tbNewClientHome);
        etNewClientName = findViewById(R.id.etNewClientName);
        etNewClientLocation = findViewById(R.id.etNewClientLocation);
        etNewClientPhoneNumber = findViewById(R.id.etNewClientPhoneNumber);
        etNewClientInsidePhone = findViewById(R.id.etNewClientInsidePhone);
        etNewClientFax = findViewById(R.id.etNewClientFax);
        etNewClientWebsite = findViewById(R.id.etNewClientWebsite);
        etNewClientDetails = findViewById(R.id.etNewClientDetails);
        Button btnNewClientSubmit = findViewById(R.id.btnNewClientSubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("לקוח חדש");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnNewClientSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNewClientName.getText().toString().isEmpty()) {
                    Toast.makeText(NewClient.this, "יש להזין את שם החברה", Toast.LENGTH_SHORT).show();
                } else {
                    name = etNewClientName.getText().toString().trim();
                    location = etNewClientLocation.getText().toString().trim();
                    phoneNumber = etNewClientPhoneNumber.getText().toString().trim();
                    insidePhone = etNewClientInsidePhone.getText().toString().trim();
                    fax = etNewClientFax.getText().toString().trim();
                    website = etNewClientWebsite.getText().toString().trim();
                    details = etNewClientDetails.getText().toString().trim();
                    home = !tbNewClientHome.isChecked();

                    final Client client = new Client();
                    client.setName(name);
                    client.setLocation(location);
                    client.setPhoneNumber(phoneNumber);
                    client.setInsidePhone(insidePhone);
                    client.setFax(fax);
                    client.setWebsite(website);
                    client.setDetails(details);
                    client.setHome(home);

                    client.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(client, new AsyncCallback<Client>() {
                        @Override
                        public void handleResponse(Client response) {
                            InventoryApp.clients.add(client);
                            Toast.makeText(NewClient.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            NewClient.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(NewClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
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
