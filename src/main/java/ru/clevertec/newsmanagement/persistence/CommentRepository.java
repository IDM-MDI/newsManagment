package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.newsmanagement.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Cacheable(cacheNames = "commentCache",key = "{#id,#pageable}")
    List<Comment> findCommentsByNews_Id(Long id, Pageable pageable);

    @Cacheable(cacheNames = "commentCache",key = "{#id,#newsID}")
    Optional<Comment> findCommentByIdAndNews_Id(Long id, long newsID);

    @Cacheable(cacheNames = "commentCache",key = "{#newsID}")
    @Query("SELECT c.id FROM Comment c WHERE c.news.id = :newsID")
    List<Long> findByNews_Id(@Param("newsID") Long newsID);

    @Override
    @Cacheable(cacheNames = "commentCache", key = "#example")
    <S extends Comment> List<S> findAll(Example<S> example);

    @Override
    @CacheEvict(cacheNames = "commentCache",key = "{#entity.id,#entity.news.id}",allEntries = true)
    <S extends Comment> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "commentCache",key = "#id",allEntries = true)
    void deleteById(Long id);
}
