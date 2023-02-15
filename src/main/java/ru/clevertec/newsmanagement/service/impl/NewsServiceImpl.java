package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.persistence.NewsRepository;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.SortDirectionHandler.getDirection;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    @Override
    public List<NewsDto> findNews(int page, int size, String filter, String direction) {
        return repository.findAll(PageRequest.of(page,size, getDirection(Sort.by(filter),direction)))
                .stream()
                .map(news -> mapper.map(news,NewsDto.class))
                .toList();
    }
    @Override
    public NewsDto findNews(long id) throws Exception {
        return mapper.map(findNewsEntity(id),NewsDto.class);
    }

    @Override
    public News findNewsEntity(long id) throws Exception {
        return repository.findById(id)
                .orElseThrow(Exception::new); // TODO:FINISH EXCEPTION
    }

    @Override
    public NewsDto saveNews(String username,
                            NewsDto news) throws Exception {
        return mapper.map(repository.save(setDefaultNews(username,news)),NewsDto.class);
    }

    @Override
    public NewsDto updateNews(long id,
                              String username,
                              NewsDto news) throws Exception {
        return mapper.map(repository.save(updateNewsField(id,news)),NewsDto.class);
    }

    @Override
    public void deleteNews(long id, String username) throws Exception {
        News fromDB = findNewsEntity(id);
        if(!UserValidator.isUserValid(fromDB.getUser(),username)) {
            throw new Exception();
        }
        repository.deleteById(id);
    }
    private News updateNewsField(long id, NewsDto client) throws Exception {
        News fromDB = findNewsEntity(id);
        fromDB.setTitle(client.getTitle());
        fromDB.setText(client.getText());
        return fromDB;
    }
    private News setDefaultNews(String username,NewsDto client) throws Exception {
        User user = userService.findUser(username);
        News result = mapper.map(client, News.class);
        result.setUser(user);
        return result;
    }
}
