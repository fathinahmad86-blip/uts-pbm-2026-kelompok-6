package com.example.banksampahkampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
private val HijauUtama = Color(0xFF216747)
private val HijauMuda = Color(0xFFE8F5EC)
private val AbuLatar = Color(0xFFF5F7F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AbuLatar
                ) {
                    HalamanUtama()
                }
            }
        }
    }
}

@Composable
fun HalamanUtama() {
    var daftarSetoran by remember {
        mutableStateOf<List<SetoranSampah>>(emptyList())
    }
    var tampilkanForm by remember { mutableStateOf(false) }
    var tampilkanDaftar by remember { mutableStateOf(false) }

    val totalBerat = daftarSetoran.sumOf { it.berat }
    val totalSaldo = daftarSetoran.sumOf {
        hitungTotalSetoran(it)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
// Header aplikasi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = HijauUtama
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoharmaini),
                    contentDescription = "Logo Bank Sampah Kampus",
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = stringResource(R.string.app_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }

        // Ringkasan
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.ringkasan_setoran),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KolomRingkasan(
                        judul = stringResource(R.string.jumlah_setoran),
                        nilai = "${daftarSetoran.size}",
                        modifier = Modifier.weight(1f)
                    )
                    KolomRingkasan(
                        judul = stringResource(R.string.total_berat),
                        nilai = "${formatBerat(totalBerat)} kg",
                        modifier = Modifier.weight(1f)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = HijauMuda
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.total_saldo),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatRupiah(totalSaldo),
                            style = MaterialTheme.typography.headlineSmall,
                            color = HijauUtama,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Daftar harga
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.daftar_harga),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                BarisHarga("Plastik", 3000.0)
                BarisHarga("Kertas", 2000.0)
                BarisHarga("Logam", 8000.0)
                BarisHarga("Kaca", 1000.0)

                Text(
                    text = stringResource(R.string.bonus_sampah),
                    style = MaterialTheme.typography.bodySmall,
                    color = HijauUtama
                )
            }
        }

        // Tombol form
        Button(
            onClick = {
                tampilkanForm = !tampilkanForm
                if (tampilkanForm) tampilkanDaftar = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = HijauUtama
            )
        ) {
            Text(
                if (tampilkanForm) {
                    stringResource(R.string.tutup_form)
                } else {
                    stringResource(R.string.tambah_setoran)
                }
            )
        }

        if (tampilkanForm) {
            FormSetoran(
                onSetoranDitambahkan = { setoran ->
                    daftarSetoran = daftarSetoran + setoran
                    tampilkanForm = false
                    tampilkanDaftar = true
                }
            )
        }

        // Daftar setoran
        OutlinedButton(
            onClick = {
                tampilkanDaftar = !tampilkanDaftar
                if (tampilkanDaftar) tampilkanForm = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (tampilkanDaftar) {
                    stringResource(R.string.sembunyikan_daftar)
                } else {
                    stringResource(R.string.lihat_daftar)
                }
            )
        }

        if (tampilkanDaftar) {
            DaftarSetoran(
                daftarSetoran = daftarSetoran,
                onHapus = { index ->
                    daftarSetoran = daftarSetoran.filterIndexed {
                            posisi, _ -> posisi != index
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            )
        }

        // Total berat per jenis
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.total_berat_per_jenis),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                listOf("Plastik", "Kertas", "Logam", "Kaca")
                    .forEach { jenis ->
                        val beratJenis = hitungTotalBerat(
                            daftarSetoran, jenis
                        )
                        BarisInformasi(
                            label = jenis,
                            nilai = "${formatBerat(beratJenis)} kg"
                        )
                    }
            }
        }

        // Saldo setiap penyetor
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.saldo_per_penyetor),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                val saldoPenyetor =
                    hitungSaldoPenyetor(daftarSetoran)
                        .toList()
                        .sortedByDescending { it.second }

                if (saldoPenyetor.isEmpty()) {
                    Text(stringResource(R.string.belum_ada_saldo))
                } else {
                    saldoPenyetor.forEachIndexed { index, pasangan ->
                        BarisInformasi(
                            label = "${index + 1}. ${pasangan.first}",
                            nilai = formatRupiah(pasangan.second)
                        )
                    }

                    val terbesar =
                        penyetorSaldoTerbesar(daftarSetoran)

                    if (terbesar != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Saldo tertinggi: " +
                                    "${terbesar.first} — " +
                                    formatRupiah(terbesar.second),
                            color = HijauUtama,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun KolomRingkasan(
    judul: String,
    nilai: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AbuLatar
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = judul,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = nilai,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BarisHarga(
    jenis: String,
    harga: Double
) {
    BarisInformasi(
        label = jenis,
        nilai = "${formatRupiah(harga)}/kg"
    )
}

@Composable
private fun BarisInformasi(
    label: String,
    nilai: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = nilai,
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatRupiah(nilai: Double): String =
    NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("id-ID")
    ).format(nilai)

fun formatBerat(nilai: Double): String =
    String.format(Locale.US, "%.2f", nilai)