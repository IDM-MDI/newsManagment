package ru.clevertec.newsmanagement.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsmanagement.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByNews_Id(long id, Pageable pageable);
    Optional<Comment> findCommentByIdAndNews_Id(Long id, Long news_id);
}
