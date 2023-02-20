package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.entity.News;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for performing CRUD operations on {@link News} entities.
 * Extends {@link JpaRepository} to inherit basic CRUD functionality.
 * @author Dayanch
 */
@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    /**
     * Returns a {@link Page} of {@link News} entities based on the provided pagination information,
     * sorted by their IDs in ascending order. This method is cached using Spring's caching abstraction
     * and the provided cache name is "newsCache". The cache key is generated using the {@link Pageable}
     * object provided as a parameter.
     * @param pageable pagination information used to construct the page
     * @return a page of News entities
     */
    @Override
    @Cacheable(cacheNames = "newsCache",key = "#pageable")
    Page<News> findAll(Pageable pageable);


    /**
     * Returns an {@link Optional} of {@link News} entity with the given ID. This method is cached using
     * Spring's caching abstraction and the provided cache name is "newsCache". The cache key is generated
     * using the ID provided as a parameter.
     * @param id the ID of the news entity to be returned
     * @return an Optional containing the news entity with the given ID, or an empty Optional if none found
     */
    @Override
    @Cacheable(cacheNames = "newsCache",key = "#id")
    Optional<News> findById(Long id);


    /**
     * Returns a list of {@link News} entities based on the provided example. This method is cached using
     * Spring's caching abstraction and the provided cache name is "newsCache". The cache key is generated
     * using the Example object provided as a parameter.
     * @param example the example to use for querying the entities
     * @return a list of News entities matching the provided example
     */
    @Override
    @Cacheable(cacheNames = "newsCache",key = "#example")
    <S extends News> List<S> findAll(Example<S> example);


    /**
     * Saves the given news entity and evicts the corresponding cache entry from "newsCache" using
     * {@link CacheEvict}. The cache key used for eviction is the ID of the news entity.
     * @param entity the news entity to be saved
     * @return the saved news entity
     */
    @Override
    @CacheEvict(cacheNames = "newsCache", key = "#entity.id", allEntries = true)
    <S extends News> S save(S entity);


    /**
     * Deletes the {@link News} entity with the given ID and evicts the corresponding cache entry
     * from "newsCache" using {@link CacheEvict}. The cache key used for eviction is the ID of the
     * news entity.
     * @param id the ID of the news entity to be deleted
     */
    @Override
    @CacheEvict(cacheNames = "newsCache", key = "#entity.id", allEntries = true)
    void deleteById(Long id);
}
