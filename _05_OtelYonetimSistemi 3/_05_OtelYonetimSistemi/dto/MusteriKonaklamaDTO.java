package com.barisd._05_OtelYonetimSistemi.dto;

public record MusteriKonaklamaDTO(
        String ad,
        String soyad,
        String tcKimlikNo,
        Long toplamKonaklama
) { }
