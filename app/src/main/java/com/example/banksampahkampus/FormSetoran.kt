package com.example.banksampahkampus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun FormMasukan() {
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
                errorBerat = it.toDoubleOrNull()?.let { nilai -> nilai <= 0 } ?: true
            },
            label = { Text("Berat sampah (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorBerat
        )

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Setoran")
        }
    }
}
