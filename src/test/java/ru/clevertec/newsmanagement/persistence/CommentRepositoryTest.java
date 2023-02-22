package ru.clevertec.newsmanagement.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NewsRepository newsRepository;
    private List<Comment> commentList;
    @BeforeEach
    void setup() {
        User user = userRepository.save(
                User.builder()
                        .username("user")
                        .password("password")
                        .role(Role.SUBSCRIBER)
                        .build()
        );
        News news = newsRepository.save(
            News.builder()
                    .id(1L)
                    .title("title")
                    .text("text")
                    .user(user)
                    .createdDate(Date.from(Instant.now()))
                    .build()
        );
        commentList = new ArrayList<>(List.of(
                Comment.builder()
                        .id(1L)
                        .user(user)
                        .news(news)
                        .createdDate(Date.from(Instant.now()))
                        .text("test")
                        .build(),
                Comment.builder()
                        .id(2L)
                        .user(user)
                        .news(news)
                        .createdDate(Date.from(Instant.now()))
                        .text("test")
                        .build(),
                Comment.builder()
                        .id(3L)
                        .user(user)
                        .news(news)
                        .createdDate(Date.from(Instant.now()))
                        .text("test")
                        .build()
        ));
        repository.saveAll(commentList);
        System.out.println(repository.findAll());
    }
    @Test
    void findCommentsByNewsIdShouldFound() {
        long newsID = newsRepository.findAll().get(0).getId();
        List<Comment> result = repository.findCommentsByNews_Id(newsID,PageRequest.of(0,3));

        Assertions.assertThat(result).hasSameSizeAs(commentList);
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

        Assertions.assertThat(ids).hasSameSizeAs(commentList);
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