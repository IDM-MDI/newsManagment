package ru.clevertec.newsmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.service.CommentService;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.JwtSecurityHandler.getUsernameByContext;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping("/{news}/comment")
    public List<CommentDto> getNewsComment(@PathVariable long news,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String filter,
                                           @RequestParam(defaultValue = "asc") String direction) {
        return service.getComments(news,page,size,filter,direction);
    }
    @GetMapping("/{news}/comment/{id}")
    public CommentDto getComment(@PathVariable long news,
                              @PathVariable long id) throws Exception {
        return service.getComment(news,id);
    }
    @PostMapping("/{news}/comment")
    @PreAuthorize("hasAnyRole()")
    public CommentDto saveComment(@PathVariable long news,
                                  @RequestBody @Valid CommentDto comment) throws Exception {
        return service.saveComment(news,getUsernameByContext(),comment);
    }
    @PutMapping("/{news}/comment/{id}")
    @PreAuthorize("hasAnyRole()")
    public CommentDto updateNews(@PathVariable long news,
                              @PathVariable long id,
                              @RequestBody @Valid CommentDto comment) throws Exception {
        return service.updateComment(news,id,getUsernameByContext(),comment);
    }
    @DeleteMapping("/{news}/comment/{id}")
    @PreAuthorize("hasAnyRole()")
    public ResponseEntity<String> deleteComment(@PathVariable long id,
                                                @PathVariable long news) throws Exception {
        service.deleteComment(id,news,getUsernameByContext());
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
