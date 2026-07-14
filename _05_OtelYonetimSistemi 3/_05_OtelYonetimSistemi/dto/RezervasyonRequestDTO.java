package com.barisd._05_OtelYonetimSistemi.dto;

import java.time.LocalDate;

public record RezervasyonRequestDTO(
        Long musteriId,
        Long odaId,
        LocalDate girisTarihi,
        LocalDate cikisTarihi,
        Integer kisiSayisi,
        String ekstraHizmetler
) {
}