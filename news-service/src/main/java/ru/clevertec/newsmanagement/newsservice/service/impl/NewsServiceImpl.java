package ru.clevertec.newsmanagement.newsservice.service.impl;

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
import ru.clevertec.newsmanagement.newsservice.cache.DeleteCache;
import ru.clevertec.newsmanagement.newsservice.cache.GetCache;
import ru.clevertec.newsmanagement.newsservice.cache.PostCache;
import ru.clevertec.newsmanagement.newsservice.cache.UpdateCache;
import ru.clevertec.newsmanagement.newsservice.client.UserClient;
import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.exception.CustomException;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.PageFilter;
import ru.clevertec.newsmanagement.newsservice.persistence.NewsRepository;
import ru.clevertec.newsmanagement.newsservice.service.CommentService;
import ru.clevertec.newsmanagement.newsservice.service.NewsService;
import ru.clevertec.newsmanagement.newsservice.util.impl.NewsMapper;

import java.util.List;

import static ru.clevertec.newsmanagement.newsservice.exception.ExceptionStatus.ENTITY_NOT_FOUND;
import static ru.clevertec.newsmanagement.newsservice.util.ExampleUtil.ENTITY_SEARCH_MATCHER;
import static ru.clevertec.newsmanagement.newsservice.util.SortDirectionUtil.getDirection;


/**
 * Service implementation for managing news.
 * @author Dayanch
 */
@Service
@Slf4j
@RequiredArgsConstructor
@EnableTransactionManagement(proxyTargetClass = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;
    private final UserClient userClient;
    private final NewsMapper mapper;
    private CommentService commentService;

    /**
     * Sets the {@link CommentService} instance lazily to avoid circular dependency issue.
     * @param commentService the CommentService instance to set.
     */
    @Autowired
    public void setCommentService(@Lazy CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DTO.News> findNews(PageFilter page) {
        return repository.findAll(PageRequest.of(page.getNumber(), page.getSize(), getDirection(Sort.by(page.getFilter()), page.getDirection())))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetCache(key = "#id", type = DTO.News.class)
    public DTO.News findNews(long id) throws CustomException {
        return mapper.toDTO(findNewsEntity(id));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public News findNewsEntity(long id) throws CustomException {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.toString()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DTO.News> findNews(DTO.News news) {
        return repository.findAll(Example.of(getEntityForSearch(news), ENTITY_SEARCH_MATCHER))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @PostCache(fieldName = "id", type = DTO.News.class)
    public DTO.News saveNews(DTO.News news) throws CustomException {
        return mapper.toDTO(repository.save(setDefaultNews(news)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @UpdateCache(key = "#id", type = DTO.News.class)
    public DTO.News updateNews(long id,
                               DTO.News news) throws CustomException {
        return mapper.toDTO(repository.save(updateNewsField(id, news)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteCache(key = "#id", type = DTO.News.class)
    @Transactional
    public void deleteNews(long id) throws CustomException {
        checkBeforeOperation(id);
        commentService.deleteAllComment(id);
        repository.deleteById(id);
    }


    /**
     * Throws a CustomException if the user attempting the operation is not authorized.
     * @param id the ID of the news entity to check authorization for
     * @throws CustomException if the user is not authorized to perform the operation
     */
    private void checkBeforeOperation(long id) throws CustomException {
//        if(UserValidator.isUserInvalid(
//                findNewsEntity(id).getUser(),
//                userService.findUser(username))) {            TODO:REFACTOR
//            throw new CustomException(NO_ACCESS.toString());
//        }
    }


    /**
     * Updates the fields of a news entity with the corresponding fields from a DTO.News object.
     * @param id the ID of the news entity to update
     * @param client the DTO.News object containing the updated fields
     * @return the updated news entity
     * @throws CustomException if the news entity does not exist
     */
    private News updateNewsField(long id, DTO.News client) throws CustomException {
        checkBeforeOperation(id);
        News fromDB = findNewsEntity(id);
        fromDB.setTitle(client.getTitle());
        fromDB.setText(client.getText());
        return fromDB;
    }


    /**
     * Returns a news entity created from a DTO.News object with the ID and createdDate fields set to null(template).
     * @param news the DTO.News object to create the news entity from
     * @return the new news entity
     */
    private News getEntityForSearch(DTO.News news) {
        News result = mapper.toEntity(news);
        result.setId(null);
        result.setCreatedDate(null);
        return result;
    }

    /**
     * Creates a new news entity based on the fields in a DTO.News object,
     * and sets the user field to the User object corresponding to the given username.
     *
     * @param client the DTO.News object containing the fields for the news entity
     * @return the new news entity
     * @throws CustomException if the user does not exist
     */
    private News setDefaultNews(DTO.News client) throws CustomException {
//        String username = userClient.getUserContext();        TODO:REFACTOR
        String username = "asb";
        News result = mapper.toEntity(client);
        result.setUsername(username);
        return result;
    }
}