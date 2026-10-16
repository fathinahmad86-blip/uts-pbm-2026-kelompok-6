package com.example.banksampahkampus

import org.junit.Assert.assertEquals
import org.junit.Test

class LogikaSampahTest {

    @Test
    fun testHitungTotalSetoran() {
        val setoran = SetoranSampah(
            namaPenyetor = "Budi",
            jenisSampah = "plastik",
            berat = 10.0,
            bersihDanTerpilah = true
        )

        val hasil = hitungTotalSetoran(setoran)

        assertEquals(33000.0, hasil, 0.01)
    }
    @Test
    fun testTanpaBonus() {
        val setoran = SetoranSampah(
            namaPenyetor = "Andi",
            jenisSampah = "kertas",
            berat = 5.0,
            bersihDanTerpilah = false
        )

        val hasil = hitungTotalSetoran(setoran)

        assertEquals(10000.0, hasil, 0.01)
    }
    @Test
    fun testBonusSampahLogam() {
        val setoran = SetoranSampah(
            namaPenyetor = "Siti",
            jenisSampah = "logam",
            berat = 2.0,
            bersihDanTerpilah = true
        )

        val hasil = hitungTotalSetoran(setoran)

        assertEquals(17600.0, hasil, 0.01)
    }
}