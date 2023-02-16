package ru.clevertec.newsmanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.model.CommentDto;
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
                                           @RequestParam(defaultValue = "0") @Min(1) int page,
                                           @RequestParam(defaultValue = "10") @Min(1) int size,
                                           @RequestParam(defaultValue = "id") @NotBlank String filter,
                                           @RequestParam(defaultValue = "asc") @NotBlank String direction) {
        return service.findComments(news,page,size,filter,direction);
    }
    @GetMapping("/{news}/comment/{id}")
    public CommentDto getComment(@PathVariable @Min(1) long news,
                                @PathVariable @Min(1) long id) throws Exception {
        return service.findComment(news,id);
    }
    @PostMapping("/{news}/comment")
    @PreAuthorize("isAuthenticated()")
    public CommentDto saveComment(@PathVariable @Min(1) long news,
                                  @RequestBody @Valid CommentDto comment) throws Exception {
        return service.saveComment(news,getUsernameByContext(),comment);
    }
    @PutMapping("/{news}/comment/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommentDto updateNews(@PathVariable @Min(1) long news,
                              @PathVariable @Min(1) long id,
                              @RequestBody @Valid CommentDto comment) throws Exception {
        return service.updateComment(news,id,getUsernameByContext(),comment);
    }
    @DeleteMapping("/{news}/comment/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteComment(@PathVariable @Min(1) long id,
                                                @PathVariable @Min(1) long news) throws Exception {
        service.deleteComment(id,news,getUsernameByContext());
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
