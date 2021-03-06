package com.guy.inventory.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister, btn2019, btn2020 ,btnDemo;
    TextView tvReset;

    final String EMAIL_2019 = "zamirbit@012.net.il";
    final String EMAIL_2020 = "zamirbit20@012.net.il";
    final String EMAIL_TEST = "test@test.com";
    final String PASSWORD = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvReset = findViewById(R.id.tvReset);
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnLoginRegister);
        btn2019 = findViewById(R.id.btn2019);
        btn2020 = findViewById(R.id.btn2020);
        btnDemo = findViewById(R.id.btnDemo);

        showProgress(true);
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {

                // Always logging into 2020
                Backendless.UserService.login(EMAIL_2020, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        InventoryApp.user = response;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "יש להזין את כל הנתונים", Toast.LENGTH_SHORT).show();
                } else {

                    String name = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    showProgress(true);

                    Backendless.UserService.login(name, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            InventoryApp.user = response;
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, true);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "יש להזין את כתובת הדואר האלקטרוני", Toast.LENGTH_SHORT).show();
                } else {
                    String name = etEmail.getText().toString().trim();
                    showProgress(false);

                    Backendless.UserService.restorePassword(name, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(LoginActivity.this, "איפוס סיסמה שנלח לכתובת הדואר האלקטרוני", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });

        //showProgress(true);
        //Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
        //    @Override
        //    public void handleResponse(final Boolean response) {
        //        if (response) {
        //            String objectUserId = UserIdStorageFactory.instance().getStorage().get();
        //            Backendless.Data.of(BackendlessUser.class).findById(objectUserId, new AsyncCallback<BackendlessUser>() {
        //                @Override
        //                public void handleResponse(BackendlessUser response) {
        //                    InventoryApp.user = response;
        //                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //                    LoginActivity.this.finish();
        //                }
//
        //                @Override
        //                public void handleFault(BackendlessFault fault) {
        //                    Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
        //                    etEmail.setText(InventoryApp.user.getEmail());
        //                    showProgress(false);
        //                }
        //            });
//
        //        } else {
        //            showProgress(false);
        //        }
        //    }
//
        //    @Override
        //    public void handleFault(BackendlessFault fault) {
        //        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
        //        showProgress(false);
        //    }
        //});

        btn2019.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_2019, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });

        btn2020.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_2020, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });

        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_TEST, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
