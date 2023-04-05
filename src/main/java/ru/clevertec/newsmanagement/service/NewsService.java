package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.model.PageFilter;

import java.util.List;

/**
 * A service interface that provides CRUD operations for news.
 * @author Dayanch
 */
public interface NewsService {
    /**
     * Returns a list of news with pagination and sorting.
     *
     * @param page the page number.
     * @return a list of news.
     * @throws CustomException if an error occurs while retrieving the news.
     */
    List<DTO.News> findNews(PageFilter page) throws CustomException;

    /**
     * Returns the news with the given id.
     * @param id the id of the news to retrieve.
     * @return the news with the given id.
     * @throws CustomException if the news with the given id cannot be found.
     */
    DTO.News findNews(long id) throws CustomException;

    /**
     * Returns the news entity with the given id.
     * @param id the id of the news entity to retrieve.
     * @return the news entity with the given id.
     * @throws CustomException if the news entity with the given id cannot be found.
     */
    News findNewsEntity(long id) throws CustomException;


    /**
     * Saves a new news entry for the given username.
     * @param username the username of the user who created the news entry.
     * @param news the news entry to be saved.
     * @return the saved news entry.
     * @throws CustomException if an error occurs while saving the news entry.
     */
    DTO.News saveNews(String username, DTO.News news) throws CustomException;


    /**
     * Updates an existing news entry with the given id for the given username.
     * @param id the id of the news entry to be updated.
     * @param username the username of the user who created the news entry.
     * @param news the updated news entry.
     * @return the updated news entry.
     * @throws CustomException if an error occurs while updating the news entry.
     */
    DTO.News updateNews(long id, String username, DTO.News news) throws CustomException;

    /**
     * Deletes the news entry with the given id for the given username.
     * @param id the id of the news entry to be deleted.
     * @param username the username of the user who created the news entry.
     * @throws CustomException if an error occurs while deleting the news entry.
     */
    void deleteNews(long id, String username) throws CustomException;


    /**
     * Returns a list of news matching the given news criteria.
     * @param news the news criteria to match.
     * @return a list of news matching the given news criteria.
     */
    List<DTO.News> findNews(DTO.News news);
}
