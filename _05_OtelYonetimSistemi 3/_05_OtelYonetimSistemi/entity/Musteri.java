package com.barisd._05_OtelYonetimSistemi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_musteri")
public class Musteri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tc_kimlik_no", unique = true, nullable = false, length = 11)
    private String tcKimlikNo;

    @Column(nullable = false, length = 50)
    private String ad;

    @Column(nullable = false, length = 50)
    private String soyad;

    @Column(length = 15)
    private String telefon;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String adres;

    @Column(length = 50)
    private String sehir;

    @Column(name = "kayit_tarihi")
    private LocalDate kayitTarihi;

    @Column(name = "son_giris_tarihi")
    private LocalDate sonGirisTarihi;

    @Column(name = "vip_musteri")
    private Boolean vipMusteri;

    @Column(length = 500)
    private String notlar;

    // Otomatik tarih ayarı
    @PrePersist
    public void prePersist() {
        if (this.kayitTarihi == null) {
            this.kayitTarihi = LocalDate.now();
        }
            this.sonGirisTarihi = LocalDate.now();
    }
}
