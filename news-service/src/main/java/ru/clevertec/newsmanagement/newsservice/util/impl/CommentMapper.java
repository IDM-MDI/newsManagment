package ru.clevertec.newsmanagement.newsservice.util.impl;

import com.google.protobuf.Timestamp;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.util.ModelMapper;

import java.time.Instant;
import java.util.Date;


/**
 * Realisation of mapper with Comment and DTO.Comment
 * @author Dayanch
 */
@Component
public class CommentMapper implements ModelMapper<Comment, DTO .Comment> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment toEntity(@NotNull DTO.Comment comment) {
        return Comment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .username(comment.getUsername())
                .createdDate(
                        Date.from(Instant.ofEpochSecond(comment.getCreatedDate().getSeconds(),
                                comment.getCreatedDate().getNanos()))
                )
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DTO.Comment toDTO(@NotNull Comment comment) {
        Instant instant = comment.getCreatedDate().toInstant();
        return DTO.Comment.newBuilder()
                .setId(comment.getId())
                .setText(comment.getText())
                .setUsername(comment.getUsername())
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .build();
    }
}
