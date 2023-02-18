package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.persistence.NewsRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.handler.ExampleHandler.ENTITY_SEARCH_MATCHER;
import static ru.clevertec.newsmanagement.handler.SortDirectionHandler.getDirection;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableTransactionManagement(proxyTargetClass = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;
    private final UserService userService;
    private CommentService commentService;
    private ModelMapper mapper;
    @Autowired
    public void setCommentService(@Lazy CommentService commentService) {
        this.commentService = commentService;
    }
    @Autowired
    public void setMapper(ModelMapper mapper) {
        mapper.createTypeMap(News.class, NewsDto.class)
                .addMappings(mapping -> mapping.map(src -> src.getUser().getUsername(),NewsDto::setUsername));
        this.mapper = mapper;
    }
    @Override
    public List<NewsDto> findNews(int page, int size, String filter, String direction) throws CustomException {
        return repository.findAll(PageRequest.of(page, size, getDirection(Sort.by(filter), direction)))
                .stream()
                .map(news -> mapper.map(news, NewsDto.class))
                .toList();
    }
    @Override
    public NewsDto findNews(long id) throws CustomException {
        return mapper.map(findNewsEntity(id),NewsDto.class);
    }

    @Override
    public News findNewsEntity(long id) throws CustomException {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.toString()));
    }

    @Override
    public List<NewsDto> findNews(NewsDto news) {
        return repository.findAll(Example.of(mapper.map(news,News.class), ENTITY_SEARCH_MATCHER))
                .stream()
                .map(n -> mapper.map(n, NewsDto.class))
                .toList();
    }

    @Override
    public NewsDto saveNews(String username,
                            NewsDto news) throws CustomException {
        return mapper.map(repository.save(setDefaultNews(username,news)),NewsDto.class);
    }

    @Override
    public NewsDto updateNews(long id,
                              String username,
                              NewsDto news) throws CustomException {
        return mapper.map(repository.save(updateNewsField(id,news)),NewsDto.class);
    }

    @Override
    @Transactional
    public void deleteNews(long id, String username) throws CustomException {
        checkBeforeOperation(id, username);
        commentService.deleteAllComment(id);
        repository.deleteById(id);
    }

    private void checkBeforeOperation(long id, String username) throws CustomException {
        if(UserValidator.isUserInvalid(
                findNewsEntity(id).getUser(),
                userService.findUser(username))) {
            throw new CustomException(NO_ACCESS.toString());
        }
    }

    private News updateNewsField(long id, NewsDto client) throws CustomException {
        News fromDB = findNewsEntity(id);
        fromDB.setTitle(client.getTitle());
        fromDB.setText(client.getText());
        return fromDB;
    }
    private News setDefaultNews(String username,NewsDto client) throws CustomException {
        User user = userService.findUser(username);
        News result = mapper.map(client, News.class);
        result.setUser(user);
        return result;
    }
}
