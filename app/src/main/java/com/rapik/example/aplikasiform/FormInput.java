package com.rapik.example.aplikasiform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormInput {
    private FormInput() {}

    public static String validate(String name, String studentClass, String gender, String birth) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            return "Nama wajib diisi, maksimal 100 karakter.";
        }
        for (char letter : name.toCharArray()) {
            if (Character.isISOControl(letter)) return "Nama tidak boleh berisi baris baru atau karakter kontrol.";
        }
        if (studentClass == null || studentClass.trim().isEmpty()) return "Pilih kelas.";
        if (!"male".equalsIgnoreCase(gender) && !"female".equalsIgnoreCase(gender)) {
            return "Pilih jenis kelamin.";
        }
        if (birth == null || !birth.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
            return "Tanggal lahir harus dd-MM-yyyy.";
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        format.setLenient(false);
        try {
            Date date = format.parse(birth);
            if (date == null || date.after(new Date())) return "Tanggal lahir tidak valid.";
        } catch (ParseException error) {
            return "Tanggal lahir tidak valid.";
        }
        return null;
    }

    public static String render(String name, String studentClass, String gender, String birth) {
        String error = validate(name, studentClass, gender, birth);
        if (error != null) throw new IllegalArgumentException(error);
        return "Full Name : " + name.trim() + "\nClass : " + studentClass
                + "\nGender : " + gender + "\nDate of Birth : " + birth + "\n";
    }
}
