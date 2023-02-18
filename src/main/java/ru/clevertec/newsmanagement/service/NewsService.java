package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> findNews(int page, int size, String filter, String direction) throws CustomException;
    NewsDto findNews(long id) throws CustomException;
    News findNewsEntity(long id) throws CustomException;

    NewsDto saveNews(String username,NewsDto news) throws CustomException;

    NewsDto updateNews(long id, String username, NewsDto news) throws CustomException;

    void deleteNews(long id, String username) throws CustomException;

    List<NewsDto> findNews(NewsDto news);
}
