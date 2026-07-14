package com.barisd._05_OtelYonetimSistemi.repository;

import com.barisd._05_OtelYonetimSistemi.utility.JpaUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public abstract class RepositoryManager<T, ID> implements ICrud<T, ID>, AutoCloseable {
	private final EntityManager em;
	private final Class<T> entityClass;
	
	public RepositoryManager(Class<T> entityClass) {
		em = JpaUtility.getEntityManager();
		this.entityClass = entityClass;
	}
	
	
	@Override
	public T save(T entity) {
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		}
		catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Kaydetme işleminde hata..." + e.getMessage());
			throw e; //ileride global exception handling ile bunu yakalayacağız.
		}
	}
	
	@Override
	public Iterable<T> saveAll(Iterable<T> entities) {
		try {
			em.getTransaction().begin();
			for (T entity : entities) {
				em.persist(entity);
			}
			em.getTransaction().commit();
			return entities;
		}
		catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Toplu Kaydetme işleminde hata..." + e.getMessage());
			throw e; //ileride global exception handling ile bunu yakalayacağız.
		}
	}
	
	@Override
	public boolean deleteById(ID id) {
		try {
			em.getTransaction().begin();
			T entityToRemove = em.find(entityClass, id);
			if (entityToRemove != null) {
				em.remove(entityToRemove);
				em.getTransaction().commit();
				return true;
			}
			em.getTransaction().rollback();
			System.out.println(id + " idli kayıt bulunamadı.");
			return false;
		}
		catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Silme işleminde hata..." + e.getMessage());
			return false;
		}
	}
	
	@Override
	public T update(T entity) {
		try {
			em.getTransaction().begin();
			T updatedEntity = em.merge(entity);
			em.getTransaction().commit();
			return updatedEntity;
		}
		catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Güncelleme işleminde hata..." + e.getMessage());
			throw e;
		}
	}
	
	@Override
	public Optional<T> findById(ID id) {
		T foundEntity = em.find(entityClass, id);
		return Optional.ofNullable(foundEntity);
		
	}
	
	@Override
	public List<T> findAll() {
		CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		return em.createQuery(criteriaQuery).getResultList();
	}
	
	@Override
	public boolean existsById(ID id) {
		return em.find(entityClass, id) != null;
	}
	
	@Override
	public void close() {
		if (em != null && em.isOpen()) {
			em.close();
		}
	}
}