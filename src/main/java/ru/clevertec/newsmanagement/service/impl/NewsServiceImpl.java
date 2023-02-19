package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.persistence.NewsRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.util.impl.NewsMapper;
import ru.clevertec.newsmanagement.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.util.ExampleUtil.ENTITY_SEARCH_MATCHER;
import static ru.clevertec.newsmanagement.util.SortDirectionUtil.getDirection;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableTransactionManagement(proxyTargetClass = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;
    private final UserService userService;
    private final NewsMapper mapper;
    private CommentService commentService;
    @Autowired
    public void setCommentService(@Lazy CommentService commentService) {
        this.commentService = commentService;
    }
    @Override
    public List<DTO.News> findNews(int page, int size, String filter, String direction) throws CustomException {
        return repository.findAll(PageRequest.of(page, size, getDirection(Sort.by(filter), direction)))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
    @Override
    public DTO.News findNews(long id) throws CustomException {
        return mapper.toDTO(findNewsEntity(id));
    }

    @Override
    public News findNewsEntity(long id) throws CustomException {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.toString()));
    }

    @Override
    public List<DTO.News> findNews(DTO.News news) {
        return repository.findAll(Example.of(getEntityForSearch(news), ENTITY_SEARCH_MATCHER))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public DTO.News saveNews(String username,
                             DTO.News news) throws CustomException {
        return mapper.toDTO(repository.save(setDefaultNews(username,news)));
    }

    @Override
    public DTO.News updateNews(long id,
                               String username,
                               DTO.News news) throws CustomException {
        return mapper.toDTO(repository.save(updateNewsField(id,news)));
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

    private News updateNewsField(long id, DTO.News client) throws CustomException {
        News fromDB = findNewsEntity(id);
        fromDB.setTitle(client.getTitle());
        fromDB.setText(client.getText());
        return fromDB;
    }
    private News getEntityForSearch(DTO.News news) {
        News result = mapper.toEntity(news);
        result.setId(null);
        result.setCreatedDate(null);
        return result;
    }
    private News setDefaultNews(String username,DTO.News client) throws CustomException {
        User user = userService.findUser(username);
        News result = mapper.toEntity(client);
        result.setUser(user);
        return result;
    }
}
