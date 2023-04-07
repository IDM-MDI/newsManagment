package ru.clevertec.newsmanagement.newsservice.service;


import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.PageFilter;

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
     */
    List<DTO.News> findNews(PageFilter page);

    /**
     * Returns the news with the given id.
     * @param id the id of the news to retrieve.
     * @return the news with the given id.
     */
    DTO.News findNews(long id);

    /**
     * Returns the news entity with the given id.
     * @param id the id of the news entity to retrieve.
     * @return the news entity with the given id.
     */
    News findNewsEntity(long id);


    /**
     * Saves a new news entry for the given username.
     *
     * @param news the news entry to be saved.
     * @return the saved news entry.
     */
    DTO.News saveNews(DTO.News news);


    /**
     * Updates an existing news entry with the given id for the given username.
     *
     * @param id   the id of the news entry to be updated.
     * @param news the updated news entry.
     * @return the updated news entry.
     */
    DTO.News updateNews(long id, DTO.News news);

    /**
     * Deletes the news entry with the given id for the given username.
     *
     * @param id the id of the news entry to be deleted.
     */
    void deleteNews(long id);


    /**
     * Returns a list of news matching the given news criteria.
     * @param news the news criteria to match.
     * @return a list of news matching the given news criteria.
     */
    List<DTO.News> findNews(DTO.News news);
}