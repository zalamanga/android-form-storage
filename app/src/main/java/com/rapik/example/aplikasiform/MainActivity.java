package com.rapik.example.aplikasiform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt_logout;
    TextView nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Preferences.requireLogin(this)) return;
        setContentView(R.layout.activity_main);
        bt_logout = findViewById(R.id.bt_logout);
        nama = findViewById(R.id.tv_namaMain);
/* Mengatur nilai Label Nama dengan data User sedang login dari
Preferences */
        nama.setText(Preferences.getLoggedInUser(getBaseContext()));
    }
    public void openRegister(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
}
    public void showRegister(View v) {
        Intent intent = new Intent(MainActivity.this, DataActivity.class);
        startActivity(intent);
    }

    public void showRegister2(View v) {
        Intent intent = new Intent(MainActivity.this, DataExternalActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
//Menghapus Status login dan kembali ke Login Activity
        if (!Preferences.clearLoggedInUser(this)) {
            Toast.makeText(this, "Sesi belum dapat dihapus. Coba lagi.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}
