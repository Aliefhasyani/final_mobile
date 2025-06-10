
## Technical Implementation

### 1. **Architecture**
- Menggunakan kombinasi `Activity` dan `Fragment` untuk navigasi utama.
- Data model kursus dikelola dengan kelas model Java.
- Komunikasi API menggunakan Retrofit.

### 2. **UI & Theme**
- Desain mengikuti Material Design dengan komponen seperti `MaterialToolbar`, `BottomNavigationView`, dan `RecyclerView`.
- Mendukung dark mode dengan tema `Theme.MaterialComponents.DayNight` dan atribut tema (`?attr/colorSurface`, `?android:windowBackground`, dll) di semua layout.
- Layout responsif menggunakan `ConstraintLayout` dan `RecyclerView` grid.

### 3. **Navigation**
- Navigasi utama menggunakan `BottomNavigationView` untuk Home, Favorites, dan Settings.
- Detail kursus dibuka dengan `Intent` ke `DetailActivity`.

### 4. **Data & Network**
- Data kursus diambil dari API Udemy via Retrofit (`ApiService`, `RetrofitClient`).
- Header API diatur pada interceptor OkHttp.
- Mendukung paginasi (load more) pada daftar kursus.

### 5. **Favorite & State**
- Kursus favorit disimpan secara lokal (SharedPreferences).
- Empty state dan loading state di-handle dengan `TextView` dan `CircularProgressIndicator`.

### 6. **Settings & Dark Mode**
- Pengaturan dark mode diakses melalui `SettingsActivity` dengan `SwitchCompat`.
- Perubahan mode langsung diterapkan dengan `AppCompatDelegate` dan restart activity.

### 7. **Best Practice**
- Semua warna dan background di layout menggunakan atribut tema agar UI otomatis menyesuaikan mode terang/gelap.
- String dan resource lain dikelola di file resource (`strings.xml`, `colors.xml`).

---

**File penting:**
- `MainActivity.java` — Navigasi utama dan fragment container.
- `HomeFragment.java`, `FavoriteFragment.java` — Tampilan utama dan favorit.
- `DetailActivity.java` — Detail kursus.
- `SettingsActivity.java` — Pengaturan dark mode.
- `adapter/CourseAdapter.java` — Adapter untuk `RecyclerView`.
- `network/ApiService.java`, `network/RetrofitClient.java` — Koneksi API.
- Layout XML di `res/layout/` sudah dioptimalkan untuk dark mode.

---
skibiditoile sigma1900


