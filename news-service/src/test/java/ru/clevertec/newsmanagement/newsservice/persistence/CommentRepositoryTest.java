package ru.clevertec.newsmanagement.newsservice.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.newsmanagement.newsservice.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentRepositoryTest extends PostgresTestContainer {

    @Autowired
    private CommentRepository repository;

    @Nested
    class FindByNewsID {
        @Test
        void findCommentsByNewsIdShouldReturnList() {
            List<Comment> actual = repository.findCommentsByNews_Id(1L, Pageable.ofSize(5));

            Assertions.assertThat(actual)
                    .isNotEmpty();
        }
        @Test
        void findCommentsByNewsIdShouldReturnEmptyList() {
            List<Comment> actual = repository.findCommentsByNews_Id(10L, Pageable.ofSize(100));

            Assertions.assertThat(actual)
                    .isEmpty();
        }
        @Test
        void findByNewsIdShouldReturnList() {
            List<Long> expected = List.of(1L, 4L);

            List<Long> actual = repository.findByNews_Id(1L);

            Assertions.assertThat(actual)
                    .isEqualTo(expected);
        }
        @Test
        void findByNewsIdShouldReturnEmptyList() {
            List<Long> actual = repository.findByNews_Id(1000L);

            Assertions.assertThat(actual)
                    .isEmpty();
        }
    }
    @Nested
    class FindByIdAndNewsID {
        @Test
        void findCommentByIdAndNewsIdIsPresent() {
            Optional<Comment> actual = repository.findCommentByIdAndNews_Id(1L, 1L);

            Assertions.assertThat(actual)
                    .isPresent();
        }
        @Test
        void findCommentByIdAndNewsIdIsEmpty() {
            Optional<Comment> actual = repository.findCommentByIdAndNews_Id(100L, 100L);

            Assertions.assertThat(actual)
                    .isNotPresent();
        }
    }
}