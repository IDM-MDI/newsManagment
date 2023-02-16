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
import ru.clevertec.newsmanagement.model.NewsDto;
import ru.clevertec.newsmanagement.service.NewsService;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.JwtSecurityHandler.getUsernameByContext;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @GetMapping
    public List<NewsDto> findNews(@RequestParam(defaultValue = "0") @Min(1) int page,
                                 @RequestParam(defaultValue = "10") @Min(1) int size,
                                 @RequestParam(defaultValue = "id") @NotBlank String filter,
                                 @RequestParam(defaultValue = "asc") @NotBlank String direction) {
        return service.findNews(page,size,filter,direction);
    }

    @GetMapping("/{id}")
    public NewsDto findNewsWithComment(@PathVariable @Min(1) long id) throws Exception {
        return service.findNews(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    public NewsDto saveNews(@RequestBody @Valid NewsDto news) throws Exception {
        return service.saveNews(getUsernameByContext(),news);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    public NewsDto updateNews(@PathVariable @Min(1) long id,
                              @RequestBody @Valid NewsDto news) throws Exception {
        return service.updateNews(id,getUsernameByContext(),news);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    public ResponseEntity<String> deleteNews(@PathVariable @Min(1) long id) throws Exception {
        service.deleteNews(id,getUsernameByContext());
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
