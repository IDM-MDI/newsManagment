package ru.clevertec.newsmanagement.newsservice.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;

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
    List<Comment> findCommentsByNews_Id(Long id, Pageable pageable);


    /**
     * Retrieves a comment with the given ID and news ID.
     * @param id The ID of the comment to retrieve.
     * @param newsID The ID of the news that the comment belongs to.
     * @return An Optional containing the Comment object with the specified ID and news ID, if it exists.
     */
    Optional<Comment> findCommentByIdAndNews_Id(Long id, long newsID);


    /**
     * Retrieves a list of comment IDs for the news with the given ID.
     * @param newsID The ID of the news to retrieve comment IDs for.
     * @return A list of comment IDs for the specified news.
     */
    @Query("SELECT c.id FROM Comment c WHERE c.news.id = :newsID")
    List<Long> findByNews_Id(@Param("newsID") Long newsID);
}
