package ru.clevertec.newsmanagement.newsservice.service;


import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.PageFilter;

import java.util.List;

/**
 * A service interface that provides CRUD operations for comments on news.
 * @author Dayanch
 */
public interface CommentService {
    /**
     * Finds comments related to the news.
     *
     * @param news id of the news.
     * @param page the page number of comments to retrieve.
     * @return a list of DTO.Comment objects representing the comments.
     */

    List<DTO.Comment> findComments(long news, PageFilter page);


    /**
     * Finds a specific comment related to the news.
     * @param news id of the news.
     * @param id id of the comment to find.
     * @return a DTO.Comment object representing the comment.
     */
    DTO.Comment findComments(long news, long id);


    /**
     * Finds comments related to news filtered by a specific comment.
     * @param news id of the news.
     * @param comment the comment to filter by.
     * @return a list of DTO.Comment objects representing the comments.
     */
    List<DTO.Comment> findComments(long news, DTO.Comment comment);


    /**
     * Saves a new comment for to news.
     *
     * @param news    id of the news.
     * @param comment the comment to save.
     * @return a DTO.Comment object representing the saved comment.
     */
    DTO.Comment saveComment(long news, DTO.Comment comment);


    /**
     * Updates an existing comment for to news.
     *
     * @param news    id of the news.
     * @param id      id of the comment to update.
     * @param comment the updated comment to save.
     * @return a DTO.Comment object representing the updated comment.
     */
    DTO.Comment updateComment(long news, long id, DTO.Comment comment);


    /**
     * Deletes an existing comment for to news.
     *
     * @param id   id of the comment to delete.
     * @param news id of the news.
     */
    void deleteComment(long id, long news);


    /**
     * Deletes all comments related to to news.
     * @param news id of the news.
     */
    void deleteAllComment(long news);
}