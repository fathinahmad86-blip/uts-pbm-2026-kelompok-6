package com.example.banksampahkampus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Setoran(
    val nama: String,
    val jenis: String,
    val berat: Double,
    val total: Double
)

@Composable
fun FormSetoran(
    onSetoranDitambahkan: (Setoran) -> Unit
) {
    var namaPenyetor by remember { mutableStateOf("") }
    var jenisSampah by remember { mutableStateOf("Plastik") }
    var berat by remember { mutableStateOf("") }

    var errorNama by remember { mutableStateOf(false) }
    var errorBerat by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Formulir Setoran Sampah",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = namaPenyetor,
            onValueChange = {
                namaPenyetor = it
                errorNama = it.isBlank()
            },
            label = { Text("Nama penyetor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorNama
        )

        if (errorNama) {
            Text(
                text = "Nama penyetor tidak boleh kosong",
                color = MaterialTheme.colorScheme.error
            )
        }

        Text("Jenis sampah")

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Plastik", "Kertas", "Logam", "Kaca")
                .forEach { jenis ->
                    FilterChip(
                        selected = jenisSampah == jenis,
                        onClick = { jenisSampah = jenis },
                        label = { Text(jenis) }
                    )
                }
        }

        OutlinedTextField(
            value = berat,
            onValueChange = {
                berat = it
                errorBerat = it.toDoubleOrNull()?.let { nilai ->
                    nilai < 0.5 || nilai > 100
                } ?: true
            },
            label = { Text("Berat sampah (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = errorBerat
        )

        if (errorBerat) {
            Text(
                text = "Berat sampah harus antara 0,5 sampai 100 kg",
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                val beratAngka = berat.replace(',', '.').toDoubleOrNull()

                if (
                    namaPenyetor.isNotBlank() &&
                    beratAngka != null &&
                    beratAngka.isFinite() &&
                    beratAngka in 0.5..100.0
                ) {
                    val hargaPerKg = when (jenisSampah) {
                        "Plastik" -> 3000.0
                        "Kertas" -> 2000.0
                        "Logam" -> 8000.0
                        "Kaca" -> 1000.0
                        else -> 0.0
                    }

                    val setoranBaru = Setoran(
                        nama = namaPenyetor.trim(),
                        jenis = jenisSampah,
                        berat = beratAngka,
                        total = hargaPerKg * beratAngka
                    )

                    onSetoranDitambahkan(setoranBaru)

                    namaPenyetor = ""
                    berat = ""
                    errorNama = false
                    errorBerat = false
                } else {
                    errorNama = namaPenyetor.isBlank()
                    errorBerat = beratAngka == null ||
                            !beratAngka.isFinite() ||
                            beratAngka !in 0.5..100.0
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = namaPenyetor.isNotBlank() &&
                    berat.replace(',', '.').toDoubleOrNull()
                        ?.let { it.isFinite() && it in 0.5..100.0 } == true
        ) {
            Text("Tambah Setoran")
        }
    }
}
