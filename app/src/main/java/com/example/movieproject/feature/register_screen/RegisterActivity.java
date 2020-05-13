package com.example.movieproject.feature.register_screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieproject.R;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.login_screen.LoginActivity;
import com.example.movieproject.feature.register_screen.contract.RegisterPresenter;
import com.example.movieproject.feature.register_screen.contract.RegisterView;
import com.example.movieproject.models.register_form.RegisterForm;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;

public class RegisterActivity extends MvpActivity<RegisterPresenter>
        implements RegisterView, View.OnClickListener {

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.tilPhone)
    TextInputLayout tilPhone;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tilConfirmPassword)
    TextInputLayout tilConfirmPassword;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    private RegisterForm registerForm;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnConfirm) {
            String fullName = getInputText(etFullName);
            String phone = getInputText(etPhoneNumber);
            String email = getInputText(etEmail);
            String password = getInputText(etPassword);
            String confirmPassword = getInputText(etConfirmPassword);

            registerForm = new RegisterForm("", phone, fullName,
                    email, password, confirmPassword);

            presenter.validateData(registerForm);
        }
    }

    @Override
    public void setErrorFocused(int widget) {
        if (widget == 1) {
            inputError(tilPhone, getString(R.string.msg_enter_phone_num), etPhoneNumber);
            return;
        } else if (widget == 2) {
            inputError(tilPhone, getString(R.string.msg_phone_range), etPhoneNumber);
            return;
        } else if (widget == 3) {
            inputError(tilPhone, getString(R.string.msg_phone_valid_number), etPhoneNumber);
            return;
        } else {
            inputErrorNull(tilPhone);
        }

        if (widget == 4) {
            inputError(tilName, getString(R.string.msg_enter_name), etFullName);
            return;
        } else {
            inputErrorNull(tilName);
        }

        if (widget == 5) {
            inputError(tilEmail, getString(R.string.msg_enter_email), etEmail);
            return;
        } else {
            inputErrorNull(tilEmail);
        }

        if (widget == 6) {
            inputError(tilPassword, getString(R.string.msg_enter_password), etPassword);
            return;
        } else if (widget == 7) {
            inputError(tilPassword, getString(R.string.msg_minimum_password), etPassword);
            return;
        } else {
            inputErrorNull(tilPassword);
        }

        if (widget == 8) {
            inputError(tilConfirmPassword, getString(R.string.msg_confirm_password), etConfirmPassword);
        } else {
            inputErrorNull(tilConfirmPassword);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
    public void moveToIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showAlertDialog(String message, String title, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        switch (id) {
            case 1:
                builder.setCancelable(false)
                        .setMessage(message)
                        .setIcon(R.drawable.ic_check_circle_green)
                        .setPositiveButton(R.string.okay, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            presenter.goToLogin(activity, LoginActivity.class);
                        })
                        .setOnCancelListener(dialogInterface -> {
                            dialogInterface.dismiss();
                            presenter.goToLogin(activity, LoginActivity.class);
                        });
                break;
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void inputError(TextInputLayout inputLayout, String errorMsg, EditText editText) {
        inputLayout.setError(errorMsg);
        editText.requestFocus();
    }

    @Override
    public void inputErrorNull(TextInputLayout inputLayout) {
        inputLayout.setError(null);
    }

    @Override
    public String getInputText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
