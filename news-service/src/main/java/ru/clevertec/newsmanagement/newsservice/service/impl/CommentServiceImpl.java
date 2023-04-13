package ru.clevertec.newsmanagement.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.newsservice.cache.DeleteCache;
import ru.clevertec.newsmanagement.newsservice.cache.GetCache;
import ru.clevertec.newsmanagement.newsservice.cache.PostCache;
import ru.clevertec.newsmanagement.newsservice.cache.UpdateCache;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;
import ru.clevertec.newsmanagement.newsservice.persistence.CommentRepository;
import ru.clevertec.newsmanagement.newsservice.service.CommentService;
import ru.clevertec.newsmanagement.newsservice.service.NewsService;
import ru.clevertec.newsmanagement.newsservice.util.impl.CommentMapper;
import ru.clevertec.newsmanagement.newsservice.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.newsservice.constant.ExampleConstant.ENTITY_SEARCH_MATCHER;

/**
 * Service implementation for managing comments.
 * @author Dayanch
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private NewsService newsService;

    /**
     * Sets the {@link NewsService} instance lazily to avoid circular dependency issue.
     * @param newsService the NewsService instance to set.
     */
    @Autowired
    public void setNewsService(@Lazy NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DTO.Comment> findComments(long news, Pageable page) {
        return repository.findCommentsByNews_Id(news, page)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetCache(key = "{#news,#id}", type = DTO.Comment.class)
    public DTO.Comment findComments(long news, long id) throws CustomException {
        return mapper.toDTO(findCommentEntity(news,id));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DTO.Comment> findComments(long news, DTO.Comment comment) throws CustomException {
        Comment entity = getEntityForSearch(news, comment);
        return repository.findAll(Example.of(entity, ENTITY_SEARCH_MATCHER))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @PostCache(fieldName = "id", type = DTO.Comment.class)
    public DTO.Comment saveComment(long news, DTO.Comment comment, UserDTO user) throws CustomException {
        return mapper.toDTO(repository.save(setDefaultComment(news, comment, user)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @UpdateCache(key = "#id", type = DTO.Comment.class)
    public DTO.Comment updateComment(long news, long id, DTO.Comment comment, UserDTO user) throws CustomException {
        return mapper.toDTO(repository.save(updateCommentField(news,id, comment, user)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteCache(key = "#id", type = DTO.Comment.class)
    public void deleteComment(long id, long news, UserDTO user) throws CustomException {
        findValidEntity(news, id, user);
        repository.deleteById(id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllComment(long news) {
        repository.findByNews_Id(news)
                .forEach(repository::deleteById);
    }


    /**
     * Sets the default fields for creating a new comment.
     *
     * @param news    the ID of the news to add the comment to.
     * @param comment the DTO.Comment instance to create.
     * @param user user by context
     * @return the created Comment entity.
     * @throws CustomException if the news or user is not found.
     */
    private Comment setDefaultComment(long news, DTO.Comment comment, UserDTO user) throws CustomException {
        Comment result = mapper.toEntity(comment);
        result.setNews(newsService.findNewsEntity(news));
        result.setUsername(user.getUsername());
        return result;
    }


    /**
     * Updates the text field of an existing comment.
     *
     * @param news    the ID of the news that contains the comment.
     * @param id      the ID of the comment to update.
     * @param comment the DTO.Comment instance to update.
     * @param user user by context
     * @return the updated Comment entity.
     * @throws CustomException if the news or user is not found, or the comment does not belong to the user.
     */
    private Comment updateCommentField(long news, long id, DTO.Comment comment, UserDTO user) throws CustomException {
        Comment entity = findValidEntity(news, id, user);
        entity.setText(comment.getText());
        return entity;
    }

    /**
     * Finds a valid Comment entity by id and news id, and checks if the requesting user has access to it.
     *
     * @param news The id of the news the Comment belongs to
     * @param id   The id of the Comment entity
     * @param user user by context
     * @return The Comment entity if it exists and the user has access to it
     * @throws CustomException If the Comment entity does not exist or the user does not have access to it
     */
    private Comment findValidEntity(long news, long id, UserDTO user) throws CustomException {
        Comment entity = findCommentEntity(news, id);
        if(UserValidator.isUserInvalid(entity.getUsername(), user)) {
            throw new CustomException(NO_ACCESS.toString());
        }
        return entity;
    }


    /**
     * Gets a Comment entity for search by converting a DTO.Comment object to a Comment entity
     * and setting the id, createdDate, and news fields to null, and the news field to the Comment's
     * corresponding News entity.
     * @param news The id of the news the Comment belongs to
     * @param comment The DTO.Comment object to convert to a Comment entity
     * @return The Comment entity for search
     * @throws CustomException If the News entity for the Comment does not exist
     */
    private Comment getEntityForSearch(long news, DTO.Comment comment) throws CustomException {
        Comment entity = mapper.toEntity(comment);
        entity.setId(null);
        entity.setCreatedDate(null);
        entity.setNews(newsService.findNewsEntity(news));
        return entity;
    }


    /**
     * Finds a Comment entity by id and news id.
     * @param news The id of the news the Comment belongs to
     * @param id The id of the Comment entity
     * @return The Comment entity if it exists
     * @throws CustomException If the Comment entity does not exist
     */
    private Comment findCommentEntity(long news, long id) throws CustomException {
        return repository.findCommentByIdAndNews_Id(id,news)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.toString()));
    }
}