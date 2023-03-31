package ru.clevertec.newsmanagement.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsmanagement.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.entity.Comment;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CommentRepositoryTest extends PostgresTestContainer {
    @Autowired
    private CommentRepository repository;
    @Autowired
    private NewsRepository newsRepository;
    @Test
    void findCommentsByNewsIdShouldFound() {
        long newsID = newsRepository.findAll().get(0).getId();
        List<Comment> result = repository.findCommentsByNews_Id(newsID,PageRequest.of(0,3));

        Assertions.assertThat(result)
                .isNotEmpty();
    }

    @Test
    void findCommentByIdAndNewsIdShouldFound() {
        long newsID = newsRepository.findAll().get(0).getId();
        Long id = repository.findByNews_Id(newsID).get(0);

        Optional<Comment> result = repository.findCommentByIdAndNews_Id(id, newsID);

        Assertions.assertThat(result).isPresent();
    }

    @Test
    void findByNewsIdShouldFound() {
        long newsID = newsRepository.findAll().get(0).getId();

        List<Long> ids = repository.findByNews_Id(newsID);

        Assertions.assertThat(ids)
                .isNotEmpty();
    }
    @Test
    void findCommentsByNewsIdShouldNotFound() {
        long newsID = newsRepository.findAll().get(0).getId();
        List<Comment> result = repository.findCommentsByNews_Id(newsID,PageRequest.of(100000000,3));

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void findCommentByIdAndNewsIdShouldNotFound() {
        Optional<Comment> result = repository.findCommentByIdAndNews_Id(0L, 0);

        Assertions.assertThat(result).isNotPresent();
    }

    @Test
    void findByNewsIdShouldNotFound() {
        List<Long> result = repository.findByNews_Id(0L);

        Assertions.assertThat(result).isEmpty();
    }
}