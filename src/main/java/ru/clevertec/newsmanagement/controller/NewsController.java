package ru.clevertec.newsmanagement.controller;

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
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @GetMapping
    public List<NewsDto> getNews(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String filter,
                                 @RequestParam(defaultValue = "asc") String direction) {
        return service.getNews(page,size,filter,direction);
    }

    @GetMapping("/{id}")
    public NewsDto getNewsWithComment(@PathVariable long id,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String filter,
                                      @RequestParam(defaultValue = "asc") String direction) {
        return service.getNewsWithComments(id,page,size,filter,direction);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','JOURNALIST')")
    public NewsDto saveNews(@RequestBody NewsDto news) {
        return service.saveNews("username",news);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JOURNALIST')")
    public NewsDto updateNews(@PathVariable long id,
                              @RequestBody NewsDto news) {
        return service.updateNews(id,"username",news);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JOURNALIST')")
    public ResponseEntity<String> deleteNews(@PathVariable long id,
                                             @RequestBody NewsDto news) {
        service.deleteNews(id,"username",news);
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
