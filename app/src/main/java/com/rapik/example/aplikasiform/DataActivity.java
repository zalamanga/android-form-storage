package com.rapik.example.aplikasiform;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Preferences.requireLogin(this)) return;
        setContentView(R.layout.activity_data);
        TextView result = findViewById(R.id.tvDataRegister);
        try {
            result.setText(FormStorage.read(new File(getFilesDir(), "register.txt")));
        } catch (FileNotFoundException error) {
            result.setText("Belum ada form di penyimpanan internal.");
        } catch (IOException error) {
            result.setText("Data internal belum dapat dibaca. File tidak dihapus.");
        }
    }
}
