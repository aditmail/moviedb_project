package com.example.movieproject.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.movieproject.models.register_form.RegisterForm;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 1;

    // -- DB Notification --
    //* TB Name
    private static final String TB_USER = "TB_USERS";

    //** Column Name
    private static final String KEY_ID_USER = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_PHONE = "message";
    private static final String KEY_EMAIL = "data";
    private static final String KEY_PASSWORD = "timestamp";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_TABLE_USER = "CREATE TABLE " + TB_USER +
                "(" + KEY_ID_USER + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," +
                KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TB_USER);
        onCreate(db);
    }

    public void addRecord(RegisterForm form) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, form.getFullName());
        values.put(KEY_PHONE, form.getPhoneNumber());
        values.put(KEY_EMAIL, form.getEmail());
        values.put(KEY_PASSWORD, form.getPassword());

        database.insert(TB_USER, null, values);
        database.close();
    }

    public void deleteRecord() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TB_USER);
        database.close();
    }

    public List<RegisterForm> getAllData() {
        List<RegisterForm> listData = new ArrayList<>();
        String query = "SELECT * FROM " + TB_USER +
                " ORDER BY " + KEY_ID_USER + " DESC";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                String password = cursor.getString(4);

                RegisterForm form = new RegisterForm("", phone, name, email, password, "");
                listData.add(form);
            } while (cursor.moveToNext());
        }
        return listData;
    }

    public RegisterForm getLoginData(String phone, String password) {
        String query = "SELECT * FROM " + TB_USER +
                " WHERE " + KEY_PHONE + " = " + "'" + phone + "'" +
                " AND " + KEY_PASSWORD + " = " + "'" + password + "'";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        RegisterForm form = null;
        if (cursor.moveToFirst()) {
            do {
                String idUser = cursor.getString(0);
                String nameUser = cursor.getString(1);
                String phoneUser = cursor.getString(2);
                String emailUser = cursor.getString(3);
                String passwordUser = cursor.getString(4);

                form = new RegisterForm(idUser, phoneUser, nameUser,
                        emailUser, passwordUser, "");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return form;
    }
}
