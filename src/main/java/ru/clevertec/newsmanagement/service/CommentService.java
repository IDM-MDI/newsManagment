package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findComments(long news, int page, int size, String filter, String direction) throws CustomException;

    CommentDto findComment(long news, long id) throws CustomException;

    CommentDto saveComment(long news, String username, CommentDto comment) throws CustomException;

    CommentDto updateComment(long news, long id, String username, CommentDto comment) throws CustomException;

    void deleteComment(long id, long news, String username) throws CustomException;
    void deleteAllComment(long news);
}
