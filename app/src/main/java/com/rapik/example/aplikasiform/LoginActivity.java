package com.rapik.example.aplikasiform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText mViewUser, mViewPassword;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* Melakukan Inisialisasi variabel dengan nilai dari UI*/
        mViewUser=findViewById(R.id.et_username);
        mViewPassword =findViewById(R.id.et_password);
// Check if UserResponse is Already Logged In
        if(Preferences.getLoggedInStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /* Melakukan pegecekan inputan Username dan Password dan Memberikan akses ke
    MainActivity */
    private void razia(){
        /* Mereset semua Error dan fokus menjadi default */
        mViewUser.setError(null);
        mViewPassword.setError(null);
        View fokus = null;
        boolean cancel = false;
        /* Mengambil nilai dari UI dengan variable baru bertipe String*/
        String user = mViewUser.getText().toString().trim();
        String password = mViewPassword.getText().toString();
        /* Pengecekan jika UI komponen username tidak diisi user*/
        if (TextUtils.isEmpty(user) || user.length() > 80){
            mViewUser.setError("This field is required");
            fokus = mViewUser;
            cancel = true;
        }else if(!cekUser(user)){
            mViewUser.setError("This Username is not found");
            fokus = mViewUser;
            cancel = true;
        }
        /* Pengecekan jika UI komponen password tidak diisi user*/
        if (TextUtils.isEmpty(password) || password.length() > 128){
            mViewPassword.setError("This field is required");
            fokus = mViewPassword;
            cancel = true;
        }else if (!cancel && !cekPassword(password)){
            mViewPassword.setError("This password is incorrect");
            fokus = mViewPassword;
            cancel = true;
        }
        /* Jika cancel true, variable fokus mendapatkan fokus */
        if (cancel)
            fokus.requestFocus();
        else
            masuk();
    }
    /* Menuju ke MainActivity dan Set User dan Status menjadi sedang login di
    Preferences */
    private void masuk(){
        if (!Preferences.signIn(this)) {
            Toast.makeText(this, "Sesi lokal belum dapat disimpan.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
    /* True jika parameter password sama dengan data password yang terdaftar dari
    Preferences */
    private boolean cekPassword(String password){
        return Preferences.verifyPassword(this, password);
    }
    /* True jika parameter user sama dengan data user yang terdaftar dari
    Preferences */
    private boolean cekUser(String user){
        return user.equals(Preferences.getRegisteredUser(getBaseContext()));
    }
    public void signin(View v){
        try {
            razia();
        } catch (RuntimeException error) {
            Toast.makeText(this, "Login lokal belum dapat diproses.", Toast.LENGTH_SHORT).show();
        }
    }
    public void signup(View v){
        startActivity(new Intent(this, RegisterActivity2.class));
}
}
