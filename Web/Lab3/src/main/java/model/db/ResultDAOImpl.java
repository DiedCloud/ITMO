package model.db;

import model.entity.PointEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Root;

import java.util.Collection;

/**
 * Implementation of the ResultDAO interface using JPA (Java Persistence API).
 * Handles database operations for ResultEntity objects.
 */
public class ResultDAOImpl implements ResultDAO {
    private final EntityManager entityManager = JPAUtils.getFactory().createEntityManager();

    @Override
    public void addNewResult(PointEntity result) {
        entityManager.getTransaction().begin();
        entityManager.persist(result);
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateResult(Long result_id, PointEntity result) {
        entityManager.getTransaction().begin();
        entityManager.merge(result);
        entityManager.getTransaction().commit();
    }

    @Override
    public PointEntity getResultById(Long result_id) {
        return entityManager.getReference(PointEntity.class, result_id);
    }

    @Override
    public Collection<PointEntity> getAllResults() {
        var cm = entityManager.getCriteriaBuilder().createQuery(PointEntity.class);
        Root<PointEntity> root = cm.from(PointEntity.class);
        return entityManager.createQuery(cm.select(root)).getResultList();
    }

    @Override
    public void deleteResult(PointEntity result) {
        entityManager.getTransaction().begin();
        entityManager.remove(result);
        entityManager.getTransaction().commit();
    }

    /**
     * This method also handles transaction rollback in case of an error.
     */
    @Override
    public void clearResults() {
        entityManager.getTransaction().begin();
        try {
            Query query = entityManager.createQuery("DELETE FROM PointEntity r");
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Or handle the exception as needed
        } finally {
            entityManager.clear();
        }
    }
}
