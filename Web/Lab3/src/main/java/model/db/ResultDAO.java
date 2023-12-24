package model.db;

import model.entity.PointEntity;

import java.util.Collection;

/**
 * Interface for data access operations related to ResultEntity.
 * Provides methods for creating, reading, updating, and deleting result records in the database.
 */
public interface ResultDAO {

    /**
     * Adds a new result entity to the database.
     *
     * @param result The ResultEntity object to be added.
     */
    void addNewResult(PointEntity result);

    /**
     * Updates an existing result entity in the database.
     *
     * @param result_id The ID of the result to be updated.
     * @param result The updated ResultEntity object.
     */
    void updateResult(Long result_id, PointEntity result);

    /**
     * Retrieves a result entity by its ID.
     *
     * @param result_id The ID of the result to retrieve.
     * @return The ResultEntity object corresponding to the specified ID.
     */
    PointEntity getResultById(Long result_id);

    /**
     * Retrieves all result entities from the database.
     *
     * @return A collection of all ResultEntity objects.
     */
    Collection<PointEntity> getAllResults();

    /**
     * Deletes a specific result entity from the database.
     *
     * @param result The ResultEntity object to be deleted.
     */
    void deleteResult(PointEntity result);

    /**
     * Clears all result entities from the database.
     */
    void clearResults();
}
