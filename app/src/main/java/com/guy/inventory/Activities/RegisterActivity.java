package com.guy.inventory.Activities;

import androidx.appcompat.app.AppCompatActivity;
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
import com.guy.inventory.R;

public class RegisterActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etName, etEmail, etPassword, etRePassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        etEmail = findViewById(R.id.etRegisterEmail);
        etName = findViewById(R.id.etRegisterName);
        etPassword = findViewById(R.id.etRegisterPassword);
        etRePassword = findViewById(R.id.etRegisterRePassword);
        btnRegister = findViewById(R.id.btnRegisterSubmit);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                etPassword.getText().toString().isEmpty() || etRePassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "יש להזין את כל הנתונים", Toast.LENGTH_SHORT).show();
                } else {
                    if (etPassword.getText().toString().trim().equals(etRePassword.getText().toString().trim())) {
                        String name = etName.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        String eMail = etEmail.getText().toString().trim();

                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(eMail);
                        user.setPassword(password);
                        user.setProperty("name", name);

                        showProgress(true);
                        tvLoad.setText("רושם אותך, אנא המתן...");
                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                Toast.makeText(RegisterActivity.this, "משתמש נרשם בהצלחה", Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(RegisterActivity.this, fault.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "וודא שאישור הסיסמה זהה לסיסמה", Toast.LENGTH_SHORT).show();
                    }
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
