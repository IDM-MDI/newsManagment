package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.model.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> findNews(int page, int size, String filter, String direction);
    NewsDto findNews(long id) throws Exception;
    News findNewsEntity(long id) throws Exception;

    NewsDto saveNews(String username,NewsDto news) throws Exception;

    NewsDto updateNews(long id, String username, NewsDto news) throws Exception;

    void deleteNews(long id, String username) throws Exception;
}
