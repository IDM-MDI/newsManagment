package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.model.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> getNews(int page, int size, String filter, String direction);
    NewsDto getNews(long id);

    NewsDto saveNews(String username,NewsDto news);

    NewsDto updateNews(long id, String username, NewsDto news);

    void deleteNews(long id, String username, NewsDto news);
}
