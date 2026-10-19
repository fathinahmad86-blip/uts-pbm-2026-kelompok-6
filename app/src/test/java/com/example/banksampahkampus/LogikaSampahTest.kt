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
    @Test
    fun testHitungTotalBerat() {
        val daftar = listOf(
            SetoranSampah("Budi", "plastik", 2.0, false),
            SetoranSampah("Andi", "kertas", 3.0, false),
            SetoranSampah("Siti", "plastik", 4.0, true)
        )

        val hasil = hitungTotalBerat(daftar, "plastik")

        assertEquals(6.0, hasil, 0.01)
    }

    @Test
    fun testHitungSaldoPenyetor() {
        val daftar = listOf(
            SetoranSampah("Budi", "plastik", 2.0, false),
            SetoranSampah(" budi ", "kertas", 1.0, false)
        )

        val hasil = hitungSaldoPenyetor(daftar)

        assertEquals(8000.0, hasil["budi"]!!, 0.01)
    }

    @Test
    fun testPenyetorSaldoTerbesar() {
        val daftar = listOf(
            SetoranSampah("Budi", "plastik", 2.0, false),
            SetoranSampah("Siti", "logam", 3.0, false)
        )

        val hasil = penyetorSaldoTerbesar(daftar)

        assertEquals("siti", hasil?.first)
        assertEquals(24000.0, hasil?.second ?: 0.0, 0.01)
    }
}