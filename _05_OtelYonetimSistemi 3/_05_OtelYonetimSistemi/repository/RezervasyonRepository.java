package com.barisd._05_OtelYonetimSistemi.repository;

import com.barisd._05_OtelYonetimSistemi.dto.*;
import com.barisd._05_OtelYonetimSistemi.entity.Rezervasyon;
import com.barisd._05_OtelYonetimSistemi.enums.RezervasyonDurumu;
import com.barisd._05_OtelYonetimSistemi.utility.JpaUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RezervasyonRepository extends RepositoryManager<Rezervasyon, Long>{

    private EntityManager em;

    public RezervasyonRepository() {
        super(Rezervasyon.class);
        this.em = JpaUtility.getEntityManager();
    }

    public List<Rezervasyon> findByTarihAraligi(LocalDate baslangic, LocalDate bitis) {
        return em.createQuery("""
        SELECT r FROM Rezervasyon r 
        WHERE r.girisTarihi >= :start AND r.cikisTarihi <= :end
    """, Rezervasyon.class)
                .setParameter("start", baslangic)
                .setParameter("end", bitis)
                .getResultList();
    }

    public List<Rezervasyon> findByMusteriId(Long musteriId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rezervasyon> cq = cb.createQuery(Rezervasyon.class);
        Root<Rezervasyon> root = cq.from(Rezervasyon.class);

        cq.where(cb.equal(root.get("musteriId"), musteriId));

        return em.createQuery(cq).getResultList();
    }public List<Rezervasyon> findByDurum(RezervasyonDurumu durum) {
        return em.createQuery("""
        SELECT r FROM Rezervasyon r 
        WHERE r.durum = :durum
    """, Rezervasyon.class)
                .setParameter("durum", durum)
                .getResultList();
    }

    public List<Rezervasyon> findCurrentlyStaying() {
        String sql = """
        SELECT * FROM rezervasyonlar 
        WHERE CURRENT_DATE BETWEEN giris_tarihi AND cikis_tarihi
    """;
        return em.createNativeQuery(sql, Rezervasyon.class).getResultList();
    }

    public List<RezervasyonDTO> findAllDTO() {
        return em.createQuery("""
        SELECT new com.barisd._05_OtelYonetimSistemi.dto.RezervasyonDTO(
            r.id,
            r.rezervasyonKodu,
            CONCAT(m.ad, ' ', m.soyad),
            o.odaNo,
            o.tip,
            r.girisTarihi,
            r.cikisTarihi,
            r.konaklamaSuresi,
            r.toplamFiyat,
            r.durum,
            r.odemeDurumu
        )
        FROM Rezervasyon r
        JOIN Musteri m ON r.musteriId = m.id
        JOIN Oda o ON r.odaId = o.id
    """, RezervasyonDTO.class).getResultList();
    }

    public List<RezervasyonRequestDTO> findSonOtuzGunRezervasyonlari() {
        LocalDateTime otuzGunOnce = LocalDateTime.now().minusDays(30);
        return em.createQuery("""
        SELECT new com.barisd._05_OtelYonetimSistemi.dto.RezervasyonRequestDTO(
            r.musteriId,
            r.odaId,
            r.girisTarihi,
            r.cikisTarihi,
            r.kisiSayisi,
            r.ekstraHizmetler
        )
        FROM Rezervasyon r
        WHERE r.olusturmaTarihi >= :otuzGunOnce
        ORDER BY r.olusturmaTarihi DESC
    """, RezervasyonRequestDTO.class)
                .setParameter("otuzGunOnce", otuzGunOnce)
                .getResultList();
    }

    public List<OdaTemelDTO> findEnCokRezervasyonYapilanOdalar() {
        return em.createQuery("""
        SELECT new com.barisd._05_OtelYonetimSistemi.dto.OdaTemelDTO(
            o.odaNo, 
            o.tip, 
            COUNT(r)
        )
        FROM Rezervasyon r
        JOIN Oda o ON r.odaId = o.id
        GROUP BY o.id, o.odaNo, o.tip
        ORDER BY COUNT(r) DESC
    """, OdaTemelDTO.class)
                .setMaxResults(3)
                .getResultList();
    }


    public BigDecimal calculateAylikCiro(int yil, int ay) {
        LocalDate baslangic = LocalDate.of(yil, ay, 1);
        LocalDate bitis = baslangic.plusMonths(1);
        return em.createQuery("""
        SELECT SUM(r.odenenTutar)
        FROM Rezervasyon r
        WHERE r.durum = :tamamlandi
        AND r.cikisTarihi >= :baslangic
        AND r.cikisTarihi <= :bitis
    """, BigDecimal.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .setParameter("tamamlandi", RezervasyonDurumu.TAMAMLANDI)
                .getSingleResult();
    }

    public BigDecimal calculateMusteriToplamHarcama(Long musteriId) {
        return em.createQuery("""
        SELECT SUM(r.odenenTutar)
        FROM Rezervasyon r
        WHERE r.musteriId = :musteriId
    """, BigDecimal.class)
                .setParameter("musteriId", musteriId)
                .getSingleResult();
    }

    public Double calculateOrtalamaKonaklamaSuresi() {
        return em.createQuery("""
        SELECT AVG(r.konaklamaSuresi)
        FROM Rezervasyon r
        WHERE r.durum = :tamamlandi
    """, Double.class)
                .setParameter("tamamlandi", RezervasyonDurumu.TAMAMLANDI) // Sadece tamamlanmış rezervasyonları hesaba katalım
                .getSingleResult();
    }

    public List<MusteriDTO> findGirisCikisAraligiMusterilerDTO(LocalDate baslangic, LocalDate bitis) {
        return em.createQuery("""
        SELECT DISTINCT new com.barisd._05_OtelYonetimSistemi.dto.MusteriDTO(
            m.id,
            m.tcKimlikNo,
            m.ad,
            m.soyad,
            m.telefon,
            m.email,
            m.sehir,
            m.vipMusteri
        )
        FROM Rezervasyon r
        JOIN Musteri m ON r.musteriId = m.id
        WHERE r.girisTarihi BETWEEN :baslangic AND :bitis
        AND r.cikisTarihi BETWEEN :baslangic AND :bitis
    """, MusteriDTO.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getResultList();
    }


    public List<MusteriKonaklamaDTO> findEnUzunKonaklayanMusteriler() {
        return em.createQuery("""
        SELECT new com.barisd._05_OtelYonetimSistemi.dto.MusteriKonaklamaDTO(
            m.ad,
            m.soyad,
            m.tcKimlikNo,
            SUM(r.konaklamaSuresi)
        )
        FROM Rezervasyon r
        JOIN Musteri m ON r.musteriId = m.id
        GROUP BY m.tcKimlikNo, m.ad, m.soyad
        ORDER BY SUM(r.konaklamaSuresi) DESC
    """)
                .setMaxResults(2)
                .getResultList();
    }

    public List<Object[]> countRezervasyonlarByDurum() {
        return em.createQuery("""
        SELECT r.durum, COUNT(r)
        FROM Rezervasyon r
        GROUP BY r.durum
    """)
                .getResultList();
    }

}