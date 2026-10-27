# Android Form Storage

Latihan Android Java untuk mengisi data siswa, menyimpan satu form pada file internal atau file eksternal khusus aplikasi, lalu membacanya kembali. Source berasal dari aplikasiform2. Layout, gambar, ikon, serta field kelas/nama/jenis kelamin/tanggal lahir dipertahankan.

## Cara mencoba

1. Buka aplikasi, pilih **Sign Up**, lalu buat satu akun demo lokal. Username wajib diisi; password baru harus 8 sampai 128 karakter.
2. Masuk melalui **Sign In**.
3. Pilih **Form Siswa**, isi data sintetis, lalu tekan **Submit internal** atau **Submit External**.
4. Kembali ke dashboard dan pilih **Show Register Internal** atau **Show Register External** untuk membaca file yang dipilih.
5. **Log Out** menghapus sesi lokal. Akun demo tetap tersimpan untuk login berikutnya.

Satu instalasi mempunyai satu akun demo, satu file form internal, dan satu file form eksternal. Menyimpan form mengganti isi file pada lokasi yang dipilih; aplikasi ini bukan daftar banyak siswa atau sistem akun banyak pengguna.

## Build

Buka root repo di Android Studio, gunakan JDK 17 dan Android SDK Platform 35. Biarkan Android Studio membuat `local.properties` dengan lokasi SDK sendiri.

| Komponen | Versi |
| --- | --- |
| Android Gradle Plugin | 8.8.2 |
| Gradle Wrapper | 8.10.2, checksum distribusi dipin |
| compileSdk / targetSdk | 35 / 34 |
| minSdk | 24, Android 7.0 |
| Source aplikasi | Java, kompatibilitas Java 8 |

Setelah SDK dan dependency tersedia:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
```

APK debug dihasilkan pada `app/build/outputs/apk/debug/`. Jalankan konfigurasi `app` pada emulator/perangkat API 24 ke atas. Tidak memerlukan Firebase, koneksi API, database server, atau kredensial bawaan.

## Data disimpan di mana

| Data | Lokasi dalam ruang aplikasi |
| --- | --- |
| Akun dan flag sesi | SharedPreferences privat |
| Form internal | `getFilesDir()/register.txt` |
| Form eksternal | `getExternalFilesDir("MyFileStorage")/SampleFile.txt` |

Eksternal di sini berarti folder eksternal milik aplikasi, bukan folder publik Downloads. Karena minSdk 24, alur ini tidak memerlukan izin baca/tulis penyimpanan umum. Bila penyimpanan eksternal tidak tersedia, tombol simpan eksternal dinonaktifkan dan halaman pembaca menampilkan keadaan tersebut.

Form memvalidasi isian, tanggal kalender, dan jenis kelamin sebelum ditulis. Penulisan menggunakan AtomicFile lalu membaca ulang hasilnya; toast berhasil hanya muncul setelah hasil yang dibaca cocok. Bila konfirmasi gagal, periksa halaman pembaca sebelum mengulang. Pembaca mempunyai pesan belum-ada-data dan gagal-baca; file bermasalah tidak dihapus otomatis.

Data file form tetap berupa teks lokal. Aplikasi tidak mengenkripsi file form dan tidak mengirimnya ke layanan luar. Gunakan data latihan; file aplikasi dapat hilang saat aplikasi dihapus.

## Akun lokal dan versi lama

Password baru memakai PBKDF2 dengan salt acak. Password plaintext lama yang cocok dimigrasi ke hash saat login; password salah tidak mengubah data. Format hash yang rusak ditolak. Data akun dan form tidak direset untuk menjalankan migrasi ini.

Akun kedua tidak menimpa akun pertama. Tidak ada reset password, verifikasi email, server autentikasi, atau jaminan perlindungan terhadap orang yang mengendalikan data perangkat. Ini latihan sesi dan penyimpanan lokal, bukan backend autentikasi produksi. Backup otomatis dinonaktifkan lewat manifest.

## Peta kode

Source utama berada di `app/src/main/java/com/rapik/example/aplikasiform/`.

| Berkas | Fungsi |
| --- | --- |
| LoginActivity / RegisterActivity2 | Masuk dan pendaftaran akun demo |
| MainActivity | Dashboard dan logout |
| RegisterActivity | Form siswa serta pilihan penyimpanan |
| DataActivity / DataExternalActivity | Membaca form dan menampilkan empty/error state |
| Preferences / PasswordHasher | Akun, sesi, hash dan migrasi password lokal |
| FormInput / FormStorage | Validasi/format form dan penulisan/pembacaan file |

Nama RegisterActivity berasal dari source awal dan menunjuk form siswa. RegisterActivity2 menangani akun. Keduanya bukan duplikat.

## Pemeriksaan

Pada 27 Oktober 2026, `assembleDebug`, `testDebugUnitTest`, dan `lintDebug` berhasil dijalankan dengan JDK 17 serta SDK 35. LocalRulesTest memeriksa password benar/salah, salt, migrasi plaintext, format hash rusak, tanggal, serta nama yang mengandung baris baru. Jumlah tes dan warning lint tercatat di [hasil pemeriksaan](BUILD-VERIFICATION.md).

File I/O pada perangkat, emulator, dan screenshot belum diuji. Tes pure Java tidak membuktikan perilaku penyimpanan Android atau tampilan UI.

## Asal source

Namespace com.rapik.example.aplikasiform dan aset lokal dipertahankan. Proyek aplikasiform yang lebih kecil hanya berisi Hello World; tidak disertakan sebagai salinan kedua. Tidak ditemukan lisensi proyek atau upstream yang dapat dipastikan, sehingga tidak ditambahkan klaim kepemilikan baru. Header lisensi Gradle Wrapper berlaku untuk wrapper; dependency mengikuti lisensi masing-masing.

Konfigurasi SDK lokal, cache, hasil build, data akun/form, service config, dan kunci penandatanganan tidak masuk repo.
