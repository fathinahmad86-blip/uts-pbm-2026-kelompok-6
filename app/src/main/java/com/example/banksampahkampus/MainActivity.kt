
package com.example.banksampahkampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.banksampahkampus.ui.theme.BankSampahKampusTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BankSampahKampusTheme {
                HalamanUtama()
            }
        }
    }
}

@Composable
fun HalamanUtama() {

    // STATE: menyimpan data setoran selama aplikasi berjalan
    val daftarSetoran = remember {
        mutableStateListOf<SetoranSampah>()
    }

    // STATE untuk formulir
    var nama by remember { mutableStateOf("") }
    var jenisSampah by remember { mutableStateOf("Plastik") }
    var beratInput by remember { mutableStateOf("") }
    var bersihDanTerpilah by remember { mutableStateOf(false) }
    var menuJenisTerbuka by remember { mutableStateOf(false) }

    // STATE untuk mengatur tampilan formulir dan daftar
    var tampilkanForm by remember { mutableStateOf(false) }
    var tampilkanDaftar by remember { mutableStateOf(false) }
    var pesanError by remember { mutableStateOf("") }

    val pilihanJenis = listOf("Plastik", "Kertas", "Logam", "Kaca")

    // Menghitung ringkasan dari data terbaru
    val data = daftarSetoran.toList()
    val totalBerat = data.sumOf { it.berat }
    val totalSaldo = data.sumOf { hitungTotalSetoran(it) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. JUDUL APLIKASI
            Text(
                text = "BANK SAMPAH KAMPUS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Kelola Setoran Sampah dengan Mudah")

            Spacer(modifier = Modifier.height(24.dp))

            // 2. RINGKASAN SETORAN
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Ringkasan Setoran",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Setoran: ${data.size}")
                    Text("Total Berat Sampah: ${"%.2f".format(totalBerat)} kg")
                    Text("Total Saldo Penyetor: ${formatRupiah(totalSaldo)}")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Berat Berdasarkan Jenis",
                        fontWeight = FontWeight.SemiBold
                    )

                    pilihanJenis.forEach { jenis ->
                        Text(
                            "$jenis: ${
                                "%.2f".format(
                                    hitungTotalBerat(data, jenis)
                                )
                            } kg"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val penyetorTerbesar = penyetorSaldoTerbesar(data)

                    Text(
                        text = "Saldo Terbesar",
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = if (penyetorTerbesar != null) {
                            "${penyetorTerbesar.first}: " +
                                    formatRupiah(penyetorTerbesar.second)
                        } else {
                            "Belum ada data setoran"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. DAFTAR HARGA
            Text(
                text = "Jenis dan Harga Sampah",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            DaftarHarga("Plastik", "Rp3.000/kg")
            DaftarHarga("Kertas", "Rp2.000/kg")
            DaftarHarga("Logam", "Rp8.000/kg")
            DaftarHarga("Kaca", "Rp1.000/kg")

            Spacer(modifier = Modifier.height(24.dp))

            // 4. TOMBOL TAMBAH SETORAN
            Button(
                onClick = {
                    tampilkanForm = !tampilkanForm
                    pesanError = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (tampilkanForm) {
                        "Tutup Form Setoran"
                    } else {
                        "Tambah Setoran Sampah"
                    }
                )
            }

            // 5. FORMULIR MUNCUL SAAT TOMBOL DITEKAN
            if (tampilkanForm) {

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Form Setoran Sampah",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = nama,
                            onValueChange = {
                                nama = it
                                pesanError = ""
                            },
                            label = { Text("Nama penyetor") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = {
                                    menuJenisTerbuka = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Jenis sampah: $jenisSampah")
                            }

                            DropdownMenu(
                                expanded = menuJenisTerbuka,
                                onDismissRequest = {
                                    menuJenisTerbuka = false
                                }
                            ) {
                                pilihanJenis.forEach { jenis ->
                                    DropdownMenuItem(
                                        text = { Text(jenis) },
                                        onClick = {
                                            jenisSampah = jenis
                                            menuJenisTerbuka = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = beratInput,
                            onValueChange = {
                                beratInput = it
                                pesanError = ""
                            },
                            label = { Text("Berat sampah (kg)") },
                            placeholder = { Text("Contoh: 2.5") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = bersihDanTerpilah,
                                onCheckedChange = {
                                    bersihDanTerpilah = it
                                }
                            )

                            Text("Bersih dan terpilah (bonus 10%)")
                        }

                        if (pesanError.isNotBlank()) {
                            Text(
                                text = pesanError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 6. VALIDASI DAN SIMPAN DATA
                        Button(
                            onClick = {
                                val berat = beratInput
                                    .trim()
                                    .replace(',', '.')
                                    .toDoubleOrNull()

                                when {
                                    nama.isBlank() -> {
                                        pesanError =
                                            "Nama penyetor wajib diisi."
                                    }

                                    berat == null ||
                                            !berat.isFinite() ||
                                            berat < 0.5 ||
                                            berat > 100.0 -> {
                                        pesanError =
                                            "Berat harus 0,5 sampai 100 kg."
                                    }

                                    else -> {
                                        daftarSetoran.add(
                                            SetoranSampah(
                                                namaPenyetor = nama.trim(),
                                                jenisSampah = jenisSampah,
                                                berat = berat,
                                                bersihDanTerpilah =
                                                    bersihDanTerpilah
                                            )
                                        )

                                        // Reset formulir setelah berhasil
                                        nama = ""
                                        beratInput = ""
                                        bersihDanTerpilah = false
                                        pesanError = ""
                                        tampilkanForm = false
                                        tampilkanDaftar = true
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simpan Setoran")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 7. TOMBOL LIHAT DAFTAR SETORAN
            OutlinedButton(
                onClick = {
                    tampilkanDaftar = !tampilkanDaftar
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (tampilkanDaftar) {
                        "Tutup Daftar Setoran"
                    } else {
                        "Lihat Daftar Setoran"
                    }
                )
            }

            // 8. DAFTAR SETORAN
            if (tampilkanDaftar) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Daftar Setoran",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (data.isEmpty()) {
                    Text("Belum ada setoran yang tercatat.")
                } else {
                    data.forEachIndexed { index, setoran ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = setoran.namaPenyetor,
                                    fontWeight = FontWeight.Bold
                                )

                                Text("Jenis: ${setoran.jenisSampah}")
                                Text(
                                    "Berat: ${
                                        "%.2f".format(setoran.berat)
                                    } kg"
                                )
                                Text(
                                    "Saldo: ${
                                        formatRupiah(
                                            hitungTotalSetoran(setoran)
                                        )
                                    }"
                                )

                                Text(
                                    text = if (setoran.bersihDanTerpilah) {
                                        "Bonus 10% diberikan"
                                    } else {
                                        "Tanpa bonus"
                                    }
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedButton(
                                    onClick = {
                                        daftarSetoran.remove(setoran)
                                    }
                                ) {
                                    Text("Hapus Setoran")
                                }
                            }
                        }
                    }
                }

                // 9. SALDO PENYETOR DARI TERBESAR KE TERKECIL
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Saldo Setiap Penyetor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                val saldoTerurut = hitungSaldoPenyetor(data)
                    .toList()
                    .sortedByDescending { it.second }

                saldoTerurut.forEachIndexed { index, pasangan ->
                    Text(
                        "${index + 1}. ${pasangan.first}: " +
                                formatRupiah(pasangan.second)
                    )
                }
            }
        }
    }
}

// KOMPONEN DAFTAR HARGA
@Composable
fun DaftarHarga(jenis: String, harga: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = jenis)
            Text(
                text = harga,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// FORMAT ANGKA MENJADI RUPIAH
fun formatRupiah(nilai: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    format.minimumFractionDigits = 0
    return format.format(nilai)
}
