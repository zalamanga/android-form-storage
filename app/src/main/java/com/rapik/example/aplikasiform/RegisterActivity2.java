package com.rapik.example.aplikasiform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity2 extends AppCompatActivity {
    private EditText mViewUser, mViewPassword, mViewRepassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        /* Melakukan Inisialisasi variable dari Layout RegisterActivity */
        mViewUser =findViewById(R.id.et_emailSignup);
        mViewPassword =findViewById(R.id.et_passwordSignup);
        mViewRepassword =findViewById(R.id.et_passwordSignup2);
    }
    /** Melakukan pegecekan inputan User dan Password dan Memberikan akses ke
     MainActivity */
    private void razia(){
        /* Mereset semua Error dan fokus menjadi default */
        mViewUser.setError(null);
        mViewPassword.setError(null);
        mViewRepassword.setError(null);
        View fokus = null;
        boolean cancel = false;
        /* Mengambil nilai dari UI komponen dengan variable baru bertipe String*/
        String repassword = mViewRepassword.getText().toString();
        String user = mViewUser.getText().toString().trim();
        String password = mViewPassword.getText().toString();
        /* Pengecekan requirement username */
        if (TextUtils.isEmpty(user) || user.length() > 80){
            mViewUser.setError("This field is required");
            fokus = mViewUser;
            cancel = true;
        }else if(Preferences.hasRegisteredAccount(this)){
            mViewUser.setError("Perangkat ini sudah memiliki satu akun demo.");
            fokus = mViewUser;
            cancel = true;
        }
        /* Pengecekan requirement password */
        if (password.length() < 8 || password.length() > 128){
            mViewPassword.setError("Gunakan password 8 sampai 128 karakter.");
            fokus = mViewPassword;
            cancel = true;
        }else if (!cekPassword(password,repassword)){
            mViewRepassword.setError("This password is incorrect");
            fokus = mViewRepassword;
            cancel = true;
        }
        /* Jika cancel true, variable fokus mendapatkan fokus. Jika false, kembali
ke LoginActivity dan Set User dan Password untuk data yang terdaftar */
        if (cancel){
            fokus.requestFocus();
        }else{
            try {
                if (!Preferences.registerAccount(this, user, password)) {
                    Toast.makeText(this, "Akun sudah ada atau belum dapat disimpan.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Akun demo tersimpan. Silakan masuk.", Toast.LENGTH_SHORT).show();
                finish();
            } catch (RuntimeException error) {
                Toast.makeText(this, "Akun lokal belum dapat disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /* True jika parameter password sama dengan parameter repassword */
    private boolean cekPassword(String password, String repassword){
        return password.equals(repassword);
    }
    /* True jika parameter user sama dengan data user yang terdaftar dari
    Preferences */
    private boolean cekUser(String user){
        return user.equals(Preferences.getRegisteredUser(getBaseContext()));
    }
    public void create(View v){
        razia();
}
}
