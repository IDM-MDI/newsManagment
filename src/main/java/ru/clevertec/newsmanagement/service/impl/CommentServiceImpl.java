package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.persistence.CommentRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.SortDirectionHandler.getDirection;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ModelMapper mapper;
    private final NewsService newsService;
    private final UserService userService;
    @Override
    public List<CommentDto> getComments(long news, int page, int size, String filter, String direction) {
        return repository.findCommentsByNews_Id(news, PageRequest.of(page,size, getDirection(Sort.by(filter),direction)))
                .stream()
                .map(comment -> mapper.map(comment,CommentDto.class))
                .toList();
    }

    @Override
    public CommentDto getComment(long news, long id) throws Exception {
        return repository.findCommentByIdAndNews_Id(id,news)
                .map(comment -> mapper.map(comment,CommentDto.class))
                .orElseThrow(Exception::new);           //TODO: FINISH EXCEPTION
    }

    @Override
    public CommentDto saveComment(long news, String username, CommentDto comment) throws Exception {
        Comment result = mapper.map(comment, Comment.class);
        result.setNews(newsService.findNewsEntity(news));                   //TODO: REFACTOR TO SEPARATE METHOD
        result.setUser(userService.findUser(username));
        return mapper.map(repository.save(result), CommentDto.class);
    }

    @Override
    public CommentDto updateComment(long news, long id, String username, CommentDto comment) {
        return null;
    }

    @Override
    public void deleteComment(long id, long news, String username) {

    }
}
