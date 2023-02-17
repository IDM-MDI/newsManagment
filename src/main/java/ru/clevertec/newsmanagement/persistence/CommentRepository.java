package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.newsmanagement.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Cacheable(cacheNames = "commentCache",key = "{#id,#pageable}")
    List<Comment> findCommentsByNews_Id(long id, Pageable pageable);

    @Cacheable(cacheNames = "commentCache",key = "{#id,#news_id}")
    Optional<Comment> findCommentByIdAndNews_Id(long id, long news_id);

    @Cacheable(cacheNames = "commentCache",key = "{#news_id}")
    @Query("SELECT c.id FROM Comment c WHERE c.news.id = :news_id")
    List<Long> findByNews_Id(@Param("news_id") long news_id);

    @Override
    @CacheEvict(cacheNames = "commentCache",key = "{#entity.id,#entity.news.id}",allEntries = true)
    <S extends Comment> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "commentCache",key = "#id",allEntries = true)
    void deleteById(Long id);
}
