package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.List;

/**
 * A service interface that provides CRUD operations for comments on news.
 * @author Dayanch
 */
public interface CommentService {
    /**
     * Finds comments related to the news.
     * @param news id of the news.
     * @param page the page number of comments to retrieve.
     * @param size the size of each page.
     * @param filter the filter for the comment search.
     * @param direction the sorting direction for comments.
     * @return a list of DTO.Comment objects representing the comments.
     * @throws CustomException if there was an error while finding the comments.
     */

    List<DTO.Comment> findComments(long news, int page, int size, String filter, String direction) throws CustomException;


    /**
     * Finds a specific comment related to the news.
     * @param news id of the news.
     * @param id id of the comment to find.
     * @return a DTO.Comment object representing the comment.
     * @throws CustomException if there was an error while finding the comment.
     */
    DTO.Comment findComments(long news, long id) throws CustomException;


    /**
     * Finds comments related to news filtered by a specific comment.
     * @param news id of the news.
     * @param comment the comment to filter by.
     * @return a list of DTO.Comment objects representing the comments.
     * @throws CustomException if there was an error while finding the comments.
     */
    List<DTO.Comment> findComments(long news, DTO.Comment comment) throws CustomException;


    /**
     * Saves a new comment for to news.
     * @param news id of the news.
     * @param username username of the user saving the comment.
     * @param comment the comment to save.
     * @return a DTO.Comment object representing the saved comment.
     * @throws CustomException if there was an error while saving the comment.
     */
    DTO.Comment saveComment(long news, String username, DTO.Comment comment) throws CustomException;


    /**
     * Updates an existing comment for to news.
     * @param news id of the news.
     * @param id id of the comment to update.
     * @param username username of the user updating the comment.
     * @param comment the updated comment to save.
     * @return a DTO.Comment object representing the updated comment.
     * @throws CustomException if there was an error while updating the comment.
     */
    DTO.Comment updateComment(long news, long id, String username, DTO.Comment comment) throws CustomException;


    /**
     * Deletes an existing comment for to news.
     * @param id id of the comment to delete.
     * @param news id of the news.
     * @param username username of the user deleting the comment.
     * @throws CustomException if there was an error while deleting the comment.
     */
    void deleteComment(long id, long news, String username) throws CustomException;


    /**
     * Deletes all comments related to to news.
     * @param news id of the news.
     */
    void deleteAllComment(long news);
}