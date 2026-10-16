package com.example.banksampahkampus

// Menentukan harga sampah per kilogram
fun hargaPerKg(jenisSampah: String): Int {
    return when (jenisSampah.trim().lowercase()) {
        "plastik" -> 3000
        "kertas" -> 2000
        "logam" -> 8000
        "kaca" -> 1000
        else -> 0
    }
}

// Menghitung nilai setoran sebelum bonus
fun hitungNilaiSetoran(setoran: SetoranSampah): Double {
    return setoran.berat * hargaPerKg(setoran.jenisSampah)
}

// Menghitung bonus 10%
fun hitungBonus(setoran: SetoranSampah): Double {
    return if (setoran.bersihDanTerpilah) {
        hitungNilaiSetoran(setoran) * 0.10
    } else {
        0.0
    }
}

// Menghitung total saldo dari satu setoran
fun hitungTotalSetoran(setoran: SetoranSampah): Double {
    return hitungNilaiSetoran(setoran) + hitungBonus(setoran)
}

// Menghitung total berat berdasarkan jenis sampah
fun hitungTotalBerat(
    daftar: List<SetoranSampah>,
    jenis: String
): Double {
    return daftar
        .filter {
            it.jenisSampah.equals(jenis, ignoreCase = true)
        }
        .sumOf { it.berat }
}

// Menghitung saldo setiap penyetor
fun hitungSaldoPenyetor(
    daftar: List<SetoranSampah>
): Map<String, Double> {
    return daftar
        .groupBy {
            it.namaPenyetor.trim().lowercase()
        }
        .mapValues { (_, setoran) ->
            setoran.sumOf {
                hitungTotalSetoran(it)
            }
        }
}

// Menentukan penyetor dengan saldo terbesar
fun penyetorSaldoTerbesar(
    daftar: List<SetoranSampah>
): Pair<String, Double>? {
    return hitungSaldoPenyetor(daftar)
        .maxByOrNull { it.value }
        ?.let { it.key to it.value }
}