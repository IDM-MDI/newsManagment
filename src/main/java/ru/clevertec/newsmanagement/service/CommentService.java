package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.List;

public interface CommentService {

    List<DTO.Comment> findComments(long news, int page, int size, String filter, String direction) throws CustomException;

    DTO.Comment findComments(long news, long id) throws CustomException;

    List<DTO.Comment> findComments(long news, DTO.Comment comment) throws CustomException;

    DTO.Comment saveComment(long news, String username, DTO.Comment comment) throws CustomException;

    DTO.Comment updateComment(long news, long id, String username, DTO.Comment comment) throws CustomException;
    void deleteComment(long id, long news, String username) throws CustomException;

    void deleteAllComment(long news);
}
