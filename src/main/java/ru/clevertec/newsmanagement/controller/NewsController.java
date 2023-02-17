package ru.clevertec.newsmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import ru.clevertec.newsmanagement.exception.CustomException;
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
    @Operation(
            summary = "News",
            description = "API Point made for return news page"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public List<NewsDto> findNews(@Parameter(description = "Page number(def: 0,min: 0)")
                                    @RequestParam(defaultValue = "0") @Min(0) int page,
                                  @Parameter(description = "Page size(def: 10, min: 1)")
                                    @RequestParam(defaultValue = "10") @Min(1) int size,
                                  @Parameter(description = "Filter by field(def: id)")
                                    @RequestParam(defaultValue = "id") @NotBlank String filter,
                                  @Parameter(description = "asc or desc(def: asc)")
                                    @RequestParam(defaultValue = "asc") @NotBlank String direction) throws CustomException {
        return service.findNews(page,size,filter,direction);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "News by ID",
            description = "API Point made for return news by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public NewsDto findNewsWithComment(@PathVariable @Min(1) long id) throws CustomException {
        return service.findNews(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @Operation(
            summary = "Save News",
            description = "API Point made for saving news"
    )
    @ApiResponse(
            responseCode = "201",
            description = "News created"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public NewsDto saveNews(@RequestBody @Valid NewsDto news) throws CustomException {
        return service.saveNews(getUsernameByContext(),news);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @Operation(
            summary = "Update News",
            description = "API Point made for updating news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News updated"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public NewsDto updateNews(@PathVariable @Min(1) long id,
                              @RequestBody @Valid NewsDto news) throws CustomException {
        return service.updateNews(id,getUsernameByContext(),news);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @Operation(
            summary = "Delete News",
            description = "API Point made for deleting news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News deleted"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteNews(@PathVariable @Min(1) long id) throws CustomException {
        service.deleteNews(id,getUsernameByContext());
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
