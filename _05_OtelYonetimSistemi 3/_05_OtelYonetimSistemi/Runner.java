package com.barisd._05_OtelYonetimSistemi;

import com.barisd._05_OtelYonetimSistemi.entity.Musteri;
import com.barisd._05_OtelYonetimSistemi.entity.Oda;
import com.barisd._05_OtelYonetimSistemi.entity.Rezervasyon;
import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;
import com.barisd._05_OtelYonetimSistemi.enums.OdemeDurumu;
import com.barisd._05_OtelYonetimSistemi.enums.RezervasyonDurumu;

import com.barisd._05_OtelYonetimSistemi.repository.MusteriRepository;
import com.barisd._05_OtelYonetimSistemi.repository.OdaRepository;
import com.barisd._05_OtelYonetimSistemi.repository.RezervasyonRepository;

import com.barisd._05_OtelYonetimSistemi.dto.MusteriKonaklamaDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Runner {
    private static final MusteriRepository musteriRepository = new MusteriRepository();
    private static final OdaRepository odaRepository = new OdaRepository();
    private static final RezervasyonRepository rezervasyonRepository = new RezervasyonRepository();


    public static void main(String[] args) {
        musteriIslemleri();
        odaIslemleri();
        rezervasyonIslemleri();
        gelismisSorgulariCagir();
        // EntityManager kapanışı
        odaRepository.close();
        // EntityManager kapanışı
        musteriRepository.close();
        // EntityManager kapanışı
        rezervasyonRepository.close();

    }


    private static void musteriIslemleri() {
        initializeMusteri();
        // Tüm müşterileri getir
        System.out.println("\nTüm Müşteriler:");
        musteriRepository.findAll().forEach(System.out::println);
        // ID ile bul
        System.out.println("\nID ile bul (id=1):");
        musteriRepository.findById(1L).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Müşteri bulunamadı.")
        );

        // TC Kimlik No ile bul
        System.out.println("\nTC Kimlik No ile bul (22222222222):");
        musteriRepository.findByTcKimlikNo("22222222222").ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Müşteri bulunamadı.")
        );

        // Şehre göre ara
        System.out.println("\nŞehir ile ara (Ankara):");
        musteriRepository.findBySehir("Ankara").forEach(System.out::println);

        // Ad ya da Soyad içerenleri bul
        System.out.println("\nAd veya Soyad 'me' içerenler:");
        musteriRepository.findByAdOrSoyadContaining("me").forEach(System.out::println);

        // VIP Müşterileri getir
        System.out.println("\nVIP Müşteriler:");
        musteriRepository.findAllVipMusteri().forEach(System.out::println);

        // DTO kullanımı
        System.out.println("\nTüm müşteriler DTO formatında:");
        musteriRepository.findAllDTO().forEach(System.out::println);

        // Güncelleme
        System.out.println("\nGüncellenmiş not:");
        musteriRepository.findByTcKimlikNo("11111111111").ifPresent(m -> {
            m.setNotlar("\nGüncellenmiş not.");
            Musteri updated = musteriRepository.update(m);
            System.out.println("\nGüncellenmiş müşteri: " + updated);
        });

        // Silme işlemi
        boolean deleted = musteriRepository.deleteById(3L); // 3 id'li müşteri silinir
        System.out.println("\nMüşteri 3 silindi mi? " + deleted);

        // Tüm müşteri listesini tekrar yazdır
        System.out.println("\nGüncel müşteri listesi:");
        musteriRepository.findAll().forEach(System.out::println);
    }

    private static void odaIslemleri() {
        initializeOda();

        System.out.println("\nTüm Odalar:");
        odaRepository.findAll().forEach(System.out::println);

        System.out.println("\nID ile bul (id=1):");
        odaRepository.findById(1L).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Oda bulunamadı.")
        );

        System.out.println("\nFiyat aralığı (500 - 1500):");
        odaRepository.findByFiyatAraligi(BigDecimal.valueOf(500), BigDecimal.valueOf(1500))
                .forEach(System.out::println);

        System.out.println("\nKapasite=2 ve aktif=true odalar:");
        odaRepository.findByKapasiteAndAktif(2, true).forEach(System.out::println);

        System.out.println("\nOda Tipi STANDART:");
        odaRepository.findByTip(OdaTipi.STANDART).forEach(System.out::println);

        System.out.println("\nKat sırasına göre odalar:");
        odaRepository.findAllOrderByKat().forEach(System.out::println);

        System.out.println("\nTüm odalar DTO formatında:");
        odaRepository.findAllDTO2().forEach(System.out::println);

        // Güncelleme örneği
        System.out.println("\nGünlük Fiyat 999 yap");
        odaRepository.findById(1L).ifPresent(oda -> {
            oda.setGunlukFiyat(BigDecimal.valueOf(999));
            oda.setAktif(false);
            Oda updated = odaRepository.update(oda);
            System.out.println("\nGüncellenmiş oda: " + updated);
        });

        // Silme örneği
        boolean deleted = odaRepository.deleteById(3L);
        System.out.println("\nOda 3 silindi mi? " + deleted);

        System.out.println("\nGüncel oda listesi:");
        odaRepository.findAll().forEach(System.out::println);

        //

    }

    private static void rezervasyonIslemleri() {
        initializeRezervasyon();

        System.out.println("\nTüm Rezervasyonlar:");
        rezervasyonRepository.findAll().forEach(System.out::println);

        System.out.println("\nID ile rezervasyon bul (id=1):");
        rezervasyonRepository.findById(1L).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Rezervasyon bulunamadı.")
        );

        LocalDate bugun = LocalDate.now();
        System.out.println("\nBelirtilen tarih aralığındaki rezervasyonlar (" + bugun.plusDays(1) + " - " + bugun.plusDays(5) + "):");
        rezervasyonRepository.findByTarihAraligi(bugun.plusDays(1), bugun.plusDays(5)).forEach(System.out::println);

        System.out.println("\nMüşteri ID'sine göre rezervasyonlar (Müşteri ID=2):");
        rezervasyonRepository.findByMusteriId(2L).forEach(System.out::println);

        System.out.println("\nRezervasyon Durumu 'ONAYLANDI' olanlar:");
        rezervasyonRepository.findByDurum(RezervasyonDurumu.ONAYLANDI).forEach(System.out::println);

        System.out.println("\nŞu anda konaklayan müşterilerin rezervasyonları:");
        rezervasyonRepository.findCurrentlyStaying().forEach(System.out::println);

        System.out.println("\nTüm rezervasyonlar DTO formatında:");
        rezervasyonRepository.findAllDTO().forEach(System.out::println);

        // Rezervasyon güncelleme örneği
        rezervasyonRepository.findById(2L).ifPresent(rezervasyon -> {
            rezervasyon.setDurum(RezervasyonDurumu.IPTAL_EDILDI);
            rezervasyon.setIptalTarihi(LocalDateTime.now());

            Rezervasyon updatedRezervasyon = rezervasyonRepository.update(rezervasyon);
            System.out.println("\nGüncellenmiş rezervasyon (iptal edildi): " + updatedRezervasyon);
        });

        // Rezervasyon silme örneği
        boolean deleted = rezervasyonRepository.deleteById(5L);
        System.out.println("\nRezervasyon 5 silindi mi? " + deleted);

        System.out.println("\nGüncel rezervasyon listesi:");
        rezervasyonRepository.findAll().forEach(System.out::println);
    }

    private static void initializeMusteri() {

        Musteri musteri1 = new Musteri();
        musteri1.setTcKimlikNo("11111111111");
        musteri1.setAd("Arda");
        musteri1.setSoyad("Güler");
        musteri1.setAdres("İstanbul, Kadıköy");
        musteri1.setSehir("İstanbul");
        musteri1.setVipMusteri(true);
        musteri1.setNotlar("VIP müşteri");

        try {
            Musteri savedMusteri = musteriRepository.save(musteri1);
            System.out.println("Müşteri 1 başarıyla kaydedildi: " + savedMusteri.getAd() + " " + savedMusteri.getSoyad());
        } catch (Exception e) {
            System.out.println("Müşteri 1 kaydedilemedi.");
            System.out.println(e.getMessage());
        }


        Musteri musteri2 = new Musteri();
        musteri2.setTcKimlikNo("22222222222");
        musteri2.setAd("Ali");
        musteri2.setSoyad("Yılmaz");
        musteri2.setTelefon("5552345678");
        musteri2.setEmail("ali.yilmaz@gmail.com");
        musteri2.setAdres("Ankara, Çankaya");
        musteri2.setSehir("Ankara");
        musteri2.setVipMusteri(false);
        musteri2.setNotlar("Sadece birkaç gün kaldı.");

        try {
            Musteri savedMusteri = musteriRepository.save(musteri2);
            System.out.println("Müşteri 2 başarıyla kaydedildi: " + savedMusteri.getAd() + " " + savedMusteri.getSoyad());
        } catch (Exception e) {
            System.out.println("Müşteri 2 kaydedilemedi.");
            System.out.println(e.getMessage());
        }

        Musteri musteri3 = new Musteri();
        musteri3.setTcKimlikNo("33333333333");
        musteri3.setAd("Ayşe");
        musteri3.setSoyad("Kara");
        musteri3.setTelefon("5553456789");
        musteri3.setEmail("ayse.kara@gmail.com");
        musteri3.setAdres("Bursa, Osmangazi");
        musteri3.setSehir("Bursa");
        musteri3.setVipMusteri(true);
        musteri3.setNotlar("Kısa süreli konaklama");

        try {
            Musteri savedMusteri = musteriRepository.save(musteri3);
            System.out.println("Müşteri 3 başarıyla kaydedildi: " + savedMusteri.getAd() + " " + savedMusteri.getSoyad());
        } catch (Exception e) {
            System.out.println("Müşteri 3 kaydedilemedi.");
            System.out.println(e.getMessage());
        }

        Musteri musteri4 = new Musteri();
        musteri4.setTcKimlikNo("44444444444");
        musteri4.setAd("Mehmet");
        musteri4.setSoyad("Çelik");
        musteri4.setTelefon("5554567890");
        musteri4.setEmail("mehmet.celik@gmail.com");
        musteri4.setAdres("İzmir, Karşıyaka");
        musteri4.setSehir("İzmir");
        musteri4.setVipMusteri(true);
        musteri4.setNotlar("VIP müşteri");

        try {
            Musteri savedMusteri = musteriRepository.save(musteri4);
            System.out.println("Müşteri 4 başarıyla kaydedildi: " + savedMusteri.getAd() + " " + savedMusteri.getSoyad());
        } catch (Exception e) {
            System.out.println("Müşteri 4 kaydedilemedi.");
            System.out.println(e.getMessage());
        }

        Musteri musteri5 = new Musteri();
        musteri5.setTcKimlikNo("55555555555");
        musteri5.setAd("Zeynep");
        musteri5.setSoyad("Öztürk");
        musteri5.setTelefon("5555678901");
        musteri5.setEmail("zeynep.ozturk@gmail.com");
        musteri5.setAdres("Antalya, Konyaaltı");
        musteri5.setSehir("Antalya");
        musteri5.setVipMusteri(false);
        musteri5.setNotlar("Yaz tatili için geldim.");

        try {
            Musteri savedMusteri = musteriRepository.save(musteri5);
            System.out.println("Müşteri 5 başarıyla kaydedildi: " + savedMusteri.getAd() + " " + savedMusteri.getSoyad());
        } catch (Exception e) {
            System.out.println("Müşteri 5 kaydedilemedi.");
            System.out.println(e.getMessage());
        }


    }

    private static void initializeOda() {
        Oda oda1 = new Oda(null, "101", OdaTipi.STANDART, 2, 2,
                new BigDecimal("750"), 20, false, true, false,
                true, true, true, "Standart çift kişilik oda", true);

        Oda oda2 = new Oda(null, "102", OdaTipi.DELUXE, 1, 2,
                BigDecimal.valueOf(1100), 25, true, true, true,
                true, true, true, "Deniz manzaralı deluxe oda", true);

        Oda oda3 = new Oda(null, "201", OdaTipi.SUIT, 2, 3,
                BigDecimal.valueOf(1800), 30, true, true, true,
                true, true, true, "Lüks suit oda", true);

        Oda oda4 = new Oda(null, "202", OdaTipi.AILE, 2, 4,
                BigDecimal.valueOf(2000), 35, true, false, true,
                false, true, true, "Aile odası geniş kullanım alanı sunar", true);

        Oda oda5 = new Oda(null, "301", OdaTipi.KRAL, 3, 5,
                BigDecimal.valueOf(3000), 50, true, true, true,
                true, true, true, "Kral dairesi: En üst düzey konfor", true);

        Oda oda6 = new Oda(null, "302", OdaTipi.STANDART, 3, 1,
                BigDecimal.valueOf(650), 18, false, false, false,
                false, true, true, "Ekonomik tek kişilik oda", true);

        Oda oda7 = new Oda(null, "303", OdaTipi.DELUXE, 3, 2,
                BigDecimal.valueOf(1250), 28, true, true, false,
                true, true, true, "Balkonlu deluxe çift kişilik oda", true);

        try {
            odaRepository.save(oda1);
        } catch (Exception e) {
            System.out.println("Oda 1 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda2);
        } catch (Exception e) {
            System.out.println("Oda 2 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda3);
        } catch (Exception e) {
            System.out.println("Oda 3 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda4);
        } catch (Exception e) {
            System.out.println("Oda 4 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda5);
        } catch (Exception e) {
            System.out.println("Oda 5 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda6);
        } catch (Exception e) {
            System.out.println("Oda 6 hatası: " + e.getMessage());
        }
        try {
            odaRepository.save(oda7);
        } catch (Exception e) {
            System.out.println("Oda 7 hatası: " + e.getMessage());
        }
    }

    private static void initializeRezervasyon() {

        Oda oda1 = odaRepository.findById(1L).orElseThrow();
        Oda oda2 = odaRepository.findById(2L).orElseThrow();
        Oda oda4 = odaRepository.findById(4L).orElseThrow();
        Oda oda5 = odaRepository.findById(5L).orElseThrow();

        Rezervasyon rezervasyon1 = new Rezervasyon(null, "R1", 1L, oda1.getId(), LocalDate.now().minusDays(10), LocalDate.now().plusDays(3), 2, oda1.getGunlukFiyat(), oda1.getGunlukFiyat().multiply(BigDecimal.valueOf(2)), OdemeDurumu.ODENDI, BigDecimal.valueOf(1500), RezervasyonDurumu.TAMAMLANDI, null, null, LocalDateTime.now(), LocalDateTime.now(), "Erken rezervasyon indirimi uygulandı.", null, null, 2);
        Rezervasyon rezervasyon2 = new Rezervasyon(null, "R2", 2L, oda2.getId(), LocalDate.now(), LocalDate.now().plusDays(10), 1, oda2.getGunlukFiyat(), oda2.getGunlukFiyat().multiply(BigDecimal.valueOf(5)), OdemeDurumu.ODENMEDI, null, RezervasyonDurumu.IPTAL_EDILDI, null, null, LocalDateTime.now(), LocalDateTime.now(), null, null, null, 5);
        Rezervasyon rezervasyon3 = new Rezervasyon(null, "R3", 3L, oda2.getId(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), 3, oda2.getGunlukFiyat(), oda2.getGunlukFiyat().multiply(BigDecimal.valueOf(2)), OdemeDurumu.KISMEN_ODENDI, BigDecimal.valueOf(2000), RezervasyonDurumu.ONAYLANDI, null, null, LocalDateTime.now(), LocalDateTime.now(), "Ek yatak talep edildi.", null, null, 2);
        Rezervasyon rezervasyon4 = new Rezervasyon(null, "R4", 4L, oda4.getId(), LocalDate.now().plusDays(7), LocalDate.now().plusDays(9), 4, oda4.getGunlukFiyat(), oda4.getGunlukFiyat().multiply(BigDecimal.valueOf(2)), OdemeDurumu.ODENDI, BigDecimal.valueOf(4000), RezervasyonDurumu.ONAY_BEKLIYOR, null, null, LocalDateTime.now(), LocalDateTime.now(), null, "Kahvaltı dahil.", BigDecimal.valueOf(200), 2);
        Rezervasyon rezervasyon5 = new Rezervasyon(null, "R5", 5L, oda5.getId(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(6), 2, oda5.getGunlukFiyat(), oda5.getGunlukFiyat().multiply(BigDecimal.valueOf(3)), OdemeDurumu.ODENDI, BigDecimal.valueOf(9000), RezervasyonDurumu.TAMAMLANDI, null, null, LocalDateTime.now(), LocalDateTime.now(), "VIP misafir.", "Özel karşılama ayarlandı.", BigDecimal.valueOf(500), 3);
        Rezervasyon rezervasyon6 = new Rezervasyon(null, "R6", 5L, oda1.getId(), LocalDate.now().minusDays(10), LocalDate.now().plusDays(3), 2, oda1.getGunlukFiyat(), oda1.getGunlukFiyat().multiply(BigDecimal.valueOf(3)), OdemeDurumu.ODENDI, BigDecimal.valueOf(1000), RezervasyonDurumu.TAMAMLANDI, null, null, LocalDateTime.now(), LocalDateTime.now(), "Erken rezervasyon indirimi uygulandı.", null, null, 3);
        //rezervasyonRepository.save(rezervasyon6);
        try {
            rezervasyonRepository.save(rezervasyon1);
        } catch (Exception e) {
            System.out.println("Rezervasyon 1 hatası: " + e.getMessage());
        }
        try {
            rezervasyonRepository.save(rezervasyon2);
        } catch (Exception e) {
            System.out.println("Rezervasyon 2 hatası: " + e.getMessage());
        }
        try {
            rezervasyonRepository.save(rezervasyon3);
        } catch (Exception e) {
            System.out.println("Rezervasyon 3 hatası: " + e.getMessage());
        }
        try {
            rezervasyonRepository.save(rezervasyon4);
        } catch (Exception e) {
            System.out.println("Rezervasyon 4 hatası: " + e.getMessage());
        }
        try {
            rezervasyonRepository.save(rezervasyon5);
        } catch (Exception e) {
            System.out.println("Rezervasyon 5 hatası: " + e.getMessage());
        }
        rezervasyonRepository.save(rezervasyon6);
    }

    private static void gelismisSorgulariCagir() {
        LocalDate bugun = LocalDate.now();

        System.out.println("\n--- Gelişmiş Sorgu Sonuçları ---");

        // 1. Belirli bir tarihe göre müsait odalar
        LocalDate kontrolTarihi = bugun.plusDays(3);
        System.out.println("\nBelirtilen tarihte (" + kontrolTarihi + ") müsait odalar:");
        odaRepository.findMusaitOdalar(kontrolTarihi).forEach(System.out::println);

        // 2. Son 30 gün içinde yapılan rezervasyonlar
        System.out.println("\nSon 30 gün içinde yapılan rezervasyonlar RezervasyonRequestDto:");
        rezervasyonRepository.findSonOtuzGunRezervasyonlari().forEach(System.out::println);


        // 3. En çok rezervasyon yapılan 3 oda
        System.out.println("\nEn çok rezervasyon yapılan 3 oda:");
        rezervasyonRepository.findEnCokRezervasyonYapilanOdalar().forEach(sonuc ->
                System.out.println("Oda No: " + sonuc.odaNo() + ", Tipi: " + sonuc.tip() + ", Rezervasyon Sayısı: " + sonuc.rezervasyonSayisi())
        );

        // 4. Belirli bir ay içindeki toplam ciro
        int yil = bugun.getYear();
        int ay = bugun.getMonthValue();
        BigDecimal aylikCiro = rezervasyonRepository.calculateAylikCiro(yil, ay);
        System.out.println("\n" + yil + " yılı " + ay + ". ayındaki toplam ciro: " + aylikCiro);

        // 5. Belirli bir müşterinin toplam harcama tutarı
        Long musteriId = 1L;
        BigDecimal musteriToplamHarcama = rezervasyonRepository.calculateMusteriToplamHarcama(musteriId);
        System.out.println("\nMüşteri ID " + musteriId + " toplam harcaması: " + musteriToplamHarcama);

        // 6. Ortalama konaklama süresi
        Double ortalamaKonaklama = rezervasyonRepository.calculateOrtalamaKonaklamaSuresi();
        System.out.println("\nOrtalama konaklama süresi (gün): " + ortalamaKonaklama);

        // 7. Hiç rezervasyonu olmayan odalar
        System.out.println("\nHiç rezervasyonu olmayan odalar:");
        odaRepository.findRezervasyonsuzOdalar().forEach(System.out::println);

        // 8. Belirli bir tarih aralığında hem giriş hem çıkış yapan müşteriler (örnek bir tarih aralığı verildi)
        LocalDate tarihBaslangic = bugun.minusDays(20);
        LocalDate tarihBitis = bugun.plusDays(20);
        System.out.println("\n" + tarihBaslangic + " - " + tarihBitis + " tarih aralığında giriş çıkış yapan müşteriler:");
        rezervasyonRepository.findGirisCikisAraligiMusterilerDTO(tarihBaslangic, tarihBitis).forEach(System.out::println);

        //9 En uzun süre konaklayan 2 müşteri
        System.out.println("\nEn uzun süre konaklayan 2 müşteri (DTO ile):");
        List<MusteriKonaklamaDTO> enUzunKonaklayanlar = rezervasyonRepository.findEnUzunKonaklayanMusteriler();
        enUzunKonaklayanlar.forEach(musteri ->
                System.out.println("Ad: " + musteri.ad() + ", Soyad: " + musteri.soyad() + ", TC Kimlik No: " + musteri.tcKimlikNo() + ", Toplam Konaklama: " + musteri.toplamKonaklama() + " gün")
        );
        // 10. Rezervasyonları durum bazında gruplayarak sayılarını verme
        System.out.println("\nRezervasyonların durumlarına göre sayıları:");
        rezervasyonRepository.countRezervasyonlarByDurum().forEach(sonuc ->
                System.out.println("Durum: " + sonuc[0] + ", Sayı: " + sonuc[1])
        );

    }
}
