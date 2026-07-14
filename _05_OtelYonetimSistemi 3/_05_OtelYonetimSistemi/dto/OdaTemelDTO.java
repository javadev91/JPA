package com.barisd._05_OtelYonetimSistemi.dto;

import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;

public record  OdaTemelDTO (
    String odaNo,
    OdaTipi tip,
    Long rezervasyonSayisi
){
}
