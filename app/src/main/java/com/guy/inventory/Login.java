package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    TextView tvReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvReset = findViewById(R.id.tvReset);
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnLoginRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "יש להזין את כל הנתונים", Toast.LENGTH_SHORT).show();
                } else {

                    String name = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    showProgress(true);

                    Backendless.UserService.login(name, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            InventoryApp.user = response;
                            startActivity(new Intent(Login.this, MainActivity.class));
                            Login.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, true);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "יש להזין את כתובת הדואר האלקטרוני", Toast.LENGTH_SHORT).show();
                } else {
                    String name = etEmail.getText().toString().trim();
                    showProgress(false);

                    Backendless.UserService.restorePassword(name, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(Login.this, "איפוס סיסמה שנלח לכתובת הדואר האלקטרוני", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });

        showProgress(true);
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if (response) {
                    String objectUserId = UserIdStorageFactory.instance().getStorage().get();
                    Backendless.Data.of(BackendlessUser.class).findById(objectUserId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            showProgress(false);
                            etEmail.setText(response.getEmail());
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });

                } else {
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
