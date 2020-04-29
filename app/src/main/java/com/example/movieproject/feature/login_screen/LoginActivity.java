package com.example.movieproject.feature.login_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieproject.R;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.login_screen.contract.LoginPresenter;
import com.example.movieproject.feature.login_screen.contract.LoginView;
import com.example.movieproject.feature.main_screen.MainActivity;
import com.example.movieproject.feature.register_screen.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView, View.OnClickListener {

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tilPhone)
    TextInputLayout tilPhone;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;

    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (presenter.checkLoggedIn()) {
            presenter.goToIntent(activity, MainActivity.class, true);
        }

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void showLoading() {
        progressDialog.showProgress(this, false);
    }

    @Override
    public void hideLoading() {
        progressDialog.hideProgress();
    }

    @Override
    public void setErrorFocused(int widget) {
        if (widget == 1) {
            tilPhone.setError(getString(R.string.msg_enter_phone_num));
            etPhoneNumber.requestFocus();
            return;
        } else if (widget == 2) {
            tilPhone.setError(getString(R.string.msg_phone_range));
            etPhoneNumber.requestFocus();
            return;
        } else if (widget == 3) {
            tilPhone.setError(getString(R.string.msg_phone_valid_number));
            etPhoneNumber.requestFocus();
            return;
        } else {
            tilPhone.setError(null);
        }

        if (widget == 4) {
            tilPassword.setError(getString(R.string.msg_enter_password));
            etPassword.requestFocus();
        } else {
            tilPassword.setError(null);
        }
    }

    @Override
    public void moveToIntent(Intent intent, boolean isFinished) {
        startActivity(intent);
        if (isFinished) {
            finish();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvRegister) {
            presenter.goToIntent(activity, RegisterActivity.class, false);
        } else if (view.getId() == R.id.btnLogin) {
            String phone = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            presenter.validateData(phone, password, activity, MainActivity.class);
        }
    }
}
