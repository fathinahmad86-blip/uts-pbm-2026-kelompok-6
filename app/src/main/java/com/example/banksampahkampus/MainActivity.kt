package com.example.banksampahkampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.banksampahkampus.ui.theme.BankSampahKampusTheme

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

            Text(
                text = "BANK SAMPAH KAMPUS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kelola Setoran Sampah dengan Mudah",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ringkasan Setoran",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Setoran: 0")

                    Text("Total Berat Sampah: 0 kg")

                    Text("Total Saldo Penyetor: Rp0")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Jenis dan Harga Sampah",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            DaftarHarga(
                jenis = "Plastik",
                harga = "Rp3.000/kg"
            )

            DaftarHarga(
                jenis = "Kertas",
                harga = "Rp2.000/kg"
            )

            DaftarHarga(
                jenis = "Logam",
                harga = "Rp8.000/kg"
            )

            DaftarHarga(
                jenis = "Kaca",
                harga = "Rp1.000/kg"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Form input akan diintegrasikan berikutnya
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah Setoran Sampah")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    // Daftar setoran akan diintegrasikan berikutnya
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lihat Daftar Setoran")
            }
        }
    }
}

@Composable
fun DaftarHarga(
    jenis: String,
    harga: String
) {
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