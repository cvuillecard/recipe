package com.recipe.config.spring.dao;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Abstract Data Access Object methods definition
 *
 * @param <T>  Interface contract
 * @param <ID> Primary key type
 */
public interface GenericDao<T, ID extends Serializable>
{
   /**
    * find an entity by its id
    * 
    * @param id
    * @return entity
    */
   T findById(final ID id);

    /**
     * find all entities of entity's table definition
     *
     * @return iterable entities
     * @throws Exception
     */
   Iterable<T> findAll();

   /**
    * Count the number of entries in table for this entity
    * 
    * @return number of entries in table
    */
   long countAll();

   /**
    * Save a new entity in database
    * 
    * @param bo
    * @return saved entity
    */
   T save(final T bo);
   
   /**
    * Merge the object with id and update the value in database
    * 
    * @param bo
    * @return persisted entity
    */
   T merge(final T bo);
   
   /**
    * delete the entry for the object id in table
    * 
    * @param bo
    */
   void delete(final T bo);

   /**
    * delete all entries in table entity
    */
   void purgeTable();

   /**
    * Flush session
    */
   void flush();

   /**
    * Clear session
    */
   void clear();

   /**
    * detach an entity from session cache
    * 
    * @param bo
    */
   void detach(final T bo);

   /**
    * Vérifie si un object est détaché
    *
    * @param bo
    * @return true if object is detached to session cache
    */
   boolean isDetached(final T bo);
}
