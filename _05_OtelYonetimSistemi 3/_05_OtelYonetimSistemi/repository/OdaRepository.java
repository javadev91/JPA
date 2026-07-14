package com.barisd._05_OtelYonetimSistemi.repository;

import com.barisd._05_OtelYonetimSistemi.dto.OdaDTO;
import com.barisd._05_OtelYonetimSistemi.entity.Oda;
import com.barisd._05_OtelYonetimSistemi.enums.OdaTipi;
import com.barisd._05_OtelYonetimSistemi.utility.JpaUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OdaRepository extends RepositoryManager<Oda, Long> {

    private EntityManager em;

    public OdaRepository() {
        super(Oda.class);
        this.em = JpaUtility.getEntityManager();
    }


    public List<Oda> findByFiyatAraligi(BigDecimal minFiyat, BigDecimal maxFiyat) {
        TypedQuery<Oda> query = em.createQuery(
                "SELECT o FROM Oda o WHERE o.gunlukFiyat BETWEEN :minFiyat AND :maxFiyat", Oda.class);
        query.setParameter("minFiyat", minFiyat);
        query.setParameter("maxFiyat", maxFiyat);
        return query.getResultList();
    }

    public List<Oda> findByKapasiteAndAktif(Integer kapasite, Boolean aktif) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Oda> cq = cb.createQuery(Oda.class);
        Root<Oda> oda = cq.from(Oda.class);

        Predicate kapasitePredicate = cb.equal(oda.get("kapasite"), kapasite);
        Predicate aktifPredicate = cb.equal(oda.get("aktif"), aktif);
        Predicate andPredicate = cb.and(kapasitePredicate, aktifPredicate);

        cq.where(andPredicate);

        TypedQuery<Oda> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Oda> findAllOrderByKat() {
        TypedQuery<Oda> query = em.createQuery("SELECT o FROM Oda o ORDER BY o.kat ASC", Oda.class);
        return query.getResultList();
    }

    public List<Oda> findByTip(OdaTipi tip) {
        TypedQuery<Oda> query = em.createQuery("SELECT o FROM Oda o WHERE o.tip = :tip", Oda.class);
        query.setParameter("tip", tip);
        return query.getResultList();
    }

    public List<OdaDTO> findAllDTO2() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OdaDTO> cq = cb.createQuery(OdaDTO.class);
        Root<Oda> oda = cq.from(Oda.class);

        // Sadece temel alanları seçiyoruz, özellikleri değil
        cq.select(cb.construct(com.barisd._05_OtelYonetimSistemi.dto.OdaDTO.class,
                oda.get("id"),
                oda.get("odaNo"),
                oda.get("tip"),
                oda.get("kat"),
                oda.get("kapasite"),
                oda.get("gunlukFiyat"),
                oda.get("denizManzarali"),
                oda.get("aktif"),
                cb.literal("") // Geçici olarak boş bir string
        ));

        TypedQuery<OdaDTO> query = em.createQuery(cq);
        List<OdaDTO> odaDTOList = query.getResultList();

        // Java tarafında özellikleri birleştirme
        return odaDTOList.stream().map(dto -> {
            Oda odaEntity = em.find(Oda.class, dto.id()); // Entity'yi tekrar çekiyoruz
            String ozellikler = getOzellikler(odaEntity);
            if (ozellikler.endsWith(", ")) {
                ozellikler = ozellikler.substring(0, ozellikler.length() - 2);
            }
            return new OdaDTO(dto.id(), dto.odaNo(), dto.tip(), dto.kat(), dto.kapasite(), dto.gunlukFiyat(), dto.denizManzarali(), dto.aktif(), ozellikler);
        }).collect(Collectors.toList());
    }

    private static String getOzellikler(Oda odaEntity) {
        StringBuilder ozelliklerBuilder = new StringBuilder();
        if (odaEntity.getBalkonVar()) ozelliklerBuilder.append("Balkon, ");
        if (odaEntity.getMinibarVar()) ozelliklerBuilder.append("Minibar, ");
        if (odaEntity.getKlimaVar()) ozelliklerBuilder.append("Klima, ");
        if (odaEntity.getTvVar()) ozelliklerBuilder.append("TV, ");
        if (odaEntity.getWifiVar()) ozelliklerBuilder.append("Wi-Fi");

        return ozelliklerBuilder.toString();
    }

    public List<Oda> findMusaitOdalar(LocalDate tarih) {
        return em.createQuery("""
        SELECT o FROM Oda o
        WHERE o.aktif = true
        AND NOT EXISTS (
            SELECT r FROM Rezervasyon r
            WHERE r.odaId = o.id
            AND :tarih BETWEEN r.girisTarihi AND r.cikisTarihi
        )
        ORDER BY o.gunlukFiyat ASC
    """, Oda.class)
                .setParameter("tarih", tarih)
                .getResultList();
    }

    public List<Oda> findRezervasyonsuzOdalar() {
        return em.createQuery("""
        SELECT o FROM Oda o
        WHERE o.aktif = true
        AND NOT EXISTS (
            SELECT r FROM Rezervasyon r
            WHERE r.odaId = o.id
        )
    """, Oda.class)
                .getResultList();
    }

}