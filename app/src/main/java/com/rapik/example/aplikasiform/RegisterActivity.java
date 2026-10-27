package com.rapik.example.aplikasiform;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    EditText etfn, etbirth;
    TextView tvhasil;
    Button btexit;
    Spinner spclass;
    RadioGroup rgender;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;
    Button btsubmit2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Preferences.requireLogin(this)) return;
        setContentView(R.layout.activity_register);


        etfn = (EditText) findViewById(R.id.etfullname);
        spclass = (Spinner) findViewById(R.id.kelas);
        rgender = (RadioGroup) findViewById(R.id.rgender);
        etbirth= (EditText) findViewById(R.id.etbirth);
        tvhasil = (TextView) findViewById(R.id.tvhasil);
        btexit = (Button) findViewById(R.id.btexit);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        btexit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Toast.makeText(getApplicationContext(), "keluar dari aplikasi", Toast.LENGTH_LONG).show();
                                          finish();
                                      }
                                  }

        );
        btsubmit2 = (Button) findViewById(R.id.btsubmit2);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            btsubmit2.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }
    }

    public void showName(View v) {
        try {
            String record = readForm();
            FormStorage.write(new File(getFilesDir(), "register.txt"), record);
            tvhasil.setText("Halo,\n" + record);
            Toast.makeText(this,"Data has been saved successfully", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            tvhasil.setText("Simpan belum dapat dikonfirmasi. Periksa isian dan data tersimpan sebelum mengulang.");
            Toast.makeText(this, e instanceof IllegalArgumentException ? e.getMessage()
                    : "Penyimpanan internal belum dapat ditulis.", Toast.LENGTH_LONG).show();
        }
    }

    public void showName2(View v) {
        try {
            String record = readForm();
            FormStorage.write(myExternalFile, record);
            tvhasil.setText("Halo,\n" + record);
            Toast.makeText(this,"Data has been saved successfully to External Storage", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            tvhasil.setText("Simpan belum dapat dikonfirmasi. Periksa isian dan data tersimpan sebelum mengulang.");
            Toast.makeText(this, e instanceof IllegalArgumentException ? e.getMessage()
                    : "Penyimpanan eksternal belum dapat ditulis.", Toast.LENGTH_LONG).show();
        }
    }


    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etbirth.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showDate(View v) {
        showDateDialog();
    }
    private String readForm() {
        int checked = rgender.getCheckedRadioButtonId();
        if (checked == -1) throw new IllegalArgumentException("Pilih jenis kelamin.");
        RadioButton gender = findViewById(checked);
        return FormInput.render(etfn.getText().toString(),
                spclass.getSelectedItem().toString(), gender.getText().toString(),
                etbirth.getText().toString());
    }
    //Proses pengecekan Eksternal Storage read only atau tidak
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    // Proses pengecekan Eksternal Storage available atau tidak
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
