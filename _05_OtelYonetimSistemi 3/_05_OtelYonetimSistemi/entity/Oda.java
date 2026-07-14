package com.barisd._05_OtelYonetimSistemi.entity;

import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "odalar")
public class Oda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oda_no", unique = true, nullable = false, length = 10)
    private String odaNo;

    @Enumerated(EnumType.STRING)
    private OdaTipi tip;

    private Integer kat;

    private Integer kapasite;

    @Column(name = "gunluk_fiyat", nullable = false)
    private BigDecimal gunlukFiyat;

    private Integer metrekare;

    @Column(name = "deniz_manzarali")
    private Boolean denizManzarali;

    @Column(name = "balkon_var")
    private Boolean balkonVar;

    @Column(name = "minibar_var")
    private Boolean minibarVar;

    @Column(name = "klima_var")
    private Boolean klimaVar;

    @Column(name = "tv_var")
    private Boolean tvVar;

    @Column(name = "wifi_var")
    private Boolean wifiVar;

    @Column(length = 500)
    private String aciklama;

    private Boolean aktif;
}