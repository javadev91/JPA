package com.barisd._05_OtelYonetimSistemi.enums;

public enum OdaTipi {
    STANDART("Standart Oda"),
    DELUXE("Deluxe Oda"),
    SUIT("Suit Oda"),
    AILE("Aile Odası"),
    KRAL("Kral Dairesi");

    private final String description;

    OdaTipi(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
