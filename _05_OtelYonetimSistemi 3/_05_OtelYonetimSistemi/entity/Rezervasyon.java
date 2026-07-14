package com.barisd._05_OtelYonetimSistemi.entity;


import com.barisd._05_OtelYonetimSistemi.enums.OdemeDurumu;
import com.barisd._05_OtelYonetimSistemi.enums.RezervasyonDurumu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rezervasyonlar")
public class Rezervasyon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rezervasyon_kodu", unique = true, nullable = false, length = 20)
    private String rezervasyonKodu;

    @Column(name = "musteri_id", nullable = false)
    private Long musteriId;

    @Column(name = "oda_id", nullable = false)
    private Long odaId;

    @Column(name = "giris_tarihi", nullable = false)
    private LocalDate girisTarihi;

    @Column(name = "cikis_tarihi", nullable = false)
    private LocalDate cikisTarihi;

    @Column(name = "kisi_sayisi", nullable = false)
    private Integer kisiSayisi;

    @Column(name = "gunluk_fiyat")
    private BigDecimal gunlukFiyat;

    @Column(name = "toplam_fiyat")
    private BigDecimal toplamFiyat;

    @Enumerated(EnumType.STRING)
    @Column(name = "odeme_durumu")
    private OdemeDurumu odemeDurumu;

    @Column(name = "odenen_tutar")
    private BigDecimal odenenTutar;

    @Enumerated(EnumType.STRING)
    private RezervasyonDurumu durum;

    @Column(name = "iptal_tarihi")
    private LocalDateTime iptalTarihi;

    @Column(name = "iptal_nedeni", length = 255)
    private String iptalNedeni;

    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi;

    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi;

    @Column(length = 500)
    private String notlar;

    @Column(name = "ekstra_hizmetler", length = 500)
    private String ekstraHizmetler;

    @Column(name = "ekstra_hizmet_ucretleri")
    private BigDecimal ekstraHizmetUcretleri;

    //@Transient
    @Formula("EXTRACT(DAY FROM (cikis_tarihi - giris_tarihi))")
    @Column(name = "konaklama_suresi")
    private Integer konaklamaSuresi;
}