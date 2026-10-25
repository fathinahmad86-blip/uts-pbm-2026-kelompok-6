package com.example.banksampahkampus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun DaftarSetoran(
    daftarSetoran: List<SetoranSampah>,
    onHapus: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text(
            text = "Daftar Setoran",
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (daftarSetoran.isEmpty()) {
            Text("Belum ada setoran sampah.")

        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(daftarSetoran) { index, setoran ->

                    ItemSetoran(
                        setoran = setoran,
                        onHapus = {
                            onHapus(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemSetoran(
    setoran: SetoranSampah,
    onHapus: () -> Unit
) {
    val saldo = hitungTotalSetoran(setoran)

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = setoran.namaPenyetor)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Jenis: ${setoran.jenisSampah}")

            Text(
                text = "Berat: ${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        setoran.berat
                    )
                } kg"
            )

            Text(
                text = "Saldo: Rp${
                    String.format(
                        Locale("id", "ID"),
                        "%,.0f",
                        saldo
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onHapus
                ) {
                    Text("Hapus Setoran")
                }
            }
        }
    }
}
