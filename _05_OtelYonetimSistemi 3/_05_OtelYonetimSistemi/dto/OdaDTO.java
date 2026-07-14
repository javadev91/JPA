package com.barisd._05_OtelYonetimSistemi.dto;

import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;

import java.math.BigDecimal;

public record OdaDTO(
        Long id,
        String odaNo,
        OdaTipi tip,
        Integer kat,
        Integer kapasite,
        BigDecimal gunlukFiyat,
        Boolean denizManzarali,
        Boolean aktif,
        String ozellikler
) {
}
