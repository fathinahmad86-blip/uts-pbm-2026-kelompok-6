package com.example.banksampahkampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HalamanUtama()
                }
            }
        }
    }
}

@Composable
fun HalamanUtama() {
    val daftarSetoran = remember {
        mutableStateListOf<SetoranSampah>()
    }

    var tampilkanForm by remember { mutableStateOf(false) }
    var tampilkanDaftar by remember { mutableStateOf(false) }

    val jenisSampah = listOf("Plastik", "Kertas", "Logam", "Kaca")

    val totalBerat = daftarSetoran.sumOf { it.berat }
    val totalSaldo = daftarSetoran.sumOf {
        hitungTotalSetoran(it)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "BANK SAMPAH KAMPUS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text("Kelola Setoran Sampah dengan Mudah")
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Ringkasan Setoran",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                BarisInfo("Jumlah Setoran", daftarSetoran.size.toString())
                BarisInfo("Total Berat", "${formatBerat(totalBerat)} kg")
                BarisInfo("Total Saldo", formatRupiah(totalSaldo))
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Daftar Harga Sampah",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                jenisSampah.forEach { jenis ->
                    BarisInfo(
                        jenis,
                        "${formatRupiah(hargaPerKg(jenis))}/kg"
                    )
                }

                Text(
                    text = "Bonus 10% untuk sampah yang bersih dan terpilah",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Button(
            onClick = { tampilkanForm = !tampilkanForm },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (tampilkanForm) "Tutup Form Setoran"
                else "Tambah Setoran"
            )
        }

        if (tampilkanForm) {
            Card(modifier = Modifier.fillMaxWidth()) {
                FormSetoran(
                    onSetoranDitambahkan = { setoran ->
                        daftarSetoran.add(setoran)
                        tampilkanForm = false
                        tampilkanDaftar = false
                    }
                )
            }
        }

        OutlinedButton(
            onClick = { tampilkanDaftar = !tampilkanDaftar },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (tampilkanDaftar) "Tutup Daftar Setoran"
                else "Lihat Daftar Setoran"
            )
        }

        if (tampilkanDaftar) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (daftarSetoran.isEmpty()) {
                        Text("Belum ada setoran.")
                    } else {
                        daftarSetoran.forEachIndexed { index, setoran ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = setoran.namaPenyetor,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Jenis: ${setoran.jenisSampah}")
                                Text("Berat: ${formatBerat(setoran.berat)} kg")
                                Text(
                                    "Saldo: ${
                                        formatRupiah(hitungTotalSetoran(setoran))
                                    }"
                                )
                                Text(
                                    if (setoran.bersihDanTerpilah) {
                                        "Bersih dan terpilah"
                                    } else {
                                        "Belum mendapat bonus kebersihan"
                                    }
                                )

                                OutlinedButton(
                                    onClick = {
                                        if (index in daftarSetoran.indices) {
                                            daftarSetoran.removeAt(index)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Hapus Setoran")
                                }
                            }
                        }
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Total Berat per Jenis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                jenisSampah.forEach { jenis ->
                    val beratJenis = daftarSetoran
                        .filter { it.jenisSampah == jenis }
                        .sumOf { it.berat }

                    BarisInfo(
                        jenis,
                        "${formatBerat(beratJenis)} kg"
                    )
                }
            }
        }
    }
}

@Composable
private fun BarisInfo(label: String, nilai: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = nilai,
            fontWeight = FontWeight.SemiBold
        )
    }
}


private fun formatRupiah(nilai: Number): String {
    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("id-ID")
    ).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }.format(nilai)
}


private fun formatBerat(nilai: Double): String {
    return String.format(Locale.US, "%.2f", nilai)
}