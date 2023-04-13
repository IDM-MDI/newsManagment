package ru.clevertec.newsmanagement.newsservice.builder.impl;


import com.google.protobuf.Timestamp;
import ru.clevertec.newsmanagement.newsservice.builder.TestDTOBuilder;
import ru.clevertec.newsmanagement.newsservice.builder.TestEntityBuilder;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;
import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

import java.time.Instant;
import java.util.Date;

import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;

public class CommentBuilder implements TestEntityBuilder<Comment>,TestDTOBuilder<DTO.Comment> {
    private Long id = 1L;
    private String username = "test username";
    private String text = "test text";
    private Date date = Date.from(Instant.ofEpochSecond(1000000));
    private News news = aNews().buildToEntity();
    private CommentBuilder() {}

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public CommentBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public CommentBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public CommentBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public CommentBuilder setNews(News news) {
        this.news = news;
        return this;
    }

    @Override
    public DTO.Comment buildToDTO() {
        Instant instant = date.toInstant();
        return DTO.Comment.newBuilder()
                .setId(id)
                .setText(text)
                .setUsername(username)
                .setCreatedDate(
                        Timestamp.newBuilder()
                                .setSeconds(instant.getEpochSecond())
                                .setNanos(instant.getNano())
                                .build()
                )
                .build();
    }

    @Override
    public Comment buildToEntity() {
        return Comment.builder()
                .id(id)
                .text(text)
                .username(username)
                .createdDate(date)
                .news(news)
                .build();
    }
}
