
package com.example.banksampahkampus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FormSetoran(
    onSetoranDitambahkan: (SetoranSampah) -> Unit
) {
    var namaPenyetor by remember { mutableStateOf("") }
    var jenisSampah by remember { mutableStateOf("Plastik") }
    var beratInput by remember { mutableStateOf("") }
    var bersihDanTerpilah by remember { mutableStateOf(false) }
    var sudahMencobaSimpan by remember { mutableStateOf(false) }

    val pilihanJenis = listOf("Plastik", "Kertas", "Logam", "Kaca")

    val nama = namaPenyetor.trim()

    // Nama harus mengandung minimal satu huruf.
    val namaValid = nama.any { it.isLetter() }

    val berat = beratInput.trim()
        .replace(',', '.')
        .toDoubleOrNull()

    val beratValid = berat != null &&
            berat.isFinite() &&
            berat in 0.5..100.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Formulir Setoran Sampah",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = namaPenyetor,
            onValueChange = { namaPenyetor = it },
            label = { Text("Nama penyetor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = sudahMencobaSimpan && !namaValid
        )

        if (sudahMencobaSimpan && !namaValid) {
            Text(
                text = "Nama harus mengandung huruf. Contoh: Luna",
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = "Jenis sampah",
            style = MaterialTheme.typography.titleMedium
        )

        pilihanJenis.chunked(2).forEach { baris ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                baris.forEach { jenis ->
                    FilterChip(
                        selected = jenisSampah == jenis,
                        onClick = { jenisSampah = jenis },
                        label = { Text(jenis) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        OutlinedTextField(
            value = beratInput,
            onValueChange = { beratInput = it },
            label = { Text("Berat sampah (kg)") },
            placeholder = { Text("Contoh: 2.5") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            isError = sudahMencobaSimpan && !beratValid
        )

        if (sudahMencobaSimpan && !beratValid) {
            Text(
                text = "Berat harus antara 0,5 sampai 100 kg",
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = bersihDanTerpilah,
                onCheckedChange = { bersihDanTerpilah = it }
            )

            Text(
                text = "Bersih dan terpilah (bonus 10%)",
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                sudahMencobaSimpan = true

                if (namaValid && beratValid) {
                    onSetoranDitambahkan(
                        SetoranSampah(
                            namaPenyetor = nama,
                            jenisSampah = jenisSampah,
                            berat = berat!!,
                            bersihDanTerpilah = bersihDanTerpilah
                        )
                    )

                    // Kosongkan formulir setelah berhasil disimpan.
                    namaPenyetor = ""
                    jenisSampah = "Plastik"
                    beratInput = ""
                    bersihDanTerpilah = false
                    sudahMencobaSimpan = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Setoran")
        }
    }
}
