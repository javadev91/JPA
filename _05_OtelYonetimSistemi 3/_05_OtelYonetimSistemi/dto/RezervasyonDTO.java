package com.barisd._05_OtelYonetimSistemi.dto;

import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;
import com.barisd._05_OtelYonetimSistemi.enums.OdemeDurumu;
import com.barisd._05_OtelYonetimSistemi.enums.RezervasyonDurumu;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RezervasyonDTO(
        Long id,
        String rezervasyonKodu,
        String musteriAdSoyad,
        String odaNo,
        OdaTipi odaTipi,
        LocalDate girisTarihi,
        LocalDate cikisTarihi,
        Integer konaklamaSuresi,
        BigDecimal toplamFiyat,
        RezervasyonDurumu durum,
        OdemeDurumu odemeDurumu
) {
}
