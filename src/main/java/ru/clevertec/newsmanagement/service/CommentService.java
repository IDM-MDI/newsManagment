package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.model.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findComments(long news, int page, int size, String filter, String direction);

    CommentDto findComment(long news, long id) throws Exception;

    CommentDto saveComment(long news, String username, CommentDto comment) throws Exception;

    CommentDto updateComment(long news, long id, String username, CommentDto comment) throws Exception;

    void deleteComment(long id, long news, String username) throws Exception;
    void deleteAllComment(long news);
}
