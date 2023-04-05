package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.cache.DeleteCache;
import ru.clevertec.newsmanagement.cache.GetCache;
import ru.clevertec.newsmanagement.cache.PostCache;
import ru.clevertec.newsmanagement.cache.UpdateCache;
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.model.PageFilter;
import ru.clevertec.newsmanagement.persistence.CommentRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.util.impl.CommentMapper;
import ru.clevertec.newsmanagement.validator.UserValidator;

import java.util.List;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.util.ExampleUtil.ENTITY_SEARCH_MATCHER;
import static ru.clevertec.newsmanagement.util.SortDirectionUtil.getDirection;

/**
 * Service implementation for managing comments.
 * @author Dayanch
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserService userService;
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
    public List<DTO.Comment> findComments(long news, PageFilter page) {
        return repository.findCommentsByNews_Id(news, PageRequest.of(page.getNumber(), page.getSize(), getDirection(Sort.by(page.getFilter()), page.getDirection())))
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
    public DTO.Comment saveComment(long news, String username, DTO.Comment comment) throws CustomException {
        return mapper.toDTO(repository.save(setDefaultComment(news, username, comment)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @UpdateCache(key = "#id", type = DTO.Comment.class)
    public DTO.Comment updateComment(long news, long id, String username, DTO.Comment comment) throws CustomException {
        return mapper.toDTO(repository.save(updateCommentField(news,id,username,comment)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteCache(key = "#id", type = DTO.Comment.class)
    public void deleteComment(long id, long news, String username) throws CustomException {
        findValidEntity(news, id, username);
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
     * @param news the ID of the news to add the comment to.
     * @param username the username of the user who creates the comment.
     * @param comment the DTO.Comment instance to create.
     * @return the created Comment entity.
     * @throws CustomException if the news or user is not found.
     */
    private Comment setDefaultComment(long news, String username, DTO.Comment comment) throws CustomException {
        Comment result = mapper.toEntity(comment);
        result.setNews(newsService.findNewsEntity(news));
        result.setUser(userService.findUser(username));
        return result;
    }


    /**
     * Updates the text field of an existing comment.
     * @param news the ID of the news that contains the comment.
     * @param id the ID of the comment to update.
     * @param username the username of the user who updates the comment.
     * @param comment the DTO.Comment instance to update.
     * @return the updated Comment entity.
     * @throws CustomException if the news or user is not found, or the comment does not belong to the user.
     */
    private Comment updateCommentField(long news, long id, String username, DTO.Comment comment) throws CustomException {
        Comment entity = findValidEntity(news, id, username);
        entity.setText(comment.getText());
        return entity;
    }

    /**
     * Finds a valid Comment entity by id and news id, and checks if the requesting user has access to it.
     * @param news The id of the news the Comment belongs to
     * @param id The id of the Comment entity
     * @param username The username of the requesting user
     * @return The Comment entity if it exists and the user has access to it
     * @throws CustomException If the Comment entity does not exist or the user does not have access to it
     */
    private Comment findValidEntity(long news, long id, String username) throws CustomException {
        Comment entity = findCommentEntity(news, id);
        User user = userService.findUser(username);
        if(UserValidator.isUserInvalid(entity.getUser(), user)) {
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