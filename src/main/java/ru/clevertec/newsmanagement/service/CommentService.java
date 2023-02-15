package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.model.NewsDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getComments(long id, int page, int size, String filter, String direction);

    NewsDto getComment(long news, long id);

    CommentDto saveComment(long news, String username, CommentDto comment);

    NewsDto updateComment(long news, long id, String username, CommentDto comment);

    void deleteComment(long id, long news, String username);
}
