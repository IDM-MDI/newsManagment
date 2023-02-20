package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.entity.Comment;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for Comment entity.
 * Provides methods to interact with the database and cache for Comment objects.
 * @author Dayanch
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    /**
     * Retrieves a list of comments for a news, given the news ID and a Pageable object for pagination.
     * @param id The ID of the news to retrieve comments for.
     * @param pageable The Pageable object to use for pagination.
     * @return A list of comments for the specified news, as specified by the given Pageable object.
     */
    @Cacheable(cacheNames = "commentCache",key = "{#id,#pageable}")
    List<Comment> findCommentsByNews_Id(Long id, Pageable pageable);


    /**
     * Retrieves a comment with the given ID and news ID.
     * @param id The ID of the comment to retrieve.
     * @param newsID The ID of the news that the comment belongs to.
     * @return An Optional containing the Comment object with the specified ID and news ID, if it exists.
     */
    @Cacheable(cacheNames = "commentCache",key = "{#id,#newsID}")
    Optional<Comment> findCommentByIdAndNews_Id(Long id, long newsID);


    /**
     * Retrieves a list of comment IDs for the news with the given ID.
     * @param newsID The ID of the news to retrieve comment IDs for.
     * @return A list of comment IDs for the specified news.
     */
    @Cacheable(cacheNames = "commentCache",key = "{#newsID}")
    @Query("SELECT c.id FROM Comment c WHERE c.news.id = :newsID")
    List<Long> findByNews_Id(@Param("newsID") Long newsID);


    /**
     * Retrieves a list of all Comment objects that match the given Example.
     * @param example An Example of the Comment objects to retrieve.
     * @param <S> A subtype of Comment.
     * @return A list of Comment objects that match the given Example.
     */
    @Override
    @Cacheable(cacheNames = "commentCache", key = "#example")
    <S extends Comment> List<S> findAll(Example<S> example);


    /**
     * Saves a Comment object to the database and updates the cache accordingly.
     * @param entity The Comment object to save.
     * @param <S> A subtype of Comment.
     * @return The saved Comment object.
     */
    @Override
    @CacheEvict(cacheNames = "commentCache",key = "{#entity.id,#entity.news.id}",allEntries = true)
    <S extends Comment> S save(S entity);


    /**
     * Deletes a Comment object with the specified ID from the database and updates the cache accordingly.
     * @param id The ID of the Comment object to delete.
     */
    @Override
    @CacheEvict(cacheNames = "commentCache",key = "#id",allEntries = true)
    void deleteById(Long id);
}
