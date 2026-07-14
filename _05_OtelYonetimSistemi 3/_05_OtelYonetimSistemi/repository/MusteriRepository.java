package com.barisd._05_OtelYonetimSistemi.repository;


import com.barisd._05_OtelYonetimSistemi.dto.MusteriDTO;
import com.barisd._05_OtelYonetimSistemi.entity.Musteri;
import com.barisd._05_OtelYonetimSistemi.utility.JpaUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class MusteriRepository extends RepositoryManager<Musteri, Long> {

    private final EntityManager em;

    public MusteriRepository() {
        super(Musteri.class);
        this.em = JpaUtility.getEntityManager();
    }

    public Optional<Musteri> findByTcKimlikNo(String tcKimlikNo) {
        TypedQuery<Musteri> query = em.createQuery("SELECT m FROM Musteri m WHERE m.tcKimlikNo = :tcKimlikNo", Musteri.class);
        query.setParameter("tcKimlikNo", tcKimlikNo);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Musteri> findByAdOrSoyadContaining(String metin) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Musteri> cq = cb.createQuery(Musteri.class);
        Root<Musteri> musteri = cq.from(Musteri.class);

        Predicate adPredicate = cb.like(cb.lower(musteri.get("ad")), "%" + metin.toLowerCase() + "%");
        Predicate soyadPredicate = cb.like(cb.lower(musteri.get("soyad")), "%" + metin.toLowerCase() + "%");
        Predicate orPredicate = cb.or(adPredicate, soyadPredicate);

        cq.where(orPredicate);

        TypedQuery<Musteri> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Musteri> findBySehir(String sehir) {
        TypedQuery<Musteri> query = em.createQuery("SELECT m FROM Musteri m WHERE m.sehir = :sehir", Musteri.class);
        query.setParameter("sehir", sehir);
        return query.getResultList();
    }

    public List<MusteriDTO> findAllDTO() {
        TypedQuery<MusteriDTO> query = em.createQuery(
                "SELECT new com.barisd._05_OtelYonetimSistemi.dto.MusteriDTO(m.id, m.tcKimlikNo, m.ad, m.soyad, m.telefon, m.email, m.sehir, m.vipMusteri) FROM Musteri m ORDER BY id",
                MusteriDTO.class
        );
        return query.getResultList();
    }

    public List<Musteri> findAllVipMusteri() {
        TypedQuery<Musteri> query = em.createQuery("SELECT m FROM Musteri m WHERE m.vipMusteri = true ORDER BY id", Musteri.class);
        return query.getResultList();
    }
}