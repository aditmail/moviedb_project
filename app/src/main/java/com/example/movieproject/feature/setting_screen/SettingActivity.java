package com.example.movieproject.feature.setting_screen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.movieproject.R;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.setting_screen.contract.SettingPresenter;
import com.example.movieproject.feature.setting_screen.contract.SettingView;
import com.example.movieproject.utils.PreferencesManager;

import java.util.HashMap;

import butterknife.BindView;

public class SettingActivity extends MvpActivity<SettingPresenter> implements SettingView {

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etEmail)
    EditText etEmail;

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        presenter.getUserData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_logout:
                presenter.dialogLogout();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void showDataPreferences(HashMap<String, String> user) {
        if (user != null) {
            String username = user.get(PreferencesManager.KEY_NAMA);
            String phone = user.get(PreferencesManager.KEY_NO_HP);
            String email = user.get(PreferencesManager.KEY_EMAIL);

            setTextData(etFullName, username);
            setTextData(etPhoneNumber, phone);
            setTextData(etEmail, email);

            etFullName.setText(username);
            etPhoneNumber.setText(phone);
            etEmail.setText(email);
        }
    }

    @Override
    public void showAlertDialog(String message, String title, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        switch (id) {
            case 1:
                builder.setCancelable(false)
                        .setMessage(message)
                        .setIcon(R.drawable.ic_logout_process_xml)
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            presenter.logout();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setOnCancelListener(DialogInterface::dismiss);
                break;
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void setTextData(EditText editText, String string) {
        editText.setText(string);
    }
}
