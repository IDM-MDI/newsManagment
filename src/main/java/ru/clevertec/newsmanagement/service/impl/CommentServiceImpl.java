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
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.persistence.CommentRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.handler.SortDirectionHandler.getDirection;
import static ru.clevertec.newsmanagement.handler.ExampleHandler.ENTITY_SEARCH_MATCHER;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private NewsService newsService;
    private ModelMapper mapper;
    @Autowired
    public void setNewsService(@Lazy NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    public void setMapper(ModelMapper mapper) {
        mapper.createTypeMap(Comment.class, CommentDto.class)
                .addMappings(mapping -> mapping.map(src -> src.getUser().getUsername(),CommentDto::setUsername));
        this.mapper = mapper;
    }
    @Override
    public List<CommentDto> findComments(long news, int page, int size, String filter, String direction) throws CustomException {
        return repository.findCommentsByNews_Id(news, PageRequest.of(page, size, getDirection(Sort.by(filter), direction)))
                .stream()
                .map(comment -> mapper.map(comment, CommentDto.class))
                .toList();
    }
    @Override
    public CommentDto findComments(long news, long id) throws CustomException {
        return mapper.map(findCommentEntity(news,id),CommentDto.class);
    }

    @Override
    public List<CommentDto> findComments(long news, CommentDto comment) throws CustomException {
        Comment entity = mapper.map(comment, Comment.class);
        entity.setNews(newsService.findNewsEntity(news));
        return repository.findAll(Example.of(entity, ENTITY_SEARCH_MATCHER))
                .stream()
                .map(com -> mapper.map(com, CommentDto.class))
                .toList();
    }

    @Override
    public CommentDto saveComment(long news, String username, CommentDto comment) throws CustomException {
        return mapper.map(repository.save(setDefaultComment(news, username, comment)), CommentDto.class);
    }

    @Override
    public CommentDto updateComment(long news, long id, String username, CommentDto comment) throws CustomException {
        return mapper.map(repository.save(updateCommentField(news,id,username,comment)), CommentDto.class);
    }

    @Override
    public void deleteComment(long id, long news, String username) throws CustomException {
        findValidEntity(news, id, username);
        repository.deleteById(id);
    }

    @Override
    public void deleteAllComment(long news) {
        repository.findByNews_Id(news)
                .forEach(repository::deleteById);
    }

    private Comment setDefaultComment(long news, String username, CommentDto comment) throws CustomException {
        Comment result = mapper.map(comment, Comment.class);
        result.setNews(newsService.findNewsEntity(news));
        result.setUser(userService.findUser(username));
        return result;
    }
    private Comment updateCommentField(long news, long id, String username, CommentDto comment) throws CustomException {
        Comment entity = findValidEntity(news, id, username);
        entity.setText(comment.getText());
        return entity;
    }

    private Comment findValidEntity(long news, long id, String username) throws CustomException {
        Comment entity = findCommentEntity(news, id);
        User user = userService.findUser(username);
        if(UserValidator.isUserInvalid(entity.getUser(), user)) {
            throw new CustomException(NO_ACCESS.toString());
        }
        return entity;
    }
    private Comment findCommentEntity(long news, long id) throws CustomException {
        return repository.findCommentByIdAndNews_Id(id,news)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.toString()));
    }
}
