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
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class EditClient extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etClientEditAddress, etClientEditPhone, etClientEditInsidePhone, etClientEditFax, etClientEditWebSite, etClientEditDetails;
    TextView tvClientEditName;
    Button btnClientEditSubmit;
    int index;
    boolean client;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvClientEditName = findViewById(R.id.tvClientEditName);
        etClientEditAddress = findViewById(R.id.etClientEditAddress);
        etClientEditPhone = findViewById(R.id.etClientEditPhone);
        etClientEditInsidePhone = findViewById(R.id.etClientEditInsidePhone);
        etClientEditFax = findViewById(R.id.etClientEditFax);
        etClientEditWebSite = findViewById(R.id.etClientEditWebSite);
        etClientEditDetails = findViewById(R.id.etClientEditDetails);

        btnClientEditSubmit = findViewById(R.id.btnClientEditSubmit);

        client = getIntent().getBooleanExtra("client", true);
        index = getIntent().getIntExtra("index", 0);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if (client) {
            actionBar.setTitle("נתוני לקוח");
        } else {
            actionBar.setTitle("נתוני ספק");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvClientEditName.setText(InventoryApp.clients.get(index).getName());
        etClientEditAddress.setText(String.valueOf(InventoryApp.clients.get(index).getLocation()));
        etClientEditPhone.setText(String.valueOf(InventoryApp.clients.get(index).getPhoneNumber()));
        etClientEditInsidePhone.setText(String.valueOf(InventoryApp.clients.get(index).getInsidePhone()));
        etClientEditFax.setText(String.valueOf(InventoryApp.clients.get(index).getFax()));
        etClientEditWebSite.setText(String.valueOf(InventoryApp.clients.get(index).getWebsite()));
        etClientEditDetails.setText(String.valueOf(InventoryApp.clients.get(index).getDetails()));

        btnClientEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            InventoryApp.clients.get(index).setLocation(location);
                            InventoryApp.clients.get(index).setPhoneNumber(phone);
                            InventoryApp.clients.get(index).setInsidePhone(insidePhone);
                            InventoryApp.clients.get(index).setFax(fax);
                            InventoryApp.clients.get(index).setWebsite(webSite);
                            InventoryApp.clients.get(index).setDetails(details);

                            showProgress(true);
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