package ru.clevertec.newsmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.service.CommentService;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;
    @PostMapping("/{news}/comment")
    @PreAuthorize("hasAnyRole()")
    public CommentDto saveComment(@PathVariable long news,
                                  @RequestBody CommentDto comment) {
        return service.saveComment(news,"username",comment);
    }
    @PutMapping("/{news}/comment/{id}")
    @PreAuthorize("hasAnyRole()")
    public NewsDto updateNews(@PathVariable long news,
                              @PathVariable long id,
                              @RequestBody CommentDto comment) {
        return service.updateComment(news,id,"username",comment);
    }
    @DeleteMapping("/{news}/comment/{id}")
    @PreAuthorize("hasAnyRole()")
    public ResponseEntity<String> deleteComment(@PathVariable long id,
                                                @PathVariable long news) {
        service.deleteComment(id,news,"username");
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
