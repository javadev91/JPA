package com.barisd._05_OtelYonetimSistemi.enums;

public enum RezervasyonDurumu {
    ONAY_BEKLIYOR("Onay Bekliyor"),
    ONAYLANDI("Onaylandı"),
    IPTAL_EDILDI("İptal Edildi"),
    TAMAMLANDI("Tamamlandı");

    private final String description;

    RezervasyonDurumu(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}