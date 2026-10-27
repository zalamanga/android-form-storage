package com.rapik.example.aplikasiform;

import android.util.AtomicFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

final class FormStorage {
    private static final int MAX_BYTES = 8192;

    private FormStorage() {}

    static void write(File target, String record) throws IOException {
        if (target == null) throw new IOException("Storage unavailable.");
        byte[] bytes = record.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > MAX_BYTES) throw new IOException("Form is too large.");
        AtomicFile file = new AtomicFile(target);
        FileOutputStream stream = null;
        try {
            stream = file.startWrite();
            stream.write(bytes);
            file.finishWrite(stream);
            stream = null;
            if (!record.equals(read(target))) throw new IOException("Saved form could not be verified.");
        } catch (IOException | RuntimeException error) {
            if (stream != null) file.failWrite(stream);
            throw error;
        }
    }

    static String read(File target) throws IOException {
        if (target == null) throw new IOException("Storage unavailable.");
        try (InputStream stream = new AtomicFile(target).openRead();
             ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = stream.read(buffer)) != -1) {
                if (bytes.size() + count > MAX_BYTES) throw new IOException("Form is too large.");
                bytes.write(buffer, 0, count);
            }
            return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
