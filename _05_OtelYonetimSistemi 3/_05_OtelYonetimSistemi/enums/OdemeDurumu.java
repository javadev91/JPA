package com.barisd._05_OtelYonetimSistemi.enums;

public enum OdemeDurumu {
    ODENMEDI("Ödenmedi"),
    KISMEN_ODENDI("Kısmen Ödendi"),
    ODENDI("Ödendi");

    private final String description;

    OdemeDurumu(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}