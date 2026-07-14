package com.barisd._05_OtelYonetimSistemi.dto;

public record MusteriDTO(
        Long id,
        String tcKimlikNo,
        String ad,
        String soyad,
        String telefon,
        String email,
        String sehir,
        Boolean vipMusteri
) {
}