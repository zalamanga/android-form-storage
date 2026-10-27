package com.rapik.example.aplikasiform;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataExternalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Preferences.requireLogin(this)) return;
        setContentView(R.layout.activity_data_external);
        TextView result = findViewById(R.id.tvDataRegister2);
        File directory = getExternalFilesDir("MyFileStorage");
        if (directory == null) {
            result.setText("Penyimpanan eksternal aplikasi belum tersedia.");
            return;
        }
        try {
            result.setText(FormStorage.read(new File(directory, "SampleFile.txt")));
        } catch (FileNotFoundException error) {
            result.setText("Belum ada form di penyimpanan eksternal aplikasi.");
        } catch (IOException error) {
            result.setText("Data eksternal belum dapat dibaca. File tidak dihapus.");
        }
    }
}
