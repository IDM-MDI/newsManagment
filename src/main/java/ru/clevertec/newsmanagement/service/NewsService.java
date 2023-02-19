package ru.clevertec.newsmanagement.service;

import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.List;

public interface NewsService {
    List<DTO.News> findNews(int page, int size, String filter, String direction) throws CustomException;
    DTO.News findNews(long id) throws CustomException;
    News findNewsEntity(long id) throws CustomException;

    DTO.News saveNews(String username, DTO.News news) throws CustomException;

    DTO.News updateNews(long id, String username, DTO.News news) throws CustomException;

    void deleteNews(long id, String username) throws CustomException;

    List<DTO.News> findNews(DTO.News news);
}
